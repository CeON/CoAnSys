/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.util

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object ngrams {
  def ngramCounts(s: String, n: Int): Map[String, Int] =
    s.sliding(n).toIterable.groupBy(identity).mapValues(_.size)

  def trigramSimilarity(s1: String, s2: String): Double = {
    val n = 3
    val c1 = ngramCounts(s1, n)
    val c2 = ngramCounts(s2, n)
    val all = c1.values.sum + c2.values.sum
    val common = (c1.keySet & c2.keySet).map(k => c1.getOrElse(k, 0) min c2.getOrElse(k, 0)).sum

    2 * common.toDouble / all
  }
}
