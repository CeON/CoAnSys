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
package pl.edu.icm.coansys.deduplication.document.comparator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.icm.coansys.commons.java.DocumentWrapperUtils;
import pl.edu.icm.coansys.commons.java.StringTools;
import pl.edu.icm.coansys.deduplication.document.voter.SimilarityVoter;
import pl.edu.icm.coansys.deduplication.document.voter.Vote;
import pl.edu.icm.coansys.models.DocumentProtos;

/**
 *
 * @author Łukasz Dumiszewski
 * @author Artur Czeczko
 *
 */
public abstract class AbstractWorkComparator implements WorkComparator {

    private static Logger logger = LoggerFactory.getLogger(AbstractWorkComparator.class);
    private List<SimilarityVoter> similarityVoters;

    /**
     * Tells whether the given documents are duplicates.
     */
    @Override
    public boolean isDuplicate(DocumentProtos.DocumentMetadata doc1, DocumentProtos.DocumentMetadata doc2, Reducer<Text, BytesWritable, Text, Text>.Context context) {

        List<Float> probabilities = new ArrayList<Float>();
        List<Float> weights = new ArrayList<Float>();

        String ids = doc1.getKey() + ", " + doc2.getKey();
        StringBuilder logBuilder = new StringBuilder();

        StringBuilder debugOutputBuilder = new StringBuilder();
        debugOutputBuilder.append(compactTitle(doc1)).append(", ").append(compactTitle(doc2));

        if (similarityVoters != null) {
            for (SimilarityVoter voter : similarityVoters) {
                debugOutputBuilder.append("#").append(voter.getClass().getSimpleName());
                Vote vote = voter.vote(doc1, doc2);
                Vote.VoteStatus status = vote.getStatus();
                debugOutputBuilder.append(":").append(vote.getStatus().name());

                switch (vote.getStatus()) {
                    case EQUALS:
                        logger.info("Documents " + ids + " considered as duplicates because of result EQUALS of voter "
                                + voter.getClass().getName());
                        writeDebugOutputToContext(context, ids, debugOutputBuilder.toString());
                        return true;
                    case NOT_EQUALS:
                        return false;
                    case ABSTAIN:
                        continue;
                    case PROBABILITY:
                        logBuilder.append(" -- voter ").append(voter.getClass().getName())
                                .append(" returned probability ").append(vote.getProbability())
                                .append(", weight ").append(voter.getWeight()).append('\n');
                        probabilities.add(vote.getProbability());
                        weights.add(voter.getWeight());
                        debugOutputBuilder.append("-").append(vote.getProbability());
                }
            }
        }

        boolean result = calculateResult(probabilities, weights, debugOutputBuilder);

        if (result) {
            logger.info(ids + " considered as duplicates because:\n" + logBuilder.toString());
            //logger.info("doc1:\n" + doc1.getDocumentMetadata());
            //logger.info("doc2:\n" + doc2.getDocumentMetadata());
            writeDebugOutputToContext(context, ids, debugOutputBuilder.toString());
        }

        return result;
    }

    private static void writeDebugOutputToContext(Reducer<Text, BytesWritable, Text, Text>.Context context, String key, String value) {
        if (context != null) {
            try {
                context.write(new Text(key), new Text(value));
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(AbstractWorkComparator.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                java.util.logging.Logger.getLogger(AbstractWorkComparator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static String compactTitle(DocumentProtos.DocumentMetadata doc) {
        String docKey = DocumentWrapperUtils.getMainTitle(doc);
        return StringTools.normalize(docKey);
    }

    protected abstract boolean calculateResult(List<Float> probabilites, List<Float> weights, StringBuilder logBuilder);

    //******************** SETTERS ********************
    public void setSimilarityVoters(List<SimilarityVoter> similarityVoters) {
        this.similarityVoters = similarityVoters;
    }
}
