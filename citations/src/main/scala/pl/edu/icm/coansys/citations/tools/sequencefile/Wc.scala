/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.tools.sequencefile

import pl.edu.icm.coansys.commons.scala.automatic_resource_management._
import org.apache.hadoop.conf.Configuration
import pl.edu.icm.coansys.citations.util.SequenceFileIterator

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object Wc {
  def main(args: Array[String]) {
    val inUri = args(0)
    val count = using(SequenceFileIterator.fromUri(new Configuration(), inUri))(_.size)
    println(count + " lines")
  }
}
