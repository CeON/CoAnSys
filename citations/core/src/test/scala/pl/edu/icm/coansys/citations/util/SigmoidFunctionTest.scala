/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.util

import org.testng.Assert._
import org.testng.annotations.Test

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class SigmoidFunctionTest {
  @Test(groups = Array("fast"))
  def genTest() {
    val threshold = List(0.05, 0.001)
    val span = List(10.0, 40.0)
    for (
      th <- threshold;
      s <- span
    ) {
      val f = SigmoidFunction.gen(th, s)
      assertEquals(f(0), 1 - th, 0.000001)
      assertEquals(f(s), th, 0.000001)
    }
  }

  @Test(groups = Array("fast"))
  def genBetterTest() {
    val threshold = List(0.05, 0.001)
    val span = List(10.0, 40.0)
    for (
      th <- threshold;
      s <- span
    ) {
      val f = SigmoidFunction.genBetter(th, s)
      assertEquals(f(0), 1, 0.000001)
      assertEquals(f(s), th, 0.000001)
    }


  }
}
