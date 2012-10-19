package pl.edu.icm.coansys.citations

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class MockDocumentWrapper(val id: String, val authors: String) extends DocumentWrapper {
  def normalisedAuthorTokens = authors.toLowerCase.split("""\s+""")
}
