/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.jobs.auxiliary

import com.nicta.scoobi.application.ScoobiApp
import pl.edu.icm.coansys.citations.util.XPathEvaluator
import com.nicta.scoobi.InputsOutputs._
import org.apache.commons.io.IOUtils
import com.nicta.scoobi.Persist._
import org.jdom.output.XMLOutputter
import util.Random
import org.w3c.dom.{Element, Node}
import org.jdom.input.DOMBuilder

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object MixedCitationExtractor extends ScoobiApp {
  override lazy val upload = false

  def run() {
    configuration.set("mapred.task.timeout", 60 * 60 * 1000)

    val nlmUri = args(0)
    val outUri = args(1)
    val nlms = convertFromSequenceFile[String, String](List(nlmUri))
    val converted = nlms
      .flatMap {
      case (_, xmlString) =>
        def nodeToString(n: Node) =
          new XMLOutputter().outputString(new DOMBuilder().build(n.asInstanceOf[Element]))
        val eval = XPathEvaluator.fromInputStream(IOUtils.toInputStream(xmlString))
        eval.asNodes( """/article/back/ref-list/ref""").map(nodeToString)
    }
      .groupBy(_ => Random.nextFloat)
      .flatMap {
      case (k, v) => Stream.continually(k) zip v.toStream
    }
    persist(convertToSequenceFile(converted, outUri, overwrite = true))
  }
}
