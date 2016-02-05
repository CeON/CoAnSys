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
package pl.edu.icm.coansys.disambiguation.author.pig.merger;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.mapreduce.Counter;
import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataByteArray;
import org.apache.pig.data.DataType;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.impl.logicalLayer.FrontendException;
import org.apache.pig.impl.logicalLayer.schema.Schema;
import org.apache.pig.tools.pigstats.PigStatusReporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.icm.coansys.commons.java.DiacriticsRemover;
import pl.edu.icm.coansys.commons.java.StackTraceExtractor;
import pl.edu.icm.coansys.models.DocumentProtos.Author;
import pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper;
import pl.edu.icm.coansys.models.DocumentProtos.KeyValue;

/**
 * 
 * @author pdendek
 */
public class MergeDocumentWithOrcid extends EvalFunc<Tuple> {

	PigStatusReporter myPigStatusReporter;
	private static final Logger logger = LoggerFactory
			.getLogger(MergeDocumentWithOrcid.class);

	@Override
	public Schema outputSchema(Schema p_input) {
		try {
			return Schema.generateNestedSchema(DataType.TUPLE,
					DataType.CHARARRAY, DataType.BYTEARRAY);
		} catch (FrontendException e) {
			logger.error("Error in creating output schema:", e);
			throw new IllegalStateException(e);
		}
	}

	@Override
	public Tuple exec(Tuple input) throws IOException {

		if (input == null || input.size() != 3) {
			return null;
		}

		try {
			myPigStatusReporter = PigStatusReporter.getInstance(); 
			
			//load input tuple
			////doi
			String docId = (String) input.get(0);
			////normal document
			DataByteArray dbaD = (DataByteArray) input.get(1);
			////orcid document
			DataByteArray dbaO = (DataByteArray) input.get(2);
			
			//load input documents
			DocumentWrapper dwD = DocumentWrapper.parseFrom(dbaD.get());
			List<Author> aDL = dwD.getDocumentMetadata().getBasicMetadata().getAuthorList(); 
			DocumentWrapper dwO = DocumentWrapper.parseFrom(dbaO.get());
			List<Author> aOL = dwO.getDocumentMetadata().getBasicMetadata().getAuthorList();
			
			//calculate merged author list
			List<Author> aRL = matchAuthors(docId,aDL,aOL);
			
			//construct resulting document
			BasicMetadata.Builder bmR = BasicMetadata.newBuilder(DocumentWrapper.newBuilder(dwD).getDocumentMetadata().getBasicMetadata());
			bmR.clearAuthor();
			bmR.addAllAuthor(aRL);
			
			DocumentMetadata.Builder dmR = DocumentMetadata.newBuilder(DocumentWrapper.newBuilder(dwD).getDocumentMetadata());
			dmR.setBasicMetadata(bmR);
			
			DocumentWrapper.Builder dwR = DocumentWrapper.newBuilder(dwD);
			dwR.setDocumentMetadata(dmR);
			
			//construct resulting tuple
			Tuple result = TupleFactory.getInstance().newTuple();
			result.append(docId);
			result.append(new DataByteArray(dwR.build().toByteArray()));
			return result;
		} catch (Exception e) {
			logger.error("Error in processing input row:", e);
			throw new IOException("Caught exception processing input row:\n"
					+ StackTraceExtractor.getStackTrace(e));
		}
	}

    protected List<Author> matchAuthors(String docId, List<Author> base,
            List<Author> second) {
        List<Author> result = new ArrayList<Author>(base.size());
        List<Author> secondCopy = new ArrayList<Author>(second);
        boolean changedBln = false;
        int changedInt = 0;
        logger.error("-------------------------------------------");
        logger.error("number of base authors: "+base.size()+"\tnumber of orcid authors");

        for (Author author : base) {
            Author foundAuthor = null;
            for (Author secondAuthor : secondCopy) {
                if (
			equalsIgnoreCaseIgnoreDiacritics(author.getName(), secondAuthor.getName())
                        || 
			//equalsIgnoreCaseIgnoreDiacritics(author.getForenames(), secondAuthor.getForenames()) &&
                        equalsIgnoreCaseIgnoreDiacritics(author.getSurname(), secondAuthor.getSurname())
		){
                    foundAuthor = secondAuthor;
                    break;
                }
            }
            if (foundAuthor != null) {
            	result.add(merge(author,foundAuthor));
            	changedBln = true;
            	changedInt++;
            	if(myPigStatusReporter != null){
            		Counter c = myPigStatusReporter.getCounter("ORCID Enhancement", "Author Enhanced");
            		if(c!=null){
            			c.increment(1);
            		}
            	}
            } else {
                result.add(Author.newBuilder(author).build());
            }
        }

        if(changedBln){
        	logger.info("------------------------------------------");
        	logger.info("Changed docId:"+docId);
        	if(myPigStatusReporter != null){
        		Counter c = myPigStatusReporter.getCounter("ORCID Enhancement", "Document Enhanced");
        		if(c!=null){
        			c.increment(1);
        		}
        	}
        }
        logger.error("number of intersections: "+changedInt);
        return result;
    }

    private Author merge(Author author, Author foundAuthor) {
		Author.Builder builder = Author.newBuilder(author);
		for(KeyValue kv : foundAuthor.getExtIdList()){
			if("orcid-author-id".equals(kv.getKey())){
				KeyValue.Builder kvb = KeyValue.newBuilder();
				kvb.setKey(kv.getKey());
				kvb.setValue(kv.getValue());
				builder.addExtId(kvb.build());
				logger.info("<k:"+kv.getKey()+"; v:"+kv.getValue()+">");
				logger.info("<kc:"+kvb.getKey()+"; vc:"+kvb.getValue()+">");
			}
		}
		Author ret = builder.build();
		logger.info("<auth:"+ret.toString()+">");
		return ret;
	}

	private boolean equalsIgnoreCaseIgnoreDiacritics(String firstName,
            String secondName) {
        if (firstName.isEmpty() || secondName.isEmpty()) {
            return false;
        }
        return DiacriticsRemover.removeDiacritics(firstName).equalsIgnoreCase(
                DiacriticsRemover.removeDiacritics(secondName));
    }
}
