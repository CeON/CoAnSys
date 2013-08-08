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

package pl.edu.icm.coansys.citations.util

import org.testng.annotations.Test
import org.testng.Assert._
import org.apache.commons.io.IOUtils
import org.jdom.output.XMLOutputter
import org.jdom.input.DOMBuilder
import org.w3c.dom.{Node, Element}

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class XPathEvaluatorTest {
  @Test(groups = Array("fast"))
  def someTest() {
    def nodeToString(n: Node) =
      new XMLOutputter().outputString(new DOMBuilder().build(n.asInstanceOf[Element]))
    val xmlString = """<article><back><ref-list><ref>test<b>test</b></ref></ref-list></back></article>"""
    val eval = XPathEvaluator.fromInputStream(IOUtils.toInputStream(xmlString))
    val result = eval.asNodes( """/article/back/ref-list/ref""")
      .map(nodeToString)
      .head
    assertEquals(result, """<ref>test<b>test</b></ref>""")
  }
}
