package pl.edu.icm.coansys.citations

import scala.collection.JavaConversions._
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentMetadata

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class DocumentMetadataWrapper(val meta: DocumentMetadata) {
  def id: String = meta.getKey

  def normalisedAuthorTokens: Iterable[String] = {
    meta.getAuthorList.toIterable
      .flatMap {
      author =>
        List(
          author.getName,
          author.getForenames,
          author.getSurname).flatMap(_.split( """\s+"""))
    }
      .filterNot(_.isEmpty)
      .toSet
  }

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