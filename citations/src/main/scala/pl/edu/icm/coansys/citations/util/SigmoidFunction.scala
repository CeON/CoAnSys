/*
 * (C) 2010-2012 ICM UW. All rights reserved.
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
