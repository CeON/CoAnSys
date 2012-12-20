/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.util

import org.apache.commons.lang.StringUtils
import pl.edu.icm.coansys.commons.scala.collections
import annotation.tailrec

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object author_matching {
  def roughMatching(exactMatches: Set[(Int, Int)]) = {
    val len1 = exactMatches.toIterable.unzip._1.max
    val len2 = exactMatches.toIterable.unzip._2.max
    val roughMatchingVal = collections.tabulate[Int](len1 + 1, len2 + 1) {
      (a, i, j) =>
        if (i == 0 || j == 0)
          0
        else
        if (exactMatches((i - 1, j - 1)))
          a(i - 1)(j - 1) + 1
        else
          math.max(a(i - 1)(j), a(i)(j - 1))
    }

    @tailrec
    def extract(i: Int, j: Int, acc: List[(Int, Int)] = Nil): List[(Int, Int)] = {
      if (i > 0 && j > 0) {
        if (exactMatches((i - 1, j - 1))) {
          extract(i - 1, j - 1, (i - 1, j - 1) :: acc)
        } else {
          if (roughMatchingVal(i - 1)(j) >= roughMatchingVal(i)(j - 1))
            extract(i - 1, j, acc)
          else
            extract(i, j - 1, acc)
        }
      }
      else {
        acc
      }
    }

    extract(len1 + 1, len2 + 1)
  }

  def matchingTokens(tokens1: List[String], tokens2: List[String]) = for {
    (w, i) <- tokens1.zipWithIndex
    (v, j) <- tokens2.zipWithIndex
    if w == v
  } yield (i, j)


  def matchFactor(tokens1: List[String], tokens2: List[String]) = {
    val len1 = tokens1.length
    val len2 = tokens2.length
    val extendedMatchingTokens =
      matchingTokens(tokens1, tokens2).map {
        case (x, y) => (x + 1, y + 1)
      }.toSet + ((0, 0)) + ((len1 + 1, len2 + 1))
    val rough = roughMatching(extendedMatchingTokens)

    val boundaries = rough.map {
      case (i, j) => (i.toDouble / (len1 + 1) + j.toDouble / (len2 + 1)) / 2
    }
    val alignment = new Alignment(rough.unzip._1, rough.unzip._2, boundaries)

    val dists = Array.tabulate(len1, len2)(alignment.distance)

    val editDists = Array.tabulate(len1, len2) {
      (i, j) =>
        if (tokens1(i) == tokens2(j))
          0
        else if (((tokens1(i) startsWith tokens2(j)) && (tokens2(j).length <= 2)) ||
          ((tokens2(j) startsWith tokens1(i)) && (tokens1(i).length <= 2)))
          1
        else
          StringUtils.getLevenshteinDistance(tokens1(i), tokens2(j))
    }

    val maxLen = math.max(len1, len2)
    val distTransform: Double => Double = SigmoidFunction.gen(0.01, 8.0 / ((len1 + len2) / 2.0))
    val editDistTransform: Int => Double = ((i: Int) => i.toDouble) andThen SigmoidFunction.gen(0.01, 4)
    val weights = Array.tabulate(maxLen, maxLen) {
      case (i, j) if i < len1 && j < len2 =>
        distTransform(dists(i)(j)) * editDistTransform(editDists(i)(j))
      case _ => 0
    }


  }

}
