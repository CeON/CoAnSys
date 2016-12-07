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

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.hadoop.mapreduce.Counter;
import org.apache.pig.EvalFunc;
import org.apache.pig.tools.pigstats.PigStatusReporter;
import pl.edu.icm.coansys.disambiguation.author.features.disambiguators.CosineSimilarity;

import pl.edu.icm.coansys.disambiguation.author.features.disambiguators.Disambiguator;
import pl.edu.icm.coansys.disambiguation.author.features.disambiguators.DisambiguatorFactory;
import pl.edu.icm.coansys.disambiguation.author.features.disambiguators.IntersectionPerSum;
import pl.edu.icm.coansys.disambiguation.author.features.extractors.DisambiguationExtractorFactory;
import pl.edu.icm.coansys.disambiguation.features.FeatureInfo;

public abstract class AND<T> extends EvalFunc<T> {

	private float threshold;

	protected PigDisambiguator[] features;
	protected FeatureInfo[] featureInfos;
	protected org.slf4j.Logger logger = null;
	protected PigStatusReporter myreporter = null;

	private Disambiguator defaultDisambiguator = new IntersectionPerSum();

	protected float getThreshold() {
		return threshold;
	}

    static DisambiguationExtractorFactory staticDisambigFactory=null;
   
    
	// benchmark staff
	public AND(org.slf4j.Logger logger, String threshold,
			String featureDescription, String useIdsForExtractorsStr)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		this.logger = logger;
		this.threshold = Float.parseFloat(threshold);
		boolean useIdsForExtractors = Boolean.parseBoolean(useIdsForExtractorsStr);

		List<FeatureInfo> FIwithEmpties = FeatureInfo
				.parseFeatureInfoString(featureDescription);
		List<FeatureInfo> FIFinall = new LinkedList<FeatureInfo>();
		List<PigDisambiguator> FeaturesFinall = new LinkedList<PigDisambiguator>();

        synchronized (this.getClass()) {
            if (staticDisambigFactory==null) {
                staticDisambigFactory=new DisambiguationExtractorFactory();
            }
        }
        
		DisambiguationExtractorFactory extrFactory = staticDisambigFactory;
		DisambiguatorFactory ff = new DisambiguatorFactory();
		Disambiguator d;

		// separate features which are fully described and able to use
		for (FeatureInfo fi : FIwithEmpties) {
			if (fi.getFeatureExtractorName().equals("")) {
				logger.error("Empty extractor name in feature info. Leaving this feature.");
				throw new IllegalArgumentException("Empty extractor name.");
			}
			if (fi.getDisambiguatorName().equals("")) {
				// giving default disambiguator
				d = defaultDisambiguator;
				logger.info("Empty disambiguator name. Creating default disambiguator for this feature.");
			} else if ((d = ff.create(fi)) == null) {
				String errMsg = "Cannot create disambugiator from given feature info (disambiguator name: "
						+ fi.getDisambiguatorName() + ")";
				logger.error(errMsg);
				throw new ClassNotFoundException(errMsg);
				// if you do not want to throw an exception, uncomment the
				// following creating default disambiguator: d = defaultDisambiguator;
			}

			if (useIdsForExtractors) {
				fi.setFeatureExtractorName(extrFactory.toExId(fi
						.getFeatureExtractorName()));
			}

			FIFinall.add(fi);
			FeaturesFinall.add(new PigDisambiguator(d));
		}

		this.featureInfos = FIFinall.toArray(new FeatureInfo[FIFinall.size()]);
		this.features = FeaturesFinall.toArray(new PigDisambiguator[FIFinall
				.size()]);
	}

    
    
    protected float calculateContribsAffinityForAllFeaturesaOnSortedLists(
			List<Map<String, CosineSimilarity.CosineSimilarityList>> contribsT, int indexA, int indexB,
			boolean breakWhenPositive) {
		Map<String, CosineSimilarity.CosineSimilarityList> mA, mB;
		double affinity = threshold;
		
		// Taking features from each keys (name of extractor = feature name)
		// In contribsT.get(i) there is map we need.
		// From this map (collection of i'th contributor's features)
		// we take Bag with value of given feature.
		// Here we have sure that following Object = DateBag.
		mA = contribsT.get(indexA);
		mB = contribsT.get(indexB);

		// probably map is empty for some contrib
		if (mA == null || mB == null) {
			return 0;
		}
		
		for (int d = 0; d < features.length; d++) {
			CosineSimilarity.CosineSimilarityList oA = mA.get(featureInfos[d].getFeatureExtractorName());
			CosineSimilarity.CosineSimilarityList oB = mB.get(featureInfos[d].getFeatureExtractorName());

			if (oA == null || oB == null) {
				continue;
			}
			
			double ca = calculateAffinitySorted(oA.getOrigList(), oB.getOrigList(),oA,oB, d);
			if ( Double.isNaN(ca) ) {
				throw new NumberFormatException("NaN result of calculated affinity. Check disambiguator (meybe 0 * inf?).");
			}
			affinity += ca;

			if (affinity >= 0 && breakWhenPositive) {
				// because we do not remember sim values this time
				// we can break calculations
				break;
			}
		}
		return (float) affinity;
	}
    
    
    
	protected float calculateContribsAffinityForAllFeatures(
			List<Map<String, Object>> contribsT, int indexA, int indexB,
			boolean breakWhenPositive) {
		Map<String, Object> mA, mB;
		double affinity = threshold;
		
		// Taking features from each keys (name of extractor = feature name)
		// In contribsT.get(i) there is map we need.
		// From this map (collection of i'th contributor's features)
		// we take Bag with value of given feature.
		// Here we have sure that following Object = DateBag.
		mA = contribsT.get(indexA);
		mB = contribsT.get(indexB);

		// probably map is empty for some contrib
		if (mA == null || mB == null) {
			return 0;
		}
		
		for (int d = 0; d < features.length; d++) {
			Object oA = mA.get(featureInfos[d].getFeatureExtractorName());
			Object oB = mB.get(featureInfos[d].getFeatureExtractorName());

			if (oA == null || oB == null) {
				continue;
			}
			
			double ca = calculateAffinity(oA, oB, d);
			if ( Double.isNaN(ca) ) {
				throw new NumberFormatException("NaN result of calculated affinity. Check disambiguator (meybe 0 * inf?).");
			}
			affinity += ca;

			if (affinity >= 0 && breakWhenPositive) {
				// because we do not remember sim values this time
				// we can break calculations
				break;
			}
		}
		return (float) affinity;
	}

	protected double calculateAffinity(Object featureDescriptionA,
			Object featureDescriptionB, int featureIndex) {

		return features[featureIndex].calculateAffinity(featureDescriptionA,
				featureDescriptionB);
	}

    protected double calculateAffinitySorted(List<Integer> featureDescriptionA,
			List<Integer> featureDescriptionB,CosineSimilarity.CosineSimilarityList l1, CosineSimilarity.CosineSimilarityList l2, int featureIndex) {

		return features[featureIndex].calculateAffinitySorted(featureDescriptionA,
				featureDescriptionB,l1,l2);
	}
    
    
	protected void pigReporterSizeInfo(String blockName, long l) {

		if ( myreporter == null ) {
			return;
		}
		// 6627 is limit for exhaustive contributors block size input
		// DESC order required!
		int periodStarts[] = { 5001, 1 };

		for (int start : periodStarts) {
			if (l >= start) {
				Counter c = myreporter.getCounter(blockName, "Size from "
						+ Integer.toString(start));
				if (c == null) {
					return;
				}
				c.increment(1);
			}
		}
	}
}
