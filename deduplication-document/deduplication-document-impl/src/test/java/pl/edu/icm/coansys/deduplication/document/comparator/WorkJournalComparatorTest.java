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

import pl.edu.icm.coansys.deduplication.document.comparator.WorkJournalComparator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pl.edu.icm.coansys.deduplication.document.tool.MockDocumentMetadataFactory;
import pl.edu.icm.coansys.models.DocumentProtos;

public class WorkJournalComparatorTest {

    private WorkJournalComparator workJournalComparator = new WorkJournalComparator();
    
    @Before
    public void setUp() throws Exception {
        
    }

    
    @Test
    public void testSameJournals() {
        DocumentProtos.DocumentMetadata doc1 = MockDocumentMetadataFactory.createDocumentMetadata("Ala ma kota", "12341234", "Koko Journal");
        DocumentProtos.DocumentMetadata doc2 = MockDocumentMetadataFactory.createDocumentMetadata("Ala ma kota", "", "Koko Journal");
        DocumentProtos.DocumentMetadata doc3 = MockDocumentMetadataFactory.createDocumentMetadata("Ala ma kota", "1234-1234", "Jojko Journal");
        DocumentProtos.DocumentMetadata doc4 = MockDocumentMetadataFactory.createDocumentMetadata("Ala ma kota", "", "JamboBambo");
        DocumentProtos.DocumentMetadata doc5 = MockDocumentMetadataFactory.createDocumentMetadata("Ala ma kota", "4444-4444", "Jojko Journal");
        
        //Assert.assertTrue(workJournalComparator.sameJournals(doc1, doc2));
        //Assert.assertTrue(workJournalComparator.sameJournals(doc1, doc3));
        //Assert.assertFalse(workJournalComparator.sameJournals(doc1, doc4));
        //Assert.assertFalse(workJournalComparator.sameJournals(doc1, doc5));
        //Assert.assertFalse(workJournalComparator.sameJournals(doc3, doc5));
        
    }
    

}
