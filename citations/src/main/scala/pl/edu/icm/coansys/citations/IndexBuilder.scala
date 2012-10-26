package pl.edu.icm.coansys.citations

import com.nicta.scoobi.application.ScoobiApp
import com.nicta.scoobi.core.DList


/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object IndexBuilder extends ScoobiApp {
  override def upload = false

  def doMatching() {
    throw new RuntimeException
  }

  def mockReadDocs(): DList[MockDocumentWrapper] = DList.apply[MockDocumentWrapper](
    new MockDocumentWrapper("1", "aaa bbb"),
    new MockDocumentWrapper("2", "bbb ccc"),
    new MockDocumentWrapper("3", "aaa ddd"),
    new MockDocumentWrapper("4", "bbb eee"),
    new MockDocumentWrapper("5", "abc adb"),
    new MockDocumentWrapper("6", "ewr fds"),
    new MockDocumentWrapper("7", "sda czx"),
    new MockDocumentWrapper("8", "aca bba"),
    new MockDocumentWrapper("9", "aa bba")
  )

  def run() {
    println(args)

    ApproximateIndex.buildIndex(mockReadDocs, args(0))
    //doMatching()
  }
}
