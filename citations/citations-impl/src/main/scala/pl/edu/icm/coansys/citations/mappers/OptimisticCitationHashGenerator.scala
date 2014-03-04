/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2014 ICM-UW
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
import pl.edu.icm.coansys.citations.data.{MarkedBytesWritable, MarkedText, MatchableEntity}
import pl.edu.icm.coansys.citations.util.misc._
import pl.edu.icm.ceon.scala_commons.collections
import scala.util.Try

/**
 * Takes citation entity as a value and generates (hash, entity id) pairs.
 *
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class OptimisticCitationHashGenerator extends Mapper[Writable, BytesWritable, MarkedText, MarkedText] {
  type Context = Mapper[Writable, BytesWritable, MarkedText, MarkedText]#Context
  val outKey = new MarkedText(marked = true)
  val outValue = new MarkedText(marked = true)

  override def setup(context: Context) {
    val marked = context.getConfiguration.getBoolean("coansys.citations.mark.citations", true)
    outKey.isMarked.set(marked)
    outValue.isMarked.set(marked)
  }

  override def map(key: Writable, value: BytesWritable, context: Context) {
    val entity = MatchableEntity.fromBytes(value.copyBytes())

    val hashes = OptimisticCitationHashGenerator.generate(entity)

    outValue.text.set(entity.id)
    hashes.foreach {hash =>
      outKey.text.set(hash)
      context.write(outKey, outValue)
    }
  }
}

object OptimisticCitationHashGenerator {
  def generate(entity: MatchableEntity) = for {
    author <- lettersNormaliseTokenise(entity.author).filterNot(stopWords).distinct.take(4)
    year <- digitsNormaliseTokenise(entity.year).filter(_.length == 4).flatMap(x => Try(x.toInt).toOption).filter(x => x < 2050 && x > 1900)
    (bpage, epage) <- collections.sortedPairs(collections.excludeOne(digitsNormaliseTokenise(entity.pages).flatMap(x => Try(x.toInt).toOption), (x:Int) => x == year))
  } yield List(author, year, bpage, epage).mkString("#")
}