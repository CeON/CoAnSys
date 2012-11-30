/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.util

import org.testng.Assert._
import org.testng.annotations.Test

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
}
