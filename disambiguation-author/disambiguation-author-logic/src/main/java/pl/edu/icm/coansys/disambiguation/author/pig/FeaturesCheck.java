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


import org.apache.pig.backend.executionengine.ExecException;
import org.apache.pig.data.Tuple;
import org.slf4j.LoggerFactory;

import pl.edu.icm.coansys.commons.java.StackTraceExtractor;
import java.util.Map;

/**
 * Verify that the author may be similar (comparable) to someone 
 * by checking whether there is a minimum number of features.
 * 
 * @author mwos
 */
public class FeaturesCheck extends AND<Boolean> {

	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(FeaturesCheck.class);
    //benchmark
	private boolean isStatistics = false;
    private static int skipedContribCounter = 0, allContribCounter = 0;
    
	
	/**
     * @param Tuple input with cid, sname, map with features
	 * @throws Exception 
     * @throws ExecException 
     * @returns String UUID
     */
	
	public FeaturesCheck( String threshold, String featureDescription, String useIdsForExtractors, String printStatistics ) throws Exception {
		super( logger, threshold, featureDescription, useIdsForExtractors );
		this.isStatistics = Boolean.parseBoolean( printStatistics );
	}
	
	@Override
	public Boolean exec( Tuple input ) {
		
		if ( input == null || input.size() == 0 ) {
			return false;
		}
		
		String cid = null, sname = null;
		Map<String,Object> featuresMap = null;
		
		try {
			cid = input.get(0).toString();
			sname = input.get(1).toString();
			featuresMap = (Map<String, Object>) input.get(2);
		} catch ( ExecException e ) {
			// Throwing an exception would cause the task to fail.
			logger.error("Caught exception processing input row:\n" 
						+ StackTraceExtractor.getStackTrace(e));
			return false;
		}

		
		if ( cid == null || sname == null || featuresMap == null ) {
			logger.info("Skipping " + (++skipedContribCounter) + " / " 
					+ allContribCounter + " contrib: cid = " 
					+ cid + ", sname = " + sname + ". Cid or sname or feature map with null value." );
			return false;
		}
		
		allContribCounter++;
		
		double partial = 0, simil = threshold;
		for ( int d = 0; d < features.length; d++ ) {
			// Taking features from each keys (name of extractor = feature name)
			// In contribsT.get(i) there is map we need.
			// From this map (collection of i'th contributor's features)
			// we take Bag with value of given feature.
			// Here we have sure that following Object = DateBag.


			Object o = featuresMap.get(featureInfos[d].getFeatureExtractorName());

			// probably feature does not exist 
			if ( o == null ) {
				continue;
			}

			partial = features[d].calculateAffinity( o, o );
			partial = partial * featureInfos[d].getWeight();

			simil += partial;

			//contributor is similar to himself so maybe comparable to other contributors
			if ( simil >= 0 ) {
				return true;
			}
		}
		
		if ( isStatistics ) {
			//System.out.println( "FCH\t" +  cid + "\t" + sname + "\t" );
			logger.info("Skipping " + (++skipedContribCounter) + " / " 
					+ allContribCounter + " contrib: cid = " 
					+ cid + ", sname = " + sname + ". Not enough features." );
		}
		
		return false;
	}
}












