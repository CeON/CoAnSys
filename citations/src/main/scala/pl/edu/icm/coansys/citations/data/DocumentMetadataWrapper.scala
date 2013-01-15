/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.data

import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentMetadata
import pl.edu.icm.coansys.citations.util.{BytesConverter, misc}

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class DocumentMetadataWrapper(val meta: DocumentMetadata) {
  def id: String = meta.getKey

  def normalisedAuthorTokens: Iterable[String] =
    misc.normalizedAuthorTokensFromAuthorList(meta.getBasicMetadata)

  override def equals(other: Any): Boolean = other match {
    case that: DocumentMetadataWrapper => id == that.id
    case _ => false
  }

  override def hashCode = id.hashCode
}

object DocumentMetadataWrapper {
  implicit val converter =
    new BytesConverter[DocumentMetadataWrapper](
      (_.meta.toByteArray),
      (b => new DocumentMetadataWrapper(DocumentMetadata.parseFrom(b))))

  implicit def fromDocumentMetadata(meta: DocumentMetadata): DocumentMetadataWrapper = new DocumentMetadataWrapper(meta)
}