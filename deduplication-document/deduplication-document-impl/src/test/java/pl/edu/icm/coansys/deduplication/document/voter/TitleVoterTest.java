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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import pl.edu.icm.coansys.deduplication.document.tool.MockDocumentMetadataFactory;
import pl.edu.icm.coansys.deduplication.document.voter.Vote.VoteStatus;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class TitleVoterTest {

    private TitleVoter workTitleVoter;
    private Vote vote;

    @BeforeTest
    public void setUp() throws Exception {
        workTitleVoter = new TitleVoter();
        workTitleVoter.setApproveLevel(0.001f);
        workTitleVoter.setDisapproveLevel(0.11f);
        workTitleVoter.setMaxNormalizedTitleLength(90);
    }

    private double readTestSetFile(String filename) throws IOException {
        URL testSetFileURL = this.getClass().getResource(filename);
        File testSetFile = new File(testSetFileURL.getFile());

        int duplicatesCount = 0;
        int nonDuplicatesCount = 0;

        BufferedReader br = new BufferedReader(new FileReader(testSetFile));
        String line;
        while ((line = br.readLine()) != null) {
            String[] fields = line.split("\t");

            DocumentMetadata doc1 = MockDocumentMetadataFactory.createDocumentMetadata(fields[2]);
            DocumentMetadata doc2 = MockDocumentMetadataFactory.createDocumentMetadata(fields[3]);

            vote = workTitleVoter.vote(doc1, doc2);
            if (voteMeansDuplicate(vote)) {
                duplicatesCount++;
            } else {
                nonDuplicatesCount++;
            }
        }
        br.close();
        return 1.0 * duplicatesCount / (duplicatesCount + nonDuplicatesCount);
    }

    private boolean voteMeansDuplicate(Vote vote) {
        return vote.getStatus().equals(VoteStatus.EQUALS)
                || (vote.getStatus().equals(VoteStatus.PROBABILITY) && vote.getProbability() > 0.5);
    }

    @Test
    public void checkFiles() throws IOException {
        // make sure thad TitleVoter doesn't give too many false positives
        Assert.assertTrue(readTestSetFile("/titles_pairs/false_duplicates_pairs_0_7") < 0.01);
        Assert.assertTrue(readTestSetFile("/titles_pairs/false_duplicates_pairs_0_8") < 0.01);
        Assert.assertTrue(readTestSetFile("/titles_pairs/false_duplicates_pairs_0_9") < 0.1);
        Assert.assertTrue(readTestSetFile("/titles_pairs/false_duplicates_pairs_1_0") < 0.1);

        // higher is better...
        Assert.assertTrue(readTestSetFile("/titles_pairs/real_duplicates_pairs_0_7") >= 0.5);
        Assert.assertTrue(readTestSetFile("/titles_pairs/real_duplicates_pairs_0_8") >= 0.5);
        Assert.assertTrue(readTestSetFile("/titles_pairs/real_duplicates_pairs_0_9") >= 0.8);
        Assert.assertTrue(readTestSetFile("/titles_pairs/real_duplicates_pairs_1_0") >= 0.9);
    }
}
