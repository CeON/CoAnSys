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
import pl.edu.icm.coansys.citations.util.ngrams.NgramStatistics
import pl.edu.icm.coansys.commons.scala.strings
import pl.edu.icm.coansys.commons.scala.automatic_resource_management._
import collection.mutable

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object LibSvmFileGenerator {
  private def tokensFromCermine(s: String): List[String] =
    CitationUtils.stringToCitation(s).getTokens.map(_.getText).toList

  case class CachedData(bibEntry: BibEntry, ngrams: NgramStatistics, letterNgrams: NgramStatistics, digitNgrams: NgramStatistics)

  private val cache = new mutable.HashMap[String, CachedData]()

  def features(parser: BibReferenceParser[BibEntry])(cit1: String, cit2: String) = {
    def getField(bibEntry: BibEntry, key: String): String =
      bibEntry.getAllFieldValues(key).mkString(" ")
    def lettersOnly(s: String) =
      s.map(x => if (x.isLetter) x else ' ').split("\\W+").mkString(" ")
    def digitsOnly(s: String) =
      s.map(x => if (x.isDigit) x else ' ').split("\\W+").mkString(" ")
    val trigramCounts: String => NgramStatistics = NgramStatistics.fromString(_, 3)

    val CachedData(parsed1, nc1, ncLetters1, ncDigits1) =
      cache.getOrElseUpdate(cit1, CachedData(parser.parseBibReference(cit1), trigramCounts(cit1),
        trigramCounts(lettersOnly(cit1)), trigramCounts(digitsOnly(cit1))))
    val CachedData(parsed2, nc2, ncLetters2, ncDigits2) =
      cache.getOrElseUpdate(cit2, CachedData(parser.parseBibReference(cit2), trigramCounts(cit2),
        trigramCounts(lettersOnly(cit2)), trigramCounts(digitsOnly(cit2))))

    //    val parsed1 = parser.parseBibReference(cit1)
    //    val parsed2 = parser.parseBibReference(cit2)

    val authorMatchFactor = author_matching.matchFactor(
      tokensFromCermine(getField(parsed1, BibEntry.FIELD_AUTHOR)),
      tokensFromCermine(getField(parsed2, BibEntry.FIELD_AUTHOR)))
    val authorTrigramMatchFactor =
      trigramSimilarity(getField(parsed1, BibEntry.FIELD_AUTHOR), getField(parsed2, BibEntry.FIELD_AUTHOR))
    val authorTokenMatchFactor = {
      val tokens1 = tokensFromCermine(getField(parsed1, BibEntry.FIELD_AUTHOR))
      val tokens2 = tokensFromCermine(getField(parsed2, BibEntry.FIELD_AUTHOR))
      val counts1 = tokens1.map(_.toLowerCase).groupBy(identity).mapValues(_.length)
      val counts2 = tokens2.map(_.toLowerCase).groupBy(identity).mapValues(_.length)
      val common = (counts1.keySet & counts2.keySet).toIterator.map(k => counts1(k) min counts2(k)).sum
      val all = tokens1.length + tokens2.length
      if (all > 0)
        common.toDouble / all
      else
        1.0
    }

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
    val overallMatchFactor = nc1 similarityTo nc2
    val lettersMatchFactor = ncLetters1 similarityTo ncLetters2
    val digitsMatchFactor = ncDigits1 similarityTo ncDigits2

    List(authorMatchFactor, authorTrigramMatchFactor, authorTokenMatchFactor, yearMatchFactor, pagesMatchFactor,
      titleMatchFactor, sourceMatchFactor, overallMatchFactor, lettersMatchFactor, digitsMatchFactor)
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
    val outMapFile = new File(outFile.getCanonicalPath + ".map")

    val citations =
      Source.fromFile(citationsFile).getLines().map {
        x => val parts = x.split(" ", 2); (parts(0), parts(1))
      }.toArray
    val citationPairs = for {
      i <- 0 until citations.length
      j <- 0 until i
    } yield (citations(i)._1 == citations(j)._1, (i, j), citations(i)._2, citations(j)._2)

    //val (matching, nonMatching) = citationPairs.partition(_._1)
    val myFeatures = features(new CRFBibReferenceParser(new FileInputStream(modelFile))) _
    val lines = citationPairs map {
      case (matches, ids, cit1, cit2) =>
        svmLightLineFromFeatures(if (matches) 1 else 0, myFeatures(cit1, cit2))
    }

    using(new FileWriter(outFile)) {
      writer =>
        lines.foreach(x => writer.write(x + "\n"))
    }

    using(new FileWriter(outMapFile)) {
      writer =>
        citationPairs.foreach {
          case (_, (i, j), _, _) => writer.write(i + " " + j + "\n")
        }
    }

  }
}
