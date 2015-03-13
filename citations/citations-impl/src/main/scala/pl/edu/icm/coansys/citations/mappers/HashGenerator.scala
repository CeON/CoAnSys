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

import org.apache.hadoop.io.{BytesWritable, Writable}
import org.apache.hadoop.mapreduce.Mapper
import pl.edu.icm.coansys.citations.data.{MatchableEntity, MarkedBytesWritable, MarkedText}
import pl.edu.icm.coansys.citations.util.misc._
import pl.edu.icm.coansys.citations.hashers.CitationNameYearPagesOptimisticHashGenerator

/**
 * Takes citation entity as a value and generates (hash, entity id) pairs.
 *
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
abstract class HashGenerator extends Mapper[Writable, BytesWritable, MarkedText, MarkedText] {
  type Context = Mapper[Writable, BytesWritable, MarkedText, MarkedText]#Context
  private val outKey = new MarkedText(marked = true)
  private val outValue = new MarkedText(marked = true)
  private var hasher: pl.edu.icm.coansys.citations.hashers.HashGenerator = null

  protected val markProperty: String
  protected val markDefault: Boolean
  protected val hasherProperty: String

  override def setup(context: Context) {
    val marked = context.getConfiguration.getBoolean(markProperty, markDefault)
    outKey.isMarked.set(marked)
    outValue.isMarked.set(marked)

    hasher = context.getConfiguration.getClass(hasherProperty, null, classOf[pl.edu.icm.coansys.citations.hashers.HashGenerator]).newInstance()
  }

  override def map(key: Writable, value: BytesWritable, context: Context) {
    val entity = MatchableEntity.fromBytes(value.copyBytes())

    val hashes = hasher.generate(entity)

    outValue.text.set(entity.id)
    hashes.foreach {hash =>
      outKey.text.set(hash)
      context.write(outKey, outValue)
    }
  }
}

