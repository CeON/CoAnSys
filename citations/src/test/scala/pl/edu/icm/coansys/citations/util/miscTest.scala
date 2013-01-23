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
  def extractYearTest() {
    assertEquals(extractYear(""), None)
    assertEquals(extractYear("320"), None)
    assertEquals(extractYear("1999"), Some("1999"))
    assertEquals(extractYear("1999r."), Some("1999"))
    assertEquals(extractYear("19992"), None)
    assertEquals(extractYear("14 1992"), Some("1992"))
    assertEquals(extractYear("9999 1992"), Some("1992"))
    assertEquals(extractYear("1000 1992"), Some("1992"))
  }
}
