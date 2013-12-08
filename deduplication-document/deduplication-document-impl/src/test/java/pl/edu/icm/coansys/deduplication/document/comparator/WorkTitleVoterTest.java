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

package pl.edu.icm.coansys.deduplication.document.comparator;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.Assert;

import pl.edu.icm.coansys.deduplication.document.tool.MockDocumentMetadataFactory;
import pl.edu.icm.coansys.deduplication.document.voter.Vote;
import pl.edu.icm.coansys.deduplication.document.voter.WorkTitleVoter;
import pl.edu.icm.coansys.models.DocumentProtos;

public class WorkTitleVoterTest {

    
    private WorkTitleVoter workTitleVoter;
    private Vote vote;
    
    @BeforeTest
    public void setUp() throws Exception {
        workTitleVoter = new WorkTitleVoter();
        workTitleVoter.setApproveLevel(0.03f);
        workTitleVoter.setDisapproveLevel(0.15f);
        workTitleVoter.setDigitsPercentageTreshold(15);
    }
    

    @Test
    public void testSameTitles_Simple() {
        DocumentProtos.DocumentMetadata doc1 = MockDocumentMetadataFactory.createDocumentMetadata("Ala m kota");
        DocumentProtos.DocumentMetadata doc2 = MockDocumentMetadataFactory.createDocumentMetadata("Ala ma wielkiego tygrysa");
        vote = workTitleVoter.vote(doc1, doc2);
        
        Assert.assertEquals(vote.getStatus(), Vote.VoteStatus.NOT_EQUALS);
        
        doc1 = MockDocumentMetadataFactory.createDocumentMetadata("Ala ma wielkkiego tygrysa");
        doc2 = MockDocumentMetadataFactory.createDocumentMetadata("Ala ma wielkiego tygrysa");
        vote = workTitleVoter.vote(doc1, doc2);
        
        Assert.assertEquals(vote.getStatus(), Vote.VoteStatus.PROBABILITY);
        Assert.assertTrue(vote.getProbability() > 0.5f);
    }
    
    
    @Test
    public void testSameTitles_DifferentTitleNumbers() {
        DocumentProtos.DocumentMetadata doc1 = MockDocumentMetadataFactory.createDocumentMetadata("Ala ma kota cz.1");
        DocumentProtos.DocumentMetadata doc2 = MockDocumentMetadataFactory.createDocumentMetadata("Ala ma kota cz.2");
        vote = workTitleVoter.vote(doc1, doc2);
        
        Assert.assertEquals(vote.getStatus(), Vote.VoteStatus.NOT_EQUALS);
        
        
        doc1 = MockDocumentMetadataFactory.createDocumentMetadata("Ala ma kota (1)");
        doc2 = MockDocumentMetadataFactory.createDocumentMetadata("Ala ma kota (2)");
        vote = workTitleVoter.vote(doc1, doc2);
        
        Assert.assertEquals(vote.getStatus(), Vote.VoteStatus.NOT_EQUALS);
        
        
        doc1 = MockDocumentMetadataFactory.createDocumentMetadata("Ala ma kota (I)");
        doc2 = MockDocumentMetadataFactory.createDocumentMetadata("Ala ma kota (II)");
        vote = workTitleVoter.vote(doc1, doc2);
        
        Assert.assertEquals(vote.getStatus(), Vote.VoteStatus.NOT_EQUALS);

        
        doc1 = MockDocumentMetadataFactory.createDocumentMetadata("Łatwiej i taniej przez granicę (cz.I)");
        doc2 = MockDocumentMetadataFactory.createDocumentMetadata("Łatwiej i taniej przez granicę (cz.II)");
        vote = workTitleVoter.vote(doc1, doc2);
        
        Assert.assertEquals(vote.getStatus(), Vote.VoteStatus.NOT_EQUALS);

        
        doc1 = MockDocumentMetadataFactory.createDocumentMetadata("Łatwiej i taniej przez granicę (part one)");
        doc2 = MockDocumentMetadataFactory.createDocumentMetadata("Łatwiej i taniej przez granicę (part 1)");
        vote = workTitleVoter.vote(doc1, doc2);
        
        Assert.assertEquals(vote.getStatus(), Vote.VoteStatus.PROBABILITY);
        Assert.assertTrue(vote.getProbability() > 0.5f);
        
        
        doc1 = MockDocumentMetadataFactory.createDocumentMetadata("Łatwiej i taniej przez granicę (part one)");
        doc2 = MockDocumentMetadataFactory.createDocumentMetadata("Łatwiej i taniej przez granicę (part two)");
        vote = workTitleVoter.vote(doc1, doc2);
        
        Assert.assertEquals(vote.getStatus(), Vote.VoteStatus.NOT_EQUALS);
    }
    
    @Test
    public void testSameTitles_DocumentsInRussian() {
        
        DocumentProtos.DocumentMetadata doc1 = MockDocumentMetadataFactory.createDocumentMetadata("Квантовый размерный эффект в трехмерных микрокристаллах полупроводников");
        DocumentProtos.DocumentMetadata doc2 = MockDocumentMetadataFactory.createDocumentMetadata("Квантовый размерный эффект в трехмерных микрокристаллах полупроводников");
        vote = workTitleVoter.vote(doc1, doc2);
        
        Assert.assertEquals(vote.getStatus(), Vote.VoteStatus.PROBABILITY);
        Assert.assertTrue(vote.getProbability() > 0.5f);  
        
        
        doc1 = MockDocumentMetadataFactory.createDocumentMetadata("Квантовый размерный эффект в трехмерных микрокристаллах полупроводников");
        doc2 = MockDocumentMetadataFactory.createDocumentMetadata("Квантовый размерный beledblsdjs полупроводников");
        vote = workTitleVoter.vote(doc1, doc2);
        
        Assert.assertEquals(vote.getStatus(), Vote.VoteStatus.NOT_EQUALS);
        
        
        doc1 = MockDocumentMetadataFactory.createDocumentMetadata("Квантовый размерный эффект в трехмерных микрокристаллах полупроводников");
        doc2 = MockDocumentMetadataFactory.createDocumentMetadata("Квантовый трехмерных мерный эффект в микрокристаллах полупроводников");
        vote = workTitleVoter.vote(doc1, doc2);
        
        Assert.assertEquals(vote.getStatus(), Vote.VoteStatus.NOT_EQUALS);
        
        
        doc1 = MockDocumentMetadataFactory.createDocumentMetadata("Квантовый размерный эффект в трехмерных микрокристаллах полупроводников 1");
        doc2 = MockDocumentMetadataFactory.createDocumentMetadata("Квантовый размерный эффект в трехмерных микрокристаллах полупроводников 2");
        vote = workTitleVoter.vote(doc1, doc2);
        
        Assert.assertEquals(vote.getStatus(), Vote.VoteStatus.NOT_EQUALS);        
    }
    
    @Test
    public void testSameTitles_EndsDifferent() {
        DocumentProtos.DocumentMetadata doc1 = MockDocumentMetadataFactory.createDocumentMetadata("Doświadczenia Unii Europejskiej w zakresie polityki proinnowacyjnej");
        DocumentProtos.DocumentMetadata doc2 = MockDocumentMetadataFactory.createDocumentMetadata(" Doświadczenia Unii Europejskiej w zakresie polityki innowacyjnej");
        vote = workTitleVoter.vote(doc1, doc2);
        
        Assert.assertEquals(vote.getStatus(), Vote.VoteStatus.PROBABILITY);
        Assert.assertTrue(vote.getProbability() > 0.5f); 

        
        doc1 = MockDocumentMetadataFactory.createDocumentMetadata("Aspiracje integracyjne państw śródziemnomorskich - Turcja");
        doc2 = MockDocumentMetadataFactory.createDocumentMetadata("Aspiracje integracyjne państw śródziemnomorskich - Malta");
        vote = workTitleVoter.vote(doc1, doc2);
        
        //Assert.assertEquals(vote.getStatus(), Vote.VoteStatus.NOT_EQUALS);
        //Assert.assertEquals(vote.getProbability(), 0.3333f);
        
        
        doc1 = MockDocumentMetadataFactory.createDocumentMetadata("Atak z sieci");
        doc2 = MockDocumentMetadataFactory.createDocumentMetadata("Atak z ulicy");
        vote = workTitleVoter.vote(doc1, doc2);
        
        Assert.assertEquals(vote.getStatus(), Vote.VoteStatus.NOT_EQUALS);        
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
