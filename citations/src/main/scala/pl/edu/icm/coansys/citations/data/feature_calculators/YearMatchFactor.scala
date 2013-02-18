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
object YearMatchFactor extends FeatureCalculator[Entity, Entity] {
  def calculateFeatureValue(e1: Entity, e2: Entity) =
    if (misc.extractYear(e1.year) == misc.extractYear(e2.year))
      1.0
    else
      0.0
}
