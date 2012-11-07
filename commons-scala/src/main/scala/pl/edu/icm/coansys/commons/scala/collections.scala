package pl.edu.icm.coansys.commons.scala

import annotation.tailrec

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object collections {
  def tabulate[T](n1: Int, n2: Int)(f: (Array[Array[T]], Int, Int) => T)(implicit arg0: ClassManifest[T]): Array[Array[T]] = {
    val opt = Array.ofDim[T](n1, n2)

    for (i <- 0 until n1) {
      for (j <- 0 until n2)
        opt(i)(j) = f(opt, i, j)
    }

    opt
  }

  def generate[T](el: T, count: Int): List[T] =
    extended(el, count, Nil)

  def extended[T](el: T, count: Int, baseList: List[T]): List[T] =
    if (count > 0)
      extended(el, count - 1, el :: baseList)
    else
      baseList

  def insert[T](elems: List[T], newEl: T): List[T] = {
    @tailrec
    def helper(elems: List[T], newEl: T, acc: List[T]): List[T] = {
      if (elems.isEmpty)
        acc.reverse
      else if (elems.tail.isEmpty)
        helper(elems.tail, newEl, elems.head :: acc)
      else
        helper(elems.tail, newEl, newEl :: elems.head :: acc)
    }
    helper(elems, newEl, Nil)
  }

  def characteristicList(elems: Traversable[Int], size: Int): List[Boolean] = {
    val diffs = (-1 +: elems.toSeq :+ size).sliding(2).map(x => x(1) - x(0))
    val falses = diffs.map(x => generate(false, x - 1)).toList

    insert(falses, List(true)).flatten
  }

  def split[T](elems: List[T])(pred: T => Boolean) = {
    val reversed = elems.foldLeft(List(List.empty[T])) {
      case (Nil, _) =>
        throw new RuntimeException
      case (h :: t, el) =>
        if (pred(el))
          Nil :: h :: t
        else
          (el :: h) :: t
    }
    reversed.filterNot(_.isEmpty).map(_.reverse).reverse
  }
}
