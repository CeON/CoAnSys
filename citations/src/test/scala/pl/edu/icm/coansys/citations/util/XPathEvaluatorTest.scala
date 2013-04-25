/*
 * (C) 2010-2012 ICM UW. All rights reserved.
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
