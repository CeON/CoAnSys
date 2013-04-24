package pl.edu.icm.coansys.citations.jobs.auxiliary

import com.nicta.scoobi.application.ScoobiApp
import com.nicta.scoobi.Scoobi
import com.nicta.scoobi.Persist._
import com.nicta.scoobi.InputsOutputs._
import org.apache.commons.lang.StringUtils

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object HeuristicStats extends ScoobiApp {
  override def upload = false

  def run() {
    val inUri = args(0)

    val results = Scoobi.convertFromSequenceFile[String, String](inUri)
      .map { case (k, v) =>
        (k, v.split("\n", 2)(0).split(" ").filterNot(StringUtils.isEmpty).map(_.substring(4)))
      }
      .map {case (k, v) =>
        val distrib =
          if (v.length > 5000)
            (0,0,0,0,0,0,1)
          else if (v.length > 1000)
            (0,0,0,0,0,1,0)
          else if (v.length > 500)
            (0,0,0,0,1,0,0)
          else if (v.length > 100)
            (0,0,0,1,0,0,0)
          else if (v.length > 50)
            (0,0,1,0,0,0,0)
          else if (v.length > 10)
            (0,1,0,0,0,0,0)
          else
            (1,0,0,0,0,0,0)
        (1, if (v contains k) 1 else 0, v.length, v.length.toLong, distrib)}
      .reduce{ case ((count1, contained1, maxLen1, sumLen1, dist1),(count2, contained2, maxLen2, sumLen2, dist2)) =>
        (count1 + count2, contained1 + contained2, maxLen1 max maxLen2, sumLen1 + sumLen2,
          (dist1._1 + dist2._1, dist1._2 + dist2._2, dist1._3 + dist2._3, dist1._4 + dist2._4, dist1._5 + dist2._5, dist1._6 + dist2._6, dist1._7 + dist2._7))
      }

    val persisted = persist(results)
    println(persisted)
  }
}
