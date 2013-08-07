package pl.edu.icm.coansys.nlmextraction;

import java.io.File;
import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.Test;
import pl.edu.icm.coansys.models.DocumentProtos;
import pl.edu.icm.coansys.models.constants.ProtoConstants;
import pl.edu.icm.oozierunner.OozieRunner;

public class TestIT {

    private static final String TITLE_STRING = "<article-title>Eco-friendly methods of protecting flax against weeds</article-title>";

    @Test
    public void testTest1() throws Exception {
        OozieRunner or = new OozieRunner();
        File workflowOutputData = or.run();

        assertTrue(workflowOutputData.exists());
        assertTrue(workflowOutputData.isDirectory());
        assertTrue(workflowOutputData.listFiles().length > 0);

        int records = 0;
        for (File f : FileUtils.listFiles(workflowOutputData, null, true)) {
            if (f.isFile() && f.getName().startsWith("part-")) {
                Configuration conf = new Configuration();
                Path path = new Path("file://" + f.getAbsolutePath());
                SequenceFile.Reader reader = new SequenceFile.Reader(conf, SequenceFile.Reader.file(path));
                Text key = new Text();
                BytesWritable value = new BytesWritable();
                while (reader.next(key, value)) {
                    DocumentProtos.Media media = DocumentProtos.Media.parseFrom(value.copyBytes());
                    assertEquals(media.getMediaType(), ProtoConstants.mediaTypeNlm);
                    String nlmString = media.getContent().toStringUtf8();
                    assertTrue(nlmString.contains(TITLE_STRING));
                    records++;
                }
            }
        }
        assertTrue(records > 0);
    }
}
