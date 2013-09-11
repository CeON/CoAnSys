/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2013 ICM-UW
 * 
 * CoAnSys is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * CoAnSys is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with CoAnSys. If not, see <http://www.gnu.org/licenses/>.
 */

package pl.edu.icm.coansys.citations.tools.matcher

import scala.io.Source
import pl.edu.icm.ceon.scala_commons.collections.DisjointSet


object Evaluator {
  def clustersFromLinks(maxCluster: Int, doLinking: Array[DisjointSet[Int]] => Unit) = {
    val sets = new Array[DisjointSet[Int]](maxCluster + 1)
    for (i <- 0 to maxCluster)
      sets(i) = new DisjointSet(i)

    doLinking(sets)

    sets.foldLeft(Set.empty[Set[Int]]) {
      (clusters, el) => if (el.find() == el) clusters + el.elements.toSet else clusters
    }
  }

  def clustersFromLinksSingle(maxCluster: Int, links: TraversableOnce[(Int, Int)]) = {
    def doSingleLink(sets: Array[DisjointSet[Int]]) {
      for ((i, j) <- links)
        sets(i).union(sets(j))
    }
    clustersFromLinks(maxCluster, doSingleLink)
  }

  def clustersFromLinksAdv(maxCluster: Int, probabilityMap: Map[(Int, Int), Double]) = {
    def doAdvLink(sets: Array[DisjointSet[Int]]) {
      val sorted = probabilityMap.toList.sortBy(-_._2)
      for (((i, j), prob) <- sorted; if prob >= 0.5) {
        val elems1 = sets(i).elements
        val elems2 = sets(j).elements
        val pairs = for (el1 <- elems1.view; el2 <- elems2.view; if el1 != el2) yield (math.min(el1, el2), math.max(el1, el2))
        if (pairs.find(probabilityMap(_) < 0.5).isEmpty)
          sets(i).union(sets(j))
      }
    }
    clustersFromLinks(maxCluster, doAdvLink)
  }

  def computeClusterRecall(expected: Set[Set[Int]], actual: Set[Set[Int]]) = {
    (expected filter actual).size.toDouble / expected.size
  }

  def computePairwisePrecisionRecall(expected: Seq[Boolean], actual: Seq[Boolean]) = {
    val pairs = (expected zip actual)
    val intersec = (pairs filter (t => t._1 == t._2 && t._1)).length
    val real = (pairs filter (_._1)).length
    val marked = (pairs filter (_._2)).length
    (intersec.toDouble / marked, intersec.toDouble / real)
  }

  def computePairwiseAccuracy(expected: Seq[Boolean], actual: Seq[Boolean]) = {
    val pairs = (expected zip actual)
    val intersec = pairs filter (t => t._1 == t._2)
    intersec.length.toDouble / expected.length
  }

  def labelToBoolean(s: String) =
    s.toInt > 0

  def probabilityStringToBoolean(s: String) = {
    val parts = s.split( """\s+""")
    parts(1).toDouble >= 0.5
  }

  def probabilityStringToDouble(s: String) = {
    val parts = s.split( """\s+""")
    parts(2).toDouble
  }

  def filterOnMask[T](sequence: Seq[T], mask: Seq[Boolean]) =
    (sequence zip mask) filter (_._2) map (_._1)

  def inspectClusters(clusters: Set[Set[Int]], probabilities: Map[(Int, Int), Double]) {
    for (cluster <- clusters) {
      println(cluster)
      println("weak links (<0.5):")
      for (elem1 <- cluster; elem2 <- cluster; if elem1 < elem2 && probabilities((elem1, elem2)) < 0.5)
        println("(%d, %d) -- %f" format(elem1, elem2, probabilities((elem1, elem2))))
      println()
    }
  }

  def inspectCluster(cluster: Set[Int], probabilities: Map[(Int, Int), Double]) {
    for (elem1 <- cluster; elem2 <- cluster; if elem1 < elem2 && probabilities((elem1, elem2)) >= 0.5)
      println("(%d, %d) -- %f" format(elem1, elem2, probabilities((elem1, elem2))))
    println()
  }

  def inspectClusterRecall(expected: Set[Set[Int]], actual: Set[Set[Int]], probabilities: Map[(Int, Int), Double]) {
    val elementMap = actual.foldLeft(Map.empty[Int, Set[Int]]) {
      (map, cluster) => cluster.foldLeft(map) {
        (map, elem) => map + (elem -> cluster)
      }
    }
    (expected filterNot actual).foreach(cluster => {
      println("Didn't found " + cluster)
      println("Its elements are in the following clusters:")
      cluster.map(elementMap).foreach(c => {
        println(c);
        inspectCluster(c, probabilities)
      })
      println()
    })
  }

  def main(args: Array[String]) {
    //    val citeMapPath = """C:\temp\cora2-lcs\sample-cora2-idmap.txt"""
    val mappingPath = """C:\Users\matfed\Desktop\matcher-test\fold2\matcherTestingSvmWOAuthor.txt.map"""
    val correctPath = """C:\Users\matfed\Desktop\matcher-test\fold2\matcherTestingSvmWOAuthor.txt"""
    val actualPath = """C:\Users\matfed\Desktop\matcher-test\fold2\outWOAuthor"""
    //    val citeMapping = Source.fromFile(citeMapPath).getLines().toSeq.map{s => val parts = s.split(" ", 2); (parts(0).toInt -> parts(1))}.toMap
    val mapping = Source.fromFile(mappingPath).getLines().toSeq map {
      s => val parts = s.split( """\s+"""); (parts(0).toInt, parts(1).toInt)
    }
    val maxCluster = mapping flatMap (t => List(t._1, t._2)) reduce math.max
    val (correctText, featureVectors) = Source.fromFile(correctPath).getLines().toSeq.map {
      s => val parts = s.split( """\s+""", 2); (parts(0), parts(1))
    }.unzip
    val correct = correctText map labelToBoolean
    //    val probabilities = Source.fromFile(actualPath).getLines().toSeq.drop(1) map probabilityStringToDouble
    //    val actual = probabilities map (_ >= 0.5)
    val actual = Source.fromFile(actualPath).getLines().toSeq map labelToBoolean
    //    val linksProb = ((mapping map { case (x, y) => (math.min(x, y), math.max(x, y)) }) zip probabilities).toMap
    val correctLinks = filterOnMask(mapping, correct)
    val actualLinks = filterOnMask(mapping, actual)
    val wrongLinks = filterOnMask(mapping zip featureVectors, correct zip actual map {
      case (c, a) => !c && a
    })
    //    println("Wrong links:")
    //    for (((i, j), fv) <- wrongLinks) {
    //      val prob = linksProb(math.min(i,j), math.max(i,j))
    //      println(((i,j),prob, fv))
    //      println(citeMapping(i))
    //      println(citeMapping(j))
    //    }
    val correctClusters = clustersFromLinksSingle(maxCluster, correctLinks)
    val actualClusters = clustersFromLinksSingle(maxCluster, actualLinks)
    //    val actualClusters = clustersFromLinksAdv(maxCluster, linksProb)
    //inspectClusterRecall(correctClusters, actualClusters, linksProb)
    //inspectClusters(actualClusters, linksProb)
    println("cluster recall: %f" format computeClusterRecall(correctClusters, actualClusters))
    val (precision, recall) = computePairwisePrecisionRecall(correct, actual)
    println("precision: %f" format precision)
    println("recall: %f" format recall)
    println("accuracy: %f" format computePairwiseAccuracy(correct, actual))
    //println("all clusters: %d" format correctClusters.size)
  }
}