/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.data.feature_calculators

import pl.edu.icm.cermine.tools.classification.features.FeatureCalculator
import pl.edu.icm.coansys.citations.data.MatchableEntity
import pl.edu.icm.coansys.citations.util.ngrams._

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object AuthorTrigramMatchFactor extends FeatureCalculator[MatchableEntity, MatchableEntity] {
  def calculateFeatureValue(e1: MatchableEntity, e2: MatchableEntity) =
    trigramSimilarity(e1.author, e2.author)
}
