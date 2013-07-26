/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.data

import collection.JavaConversions._
import pl.edu.icm.coansys.citations.util.misc._
import pl.edu.icm.coansys.commons.scala.strings
import pl.edu.icm.coansys.commons.java.DiacriticsRemover.removeDiacritics
import pl.edu.icm.cermine.bibref.BibReferenceParser
import pl.edu.icm.coansys.models.DocumentProtos.{DocumentMetadata, BasicMetadata, ReferenceMetadata}
import pl.edu.icm.coansys.citations.data.CitationMatchingProtos.MatchableEntityData
import pl.edu.icm.cermine.bibref.model.BibEntry
import pl.edu.icm.coansys.citations.util.BytesConverter
import com.nicta.scoobi.core.Grouping
import scala.Some

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class MatchableEntity(val data: MatchableEntityData) {
  def id = data.getId

  def author = removeDiacritics(data.getAuthor)

  def source = removeDiacritics(data.getSource)

  def title = removeDiacritics(data.getTitle)

  def pages = data.getPages

  def year = data.getYear

  def rawText = data.getAuxiliaryList.find(_.getKey == "rawText").map(x => removeDiacritics(x.getValue))

  def normalisedAuthorTokens: Iterable[String] =
    tokensFromCermine(strings.lettersOnly(author))
      .flatMap { tok =>
        if (tok.length <= 3 && tok.forall(_.isUpper))
          tok.toCharArray.map(_.toString)
        else
          Some(tok)
      }
      .filter(_.length > 1)
      .map(_.toLowerCase)
      .toSet

  def toReferenceString: String = List(author, title, source, pages, year).mkString("; ")

  def toDebugString: String =
    "id: " + id + "\n" +
      "author: " + author + "\n" +
      "author tokens: " + normalisedAuthorTokens + "\n" +
      "source: " + source + "\n" +
      "title: " + title + "\n" +
      "pages: " + pages + "\n" +
      "year: " + year + "\n" +
      "raw text: " + rawText + "\n"
}

object MatchableEntity {
  implicit val converter =
    new BytesConverter[MatchableEntity](
      (_.data.toByteArray),
      (x => new MatchableEntity(MatchableEntityData.parseFrom(x))))

  implicit val grouping = new Grouping[MatchableEntity] {
    def groupCompare(x: MatchableEntity, y: MatchableEntity) = scalaz.Ordering.fromInt(x.id compareTo y.id)
  }

  def fromBytes(bytes: Array[Byte]): MatchableEntity = {
    new MatchableEntity(MatchableEntityData.parseFrom(bytes))
  }

  def fromParameters(id: String = "",
                     author: String = "",
                     source: String = "",
                     title: String = "",
                     pages: String = "",
                     year: String = ""): MatchableEntity = {
    val data = MatchableEntityData.newBuilder()
    data.setId(id)
    data.setAuthor(author)
    data.setSource(source)
    data.setTitle(title)
    data.setPages(pages)
    data.setYear(year)

    new MatchableEntity(data.build())
  }

  private def fillUsingBasicMetadata(data: MatchableEntityData.Builder, meta: BasicMetadata) {
    data.setAuthor(meta.getAuthorList.map(a => if (a.hasName) a.getName else a.getForenames + " " + a.getSurname).mkString(" "))
    data.setSource(meta.getJournal)
    data.setTitle(meta.getTitleList.map(_.getText).mkString(" "))
    data.setPages(meta.getPages)
    data.setYear(meta.getYear)
  }

  def fromBasicMetadata(id: String, meta: BasicMetadata): MatchableEntity = {
    val data = MatchableEntityData.newBuilder()
    data.setId(id)
    fillUsingBasicMetadata(data, meta)
    new MatchableEntity(data.build())
  }

  def fromDocumentMetadata(meta: DocumentMetadata): MatchableEntity =
    fromDocumentMetadata("doc_" + meta.getKey, meta)

  def fromDocumentMetadata(id: String, meta: DocumentMetadata): MatchableEntity = {
    val data = MatchableEntityData.newBuilder()
    data.setId(id)
    fillUsingBasicMetadata(data, meta.getBasicMetadata)
    new MatchableEntity(data.build())
  }

  def fromReferenceMetadata(meta: ReferenceMetadata): MatchableEntity =
    fromReferenceMetadata("cit_" + meta.getSourceDocKey + "_" + meta.getPosition, meta)

  def fromReferenceMetadata(id: String, meta: ReferenceMetadata): MatchableEntity = {
    val data = MatchableEntityData.newBuilder()
    data.setId(id)
    fillUsingBasicMetadata(data, meta.getBasicMetadata)
    new MatchableEntity(data.build())
  }

  def fromUnparsedReference(bibReferenceParser: BibReferenceParser[BibEntry], id: String, rawText: String): MatchableEntity = {
    def getField(bibEntry: BibEntry, key: String): String =
      bibEntry.getAllFieldValues(key).mkString(" ")

    val bibEntry = bibReferenceParser.parseBibReference(removeDiacritics(rawText))
    val data = MatchableEntityData.newBuilder()
    data.setId(id)
    data.setAuthor(getField(bibEntry, BibEntry.FIELD_AUTHOR))
    data.setSource(getField(bibEntry, BibEntry.FIELD_JOURNAL))
    data.setTitle(getField(bibEntry, BibEntry.FIELD_TITLE))
    data.setPages(getField(bibEntry, BibEntry.FIELD_PAGES))
    data.setYear(getField(bibEntry, BibEntry.FIELD_YEAR))

    new MatchableEntity(data.build())
  }

  def fromUnparsedReferenceMetadata(bibReferenceParser: BibReferenceParser[BibEntry],
                                    meta: ReferenceMetadata): MatchableEntity =
    fromUnparsedReference(
      bibReferenceParser,
      "cit_" + meta.getSourceDocKey + "_" + meta.getPosition,
      meta.getRawCitationText)


}
