/*
 * (C) 2010-2012 ICM UW. All rights reserved.
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