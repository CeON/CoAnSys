package pl.edu.icm.coansys.citations.reducers

import org.apache.hadoop.io.Writable
import org.apache.hadoop.mapreduce.Reducer

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class Xor extends Reducer[Writable, Writable, Writable, Writable] {
  type Context = Reducer[Writable, Writable, Writable, Writable]#Context
  override def reduce(key: Writable, values: java.lang.Iterable[Writable], context: Context) {
    val it = values.iterator()
    val first = it.next()
    if (!it.hasNext) {
      context.write(key, first)
    }
  }
}