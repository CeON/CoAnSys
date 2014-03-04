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

import pl.edu.icm.coansys.citations.data.{MarkedBytesWritable, MarkedText, MatchableEntity}
import pl.edu.icm.coansys.citations.util.misc
import pl.edu.icm.coansys.citations.util.misc._
import scala.util.Try
import pl.edu.icm.ceon.scala_commons.{strings, collections}
import org.apache.hadoop.mapreduce.Mapper
import org.apache.hadoop.io.{BytesWritable, Writable}

/**
 * Created by matfed on 27.02.14.
 */
class DocumentHashGenerator extends Mapper[Writable, BytesWritable, MarkedText, MarkedText] {
  type Context = Mapper[Writable, BytesWritable, MarkedText, MarkedText]#Context
  val outKey = new MarkedText()
  val outValue = new MarkedText()

  override def setup(context: Context) {
    val marked = context.getConfiguration.getBoolean("coansys.citations.mark.documents", false)
    outKey.isMarked.set(marked)
    outValue.isMarked.set(marked)
  }

  override def map(key: Writable, value: BytesWritable, context: Context) {
    val entity = MatchableEntity.fromBytes(value.copyBytes())

    val hashes = DocumentHashGenerator.generate(entity)

    outValue.text.set(entity.id)
    hashes.foreach{ hash =>
      outKey.text.set(hash)
      context.write(outKey, outValue)
    }
  }
}

object DocumentHashGenerator {
  def blur(n: Int) = List(n-1,n,n+1)

  def generate(entity: MatchableEntity) = for {
    author <- misc.lettersNormaliseTokenise(entity.author).filterNot(stopWords).distinct.take(4)
    year <- digitsNormaliseTokenise(entity.year).filter(_.length == 4).flatMap(x => Try(x.toInt).toOption).filter(x => x < 2050 && x > 1900)
    (bpage, epage) <- collections.sortedPairs(digitsNormaliseTokenise(entity.pages).flatMap(x => Try(x.toInt).toOption))
    bluredYear <- blur(year)
    bluredBpage <- blur(bpage)
    bluredEpage <- blur(epage)
  } yield List(author, bluredYear, bluredBpage, bluredEpage).mkString("#")
}
