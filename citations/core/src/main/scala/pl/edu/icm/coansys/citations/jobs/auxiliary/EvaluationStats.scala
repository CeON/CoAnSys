package pl.edu.icm.coansys.citations.jobs.auxiliary

import com.nicta.scoobi.Scoobi._

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object EvaluationStats extends ScoobiApp {
  def run() {
    val inUri = args(0)

    val stats = fromTextFile(inUri).map(s => (s.toInt, 1)).reduce(Reduction{ case((c1,a1),(c2,a2)) => (c1+c2,a1+a2) })
    val persisted = persist(stats)
    println(persisted)
  }
}
