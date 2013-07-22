/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.data.feature_calculators

import pl.edu.icm.cermine.tools.classification.features.FeatureCalculator
import pl.edu.icm.coansys.citations.data.MatchableEntity
import pl.edu.icm.coansys.citations.util.misc
import pl.edu.icm.coansys.citations.util.misc._

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object YearRawTextMatchFactor extends FeatureCalculator[MatchableEntity, MatchableEntity] {
  def calculateFeatureValue(e1: MatchableEntity, e2: MatchableEntity) = {
    val year1 = misc.extractNumbers(e1.year)
    val year2 = misc.extractNumbers(e2.year)
    val yearRaw1 = misc.extractNumbers(e1.rawText.getOrElse(""))
    val yearRaw2 = misc.extractNumbers(e2.rawText.getOrElse(""))

    safeDiv((yearRaw2.toSet & year1.toSet).size, year1.size) max
      safeDiv((yearRaw1.toSet & year2.toSet).size, year2.size)
  }
}
