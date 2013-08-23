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
package pl.edu.icm.coansys.disambiguation.author.pig.extractor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DataByteArray;
import org.apache.pig.data.DataType;
import org.apache.pig.data.DefaultDataBag;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.impl.logicalLayer.FrontendException;
import org.apache.pig.impl.logicalLayer.schema.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.icm.coansys.commons.java.StackTraceExtractor;
import pl.edu.icm.coansys.disambiguation.features.FeatureInfo;
import pl.edu.icm.coansys.models.DocumentProtos.Author;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper;

/**
 *
 * @author pdendek
 * @author mwos
 */
public class EXTRACT_CONTRIBDATA_GIVENDATA extends EvalFunc<DataBag> {

    private static final Logger logger = LoggerFactory.getLogger(EXTRACT_CONTRIBDATA_GIVENDATA.class);
    private List< DisambiguationExtractorDocument> des4Doc = new ArrayList< DisambiguationExtractorDocument>();
    private List< DisambiguationExtractorAuthor> des4Author = new ArrayList< DisambiguationExtractorAuthor>();
    private String language = null;
    private boolean skipEmptyFeatures = false; 

    @Override
    public Schema outputSchema(Schema p_input) {
        try {
            return Schema.generateNestedSchema(DataType.BAG);
        } catch (FrontendException e) {
            logger.error("Error in creating output schema:", e);
            throw new IllegalStateException(e);
        }
    }
    
    private void setDisambiguationExtractor( String featureinfo ) throws 
    	Exception {
        
    	List<FeatureInfo> features = FeatureInfo.parseFeatureInfoString( featureinfo );
        
        String ExtractorDocClassName = new DisambiguationExtractorDocument().getClass().getSimpleName();
        String ExtractorAuthorClassName = new DisambiguationExtractorAuthor().getClass().getSimpleName();
        
        for ( int i = 0; i < features.size(); i++ ){
        	String currentClassName = "pl.edu.icm.coansys.disambiguation.author.pig.extractor." 
					+ features.get(i).getFeatureExtractorName();
        	
        	// creating extractor with given name
        	Class<?> c = null;
			try {
				c = Class.forName( currentClassName );
			} catch (ClassNotFoundException e) {
				//e.printStackTrace();
				String m = "Cannot find class for create: " + currentClassName;
				logger.error( m + StackTraceExtractor.getStackTrace(e) );
				throw new ClassNotFoundException( m, e );
			}
            
			// recognition of extractor type
            String currentSuperClassName = c.getSuperclass().getSimpleName();
            
            try {
	            if ( currentSuperClassName.equals( ExtractorDocClassName ) ) {
	            	des4Doc.add( (DisambiguationExtractorDocument) c.newInstance() );
	            } else if ( currentSuperClassName.equals( ExtractorAuthorClassName ) ) {
	            	des4Author.add( (DisambiguationExtractorAuthor) c.newInstance() );
	            } else {
	            	String m = "Cannot create extractor: " 
	            			+ c.getSimpleName() + ". Its superclass: " 
	            			+ currentSuperClassName + " does not match to any superclass.";
	            	throw new Exception( m );
	            }
            } catch( Exception e ) {
            	logger.error( StackTraceExtractor.getStackTrace(e) );
            	throw e;
            }
        }
    }
    
    public EXTRACT_CONTRIBDATA_GIVENDATA( String featureinfo ) throws 
    		Exception {
    	setDisambiguationExtractor( featureinfo );
    }
    
    public EXTRACT_CONTRIBDATA_GIVENDATA( String featureinfo, String lang ) throws 
    		Exception {
    	setDisambiguationExtractor( featureinfo );
    	language = lang;
    }
    
    public EXTRACT_CONTRIBDATA_GIVENDATA( String featureinfo, String lang, String skipEmptyFeatures ) throws 
    		Exception {
    	setDisambiguationExtractor( featureinfo );
    	language = lang;
    	this.skipEmptyFeatures = Boolean.parseBoolean( skipEmptyFeatures );
    }
    
    private boolean checkLanguage() {
        return (language != null
                && !language.equalsIgnoreCase("all")
                && !language.equalsIgnoreCase("null")
                && !language.equals(""));
    }

    @Override
    public DataBag exec(Tuple input) throws IOException {

        if (input == null || input.size() == 0) {
            return null;
        }

        try {
            DataByteArray dba = (DataByteArray) input.get(0);

            DocumentWrapper dw = DocumentWrapper.parseFrom(dba.get());
            dba = null;

            //metadata
            DocumentMetadata dm = dw.getDocumentMetadata();
            dw = null;

            //result bag with tuples, which des4Doccribes each contributor
            DataBag ret = new DefaultDataBag();

            //author list
            List<Author> authors =
                    dm.getBasicMetadata().getAuthorList();

            //in arrays we are storing DataBags from extractors
            DataBag[] extractedDocObj = new DataBag[des4Doc.size()];
            DataBag[] extractedAuthorObj;
            Map<String, DataBag> map = new HashMap<String, DataBag>();
            Map<String, DataBag> finalMap;


            if (checkLanguage()) {
                for (int i = 0; i < des4Doc.size(); i++) {
                    extractedDocObj[i] = des4Doc.get(i).extract(dm, language);
                }
            } else {
                for (int i = 0; i < des4Doc.size(); i++) {
                    extractedDocObj[i] = des4Doc.get(i).extract(dm);
                }
            }

            //adding to map extractor name and features' data
            for (int i = 0; i < des4Doc.size(); i++) {
                if ( extractedDocObj[i] == null ) {
                    continue;
                }
                if ( extractedDocObj[i].size() == 0 && skipEmptyFeatures ) {
                    continue;
                }
                map.put(des4Doc.get(i).getClass().getSimpleName(), extractedDocObj[i]);
            }
            extractedDocObj = null;


            //bag making tuples (one tuple for one contributor from document)
            //with replicated metadata for
            for (int i = 0; i < authors.size(); i++) {
                String sname = authors.get(i).getSurname();

                //here we have sure that Object = Integer
                Object normalizedSname =
                        DisambiguationExtractor.normalizeExtracted(sname);
                String cId = authors.get(i).getKey();

                finalMap = new HashMap<String, DataBag>(map);

                //put author metadata into finalMap
                extractedAuthorObj = new DataBag[des4Author.size()];
                if (checkLanguage()) {
                    for (int j = 0; j < des4Author.size(); j++) {
                        extractedAuthorObj[j] = des4Author.get(j).extract(dm, i, language);
                    }
                } else {
                    for (int j = 0; j < des4Author.size(); j++) {
                        extractedAuthorObj[j] = des4Author.get(j).extract(dm, i);
                    }
                }

                //adding to map extractor name and features' data
                for (int j = 0; j < des4Author.size(); j++) {
                    if (extractedAuthorObj[j] == null) {
                        continue;
                    }
                    if ( extractedAuthorObj[i].size() == 0 && skipEmptyFeatures ) {
                        continue;
                    }
                    finalMap.put(des4Author.get(j).getClass().getSimpleName(), extractedAuthorObj[j]);
                }
                extractedAuthorObj = null;


                Object[] to = new Object[]{cId, normalizedSname, finalMap};
                Tuple t = TupleFactory.getInstance().newTuple(Arrays.asList(to));
                ret.add(t);
            }
            map = null;
            dm = null;

            return ret;

        } catch (Exception e) {
            logger.error("Error in processing input row:", e);
            throw new IOException("Caught exception processing input row:\n"
                    + StackTraceExtractor.getStackTrace(e));
        }
    }
}
