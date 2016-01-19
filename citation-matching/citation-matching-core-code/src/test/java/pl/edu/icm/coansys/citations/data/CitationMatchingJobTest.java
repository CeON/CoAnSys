package pl.edu.icm.coansys.citations.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.io.Files;

import pl.edu.icm.coansys.commons.hadoop.LocalSequenceFileUtils;
import pl.edu.icm.sparkutils.test.SparkJob;
import pl.edu.icm.sparkutils.test.SparkJobBuilder;
import pl.edu.icm.sparkutils.test.SparkJobExecutor;

/**
 * @author madryk
 */
public class CitationMatchingJobTest {


    private SparkJobExecutor executor = new SparkJobExecutor();
    
    private File workingDir;
        
        
    @BeforeMethod
    public void before() {
        
        workingDir = Files.createTempDir();
        
    }
    
    
    @AfterMethod
    public void after() throws IOException {
        
        FileUtils.deleteDirectory(workingDir);
        
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void citationMatchingJob() throws IOException {
        
        
        // given
        
        String citationPath = "src/test/resources/heuristic/citations";
        
        String documentPath = "src/test/resources/heuristic/documents";
        
        String outputDirPath = workingDir.getAbsolutePath() + "/output";
 
        SparkJob sparkJob = createCitationMatchingJob(citationPath, documentPath, outputDirPath);
        
        
        // execute
        
        executor.execute(sparkJob);
        
        
        
        // assert
        
        assertMatchedCitations(outputDirPath, "src/test/resources/heuristic_with_docs");
        
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private SparkJob createCitationMatchingJob(String citationPath, String documentPath, String outputDirPath) {
        
        SparkJob sparkJob = SparkJobBuilder
                                           .create()
                                           
                                           .setAppName("CitationMatchingJob")
        
                                           .setMainClass(CitationMatchingJob.class)
                                           .addArg("-citationPath", citationPath)
                                           .addArg("-documentPath", documentPath)
                                           
                                           .addArg("-hashGeneratorClasses", "pl.edu.icm.coansys.citations.hashers.CitationNameYearPagesHashGenerator:pl.edu.icm.coansys.citations.hashers.DocumentNameYearPagesHashGenerator")
                                           
                                           .addArg("-hashGeneratorClasses", "pl.edu.icm.coansys.citations.hashers.CitationNameYearPagesHashGenerator:pl.edu.icm.coansys.citations.hashers.DocumentNameYearNumNumHashGenerator")
                                           
                                           .addArg("-hashGeneratorClasses", "pl.edu.icm.coansys.citations.hashers.CitationNameYearHashGenerator:pl.edu.icm.coansys.citations.hashers.DocumentNameYearStrictHashGenerator")
                                           
                                           .addArg("-hashGeneratorClasses", "pl.edu.icm.coansys.citations.hashers.CitationNameYearHashGenerator:pl.edu.icm.coansys.citations.hashers.DocumentNameYearHashGenerator")
                                           
                                           .addArg("-outputDirPath", outputDirPath)
                                           
                                           .build();
        return sparkJob;
    }
    
    
    private void assertMatchedCitations(String outputDirPath, String expectedMatchedCitationDirPath) throws IOException {
        
        List<Pair<MarkedText, TextWithBytesWritable>> actualMatchedCitations = LocalSequenceFileUtils.readSequenceFile(new File(outputDirPath), MarkedText.class, TextWithBytesWritable.class);
        
        List<Pair<MarkedText, TextWithBytesWritable>> expectedMatchedCitations = LocalSequenceFileUtils.readSequenceFile(new File(expectedMatchedCitationDirPath), MarkedText.class, TextWithBytesWritable.class);
        
        assertEquals(expectedMatchedCitations.size(), actualMatchedCitations.size());
        
        for (Pair<MarkedText, TextWithBytesWritable> actualCitationDocIdPair : actualMatchedCitations) {
            assertTrue(isInMatchedCitations(expectedMatchedCitations, actualCitationDocIdPair));
        }
    }
    
    
    private boolean isInMatchedCitations(List<Pair<MarkedText, TextWithBytesWritable>> citations, Pair<MarkedText, TextWithBytesWritable> citationIdDocPair) {
        
        for (Pair<MarkedText, TextWithBytesWritable> citIdDocPair : citations) { 
            if (citIdDocPair.getKey().equals(citationIdDocPair.getKey()) && (citIdDocPair.getValue().text().equals(citationIdDocPair.getValue().text()))) {
                return Arrays.equals(citIdDocPair.getValue().bytes().copyBytes(), citationIdDocPair.getValue().bytes().copyBytes());
            }
        }
        
        return false;
        
    }
}
