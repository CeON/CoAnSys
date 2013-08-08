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

package pl.edu.icm.coansys.citations.util

import math.log
import math.exp

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class SigmoidFunction(val alpha: Double, val beta: Double, val yOffset: Double = 0.0) extends Function1[Double, Double] {
  def apply(x: Double): Double =
    1.0 / (1.0 + exp(-(alpha * x + beta))) + yOffset
}

object SigmoidFunction {
  def gen(threshold: Double, span: Double) = {
    val alpha = 2 * (log(threshold) - log(1 - threshold)) / span
    val beta = log(1 - threshold) - log(threshold)
    new SigmoidFunction(alpha, beta)
  }

  def genBetter(threshold: Double, span: Double) = {
    val basic = gen(threshold / 2, span)
    new SigmoidFunction(basic.alpha, basic.beta, threshold / 2)
  }
}
