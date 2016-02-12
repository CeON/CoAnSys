package pl.edu.icm.coansys.citations.coansys;

import java.io.File;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.testng.annotations.Test;

import pl.edu.icm.coansys.commons.hadoop.LocalSequenceFileUtils;
import pl.edu.icm.oozierunner.OozieRunner;

/**
 * @author madryk
 */
public class CoansysCitationMatchingWorkflowIT {

    
    //------------------------ TESTS --------------------------
    
    @Test
    public void testCoansysCitationMatchingWorkflow() throws Exception {
        
        // given
        
        OozieRunner oozieRunner = new OozieRunner();
        
        
        // execute
        
        File workflowOutputData = oozieRunner.run();
        
        
        // assert
        
        List<Pair<Text, BytesWritable>> actualOutputDocIdPicOuts = LocalSequenceFileUtils.readSequenceFile(workflowOutputData, Text.class, BytesWritable.class);
        
        List<Pair<Text, BytesWritable>> expectedOutputDocIdPicOuts = LocalSequenceFileUtils.readSequenceFile(new File("src/test/resources/expectedOutput"), Text.class, BytesWritable.class);
        
        PicOutAssert.assertDocIdPicOutsEquals(expectedOutputDocIdPicOuts, actualOutputDocIdPicOuts);
        
    }
}
