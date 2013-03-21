/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.tools.sequencefile

import org.apache.hadoop.conf.Configuration

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object Head {
  def main(args: Array[String]) {
    val n = args(0).toInt
    val inUri = args(1)
    val outUri = args(2)
    val written = util.transformAndWrite(new Configuration(), inUri, outUri, _.take(n))

    println("Successfully written " + written + " records")
  }
}
