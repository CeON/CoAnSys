/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.data

import pl.edu.icm.coansys.citations.util.ngrams._
import pl.edu.icm.coansys.citations.util.{misc, author_matching}
import pl.edu.icm.coansys.citations.util.misc.tokensFromCermine
import pl.edu.icm.coansys.commons.scala.strings

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
trait Entity {
  def entityId: String

  def author: String

  def source: String

  def title: String

  def pages: String

  def year: String

  def normalisedAuthorTokens: Iterable[String] =
    tokensFromCermine(strings.lettersOnly(author.toLowerCase)).toSet

  def similarityTo(other: Entity): Double =
    Context.similarityComputator(similarityFeaturesWith(other))

  def similarityFeaturesWith(other: Entity): List[Double] = {
    val authorMatchFactor = author_matching.matchFactor(
      tokensFromCermine(author),
      tokensFromCermine(other.author))
    val authorTrigramMatchFactor =
      trigramSimilarity(author, other.author)
    val authorTokenMatchFactor = {
      val tokens1 = tokensFromCermine(author)
      val tokens2 = tokensFromCermine(other.author)
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
      if (misc.extractYear(year) ==
        misc.extractYear(other.year))
        1.0
      else
        0.0
    val pagesMatchFactor = {
      val pages1 = misc.extractNumbers(pages)
      val pages2 = misc.extractNumbers(other.pages)
      if (pages1.size + pages2.size > 0)
        2 * (pages1.toSet & pages2.toSet).size.toDouble / (pages1.size + pages2.size)
      else
        1.0
    }
    val titleMatchFactor =
      trigramSimilarity(title, other.title)
    val sourceMatchFactor = {
      val minLen = math.min(source.length, other.source.length)
      if (minLen > 0) {
        strings.lcs(source, other.source).length.toDouble / minLen
      }
      else
      if (source.length == other.source.length)
        1.0
      else
        0.0
    }


    List(authorMatchFactor, authorTrigramMatchFactor, authorTokenMatchFactor, yearMatchFactor, pagesMatchFactor,
      titleMatchFactor, sourceMatchFactor)
  }
}
