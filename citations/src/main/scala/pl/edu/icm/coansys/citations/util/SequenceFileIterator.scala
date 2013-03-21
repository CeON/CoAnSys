/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.util

import org.apache.hadoop.io.{SequenceFile, Writable}
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class SequenceFileIterator(reader: SequenceFile.Reader) extends Iterator[(Writable, Writable)] {
  private val key = reader.getKeyClass.newInstance().asInstanceOf[Writable]
  private val value = reader.getValueClass.newInstance().asInstanceOf[Writable]
  private var preloaded = false

  def hasNext =
    preloaded || {
      preloaded = reader.next(key, value); preloaded
    }

  def next(): (Writable, Writable) = {
    if (!preloaded) {
      reader.next(key, value)
    }
    return (key, value)
  }

  def close() {
    reader.close()
  }
}

object SequenceFileIterator {
  def fromUri(conf: Configuration, uri: String): SequenceFileIterator = {
    val reader = new SequenceFile.Reader(conf, SequenceFile.Reader.file(new Path(uri)))
    new SequenceFileIterator(reader)
  }
}