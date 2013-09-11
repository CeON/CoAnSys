/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2013 ICM-UW
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

package pl.edu.icm.coansys.citations.tools.sequencefile

import resource._
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.io.{SequenceFile, Writable}
import org.apache.hadoop.fs.Path
import pl.edu.icm.coansys.citations.util.sequencefile.SequenceFileIterator

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object util {
  def transformAndWrite(conf: Configuration,
                        inUri: String,
                        outUri: String,
                        transformation: Iterator[(Writable, Writable)] => Iterator[(Writable, Writable)]): Int = {
    var written = 0

    for {
      reader <- managed(new SequenceFile.Reader(conf, SequenceFile.Reader.file(new Path(inUri))))
      writer <- managed(SequenceFile.createWriter(conf, SequenceFile.Writer.file(new Path(outUri)),
        SequenceFile.Writer.keyClass(reader.getKeyClass), SequenceFile.Writer.valueClass(reader.getValueClass)))
    } {
      transformation(new SequenceFileIterator(reader)).foreach {
        case (key, value) =>
          writer.append(key, value)
          written = written + 1
      }
    }

    written
  }
}
