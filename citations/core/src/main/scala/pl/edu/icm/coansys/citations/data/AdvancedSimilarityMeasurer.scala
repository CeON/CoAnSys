/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.data

import feature_calculators._
import pl.edu.icm.coansys.citations.util.classification.features.FeatureVectorBuilder
import pl.edu.icm.coansys.citations.util.classification.svm.SvmClassifier

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class AdvancedSimilarityMeasurer {
  val featureVectorBuilder = new FeatureVectorBuilder(List(
    AuthorMatchFactor,
    AuthorTrigramMatchFactor,
    AuthorTokenMatchFactor,
    PagesMatchFactor,
    PagesRawTextMatchFactor,
    SourceMatchFactor,
    SourceRawTextMatchFactor,
    TitleMatchFactor,
    TitleTokenMatchFactor,
    YearMatchFactor,
    YearRawTextMatchFactor))

  val classifier = SvmClassifier.fromResource("/pl/edu/icm/coansys/citations/pubmedMatchingBetter.model")

  def similarity(e1: MatchableEntity, e2: MatchableEntity): Double =
    classifier.predictProbabilities(featureVectorBuilder.calculateFeatureVectorValues((e1, e2)))(1)
}


