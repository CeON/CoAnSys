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
import pl.edu.icm.coansys.disambiguation.auxil.DiacriticsRemover.removeDiacritics
import com.nicta.scoobi.core.Grouping
import pl.edu.icm.cermine.bibref.BibReferenceParser


/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
trait CitationEntityOld extends EntityOld {
  def entityId: String = "cit-" + sourceDocKey + "-" + position

  def sourceDocKey: String

  def position: Int

  def rawText: String

  override def toReferenceString: String = rawText

  override def similarityFeaturesWith(other: EntityOld): List[Double] = {
    val base = super.similarityFeaturesWith(other)
    if (other.isInstanceOf[CitationEntityOld]) {
      val overallMatchFactor =
        trigramSimilarity(rawText, other.asInstanceOf[CitationEntityOld].rawText)
      val lettersMatchFactor =
        trigramSimilarity(lettersOnly(rawText), lettersOnly(other.asInstanceOf[CitationEntityOld].rawText))
      val digitsMatchFactor =
        trigramSimilarity(digitsOnly(rawText), digitsOnly(other.asInstanceOf[CitationEntityOld].rawText))

      base ++ List(overallMatchFactor, lettersMatchFactor, digitsMatchFactor)
    } else {
      base
    }
  }
}

object CitationEntityOld {
  def fromBytes(bytes: Array[Byte]) =
    fromReferenceMetadata(ReferenceMetadata.parseFrom(bytes))

  def fromReferenceMetadata(meta: ReferenceMetadata): CitationEntityOld =
    new CitationEntityImplOld(meta)

  def fromUnparsedReferenceMetadata(bibReferenceParser: BibReferenceParser[BibEntry], meta: ReferenceMetadata): CitationEntityOld = {
    def getField(bibEntry: BibEntry, key: String): String =
      bibEntry.getAllFieldValues(key).mkString(" ")

    val bibEntry = bibReferenceParser.parseBibReference(removeDiacritics(meta.getRawCitationText))
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

    new CitationEntityImplOld(builder.build())
  }

  implicit val converter =
    new BytesConverter[CitationEntityOld](
      (_.toTypedBytes),
      (EntityOld.fromTypedBytes(_).asInstanceOf[CitationEntityOld]))

  implicit val grouping = new Grouping[CitationEntityOld] {
    def groupCompare(x: CitationEntityOld, y: CitationEntityOld) = x.entityId compareTo y.entityId
  }
}
