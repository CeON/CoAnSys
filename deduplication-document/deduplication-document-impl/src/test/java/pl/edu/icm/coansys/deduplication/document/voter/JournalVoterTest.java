/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2015 ICM-UW
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

package pl.edu.icm.coansys.deduplication.document.voter;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import pl.edu.icm.coansys.deduplication.document.tool.MockDocumentMetadataFactory;
import pl.edu.icm.coansys.models.DocumentProtos;

public class JournalVoterTest {

    private JournalVoter journalVoter;
    private Vote vote;
    
    @BeforeTest
    public void setUp() throws Exception {
        journalVoter = new JournalVoter();
        journalVoter.setDisapproveLevel(0.5f);
        journalVoter.setApproveLevel(0.05f);
    }

    
    @Test
    public void testSameJournals() {
        DocumentProtos.DocumentMetadata doc1 = MockDocumentMetadataFactory.createDocumentMetadata("Ala ma kota", "12341234", "Koko Journal");
        DocumentProtos.DocumentMetadata doc2 = MockDocumentMetadataFactory.createDocumentMetadata("Ala ma kota", "", "Koko Journal");
        DocumentProtos.DocumentMetadata doc3 = MockDocumentMetadataFactory.createDocumentMetadata("Ala ma kota", "1234-1234", "New Jojko Journal");
        DocumentProtos.DocumentMetadata doc4 = MockDocumentMetadataFactory.createDocumentMetadata("Ala ma kota", "", "JamboBambo");
        DocumentProtos.DocumentMetadata doc5 = MockDocumentMetadataFactory.createDocumentMetadata("Ala ma kota", "4444-4444", "New Jojko Journal");
        
        vote = journalVoter.vote(doc1, doc2);
        Assert.assertEquals(vote.getStatus(), Vote.VoteStatus.PROBABILITY);
        Assert.assertTrue(vote.getProbability() > 0.5);
        
        vote = journalVoter.vote(doc1, doc3);
        Assert.assertEquals(vote.getStatus(), Vote.VoteStatus.PROBABILITY);
        Assert.assertTrue(vote.getProbability() < 0.5);
        
        vote = journalVoter.vote(doc1, doc4);
        Assert.assertEquals(vote.getStatus(), Vote.VoteStatus.NOT_EQUALS);

        vote = journalVoter.vote(doc1, doc5);
        Assert.assertEquals(vote.getStatus(), Vote.VoteStatus.PROBABILITY);
        Assert.assertTrue(vote.getProbability() < 0.5);
        
        vote = journalVoter.vote(doc3, doc5);
        Assert.assertEquals(vote.getStatus(), Vote.VoteStatus.PROBABILITY);
        Assert.assertTrue(vote.getProbability() > 0.5);
    }
}
