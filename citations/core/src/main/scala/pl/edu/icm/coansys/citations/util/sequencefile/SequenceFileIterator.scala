/*
 * This file is part of CoAnSys project.
 * Copyright (c) 20012-2013 ICM-UW
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

package pl.edu.icm.coansys.citations.util.sequencefile

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
      preloaded = reader.next(key, value);
      preloaded
    }

  def next(): (Writable, Writable) = {
    if (!preloaded) {
      reader.next(key, value)
    }
    else {
      preloaded = false
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
