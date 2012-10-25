package pl.edu.icm.coansys.citations

import java.net.URI
import com.nicta.scoobi.application.ScoobiApp
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{Path, FileSystem}
import org.apache.hadoop.io._

import pl.edu.icm.coansys.commons.scala.strings.rotations
import pl.edu.icm.coansys.commons.scala.automatic_resource_management.using
import com.nicta.scoobi.core.{WireFormat, DList}
import com.nicta.scoobi.Persist.persist
import com.nicta.scoobi.InputsOutputs.{convertToSequenceFile, toSequenceFile}
import java.io.{DataInput, DataOutput}
import com.nicta.scoobi.io.sequence.SeqSchema
import collection.mutable.ListBuffer
import org.apache.hadoop.io.SequenceFile.Sorter

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object App extends ScoobiApp {
  override def upload = false

  def extractSeqTypes(uri: String): (Class[_], Class[_]) = {
    val conf = new Configuration()
    val fs = FileSystem.get(URI.create(uri), conf)
    val path = new Path(uri)
    using(new SequenceFile.Reader(fs, path, conf)) {
      reader =>
        val keyClass = reader.getKeyClass
        val valueClass = reader.getValueClass
        (keyClass, valueClass)
    }
  }

  def convertSeqToMap(uri: String) {
    val conf = new Configuration()
    val fs = FileSystem.get(URI.create(uri), conf)
    val map = new Path(uri)
    val mapData = new Path(map, MapFile.DATA_FILE_NAME)
    val (keyClass, valueClass) = extractSeqTypes(mapData.toUri.toString)
    MapFile.fix(fs, map, keyClass.asInstanceOf[Class[_ <: Writable]], valueClass.asInstanceOf[Class[_ <: Writable]], false, conf)
  }

  def mergeSeqs(uri: String) {
    val conf = new Configuration()
    val fs = FileSystem.get(URI.create(uri), conf)
    val dir = new Path(uri)
    val paths: Array[Path] = fs.listStatus(dir).map(_.getPath)
    val mapData = new Path(dir, MapFile.DATA_FILE_NAME)
    val (keyClass, valueClass) = extractSeqTypes(paths(0).toUri.toString)
    val sorter = new Sorter(fs, keyClass.asInstanceOf[Class[_ <: WritableComparable[_]]], valueClass, conf)
    sorter.sort(paths, mapData, true)
  }

  def buildIndex(readDocs: () => DList[MockDocumentWrapper], indexFile: String) {
    def indexEntries(allDocs: DList[MockDocumentWrapper]) = {
      val tokensWithDocs =
        allDocs
          .flatMap(d => d.normalisedAuthorTokens zip Iterator.continually(d).toIterable)
          .groupByKey[String, MockDocumentWrapper]

      val rotationsWithDocs = tokensWithDocs.flatMap {
        case (token, docs) =>
          val materializedDocs = docs.toList.toIterable //that's important: if we don't do that docs can be traversed once only
          rotations(token) zip Iterator.continually(materializedDocs).toIterable
      }

      rotationsWithDocs
    }
    implicit object docsIterableSchema extends SeqSchema[Iterable[MockDocumentWrapper]] {
      def toWritable(x: Iterable[MockDocumentWrapper]) = new MockDocumentWritableIterable(x)

      def fromWritable(x: SeqType) =
        x.iterable

      type SeqType = MockDocumentWritableIterable
      val mf = manifest[MockDocumentWritableIterable]
    }
    persist(convertToSequenceFile(indexEntries(readDocs()), indexFile))
    mergeSeqs(indexFile)
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
