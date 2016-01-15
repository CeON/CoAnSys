package pl.edu.icm.coansys.citations;

import static org.testng.Assert.assertEqualsNoOrder;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.io.Files;

import pl.edu.icm.coansys.citations.data.MarkedText;
import pl.edu.icm.coansys.citations.data.TextWithBytesWritable;
import pl.edu.icm.coansys.commons.hadoop.LocalSequenceFileUtils;
import pl.edu.icm.sparkutils.test.SparkJob;
import pl.edu.icm.sparkutils.test.SparkJobBuilder;
import pl.edu.icm.sparkutils.test.SparkJobExecutor;

/**
 * 
 * @author madryk
 *
 */
public class DocAttacherJobTest {

    private SparkJobExecutor executor = new SparkJobExecutor();
    
    private File workingDir;
    
    private String outputDirPath;
    
    
    @BeforeMethod
    public void setup() {
        workingDir = Files.createTempDir();
        outputDirPath = workingDir + "/output";
    }
    
    @AfterMethod
    public void cleanup() throws IOException {
    	 FileUtils.deleteDirectory(workingDir);
    }
    
    
    @Test
    public void docAttacherJob() throws IOException {
        
        
        // given
        
        String inputMatchedFile = "src/test/resources/doc_attacher/input/matched_citations_*";
        String inputDocumentsFile = "src/test/resources/doc_attacher/input/documents";
        
        
        // execute
        
        executor.execute(buildDocumentAttacherJob(inputMatchedFile, inputDocumentsFile, outputDirPath));
        
        
        
        // assert
        
        String expectedOutputFile = "src/test/resources/doc_attacher/expected_output/matched_with_docs";
        
        List<Pair<MarkedText, TextWithBytesWritable>> output = LocalSequenceFileUtils.readSequenceFile(new File(outputDirPath), MarkedText.class, TextWithBytesWritable.class);
        List<Pair<MarkedText, TextWithBytesWritable>> expectedOutput = LocalSequenceFileUtils.readSequenceFile(new File(expectedOutputFile), MarkedText.class, TextWithBytesWritable.class);
        
        assertEqualsNoOrder(output.toArray(), expectedOutput.toArray());
    }
    
    @Test
    public void docAttacherJob_NO_DOCUMENTS() throws IOException {
        
      // given
        
      String inputMatchedFile = "src/test/resources/doc_attacher/input/matched_citations_*";
      String inputDocumentsFile = "src/test/resources/doc_attacher/input/documents_empty";
      
      
      // execute
      
      executor.execute(buildDocumentAttacherJob(inputMatchedFile, inputDocumentsFile, outputDirPath));
      
      
      
      // assert
      
      List<Pair<MarkedText, TextWithBytesWritable>> output = LocalSequenceFileUtils.readSequenceFile(new File(outputDirPath), MarkedText.class, TextWithBytesWritable.class);
      
      assertTrue(output.isEmpty());
      
    }
    
    
    private SparkJob buildDocumentAttacherJob(String inputMatchedPath, String inputDocsPath, String outputDirPath) {
        SparkJob sparkJob = SparkJobBuilder
                .create()
                
                .setAppName("Document Attacher")

                .setMainClass(DocAttacherJob.class)
                .addArg("-inputMatchedPath", inputMatchedPath)
                .addArg("-inputDocsPath", inputDocsPath)
                .addArg("-outputPath", outputDirPath)
                
                .build();
        
        return sparkJob;
    }
    
    
}
