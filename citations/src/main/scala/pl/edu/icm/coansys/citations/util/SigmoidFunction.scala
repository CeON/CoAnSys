/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.util

import math.log
import math.exp

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class SigmoidFunction(val alpha: Double, val beta: Double) extends Function1[Double, Double] {
  def apply(x: Double): Double =
    1./(1.+ exp(-(alpha * x + beta)))
}

object SigmoidFunction {
  def gen(threshold: Double, span: Double) = {
    val alpha = 2 * (log(threshold) - log(1 - threshold)) / span
    val beta = log(1 - threshold) - log(threshold)
    new SigmoidFunction(alpha, beta)
  }
}
