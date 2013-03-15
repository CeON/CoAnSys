/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.tools.sequencefile

import pl.edu.icm.coansys.commons.scala.automatic_resource_management._
import pl.edu.icm.coansys.citations.data.Entity
import pl.edu.icm.coansys.citations.util.{EncapsulatedSequenceFileWriter, SequenceFileIterator}
import org.apache.hadoop.conf.Configuration
import com.nicta.scoobi.io.sequence.SeqSchema

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object head {
  def main(args: Array[String]) {
    val n = args(0).toInt
    val inUri = args(1)
    val outUri = args(2)
    var written = 0
    using(SequenceFileIterator.fromUri[String, Entity](new Configuration(), inUri)) {
      records =>
        using(EncapsulatedSequenceFileWriter.fromLocal[String, Entity](outUri)) {
          write =>
            records.take(n).foreach {
              x =>
                write(x)
                written = written + 1
            }
        }
    }

    println("Successfully written " + written + " records")
  }
}
