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

package pl.edu.icm.coansys.citations.mappers

import org.apache.hadoop.io.{Text, BytesWritable, Writable}
import org.apache.hadoop.mapreduce.Mapper
import pl.edu.icm.coansys.citations.data.{MarkedBytesWritable, MarkedText, MatchableEntity}
import pl.edu.icm.coansys.citations.util.misc
import pl.edu.icm.coansys.citations.util.misc._

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class AuthorIndexer extends Mapper[Writable, BytesWritable, MarkedText, MarkedBytesWritable] {
  type Context = Mapper[Writable, BytesWritable, MarkedText, MarkedBytesWritable]#Context
  val outKey = new MarkedText()
  val outValue = new MarkedBytesWritable()

  override def map(key: Writable, value: BytesWritable, context: Context) {
    val entity = MatchableEntity.fromBytes(value.copyBytes())
    val authorTokens = misc.lettersNormaliseTokenise(entity.author).filterNot(stopWords).distinct
    outValue.bytes.set(value)
    authorTokens.foreach{ token =>
      outKey.text.set(token + entity.year)
      context.write(outKey, outValue)
    }
  }
}
