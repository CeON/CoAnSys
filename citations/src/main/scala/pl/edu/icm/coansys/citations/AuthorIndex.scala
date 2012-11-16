package pl.edu.icm.coansys.citations

import org.apache.hadoop.io.Text
import org.apache.hadoop.conf.Configuration

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class AuthorIndex(val indexFileUri: String, val conf: Configuration) {
  private val index = new ApproximateIndex[BytesIterable](indexFileUri, conf)

  def getDocumentsByAuthor(author: String): Iterable[String] = {
    index.get(author).flatMap(_.iterable map (Text.decode(_))).toSet
  }

}
