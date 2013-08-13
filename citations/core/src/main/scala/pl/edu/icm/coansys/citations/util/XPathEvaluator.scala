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