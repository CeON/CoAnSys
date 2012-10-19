package pl.edu.icm.coansys.citations

import com.twitter.scalding._
import org.apache.hadoop
import hadoop.io.{Writable, MapFile}
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{Path, FileSystem}
import java.net.URI
import pl.edu.icm.coansys.commons.scala.strings.rotations
import pl.edu.icm.coansys.commons.scala.automatic_resource_management.using
import com.twitter.scalding.SequenceFile
import cascading.tuple.Fields

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class Main(args: Args) extends Job(args) {

  def convertSeqToMap(uri: String) {
    val conf = new Configuration()
    val fs = FileSystem.get(URI.create(uri), conf)
    val map = new Path(uri)
    val mapData = new Path(map, MapFile.DATA_FILE_NAME)
    using(new hadoop.io.SequenceFile.Reader(fs, mapData, conf)) {
      reader =>
        val keyClass = reader.getKeyClass
        val valueClass = reader.getValueClass
        MapFile.fix(fs, map, keyClass.asInstanceOf[Class[_ <: Writable]], valueClass.asInstanceOf[Class[_ <: Writable]], false, conf)
    }
  }

  def buildIndex(readDocs: () => TypedPipe[DocumentWrapper], indexFile: String) {
//    def readDocs(): TypedPipe[DocumentWrapper] = throw new RuntimeException
    def indexEntries(allDocs: TypedPipe[DocumentWrapper]) = {
      val tokensWithDocs =
        allDocs
          .flatMap(d => d.normalisedAuthorTokens zip Iterator.continually(d).toIterable)
          .groupBy(_._1)
          .mapValues(_._2)
          .toList

      val rotationsWithDocs = tokensWithDocs.flatMap {
        case (token, docs) => rotations(token) zip Iterator.continually(docs).toIterable
      }

      rotationsWithDocs
    }

//    indexEntries(readDocs()).write((0, 1), SequenceFile(indexFile))
//    convertSeqToMap(indexFile)
  }

  def doMatching() {
    throw new RuntimeException
  }

  def mockReadDocs(): TypedPipe[DocumentWrapper] = {
    val docs = List(
      new MockDocumentWrapper("1", "aaa bbb"),
      new MockDocumentWrapper("2", "bbb ccc"),
      new MockDocumentWrapper("3", "aaa ddd"),
      new MockDocumentWrapper("4", "bbb eee"),
      new MockDocumentWrapper("5", "abc adb"),
      new MockDocumentWrapper("6", "ewr fds"),
      new MockDocumentWrapper("7", "sda czx"),
      new MockDocumentWrapper("8", "aca bba")
    )
    TypedPipe.from(IterableSource(docs))
  }

  def mockReadStrings(): TypedPipe[String] =
    TypedPipe.from(TextLine(args("input")))


  mockReadStrings().map(d => (d, d)).write((0, 1), SequenceFile(args("indexFile")))
//  buildIndex(mockReadDocs, args("indexFile"))
//  doMatching()
}
