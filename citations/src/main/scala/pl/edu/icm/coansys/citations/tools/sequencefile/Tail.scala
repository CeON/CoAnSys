/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.tools.sequencefile

import org.apache.hadoop.conf.Configuration

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object Tail {
  def main(args: Array[String]) {
    if (args.length != 1) {
      println ("Omits n first records of a sequence file and copies the rest to a new one.")
      println ()
      println ("Usage: Tail <n> <in_sequence_file> <out_sequence_file>")
      System.exit(1)
    }

    val n = args(0).toInt
    val inUri = args(1)
    val outUri = args(2)
    val written = util.transformAndWrite(new Configuration(), inUri, outUri, _.drop(n))

    println("Successfully written " + written + " records")
  }
}
