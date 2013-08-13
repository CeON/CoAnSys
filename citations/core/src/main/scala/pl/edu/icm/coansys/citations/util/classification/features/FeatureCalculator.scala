/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2013 ICM-UW
 * 
 * CoAnSys is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * CoAnSys is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with CoAnSys. If not, see <http://www.gnu.org/licenses/>.
 */

package pl.edu.icm.coansys.citations.util.classification.features

/**
 * Feature calculator is able to calculate a single feature's value.
 *
 * @author Dominika Tkaczyk (d.tkaczyk@icm.edu.pl)
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
abstract class FeatureCalculator[A] {
  /**
   * Returns the name of the feature that can be calculated by the calculator.
   * Two different feature calculators of the same parameter types should
   * return different feature names.
   */
  def name: String = {
    var className: String = this.getClass.getName
    val classNameParts: Array[String] = className.split("\\.")
    className = classNameParts(classNameParts.length - 1)
    if (className.contains("Feature")) {
      className.replace("Feature", "")
    }
    else {
      className
    }
  }

  /**
   * Calculates the value of a single feature.
   */
  def calculateValue(obj: A): Double
}

