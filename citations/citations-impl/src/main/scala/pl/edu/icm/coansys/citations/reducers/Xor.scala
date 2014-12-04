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