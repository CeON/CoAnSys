/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.tools.sequencefile

import pl.edu.icm.coansys.commons.scala.automatic_resource_management._
import pl.edu.icm.coansys.citations.util.SequenceFileIterator
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.io.Writable
import com.nicta.scoobi.io.sequence.SeqSchema

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object Wc {
  def main(args: Array[String]) {
    val inUri = args(0)
    implicit val _ = new SeqSchema[Writable] {
      type SeqType = Writable

      def toWritable(x: Writable) = x

      def fromWritable(x: this.type#SeqType) = x

      val mf = manifest[Writable]
    }
    using(SequenceFileIterator.fromUri[Writable, Writable](new Configuration(), inUri)) {
      iterator =>
        println(iterator.size + " lines")
    }
  }
}
