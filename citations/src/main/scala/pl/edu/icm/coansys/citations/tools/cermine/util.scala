/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.tools.cermine

import collection.JavaConversions._
import java.io.{FileWriter, FileInputStream, InputStream, File}
import org.jdom.{Document, Element}
import org.jdom.input.SAXBuilder
import org.jdom.filter.Filter
import pl.edu.icm.coansys.commons.scala.automatic_resource_management._

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

    using(new FileWriter(outFile)) {
      writer =>
        writer.write(xmlHeader + "\n")
        citations.foreach(x => writer.write(x + "\n"))
        writer.write(xmlFooter + "\n")
    }
  }

}
