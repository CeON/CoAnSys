/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.jobs

import com.nicta.scoobi.application.ScoobiApp
import pl.edu.icm.coansys.citations.util.{XPathEvaluator, BytesConverter}
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentWrapper
import com.nicta.scoobi.InputsOutputs._
import org.apache.commons.io.IOUtils
import com.nicta.scoobi.Persist._
import org.jdom.output.XMLOutputter
import org.jdom.Element
import util.Random

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object MixedCitationExtractor extends ScoobiApp {
  override def upload = false

  def run() {
    configuration.set("mapred.task.timeout", 60 * 60 * 1000)
    implicit val wrapperConverter =
      new BytesConverter[DocumentWrapper](_.toByteString.toByteArray, DocumentWrapper.parseFrom(_))
    val nlmUri = args(0)
    val outUri = args(1)
    val nlms = convertFromSequenceFile[String, String](List(nlmUri))
    val converted = nlms
      .flatMap {
      case (_, xmlString) =>
        val eval = XPathEvaluator.fromInputStream(IOUtils.toInputStream(xmlString))
        eval.asNodes( """/article/back/ref-list/ref""")
          .map(entity => new XMLOutputter().outputString(entity.asInstanceOf[Element]))
    }
      .groupBy(_ => Random.nextFloat)
      .flatMap {
      case (k, v) => Stream.continually(k) zip v
    }
    persist(convertToSequenceFile(converted, outUri, overwrite = true))
  }
}
