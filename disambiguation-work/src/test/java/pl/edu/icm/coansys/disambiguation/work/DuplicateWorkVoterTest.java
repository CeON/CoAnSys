package pl.edu.icm.coansys.disambiguation.work;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import pl.edu.icm.coansys.disambiguation.work.comparator.WorkAuthorComparator;
import pl.edu.icm.coansys.disambiguation.work.comparator.WorkTitleComparator;
import pl.edu.icm.coansys.disambiguation.work.comparator.WorkYearComparator;
import pl.edu.icm.coansys.disambiguation.work.tool.MockDocumentWrapperFactory;
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentWrapper;

public class DuplicateWorkVoterTest {

    
    private DuplicateWorkVoter duplicateWorkVoter;
    private WorkTitleComparator workTitleComparator = Mockito.mock(WorkTitleComparator.class);
    private WorkAuthorComparator workAuthorComparator = Mockito.mock(WorkAuthorComparator.class);
    private WorkYearComparator workYearComparator = Mockito.mock(WorkYearComparator.class);
    
    private DocumentWrapper doc1 = MockDocumentWrapperFactory.createDocumentWrapper("a");
    private DocumentWrapper doc2 = MockDocumentWrapperFactory.createDocumentWrapper("b");
    
    
    @Before
    public void setUp() throws Exception {
        duplicateWorkVoter = new DuplicateWorkVoter();
        duplicateWorkVoter.setWorkTitleComparator(workTitleComparator);
        duplicateWorkVoter.setWorkAuthorComparator(workAuthorComparator);
        duplicateWorkVoter.setWorkYearComparator(workYearComparator);
        
    }

    
    @Test
    public void testSameTitleSameAuthorsSameYear() {
        Mockito.when(workTitleComparator.sameTitles(Mockito.any(DocumentWrapper.class), Mockito.any(DocumentWrapper.class))).thenReturn(true);
        Mockito.when(workAuthorComparator.sameAuthors(Mockito.any(DocumentWrapper.class), Mockito.any(DocumentWrapper.class))).thenReturn(true);
        Mockito.when(workYearComparator.sameYears(Mockito.any(DocumentWrapper.class), Mockito.any(DocumentWrapper.class))).thenReturn(true);
        
        Assert.assertTrue(duplicateWorkVoter.isDuplicate(doc1, doc2));
        
        Mockito.verify(workTitleComparator, Mockito.times(1)).sameTitles(Mockito.any(DocumentWrapper.class), Mockito.any(DocumentWrapper.class));
        Mockito.verify(workAuthorComparator, Mockito.times(1)).sameAuthors(Mockito.any(DocumentWrapper.class), Mockito.any(DocumentWrapper.class));
        Mockito.verify(workYearComparator, Mockito.times(1)).sameYears(Mockito.any(DocumentWrapper.class), Mockito.any(DocumentWrapper.class));
    }
    
    
    @Test
    public void testSameTitleSameAuthorsDifferentYear() {
        Mockito.when(workTitleComparator.sameTitles(Mockito.any(DocumentWrapper.class), Mockito.any(DocumentWrapper.class))).thenReturn(true);
        Mockito.when(workAuthorComparator.sameAuthors(Mockito.any(DocumentWrapper.class), Mockito.any(DocumentWrapper.class))).thenReturn(true);
        Mockito.when(workYearComparator.sameYears(Mockito.any(DocumentWrapper.class), Mockito.any(DocumentWrapper.class))).thenReturn(false);
        
        Assert.assertFalse(duplicateWorkVoter.isDuplicate(doc1, doc2));
        
        Mockito.verify(workTitleComparator, Mockito.times(1)).sameTitles(Mockito.any(DocumentWrapper.class), Mockito.any(DocumentWrapper.class));
        Mockito.verify(workAuthorComparator, Mockito.times(1)).sameAuthors(Mockito.any(DocumentWrapper.class), Mockito.any(DocumentWrapper.class));
        Mockito.verify(workYearComparator, Mockito.times(1)).sameYears(Mockito.any(DocumentWrapper.class), Mockito.any(DocumentWrapper.class));
    }
    
    @Test
    public void testSameTitleDifferentAuthorsSameYear() {
        Mockito.when(workTitleComparator.sameTitles(Mockito.any(DocumentWrapper.class), Mockito.any(DocumentWrapper.class))).thenReturn(true);
        Mockito.when(workAuthorComparator.sameAuthors(Mockito.any(DocumentWrapper.class), Mockito.any(DocumentWrapper.class))).thenReturn(false);
        Mockito.when(workYearComparator.sameYears(Mockito.any(DocumentWrapper.class), Mockito.any(DocumentWrapper.class))).thenReturn(true);
        
        Assert.assertFalse(duplicateWorkVoter.isDuplicate(doc1, doc2));
        
        Mockito.verify(workTitleComparator, Mockito.times(1)).sameTitles(Mockito.any(DocumentWrapper.class), Mockito.any(DocumentWrapper.class));
        Mockito.verify(workAuthorComparator, Mockito.times(1)).sameAuthors(Mockito.any(DocumentWrapper.class), Mockito.any(DocumentWrapper.class));
        Mockito.verify(workYearComparator, Mockito.times(0)).sameYears(Mockito.any(DocumentWrapper.class), Mockito.any(DocumentWrapper.class));
    }
    
    
    @Test
    public void testDifferentTitleSameAuthorsSameYear() {
        Mockito.when(workTitleComparator.sameTitles(Mockito.any(DocumentWrapper.class), Mockito.any(DocumentWrapper.class))).thenReturn(false);
        Mockito.when(workAuthorComparator.sameAuthors(Mockito.any(DocumentWrapper.class), Mockito.any(DocumentWrapper.class))).thenReturn(true);
        Mockito.when(workYearComparator.sameYears(Mockito.any(DocumentWrapper.class), Mockito.any(DocumentWrapper.class))).thenReturn(true);
        
        Assert.assertFalse(duplicateWorkVoter.isDuplicate(doc1, doc2));
        
        Mockito.verify(workTitleComparator, Mockito.times(1)).sameTitles(Mockito.any(DocumentWrapper.class), Mockito.any(DocumentWrapper.class));
        Mockito.verify(workAuthorComparator, Mockito.times(0)).sameAuthors(Mockito.any(DocumentWrapper.class), Mockito.any(DocumentWrapper.class));
        Mockito.verify(workYearComparator, Mockito.times(0)).sameYears(Mockito.any(DocumentWrapper.class), Mockito.any(DocumentWrapper.class));
    }
    
     


}
