package pl.edu.icm.coansys.disambiguation.work;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pl.edu.icm.coansys.disambiguation.work.tool.MockDocumentWrapperFactory;
import pl.edu.icm.coansys.importers.models.DocumentProtos.Author;
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentWrapper;

import com.google.common.collect.Lists;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:spring/applicationContext.xml")
public class DuplicateWorkServiceTest {
    
    private static Logger log = LoggerFactory.getLogger(DuplicateWorkServiceTest.class);

    @Autowired
    private DuplicateWorkService duplicateWorkService;
    
    
    private List<DocumentWrapper> documentWrappers = Lists.newArrayList();
    
    private DocumentWrapper documentWrapper1;
    private DocumentWrapper documentWrapper5;
    
    @Before
    public void setUp() throws Exception {
        Author janKowalski = MockDocumentWrapperFactory.createAuthor("Jan", "Kowalski", 1);
        Author adamNowak = MockDocumentWrapperFactory.createAuthor("Adam", "Nowak", 2);
        
        documentWrapper1 = MockDocumentWrapperFactory.createDocumentWrapper("Ala ma kota a", 2012, janKowalski, adamNowak);
        DocumentWrapper documentWrapper2 = MockDocumentWrapperFactory.createDocumentWrapper("Ala ma kota b", 2012, janKowalski, adamNowak);
        DocumentWrapper documentWrapper3 = MockDocumentWrapperFactory.createDocumentWrapper("Ala ma kota gh", 2012, janKowalski, adamNowak);
        DocumentWrapper documentWrapper4 = MockDocumentWrapperFactory.createDocumentWrapper("Ala ma kota ff", 2012, janKowalski, adamNowak);
        documentWrapper5 = MockDocumentWrapperFactory.createDocumentWrapper("Ola ma psiaka 300", 2012, janKowalski, adamNowak);
        DocumentWrapper documentWrapper6 = MockDocumentWrapperFactory.createDocumentWrapper("Ola mma pisaka 300", 2012, janKowalski, adamNowak);
        
        documentWrappers.add(documentWrapper1);
        documentWrappers.add(documentWrapper2);
        documentWrappers.add(documentWrapper3);
        documentWrappers.add(documentWrapper4);
        documentWrappers.add(documentWrapper5);
        documentWrappers.add(documentWrapper6);
    }

    @Test
    public void testFindDuplicates() {
        Map<Integer, Set<DocumentWrapper>> duplicates = duplicateWorkService.findDuplicates(documentWrappers);
        for (Map.Entry<Integer, Set<DocumentWrapper>> entry : duplicates.entrySet()) {
            log.info("key   : {}", ""+entry.getKey());
            for (DocumentWrapper documentWrapper: entry.getValue()) {
                log.info("------ title0: {}", DocumentWrapperUtils.getMainTitle(documentWrapper));
            }
        }
        Assert.assertEquals(2, duplicates.size());
        for (Map.Entry<Integer, Set<DocumentWrapper>> entry : duplicates.entrySet()) {
            if (entry.getValue().contains(documentWrapper1)) {
                Assert.assertEquals(4, entry.getValue().size());
            }
            else if (entry.getValue().contains(documentWrapper5)) {
                Assert.assertEquals(2, entry.getValue().size());
            } else {
                Assert.fail();
            }
        }
    }

}
