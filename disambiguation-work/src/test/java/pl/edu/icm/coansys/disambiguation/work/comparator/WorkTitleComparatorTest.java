package pl.edu.icm.coansys.disambiguation.work.comparator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pl.edu.icm.coansys.disambiguation.work.tool.MockDocumentWrapperFactory;
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentWrapper;

public class WorkTitleComparatorTest {

    
    private WorkTitleComparator workTitleComparator; 
    
    @Before
    public void setUp() throws Exception {
        WorkTitleComparatorConfiguration config = new WorkTitleComparatorConfiguration();
        config.setTitleMaxLevenshteinDistance(5);
        config.setTitleEndMaxLevenshteinDistance(1);
        config.setTitleMostMeaningfulEndLength(8);
        config.setTitlePartLength(7);
        config.setTitleLevenshteinDistancePerPart(1);
        config.setTitleLengthExactComparison(7);
        workTitleComparator = new WorkTitleComparator(config);
    }
    

    @Test
    public void testSameTitles_Simple() {
        DocumentWrapper doc1 = MockDocumentWrapperFactory.createDocumentWrapper("Ala m kota");
        DocumentWrapper doc2 = MockDocumentWrapperFactory.createDocumentWrapper("Ala ma wielkiego tygrysa");
        
        Assert.assertFalse(workTitleComparator.sameTitles(doc1, doc2));
        
        
        doc1 = MockDocumentWrapperFactory.createDocumentWrapper("Ala ma wielkkiego tygrysa");
        doc2 = MockDocumentWrapperFactory.createDocumentWrapper("Ala ma wielkiego tygrysa");
        
        Assert.assertTrue(workTitleComparator.sameTitles(doc1, doc2));
        
    }
    
    
    @Test
    public void testSameTitles_DifferentTitleNumbers() {
        DocumentWrapper doc1 = MockDocumentWrapperFactory.createDocumentWrapper("Ala ma kota cz.1");
        DocumentWrapper doc2 = MockDocumentWrapperFactory.createDocumentWrapper("Ala ma kota cz.2");
        
        Assert.assertFalse(workTitleComparator.sameTitles(doc1, doc2));

        
        doc1 = MockDocumentWrapperFactory.createDocumentWrapper("Ala ma kota (1)");
        doc2 = MockDocumentWrapperFactory.createDocumentWrapper("Ala ma kota (2)");
        
        Assert.assertFalse(workTitleComparator.sameTitles(doc1, doc2));
        
        
        doc1 = MockDocumentWrapperFactory.createDocumentWrapper("Ala ma kota (I)");
        doc2 = MockDocumentWrapperFactory.createDocumentWrapper("Ala ma kota (II)");

        
        Assert.assertFalse(workTitleComparator.sameTitles(doc1, doc2));

        
        doc1 = MockDocumentWrapperFactory.createDocumentWrapper("Łatwiej i taniej przez granicę (cz.I)");
        doc2 = MockDocumentWrapperFactory.createDocumentWrapper("Łatwiej i taniej przez granicę (cz.II)");
        
        Assert.assertFalse(workTitleComparator.sameTitles(doc1, doc2));


        doc1 = MockDocumentWrapperFactory.createDocumentWrapper("Łatwiej i taniej przez granicę (part one)");
        doc2 = MockDocumentWrapperFactory.createDocumentWrapper("Łatwiej i taniej przez granicę (part 1)");
        
        Assert.assertTrue(workTitleComparator.sameTitles(doc1, doc2));
        
        
        doc1 = MockDocumentWrapperFactory.createDocumentWrapper("Łatwiej i taniej przez granicę (part one)");
        doc2 = MockDocumentWrapperFactory.createDocumentWrapper("Łatwiej i taniej przez granicę (part two)");
        
        Assert.assertFalse(workTitleComparator.sameTitles(doc1, doc2));
        
    }
    
    @Test
    public void testSameTitles_DocumentsInRussian() {
        
        DocumentWrapper doc1 = MockDocumentWrapperFactory.createDocumentWrapper("Квантовый размерный эффект в трехмерных микрокристаллах полупроводников");
        DocumentWrapper doc2 = MockDocumentWrapperFactory.createDocumentWrapper("Квантовый размерный эффект в трехмерных микрокристаллах полупроводников");
        
        Assert.assertTrue(workTitleComparator.sameTitles(doc1, doc2));
        
        
        doc1 = MockDocumentWrapperFactory.createDocumentWrapper("Квантовый размерный эффект в трехмерных микрокристаллах полупроводников");
        doc2 = MockDocumentWrapperFactory.createDocumentWrapper("Квантовый размерный beledblsdjs полупроводников");
        
        Assert.assertFalse(workTitleComparator.sameTitles(doc1, doc2));
        
        
        
        doc1 = MockDocumentWrapperFactory.createDocumentWrapper("Квантовый размерный эффект в трехмерных микрокристаллах полупроводников");
        doc2 = MockDocumentWrapperFactory.createDocumentWrapper("Квантовый трехмерных мерный эффект в микрокристаллах полупроводников");
        
        Assert.assertFalse(workTitleComparator.sameTitles(doc1, doc2));
        
        
        doc1 = MockDocumentWrapperFactory.createDocumentWrapper("Квантовый размерный эффект в трехмерных микрокристаллах полупроводников 1");
        doc2 = MockDocumentWrapperFactory.createDocumentWrapper("Квантовый размерный эффект в трехмерных микрокристаллах полупроводников 2");
        
        Assert.assertFalse(workTitleComparator.sameTitles(doc1, doc2));
        
    }
    
    @Test
    public void testSameTitles_EndsDifferent() {
        DocumentWrapper doc1 = MockDocumentWrapperFactory.createDocumentWrapper("Doświadczenia Unii Europejskiej w zakresie polityki proinnowacyjnej");
        DocumentWrapper doc2 = MockDocumentWrapperFactory.createDocumentWrapper(" Doświadczenia Unii Europejskiej w zakresie polityki innowacyjnej");
        
        Assert.assertTrue(workTitleComparator.sameTitles(doc1, doc2));

        
        doc1 = MockDocumentWrapperFactory.createDocumentWrapper("Aspiracje integracyjne państw śródziemnomorskich - Turcja");
        doc2 = MockDocumentWrapperFactory.createDocumentWrapper("Aspiracje integracyjne państw śródziemnomorskich - Malta");
        
        Assert.assertFalse(workTitleComparator.sameTitles(doc1, doc2));
        
        
        doc1 = MockDocumentWrapperFactory.createDocumentWrapper("Atak z sieci");
        doc2 = MockDocumentWrapperFactory.createDocumentWrapper("Atak z ulicy");
        
        Assert.assertFalse(workTitleComparator.sameTitles(doc1, doc2));
        
    }
    
    @Test
    public void testSameTitles_OnLevenshteinBound() {
        DocumentWrapper doc1 = MockDocumentWrapperFactory.createDocumentWrapper("Ala ma kota b");
        DocumentWrapper doc2 = MockDocumentWrapperFactory.createDocumentWrapper("Ala mna kota f");
        
        Assert.assertTrue(workTitleComparator.sameTitles(doc1, doc2));
    }
    
    @Test
    public void testSameTitles_ShortTitles() {
        DocumentWrapper doc1 = MockDocumentWrapperFactory.createDocumentWrapper("Makumba");
        DocumentWrapper doc2 = MockDocumentWrapperFactory.createDocumentWrapper("Matumba");
        
        Assert.assertFalse(workTitleComparator.sameTitles(doc1, doc2));
        
        
        doc1 = MockDocumentWrapperFactory.createDocumentWrapper("Makumba bvx");
        doc2 = MockDocumentWrapperFactory.createDocumentWrapper("Makumba bwx");
        
        Assert.assertTrue(workTitleComparator.sameTitles(doc1, doc2));
        
        
        doc1 = MockDocumentWrapperFactory.createDocumentWrapper("Makbvxy");
        doc2 = MockDocumentWrapperFactory.createDocumentWrapper("Makbvxya");
        
        Assert.assertTrue(workTitleComparator.sameTitles(doc1, doc2));
        
        
        
    }
   

}
