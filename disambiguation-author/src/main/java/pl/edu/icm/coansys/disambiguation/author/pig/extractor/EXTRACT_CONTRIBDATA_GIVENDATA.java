/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.disambiguation.author.pig.extractor;

import java.io.IOException;
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
import pl.edu.icm.coansys.disambiguation.author.pig.normalizers.PigNormalizer;
import pl.edu.icm.coansys.disambiguation.author.pig.normalizers.ToEnglishLowerCase;
import pl.edu.icm.coansys.disambiguation.features.FeatureInfo;
import pl.edu.icm.coansys.models.DocumentProtos.Author;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper;

/**
 *
 * @author pdendek
 */
public class EXTRACT_CONTRIBDATA_GIVENDATA extends EvalFunc<DataBag> {

    private static final Logger logger = LoggerFactory.getLogger(EXTRACT_CONTRIBDATA_GIVENDATA.class);
    private DisambiguationExtractor[] des = null;
    private String language = null;
    private PigNormalizer normalizer = new ToEnglishLowerCase();
    
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
    		ClassNotFoundException, InstantiationException, IllegalAccessException {
        
    	List<FeatureInfo> features = FeatureInfo.parseFeatureInfoString( featureinfo );
        des = new DisambiguationExtractor[features.size()];

        for ( int i = 0; i < features.size(); i++ ){
            Class<?> c = Class.forName("pl.edu.icm.coansys.disambiguation.author.pig.extractor." 
            		+ features.get(i).getFeatureExtractorName());
            des[i] = (DisambiguationExtractor) c.newInstance();
        }
    }
    
    public EXTRACT_CONTRIBDATA_GIVENDATA( String featureinfo ) throws 
    		ClassNotFoundException, InstantiationException, IllegalAccessException {
    	setDisambiguationExtractor( featureinfo );
    }
    
    public EXTRACT_CONTRIBDATA_GIVENDATA( String featureinfo, String lang ) throws 
    		ClassNotFoundException, InstantiationException, IllegalAccessException {
    	setDisambiguationExtractor( featureinfo );
    	language = lang;
    }
    
    public EXTRACT_CONTRIBDATA_GIVENDATA() throws ClassNotFoundException, 
    		InstantiationException, IllegalAccessException {
        des = new DisambiguationExtractor[1];
        Class<?> c = Class.forName("pl.edu.icm.coansys.disambiguation.author.pig.extractor.EX_TITLE");
        des[0] = (DisambiguationExtractor) c.newInstance();
    }

    @Override
    public DataBag exec(Tuple input) throws IOException {

        if (input == null || input.size() == 0) {
            return null;
        }

        try {
            DataByteArray dba = (DataByteArray) input.get( 0 );

            DocumentWrapper dw = DocumentWrapper.parseFrom(dba.get());

            //metadata
            DocumentMetadata dm = dw.getDocumentMetadata();

            //result bag with tuples, which describes each contributor
            DataBag ret = new DefaultDataBag();

            //author list
            List<Author> authors =
                    dm.getBasicMetadata().getAuthorList();
            
            //so far result objects have contained only data, which describes documents
            //in future we will need to get data involving author's data (e.g. 
            //email, institution, etc...). Probably we will need one more 'for'
            Object[] retObj = new Object[des.length];
            
            if ( language != null 
            		&& !language.equalsIgnoreCase("all") 
            		&& !language.equalsIgnoreCase("null")
            		&& !language.equals("") ) {
            	for ( int i = 0; i < des.length; i++ ){
            		retObj[i] = des[ i ].extract( dm, language );
            		if ( retObj[i] == null ) {
                        logger.info("Uncomplete or no metadata IN GIVEN LANG (" 
                        		+ language + "). Ignoring document with key: \"" 
                        		+ dm.getKey() + "\"!");
                        return null;
            		}
            	}
        	}
            else {
            	for ( int i = 0; i < des.length; i++ ) {
            		retObj[i] = des[ i ].extract( dm );
            	}
            }
            
            //adding to map extractor name and features' data, which we got above
            Map<String, Object> map = new HashMap<String, Object>();
            for ( int i = 0; i < des.length; i++ ){
                map.put( des[i].getClass().getSimpleName(), retObj[i] );
            }

            //bag making tuples (one tuple for one contributor from document)
            //with replicated metadata for
            for ( int i = 0; i < authors.size(); i++ ) {
            	String sname = normalizer.normalize( authors.get( i ).getSurname() );
                String cId = authors.get( i ).getKey();
                Object[] to = new Object[]{ cId, i, sname, map };
                Tuple t = TupleFactory.getInstance().newTuple(Arrays.asList( to ));
                ret.add( t );
            }

            return ret;

        } catch (Exception e) {
            logger.error("Error in processing input row:", e);
            throw new IOException("Caught exception processing input row:\n"
                    + StackTraceExtractor.getStackTrace(e));
        }
    }
}
