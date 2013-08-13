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
import org.jdom.output.XMLOutputter
import util.Random
import org.w3c.dom.{Element, Node}
import org.jdom.input.DOMBuilder

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object MixedCitationExtractor extends ScoobiApp {
  def run() {
    configuration.set("mapred.task.timeout", 60 * 60 * 1000)

    val nlmUri = args(0)
    val outUri = args(1)
    val nlms = fromSequenceFile[String, String](List(nlmUri))
    val converted = nlms
      .flatMap {
      case (_, xmlString) =>
        def nodeToString(n: Node) =
          new XMLOutputter().outputString(new DOMBuilder().build(n.asInstanceOf[Element]))
        val eval = XPathEvaluator.fromInputStream(IOUtils.toInputStream(xmlString))
        eval.asNodes( """/article/back/ref-list/ref""").map(nodeToString)
    }
      .groupBy(_ => Random.nextFloat())
      .flatMap {
      case (k, v) => Stream.continually(k) zip v.toStream
    }
    persist(toSequenceFile(converted, outUri, overwrite = true))
  }
}
