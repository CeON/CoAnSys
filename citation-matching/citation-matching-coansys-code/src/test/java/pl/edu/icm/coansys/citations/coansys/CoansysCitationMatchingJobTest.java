package pl.edu.icm.coansys.citations.coansys;


import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.io.Files;

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
    public void citationMatching() throws IOException {
        
        
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
                .addArg("-numberOfPartitions", "5")
                
                .build();
        
        
        
        // execute
        
        executor.execute(sparkJob);
        
        
        // assert
        
        
    }
    
    
    
    
}
