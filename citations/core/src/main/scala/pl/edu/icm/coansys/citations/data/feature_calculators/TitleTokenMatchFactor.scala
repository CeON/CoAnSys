/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.data.feature_calculators

import pl.edu.icm.coansys.citations.data.MatchableEntity
import java.util.Locale
import pl.edu.icm.coansys.citations.util.misc._
import pl.edu.icm.coansys.citations.util.classification.features.FeatureCalculator

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object TitleTokenMatchFactor extends FeatureCalculator[(MatchableEntity, MatchableEntity)] {
  def calculateValue(entities: (MatchableEntity, MatchableEntity)): Double = {
    val tokens1 = tokensFromCermine(entities._1.title.toLowerCase(Locale.ENGLISH))
    val tokens2 = tokensFromCermine(entities._2.title.toLowerCase(Locale.ENGLISH))
    val counts1 = tokens1.map(_.toLowerCase).groupBy(identity).mapValues(_.length)
    val counts2 = tokens2.map(_.toLowerCase).groupBy(identity).mapValues(_.length)
    val common = (counts1.keySet & counts2.keySet).toIterator.map(k => counts1(k) min counts2(k)).sum

    if ((tokens1.length min tokens2.length) > 0)
      common.toDouble / (tokens1.length min tokens2.length)
    else
      0.0
  }
}
