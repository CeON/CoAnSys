/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.util

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object ngrams {

  case class NgramStatistics(counter: Map[String, Int], overall: Int) {
    def similarityTo(other: NgramStatistics): Double = {
      val c1 = counter
      val c2 = other.counter
      val all = overall + other.overall
      val common = (c1.keySet & c2.keySet).toIterator.map(k => c1(k) min c2(k)).sum

      if (all > 0)
        2 * common.toDouble / all
      else
        0.0
    }
  }

  object NgramStatistics {
    def fromString(s: String, n: Int): NgramStatistics = {
      val counter = s.sliding(n).toIterable.groupBy(identity).mapValues(_.size)
      val overall = counter.values.sum
      NgramStatistics(counter, overall)
    }
  }

  def trigramSimilarity(s1: String, s2: String): Double = {
    val n = 3
    NgramStatistics.fromString(s1, n) similarityTo NgramStatistics.fromString(s2, n)
  }
}
