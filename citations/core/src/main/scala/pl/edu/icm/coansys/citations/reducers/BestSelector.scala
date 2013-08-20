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
