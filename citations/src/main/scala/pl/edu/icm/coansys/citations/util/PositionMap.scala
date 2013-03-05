/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.util

import collection.SortedMap

/**
 * There are some indices with specified position. Others position is defined in respect to adjacent defined indices.
 *
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class PositionMap(map: SortedMap[Int, Double]) {
  def position(i: Int) = {
    val (right, rv) = map.from(i).head
    val (left, lv) = map.to(i).last
    if (left == right)
      lv
    else
      lv + (i - left) * (rv - lv) / (right - left)
  }
}

object PositionMap {
  implicit def toPositionMap(map: SortedMap[Int, Double]) = new PositionMap(map)
}
