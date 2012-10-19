package pl.edu.icm.coansys.citations

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
trait DocumentWrapper {
  def id: String
  def normalisedAuthorTokens: Iterable[String]
}
