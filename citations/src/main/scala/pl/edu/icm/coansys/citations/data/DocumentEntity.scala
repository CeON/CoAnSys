/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.data

import pl.edu.icm.coansys.importers.models.DocumentProtos._

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
trait DocumentEntity extends Entity {
  def entityId: String = "doc-" + docId

  def docId: String

  def extId: String
}

object DocumentEntity {
  def fromBytes(bytes: Array[Byte]): DocumentEntity =
    fromDocumentMetadata(DocumentMetadata.parseFrom(bytes))

  def fromDocumentMetadata(meta: DocumentMetadata) =
    new DocumentEntityImpl(meta)
}
