/*
 * (C) 2010-2012 ICM UW. All rights reserved.
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
    val counts = ngramCounts("ala ma kota a kot ma ale", 3)
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
