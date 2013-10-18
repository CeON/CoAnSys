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

package pl.edu.icm.coansys.disambiguation.work;

import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import pl.edu.icm.coansys.disambiguation.work.tool.MockDocumentWrapperFactory;
import pl.edu.icm.coansys.disambiguation.work.voter.AuthorsVoter;
import pl.edu.icm.coansys.disambiguation.work.voter.SimilarityVoter;
import pl.edu.icm.coansys.disambiguation.work.voter.Vote;
import pl.edu.icm.coansys.disambiguation.work.voter.WorkTitleVoter;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper;

public class DuplicateWorkVoterTest {

    
    private DuplicateWorkComparator duplicateWorkComparator;
    private WorkTitleVoter workTitleVoter = mock(WorkTitleVoter.class);
    private AuthorsVoter workAuthorVoter = mock(AuthorsVoter.class);
    private SimilarityVoter simVoter = mock(SimilarityVoter.class);
    
    private DocumentWrapper doc1 = MockDocumentWrapperFactory.createDocumentWrapper("a");
    private DocumentWrapper doc2 = MockDocumentWrapperFactory.createDocumentWrapper("b");
    
    
    @Before
    public void setUp() throws Exception {
        List<SimilarityVoter> voters = new ArrayList<SimilarityVoter>();
        voters.add(workTitleVoter);
        voters.add(workAuthorVoter);
        
        duplicateWorkComparator = new DuplicateWorkComparator();
        duplicateWorkComparator.setSimilarityVoters(voters);
    }

    
    @Test
    public void testSameTitleSameAuthorsSameYear() {
        when(workTitleVoter.vote(any(DocumentWrapper.class), any(DocumentWrapper.class)))
                .thenReturn(new Vote(Vote.VoteStatus.PROBABILITY, 1.0f));
        when(workTitleVoter.getWeight()).thenReturn(1.0f);

        when(workAuthorVoter.vote(any(DocumentWrapper.class), any(DocumentWrapper.class)))
                .thenReturn(new Vote(Vote.VoteStatus.PROBABILITY, 0.8f));
        when(workAuthorVoter.getWeight()).thenReturn(1.0f);
        
        Assert.assertTrue(duplicateWorkComparator.isDuplicate(doc1, doc2));
        
        verify(workTitleVoter, times(1)).vote(any(DocumentWrapper.class), any(DocumentWrapper.class));
        /*
        verify(workTitleComparator, times(1))
                .sameTitles(any(DocumentWrapper.class), any(DocumentWrapper.class));
        verify(workAuthorComparator, times(1))
                .sameAuthors(any(DocumentWrapper.class), any(DocumentWrapper.class));
        verify(workYearComparator, times(1))
                .sameYears(any(DocumentWrapper.class), any(DocumentWrapper.class));
                */
    }
    
    
    @Test
    public void testSameTitleSameAuthorsDifferentYear() {
        /*
        when(workTitleComparator.sameTitles(any(DocumentWrapper.class), any(DocumentWrapper.class))).thenReturn(true);
        when(workAuthorComparator.sameAuthors(any(DocumentWrapper.class), any(DocumentWrapper.class))).thenReturn(true);
        when(workYearComparator.sameYears(any(DocumentWrapper.class), any(DocumentWrapper.class))).thenReturn(false);
        
        Assert.assertFalse(duplicateWorkComparator.isDuplicate(doc1, doc2));
        
        verify(workTitleComparator, times(1)).sameTitles(any(DocumentWrapper.class), any(DocumentWrapper.class));
        verify(workAuthorComparator, times(1)).sameAuthors(any(DocumentWrapper.class), any(DocumentWrapper.class));
        verify(workYearComparator, times(1)).sameYears(any(DocumentWrapper.class), any(DocumentWrapper.class));
        */
    }
    
    @Test
    public void testSameTitleDifferentAuthorsSameYear() {
        /*
        when(workTitleComparator.sameTitles(any(DocumentWrapper.class), any(DocumentWrapper.class))).thenReturn(true);
        when(workAuthorComparator.sameAuthors(any(DocumentWrapper.class), any(DocumentWrapper.class))).thenReturn(false);
        when(workYearComparator.sameYears(any(DocumentWrapper.class), any(DocumentWrapper.class))).thenReturn(true);
        
        Assert.assertFalse(duplicateWorkComparator.isDuplicate(doc1, doc2));
        
        verify(workTitleComparator, times(1)).sameTitles(any(DocumentWrapper.class), any(DocumentWrapper.class));
        verify(workAuthorComparator, times(1)).sameAuthors(any(DocumentWrapper.class), any(DocumentWrapper.class));
        verify(workYearComparator, times(0)).sameYears(any(DocumentWrapper.class), any(DocumentWrapper.class));
        */
    }
    
    
    @Test
    public void testDifferentTitleSameAuthorsSameYear() {
        /*
        when(workTitleComparator.sameTitles(any(DocumentWrapper.class), any(DocumentWrapper.class))).thenReturn(false);
        when(workAuthorComparator.sameAuthors(any(DocumentWrapper.class), any(DocumentWrapper.class))).thenReturn(true);
        when(workYearComparator.sameYears(any(DocumentWrapper.class), any(DocumentWrapper.class))).thenReturn(true);
        
        Assert.assertFalse(duplicateWorkComparator.isDuplicate(doc1, doc2));
        
        verify(workTitleComparator, times(1)).sameTitles(any(DocumentWrapper.class), any(DocumentWrapper.class));
        verify(workAuthorComparator, times(0)).sameAuthors(any(DocumentWrapper.class), any(DocumentWrapper.class));
        verify(workYearComparator, times(0)).sameYears(any(DocumentWrapper.class), any(DocumentWrapper.class));
        */
    }
    
     


}
