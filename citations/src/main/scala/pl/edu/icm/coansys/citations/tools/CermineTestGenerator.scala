/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.tools

import pl.edu.icm.coansys.commons.scala.automatic_resource_management.using
import collection.JavaConversions._
import java.io.{FileWriter, InputStream, FileInputStream, File}
import org.jdom.{Document, Element}
import org.jdom.input.SAXBuilder
import org.jdom.filter.Filter
import util.Random
import org.jdom.output.XMLOutputter

/**
 * Generates Mallet files for n-fold x-validation from XML-s containing mixed citations.
 *
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object CermineTestGenerator {

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

  /**
   * Extracts mixed citations from XML files, shuffles them and creates folds.
   */
  def xmlsToFolds(inFiles: TraversableOnce[File], outDir: File, foldsNo: Int = 5) {
    val xmlHeader =
      """<?xml version="1.0" encoding="utf-8" standalone="yes" ?>
        |<citations>
      """.stripMargin
    val xmlFooter = """</citations>"""

    val shuffledCitations = Random.shuffle(extractMixedCitationsFromXmls(inFiles)).toList

    for (i <- 0 until foldsNo) {
      val (test, train) = shuffledCitations.zipWithIndex.partition(_._2 % foldsNo == i)
      val testDir = new File(outDir, "fold" + i + "-test")
      val trainDir = new File(outDir, "fold" + i + "-train")
      testDir.mkdirs()
      trainDir.mkdirs()

      def write(file: File, citations: List[Element]) {
        using(new FileWriter(file)) {
          writer =>
            writer.write(xmlHeader + "\n")
            citations.foreach(x => writer.write(new XMLOutputter().outputString(x) + "\n"))
            writer.write(xmlFooter + "\n")
        }
      }

      write(new File(testDir, "mixed-citations.xml"), test.unzip._1)
      write(new File(trainDir, "mixed-citations.xml"), train.unzip._1)
    }

  }

  def main(args: Array[String]) {
    val inDir = """C:\Users\matfed\Desktop\parser-training"""
    val outDir = """C:\Users\matfed\Desktop\cermine-testing"""
    xmlsToFolds(new File(inDir).listFiles(), new File(outDir), foldsNo = 5)
  }
}