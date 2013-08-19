/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2013 ICM-UW
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
 * You should have received a copy of the GNU Affero General Public Licensealong with CoAnSys. If not, see <http://www.gnu.org/licenses/>.
 */

package pl.edu.icm.coansys.citations.jobs

import com.nicta.scoobi.Scoobi._
import Reduction._
import pl.edu.icm.coansys.citations.util.{misc, MyScoobiApp}
import pl.edu.icm.coansys.citations.data.MatchableEntity
import scala.util.Try

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object NewHeuristicAdder extends MyScoobiApp {
  def minMatchingTitleTokens = 3
  def indexedTitleTokens = 4

  def approximateYear(year: String) = for {
      diff <- -1 to 1
      year <- Try(year.toInt).toOption
    } yield (year + diff).toString

  def run() {
    val entitiesUrl = args(0)
    val dbUrl = args(1)
    val outUrl = args(2)

    val entities = valueFromSequenceFile[MatchableEntity](entitiesUrl)
    val entitiesDb = valueFromSequenceFile[MatchableEntity](dbUrl)

    val nameIndex = entitiesDb.mapFlatten {
      entity =>
        for {
          author <- misc.lettersNormaliseTokenise(entity.author).distinct
        } yield (author + entity.year, entity.id)
    }

    val titleIndex = entitiesDb.mapFlatten {
      entity =>
        for {
          title <- misc.lettersNormaliseTokenise(entity.title).take(indexedTitleTokens).distinct
        } yield (title + entity.year, entity.id)
    }

    val authorTaggedEntities = entities.mapFlatten {
      entity =>
        val text = entity.rawText.getOrElse("")
        for {
          year <- misc.digitsNormaliseTokenise(text)
          approxYear <- approximateYear(year)
          author <- misc.lettersNormaliseTokenise(text)
        } yield (author + approxYear, entity)
    }

    val authorMatched = authorTaggedEntities.joinLeft(nameIndex).values.mapFlatten {
      case (entity, Some(candId)) => Some((entity, candId))
      case _ => None
    }

    val unmatched = entities.diff(authorMatched.keys)

    val titleTaggedEntities = unmatched.mapFlatten {
      entity =>
        val text = entity.rawText.getOrElse("")
        for {
          year <- misc.digitsNormaliseTokenise(text)
          approxYear <- approximateYear(year)
          title <- misc.lettersNormaliseTokenise(text)
        } yield (title + approxYear, entity)
    }

    implicit val grouping = new Grouping[(MatchableEntity, String)] {
      def groupCompare(x: (MatchableEntity, String), y: (MatchableEntity, String)) = {
        val res = scalaz.Ordering.fromInt(x._1.id compareTo y._1.id)
        if (res == scalaz.Ordering.EQ)
          scalaz.Ordering.fromInt(x._1.id compareTo y._1.id)
        else
          res
      }
    }
    
    val titleMatched = titleTaggedEntities.join(titleIndex).values.map(x => (x, 1))
      .groupByKey[(MatchableEntity, String), Int].combine(Sum.int).filter(_._2 >= minMatchingTitleTokens).keys
    persist(authorMatched.toSequenceFile(outUrl + "_authorMatched", overwrite = true))
    persist(titleMatched.toSequenceFile(outUrl + "_titleMatched", overwrite = true))
    persist((fromSequenceFile[MatchableEntity, String](outUrl + "_authorMatched") ++ fromSequenceFile[MatchableEntity, String](outUrl + "_titleMatched")).distinct.toSequenceFile(outUrl, overwrite = true))
//    persist((authorMatched ++ titleMatched).toSequenceFile(outUrl, overwrite = true))
  }
}
