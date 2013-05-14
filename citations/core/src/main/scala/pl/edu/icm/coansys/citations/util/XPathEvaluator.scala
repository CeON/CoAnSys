/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.util

import javax.xml.xpath.{XPathConstants, XPathFactory}
import org.w3c.dom.{NodeList, Node}
import java.io.{StringReader, InputStream}
import javax.xml.parsers.DocumentBuilderFactory
import org.xml.sax.{InputSource, EntityResolver}

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class XPathEvaluator(private val obj: Any) extends (String => String) {

  private val evaluator = XPathFactory.newInstance().newXPath()

  def apply(xpath: String): String =
    evaluator.evaluate(xpath, obj)

  def asNode(xpath: String) =
    evaluator.evaluate(xpath, obj, XPathConstants.NODE).asInstanceOf[Node]

  def asNodes(xpath: String) = {
    val nodes = evaluator.evaluate(xpath, obj, XPathConstants.NODESET).asInstanceOf[NodeList]
    for (i <- 0 until nodes.getLength)
    yield nodes.item(i)
  }
}

object XPathEvaluator {
  def fromInputStream(is: InputStream) = {
    val docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
    docBuilder.setEntityResolver(new EntityResolver {
      def resolveEntity(publicId: String, systemId: String) = new InputSource(new StringReader(""))
    })
    val doc = docBuilder.parse(is)
    new XPathEvaluator(doc)
  }
}