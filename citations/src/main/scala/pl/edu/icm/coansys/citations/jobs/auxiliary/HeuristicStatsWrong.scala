package pl.edu.icm.coansys.citations.jobs.auxiliary

import com.nicta.scoobi.application.ScoobiApp
import com.nicta.scoobi.Scoobi
import com.nicta.scoobi.Persist._
import pl.edu.icm.coansys.citations.util.{XPathEvaluator, nlm}
import org.apache.commons.io.IOUtils
import pl.edu.icm.coansys.citations.data.MatchableEntity

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object HeuristicStatsWrong extends ScoobiApp {
  def run() {
    val inUri = args(0)

    val results = Scoobi.convertFromSequenceFile[String, String](inUri)
      .flatMap { case (k, v) =>
        val parts = v.split("\n", 2)
        val ids = parts(0).split(" ").map(_.substring(4))
        val xmlString = parts(1)
        if (ids contains k) {
          None
        } else {
          val eval = XPathEvaluator.fromInputStream(IOUtils.toInputStream(xmlString))
          val ref = nlm.referenceMetadataBuilderFromNode(eval.asNode("."))
          val authorTokens = MatchableEntity.fromReferenceMetadata(ref.build()).normalisedAuthorTokens
          Some((1, ids.length, authorTokens.size))
        }
      }
      .reduce{ case ((count1, idsLen1, tokLen1),(count2, idsLen2, tokLen2)) =>
        (count1 + count2, idsLen1 + idsLen2, tokLen1 + tokLen2)
      }

    val persisted = persist(results)
    println(persisted)
  }
}
