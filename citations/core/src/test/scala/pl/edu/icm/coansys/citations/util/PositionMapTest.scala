/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.util

import org.testng.Assert._
import org.testng.annotations.Test
import collection.immutable.SortedMap

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class PositionMapTest {
  val epsilon = 0.000001

  @Test(groups = Array("fast"))
  def simpleTest() {
    val map = new PositionMap(SortedMap(1 -> 0.5, 3 -> 1.0))
    assertEquals(map.position(1), 0.5, epsilon)
    assertEquals(map.position(3), 1, epsilon)
    assertEquals(map.position(2), 0.75, epsilon)
  }

  @Test(groups = Array("fast"))
  def biggerIntervalTest() {
    val map = new PositionMap(SortedMap(5 -> 0.5, 10 -> 1.0))
    assertEquals(map.position(5), 0.5, epsilon)
    assertEquals(map.position(6), 0.6, epsilon)
    assertEquals(map.position(7), 0.7, epsilon)
    assertEquals(map.position(8), 0.8, epsilon)
    assertEquals(map.position(9), 0.9, epsilon)
    assertEquals(map.position(10), 1.0, epsilon)
  }

  @Test(groups = Array("fast"))
  def moreIntervalsTest() {
    val map = new PositionMap(SortedMap(3 -> 0.3, 5 -> 0.5, 10 -> 1.0))
    assertEquals(map.position(3), 0.3, epsilon)
    assertEquals(map.position(4), 0.4, epsilon)
    assertEquals(map.position(5), 0.5, epsilon)
    assertEquals(map.position(6), 0.6, epsilon)
    assertEquals(map.position(7), 0.7, epsilon)
    assertEquals(map.position(8), 0.8, epsilon)
    assertEquals(map.position(9), 0.9, epsilon)
    assertEquals(map.position(10), 1.0, epsilon)
  }
}
