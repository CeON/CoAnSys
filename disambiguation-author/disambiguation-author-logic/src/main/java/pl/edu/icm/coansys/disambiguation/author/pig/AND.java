package pl.edu.icm.coansys.disambiguation.author.pig;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.pig.EvalFunc;

import pl.edu.icm.coansys.disambiguation.author.features.disambiguators.DisambiguatorFactory;
import pl.edu.icm.coansys.disambiguation.author.pig.extractor.DisambiguationExtractorFactory;
import pl.edu.icm.coansys.disambiguation.features.Disambiguator;
import pl.edu.icm.coansys.disambiguation.features.FeatureInfo;

public abstract class AND<T> extends EvalFunc<T> {

	protected float threshold;
	protected PigDisambiguator[] features;
	protected FeatureInfo[] featureInfos;

	protected org.slf4j.Logger logger = null;
	private DisambiguationExtractorFactory extrFactory = new DisambiguationExtractorFactory();
	private boolean useIdsForExtractors = false;

	// private float sim[][];
	// private Tuple datain[];
	// private int N;

	// benchmark staff
	/*
	 * protected boolean isStatistics = false; private TimerSyso timer = new
	 * TimerSyso(); private int calculatedSimCounter; private int timerPlayId =
	 * 0; private int finalClusterNumber = 0; private
	 * List<Integer>clustersSizes;
	 */

	public AND(org.slf4j.Logger logger, String threshold,
			String featureDescription, String useIdsForExtractors)
			throws Exception {
		this.logger = logger;
		this.threshold = Float.parseFloat(threshold);
		this.useIdsForExtractors = Boolean.parseBoolean(useIdsForExtractors);

		List<FeatureInfo> FIwithEmpties = FeatureInfo
				.parseFeatureInfoString(featureDescription);
		List<FeatureInfo> FIFinall = new LinkedList<FeatureInfo>();
		List<PigDisambiguator> FeaturesFinall = new LinkedList<PigDisambiguator>();

		DisambiguatorFactory ff = new DisambiguatorFactory();
		Disambiguator d;

		// separate features which are fully described and able to use
		for (FeatureInfo fi : FIwithEmpties) {
			if (fi.getFeatureExtractorName().equals("")) {
				logger.error("Empty extractor name in feature info. Leaving this feature.");
				throw new Exception("Empty extractor name.");
				// continue;
			}
			if (fi.getDisambiguatorName().equals("")) {
				// creating default disambiguator
				d = new Disambiguator();
				logger.info("Empty disambiguator name. Creating default disambiguator for this feature.");
			} else if ((d = ff.create(fi)) == null) {
				// creating default disambiguator
				d = new Disambiguator();
				logger.error("Cannot create disambugiator from given feature info.");
				throw new Exception(
						"Cannot create disambugiator from given feature info.");
			}
			// wrong max value (would cause dividing by zero)
			if (fi.getMaxValue() == 0) {
				logger.warn("Incorrect max value for feature: "
						+ fi.getFeatureExtractorName()
						+ ". Max value cannot equal 0.");
				throw new Exception("Incorrect max value for feature: "
						+ fi.getFeatureExtractorName()
						+ ". Max value cannot equal 0.");
			}

			if (this.useIdsForExtractors) {
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

	protected float calculateContribsAffinityForAllFeatures(
			List<Map<String, Object>> contribsT, int indexA, int indexB,
			boolean breakWhenPositive) {
		Map<String, Object> mA, mB;
		double affinity = threshold;

		for (int d = 0; d < features.length; d++) {
			// Taking features from each keys (name of extractor = feature name)
			// In contribsT.get(i) there is map we need.
			// From this map (collection of i'th contributor's features)
			// we take Bag with value of given feature.
			// Here we have sure that following Object = DateBag.
			mA = contribsT.get(indexA);
			mB = contribsT.get(indexB);

			// probably map is empty for some contrib
			if (mA == null || mB == null) {
				continue;
			}

			Object oA = mA.get(featureInfos[d].getFeatureExtractorName());
			Object oB = mB.get(featureInfos[d].getFeatureExtractorName());

			if (oA == null || oB == null) {
				continue;
			}

			affinity += calculateAffinity(oA, oB, d);

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
		double partial = features[featureIndex].calculateAffinity(
				featureDescriptionA, featureDescriptionB);

		partial = partial
				/ featureInfos[featureIndex].getMaxValue()
				* featureInfos[featureIndex].getWeight();

		return partial;
	}

}
