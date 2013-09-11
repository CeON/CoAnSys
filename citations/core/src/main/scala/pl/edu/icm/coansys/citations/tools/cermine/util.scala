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

package pl.edu.icm.coansys.citations.tools.cermine

import resource._
import collection.JavaConversions._
import java.io.{FileWriter, FileInputStream, InputStream, File}
import org.jdom.{Document, Element}
import org.jdom.input.SAXBuilder
import org.jdom.filter.Filter

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object util {
  def extractMixedCitationsFromXmls(inFiles: TraversableOnce[File]): TraversableOnce[Element] = {
    val citationTag = "mixed-citation"

    val citations = inFiles.flatMap {
      file =>
        val stream: InputStream = new FileInputStream(file)
        val doc: Document = new SAXBuilder("org.apache.xerces.parsers.SAXParser").build(stream)
        doc.getDescendants(new Filter {
          def matches(obj: Any) = obj.isInstanceOf[Element] && (obj.asInstanceOf[Element].getName == citationTag)
        })
    }.map(_.asInstanceOf[Element])

    citations
  }

  def writeCitationsToXml(outFile: File, citations: TraversableOnce[String]) {
    val xmlHeader =
      """<?xml version="1.0" encoding="utf-8" standalone="yes" ?>
        |<citations>
      """.stripMargin
    val xmlFooter = """</citations>"""

    for(writer <- managed(new FileWriter(outFile))) {
        writer.write(xmlHeader + "\n")
        citations.foreach(x => writer.write(x + "\n"))
        writer.write(xmlFooter + "\n")
    }
  }

}
