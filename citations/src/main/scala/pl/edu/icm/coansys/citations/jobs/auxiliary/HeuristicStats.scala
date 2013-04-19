package pl.edu.icm.coansys.citations.jobs.auxiliary

import com.nicta.scoobi.application.ScoobiApp
import com.nicta.scoobi.Scoobi
import com.nicta.scoobi.Persist._
import com.nicta.scoobi.InputsOutputs._

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object HeuristicStats extends ScoobiApp {
  override def upload = false

  def run() {
    val inUri = args(0)
    val outUri = args(1)

    val results = Scoobi.convertFromSequenceFile[String, String](inUri)
      .map { case (k, v) =>
        (k, v.split("\n", 2)(0).split(" ").map(_.substring(4)))
      }
      .map {case (k, v) =>
        (1, if (v contains k) 1 else 0, v.length, v.length.toLong)}
      .reduce{ case ((count1, contained1, maxLen1, sumLen1),(count2, contained2, maxLen2, sumLen2)) =>
        (count1 + count2, contained1 + contained2, maxLen1 max maxLen2, sumLen1 + sumLen2)
      }

    val persisted = persist(results)
    println(persisted)
  }
}
