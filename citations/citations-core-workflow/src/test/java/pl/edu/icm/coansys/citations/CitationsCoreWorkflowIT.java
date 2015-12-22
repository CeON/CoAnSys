package pl.edu.icm.coansys.citations;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.fail;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.testng.annotations.Test;

import pl.edu.icm.coansys.citations.data.MatchableEntity;
import pl.edu.icm.oozierunner.OozieRunner;


/**
 * 
 * @author madryk
 *
 */
public class CitationsCoreWorkflowIT {

    private final static double DOUBLE_EPSILON = 1e-5;
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void testCoreWorkflow() throws IOException {
        
        // given 
        OozieRunner oozieRunner = new OozieRunner();
        
        
        // execute
        
        File workflowOutputData = oozieRunner.run();
        
        
        // assert
        
        List<Pair<MatchableEntity, String>> actualMatchedCitations = MatchableEntitySequenceFileUtils.readMatchedCitations(workflowOutputData);
        
        List<Pair<MatchableEntity, String>> expectedMatchedCitations = MatchableEntitySequenceFileUtils.readMatchedCitations(new File("src/test/resources/expectedOutput/matchedCitations"));
        
        
        for (Pair<MatchableEntity, String> expectedMatchedCitation : expectedMatchedCitations) {
            String expectedSourceId = expectedMatchedCitation.getLeft().id();
            String expectedDestId = expectedMatchedCitation.getRight().substring(expectedMatchedCitation.getRight().indexOf(":") + 1);
            
            Pair<MatchableEntity, String> actualMatchedCitation = findCitation(actualMatchedCitations, expectedSourceId, expectedDestId);
            
            
            if (actualMatchedCitation == null) {
                fail("Expected matched citation not found (" + expectedSourceId + ", " + expectedDestId + ")");
            }
            
            assertMatchedCitationEquals(expectedMatchedCitation, actualMatchedCitation);
        }
        
        assertEquals(expectedMatchedCitations.size(), actualMatchedCitations.size());
        
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private void assertMatchedCitationEquals(Pair<MatchableEntity, String> expectedMatchedCitation, Pair<MatchableEntity, String> actualMatchedCitation) {
        
        double expectedSimilarity = Double.parseDouble(expectedMatchedCitation.getRight().split(":")[0]);
        byte[] expectedCitationBytes = expectedMatchedCitation.getLeft().data().toByteArray();
        
        double actualSimilarity = Double.parseDouble(actualMatchedCitation.getRight().split(":")[0]);
        byte[] actualCitationBytes = actualMatchedCitation.getLeft().data().toByteArray();
        
        assertEquals(expectedCitationBytes, actualCitationBytes);
        assertEquals(expectedSimilarity, actualSimilarity, DOUBLE_EPSILON);
    }
    
    
    private Pair<MatchableEntity, String> findCitation(List<Pair<MatchableEntity, String>> citations, String sourceId, String destId) {
        
        for (Pair<MatchableEntity, String> citation : citations) {
            
            String actualSourceId = citation.getLeft().id();
            String actualDestId = citation.getRight().substring(citation.getRight().indexOf(":") + 1);
            
            if (StringUtils.equals(actualSourceId, sourceId) && StringUtils.equals(actualDestId, destId)) {
                return citation;
            }
            
        }
        return null;
    }
}
