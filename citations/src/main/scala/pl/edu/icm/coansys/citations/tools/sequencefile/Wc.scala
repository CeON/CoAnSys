/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.tools.sequencefile

import pl.edu.icm.coansys.commons.scala.automatic_resource_management._
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.io.{SequenceFile, Writable}
import org.apache.hadoop.fs.Path

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object Wc {
  def main(args: Array[String]) {
    val inUri = args(0)
    var count = 0

    using(new SequenceFile.Reader(new Configuration(), SequenceFile.Reader.file(new Path(inUri)))) {
      reader =>
        val key = reader.getKeyClass.newInstance().asInstanceOf[Writable]
        val value = reader.getValueClass.newInstance().asInstanceOf[Writable]
        while (reader.next(key, value)) {
          count = count + 1
        }

    }
    println(count + " lines")
  }
}
