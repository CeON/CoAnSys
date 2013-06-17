/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.reducers

import collection.JavaConversions._
import org.apache.hadoop.mapreduce.Reducer
import org.apache.hadoop.io.Text

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class BestSelector extends Reducer[Text, Text, Text, Text] {
  type Context = Reducer[Text, Text, Text, Text]#Context

  override def reduce(key: Text, values: java.lang.Iterable[Text], context: Context) {
    if (values.isEmpty)
      return

    val best = values.maxBy(_.toString.split(":", 2)(0).toDouble)
    context.write(key, best)
  }

}
