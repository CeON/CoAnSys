package pl.edu.icm.coansys.citations

import com.nicta.scoobi.application.ScoobiApp
import org.apache.hadoop.conf.Configuration

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object IndexTester extends ScoobiApp {
  override def upload = false

  def testIndex(indexUri: String, query: String) {
    val index = new AuthorIndex(indexUri, new Configuration())
    index.getDocumentsByAuthor(query).foreach(println)
  }

  def run() {
    if (args.length != 2) {
      println("Usage: IndexTester <index_path> <query>")
    } else {
      testIndex(args(0), args(1))
    }
  }
}
