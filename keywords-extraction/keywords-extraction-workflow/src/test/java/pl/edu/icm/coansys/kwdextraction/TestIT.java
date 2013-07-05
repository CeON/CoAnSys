package pl.edu.icm.coansys.kwdextraction;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import static org.testng.Assert.*;
import org.testng.annotations.Test;
import pl.edu.icm.coansys.models.KeywordExtractionProtos;
import pl.edu.icm.oozierunner.OozieRunner;

public class TestIT {

    @Test
    public void testTest1() throws Exception {
        System.out.println("Test1");
        OozieRunner or = new OozieRunner();
        File workflowOutputData = or.run();

        assertTrue(workflowOutputData.exists());
        assertTrue(workflowOutputData.isDirectory());
        assertTrue(workflowOutputData.listFiles().length > 0);

        List<String> expected = Arrays.asList(
            "inducing cellular organic functional disturbances",
            "normal red blood cell formation",
            "inadequate bone marrow responses compared",
            "erythroid colony forming units",
            "decreased total iron binding capacity",
            "diseases including inflammatory disorders",
            "senescent red blood cells",
            "iron deficiency body iron homeostasis");
        List<String> actual = null;

        int records = 0;
        for (File f : FileUtils.listFiles(workflowOutputData, null, true)) {
            if (f.isFile() && f.getName().startsWith("part-")) {
                Configuration conf = new Configuration();
                Path path = new Path("file://" + f.getAbsolutePath());
                SequenceFile.Reader reader = new SequenceFile.Reader(conf, SequenceFile.Reader.file(path));
                Text key = new Text();
                BytesWritable value = new BytesWritable();
                while (reader.next(key, value)) {
                    KeywordExtractionProtos.ExtractedKeywords extractedKwds =
                            KeywordExtractionProtos.ExtractedKeywords.parseFrom(value.copyBytes());
                    actual = extractedKwds.getKeywordList();
                    records ++;
                }
            }
        }
        assertEquals(records, 1);
        assertEquals(actual, expected);
    }
}