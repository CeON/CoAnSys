
package pl.edu.icm.coansys.citations.data;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.hadoop.io.Text;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Files;

import pl.edu.icm.coansys.commons.hadoop.LocalSequenceFileUtils;
import pl.edu.icm.sparkutils.test.SparkJob;
import pl.edu.icm.sparkutils.test.SparkJobBuilder;
import pl.edu.icm.sparkutils.test.SparkJobExecutor;


/**
 * @author Łukasz Dumiszewski
 */
public class CitationMatchingJobTest {

    private Logger log = LoggerFactory.getLogger(CitationMatchingJobTest.class);
    	
    private SparkJobExecutor executor = new SparkJobExecutor();
    
    private File workingDir;
        
        
    @Before
    public void before() {
        
        workingDir = Files.createTempDir();
        
    }
    
    
    @After
    public void after() throws IOException {
        
        FileUtils.deleteDirectory(workingDir);
        
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void citationMatchingJob() throws IOException {
        
        
        // given
        
        String citationPath = "src/test/resources/heuristic/citations";
        
        String documentPath = "src/test/resources/heuristic/documents";
        
        String citationHashGeneratorClass = "pl.edu.icm.coansys.citations.hashers.CitationNameYearPagesHashGenerator";
        
        String documentHashGeneratorClass = "pl.edu.icm.coansys.citations.hashers.DocumentNameYearPagesHashGenerator";
        
        String unmatchedCitationDirPath = workingDir.getAbsolutePath() + "/unmatched";
        
        String outputDirPath = workingDir.getAbsolutePath() + "/output";
 
        SparkJob sparkJob = SparkJobBuilder
                                           .create()
                                           
                                           .setAppName("CitationMatchingJob")
        
                                           .setMainClass(CitationMatchingJob.class)
                                           .addArg("-citationPath", citationPath)
                                           .addArg("-documentPath", documentPath)
                                           .addArg("-citationHashGeneratorClass", citationHashGeneratorClass)
                                           .addArg("-documentHashGeneratorClass", documentHashGeneratorClass)
                                           .addArg("-unmatchedCitationDirPath", unmatchedCitationDirPath)
                                           .addArg("-outputDirPath", outputDirPath)
                                           
                                           .build();
        
        
        // execute
        
        executor.execute(sparkJob);
        
        
        
        // assert
        
        File outputFile = new File(outputDirPath);
        List<Pair<Text, Text>> matchedCitations = LocalSequenceFileUtils.readSequenceFile(outputFile, Text.class, Text.class);
        System.out.println("------======== Result =========--------");
        matchedCitations.forEach(System.out::println);
        
        
        
    }

    


    
    
    
}
