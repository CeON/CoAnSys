/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.tools.sequencefile

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.io.{SequenceFile, Writable}
import pl.edu.icm.coansys.commons.scala.automatic_resource_management._
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

    using(new SequenceFile.Reader(conf, SequenceFile.Reader.file(new Path(inUri)))) {
      reader =>
        using(SequenceFile.createWriter(conf, SequenceFile.Writer.file(new Path(outUri)),
          SequenceFile.Writer.keyClass(reader.getKeyClass), SequenceFile.Writer.valueClass(reader.getValueClass))) {
          writer =>
            transformation(new SequenceFileIterator(reader)).foreach {
              case (key, value) =>
                writer.append(key, value)
                written = written + 1
            }
        }
    }

    written
  }
}
