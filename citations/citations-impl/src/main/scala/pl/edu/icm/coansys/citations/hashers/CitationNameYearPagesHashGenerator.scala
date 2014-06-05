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

package pl.edu.icm.coansys.citations.hashers

import pl.edu.icm.ceon.scala_commons.collections
import pl.edu.icm.coansys.citations.data.MatchableEntity
import pl.edu.icm.coansys.citations.util.misc._
import pl.edu.icm.coansys.citations.hashers.util._
import scala.util.Try

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class CitationNameYearPagesHashGenerator extends HashGenerator {
  def generate(entity: MatchableEntity): Iterable[String] = {
    val text = entity.rawText.getOrElse(genText(entity))
    val digits = digitsNormaliseTokenise(text)

    if (digits.length > 15) 
      Nil //too many possible hashes to handle
    else
      for {
        author <- lettersNormaliseTokenise(text).filterNot(stopWords).distinct.take(4)
        year <- digits.filter(_.length == 4).flatMap(x => Try(x.toInt).toOption).filter(x => x < 2050 && x > 1900)
        (bpage, epage) <- collections.sortedPairs(collections.excludeOne(digits.flatMap(x => Try(x.toInt).toOption), (x:Int) => x == year))
      } yield List(author, year, bpage, epage).mkString("#")
  }
}