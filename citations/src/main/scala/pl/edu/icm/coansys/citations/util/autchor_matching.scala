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
  def matchFactor(tokens1: List[String], tokens2: List[String]) = {
    val len1 = tokens1.length
    val len2 = tokens2.length
    val editDists = Array.tabulate(len1, len2) {
      (i, j) =>
        StringUtils.getLevenshteinDistance(tokens1(i), tokens2(j))
    }
    val roughMatchingVal = collections.tabulate[Int](len1 + 1, len2 + 1) {
      (a, i, j) =>
        if (i == 0 || j == 0)
          0
        else
        if (editDists(i - 1)(j - 1) == 0)
          a(i - 1)(j - 1) + 1
        else
          math.max(a(i - 1)(j), a(i)(j - 1))
    }

    @tailrec
    def extract(i: Int, j: Int, acc: List[(Int, Int)] = Nil): List[(Int, Int)] = {
      if (i > 0 && j > 0) {
        if (editDists(i - 1)(j - 1) == 0) {
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
    val roughMatching = List(
      List((0, 0)),
      extract(len1, len2).map {
        case (i, j) => (i + 1, j + 1)
      },
      List((len1 + 1, len2 + 1))).flatten

    val boundaries = roughMatching.map {
      case (i, j) => (i.toDouble / (len1 + 1) + j.toDouble / (len2 + 1)) / 2
    }
    val boundaries1 = roughMatching.unzip._1 zip boundaries
    val boundaries2 = roughMatching.unzip._2 zip boundaries
    def position(boundaries: List[(Int, Double)])(i: Int): Double = boundaries match {
      case _ :: (r, rv) :: tail if i > r =>
        position((r, rv) :: tail)(i)
      case (l, lv) :: (r, rv) :: _ if l <= i && i <= r =>
        lv + (i - l) * (rv - lv) / (r - l)
      case _ =>
        throw new Exception("Couldn't find appripriate location for " + i + " in " + boundaries)
    }

    def distance(i: Int, j: Int) =
      math.abs(position(boundaries1)(i + 1) - position(boundaries2)(j + 1))

    val dists = Array.tabulate(len1, len2)(distance)

    val maxLen = math.max(len1, len2)
    val distTransform: Double => Double = identity
    val editDistTransform: Int => Double = _.toDouble
    val weights = Array.tabulate(maxLen, maxLen) {
      case (i, j) =>
        if (i < len1 && j < len2)
          distTransform(dists(i)(j)) + editDistTransform(editDists(i)(j))
        else
          0
    }


  }

}
