/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.data.feature_calculators

import pl.edu.icm.cermine.tools.classification.features.FeatureCalculator
import pl.edu.icm.coansys.citations.data.MatchableEntity
import pl.edu.icm.coansys.citations.util.misc._

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object AuthorTokenMatchFactor extends FeatureCalculator[MatchableEntity, MatchableEntity] {
  def calculateFeatureValue(e1: MatchableEntity, e2: MatchableEntity) = {
    val tokens1 = tokensFromCermine(e1.author)
    val tokens2 = tokensFromCermine(e2.author)
    val counts1 = tokens1.map(_.toLowerCase).groupBy(identity).mapValues(_.length)
    val counts2 = tokens2.map(_.toLowerCase).groupBy(identity).mapValues(_.length)
    val common = (counts1.keySet & counts2.keySet).toIterator.map(k => counts1(k) min counts2(k)).sum
    val all = tokens1.length + tokens2.length
    if (all > 0)
      2 * common.toDouble / all
    else
      0.0
  }
}
