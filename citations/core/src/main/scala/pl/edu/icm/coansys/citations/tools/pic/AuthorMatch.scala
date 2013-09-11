package pl.edu.icm.coansys.citations.tools.pic

import scala.collection.JavaConversions._
import java.io.File
import pl.edu.icm.coansys.citations.util.misc._
import pl.edu.icm.coansys.commons.java.DiacriticsRemover
import java.util.Locale
import pl.edu.icm.ceon.scala_commons.strings
import scala.collection.mutable

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object AuthorMatch {
  def main(args: Array[String]) {
    val dbUrl = "file:///C:/Users/matfed/Desktop/pic-eval/db.seq"
    val parsedUrl = "file:///C:/Users/matfed/Desktop/pic-eval/parsed.seq"
    val unparsedUrl = "file:///C:/Users/matfed/Desktop/pic-eval/unparsed.seq"
    val heurUrl = "file:///C:/Users/matfed/Desktop/pic-eval/heur.seq"
    val goldPath = "C:\\Users\\matfed\\Desktop\\pic-eval\\citations-rewritten.csv"

    val db = TempCommons.readDocumentsDatabase(dbUrl)
    val rawDb = TempCommons.readRawDocumentsDatabase(dbUrl)
    val entities = TempCommons.readParsedCitations(parsedUrl, unparsedUrl)
    val gold = TempCommons.readPicGroundTruth(new File(goldPath))

    var checked = 0
    gold.filterNot(_._2.isEmpty).foreach {
      case (src, dst) =>
        if (entities.contains(src) && db.contains(dst)) {
          checked += 1
          val authors = rawDb(dst).getDocumentMetadata.getBasicMetadata.getAuthorList.map(x => DiacriticsRemover.removeDiacritics(x.getName.toLowerCase(Locale.ENGLISH)))
          val tokens = tokensFromCermine(strings.lettersOnly(db(dst).author.toLowerCase(Locale.ENGLISH))).toArray
          val cit = entities(src).rawText.getOrElse("").toLowerCase(Locale.ENGLISH)
          val citTokens =
            tokensFromCermine(strings.lettersOnly(cit))
              .toArray

          if (!authors.map(strings.lettersOnly).map(tokensFromCermine).forall(_.filter(_.length > 1).exists(cit.contains))) {
            println("No match for: ")
            println(db(dst).author)
            println(entities(src).rawText)
            println()
          }


//          var matching = Array.fill(tokens.length)(mutable.Set[Int]())
//          for {
//            i <- 0 until tokens.length
//            j <- 0 until citTokens.length
//          } {
//            if (tokens(i) == citTokens(j)) {
//              matching(i) += j
//            }
//          }
//          var didAnything = true
//          while (didAnything) {
//            didAnything = false
//            for {
//              i <- 0 until tokens.length
//              j <- 0 until citTokens.length
//            } {
//              if ((tokens(i).startsWith(citTokens(j)) || citTokens(j).startsWith(tokens(i))) &&
//                ((i > 0 && matching(i-1).flatMap(x => List(x+1, x-1)).contains(j)) ||
//                  (i < tokens.length - 1 && matching(i+1).flatMap(x => List(x+1, x-1)).contains(j))) &&
//                !matching(i).contains(j)) {
//                matching(i) += j
//                didAnything = true
//              }
//            }
//          }
//
//
//
//          if (!matching.forall(!_.isEmpty)) {
//            println("No match for: ")
//            println(db(dst).author)
//            println(entities(src).rawText)
//            println()
//          }
        }
    }
  }
}
