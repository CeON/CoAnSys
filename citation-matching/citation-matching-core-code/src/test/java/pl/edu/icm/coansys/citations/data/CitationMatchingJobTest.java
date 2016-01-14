
package pl.edu.icm.coansys.citations.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
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
    public void citationMatchingJob_1() throws IOException {
        
        
        // given
        
        String citationPath = "src/test/resources/heuristic/citations";
        
        String documentPath = "src/test/resources/heuristic/documents";
        
        String citationHashGeneratorClass = "pl.edu.icm.coansys.citations.hashers.CitationNameYearPagesHashGenerator";
        
        String documentHashGeneratorClass = "pl.edu.icm.coansys.citations.hashers.DocumentNameYearPagesHashGenerator";
        
        String unmatchedCitationDirPath = workingDir.getAbsolutePath() + "/unmatched";
        
        String outputDirPath = workingDir.getAbsolutePath() + "/output";
 
        SparkJob sparkJob = createCitationMatchingJob(citationPath, documentPath, citationHashGeneratorClass, documentHashGeneratorClass, unmatchedCitationDirPath, outputDirPath);
        
        
        // execute
        
        executor.execute(sparkJob);
        
        
        
        // assert
        
        assertMatchedCitations(outputDirPath, "src/test/resources/heuristic/heuristic-out-1");
        
        assertUnmatchedCitations(unmatchedCitationDirPath, "src/test/resources/heuristic/unmatched-1");
        
    }

    
    @Test
    public void citationMatchingJob_2() throws IOException {
        
        
        // given
        
        String citationPath = "src/test/resources/heuristic/unmatched-1";
        
        String documentPath = "src/test/resources/heuristic/documents";
        
        String citationHashGeneratorClass = "pl.edu.icm.coansys.citations.hashers.CitationNameYearPagesHashGenerator";
        
        String documentHashGeneratorClass = "pl.edu.icm.coansys.citations.hashers.DocumentNameYearNumNumHashGenerator";
        
        String unmatchedCitationDirPath = workingDir.getAbsolutePath() + "/unmatched";
        
        String outputDirPath = workingDir.getAbsolutePath() + "/output";
 
        SparkJob sparkJob = createCitationMatchingJob(citationPath, documentPath, citationHashGeneratorClass, documentHashGeneratorClass, unmatchedCitationDirPath, outputDirPath);
        
        
        // execute
        
        executor.execute(sparkJob);
        
        
        
        // assert
        
        assertMatchedCitations(outputDirPath, "src/test/resources/heuristic/heuristic-out-2");
        
        assertUnmatchedCitations(unmatchedCitationDirPath, "src/test/resources/heuristic/unmatched-2");
        
    }
    
    
    @Test
    public void citationMatchingJob_3() throws IOException {
        
        
        // given
        
        String citationPath = "src/test/resources/heuristic/unmatched-2";
        
        String documentPath = "src/test/resources/heuristic/documents";
        
        String citationHashGeneratorClass = "pl.edu.icm.coansys.citations.hashers.CitationNameYearHashGenerator";
        
        String documentHashGeneratorClass = "pl.edu.icm.coansys.citations.hashers.DocumentNameYearStrictHashGenerator";
        
        String unmatchedCitationDirPath = workingDir.getAbsolutePath() + "/unmatched";
        
        String outputDirPath = workingDir.getAbsolutePath() + "/output";
 
        SparkJob sparkJob = createCitationMatchingJob(citationPath, documentPath, citationHashGeneratorClass, documentHashGeneratorClass, unmatchedCitationDirPath, outputDirPath);
        
        
        // execute
        
        executor.execute(sparkJob);
        
        
        
        // assert
        
        assertMatchedCitations(outputDirPath, "src/test/resources/heuristic/heuristic-out-3");
        
        assertUnmatchedCitations(unmatchedCitationDirPath, "src/test/resources/heuristic/unmatched-3");
        
    }
    
    
    @Test
    public void citationMatchingJob_4() throws IOException {
        
        
        // given
        
        String citationPath = "src/test/resources/heuristic/unmatched-3";
        
        String documentPath = "src/test/resources/heuristic/documents";
        
        String citationHashGeneratorClass = "pl.edu.icm.coansys.citations.hashers.CitationNameYearHashGenerator";
        
        String documentHashGeneratorClass = "pl.edu.icm.coansys.citations.hashers.DocumentNameYearHashGenerator";
        
        String unmatchedCitationDirPath = "";
        
        String outputDirPath = workingDir.getAbsolutePath() + "/output";
 
        SparkJob sparkJob = createCitationMatchingJob(citationPath, documentPath, citationHashGeneratorClass, documentHashGeneratorClass, unmatchedCitationDirPath, outputDirPath);
        
        
        // execute
        
        executor.execute(sparkJob);
        
        
        
        // assert
        
        assertMatchedCitations(outputDirPath, "src/test/resources/heuristic/heuristic-out-4");
        
        
    }



    
    
    //------------------------ PRIVATE --------------------------

    
    private SparkJob createCitationMatchingJob(String citationPath, String documentPath, String citationHashGeneratorClass, String documentHashGeneratorClass, String unmatchedCitationDirPath, String outputDirPath) {
        
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
        return sparkJob;
    }

    
    private void assertUnmatchedCitations(String unmatchedCitationDirPath, String expectedUnmatchedCitationDirPath) throws IOException {
        
        List<Pair<Text, BytesWritable>> actualUnmatchedCitations = LocalSequenceFileUtils.readSequenceFile(new File(unmatchedCitationDirPath), Text.class, BytesWritable.class);
        List<Pair<Text, BytesWritable>> expectedUnmatchedCitations = LocalSequenceFileUtils.readSequenceFile(new File(expectedUnmatchedCitationDirPath), Text.class, BytesWritable.class);
        
        assertEquals(expectedUnmatchedCitations.size(), actualUnmatchedCitations.size());
        
        for (Pair<Text, BytesWritable> actualUnmatchedCitation : actualUnmatchedCitations) {
            assertTrue(isInUnmatchedCitations(expectedUnmatchedCitations, actualUnmatchedCitation));
        }
        
    }
    
    
    private void assertMatchedCitations(String outputDirPath, String expectedMatchedCitationDirPath) throws IOException {
        
        List<Pair<Text, Text>> actualMatchedCitations = LocalSequenceFileUtils.readSequenceFile(new File(outputDirPath), Text.class, Text.class);
        List<Pair<Text, Text>> expectedMatchedCitations = LocalSequenceFileUtils.readSequenceFile(new File(expectedMatchedCitationDirPath), Text.class, Text.class);
        
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

    private boolean isInUnmatchedCitations(List<Pair<Text, BytesWritable>> citations, Pair<Text, BytesWritable> citation) {
        
        for (Pair<Text, BytesWritable> cit : citations) {
            if (cit.getKey().equals(citation.getKey())) {
                return Arrays.equals(cit.getValue().getBytes(), citation.getValue().getBytes());
            }
                
        }
        
        return false;
        
    }
    


    
    
    
}
