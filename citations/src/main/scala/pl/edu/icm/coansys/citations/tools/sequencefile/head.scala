/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.tools.sequencefile

import pl.edu.icm.coansys.commons.scala.automatic_resource_management._
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.io.{Writable, SequenceFile}
import org.apache.hadoop.fs.Path

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object Head {
  def main(args: Array[String]) {
    val n = args(0).toInt
    val inUri = args(1)
    val outUri = args(2)
    val conf = new Configuration()
    var written = 0

    using(new SequenceFile.Reader(conf, SequenceFile.Reader.file(new Path(inUri)))) {
      reader =>
        using(SequenceFile.createWriter(conf, SequenceFile.Writer.file(new Path(outUri)),
          SequenceFile.Writer.keyClass(reader.getKeyClass), SequenceFile.Writer.valueClass(reader.getValueClass))) {
          writer =>
            var haveRead = true
            val key = reader.getKeyClass.newInstance().asInstanceOf[Writable]
            val value = reader.getValueClass.newInstance().asInstanceOf[Writable]
            while (written < n && haveRead) {
              haveRead = reader.next(key, value)
              writer.append(key, value)
              written = written + 1
            }
        }
    }

    println("Successfully written " + written + " records")
  }
}
