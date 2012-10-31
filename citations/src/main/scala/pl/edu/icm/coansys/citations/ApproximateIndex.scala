package pl.edu.icm.coansys.citations

import scala.collection.JavaConversions._
import pl.edu.icm.coansys.commons.scala.strings.rotations
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.{Writable, Text, MapFile}
import collection.mutable.ListBuffer
import com.nicta.scoobi.core.DList
import com.nicta.scoobi.io.sequence.SeqSchema
import com.nicta.scoobi.Persist.persist
import com.nicta.scoobi.InputsOutputs.convertToSequenceFile
import com.nicta.scoobi.application.ScoobiConfiguration
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentMetadata

/**
 * A class helping in approximate index saved in MapFile usage.
 *
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class ApproximateIndex[V <: Writable : Manifest](val indexFileUri: String) {
  val conf = new Configuration()
  val reader = new MapFile.Reader(new Path(indexFileUri), conf)

  def get(query: String): Iterable[V] = {
    def isTooBig(query: String, key:String): Boolean =
      !key.startsWith(query.substring(0, query.length - 1))

    def isMatching(query: String, key: String): Boolean =
      (key.startsWith(query.substring(0, query.length - 1)) && key.length <= query.length) ||
        (key.startsWith(query) && key.length <= query.length + 1)

    def addIfMatches(query: String, key: Text, value: V, buffer: ListBuffer[V]): V = {
      val keyStr = key.toString
      if (isMatching(query, keyStr)) {
        buffer.append(value)
        manifest[V].erasure.newInstance().asInstanceOf[V]
      }
      else {
        value
      }
    }

    val rots = rotations(query + ApproximateIndex.endOfWordMarker)
    val buffer = new ListBuffer[V]
    val k: Text = new Text()
    var v: V = manifest[V].erasure.newInstance().asInstanceOf[V]
    val tmpkey: Text = new Text()

    rots foreach {rot =>
      tmpkey.set(rot.substring(0, rot.length - 1))
      val fstkey = reader.getClosest(tmpkey, v, true).asInstanceOf[Text]
      if (fstkey != null) {
        v = addIfMatches(rot, fstkey, v, buffer)
      }

      var exit = false
      while(reader.next(k, v) && !exit) {
        if (isTooBig(rot, k.toString)) {
          exit = true
        } else {
          v = addIfMatches(rot, k, v, buffer)
        }
      }
    }

    buffer
  }

  def close() {
    if (reader != null)
      reader.close()
  }
}

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object ApproximateIndex {
  private val endOfWordMarker: String = "$"

  /**
   * MR jobs building an approximate index.
   *
   * @param readDocs a procedure returning documents to be indexed
   * @param indexFile an URI of location where a MapFile representing an index will be saved
   */
  def buildAuthorIndex(readDocs: () => DList[DocumentMetadataWrapper], indexFile: String)(implicit conf: ScoobiConfiguration) {
    def indexEntries(allDocs: DList[DocumentMetadataWrapper]) = {
      val tokensWithDocs =
        allDocs
          .flatMap(d => d.normalisedAuthorTokens zip Iterator.continually(d).toIterable)
          .groupByKey[String, DocumentMetadataWrapper]

      val rotationsWithDocs = tokensWithDocs.flatMap {
        case (token, docs) =>
          // The next line is important. The supplied iterable can be traversed once only. If we didn't convert it to a
          // list, we'd end up with a bunch of empty iterators.
          val materializedDocs = docs.toList.toIterable
          rotations(token + endOfWordMarker) zip Iterator.continually(materializedDocs).toIterable
      }

      rotationsWithDocs
    }

    implicit object docsIterableSchema extends SeqSchema[Iterable[DocumentMetadataWrapper]] {
      def toWritable(x: Iterable[DocumentMetadataWrapper]) = new BytesIterable(x map (_.meta.toByteArray))

      def fromWritable(x: SeqType) =
        x.iterable map (bs => new DocumentMetadataWrapper(DocumentMetadata.parseFrom(bs)))

      type SeqType = BytesIterable
      val mf = manifest[BytesIterable]
    }
    persist(convertToSequenceFile(indexEntries(readDocs()), indexFile))
    hdfs.mergeSeqs(indexFile)
    hdfs.convertSeqToMap(indexFile)
  }
}
