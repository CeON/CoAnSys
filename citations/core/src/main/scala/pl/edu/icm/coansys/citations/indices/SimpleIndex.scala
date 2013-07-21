/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.indices

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.io.{WritableComparable, Writable, MapFile}
import org.apache.hadoop.fs.Path
import com.nicta.scoobi.core.DList
import pl.edu.icm.coansys.citations.data.MatchableEntity
import pl.edu.icm.coansys.citations.util.hdfs
import com.nicta.scoobi.Scoobi._
import scala.Some
import org.apache.commons.io.FileUtils

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class SimpleIndex[K <: WritableComparable[_] : Manifest, V <: Writable : Manifest](val indexFileUri: String, val useDistributedCache: Boolean) {
  val conf = new Configuration()
  val reader =
  if (useDistributedCache) {
    val path = new java.io.File(new java.io.File(".").listFiles().map(_.getCanonicalPath).filter(_.endsWith(indexFileUri + "/" + MapFile.DATA_FILE_NAME)).head).getParent
    val indexPathCandidates = new java.io.File(".").listFiles().map(_.getCanonicalPath).filter(_.endsWith(indexFileUri + "/" + MapFile.INDEX_FILE_NAME))
    if (indexPathCandidates.length > 0) {
      val indexPath = new java.io.File(indexPathCandidates.head).getParent
      FileUtils.moveFile(new java.io.File(indexPath, MapFile.INDEX_FILE_NAME), new java.io.File(path, MapFile.INDEX_FILE_NAME))
    }
    new MapFile.Reader(new Path("file://" + path), conf)
  } else {
    new MapFile.Reader(new Path(indexFileUri), conf)
  }


  def get(key: K): Option[V] = {
    val value = manifest[V].erasure.newInstance().asInstanceOf[V]
    val v = reader.get(key, value)
    if (v != null)
      Some(v.asInstanceOf[V])
    else
      None
  }

  def close() {
    if (reader != null)
      reader.close()
  }
}

object SimpleIndex {
  def buildKeyIndex(documents: DList[MatchableEntity], indexFile: String)(implicit conf: ScoobiConfiguration) {
    persist(toSequenceFile(documents.map(doc => (doc.id, doc)), indexFile))
    hdfs.mergeSeqs(indexFile)
    hdfs.convertSeqToMap(indexFile)
  }
}
