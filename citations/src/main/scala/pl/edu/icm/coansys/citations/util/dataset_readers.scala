/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.util

import org.jdom.Element
import pl.edu.icm.coansys.commons.scala.xml._
import pl.edu.icm.cermine.bibref.model.BibEntry
import io.Source
import collection.mutable.ListBuffer
import org.jdom.output.XMLOutputter
import pl.edu.icm.cermine.bibref.parsing.tools.CitationUtils
import java.io.{FileWriter, BufferedWriter, FilenameFilter, File}
import pl.edu.icm.coansys.commons.scala.automatic_resource_management._
import pl.edu.icm.coansys.commons.scala.xml.StartTag
import scala.Some
import pl.edu.icm.coansys.commons.scala.xml.EndTag
import pl.edu.icm.coansys.commons.scala.xml.Text
import scala.collection.JavaConversions._

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object dataset_readers {
  /**
   * Reads tagged reference strings from Cora-ref dataset source.
   */
  def getTaggedReferenceListFromCorarefSource(source: Source): List[(String, String)] = {
    val listBuffer = new ListBuffer[String]
    var stringBuffer = new StringBuffer
    for (line <- source.getLines()) {
      if (line contains "<NEWREFERENCE>") {
        listBuffer.append(stringBuffer.toString)
        stringBuffer = new StringBuffer
      }
      else {
        stringBuffer.append(line)
      }
    }
    listBuffer.append(stringBuffer.toString)

    val refs = listBuffer.filterNot(_.isEmpty).toList

    refs.map(_.span(_ != '<'))
  }

  /**
   * Reads tagged reference strings from CiteSeer dataset source.
   */
  def getTaggedReferenceListFromCiteseerSource(source: Source): List[(String, String)] = {
    def parse(lines: Iterator[String], lastId: String, sb: StringBuilder, acc: ListBuffer[(String, String)]): List[(String, String)] = {
      if (!lines.hasNext)
        (acc += ((lastId, sb.toString()))).toList
      else {
        val CiteSeerMeta = """<meta reference_no="\d+" cluster_no="\d+" true_id="([^"]+)"></meta>""".r
        val CoraMeta = """<meta ref_no="[^"]+" class_no="([^"]+)" bib_no="[^"]+"></meta>""".r
        val current = lines.next()
        current match {
          case CiteSeerMeta(newId) =>
            val newAcc = if (sb.isEmpty) acc else (acc += ((lastId, sb.toString())))
            parse(lines, newId, new StringBuilder(), newAcc)
          case CoraMeta(newId) =>
            val newAcc = if (sb.isEmpty) acc else (acc += ((lastId, sb.toString())))
            parse(lines, newId, new StringBuilder(), newAcc)
          case text =>
            parse(lines, lastId, sb append text append ' ', acc)
        }
      }
    }

    val lines = source.getLines()
    parse(lines, "", new StringBuilder(), new ListBuffer())
  }

  /**
   * Converts tagged reference string to BibEntry
   *
   * @param citation - citation string
   * @param mapping - maps tags to BibEntry field; fields will be only created for tags having a mapping
   */
  def taggedReferenceToBibEntry(citation: String, mapping: Map[String, String]): BibEntry = {
    val entry = new BibEntry()
    val textBuffer = new StringBuffer()
    var stack: List[String] = Nil
    for (elem <- xmlToElems(citation)) {
      elem match {
        case StartTag(name) =>
          stack = name :: stack

        case EndTag(name) =>
          if (Some(name) == stack.headOption)
            stack = stack.tail

        case Text(s) =>
          if (!stack.isEmpty && mapping.contains(stack.head)) {
            val currentIdx = textBuffer.length()
            entry.addField(mapping(stack.head), s, currentIdx, currentIdx + s.length)
          }
          textBuffer.append(s)
      }
    }
    entry.setText(textBuffer.toString)
  }

  val citeseerTagMapping: Map[String, String] = Map(
    "author" -> BibEntry.FIELD_AUTHOR,
    "title" -> BibEntry.FIELD_TITLE,
    "journal" -> BibEntry.FIELD_JOURNAL,
    "booktitle" -> BibEntry.FIELD_JOURNAL,
    "tech" -> BibEntry.FIELD_JOURNAL,
    //      "location" -> BibEntry.FIELD_LOCATION,
    //      "volume" -> BibEntry.FIELD_VOLUME,
    "pages" -> BibEntry.FIELD_PAGES,
    //      "publisher" -> BibEntry.FIELD_PUBLISHER,
    //      "institution" -> BibEntry.FIELD_PUBLISHER,
    "date" -> BibEntry.FIELD_YEAR
  )

  def importBibEntriesFromCiteseerFile(path: String): List[BibEntry] =
    getTaggedReferenceListFromCiteseerSource(Source.fromFile(path))
      .unzip._2.map(taggedReferenceToBibEntry(_, citeseerTagMapping))

  val corarefTagMapping: Map[String, String] = Map(
    //      "address" -> BibEntry.FIELD_ADDRESS,
    "author" -> BibEntry.FIELD_AUTHOR,
    "booktitle" -> BibEntry.FIELD_JOURNAL,
    "date" -> BibEntry.FIELD_YEAR,
    //      "editor" -> BibEntry.FIELD_EDITOR,
    //      "institution" -> BibEntry.FIELD_INSTITUTION,
    "journal" -> BibEntry.FIELD_JOURNAL,
    //      "month" -> BibEntry.FIELD_MONTH,
    //      "note" -> BibEntry.FIELD_NOTE,
    "pages" -> BibEntry.FIELD_PAGES,
    //      "publisher" -> BibEntry.FIELD_PUBLISHER,
    "tech" -> BibEntry.FIELD_JOURNAL,
    "title" -> BibEntry.FIELD_TITLE,
    "type" -> BibEntry.FIELD_JOURNAL,
    //      "volume" -> BibEntry.FIELD_VOLUME,
    "year" -> BibEntry.FIELD_YEAR
  )

  def importBibEntriesFromCorarefFile(path: String): List[BibEntry] = {
    getTaggedReferenceListFromCorarefSource(Source.fromFile(path)(io.Codec.ISO8859))
      .unzip._2.map(taggedReferenceToBibEntry(_, corarefTagMapping))
  }

  /**
   * Tags all string-name contents as surname removing previous tags.
   */
  def findAndCollapseStringName(elem: Element) {
    val tagName = "string-name"
    val surnameTag = "surname"
    if (elem.getName == tagName)
      collapseContent(elem, surnameTag)
    else
      elem.getChildren.foreach(obj => findAndCollapseStringName(obj.asInstanceOf[Element]))
  }

  def collapseContent(elem: Element, tagName: String) {
    val value = elem.getValue
    val surnameElem = new Element(tagName).setText(value)
    elem.removeContent()
    elem.addContent(surnameElem)
  }

  def main(args: Array[String]) {
    val path = "C:\\Users\\matfed\\Desktop\\citeseer.ie.raw.tar"
    val output = "C:\\Users\\matfed\\Desktop\\mixed-citations-citeseer.xml"
    //        val files = new File(path).listFiles(new FilenameFilter {
    //          def accept(dir: File, name: String) = name.endsWith("labeled")
    //        }).map(_.getCanonicalPath)
    //          .flatMap(file => getTaggedReferenceListFromCorarefSource(Source.fromFile(file)(io.Codec.ISO8859)).unzip._2.flatMap(xmlToElems))
    //          .flatMap(_ match { case Text(s) => None; case StartTag(name) => Some(name); case EndTag(name) => Some(name)})
    //          .toSet.toList.sorted
    //          .foreach(println)

    val mixedCitations =
      new File(path).listFiles(new FilenameFilter {
        def accept(dir: File, name: String) = name.endsWith("Out")
      }).map(_.getCanonicalPath)
        .flatMap(importBibEntriesFromCiteseerFile)
        .map(entry => new XMLOutputter().outputString({
        val elem = CitationUtils.bibEntryToNLM(entry)
        findAndCollapseStringName(elem)
        elem
      }))
    using(new BufferedWriter(new FileWriter(output))) {
      writer =>
        mixedCitations foreach (l => writer.write(l + "\n"))
    }
  }
}
