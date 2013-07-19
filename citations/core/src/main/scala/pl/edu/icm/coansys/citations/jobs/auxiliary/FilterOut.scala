package pl.edu.icm.coansys.citations.jobs.auxiliary

import com.nicta.scoobi.Scoobi._

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object FilterOut extends ScoobiApp {
  def run() {
    val inFile = args(0)
    val filterOutFile = args(1)
    val outUri = args(2)
    val in = fromSequenceFile[String, String](inFile).map {case (k, v) => (k, Option(v))}
    val filterOut = keyFromSequenceFile[String](filterOutFile).map {case k => (k, Option.empty[String])}
    val result = (in ++ filterOut).groupByKey[String, Option[String]].flatMap {case (k, vsIter) =>
      val vs = vsIter.toList
      if(vs.contains(None))
        Stream.continually(k) zip vs.flatten
      else
        Stream()
    }
    persist(toSequenceFile(result, outUri, overwrite = true))
  }
}
