/*
 * This file is part of CoAnSys project.
 * Copyright (c) 20012-2013 ICM-UW
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
