package pl.edu.icm.coansys.commons.scala

import annotation.tailrec
import collections._

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object strings {
  /**
   * Returns all rotations of a given string
   */
  def rotations(s: String): IndexedSeq[String] = {
    for {
      b <- 0 to (s.length - 1)
      rot = s.substring(b) + s.substring(0, b)
    } yield rot
  }

  /**
   * Computes HCS (Heaviest Common Subsequence) of two strings using given weight function.
   */
  def hcs(s1: String, s2: String, weight: Char => Double) = {
    val hcsArray = tabulate[Double](s1.length + 1, s2.length + 1) {
      (hcs, i, j) =>
        if (i == 0 || j == 0) {
          0
        } else {
          val eqBonus = if (s1.charAt(i - 1) == s2.charAt(j - 1)) weight(s1.charAt(i - 1)) else 0
          List(hcs(i - 1)(j - 1) + eqBonus, hcs(i - 1)(j), hcs(i)(j - 1)).max
        }
    }

    @tailrec
    def extractHcs(i: Int, j: Int, acc: List[(Int, Int)] = Nil): List[(Int, Int)] = {
      if (i > 0 && j > 0) {
        if (hcsArray(i - 1)(j - 1) >= math.max(hcsArray(i - 1)(j), hcsArray(i)(j - 1))) {
          if (s1.charAt(i - 1) == s2.charAt(j - 1))
            extractHcs(i - 1, j - 1, (i - 1, j - 1) :: acc)
          else
            extractHcs(i - 1, j - 1, acc)
        } else {
          if (hcsArray(i - 1)(j) >= hcsArray(i)(j - 1))
            extractHcs(i - 1, j, acc)
          else
            extractHcs(i, j - 1, acc)
        }
      }
      else {
        acc
      }
    }

    extractHcs(s1.length, s2.length)
  }

  /**
   * Computes LCS of two strings.
   */
  def lcs(s1: String, s2: String) =
    hcs(s1, s2, (_ => 1))

  /**
   * Returns a subsequence of given string consisting of characters at specified positions.
   */
  def subsequence(s: String, indices: TraversableOnce[Int]) = {
    indices map (s.charAt) mkString ("")
  }
}
