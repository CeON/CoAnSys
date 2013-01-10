/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.util

import org.testng.Assert._
import org.testng.annotations.Test
import author_matching._

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class author_matchingTest {
  @Test(groups = Array("fast"))
  def matchingTokensTest() {
    assertEquals(exactMatchingTokens("ala ma kota".split(" ").toList, "kot ma ale".split(" ").toList), List((1, 1)))
    assertEquals(exactMatchingTokens("ala ma kota".split(" ").toList, "kota ala ma".split(" ").toList), List((0, 1), (1, 2), (2, 0)))
  }

  @Test(groups = Array("fast"))
  def roughMatchingTest() {
    assertEquals(nonOverlappingMatching(Set((0, 0), (1, 1))), List((0, 0), (1, 1)))
    assertEquals(nonOverlappingMatching(Set((0, 0), (1, 4), (2, 2), (3, 3), (5, 5))), List((0, 0), (2, 2), (3, 3), (5, 5)))
  }
}
