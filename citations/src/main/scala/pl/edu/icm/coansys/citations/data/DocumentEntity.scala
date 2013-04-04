/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.data

import pl.edu.icm.coansys.importers.models.DocumentProtos._

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
trait DocumentEntityOld extends EntityOld {
  def entityId: String = "doc-" + docId

  def docId: String

  def extId: String
}

object DocumentEntityOld {
  def fromBytes(bytes: Array[Byte]): DocumentEntityOld =
    fromDocumentMetadata(DocumentMetadata.parseFrom(bytes))

  def fromDocumentMetadata(meta: DocumentMetadata) =
    new DocumentEntityImplOld(meta)
}
