package pl.edu.icm.coansys.citations.tools.pic

import pl.edu.icm.coansys.citations.util.misc._
import scala.Some
import pl.edu.icm.ceon.scala_commons.strings
import pl.edu.icm.coansys.commons.java.DiacriticsRemover
import scala.util.{Success, Try}
import pl.edu.icm.coansys.citations.util.sequencefile.ConvertingSequenceFileWriter
import pl.edu.icm.coansys.citations.data.MatchableEntity
import java.io.File

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object NewHeuristicTest {
  def lettersNormaliseTokenise(str: String) =
    normaliseTokenise(strings.lettersOnly(str))

  def digitsNormaliseTokenise(str: String) =
    normaliseTokenise(strings.digitsOnly(str))

  def normaliseTokenise(str: String) =
    tokensFromCermine(DiacriticsRemover.removeDiacritics(str))
      .flatMap { tok =>
      if (tok.length <= 3 && tok.forall(_.isUpper))
        None
      else
        Some(tok)
      }
      .filter(_.length > 2)
      .map(_.toLowerCase)


  def main(args: Array[String]) {
    val dbUrl = "file:///C:/Users/matfed/Desktop/pic-eval/db.seq"
    val parsedUrl = "file:///C:/Users/matfed/Desktop/pic-eval/parsed.seq"
    val unparsedUrl = "file:///C:/Users/matfed/Desktop/pic-eval/unparsed.seq"
    val heurUrl = "file:///C:/Users/matfed/Desktop/pic-eval/heur.seq"
    val goldPath = "C:\\Users\\matfed\\Desktop\\pic-eval\\citations-rewritten.csv"

    val db = TempCommons.readDocumentsDatabase(dbUrl)
//    db.toIterable.flatMap { case (id, meta) =>
//      val toProcess = List(
//        lettersNormaliseTokenise(meta.author),
//        digitsNormaliseTokenise(meta.year),
//        lettersNormaliseTokenise(meta.title).take(2)).flatten
//      toProcess.filterNot(TempCommons.stopWords).distinct zip Stream.continually(id)
//    }.groupBy(_._1).mapValues(_.unzip._2).toList.sortBy(_._2.size).foreach(println)

    val yearIndex = db.toIterable.flatMap { case (id, meta) =>
      val toProcess = digitsNormaliseTokenise(meta.year).distinct
      toProcess.filterNot(TempCommons.stopWords).distinct zip Stream.continually(id)
    }.groupBy(_._1).mapValues(_.unzip._2)

    val authorIndex = db.toIterable.flatMap { case (id, meta) =>
      val toProcess = lettersNormaliseTokenise(meta.author).distinct
      toProcess.filterNot(TempCommons.stopWords).distinct zip Stream.continually(id)
    }.groupBy(_._1).mapValues(_.unzip._2)

    val titleIndex = db.toIterable.flatMap { case (id, meta) =>
      val toProcess = lettersNormaliseTokenise(meta.title).take(4).distinct
      toProcess.filterNot(TempCommons.stopWords).distinct zip Stream.continually(id)
    }.groupBy(_._1).mapValues(_.unzip._2)

    val entities = TempCommons.readParsedCitations(parsedUrl, unparsedUrl)
    val heurs = entities.mapValues(_.rawText.getOrElse("")).map {case (id, text) =>
      val tokens = normaliseTokenise(text)
      val numbers = normaliseTokenise(strings.digitsOnly(text))
      val possibleYear = numbers.flatMap(x => Try(x.toInt).toOption).flatMap(x => List(x + 1, x, x - 1))
        .map(_.toString).flatMap(t => yearIndex.getOrElse(t, List()))
      val possibleAuthor = tokens.flatMap(t => authorIndex.getOrElse(t, List()))
      val authorYearMatch = possibleYear.toSet & possibleAuthor.toSet
      if (!authorYearMatch.isEmpty)
        (id, authorYearMatch)
      else {
        val possibleTitle = tokens.flatMap(t => titleIndex.getOrElse(t, List())).groupBy(identity)
          .filter(_._2.size >= 3).keys
        val titleYearMatch = possibleYear.toSet & possibleTitle.toSet
        (id, titleYearMatch)
      }
    }

//    using (ConvertingSequenceFileWriter.fromLocal[MatchableEntity, String]("file:///C:/Users/matfed/Desktop/pic-eval/new-heur.sq")) {
//      write =>
//        heurs.toIterable.flatMap{case (src, dsts) =>
//          Try(entities(src)) match {
//            case Success(ent) =>
//              Stream.continually(ent) zip (dsts.map("doc_" + _))
//            case _ =>
//              Nil
//          }
//        }.foreach(write)
//    }

    val heurCounts = heurs.values.map(_.size)
    println(heurCounts.max)
    println(heurCounts.min)
    println(heurCounts.sum)
    println(heurCounts.sum.toDouble / heurCounts.size)

    val gold = TempCommons.readPicGroundTruth(new File(goldPath))
//    gold.filterNot(_._2.isEmpty).foreach {case (src, dst) =>
//      if(!(heurs.getOrElse(src, Set()) contains dst)) {
//        println
//        println(Try(entities(src).toDebugString))
//        println(Try(db(dst).toDebugString))
//        println
//      }
//    }
//
    val (good, bad) =
      gold.filterNot(_._2.isEmpty).map{case (src, dst) => heurs.getOrElse(src, Set()) contains dst}.partition(identity)

    println(good.size)
    println(bad.size)

  }
}
