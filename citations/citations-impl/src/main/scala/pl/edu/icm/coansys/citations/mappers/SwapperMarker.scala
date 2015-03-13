/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2015 ICM-UW
 *
 * CoAnSys is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CoAnSys is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with CoAnSys. If not, see <http://www.gnu.org/licenses/>.
 */

package pl.edu.icm.coansys.citations.mappers

import org.apache.hadoop.mapreduce.Mapper
import org.apache.hadoop.io.{BytesWritable, Text}
import pl.edu.icm.coansys.citations.data.{TextWithBytesWritable, MarkedText}

/**
 * Created by matfed on 28.02.14.
 */
class SwapperMarker extends Mapper[Text, Text, MarkedText, TextWithBytesWritable] {
  type Context = Mapper[Text, Text, MarkedText, TextWithBytesWritable]#Context
  val outKey = new MarkedText(false)
  val outValue = new TextWithBytesWritable()

  override def setup(context: Context) {
    val marked = context.getConfiguration.getBoolean("coansys.citations.mark.ids", false)
    outKey.isMarked.set(marked)
  }

  override def map(key: Text, value: Text, context: Context) {
    outKey.text.set(value)
    outValue.text.set(key)
    context.write(outKey, outValue)
  }
}