package pl.edu.icm.coansys.citations.jobs

import com.nicta.scoobi.Scoobi._
import Reduction._
import pl.edu.icm.coansys.citations.util.{misc, MyScoobiApp}
import pl.edu.icm.coansys.citations.data.WireFormats._
import pl.edu.icm.coansys.citations.data.MatchableEntity
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper
import scala.util.Try

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object NewHeuristicAdder extends MyScoobiApp {
  val minMatchingTitleTokens = 3
  val indexedTitleTokens = 4

  def approximateYear(year: String) = for {
      diff <- -1 to 1
      year <- Try(year.toInt).toOption
    } yield (year + diff).toString

  def run() {
    val entitiesUrl = args(0)
    val dbUrl = args(1)
    val outUrl = args(2)

    val entities = valueFromSequenceFile[MatchableEntity](entitiesUrl)
    val db = valueFromSequenceFile[DocumentWrapper](dbUrl)
    val entitiesDb = db.map(wrapper => MatchableEntity.fromDocumentMetadata(wrapper.getRowId, wrapper.getDocumentMetadata))

    val nameIndex = entitiesDb.mapFlatten {
      entity =>
        for {
          author <- misc.lettersNormaliseTokenise(entity.author)
        } yield (author + entity.year, entity.id)
    }

    val titleIndex = entitiesDb.mapFlatten {
      entity =>
        for {
          title <- misc.lettersNormaliseTokenise(entity.title).take(indexedTitleTokens)
        } yield (title + entity.year, entity.id)
    }

    val authorTaggedEntities = entities.mapFlatten {
      entity =>
        for {
          year <- approximateYear(entity.year)
          author <- entity.normalisedAuthorTokens
        } yield (author + year, entity)
    }

    val (matched, unmatched) = authorTaggedEntities.joinLeft(nameIndex).values.partition(_._2.isDefined)

    val authorMatched = matched.map{
      case (entity, Some(candId)) => (entity, candId)
      case _ => throw new RuntimeException("It should never happen")
    }

    val titleTaggedEntities = unmatched.keys.mapFlatten {
      entity =>
        for {
          year <- approximateYear(entity.year)
          title <- entity.title.split(raw"\s+")
        } yield (title + year, entity)
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
    (authorMatched ++ titleMatched).toSequenceFile(outUrl, overwrite = true)
  }
}