package pl.edu.icm.coansys.disambiguation.work;

import java.util.Date;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pl.edu.icm.coansys.disambiguation.work.tool.MockDocumentWrapperFactory;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper;

import com.google.common.collect.Lists;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:spring/applicationContext.xml")
public class DuplicateWorkDetectReduceServiceTest {

    private static Logger log = Logger.getLogger(DuplicateWorkDetectReduceServiceTest.class);
    
    @Autowired
    private DuplicateWorkDetectReduceService duplicateWorkDetectReduceService;
    
    private List<DocumentWrapper> documents = Lists.newArrayList();
    @SuppressWarnings("unchecked")
    private Reducer<Text, BytesWritable, Text, BytesWritable>.Context context = Mockito.mock(Context.class);
    
    @Before
    public void setUp() throws Exception {
        
        for (int i = 0; i < 2000; i++) {
            documents.add(MockDocumentWrapperFactory.createDocumentWrapper("A brief story of time " + i));
        }
        
        for (int i = 0; i <= 300; i++) {
            documents.add(MockDocumentWrapperFactory.createDocumentWrapper("The news in brief"));
        }
        
        
    }

    @Test
    public void testProcess() throws Exception {
        long startTime = new Date().getTime();
        duplicateWorkDetectReduceService.process(new Text(""), context, documents, 0, 100);
        long endTime = new Date().getTime();
        log.info("time[ms]: " + (endTime-startTime));
        
    }
    
    @Test
    public void testSplitDocuments() {
        Map<Text, List<DocumentWrapper>> splitDocuments = duplicateWorkDetectReduceService.splitDocuments(new Text(""), documents, 0);
        Assert.assertEquals(2, splitDocuments.size());
        
        splitDocuments = duplicateWorkDetectReduceService.splitDocuments(new Text(""), documents, 1);
        Assert.assertEquals(2, splitDocuments.size());
        
        splitDocuments = duplicateWorkDetectReduceService.splitDocuments(new Text(""), documents, 2);
        Assert.assertEquals(11, splitDocuments.size());
    }
    
    

}
