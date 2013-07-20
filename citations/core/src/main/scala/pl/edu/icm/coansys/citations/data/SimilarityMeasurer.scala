/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.data

import collection.JavaConversions._
import feature_calculators._
import pl.edu.icm.cermine.tools.classification.svm.SVMClassifier
import pl.edu.icm.cermine.tools.classification.features.FeatureVectorBuilder
import pl.edu.icm.coansys.citations.util.SvmClassifier

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class SimilarityMeasurer {
  val featureVectorBuilder = List(
    AuthorTrigramMatchFactor,
    AuthorTokenMatchFactor,
    PagesMatchFactor,
    SourceMatchFactor,
    TitleMatchFactor,
    YearMatchFactor)

  val classifier = SvmClassifier.fromResource("/pl/edu/icm/coansys/citations/weakMatching.model")

  def similarity(e1: MatchableEntity, e2: MatchableEntity): Double =
    classifier.predictProbabilities(featureVectorBuilder.map(_.calculateFeatureValue(e1, e2)).toArray)(1)
}

object SimilarityMeasurer {
  def main(args: Array[String]) {
    val measurer = new SimilarityMeasurer
    val doc1 = MatchableEntity.fromParameters("1", "Jan Kowalski", "J. App. Phis.", "Some random title", "120-126", "2010")
    val doc2 = MatchableEntity.fromParameters("2", "Jan Kowalski", "J. App. Phis.", "Totally different title", "32-36", "2010")
    val doc3 = MatchableEntity.fromParameters("3", "Zbigniew Nowak", "Przegląd leśniczy", "Inny tytuł", "15-20", "1995")
    println(measurer.similarity(doc1, doc1))
    println(measurer.similarity(doc1, doc2))
    println(measurer.similarity(doc1, doc3))

  }
}
