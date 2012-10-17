package pl.edu.icm.coansys.citations

import com.twitter.scalding.{SequenceFile, TypedPipe, Args, Job}
import commons.strings.rotations
import org.apache.hadoop
import hadoop.io.{Writable, MapFile}
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{Path, FileSystem}
import java.net.URI

import pl.edu.icm.coansys.commons.scala.automatic_resource_management.using

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

  def buildIndex() {
    def readDocs(): TypedPipe[DocumentWrapper] = throw new RuntimeException
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

    val tmpSeqFile = throw new RuntimeException
    indexEntries(readDocs()).write((0, 1), new SequenceFile(tmpSeqFile))
    convertSeqToMap(tmpSeqFile)
  }

  def doMatching() {
    throw new RuntimeException
  }

  buildIndex()
  doMatching()
}
