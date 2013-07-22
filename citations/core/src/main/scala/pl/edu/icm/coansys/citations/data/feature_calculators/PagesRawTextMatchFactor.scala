/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.data.feature_calculators

import pl.edu.icm.cermine.tools.classification.features.FeatureCalculator
import pl.edu.icm.coansys.citations.data.MatchableEntity
import pl.edu.icm.coansys.citations.util.misc
import pl.edu.icm.coansys.citations.util.misc.safeDiv

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object PagesRawTextMatchFactor extends FeatureCalculator[MatchableEntity, MatchableEntity] {


  def calculateFeatureValue(e1: MatchableEntity, e2: MatchableEntity) = {
    val pages1 = misc.extractNumbers(e1.pages)
    val pages2 = misc.extractNumbers(e2.pages)
    val pagesRaw1 = misc.extractNumbers(e1.rawText.getOrElse(""))
    val pagesRaw2 = misc.extractNumbers(e2.rawText.getOrElse(""))

    safeDiv((pagesRaw2.toSet & pages1.toSet).size, pages1.size) max
      safeDiv((pagesRaw1.toSet & pages2.toSet).size, pages2.size)
  }
}
