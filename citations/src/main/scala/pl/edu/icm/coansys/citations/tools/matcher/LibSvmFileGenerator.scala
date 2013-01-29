/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.tools.matcher

import java.io.{FileWriter, FileInputStream, File}
import io.Source
import collection.JavaConversions._
import pl.edu.icm.cermine.bibref.{CRFBibReferenceParser, BibReferenceParser}
import pl.edu.icm.cermine.bibref.model.BibEntry
import pl.edu.icm.coansys.citations.util.author_matching
import pl.edu.icm.coansys.citations.util.misc
import pl.edu.icm.cermine.bibref.parsing.tools.CitationUtils
import pl.edu.icm.coansys.citations.util.ngrams.trigramSimilarity
import pl.edu.icm.coansys.commons.scala.strings
import pl.edu.icm.coansys.commons.scala.automatic_resource_management._
import collection.mutable

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object LibSvmFileGenerator {
  private def tokensFromCermine(s: String): List[String] =
    CitationUtils.stringToCitation(s).getTokens.map(_.getText).toList


  def features(parser: BibReferenceParser[BibEntry])(cit1: String, cit2: String) = {
    def getField(bibEntry: BibEntry, key: String): String =
      bibEntry.getAllFieldValues(key).mkString(" ")

    val parsed1 = parser.parseBibReference(cit1)
    val parsed2 = parser.parseBibReference(cit2)

    val authorMatchFactor = author_matching.matchFactor(
      tokensFromCermine(getField(parsed1, BibEntry.FIELD_AUTHOR)),
      tokensFromCermine(getField(parsed2, BibEntry.FIELD_AUTHOR)))

    val yearMatchFactor =
      if (misc.extractYear(getField(parsed1, BibEntry.FIELD_YEAR)) ==
        misc.extractYear(getField(parsed2, BibEntry.FIELD_YEAR)))
        1.0
      else
        0.0
    val pagesMatchFactor = {
      val pages1 = misc.extractNumbers(getField(parsed1, BibEntry.FIELD_PAGES))
      val pages2 = misc.extractNumbers(getField(parsed2, BibEntry.FIELD_PAGES))
      if (pages1.size + pages2.size > 0)
        2 * (pages1.toSet & pages2.toSet).size.toDouble / (pages1.size + pages2.size)
      else
        1.0
    }
    val titleMatchFactor =
      trigramSimilarity(getField(parsed1, BibEntry.FIELD_TITLE), getField(parsed2, BibEntry.FIELD_TITLE))
    val sourceMatchFactor = {
      val source1 = getField(parsed1, BibEntry.FIELD_JOURNAL)
      val source2 = getField(parsed2, BibEntry.FIELD_JOURNAL)
      val minLen = math.min(source1.length, source2.length)
      if (minLen > 0) {
        strings.lcs(source1, source2).length.toDouble / minLen
      }
      else
      if (source1.length == source2.length)
        1.0
      else
        0.0
    }

    List(authorMatchFactor, yearMatchFactor, pagesMatchFactor, titleMatchFactor, sourceMatchFactor)
  }

  def svmLightLineFromFeatures(target: Int, featureList: Iterable[Double]): String = {
    val indexed = featureList.view.zip(Stream.from(1))
    val featureStrings = indexed map {
      case (value, idx) => idx.toString + ":" + value.toString
    }
    val jointFeatureString = featureStrings.mkString(" ")

    target + " " + jointFeatureString
  }

  def main(args: Array[String]) {
    val citationsFile = new File(args(1))
    val modelFile = new File(args(0))
    val outFile = new File(args(2))

    val citations =
      Source.fromFile(citationsFile).getLines().map {
        x => val parts = x.split(" ", 2); (parts(0), parts(1))
      }.toArray
    val citationPairs = for {
      i <- 0 until citations.length
      j <- 0 until i
    } yield (citations(i)._1 == citations(j)._1, citations(i)._2, citations(j)._2)

    //val (matching, nonMatching) = citationPairs.partition(_._1)
    val myFeatures = features(new CRFBibReferenceParser(new FileInputStream(modelFile)) {
      val cache = new mutable.HashMap[String, BibEntry]()

      override def parseBibReference(text: String): BibEntry =
        cache.getOrElseUpdate(text, super.parseBibReference(text))
    }) _
    val lines = citationPairs map {
      case (matches, cit1, cit2) =>
        svmLightLineFromFeatures(if (matches) 1 else 0, myFeatures(cit1, cit2))
    }

    using(new FileWriter(outFile)) {
      writer =>
        lines.foreach(x => writer.write(x + "\n"))
    }

  }
}
