/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2015 ICM-UW
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

package pl.edu.icm.coansys.citations.reducers

import org.apache.hadoop.io.{WritableComparable, Writable}
import org.apache.hadoop.mapreduce.Reducer

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class Xor[K, V] extends Reducer[K, V, K, V] {
  type Context = Reducer[K, V, K, V]#Context
  override def reduce(key: K, values: java.lang.Iterable[V], context: Context) {
    val it = values.iterator()
    val first = it.next()
    if (!it.hasNext) {
      context.write(key, first)
    }
  }
}