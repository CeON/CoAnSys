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

package pl.edu.icm.coansys.commons.scala

import org.testng.Assert._
import org.testng.annotations.Test
import strings._

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class strigsTest {
  @Test(groups = Array("fast"))
  def rotationsTest() {
    assertEquals(Set("abc", "bca", "cab"), rotations("abc").toSet)
  }

  @Test(groups = Array("fast"))
  def subsequenceTest() {
    assertEquals("", subsequence("abcdef", Seq()))
    assertEquals("adf", subsequence("abcdef", Seq(0, 3, 5)))
  }

  @Test(groups = Array("fast"))
  def lcsTest() {
    assertEquals(Seq((0, 0), (1, 1), (2, 2)), lcs("aaa", "aaa"))
    assertEquals("aa", subsequence("aba", lcs("aba", "aaa").unzip._1))
    assertEquals("MJAU", subsequence("XMJYAUZ", lcs("XMJYAUZ", "MZJAWXU").unzip._1))
    assertEquals("MJAU", subsequence("MZJAWXU", lcs("XMJYAUZ", "MZJAWXU").unzip._2))
    assertEquals("HMAN", subsequence("HUMAN", lcs("HUMAN", "CHIMPANZEE").unzip._1))
    assertEquals("HMAN", subsequence("CHIMPANZEE", lcs("HUMAN", "CHIMPANZEE").unzip._2))
  }

  @Test(groups = Array("fast"))
  def hcsTest() {
    def defaultWeight(x: Char): Double = 1.0
    assertEquals(Seq((0, 0), (1, 1), (2, 2)), hcs("aaa", "aaa", defaultWeight))
    assertEquals("aa", subsequence("aba", hcs("aba", "aaa", defaultWeight).unzip._1))
    assertEquals("MJAU", subsequence("XMJYAUZ", hcs("XMJYAUZ", "MZJAWXU", defaultWeight).unzip._1))
    assertEquals("MJAU", subsequence("MZJAWXU", hcs("XMJYAUZ", "MZJAWXU", defaultWeight).unzip._2))
    assertEquals("HMAN", subsequence("HUMAN", hcs("HUMAN", "CHIMPANZEE", defaultWeight).unzip._1))
    assertEquals("HMAN", subsequence("CHIMPANZEE", hcs("HUMAN", "CHIMPANZEE", defaultWeight).unzip._2))

    def avoidSpaceWeight(x: Char): Double = {
      val epsilon = 0.0001
      x match {
        case ' ' => 1.0 - epsilon
        case _ => 1.0
      }
    }

    assertEquals("abc", subsequence("a bc", hcs("a bc", "ab c", avoidSpaceWeight).unzip._1))
    assertEquals("abc", subsequence("ab c", hcs("a bc", "ab c", avoidSpaceWeight).unzip._2))
    assertEquals("abc", subsequence("ab c", hcs("ab c", "a bc", avoidSpaceWeight).unzip._1))
    assertEquals("abc", subsequence("a bc", hcs("ab c", "a bc", avoidSpaceWeight).unzip._2))
  }

  @Test(groups = Array("fast"))
  def splitOnRegexTest() {
    assertEquals(splitOnRegex(" ".r, "ala ma kota").length, 5)
    assertEquals(splitOnRegex(" ".r, "ala ma kota").mkString(""), "ala ma kota")
    assertEquals(splitOnRegex(" ".r, "ala  ma  kota").length, 7)
    assertEquals(splitOnRegex(" ".r, "ala  ma  kota").mkString(""), "ala  ma  kota")
  }
}
