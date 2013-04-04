/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.jobs

import com.nicta.scoobi.application.ScoobiApp
import com.nicta.scoobi.InputsOutputs.convertValueFromSequenceFile
import pl.edu.icm.coansys.citations.data.MatchableEntity
import pl.edu.icm.coansys.citations.indices.{SimpleIndex, ApproximateIndex}

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object IndexBuilder extends ScoobiApp {
  override def upload = false

  def run() {
    val usage = "Usage: IndexBuilder [-key|-author] <input_seqfile> <output_index_path>"
    if (args.length != 3) {
      println(usage)
    } else {
      args(0) match {
        case "-key" =>
          SimpleIndex.buildKeyIndex(convertValueFromSequenceFile[MatchableEntity](args(1)), args(2))
        case "-author" =>
          ApproximateIndex.buildAuthorIndex(convertValueFromSequenceFile[MatchableEntity](args(1)), args(2))
        case _ =>
          println(usage)
      }
    }
  }
}
