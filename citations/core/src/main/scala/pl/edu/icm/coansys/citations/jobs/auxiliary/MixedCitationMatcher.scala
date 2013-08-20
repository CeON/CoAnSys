/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2013 ICM-UW
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
import pl.edu.icm.coansys.citations.util.AugmentedDList.augmentDList
import pl.edu.icm.coansys.citations.indices.EntityIndex

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object MixedCitationMatcher extends ScoobiApp {
  def run() {
    configuration.set("mapred.task.timeout", 60 * 60 * 1000)

    val indexUri = args(0)
    val citationsUri = args(1)
    val outUri = args(2)
    val cits = valueFromSequenceFile[String](List(citationsUri))
    val res = cits.flatMapWithResource(new EntityIndex(indexUri)) {
      case (index, xml) =>
        val eval = XPathEvaluator.fromInputStream(IOUtils.toInputStream(xml))
        val pmid = eval( """/ref//pub-id[@pub-id-type='pmid']""")
        try {
          Some((xml, index.getEntityById("doc-" + pmid)))
        } catch {
          case ex: Exception =>
            None
        }
    }

    persist(toSequenceFile(res, outUri, overwrite = true))
  }
}
