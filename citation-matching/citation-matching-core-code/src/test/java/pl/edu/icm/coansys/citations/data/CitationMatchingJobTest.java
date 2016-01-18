package pl.edu.icm.coansys.citations.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.hadoop.io.Text;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.collect.Lists;
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
        
        assertMatchedCitations(outputDirPath, 
                "src/test/resources/heuristic/heuristic-out-1",
                "src/test/resources/heuristic/heuristic-out-2",
                "src/test/resources/heuristic/heuristic-out-3",
                "src/test/resources/heuristic/heuristic-out-4");
        
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
    
    
    private void assertMatchedCitations(String outputDirPath, String ... expectedMatchedCitationPartsDirPath) throws IOException {
        
        List<Pair<Text, Text>> actualMatchedCitations = LocalSequenceFileUtils.readSequenceFile(new File(outputDirPath), Text.class, Text.class);
        
        List<Pair<Text, Text>> expectedMatchedCitations = Lists.newArrayList();
        for (String expectedMatchedCitationPartDirPath : expectedMatchedCitationPartsDirPath) {
            expectedMatchedCitations.addAll(LocalSequenceFileUtils.readSequenceFile(new File(expectedMatchedCitationPartDirPath), Text.class, Text.class));
        }
        
        assertEquals(expectedMatchedCitations.size(), actualMatchedCitations.size());
        
        for (Pair<Text, Text> actualCitationDocIdPair : actualMatchedCitations) {
            assertTrue(isInMatchedCitations(expectedMatchedCitations, actualCitationDocIdPair));
        }
    }
    
    
    private boolean isInMatchedCitations(List<Pair<Text, Text>> citations, Pair<Text, Text> citationDocIdPair) {
        
        for (Pair<Text, Text> citDocIdPair : citations) { 
            if (citDocIdPair.getKey().equals(citationDocIdPair.getKey()) && (citDocIdPair.getValue().equals(citationDocIdPair.getValue()))) {
                return true;
            }
        }
        
        return false;
        
    }
}
