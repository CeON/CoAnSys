package pl.edu.icm.coansys.citations.coansys;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
import pl.edu.icm.coansys.models.PICProtos.PicOut;
import pl.edu.icm.coansys.models.PICProtos.Reference;
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
        
    
        assertEquals(actualOutputDocIdPicOuts.size(), expectedOutputDocIdPicOuts.size());
    
        for (Pair<Text, BytesWritable> actualDocIdPicOut : actualOutputDocIdPicOuts) {
            System.out.println("-----------------------BEGIN------------------------------");
            System.out.println("actual key:" + actualDocIdPicOut.getKey().toString());
            System.out.println("actual value: ");
            printPicOut(actualDocIdPicOut.getValue());
            assertTrue(isInExcpectedDocIdPicOuts(expectedOutputDocIdPicOuts, actualDocIdPicOut));
            System.out.println("-----------------------END------------------------------");
            
        }
    

    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private boolean isInExcpectedDocIdPicOuts(List<Pair<Text, BytesWritable>> expectedOutputDocIdPicOuts, Pair<Text, BytesWritable> actualDocIdPicOut) throws Exception {
    
        for (Pair<Text, BytesWritable> expectedDocIdPicOut : expectedOutputDocIdPicOuts) {
            System.out.println("expected key:" + expectedDocIdPicOut.getKey().toString());
            System.out.println("expected value: ");
            printPicOut(expectedDocIdPicOut.getValue());
            
            if (expectedDocIdPicOut.getKey().toString().equals(actualDocIdPicOut.getKey().toString())) {
                System.out.println("...same keys");
                PicOut expectedPicOut = PicOut.parseFrom(expectedDocIdPicOut.getValue().copyBytes());
                PicOut actualPicOut = PicOut.parseFrom(actualDocIdPicOut.getValue().copyBytes());
                assertPicOuts(expectedPicOut, actualPicOut);
                return true;
            }
        }
        
        return false;
    
    }
    
    
    private void assertPicOuts(PicOut expectedPicOut, PicOut actualPicOut) {
        assertEquals(actualPicOut.getDocId(), expectedPicOut.getDocId());
        System.out.println("...same docid");
        assertEquals(actualPicOut.getRefsCount(), expectedPicOut.getRefsCount());
        System.out.println("...same number of references");
        for (Reference actualRef : actualPicOut.getRefsList()) {
            assertTrue(isInExpectedRefs(expectedPicOut.getRefsList(), actualRef));
        }
        
    }


    private boolean isInExpectedRefs(List<Reference> expectedRefs, Reference actualRef) {
        for (Reference expectedRef : expectedRefs) {
            if (refsEqual(expectedRef, actualRef)) {
                return true;
            }
        }
        
        return false;
        
    }

    private boolean refsEqual(Reference ref1, Reference ref2) {
        return ref1.getDocId().equals(ref2.getDocId()) &&
               (ref1.getRefNum() == ref2.getRefNum()) &&
               ref1.getRawText().equals(ref2.getRawText()); 
    }

    
    private void printPicOut(BytesWritable bw) throws Exception {
        PicOut picOut = PicOut.parseFrom(bw.copyBytes());
        System.out.println("docId: " + picOut.getDocId());
        System.out.println("references: ");
        for (Reference ref: picOut.getRefsList()) {
            System.out.println("---- docId: " + ref.getDocId());
            System.out.println("---- refNum: " + ref.getRefNum());
            System.out.println("---- rawText: " + ref.getRawText());
        }
    }
    
    /*
actual key:50|od========18::f67eb9ee51e9e52fbb14b86acc9261ba
actual value: 
docId: 50|od========18::f67eb9ee51e9e52fbb14b86acc9261ba
references: 
---- docId: 50|od========18::3a38fd978eeb55784dbd4d90c88d28da
---- refNum: 7
---- rawText: [7] G. Ghoshal, V. Zlati c, G. Caldarelli, and M. E. J. Newman. Random hypergraphs and their applications. Phys. Rev. E, 79(6):066118, 2009.
---- docId: 50|od========18::d436ddc6c0013761d58ffab1462ad3f4
---- refNum: 16
---- rawText: [16] M. E. J. Newman. Properties of highly clustered networks. Phys. Rev. E, 68(2):026121, 2003.
---- docId: 50|od=======279::c849c9f653f6cae13825e5349495a8b0
---- refNum: 33
---- rawText: [33] S. Funk and V. A. A. Jansen. Interacting epidemics on overlay networks. Phys. Rev. E, 81(3):036118, 2010.

expected key:50|od========18::f67eb9ee51e9e52fbb14b86acc9261ba
expected value: 
docId: 50|od========18::f67eb9ee51e9e52fbb14b86acc9261ba
references: 
---- docId: 50|od========18::d436ddc6c0013761d58ffab1462ad3f4
---- refNum: 16
---- rawText: [16] M. E. J. Newman. Properties of highly clustered networks. Phys. Rev. E, 68(2):026121, 2003.
---- docId: 50|od========18::3a38fd978eeb55784dbd4d90c88d28da
---- refNum: 7
---- rawText: [7] G. Ghoshal, V. Zlati c, G. Caldarelli, and M. E. J. Newman. Random hypergraphs and their applications. Phys. Rev. E, 79(6):066118, 2009.
---- docId: 50|od=======279::c849c9f653f6cae13825e5349495a8b0
---- refNum: 33
---- rawText: [33] S. Funk and V. A. A. Jansen. Interacting epidemics on overlay networks. Phys. Rev. E, 81(3):036118, 2010.
     
      
     * */
    
}
