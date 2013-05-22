/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.util

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{Path, FileSystem}
import java.net.URI
import pl.edu.icm.coansys.commons.scala.automatic_resource_management._
import org.apache.hadoop.io.{WritableComparable, Writable, MapFile, SequenceFile}
import org.apache.hadoop.io.SequenceFile.Sorter

/**
 * A collection of functions used to manipulate hdfs files
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object hdfs {
  /**
   * Reads from a SequenceFile its key and value types
   * @return a pair of key and value type
   */
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

  /**
   * Converts SequenceFile to a MapFile.
   */
  def convertSeqToMap(uri: String) {
    val conf = new Configuration()
    val fs = FileSystem.get(URI.create(uri), conf)
    val map = new Path(uri)
    val mapContents = fs.listStatus(map).head.getPath
    val mapData = new Path(map, MapFile.DATA_FILE_NAME)
    fs.rename(mapContents, mapData)
    val (keyClass, valueClass) = extractSeqTypes(mapData.toUri.toString)
    MapFile.fix(fs, map, keyClass.asInstanceOf[Class[_ <: Writable]], valueClass.asInstanceOf[Class[_ <: Writable]], false, conf)
  }

  /**
   * Merges and sorts all SequenceFiles in given directory.
   */
  def mergeSeqs(uri: String) {
    val conf = new Configuration()
    val fs = FileSystem.get(URI.create(uri), conf)
    val dir = new Path(uri)
    val paths: Array[Path] = fs.listStatus(dir).map(_.getPath).filterNot(_.getName.startsWith("_"))
    val mapData = new Path(dir, MapFile.DATA_FILE_NAME)
    val (keyClass, valueClass) = extractSeqTypes(paths(0).toUri.toString)
    val sorter = new Sorter(fs, keyClass.asInstanceOf[Class[_ <: WritableComparable[_]]], valueClass, conf)
    sorter.setMemory(128 * 1000 * 1000)
    sorter.sort(paths, mapData, true)
  }

}
