package pl.edu.icm.coansys.citations.jobs.auxiliary

import collection.JavaConversions._
import com.nicta.scoobi.application.ScoobiApp
import com.nicta.scoobi.Scoobi
import com.nicta.scoobi.InputsOutputs._
import com.nicta.scoobi.Persist._
import org.apache.commons.lang.StringUtils
import pl.edu.icm.coansys.citations.util.AugmentedDList.augmentDList
import pl.edu.icm.coansys.citations.indices.{AuthorIndex, EntityIndex}
import pl.edu.icm.coansys.citations.util.{NoOpClose, libsvm, nlm, XPathEvaluator}
import org.apache.commons.io.IOUtils
import pl.edu.icm.coansys.citations.data.{SimilarityMeasurer, MatchableEntity}
import pl.edu.icm.cermine.tools.classification.features.FeatureVectorBuilder
import pl.edu.icm.coansys.citations.data.feature_calculators._
import com.nicta.scoobi.core.DList
import pl.edu.icm.coansys.citations.util.matching._
import com.nicta.scoobi.lib.Relational

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object Evaluator extends ScoobiApp {
  def run() {
    val indexUri = args(0)
    val inUri = args(1)
    val outUri = args(2)

    val indexList = Scoobi.convertFromSequenceFile[String, MatchableEntity](indexUri)
    val heuristic = Scoobi.convertFromSequenceFile[String, String](inUri)
    // citId->citData
    val citations = heuristic.map {
      case (k, v) =>
        println("citations step on " + k)
        val parts = v.split("\n", 2)
        val xmlString = parts(1)
        val eval = XPathEvaluator.fromInputStream(IOUtils.toInputStream(xmlString))
        val ref = nlm.referenceMetadataBuilderFromNode(eval.asNode(".")).build()
        val cit = MatchableEntity.fromReferenceMetadata(ref)
        (k, cit)
    }
    // doc_destDocId->correctId
    val proposed = heuristic.flatMap {
      case (k, v) =>
        println("proposed step on " + k)
        val parts = v.split("\n", 2)
        val ids = parts(0).split(" ").filterNot(StringUtils.isEmpty).toSet
        ids zip Stream.continually(k)
    }

    //citId->heuristicly matched data
    val withData = Relational.joinLeft(proposed, indexList).map {
      case (prefixedDest, (src, Some(entity))) =>
        println("withData on " + prefixedDest)
        //val dest = prefixedDest.substring(4)
        (src, entity)
    }.groupByKey[String, MatchableEntity]
    val results = Relational.joinLeft(citations, withData).flatMapWithResource(new SimilarityMeasurer with NoOpClose) {
      case (measurer, (key, (cit, Some(iter)))) =>
        println("last step on " + key)
        val best = iter.maxBy(measurer.similarity(_, cit))
        if (best.id.substring(4) != key)
          Some(cit.toDebugString, best.toDebugString)
        else
          None
    }

    persist(toTextFile(results, outUri, overwrite = true))
  }
}
