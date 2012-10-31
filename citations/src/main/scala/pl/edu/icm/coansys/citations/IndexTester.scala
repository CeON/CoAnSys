package pl.edu.icm.coansys.citations

import com.nicta.scoobi.application.ScoobiApp

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object IndexTester extends ScoobiApp {
  override def upload = false

  def testIndex(indexUri: String) {
    val index = new AuthorIndex(indexUri)
    index.getDocumentsByAuthor("aaa").foreach(x => println(x.id))
    println()
    index.getDocumentsByAuthor("bbb").foreach(x => println(x.id))

  }

  def run() {
    testIndex(args(0))
  }
}
