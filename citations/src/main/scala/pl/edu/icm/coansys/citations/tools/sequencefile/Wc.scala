/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.tools.sequencefile

import pl.edu.icm.coansys.commons.scala.automatic_resource_management._
import org.apache.hadoop.conf.Configuration
import pl.edu.icm.coansys.citations.util.sequencefile.SequenceFileIterator

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object Wc {
  def main(args: Array[String]) {
    if (args.length != 1) {
      println("Counts records in a given sequence file.")
      println()
      println("Usage: Wc <sequence_file>")
      System.exit(1)
    }

    val inUri = args(0)
    val count = using(SequenceFileIterator.fromUri(new Configuration(), inUri))(_.size)
    println(count + " records")
  }
}
