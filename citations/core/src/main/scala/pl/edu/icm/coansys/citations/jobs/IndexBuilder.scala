/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.jobs

import com.nicta.scoobi.Scoobi._
import pl.edu.icm.coansys.citations.data.MatchableEntity
import pl.edu.icm.coansys.citations.indices.{SimpleIndex, ApproximateIndex}
import pl.edu.icm.coansys.citations.util.MyScoobiApp

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object IndexBuilder extends MyScoobiApp {

  def run() {
    val usage = "Usage: IndexBuilder [-key|-author] <input_seqfile> <output_index_path>"
    if (args.length != 3) {
      println(usage)
    } else {
      args(0) match {
        case "-key" =>
          SimpleIndex.buildKeyIndex(valueFromSequenceFile[MatchableEntity](args(1)), args(2))
        case "-author" =>
          ApproximateIndex.buildAuthorIndex(valueFromSequenceFile[MatchableEntity](args(1)), args(2))
        case _ =>
          println(usage)
      }
    }
  }
}
