/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.data

import pl.edu.icm.coansys.importers.models.DocumentProtos.ReferenceMetadata
import com.nicta.scoobi.core.Grouping
import pl.edu.icm.coansys.citations.util.{misc, BytesConverter}

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class CitationWrapper(val meta: ReferenceMetadata) {
  def normalisedAuthorTokens: Iterable[String] = {
    if (meta.getBasicMetadata.getAuthorCount > 0)
      misc.normalizedAuthorTokensFromAuthorList(meta.getBasicMetadata)
    else
      meta.getRawCitationText.split( """[^\p{L}]+""").filter(_.length > 1).toSet
  }

  override def equals(other: Any): Boolean = other match {
    case that: CitationWrapper =>
      meta.getSourceDocKey == that.meta.getSourceDocKey && meta.getPosition == that.meta.getPosition
    case _ => false
  }

  override def hashCode = {
    val prime = 11
    meta.getSourceDocKey.hashCode * prime + meta.getPosition
  }
}

object CitationWrapper {
  implicit val converter =
    new BytesConverter[CitationWrapper](
      (_.meta.toByteArray),
      (b => new CitationWrapper(ReferenceMetadata.parseFrom(b))))

  implicit val grouping = new Grouping[CitationWrapper] {
    def groupCompare(x: CitationWrapper, y: CitationWrapper) = {
      val c1 = x.meta.getSourceDocKey.compare(y.meta.getSourceDocKey)
      if (c1 == 0)
        x.meta.getPosition.compare(y.meta.getPosition)
      else
        c1
    }
  }
}
