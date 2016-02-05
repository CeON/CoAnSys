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

package pl.edu.icm.coansys.disambiguation.author.pig;

import java.util.Map;

import org.apache.hadoop.mapreduce.Counter;
import org.apache.pig.backend.executionengine.ExecException;
import org.apache.pig.data.Tuple;
import org.apache.pig.tools.pigstats.PigStatusReporter;
import org.slf4j.LoggerFactory;

import pl.edu.icm.coansys.commons.java.StackTraceExtractor;

/**
 * Verify that the author may be similar (comparable) to someone by checking
 * whether there is a minimum number of features.
 * 
 * @author mwos
 */
public class FeaturesCheck extends AND<Boolean> {

	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(FeaturesCheck.class);
	// benchmark
	private boolean isStatistics = false;
	private static int skipedContribCounter = 0, allContribCounter = 0;

	/**
	 * @param Tuple
	 *            input with cid, sname, map with features
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 * @throws Exception
	 * @throws ExecException
	 * @returns String UUID
	 */

	public FeaturesCheck(String threshold, String featureDescription,
			String useIdsForExtractors, String printStatistics)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		super(logger, threshold, featureDescription, useIdsForExtractors);
		this.isStatistics = Boolean.parseBoolean(printStatistics);
	}

	@Override
	public Boolean exec(Tuple input) {
		// instance of reporter may change in each exec(...) run
		myreporter = PigStatusReporter.getInstance();
		
		if (input == null || input.size() == 0) {
			reportContrib(REPORTER_CONST.EMPTY);
			return false;
		}

		String cid = null, sname = null;
		Map<String, Object> featuresMap = null;

		try {
			cid = input.get(0).toString();
			sname = input.get(1).toString();
			featuresMap = (Map<String, Object>) input.get(2);
		} catch (ExecException e) {
			// Throwing an exception would cause the task to fail.
			logger.error("Caught exception processing input row:\n"
					+ StackTraceExtractor.getStackTrace(e));
			reportContrib(REPORTER_CONST.EMPTY);
			return false;
		}

		if (cid == null || sname == null || featuresMap == null
				|| cid.isEmpty() || sname.isEmpty()) {
			logger.info("Skipping " + (++skipedContribCounter) + " / "
					+ allContribCounter + " contrib: cid = " + cid
					+ ", sname = " + sname
					+ ". Cid or sname or feature map with null value.");
			reportContrib(REPORTER_CONST.EMPTY);
			return false;
		}

		allContribCounter++;

		double simil = getThreshold();
		for (int d = 0; d < features.length; d++) {
			// Taking features from each keys (name of extractor = feature name)
			// In contribsT.get(i) there is map we need.
			// From this map (collection of i'th contributor's features)
			// we take Bag with value of given feature.
			// Here we have sure that following Object = DateBag.

			Object o = featuresMap.get(featureInfos[d]
					.getFeatureExtractorName());

			// probably feature does not exist
			if (o == null) {
				continue;
			}

			simil += calculateAffinity(o, o, d);

			// contributor is similar to himself so maybe comparable to other
			// contributors
			if (simil >= 0) {
				reportContrib(REPORTER_CONST.SIMILAR);
				return true;
			}
		}

		if (isStatistics) {
			logger.info("Skipping " + (++skipedContribCounter) + " / "
					+ allContribCounter + " contrib: cid = " + cid
					+ ", sname = " + sname + ". Not enough features.");
		}

		reportContrib(REPORTER_CONST.DISIMILAR);
		return false;
	}

	// Pig Status Reporter staff:
	static class REPORTER_CONST {
		public static final String EMPTY = "Some info null or empty";
		public static final String DISIMILAR = "Disimilar to themselves";
		public static final String SIMILAR = "Similar to themselves";
	}
	
	private void reportContrib(String which) {
		if ( myreporter == null ) {
			return;
		}
		Counter counter = myreporter.getCounter("Contributors", which);
		
		if (counter != null) {
			counter.increment(1);
		}
	}
}