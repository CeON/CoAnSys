package pl.edu.icm.coansys.citations

import java.net.URI
import com.nicta.scoobi.application.ScoobiApp
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{Path, FileSystem}
import org.apache.hadoop.io.{SequenceFile, MapFile, Writable}

import pl.edu.icm.coansys.commons.scala.strings.rotations
import pl.edu.icm.coansys.commons.scala.automatic_resource_management.using
import com.nicta.scoobi.core.{WireFormat, DList}
import com.nicta.scoobi.Persist.persist
import com.nicta.scoobi.InputsOutputs.{convertToSequenceFile, toSequenceFile}
import java.io.{DataInput, DataOutput}
import com.nicta.scoobi.io.sequence.SeqSchema
import collection.mutable.ListBuffer

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
    using(new SequenceFile.Reader(fs, map, conf)) {
      reader =>
        val keyClass = reader.getKeyClass
        val valueClass = reader.getValueClass
        MapFile.fix(fs, map, keyClass.asInstanceOf[Class[_ <: Writable]], valueClass.asInstanceOf[Class[_ <: Writable]], false, conf)
    }
  }

  def buildIndex(readDocs: () => DList[MockDocumentWrapper], indexFile: String) {
    //    def readDocs(): TypedPipe[MockDocumentWrapper] = throw new RuntimeException
    def indexEntries(allDocs: DList[MockDocumentWrapper]) = {
      val tokensWithDocs =
        allDocs
          .flatMap(d => d.normalisedAuthorTokens zip Iterator.continually(d).toIterable)
          .groupByKey[String, MockDocumentWrapper]

      val rotationsWithDocs = tokensWithDocs.flatMap {
        case (token, docs) => rotations(token) zip Iterator.continually(docs).toIterable
      }

      rotationsWithDocs
    }
    implicit val docsIterableSchema = new IterableSchema(new MockDocumentWrapper())
    persist(convertToSequenceFile(indexEntries(readDocs()), indexFile))
    convertSeqToMap(indexFile)
  }

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
    new MockDocumentWrapper("8", "aca bba")
  )

  def run() {
    println(args)

    buildIndex(mockReadDocs, args(0))
    //doMatching()
  }
}
