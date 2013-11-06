/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2013 ICM-UW
 * 
 * CoAnSys is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * CoAnSys is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with CoAnSys. If not, see <http://www.gnu.org/licenses/>.
 */

package pl.edu.icm.coansys.disambiguation.work.comparator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pl.edu.icm.coansys.disambiguation.work.tool.MockDocumentMetadataFactory;
import pl.edu.icm.coansys.models.DocumentProtos;

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
        DocumentProtos.DocumentMetadata doc1 = MockDocumentMetadataFactory.createDocumentMetadata("Ala m kota");
        DocumentProtos.DocumentMetadata doc2 = MockDocumentMetadataFactory.createDocumentMetadata("Ala ma wielkiego tygrysa");
        
        //Assert.assertFalse(workTitleComparator.sameTitles(doc1, doc2));
        
        
        doc1 = MockDocumentMetadataFactory.createDocumentMetadata("Ala ma wielkkiego tygrysa");
        doc2 = MockDocumentMetadataFactory.createDocumentMetadata("Ala ma wielkiego tygrysa");
        
        //Assert.assertTrue(workTitleComparator.sameTitles(doc1, doc2));
        
    }
    
    
    @Test
    public void testSameTitles_DifferentTitleNumbers() {
        DocumentProtos.DocumentMetadata doc1 = MockDocumentMetadataFactory.createDocumentMetadata("Ala ma kota cz.1");
        DocumentProtos.DocumentMetadata doc2 = MockDocumentMetadataFactory.createDocumentMetadata("Ala ma kota cz.2");
        
        //Assert.assertFalse(workTitleComparator.sameTitles(doc1, doc2));

        
        doc1 = MockDocumentMetadataFactory.createDocumentMetadata("Ala ma kota (1)");
        doc2 = MockDocumentMetadataFactory.createDocumentMetadata("Ala ma kota (2)");
        
        //Assert.assertFalse(workTitleComparator.sameTitles(doc1, doc2));
        
        
        doc1 = MockDocumentMetadataFactory.createDocumentMetadata("Ala ma kota (I)");
        doc2 = MockDocumentMetadataFactory.createDocumentMetadata("Ala ma kota (II)");

        
        //Assert.assertFalse(workTitleComparator.sameTitles(doc1, doc2));

        
        doc1 = MockDocumentMetadataFactory.createDocumentMetadata("Łatwiej i taniej przez granicę (cz.I)");
        doc2 = MockDocumentMetadataFactory.createDocumentMetadata("Łatwiej i taniej przez granicę (cz.II)");
        
        //Assert.assertFalse(workTitleComparator.sameTitles(doc1, doc2));


        doc1 = MockDocumentMetadataFactory.createDocumentMetadata("Łatwiej i taniej przez granicę (part one)");
        doc2 = MockDocumentMetadataFactory.createDocumentMetadata("Łatwiej i taniej przez granicę (part 1)");
        
        //Assert.assertTrue(workTitleComparator.sameTitles(doc1, doc2));
        
        
        doc1 = MockDocumentMetadataFactory.createDocumentMetadata("Łatwiej i taniej przez granicę (part one)");
        doc2 = MockDocumentMetadataFactory.createDocumentMetadata("Łatwiej i taniej przez granicę (part two)");
        
        //Assert.assertFalse(workTitleComparator.sameTitles(doc1, doc2));
        
    }
    
    @Test
    public void testSameTitles_DocumentsInRussian() {
        
        DocumentProtos.DocumentMetadata doc1 = MockDocumentMetadataFactory.createDocumentMetadata("Квантовый размерный эффект в трехмерных микрокристаллах полупроводников");
        DocumentProtos.DocumentMetadata doc2 = MockDocumentMetadataFactory.createDocumentMetadata("Квантовый размерный эффект в трехмерных микрокристаллах полупроводников");
        
        //Assert.assertTrue(workTitleComparator.sameTitles(doc1, doc2));
        
        
        doc1 = MockDocumentMetadataFactory.createDocumentMetadata("Квантовый размерный эффект в трехмерных микрокристаллах полупроводников");
        doc2 = MockDocumentMetadataFactory.createDocumentMetadata("Квантовый размерный beledblsdjs полупроводников");
        
        //Assert.assertFalse(workTitleComparator.sameTitles(doc1, doc2));
        
        
        
        doc1 = MockDocumentMetadataFactory.createDocumentMetadata("Квантовый размерный эффект в трехмерных микрокристаллах полупроводников");
        doc2 = MockDocumentMetadataFactory.createDocumentMetadata("Квантовый трехмерных мерный эффект в микрокристаллах полупроводников");
        
        //Assert.assertFalse(workTitleComparator.sameTitles(doc1, doc2));
        
        
        doc1 = MockDocumentMetadataFactory.createDocumentMetadata("Квантовый размерный эффект в трехмерных микрокристаллах полупроводников 1");
        doc2 = MockDocumentMetadataFactory.createDocumentMetadata("Квантовый размерный эффект в трехмерных микрокристаллах полупроводников 2");
        
        //Assert.assertFalse(workTitleComparator.sameTitles(doc1, doc2));
        
    }
    
    @Test
    public void testSameTitles_EndsDifferent() {
        DocumentProtos.DocumentMetadata doc1 = MockDocumentMetadataFactory.createDocumentMetadata("Doświadczenia Unii Europejskiej w zakresie polityki proinnowacyjnej");
        DocumentProtos.DocumentMetadata doc2 = MockDocumentMetadataFactory.createDocumentMetadata(" Doświadczenia Unii Europejskiej w zakresie polityki innowacyjnej");
        
        //Assert.assertTrue(workTitleComparator.sameTitles(doc1, doc2));

        
        doc1 = MockDocumentMetadataFactory.createDocumentMetadata("Aspiracje integracyjne państw śródziemnomorskich - Turcja");
        doc2 = MockDocumentMetadataFactory.createDocumentMetadata("Aspiracje integracyjne państw śródziemnomorskich - Malta");
        
        //Assert.assertFalse(workTitleComparator.sameTitles(doc1, doc2));
        
        
        doc1 = MockDocumentMetadataFactory.createDocumentMetadata("Atak z sieci");
        doc2 = MockDocumentMetadataFactory.createDocumentMetadata("Atak z ulicy");
        
        //Assert.assertFalse(workTitleComparator.sameTitles(doc1, doc2));
        
    }
    
    @Test
    public void testSameTitles_OnLevenshteinBound() {
        DocumentProtos.DocumentMetadata doc1 = MockDocumentMetadataFactory.createDocumentMetadata("Ala ma kota b");
        DocumentProtos.DocumentMetadata doc2 = MockDocumentMetadataFactory.createDocumentMetadata("Ala mna kota f");
        
        //Assert.assertTrue(workTitleComparator.sameTitles(doc1, doc2));
    }
    
    @Test
    public void testSameTitles_ShortTitles() {
        DocumentProtos.DocumentMetadata doc1 = MockDocumentMetadataFactory.createDocumentMetadata("Makumba");
        DocumentProtos.DocumentMetadata doc2 = MockDocumentMetadataFactory.createDocumentMetadata("Matumba");
        
        //Assert.assertFalse(workTitleComparator.sameTitles(doc1, doc2));
        
        
        doc1 = MockDocumentMetadataFactory.createDocumentMetadata("Makumba bvx");
        doc2 = MockDocumentMetadataFactory.createDocumentMetadata("Makumba bwx");
        
        //Assert.assertTrue(workTitleComparator.sameTitles(doc1, doc2));
        
        
        doc1 = MockDocumentMetadataFactory.createDocumentMetadata("Makbvxy");
        doc2 = MockDocumentMetadataFactory.createDocumentMetadata("Makbvxya");
        
        //Assert.assertTrue(workTitleComparator.sameTitles(doc1, doc2));
        
        
        
    }
   

}
