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

class DisjointSet[T](val value: T) {
  var parent: DisjointSet[T] = this
  var rank: Int = 0
  var next: List[DisjointSet[T]] = Nil

  def find(): DisjointSet[T] = {
    if (parent != this) {
      parent = parent.find()
    }
    parent
  }

  def union(other: DisjointSet[T]) {
    val myRoot = find()
    val theirRoot = other.find()
    if (myRoot == theirRoot) {
      return
    }

    if (myRoot.rank < theirRoot.rank) {
      myRoot.parent = theirRoot
      theirRoot.next = myRoot :: theirRoot.next
    } else if (myRoot.rank > theirRoot.rank) {
      theirRoot.parent = myRoot
      myRoot.next = theirRoot :: myRoot.next
    } else {
      theirRoot.parent = myRoot
      myRoot.next = theirRoot :: myRoot.next
      myRoot.rank += 1
    }
  }

  def elements = {
    def traverse(s: DisjointSet[T], acc: List[T]): List[T] = {
      s.next.foldLeft(s.value :: acc)((acc, el) => traverse(el, acc))
    }
    traverse(this.find(), Nil)
  }
}