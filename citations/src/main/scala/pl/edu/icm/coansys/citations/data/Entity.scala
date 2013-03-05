/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.data

import pl.edu.icm.coansys.citations.util.ngrams._
import pl.edu.icm.coansys.citations.util.{BytesConverter, misc, author_matching}
import pl.edu.icm.coansys.citations.util.misc.tokensFromCermine
import pl.edu.icm.coansys.commons.scala.strings
import java.io.{DataInputStream, ByteArrayInputStream, DataOutputStream, ByteArrayOutputStream}
import pl.edu.icm.coansys.disambiguation.auxil.DiacriticsRemover.removeDiacritics

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

  def toBytes: Array[Byte]

  def toReferenceString: String = List(author, title, source, pages, year).mkString("; ")

  def normalisedAuthorTokens: Iterable[String] =
    tokensFromCermine(strings.lettersOnly(removeDiacritics(author.toLowerCase)))
      .filter(_.length > 1)
      .map(_.toLowerCase)
      .toSet

  override def equals(other: Any): Boolean = other match {
    case that: Entity => entityId == that.entityId
    case _ => false
  }

  override def hashCode =
    entityId.hashCode

  def toTypedBytes: Array[Byte] = {
    val buffer = new ByteArrayOutputStream()
    val output = new DataOutputStream(buffer)
    val bytes = toBytes
    output.writeUTF(entityId)
    output.writeInt(bytes.length)
    output.write(bytes)
    output.flush()
    buffer.toByteArray
  }

  def similarityTo(other: Entity): Double = {
    val features = similarityFeaturesWith(other)
    features.sum / features.length
  }

  def similarityFeaturesWith(other: Entity): List[Double] = {
//    val authorMatchFactor = author_matching.matchFactor(
//      tokensFromCermine(author),
//      tokensFromCermine(other.author))
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


    List(/*authorMatchFactor,*/ authorTrigramMatchFactor, authorTokenMatchFactor, yearMatchFactor, pagesMatchFactor,
      titleMatchFactor, sourceMatchFactor)
  }
}

object Entity {
  implicit val converter =
    new BytesConverter[Entity](
      (_.toTypedBytes),
      (Entity.fromTypedBytes))

  def fromBytes(entityId: String, bytes: Array[Byte]): Entity = {
    entityId.substring(0, 3) match {
      case "cit" =>
        CitationEntity.fromBytes(bytes)
      case "doc" =>
        DocumentEntity.fromBytes(bytes)
    }
  }

  def fromTypedBytes(bytes: Array[Byte]): Entity = {
    val buffer = new ByteArrayInputStream(bytes)
    val input = new DataInputStream(buffer)
    val entityId = input.readUTF()
    val length = input.readInt()
    val newBytes = Array.ofDim[Byte](length)
    input.read(newBytes)
    fromBytes(entityId, newBytes)
  }
}
