package pl.edu.icm.coansys.citations.tools

import pl.edu.icm.coansys.citations.data.MatchableEntity
import pl.edu.icm.coansys.commons.scala.strings
import pl.edu.icm.coansys.citations.util.misc._
import scala.Some
import pl.edu.icm.coansys.commons.java.DiacriticsRemover
import scala.util.Try

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class NewHeuristic(entities: Iterable[MatchableEntity]) {
  val yearIndex = entities.flatMap {
    meta =>
      val toProcess = NewHeuristic.digitsNormaliseTokenise(meta.year).distinct
      toProcess.filterNot(TempCommons.stopWords).distinct zip Stream.continually(meta.id)
  }.groupBy(_._1).mapValues(_.unzip._2)

  val authorIndex = entities.flatMap {
    meta =>
      val toProcess = NewHeuristic.lettersNormaliseTokenise(meta.author).distinct
      toProcess.filterNot(TempCommons.stopWords).distinct zip Stream.continually(meta.id)
  }.groupBy(_._1).mapValues(_.unzip._2)

  val titleIndex = entities.flatMap {
    meta =>
      val toProcess = NewHeuristic.lettersNormaliseTokenise(meta.title).take(4).distinct
      toProcess.filterNot(TempCommons.stopWords).distinct zip Stream.continually(meta.id)
  }.groupBy(_._1).mapValues(_.unzip._2)

  def getHeuristiclyMatching(entity: MatchableEntity) = {
    val text = entity.rawText.getOrElse("")
    val tokens = NewHeuristic.normaliseTokenise(text)
    val numbers = NewHeuristic.digitsNormaliseTokenise(text)
    val possibleYear = numbers.flatMap(x => Try(x.toInt).toOption).flatMap(x => List(x + 1, x, x - 1))
      .map(_.toString).flatMap(t => yearIndex.getOrElse(t, List()))
    val possibleAuthor = tokens.flatMap(t => authorIndex.getOrElse(t, List()))
    val authorYearMatch = possibleYear.toSet & possibleAuthor.toSet

    if (!authorYearMatch.isEmpty)
      authorYearMatch
    else {
      val possibleTitle = tokens.flatMap(t => titleIndex.getOrElse(t, List())).groupBy(identity)
        .filter(_._2.size >= 3).keys
      val titleYearMatch = possibleYear.toSet & possibleTitle.toSet

      titleYearMatch
    }
  }
}

object NewHeuristic {
  def lettersNormaliseTokenise(str: String) =
    normaliseTokenise(strings.lettersOnly(str))

  def digitsNormaliseTokenise(str: String) =
    normaliseTokenise(strings.digitsOnly(str))

  def normaliseTokenise(str: String) =
    tokensFromCermine(DiacriticsRemover.removeDiacritics(str))
      .flatMap {
      tok =>
        if (tok.length <= 3 && tok.forall(_.isUpper))
          None
        else
          Some(tok)
    }
      .filter(_.length > 2)
      .map(_.toLowerCase)


}
