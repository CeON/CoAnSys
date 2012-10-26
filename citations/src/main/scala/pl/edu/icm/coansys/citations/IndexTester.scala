package pl.edu.icm.coansys.citations

import com.nicta.scoobi.application.ScoobiApp

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object IndexTester extends ScoobiApp {
  def testIndex(indexUri: String) {
    val index = new ApproximateIndex[MockDocumentWritableIterable](indexUri)
    index.get("aaa").flatMap(_.iterable).foreach(x => println(x.id))
    println()
    index.get("bbb").flatMap(_.iterable).foreach(x => println(x.id))

  }

  def run() {
    testIndex(args(0))
  }
}
