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

package pl.edu.icm.coansys.commons.scala.collections

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DisjointSetTest {
  @Test
  def basicTest() {
    val set1 = new DisjointSet(1)
    val set2 = new DisjointSet(2)
    val set3 = new DisjointSet(3)
    val set4 = new DisjointSet(4)

    assertEquals(Set(1), set1.elements.toSet)
    assertEquals(Set(2), set2.elements.toSet)
    assertEquals(Set(3), set3.elements.toSet)
    assertEquals(Set(4), set4.elements.toSet)

    set1.union(set2)
    set4.union(set3)

    assertTrue(set1.find() == set2.find())
    assertTrue(set3.find() == set4.find())

    assertEquals(Set(1, 2), set1.elements.toSet)
    assertEquals(Set(1, 2), set2.elements.toSet)
    assertEquals(Set(3, 4), set3.elements.toSet)
    assertEquals(Set(3, 4), set4.elements.toSet)

    set1.union(set4)

    assertEquals(Set(1, 2, 3, 4), set1.elements.toSet)
    assertEquals(Set(1, 2, 3, 4), set2.elements.toSet)
    assertEquals(Set(1, 2, 3, 4), set3.elements.toSet)
    assertEquals(Set(1, 2, 3, 4), set4.elements.toSet)
  }
}