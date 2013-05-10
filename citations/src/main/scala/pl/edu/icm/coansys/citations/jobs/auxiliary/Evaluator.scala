package pl.edu.icm.coansys.citations.jobs.auxiliary

import collection.JavaConversions._
import com.nicta.scoobi.application.ScoobiApp
import com.nicta.scoobi.Scoobi
import com.nicta.scoobi.InputsOutputs._
import com.nicta.scoobi.Persist._
import org.apache.commons.lang.StringUtils
import pl.edu.icm.coansys.citations.util.AugmentedDList.augmentDList
import pl.edu.icm.coansys.citations.indices.{AuthorIndex, EntityIndex}
import pl.edu.icm.coansys.citations.util.{libsvm, nlm, XPathEvaluator}
import org.apache.commons.io.IOUtils
import pl.edu.icm.coansys.citations.data.{SimilarityMeasurer, MatchableEntity}
import pl.edu.icm.cermine.tools.classification.features.FeatureVectorBuilder
import pl.edu.icm.coansys.citations.data.feature_calculators._
import com.nicta.scoobi.core.DList
import pl.edu.icm.coansys.citations.util.matching._

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object Evaluator extends ScoobiApp {
  def run() {
    val indexUri = args(0)
    val inUri = args(1)
    val outUri = args(2)

    val results = Scoobi.convertFromSequenceFile[String, String](inUri)
      .mapWithResource(new ScalaObject {
      val index = new EntityIndex(indexUri)
      val similarityMeasurer = new SimilarityMeasurer

      def close() {
        index.close()
      }
    }) {
      case (res, (k, v)) =>
        val parts = v.split("\n", 2)
        val ids = parts(0).split(" ").filterNot(StringUtils.isEmpty).map(_.substring(4)).toSet
        val xmlString = parts(1)
        val eval = XPathEvaluator.fromInputStream(IOUtils.toInputStream(xmlString))
        val ref = nlm.referenceMetadataBuilderFromNode(eval.asNode(".")).build()
        val srcCit = MatchableEntity.fromReferenceMetadata(ref)
        val dstDocs = ids.toList.map(id => (id, res.index.getEntityById("doc_" + id)))

        if (dstDocs.isEmpty) {
          0
        } else {
          val featureVectorBuilder = new FeatureVectorBuilder[MatchableEntity, MatchableEntity]
          featureVectorBuilder.setFeatureCalculators(List(
            AuthorTrigramMatchFactor,
            AuthorTokenMatchFactor,
            PagesMatchFactor,
            SourceMatchFactor,
            TitleMatchFactor,
            YearMatchFactor))


          val sims = dstDocs.map {
            case (id, dst) =>
              (id, res.similarityMeasurer.similarity(srcCit, dst))
          }

          val best = sims.maxBy(_._2)._1
          if (best == k)
            1
          else
            0
        }
    }

    persist(toTextFile(results, outUri, overwrite = true))
  }
}
