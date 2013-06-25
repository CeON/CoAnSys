package pl.edu.icm.coansys.disambiguation.work;

import java.io.File;
import java.net.URL;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.ToolRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pl.edu.icm.coansys.disambiguation.work.tool.Bw2ProtoFileUtils;
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentWrapper;

/** This test assumes there is /generated/ambiguous-publications.seq file on the classpath. 
 *  The file will be generated during maven test phase
 * 
 * */
public class DuplicateWorkDetectorTest {
    
    private URL inputFileUrl = this.getClass().getResource("/generated/ambiguous-publications.seq");
    private URL baseOutputUrl = this.getClass().getResource("/");
    private String outputDir = baseOutputUrl.getPath() + "/testOut";
    
    
    @Before
    public void before() throws Exception{
        FileUtils.deleteDirectory(new File(outputDir));
        ToolRunner.run(new Configuration(), new DuplicateWorkDetector(), new String[]{inputFileUrl.getPath(), outputDir});
    }
    
    @After
    public void after() throws Exception{
        FileUtils.deleteDirectory(new File(outputDir));
    }
    
    @Test
    public void test() throws Exception {
        List<DocumentWrapper> docWrappers = Bw2ProtoFileUtils.readDocWrappers(outputDir+"/part-r-00000"); 
        Assert.assertEquals(4, docWrappers.size());
       
    }

    
}
