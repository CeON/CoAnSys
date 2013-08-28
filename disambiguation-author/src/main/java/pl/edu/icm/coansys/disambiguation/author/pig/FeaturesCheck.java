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


import org.apache.pig.EvalFunc;
import org.apache.pig.backend.executionengine.ExecException;
import org.apache.pig.data.Tuple;
import org.slf4j.LoggerFactory;

import pl.edu.icm.coansys.commons.java.StackTraceExtractor;
import pl.edu.icm.coansys.disambiguation.author.features.disambiguators.DisambiguatorFactory;
import pl.edu.icm.coansys.disambiguation.author.pig.extractor.DisambiguationExtractorFactory;
import pl.edu.icm.coansys.disambiguation.features.Disambiguator;
import pl.edu.icm.coansys.disambiguation.features.FeatureInfo;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Verify that the author may be similar (comparable) to someone 
 * by checking whether there is a minimum number of features.
 * 
 * @author mwos
 */
public class FeaturesCheck extends EvalFunc< Boolean > {

	private PigDisambiguator[] features;
	private FeatureInfo[] featureInfos;
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(FeaturesCheck.class);
    private float threshold;
	private boolean useIdsForExtractors;
	private String printStatistics;
	private boolean isStatistics;
    private DisambiguationExtractorFactory extrFactory = new DisambiguationExtractorFactory();

	/**
     * @param Tuple input with cid, sname, map with features
	 * @throws Exception 
     * @throws ExecException 
     * @returns String UUID
     */
	
	public FeaturesCheck( String threshold, String featureDescription, String useIdsForExtractors, String printStatistics ) throws Exception {
		this.threshold = Float.parseFloat(threshold);
		this.useIdsForExtractors = Boolean.parseBoolean( useIdsForExtractors );
		this.isStatistics = Boolean.parseBoolean( printStatistics );
		
		List<FeatureInfo> FIwithEmpties 
			= FeatureInfo.parseFeatureInfoString(featureDescription);
		List<FeatureInfo> FIFinall = new LinkedList<FeatureInfo>();
		List<PigDisambiguator> FeaturesFinall = new LinkedList<PigDisambiguator>();
		
        DisambiguatorFactory ff = new DisambiguatorFactory();
        Disambiguator d;
        
        //separate features which are fully described and able to use
        for ( FeatureInfo fi : FIwithEmpties ) {
            if ( fi.getDisambiguatorName().equals("") ) {
                //creating default disambugiator
            	d = new Disambiguator();
            	logger.info("Empty disambiguator name. Creating default disambiguator for this feature.");
            }
            if ( fi.getFeatureExtractorName().equals("") ) {
            	logger.error("Empty extractor name in feature info. Leaving this feature.");
            	throw new Exception("Empty extractor name.");
            	//continue;
            }
            d = ff.create( fi );
            if ( d == null ) {
            	//creating default disambugiator
            	//d = new Disambiguator();
            	logger.error("Cannot create disambugiator from given feature info.");
            	throw new Exception("Cannot create disambugiator from given feature info.");
            }
			//wrong max value (would cause dividing by zero)
			if ( fi.getMaxValue() == 0 ){
				logger.warn( "Incorrect max value for feature: " + fi.getFeatureExtractorName() + ". Max value cannot equal 0." );
				throw new Exception("Incorrect max value for feature: " + fi.getFeatureExtractorName() + ". Max value cannot equal 0.");
			}
            
			if ( this.useIdsForExtractors ) {
				fi.setFeatureExtractorName( 
						extrFactory.toExId( fi.getFeatureExtractorName() ) );
			}
			
            FIFinall.add( fi );
            FeaturesFinall.add( new PigDisambiguator( d ) );
        }
        
		this.featureInfos = FIFinall.toArray( new FeatureInfo[ FIFinall.size() ] );
        this.features = 
        		FeaturesFinall.toArray( new PigDisambiguator[ FIFinall.size() ] );
	}
	
	@Override
	public Boolean exec( Tuple input ) {
		
		if ( input == null || input.size() == 0 ) return null;
		
		String cid = null, sname = null;
		Map<String,Object> featuresMap = null;
		
		try {
			cid = input.get(0).toString();
			sname = input.get(1).toString();
			featuresMap = (Map<String, Object>) input.get(2);
		} catch (ExecException e) {
			// Throwing an exception would cause the task to fail.
			logger.error("Caught exception processing input row:\n" 
						+ StackTraceExtractor.getStackTrace(e));
			return null;
		}

		
		if ( cid == null || sname == null || featuresMap == null ) {
			return null;
		}
		
		double partial = 0, simil = threshold;
		for (int d = 0; d < features.length; d++) {
			// Taking features from each keys (name of extractor = feature name)
			// In contribsT.get(i) there is map we need.
			// From this map (collection of i'th contributor's features)
			// we take Bag with value of given feature.
			// Here we have sure that following Object = DateBag.


			Object o = featuresMap.get(featureInfos[d].getFeatureExtractorName());

			// probably feature does not exist 
			if (o == null ) {
				continue;
			}

			partial = features[d].calculateAffinity(o, o);

			partial = partial / featureInfos[d].getMaxValue()
					* featureInfos[d].getWeight();

			simil += partial;

			//contributor is similar to himself so maybe comparable to other contributors
			if ( simil >= 0 ) {
				return true;
			}
		}
		
		return false;
	}

}
