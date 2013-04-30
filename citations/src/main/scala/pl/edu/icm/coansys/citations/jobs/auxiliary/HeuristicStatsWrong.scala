package pl.edu.icm.coansys.citations.jobs.auxiliary

import com.nicta.scoobi.application.ScoobiApp
import com.nicta.scoobi.Scoobi
import com.nicta.scoobi.InputsOutputs._
import com.nicta.scoobi.Persist._
import org.apache.commons.lang.StringUtils
import pl.edu.icm.coansys.citations.util.AugmentedDList.augmentDList
import pl.edu.icm.coansys.citations.indices.EntityIndex
import pl.edu.icm.coansys.citations.util.{nlm, XPathEvaluator}
import org.apache.commons.io.IOUtils
import pl.edu.icm.coansys.citations.data.MatchableEntity

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object HeuristicStatsWrong extends ScoobiApp {
  def run() {
    val indexUri = args(0)
    val inUri = args(1)
    val outUri = args(2)

    val results = Scoobi.convertFromSequenceFile[String, String](inUri)
      .flatMapWithResource(new EntityIndex(indexUri)) { case (index, (k, v)) =>
        val parts = v.split("\n", 2)
        val ids = parts(0).split(" ").filterNot(StringUtils.isEmpty).map(_.substring(4))
        val xmlString = parts(1)
        if (ids contains k) {
          None
        } else {
          val eval = XPathEvaluator.fromInputStream(IOUtils.toInputStream(xmlString))
          val ref = nlm.referenceMetadataBuilderFromNode(eval.asNode(".")).build()
          val srcCit = MatchableEntity.fromReferenceMetadata(ref)
          val srcDoc = index.getEntityById("doc_" + k)
          val dstDocs = ids.map(id => index.getEntityById("doc_" + id))
          Some((xmlString + "\n\n" + srcCit.toDebugString + "\n\n" + srcDoc.toDebugString,
            dstDocs.map(_.toDebugString).mkString("\n")))
        }
      }

    persist(convertToSequenceFile(results, outUri, overwrite = true))
  }
}
