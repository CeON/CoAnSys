package pl.edu.icm.coansys.citations

import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentMetadata

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class AuthorIndex(val indexFileUri: String) {
  private val index = new ApproximateIndex[BytesIterable](indexFileUri)

  def getDocumentsByAuthor(author: String): Iterable[DocumentMetadataWrapper] = {
    index.get(author).flatMap(_.iterable map (bs => new DocumentMetadataWrapper(DocumentMetadata.parseFrom(bs))))
  }

}
