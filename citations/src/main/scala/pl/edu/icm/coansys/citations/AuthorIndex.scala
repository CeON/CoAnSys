package pl.edu.icm.coansys.citations

import org.apache.hadoop.io.Text

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class AuthorIndex(val indexFileUri: String) {
  private val index = new ApproximateIndex[BytesIterable](indexFileUri)

  def getDocumentsByAuthor(author: String): Iterable[String] = {
    index.getApproximate(author).flatMap(_.iterable map (Text.decode(_))).toSet
  }

}
