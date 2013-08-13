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
