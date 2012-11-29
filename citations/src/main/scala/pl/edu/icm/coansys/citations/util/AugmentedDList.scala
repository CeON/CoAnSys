package pl.edu.icm.coansys.citations.util

import com.nicta.scoobi.core.{WireFormat, Emitter, DoFn, DList}

/**
 * A class extending DList[A] capabilities
 *
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class AugmentedDList[A](dlist: DList[A]) {

  /**
   * Similar to map, but uses a common resource for all executions. Just to speed things up. For instance, you may
   * have a helper object to access an index and you don't want to recreate it for each map function execution.
   */
  def mapWithResource[T <: {def close()}, B: Manifest : WireFormat](resource: => T)(block: (T, A) => B): DList[B] = {
    dlist.parallelDo(new DoFn[A, B] {
      var res = null.asInstanceOf[T]

      def setup() {
        res = resource
      }

      def process(input: A, emitter: Emitter[B]) {
        emitter.emit(block(res, input))
      }

      def cleanup(emitter: Emitter[B]) {
        res.close()
      }
    })
  }

  /**
   * Just like mapWithResource, but flattens results.
   */
  def flatMapWithResource[T <: {def close()}, C: Manifest : WireFormat](resource: => T)(block: (T, A) => Iterable[C]): DList[C] = {
    dlist.parallelDo(new DoFn[A, C] {
      var res = null.asInstanceOf[T]

      def setup() {
        res = resource
      }

      def process(input: A, emitter: Emitter[C]) {
        block(res, input).foreach(emitter.emit(_))
      }

      def cleanup(emitter: Emitter[C]) {
        res.close()
      }
    })
  }
}

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object AugmentedDList {
  implicit def augmentDList[T](dlist: DList[T]) = new AugmentedDList[T](dlist)
}
