/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.util

import pl.edu.icm.coansys.importers.models.DocumentProtos.BasicMetadata
import scala.collection.JavaConversions._

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object misc {
  def normalizedAuthorTokensFromAuthorList(meta: BasicMetadata) = {
    meta.getAuthorList.toIterable
      .flatMap {
      author =>
        List(
          author.getName,
          author.getForenames,
          author.getSurname).flatMap(_.split( """[^\p{L}]+"""))
    }
      .filter(_.length > 1)
      .map(_.toLowerCase)
      .toSet
  }

  private val uuidCharset = "UTF-8"

  def uuidEncode(uuid: String): Array[Byte] =
    uuid.getBytes(uuidCharset)

  def uuidDecode(bytes: Array[Byte]): String =
    new String(bytes, uuidCharset)

  def extractNumbers(s: String): List[String] = {
    """(?<=(^|\D))\d+(?=($|\D))""".r.findAllIn(s).toList
  }

  def extractYear(s: String): Option[String] = {
    val baseYear = 2000
    val candidates = extractNumbers(s).filter(_.length == 4).map(_.toInt)

    if (candidates.isEmpty)
      None
    else
      Some(candidates.minBy(x => math.abs(x - baseYear)).toString)

  }
}
