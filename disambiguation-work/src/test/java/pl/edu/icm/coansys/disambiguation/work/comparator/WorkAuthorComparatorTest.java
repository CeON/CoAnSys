package pl.edu.icm.coansys.disambiguation.work.comparator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import pl.edu.icm.coansys.disambiguation.work.tool.MockDocumentWrapperFactory;
import pl.edu.icm.coansys.importers.models.DocumentProtos.Author;
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentWrapper;

public class WorkAuthorComparatorTest {

    private WorkAuthorComparator workAuthorComparator; 
    private WorkJournalComparator workJournalComparator;
    
    @Before
    public void setUp() throws Exception {
        
        workAuthorComparator = new WorkAuthorComparator();
        
        workJournalComparator = Mockito.mock(WorkJournalComparator.class);
        workAuthorComparator.setWorkJournalComparator(workJournalComparator);
    }

    
    
    @Test
    public void testSameAuthors_ExactlySame() {
        Author janKowalski = MockDocumentWrapperFactory.createAuthor("Jan", "Kowalski", 1);
        Author adamNowak = MockDocumentWrapperFactory.createAuthor("Adam", "Nowak", 2);
        DocumentWrapper doc1 = MockDocumentWrapperFactory.createDocumentWrapper("Ala m kota", 2012, janKowalski, adamNowak);
        DocumentWrapper doc2 = MockDocumentWrapperFactory.createDocumentWrapper("Ala ma kota", 2012, janKowalski, adamNowak);
        
        Assert.assertTrue(workAuthorComparator.sameAuthors(doc1, doc2));
    }
    
    
    
    @Test
    public void testSameAuthors_TotallyDifferent() {
        Author janKowalski = MockDocumentWrapperFactory.createAuthor("Jan", "Kowalski", 1);
        Author adamNowak = MockDocumentWrapperFactory.createAuthor("Adam", "Nowak", 2);
        DocumentWrapper doc1 = MockDocumentWrapperFactory.createDocumentWrapper("Ala m kota", 2012, janKowalski, adamNowak);
        
        Author johnSmith = MockDocumentWrapperFactory.createAuthor("John", "Smith", 1);
        DocumentWrapper doc2 = MockDocumentWrapperFactory.createDocumentWrapper("Ala ma kota", 2012, johnSmith);
        
        Assert.assertFalse(workAuthorComparator.sameAuthors(doc1, doc2));
    }
    
    
    @Test
    public void testSameAuthors_NotSameAuthors() {
        Author janKowalski = MockDocumentWrapperFactory.createAuthor("Jan", "Kowalski", 1);
        Author adamNowak = MockDocumentWrapperFactory.createAuthor("Adam", "Nowak", 2);
        Author Онущенко = MockDocumentWrapperFactory.createAuthor("A", "Онущенко", 3);
        DocumentWrapper doc1 = MockDocumentWrapperFactory.createDocumentWrapper("Ala m kota", 2012, janKowalski, adamNowak, Онущенко);
        
        adamNowak = MockDocumentWrapperFactory.createAuthor("Adam", "Nowak", 1);
        janKowalski = MockDocumentWrapperFactory.createAuthor("Jan", "Kowalski", 2);
        Author adamZbik = MockDocumentWrapperFactory.createAuthor("Adam", "Żbik", 3);
        
        DocumentWrapper doc2 = MockDocumentWrapperFactory.createDocumentWrapper("Ala ma kota", 2012, janKowalski, adamNowak, adamZbik);
        
        Assert.assertFalse(workAuthorComparator.sameAuthors(doc1, doc2));
    }
    
   
    @Test
    public void testSameAuthors_SameAuthors_DifferentPositions() {
        Author janKowalski = MockDocumentWrapperFactory.createAuthor("Jan", "Kowalski", 1);
        Author adamNowak = MockDocumentWrapperFactory.createAuthor("Adam", "Nowak", 2);
        Author Онущенко = MockDocumentWrapperFactory.createAuthor("A", "Онущенко", 3);
        DocumentWrapper doc1 = MockDocumentWrapperFactory.createDocumentWrapper("Ala m kota", 2012, janKowalski, adamNowak, Онущенко);
        
        janKowalski = MockDocumentWrapperFactory.createAuthor("Jan", "Kowalski", 3);
        adamNowak = MockDocumentWrapperFactory.createAuthor("Adam", "Nowak", 2);
        Онущенко = MockDocumentWrapperFactory.createAuthor("A", "Онущенко", 1);
        DocumentWrapper doc2 = MockDocumentWrapperFactory.createDocumentWrapper("Ala ma kota", 2012, janKowalski, adamNowak, Онущенко);
        
        // different journals
        Mockito.when(workJournalComparator.sameJournals(Mockito.any(DocumentWrapper.class), Mockito.any(DocumentWrapper.class))).thenReturn(false);
        Assert.assertFalse(workAuthorComparator.sameAuthors(doc1, doc2));
        
        // same journals
        Mockito.when(workJournalComparator.sameJournals(Mockito.any(DocumentWrapper.class), Mockito.any(DocumentWrapper.class))).thenReturn(true);
        Assert.assertTrue(workAuthorComparator.sameAuthors(doc1, doc2));
        
        
        janKowalski = MockDocumentWrapperFactory.createAuthor("Jan", "Kowalski", 3);
        adamNowak = MockDocumentWrapperFactory.createAuthor("Adam", "Nowak", 2);
        Author noname = MockDocumentWrapperFactory.createAuthor("A", WorkAuthorComparator.NONAME_SURNAME, 1);
        DocumentWrapper doc3 = MockDocumentWrapperFactory.createDocumentWrapper("Ala ma kota", 2012, janKowalski, adamNowak, noname);
        
        Assert.assertTrue(workAuthorComparator.sameAuthors(doc1, doc3));
        
        
    }
    
     

}
