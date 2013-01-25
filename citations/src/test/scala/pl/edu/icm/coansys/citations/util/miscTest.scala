/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.util

import org.testng.Assert._
import org.testng.annotations.Test
import misc._

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class miscTest {
  @Test(groups = Array("fast"))
  def uuidEncoderDecoderEmptyTest() {
    val s = ""
    assertEquals(misc.uuidDecode(misc.uuidEncode(s)), s)
  }

  @Test(groups = Array("fast"))
  def uuidEncoderDecoderNonEmptyTest() {
    val s = "ala ma kota"
    assertEquals(misc.uuidDecode(misc.uuidEncode(s)), s)
  }

  @Test(groups = Array("fast"))
  def uuidEncoderDecoderUtf8Test() {
    val s = "zażółć gęślą jaźń"
    assertEquals(misc.uuidDecode(misc.uuidEncode(s)), s)
  }

  @Test(groups = Array("fast"))
  def extractNumbersTest() {
    assertEquals(extractNumbers(""), Nil, "Empty string")
    assertEquals(extractNumbers("320"), List("320"), "One number")
    assertEquals(extractNumbers("1999r."), List("1999"), "Number with some chars afterwards")
    assertEquals(extractNumbers("14 1992"), List("14", "1992"), "Two numbers")
    assertEquals(extractNumbers("10-11"), List("10", "11"), "Two numbers separated by dash")
    assertEquals(extractNumbers("pp.10-11."), List("10", "11"), "Two numbers separated by dash and some rubbish")
  }

  @Test(groups = Array("fast"))
  def extractYearTest() {
    assertEquals(extractYear(""), None, "Empty string")
    assertEquals(extractYear("320"), None, "Too short numeral")
    assertEquals(extractYear("1999"), Some("1999"), "Only a year")
    assertEquals(extractYear("1999r."), Some("1999"), "Year with some chars afterwards")
    assertEquals(extractYear("19992"), None, "Too long to be year")
    assertEquals(extractYear("14 1992"), Some("1992"), "Two numerals, only one is year")
    assertEquals(extractYear("9999 1992"), Some("1992"), "Two four-digit numerals, one is year")
    assertEquals(extractYear("1000 1992"), Some("1992"), "Two four-digit numerals, one more likely to be year")
  }
}
