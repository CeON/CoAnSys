/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.commons.scala

import org.testng.Assert._
import org.testng.annotations.Test
import pl.edu.icm.coansys.commons.scala.xml._


/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class xmlTest {
  @Test(groups = Array("fast"))
  def xmlToElemsTest() {
    assertEquals(xmlToElems("<tag>"), List(StartTag("tag")))
    assertEquals(xmlToElems("<tag>text</tag>"), List(StartTag("tag"), Text("text"), EndTag("tag")))
    assertEquals(xmlToElems("begin<tag>text</tag>end"), List(Text("begin"), StartTag("tag"), Text("text"), EndTag("tag"), Text("end")))
  }
}
