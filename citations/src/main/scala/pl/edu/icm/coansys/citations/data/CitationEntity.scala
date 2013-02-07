/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.data

import collection.JavaConversions._
import pl.edu.icm.coansys.commons.scala.strings._
import pl.edu.icm.coansys.citations.util.ngrams._
import pl.edu.icm.coansys.importers.models.DocumentProtos.{TextWithLanguage, Author, BasicMetadata, ReferenceMetadata}
import pl.edu.icm.cermine.bibref.model.BibEntry
import pl.edu.icm.coansys.citations.util.BytesConverter
import com.nicta.scoobi.core.Grouping
import pl.edu.icm.cermine.bibref.BibReferenceParser

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
trait CitationEntity extends Entity {
  def entityId: String = "cit-" + sourceDocKey + "-" + position

  def sourceDocKey: String

  def position: Int

  def rawText: String

  override def similarityFeaturesWith(other: Entity): List[Double] = {
    val base = super.similarityFeaturesWith(other)
    if (other.isInstanceOf[CitationEntity]) {
      val overallMatchFactor =
        trigramSimilarity(rawText, other.asInstanceOf[CitationEntity].rawText)
      val lettersMatchFactor =
        trigramSimilarity(lettersOnly(rawText), lettersOnly(other.asInstanceOf[CitationEntity].rawText))
      val digitsMatchFactor =
        trigramSimilarity(digitsOnly(rawText), digitsOnly(other.asInstanceOf[CitationEntity].rawText))

      base ++ List(overallMatchFactor, lettersMatchFactor, digitsMatchFactor)
    } else {
      base
    }
  }
}

object CitationEntity {
  def fromBytes(bytes: Array[Byte]) =
    fromReferenceMetadata(ReferenceMetadata.parseFrom(bytes))

  def fromReferenceMetadata(meta: ReferenceMetadata): CitationEntity =
    new CitationEntityImpl(meta)

  def fromUnparsedReferenceMetadata(bibReferenceParser: BibReferenceParser[BibEntry], meta: ReferenceMetadata): CitationEntity = {
    def getField(bibEntry: BibEntry, key: String): String =
      bibEntry.getAllFieldValues(key).mkString(" ")

    val bibEntry = bibReferenceParser.parseBibReference(meta.getRawCitationText)
    val builder = ReferenceMetadata.newBuilder(meta)

    val basicMetadata = BasicMetadata.newBuilder
    basicMetadata.clearAuthor()
    for ((author, key) <- bibEntry.getAllFieldValues(BibEntry.FIELD_AUTHOR).toIterable.zipWithIndex) {
      basicMetadata.addAuthor(Author.newBuilder.setName(author).setKey(key.toString))
    }
    basicMetadata.setJournal(getField(bibEntry, BibEntry.FIELD_JOURNAL))
    basicMetadata.setPages(getField(bibEntry, BibEntry.FIELD_PAGES))
    basicMetadata.setYear(getField(bibEntry, BibEntry.FIELD_YEAR))
    basicMetadata.addTitle(TextWithLanguage.newBuilder.setText(getField(bibEntry, BibEntry.FIELD_TITLE)))
    builder.setBasicMetadata(basicMetadata)

    new CitationEntityImpl(builder.build())
  }

  implicit val converter =
    new BytesConverter[CitationEntity](
      (_.toTypedBytes),
      (Entity.fromTypedBytes(_).asInstanceOf[CitationEntity]))

  implicit val grouping = new Grouping[CitationEntity] {
    def groupCompare(x: CitationEntity, y: CitationEntity) = x.entityId compareTo y.entityId
  }
}
