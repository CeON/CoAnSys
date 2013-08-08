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

import pl.edu.icm.coansys.citations.util.ngrams._
import org.testng.annotations.Test
import org.testng.Assert._

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class ngramsTest {
  @Test(groups = Array("fast"))
  def ngramCountsTest() {
    val NgramStatistics(counts, _) = NgramStatistics.fromString("ala ma kota a kot ma ale", 3)
    assertEquals(counts("ala"), 1)
    assertEquals(counts("kot"), 2)
    assertEquals(counts(" a "), 1)
  }

  @Test(groups = Array("fast"))
  def trigramSimilarityTest() {
    val epsilon = 0.000001
    assertEquals(trigramSimilarity("a", "a"), 1.0, epsilon)
    assertEquals(trigramSimilarity("a", "b"), 0.0, epsilon)
    assertEquals(trigramSimilarity("ala ma kota", "ala ma kota"), 1.0, epsilon)
    assertTrue(trigramSimilarity("ala ma kota", "ala kota ma") < 1.0)
  }

}
