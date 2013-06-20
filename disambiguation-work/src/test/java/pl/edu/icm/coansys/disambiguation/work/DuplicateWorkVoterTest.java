package pl.edu.icm.coansys.disambiguation.work;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pl.edu.icm.coansys.disambiguation.work.tool.MockDocumentWrapperFactory;
import pl.edu.icm.coansys.importers.models.DocumentProtos.Author;
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentWrapper;

public class DuplicateWorkVoterTest {

    
    private DuplicateWorkVoter duplicateWorkVoter;
    
    @Before
    public void setUp() throws Exception {
        DuplicateWorkVoterConfiguration config = new DuplicateWorkVoterConfiguration();
        config.setTitleMaxLevenshteinDistance(5);
        config.setTitleEndMaxLevenshteinDistance(3);
        config.setTitleMostMeaningfulEndLength(8);
        duplicateWorkVoter = new DuplicateWorkVoter(new DuplicateWorkVoterConfiguration());
    }

    
    @Test
    public void testSameTitleSameFirstAuthorSameYear() {
        Author janKowalski = MockDocumentWrapperFactory.createAuthor("Jan", "Kowalski", 1);
        Author adamNowak = MockDocumentWrapperFactory.createAuthor("Adam", "Nowak", 2);
        DocumentWrapper doc1 = MockDocumentWrapperFactory.createDocumentWrapper("Ala m kota", 2012, janKowalski, adamNowak);
        
        DocumentWrapper doc2 = MockDocumentWrapperFactory.createDocumentWrapper("Ala ma kota", 2012, janKowalski, adamNowak);
        
        Assert.assertTrue(duplicateWorkVoter.isDuplicate(doc1, doc2));
    }
    
    
    
    @Test
    public void testSameTitleSameFirstAuthorDifferentYear() {
        Author janKowalski = MockDocumentWrapperFactory.createAuthor("Jan", "Kowalski", 1);
        Author adamNowak = MockDocumentWrapperFactory.createAuthor("Adam", "Nowak", 2);
        DocumentWrapper doc1 = MockDocumentWrapperFactory.createDocumentWrapper("Ala m kota", 2012, janKowalski, adamNowak);
        
        DocumentWrapper doc2 = MockDocumentWrapperFactory.createDocumentWrapper("Ala ma kota", 2011, janKowalski, adamNowak);
        
        Assert.assertFalse(duplicateWorkVoter.isDuplicate(doc1, doc2));
    }
    
    @Test
    public void testSameTitleDifferentFirstAuthorSameYear() {
        Author janKowalski = MockDocumentWrapperFactory.createAuthor("Jan", "Kowalski", 1);
        Author adamNowak = MockDocumentWrapperFactory.createAuthor("Adam", "Nowak", 2);
        DocumentWrapper doc1 = MockDocumentWrapperFactory.createDocumentWrapper("Ala m kota", 2012, janKowalski, adamNowak);
        
        Author johnSmith = MockDocumentWrapperFactory.createAuthor("John", "Smith", 1);
        DocumentWrapper doc2 = MockDocumentWrapperFactory.createDocumentWrapper("Ala ma kota", 2012, johnSmith);
        
        Assert.assertFalse(duplicateWorkVoter.isDuplicate(doc1, doc2));
    }
    
    
    @Test
    public void testDifferentTitleSameFirstAuthorSameYear() {
        Author janKowalski = MockDocumentWrapperFactory.createAuthor("Jan", "Kowalski", 1);
        Author adamNowak = MockDocumentWrapperFactory.createAuthor("Adam", "Nowak", 2);
        DocumentWrapper doc1 = MockDocumentWrapperFactory.createDocumentWrapper("Ala m kota", 2012, janKowalski, adamNowak);
        DocumentWrapper doc2 = MockDocumentWrapperFactory.createDocumentWrapper("Ala ma wielkiego tygrysa", 2012, janKowalski, adamNowak);
        
        Assert.assertFalse(duplicateWorkVoter.isDuplicate(doc1, doc2));
    }
    
     
    @Test
    public void testDifferentTitleNumbersSameFirstAuthorSameYear() {
        Author janKowalski = MockDocumentWrapperFactory.createAuthor("Jan", "Kowalski", 1);
        Author adamNowak = MockDocumentWrapperFactory.createAuthor("Adam", "Nowak", 2);
        
        
        DocumentWrapper doc1 = MockDocumentWrapperFactory.createDocumentWrapper("Ala ma kota cz.1", 2012, janKowalski, adamNowak);
        DocumentWrapper doc2 = MockDocumentWrapperFactory.createDocumentWrapper("Ala ma kota cz.2", 2012, janKowalski, adamNowak);
        
        Assert.assertFalse(duplicateWorkVoter.isDuplicate(doc1, doc2));

        
        doc1 = MockDocumentWrapperFactory.createDocumentWrapper("Ala ma kota (1)", 2012, janKowalski, adamNowak);
        doc2 = MockDocumentWrapperFactory.createDocumentWrapper("Ala ma kota (2)", 2012, janKowalski, adamNowak);
        
        Assert.assertFalse(duplicateWorkVoter.isDuplicate(doc1, doc2));
        
        
        doc1 = MockDocumentWrapperFactory.createDocumentWrapper("Ala ma kota (I)", 2012, janKowalski, adamNowak);
        doc2 = MockDocumentWrapperFactory.createDocumentWrapper("Ala ma kota (II)", 2012, janKowalski, adamNowak);

        
        Assert.assertFalse(duplicateWorkVoter.isDuplicate(doc1, doc2));

        
        doc1 = MockDocumentWrapperFactory.createDocumentWrapper("Łatwiej i taniej przez granicę (cz.I)", 2012, janKowalski, adamNowak);
        doc2 = MockDocumentWrapperFactory.createDocumentWrapper("Łatwiej i taniej przez granicę (cz.II)", 2012, janKowalski, adamNowak);
        
        Assert.assertFalse(duplicateWorkVoter.isDuplicate(doc1, doc2));
        

        doc1 = MockDocumentWrapperFactory.createDocumentWrapper("Łatwiej i taniej przez granicę (part one)", 2012, janKowalski, adamNowak);
        doc2 = MockDocumentWrapperFactory.createDocumentWrapper("Łatwiej i taniej przez granicę (part 1)", 2012, janKowalski, adamNowak);
        
        Assert.assertTrue(duplicateWorkVoter.isDuplicate(doc1, doc2));
        
        
        doc1 = MockDocumentWrapperFactory.createDocumentWrapper("Łatwiej i taniej przez granicę (part one)", 2012, janKowalski, adamNowak);
        doc2 = MockDocumentWrapperFactory.createDocumentWrapper("Łatwiej i taniej przez granicę (part two)", 2012, janKowalski, adamNowak);
        
        Assert.assertFalse(duplicateWorkVoter.isDuplicate(doc1, doc2));
        
    }
    
    @Test
    public void testDocumentsInRussian() {
        Author ekimow = MockDocumentWrapperFactory.createAuthor("АИ", "Екимов", 1);
        Author onyszenko = MockDocumentWrapperFactory.createAuthor("AA", "Онущенко", 2);
       
        DocumentWrapper doc1 = MockDocumentWrapperFactory.createDocumentWrapper("Квантовый размерный эффект в трехмерных микрокристаллах полупроводников", 2012, ekimow, onyszenko);
        DocumentWrapper doc2 = MockDocumentWrapperFactory.createDocumentWrapper("Квантовый размерный эффект в трехмерных микрокристаллах полупроводников", 2012, ekimow, onyszenko);
        
        Assert.assertTrue(duplicateWorkVoter.isDuplicate(doc1, doc2));
        
        
        doc1 = MockDocumentWrapperFactory.createDocumentWrapper("Квантовый размерный эффект в трехмерных микрокристаллах полупроводников", 2012, ekimow, onyszenko);
        doc2 = MockDocumentWrapperFactory.createDocumentWrapper("Квантовый размерный beledblsdjs полупроводников", 2012, ekimow, onyszenko);
        
        Assert.assertFalse(duplicateWorkVoter.isDuplicate(doc1, doc2));
        
        
        
        doc1 = MockDocumentWrapperFactory.createDocumentWrapper("Квантовый размерный эффект в трехмерных микрокристаллах полупроводников", 2012, ekimow, onyszenko);
        doc2 = MockDocumentWrapperFactory.createDocumentWrapper("Квантовый трехмерных мерный эффект в микрокристаллах полупроводников", 2012, ekimow, onyszenko);
        
        Assert.assertFalse(duplicateWorkVoter.isDuplicate(doc1, doc2));
        
        
        doc1 = MockDocumentWrapperFactory.createDocumentWrapper("Квантовый размерный эффект в трехмерных микрокристаллах полупроводников 1", 2012, ekimow, onyszenko);
        doc2 = MockDocumentWrapperFactory.createDocumentWrapper("Квантовый размерный эффект в трехмерных микрокристаллах полупроводников 2", 2012, ekimow, onyszenko);
        
        Assert.assertFalse(duplicateWorkVoter.isDuplicate(doc1, doc2));
        
    }
    
    @Test
    public void testTitleEndsDifferent() {
        Author janKowalski = MockDocumentWrapperFactory.createAuthor("Jan", "Kowalski", 1);
        Author adamNowak = MockDocumentWrapperFactory.createAuthor("Adam", "Nowak", 2);
        
        DocumentWrapper doc1 = MockDocumentWrapperFactory.createDocumentWrapper("Doświadczenia Unii Europejskiej w zakresie polityki proinnowacyjnej", 2012, janKowalski, adamNowak);
        DocumentWrapper doc2 = MockDocumentWrapperFactory.createDocumentWrapper(" Doświadczenia Unii Europejskiej w zakresie polityki innowacyjnej", 2012, janKowalski, adamNowak);
        
        Assert.assertTrue(duplicateWorkVoter.isDuplicate(doc1, doc2));

        
        doc1 = MockDocumentWrapperFactory.createDocumentWrapper("Aspiracje integracyjne państw śródziemnomorskich - Turcja", 2012, janKowalski, adamNowak);
        doc2 = MockDocumentWrapperFactory.createDocumentWrapper("Aspiracje integracyjne państw śródziemnomorskich - Malta", 2012, janKowalski, adamNowak);
        
        Assert.assertFalse(duplicateWorkVoter.isDuplicate(doc1, doc2));
        
        
        doc1 = MockDocumentWrapperFactory.createDocumentWrapper("Atak z sieci", 2012, janKowalski, adamNowak);
        doc2 = MockDocumentWrapperFactory.createDocumentWrapper("Atak z ulicy", 2012, janKowalski, adamNowak);
        
        Assert.assertFalse(duplicateWorkVoter.isDuplicate(doc1, doc2));
        
    }
    
   

}
