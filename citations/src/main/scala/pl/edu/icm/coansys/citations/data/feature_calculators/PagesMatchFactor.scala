/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.data.feature_calculators

import pl.edu.icm.cermine.tools.classification.features.FeatureCalculator
import pl.edu.icm.coansys.citations.data.Entity
import pl.edu.icm.coansys.citations.util.misc

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object PagesMatchFactor extends FeatureCalculator[Entity, Entity] {
  def calculateFeatureValue(e1: Entity, e2: Entity) = {
    val pages1 = misc.extractNumbers(e1.pages)
    val pages2 = misc.extractNumbers(e2.pages)
    if (pages1.size + pages2.size > 0)
      2 * (pages1.toSet & pages2.toSet).size.toDouble / (pages1.size + pages2.size)
    else
      1.0
  }
}
