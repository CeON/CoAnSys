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
      private var res = null.asInstanceOf[T]

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
      private var res = null.asInstanceOf[T]

      def setup() {
        res = resource
      }

      def process(input: A, emitter: Emitter[C]) {
        block(res, input).foreach(emitter.emit)
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
