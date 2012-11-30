/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.data

import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentMetadata
import com.nicta.scoobi.core.Grouping
import pl.edu.icm.coansys.citations.util.{misc, BytesConverter}

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class CitationWrapper(val meta: DocumentMetadata) {
  def normalisedAuthorTokens: Iterable[String] = {
    if (meta.getAuthorCount > 0)
      misc.normalizedAuthorTokensFromAuthorList(meta)
    else
      meta.getText.split( """[^\p{L}]+""").filter(_.length > 1).toSet
  }

  override def equals(other: Any): Boolean = other match {
    case that: DocumentMetadataWrapper => meta.getKey == that.meta.getKey
    case _ => false
  }

  override def hashCode = meta.getKey.hashCode
}

object CitationWrapper {
  implicit val converter =
    new BytesConverter[CitationWrapper](
      (_.meta.toByteArray),
      (b => new CitationWrapper(DocumentMetadata.parseFrom(b))))

  implicit val grouping = new Grouping[CitationWrapper] {
    def groupCompare(x: CitationWrapper, y: CitationWrapper) = x.meta.getKey.compare(y.meta.getKey)
  }
}
