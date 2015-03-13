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

import org.apache.hadoop.io.{Text, BytesWritable}
import org.apache.hadoop.mapreduce.Mapper
import pl.edu.icm.coansys.citations.data.{TextWithBytesWritable, MarkedText}

/**
 * Created by matfed on 28.02.14.
 */
class EntityMarker extends Mapper[Text, BytesWritable, MarkedText, TextWithBytesWritable] {
  type Context = Mapper[Text, BytesWritable, MarkedText, TextWithBytesWritable]#Context
  val outKey = new MarkedText(true)
  val outValue = new TextWithBytesWritable()

  override def setup(context: Context) {
    val marked = context.getConfiguration.getBoolean("coansys.citations.mark.entities", true)
    outKey.isMarked.set(marked)
  }

  override def map(key: Text, value: BytesWritable, context: Context) {
   outKey.text.set(key)
   outValue.bytes.set(value)
   context.write(outKey, outValue)
  }
}