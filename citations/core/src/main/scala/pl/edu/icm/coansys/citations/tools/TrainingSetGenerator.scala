package pl.edu.icm.coansys.citations.tools

import java.io.{FileWriter, BufferedWriter, File}
import pl.edu.icm.cermine.bibref.CRFBibReferenceParser
import pl.edu.icm.coansys.citations.util.dataset_readers
import scala.io.{Codec, Source}
import pl.edu.icm.coansys.citations.data.{SimilarityMeasurer, MatchableEntity}
import pl.edu.icm.coansys.commons.scala.xml
import pl.edu.icm.coansys.commons.scala.automatic_resource_management._
import pl.edu.icm.coansys.citations.util.classification.svm.SvmClassifier

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object TrainingSetGenerator {
  def main(args: Array[String]) {
    val citationsFile = new File(raw"C:\Users\matfed\Desktop\cora-all")
    val outFile = new File(raw"C:\Users\matfed\Desktop\coraSvmTrainingHeur.txt")
    val bibReferenceParser = new CRFBibReferenceParser(
      this.getClass.getResourceAsStream("/pl/edu/icm/cermine/bibref/acrf-small.ser.gz"))

    val citations =
      dataset_readers.getTaggedReferenceListFromCorarefSource(Source.fromFile(citationsFile)(Codec.ISO8859))

    val indexedCitations = citations.zipWithIndex

    val entities = indexedCitations.map {case ((cluster, text), id) =>
      MatchableEntity.fromUnparsedReference(
        bibReferenceParser, id.toString, xml.removeTags(text, " ").split(raw"\s+").mkString(" "))}

    val heuristic = new NewHeuristic(entities)

    val matchArray = Array.ofDim[Boolean](citations.size, citations.size)
    for {((cluster1, _), id1) <- indexedCitations
         ((cluster2, _), id2) <- indexedCitations} {
      matchArray(id1)(id2) = cluster1 == cluster2
    }

    val pairs = entities
      .flatMap(ent => Stream.continually(ent.id) zip heuristic.getHeuristiclyMatching(ent).toIterable)
      .filter{case (x,y) => x.toInt < y.toInt}

    val entitiesIndex = entities.map(ent => (ent.id, ent)).toMap
    using(new BufferedWriter(new FileWriter(outFile))) {
      writer =>
        pairs.map { case (x, y) =>
          val e1 = entitiesIndex(x)
          val e2 = entitiesIndex(y)
          val fv = SimilarityMeasurer.advancedFvBuilder.calculateFeatureVectorValues((e1, e2))
          val line = SvmClassifier.featureVectorValuesToLibSvmLine(fv, if (matchArray(x.toInt)(y.toInt)) 1 else 0)
          writer.write(line + "\n")
        }
    }
  }
}
