/*
 * This file is part of CoAnSys project.
 * Copyright (c) 20012-2013 ICM-UW
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

package pl.edu.icm.coansys.citations.jobs.auxiliary

import com.nicta.scoobi.Scoobi._
import pl.edu.icm.coansys.citations.util.XPathEvaluator
import org.apache.commons.io.IOUtils
import org.apache.commons.lang.StringUtils
import scala.Some

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object HeuristicEvaluator extends ScoobiApp {
  def run() {
    val refsUri = args(0)
    val docsUri = args(1)
    val outUri = args(2)

    val refs = valueFromSequenceFile[String](refsUri).flatMap {
      xml =>
        val eval = XPathEvaluator.fromInputStream(IOUtils.toInputStream(xml))
        val id = eval( """.//pub-id[@pub-id-type='pmid']""")
        if (StringUtils.isNotEmpty(id))
          Some(id, Option.apply(xml))
        else
          None
    }
    val docs = valueFromSequenceFile[String](docsUri).flatMap {
      xml =>
        val eval = XPathEvaluator.fromInputStream(IOUtils.toInputStream(xml))
        val id = eval( """/article/front/article-meta/article-id[@pub-id-type='pmid']""")
        if (StringUtils.isNotEmpty(id))
          Some(id, Option.empty[String])
        else
          None
    }

    val matchable = (refs ++ docs).groupByKey[String, Option[String]].flatMap {
      case (id, iter) =>
        try {
          val (defined, undefined) = iter.partition(_.isDefined)
          if (!undefined.isEmpty) 
            Stream.continually(id) zip defined.flatten.toStream
          else
            Stream.empty[(String, String)]
        } catch {
          case e: Exception =>
            e.printStackTrace()
            Stream.empty[(String, String)]
        }
    }

    persist(toSequenceFile(matchable, outUri, overwrite = true))
  }
}
