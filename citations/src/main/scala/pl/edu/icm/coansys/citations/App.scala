package pl.edu.icm.coansys.citations

import java.net.URI
import com.nicta.scoobi.application.ScoobiApp
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{Path, FileSystem}
import org.apache.hadoop.io.{SequenceFile, MapFile, Writable}

import pl.edu.icm.coansys.commons.scala.strings.rotations
import pl.edu.icm.coansys.commons.scala.automatic_resource_management.using
import com.nicta.scoobi.core.DList
import com.nicta.scoobi.Persist.persist
import com.nicta.scoobi.InputsOutputs.toSequenceFile

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object App extends ScoobiApp {
  override def upload = false

  def convertSeqToMap(uri: String) {
    val conf = new Configuration()
    val fs = FileSystem.get(URI.create(uri), conf)
    val map = new Path(uri)
    val mapData = new Path(map, MapFile.DATA_FILE_NAME)
    using(new SequenceFile.Reader(fs, mapData, conf)) {
      reader =>
        val keyClass = reader.getKeyClass
        val valueClass = reader.getValueClass
        MapFile.fix(fs, map, keyClass.asInstanceOf[Class[_ <: Writable]], valueClass.asInstanceOf[Class[_ <: Writable]], false, conf)
    }
  }

  def buildIndex(readDocs: () => DList[DocumentWrapper], indexFile: String) {
    //    def readDocs(): TypedPipe[DocumentWrapper] = throw new RuntimeException
    def indexEntries(allDocs: DList[DocumentWrapper]) = {
      val tokensWithDocs =
        allDocs
          .flatMap(d => d.normalisedAuthorTokens zip Iterator.continually(d).toIterable)
          .groupByKey[String, DocumentWrapper]

      val rotationsWithDocs = tokensWithDocs.flatMap {
        case (token, docs) => rotations(token) zip Iterator.continually(docs).toIterable
      }

      rotationsWithDocs
    }
    persist(toSequenceFile(indexEntries(readDocs()), indexFile))
    convertSeqToMap(indexFile)
  }

  def doMatching() {
    throw new RuntimeException
  }

  def mockReadDocs(): DList[DocumentWrapper] = DList.apply[DocumentWrapper](
    new MockDocumentWrapper("1", "aaa bbb"),
    new MockDocumentWrapper("2", "bbb ccc"),
    new MockDocumentWrapper("3", "aaa ddd"),
    new MockDocumentWrapper("4", "bbb eee"),
    new MockDocumentWrapper("5", "abc adb"),
    new MockDocumentWrapper("6", "ewr fds"),
    new MockDocumentWrapper("7", "sda czx"),
    new MockDocumentWrapper("8", "aca bba")
  )

  def run() {
    println(args)

    buildIndex(mockReadDocs, args(0))
    //doMatching()
  }
}
