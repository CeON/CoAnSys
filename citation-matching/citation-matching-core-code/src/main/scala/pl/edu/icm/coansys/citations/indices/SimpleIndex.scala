/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2015 ICM-UW
 * 
 * CoAnSys is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * CoAnSys is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with CoAnSys. If not, see <http://www.gnu.org/licenses/>.
 */

package pl.edu.icm.coansys.citations.indices

import com.nicta.scoobi.Scoobi._
import com.nicta.scoobi.core.DList
import java.io.IOException
import org.apache.commons.io.FileUtils
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.{BytesWritable, WritableComparable, Writable, MapFile}
import org.slf4j.LoggerFactory
import pl.edu.icm.coansys.citations.data.MatchableEntity
import scala.Some
import pl.edu.icm.ceon.scala_commons.hadoop.sequencefile

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class SimpleIndex[K <: WritableComparable[_] : Manifest, V <: Writable : Manifest](val indexFileUri: String, val useDistributedCache: Boolean) {
  private val logger = LoggerFactory.getLogger(SimpleIndex.getClass)

  val conf = new Configuration()
  val reader =
  if (useDistributedCache) {
    val path = new java.io.File(new java.io.File(".").listFiles().map(_.getCanonicalPath).filter(_.endsWith(indexFileUri + "/" + MapFile.DATA_FILE_NAME)).head).getParent
    val indexPathCandidates = new java.io.File(".").listFiles().map(_.getCanonicalPath).filter(_.endsWith(indexFileUri + "/" + MapFile.INDEX_FILE_NAME))
    if (indexPathCandidates.length > 0) {
      val indexPath = new java.io.File(indexPathCandidates.head).getParent
      try {
        FileUtils.moveFile(
          new java.io.File(indexPath, MapFile.INDEX_FILE_NAME),
          new java.io.File(path, MapFile.INDEX_FILE_NAME))
      } catch {
        case e: IOException => logger.warn("Possible error when moving a index file", e)
      }
    }
    new MapFile.Reader(new Path("file://" + path), conf)
  } else {
    new MapFile.Reader(new Path(indexFileUri), conf)
  }


  def get(key: K): Option[V] = {
    val value = manifest[V].runtimeClass.newInstance().asInstanceOf[V]
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
    documents.map(doc => (doc.id, doc)).toSequenceFile(indexFile).persist
    sequencefile.mergeWithScoobi[String, BytesWritable](indexFile)
    sequencefile.convertToMapFile(indexFile)(conf)
  }
}
