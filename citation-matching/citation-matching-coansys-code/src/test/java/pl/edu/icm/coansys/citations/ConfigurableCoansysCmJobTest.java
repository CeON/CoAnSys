package pl.edu.icm.coansys.citations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.io.Files;
import com.google.protobuf.InvalidProtocolBufferException;

import pl.edu.icm.coansys.citations.data.TextWithBytesWritable;
import pl.edu.icm.coansys.commons.hadoop.LocalSequenceFileUtils;
import pl.edu.icm.coansys.models.PICProtos.PicOut;
import pl.edu.icm.sparkutils.test.SparkJob;
import pl.edu.icm.sparkutils.test.SparkJobBuilder;
import pl.edu.icm.sparkutils.test.SparkJobExecutor;

/**
* @author Łukasz Dumiszewski
*/

public class ConfigurableCoansysCmJobTest {
    
    private SparkJobExecutor executor = new SparkJobExecutor();
    
    private File outputDir;
    
    private String outputDirPath;
        
    private final String inputCitationPath = "src/test/resources/cm-input/citDocWrappers";
    
    private final String inputDocumentPath = "src/test/resources/cm-input/docWrappers";
    
    
        
    @BeforeMethod
    public void before() {
        
        outputDir = Files.createTempDir();
        
        outputDirPath = outputDir.getAbsolutePath();
    
    }
    
    
    @AfterMethod
    public void after() throws IOException {
        
        FileUtils.deleteDirectory(outputDir);
        
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void configurableCoansysCmJob() throws IOException, com.google.protobuf.InvalidProtocolBufferException {
        
        /*
         *     private InputCitationReader<INPUT_CIT_KEY, INPUT_CIT_VALUE> inputCitationReader;
    
    private InputCitationConverter<INPUT_CIT_KEY, INPUT_CIT_VALUE> inputCitationConverter;
    
    
    private InputDocumentReader<INPUT_DOC_KEY, INPUT_DOC_VALUE> inputDocumentReader;
    
    private InputDocumentConverter<INPUT_DOC_KEY, INPUT_DOC_VALUE> inputDocumentConverter;
    
    
    private OutputConverter<OUTPUT_MATCHED_KEY, OUTPUT_MACHED_VALUE> outputConverter;
    
    private OutputWriter<OUTPUT_MATCHED_KEY, OUTPUT_MACHED_VALUE> outputWriter;
         */
        
        // given
        
        SparkJob sparkJob = createCitationMatchingJob();
        
        String outputPath = "src/test/resources/cm-output";
        List<Pair<Text, BytesWritable>> actualMatchedCitations = LocalSequenceFileUtils.readSequenceFile(new File(outputPath), Text.class, BytesWritable.class);
        
        List<Pair<String, PicOut>> actualMatchedCitPicOuts = actualMatchedCitations.stream().map(p->Pair.of(p.getKey().toString(), parse(p.getValue().copyBytes()))).collect(Collectors.toList());
        
        actualMatchedCitPicOuts.forEach(System.out::println);
        // execute
        
        // executor.execute(sparkJob);
        
        
        
        // assert
        
        
        // assertMatchedCitations(outputDirPath, "src/test/resources/matched_output");
        
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private PicOut parse(byte[] picoutBytes) {
        PicOut picOut = null;
        try {
            
            picOut = PicOut.parseFrom(picoutBytes);
        } catch (InvalidProtocolBufferException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return picOut;
        
    }
    
    private SparkJob createCitationMatchingJob() {
        
        SparkJob sparkJob = SparkJobBuilder
                                           .create()
                                           
                                           .setAppName("CoansysCitationMatchingJob")
        
                                           .setMainClass(CitationMatchingJob.class)
                                           
                                           .addArg("-citationPath", inputCitationPath)
                                           
                                           .addArg("-documentPath", inputDocumentPath)
                                           
                                           .addArg("-hashGeneratorClasses", "pl.edu.icm.coansys.citations.hashers.CitationNameYearPagesHashGenerator:pl.edu.icm.coansys.citations.hashers.DocumentNameYearPagesHashGenerator")
                                           
                                           .addArg("-hashGeneratorClasses", "pl.edu.icm.coansys.citations.hashers.CitationNameYearPagesHashGenerator:pl.edu.icm.coansys.citations.hashers.DocumentNameYearNumNumHashGenerator")
                                           
                                           .addArg("-hashGeneratorClasses", "pl.edu.icm.coansys.citations.hashers.CitationNameYearHashGenerator:pl.edu.icm.coansys.citations.hashers.DocumentNameYearStrictHashGenerator")
                                           
                                           .addArg("-hashGeneratorClasses", "pl.edu.icm.coansys.citations.hashers.CitationNameYearHashGenerator:pl.edu.icm.coansys.citations.hashers.DocumentNameYearHashGenerator")
                                           
                                           .addArg("-outputDirPath", outputDirPath)
                                           
                                           .addArg("-numberOfPartitions", "5")
                                           
                                           .build();
        return sparkJob;
    }
    
    
    private void assertMatchedCitations(String outputDirPath, String expectedMatchedCitationDirPath) throws IOException {
        
        List<Pair<TextWithBytesWritable, Text>> actualMatchedCitations = LocalSequenceFileUtils.readSequenceFile(new File(outputDirPath), TextWithBytesWritable.class, Text.class);
        
        List<Pair<TextWithBytesWritable, Text>> expectedMatchedCitations = LocalSequenceFileUtils.readSequenceFile(new File(expectedMatchedCitationDirPath), TextWithBytesWritable.class, Text.class);
        
        assertEquals(expectedMatchedCitations.size(), actualMatchedCitations.size());
        
        for (Pair<TextWithBytesWritable, Text> actualCitationDocIdPair : actualMatchedCitations) {
            assertTrue(isInMatchedCitations(expectedMatchedCitations, actualCitationDocIdPair));
        }
        
    }
    
    
    private boolean isInMatchedCitations(List<Pair<TextWithBytesWritable, Text>> citations, Pair<TextWithBytesWritable, Text> citationIdDocPair) {
        
        for (Pair<TextWithBytesWritable, Text> citIdDocPair : citations) {
            if (citIdDocPair.getKey().text().equals(citationIdDocPair.getKey().text())) {
                return Arrays.equals(citIdDocPair.getKey().bytes().copyBytes(), citationIdDocPair.getKey().bytes().copyBytes())
                        && citIdDocPair.getValue().equals(citationIdDocPair.getValue());
            }
        }
        
        return false;
        
    }
    
}
