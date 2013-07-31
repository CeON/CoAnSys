package pl.edu.icm.coansys.disambiguation.author.pig;

import java.io.IOException;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataByteArray;
import org.apache.pig.data.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.icm.coansys.commons.java.StackTraceExtractor;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata;

public class GetContributors extends EvalFunc<String> {

    private static final Logger logger = LoggerFactory.getLogger(GetContributors.class);

    /**
     * @param Tuple input with DocumentMetadata metadata of document and int
     * index of contributor in document authors list
     * @return String author's key
     */
    @Override
    public String exec(Tuple input) throws IOException {

        if (input == null || input.size() == 0) {
            return null;
        }

        try {
            //getting metadata
            DataByteArray dba = (DataByteArray) input.get(0);

            DocumentMetadata metadane = DocumentMetadata.parseFrom(dba.get());

            //getting contributor index in list of this document's authors
            int contributorPos = (Integer) input.get(1);

            //DataBag ret = new DefaultDataBag();

            return metadane.getBasicMetadata().getAuthorList().
                    get(contributorPos).getKey();

        } catch (Exception e) {
            logger.error("Error in processing input row:", e);
            throw new IOException("Caught exception processing input row:\n"
                    + StackTraceExtractor.getStackTrace(e));
        }
    }
}