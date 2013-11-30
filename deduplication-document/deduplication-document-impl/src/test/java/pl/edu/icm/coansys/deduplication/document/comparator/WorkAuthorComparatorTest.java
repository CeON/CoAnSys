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
import pl.edu.icm.coansys.deduplication.document.comparator.WorkAuthorComparator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import pl.edu.icm.coansys.deduplication.document.tool.MockDocumentMetadataFactory;
import pl.edu.icm.coansys.models.DocumentProtos;
import pl.edu.icm.coansys.models.DocumentProtos.Author;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper;

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
        Author janKowalski = MockDocumentMetadataFactory.createAuthor("Jan", "Kowalski", 1);
        Author adamNowak = MockDocumentMetadataFactory.createAuthor("Adam", "Nowak", 2);
        DocumentProtos.DocumentMetadata doc1 = MockDocumentMetadataFactory.createDocumentMetadata("Ala m kota", 2012, janKowalski, adamNowak);
        DocumentProtos.DocumentMetadata doc2 = MockDocumentMetadataFactory.createDocumentMetadata("Ala ma kota", 2012, janKowalski, adamNowak);
        
        //Assert.assertTrue(workAuthorComparator.sameAuthors(doc1, doc2));
    }
    
    
    
    @Test
    public void testSameAuthors_TotallyDifferent() {
        Author janKowalski = MockDocumentMetadataFactory.createAuthor("Jan", "Kowalski", 1);
        Author adamNowak = MockDocumentMetadataFactory.createAuthor("Adam", "Nowak", 2);
        DocumentProtos.DocumentMetadata doc1 = MockDocumentMetadataFactory.createDocumentMetadata("Ala m kota", 2012, janKowalski, adamNowak);
        
        Author johnSmith = MockDocumentMetadataFactory.createAuthor("John", "Smith", 1);
        DocumentProtos.DocumentMetadata doc2 = MockDocumentMetadataFactory.createDocumentMetadata("Ala ma kota", 2012, johnSmith);
        
        //Assert.assertFalse(workAuthorComparator.sameAuthors(doc1, doc2));
    }
    
    
    @Test
    public void testSameAuthors_NotSameAuthors() {
        Author janKowalski = MockDocumentMetadataFactory.createAuthor("Jan", "Kowalski", 1);
        Author adamNowak = MockDocumentMetadataFactory.createAuthor("Adam", "Nowak", 2);
        Author Онущенко = MockDocumentMetadataFactory.createAuthor("A", "Онущенко", 3);
        DocumentProtos.DocumentMetadata doc1 = MockDocumentMetadataFactory.createDocumentMetadata("Ala m kota", 2012, janKowalski, adamNowak, Онущенко);
        
        adamNowak = MockDocumentMetadataFactory.createAuthor("Adam", "Nowak", 1);
        janKowalski = MockDocumentMetadataFactory.createAuthor("Jan", "Kowalski", 2);
        Author adamZbik = MockDocumentMetadataFactory.createAuthor("Adam", "Żbik", 3);
        
        DocumentProtos.DocumentMetadata doc2 = MockDocumentMetadataFactory.createDocumentMetadata("Ala ma kota", 2012, janKowalski, adamNowak, adamZbik);
        
        //Assert.assertFalse(workAuthorComparator.sameAuthors(doc1, doc2));
    }
    
   
    @Test
    public void testSameAuthors_SameAuthors_DifferentPositions() {
        Author janKowalski = MockDocumentMetadataFactory.createAuthor("Jan", "Kowalski", 1);
        Author adamNowak = MockDocumentMetadataFactory.createAuthor("Adam", "Nowak", 2);
        Author Онущенко = MockDocumentMetadataFactory.createAuthor("A", "Онущенко", 3);
        DocumentProtos.DocumentMetadata doc1 = MockDocumentMetadataFactory.createDocumentMetadata("Ala m kota", 2012, janKowalski, adamNowak, Онущенко);
        
        janKowalski = MockDocumentMetadataFactory.createAuthor("Jan", "Kowalski", 3);
        adamNowak = MockDocumentMetadataFactory.createAuthor("Adam", "Nowak", 2);
        Онущенко = MockDocumentMetadataFactory.createAuthor("A", "Онущенко", 1);
        DocumentProtos.DocumentMetadata doc2 = MockDocumentMetadataFactory.createDocumentMetadata("Ala ma kota", 2012, janKowalski, adamNowak, Онущенко);
        
        // different journals
        Mockito.when(workJournalComparator.sameJournals(Mockito.any(DocumentWrapper.class), Mockito.any(DocumentWrapper.class))).thenReturn(false);
        //Assert.assertFalse(workAuthorComparator.sameAuthors(doc1, doc2));
        
        // same journals
        Mockito.when(workJournalComparator.sameJournals(Mockito.any(DocumentWrapper.class), Mockito.any(DocumentWrapper.class))).thenReturn(true);
        //Assert.assertTrue(workAuthorComparator.sameAuthors(doc1, doc2));
        
        
        janKowalski = MockDocumentMetadataFactory.createAuthor("Jan", "Kowalski", 3);
        adamNowak = MockDocumentMetadataFactory.createAuthor("Adam", "Nowak", 2);
        Author noname = MockDocumentMetadataFactory.createAuthor("A", WorkAuthorComparator.NONAME_SURNAME, 1);
        DocumentProtos.DocumentMetadata doc3 = MockDocumentMetadataFactory.createDocumentMetadata("Ala ma kota", 2012, janKowalski, adamNowak, noname);
        
        //Assert.assertTrue(workAuthorComparator.sameAuthors(doc1, doc3));
        
        
    }
    
     

}
