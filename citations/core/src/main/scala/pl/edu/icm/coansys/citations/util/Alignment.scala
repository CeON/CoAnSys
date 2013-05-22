/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.util

import collection.immutable.SortedMap

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class Alignment(private val matches1: List[Int],
                private val matches2: List[Int],
                private val boundaries: List[Double]) {
  private val positionMap1 = new PositionMap(SortedMap((matches1 zip boundaries): _*))
  private val positionMap2 = new PositionMap(SortedMap((matches2 zip boundaries): _*))

  def distance(i: Int, j: Int) =
    math.abs(positionMap1.position(i + 1) - positionMap2.position(j + 1))
}
