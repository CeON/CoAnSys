package pl.edu.icm.coansys.citations.datacreation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.hadoop.io.Text;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.io.Files;

import pl.edu.icm.coansys.citations.data.TextWithBytesWritable;
import pl.edu.icm.coansys.commons.hadoop.LocalSequenceFileUtils;
import pl.edu.icm.sparkutils.test.SparkJob;
import pl.edu.icm.sparkutils.test.SparkJobBuilder;
import pl.edu.icm.sparkutils.test.SparkJobExecutor;

/**
 * @author 
 */
public class DocDataCreationJobTest {


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
    public void docCreationJob() throws IOException {
        
        
        // given
        
        SparkJob sparkJob = createDocDataCreationJob();
        
        
        // execute
        
        executor.execute(sparkJob);
        
        
        
        // assert
        
        //assertMatchedCitations(outputDirPath, "src/test/resources/matched_output");
        
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private SparkJob createDocDataCreationJob() {
        
        SparkJob sparkJob = SparkJobBuilder
                                           .create()
                                           
                                           .setAppName("DocDataCreationJob")
        
                                           .setMainClass(DocDataCreationJob.class)
                                           
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
