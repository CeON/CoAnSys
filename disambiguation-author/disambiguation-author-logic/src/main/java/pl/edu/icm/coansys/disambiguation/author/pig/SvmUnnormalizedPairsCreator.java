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
package pl.edu.icm.coansys.disambiguation.author.pig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.pig.EvalFunc;
import org.apache.pig.backend.executionengine.ExecException;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DataType;
import org.apache.pig.data.DefaultDataBag;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.impl.logicalLayer.FrontendException;
import org.apache.pig.impl.logicalLayer.schema.Schema;
import org.apache.pig.tools.pigstats.PigStatusReporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.icm.coansys.disambiguation.author.features.disambiguators.CosineSimilarity;
import pl.edu.icm.coansys.disambiguation.author.features.disambiguators.Disambiguator;
import pl.edu.icm.coansys.disambiguation.author.features.disambiguators.Intersection;

public class SvmUnnormalizedPairsCreator  extends EvalFunc<DataBag> {

	private static final Logger logger = LoggerFactory.getLogger(SvmUnnormalizedPairsCreator.class);
			
	private String[] featureNames = null;

	public SvmUnnormalizedPairsCreator(String maxValsString){
		String[] fns = maxValsString.split(",");
		featureNames = new String[fns.length];
		for(int i=0;i<fns.length;i++){
			featureNames[i] = fns[i].split("#")[1];
		}
	}
    
	@SuppressWarnings("boxing")
	@Override
	public DataBag exec(Tuple tuple) throws IOException {
		PigStatusReporter reporter = PigStatusReporter.getInstance();
		boolean reporterIsNotNull = reporter!=null;
		
		if(tuple == null || tuple.size() !=2){
			if(reporterIsNotNull){
				reporter.getCounter("data error", "input tuple is null or size is not equal to 2").increment(1);
			}
			return null;
		}
		
		// String sname = (String) tuple.get(0);
		DataBag contribs = (DataBag) tuple.get(1); 
		
		Tuple[] contribsT = new Tuple[(int) contribs.size()]; 
		
		int counter = 0;
		for(Tuple contrib : contribs){
			contribsT[counter] = contrib;
			counter++;
		}
		
		TupleFactory tf = TupleFactory.getInstance();
		
		Disambiguator intersectionDisambiguator = new Intersection();
		Disambiguator cosineSimDisambiguator = new CosineSimilarity();
		
		
		DataBag retBag = new DefaultDataBag();
		
		for(int i = 0; i<counter;i++){
			for(int j =i+1; j<counter;j++){
				Tuple cA = contribsT[i]; 
				Tuple cB = contribsT[j];
				
				String cidA = (String) cA.get(0);
				String snA = (String) cA.get(1);
				Map<String, DataBag> mapA = (Map<String, DataBag>) cA.get(2);
				Map<String,ArrayList<Object>> extractedMapA = extractFeatureNameFeatureValueList(mapA);
				
				String cidB = (String) cB.get(0);
				String snB = (String) cB.get(1);
				Map<String, DataBag> mapB = (Map<String, DataBag>) cB.get(2);
				Map<String,ArrayList<Object>> extractedMapB = extractFeatureNameFeatureValueList(mapB);

				Tuple t = tf.newTuple();
				t.append(UUID.nameUUIDFromBytes((cidA+cidB).getBytes("UTF-8")).toString());
				
				for(int k =0; k<featureNames.length;k++){
					List<Object> listA = extractedMapA.get(featureNames[k]);
					List<Object> listB = extractedMapB.get(featureNames[k]);
					
					t.append(featureNames[k]);
					t.append(intersectionDisambiguator.calculateAffinity(listA,listB));
					t.append(cosineSimDisambiguator.calculateAffinity(listA,listB));
				}
				retBag.add(t);
			}
		}
		return retBag;
	}
	
    private Map<String, ArrayList<Object>> extractFeatureNameFeatureValueList(
			Map<String, DataBag> mapA) throws ExecException {
    	HashMap<String,ArrayList<Object>> translated = new HashMap<String,ArrayList<Object>>();
    	for(int k = 0; k<featureNames.length;k++){
    		DataBag db = mapA.get(featureNames[k]);
    		ArrayList<Object> al = new ArrayList<Object>();
    		for(Tuple t : db){
    			Object tmp = t.get(0);
    			if(tmp.hashCode()!=0){
    				al.add(tmp);    				
    			}
    			
    		}
    		translated.put(featureNames[k], al);
    	}
		return translated;
	}

	@Override
    public Schema outputSchema(@SuppressWarnings("unused") Schema p_input) {
        try {
            return Schema.generateNestedSchema(DataType.TUPLE);
        } catch (FrontendException e) {
            logger.error("Error in creating output schema:", e);
            throw new IllegalStateException(e);
        }
    }
}
