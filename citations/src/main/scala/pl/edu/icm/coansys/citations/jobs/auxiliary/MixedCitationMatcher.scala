/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.jobs.auxiliary

import com.nicta.scoobi.application.ScoobiApp
import com.nicta.scoobi.InputsOutputs._
import pl.edu.icm.coansys.citations.util.XPathEvaluator
import org.apache.commons.io.IOUtils
import com.nicta.scoobi.Persist._
import pl.edu.icm.coansys.citations.util.AugmentedDList.augmentDList
import pl.edu.icm.coansys.citations.indices.EntityIndex

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object MixedCitationMatcher extends ScoobiApp {
  override def upload = false

  def run() {
    configuration.set("mapred.task.timeout", 60 * 60 * 1000)

    val indexUri = args(0)
    val citationsUri = args(1)
    val outUri = args(2)
    val cits = convertValueFromSequenceFile[String](List(citationsUri))
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

    persist(convertToSequenceFile(res, outUri, overwrite = true))
  }
}
