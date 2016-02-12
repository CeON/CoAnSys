package pl.edu.icm.coansys.citations.coansys;


import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.io.Files;

import pl.edu.icm.coansys.commons.hadoop.LocalSequenceFileUtils;
import pl.edu.icm.sparkutils.test.SparkJob;
import pl.edu.icm.sparkutils.test.SparkJobBuilder;
import pl.edu.icm.sparkutils.test.SparkJobExecutor;

/**
 * @author Łukasz Dumiszewski
 */
public class CoansysCitationMatchingJobTest {

    private SparkJobExecutor executor = new SparkJobExecutor();
    
    private File workingDir;
    
    private String outputDirPath;
    
    
    @BeforeMethod
    public void before() {
        
        workingDir = Files.createTempDir();
        outputDirPath = workingDir + "/coansys_citation_matching/output";
    }
    
    
    @AfterMethod
    public void after() throws IOException {
        
        FileUtils.deleteDirectory(workingDir);
        
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void citationMatching() throws Exception {
        
        
        // given
        
        String inputCitationPath = "src/test/resources/cm-input/citDocWrappers";
        String inputDocumentPath = "src/test/resources/cm-input/docWrappers";
        
        SparkJob sparkJob = SparkJobBuilder
                .create()
                
                .setAppName("Spark Citation Matching")

                .setMainClass(CoansysCitationMatchingJob.class)
                .addArg("-inputDocumentPath", inputDocumentPath)
                .addArg("-inputCitationPath", inputCitationPath)
                .addArg("-outputDirPath", outputDirPath)
                .addArg("-numberOfPartitions", "1")
                
                .build();
        
        
        
        // execute
        
        executor.execute(sparkJob);
        
        
        // assert
        
        List<Pair<Text, BytesWritable>> actualOutputDocIdPicOuts = LocalSequenceFileUtils.readSequenceFile(new File(outputDirPath), Text.class, BytesWritable.class);
        
        List<Pair<Text, BytesWritable>> expectedOutputDocIdPicOuts = LocalSequenceFileUtils.readSequenceFile(new File("src/test/resources/cm-output"), Text.class, BytesWritable.class);
        
    
        PicOutAssert.assertDocIdPicOutsEquals(expectedOutputDocIdPicOuts, actualOutputDocIdPicOuts);
    

    }

 
}
