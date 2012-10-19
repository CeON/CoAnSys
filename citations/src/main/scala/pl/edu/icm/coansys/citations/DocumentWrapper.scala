package pl.edu.icm.coansys.citations

import org.apache.hadoop.io.Writable

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
trait DocumentWrapper extends Writable {
  def id: String
  def normalisedAuthorTokens: Iterable[String]
}
