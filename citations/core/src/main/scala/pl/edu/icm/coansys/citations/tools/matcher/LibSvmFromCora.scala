package pl.edu.icm.coansys.citations.tools.matcher

import resource._
import java.io.{BufferedWriter, FileWriter, File}
import scala.io.{Codec, Source}
import pl.edu.icm.ceon.scala_commons.xml
import pl.edu.icm.coansys.citations.data.{SimilarityMeasurer, MatchableEntity}
import pl.edu.icm.cermine.bibref.CRFBibReferenceParser
import pl.edu.icm.coansys.citations.util.classification.svm.SvmClassifier
import pl.edu.icm.coansys.citations.util.dataset_readers

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object LibSvmFromCora {
  def main(args: Array[String]) {
    val citationsFile = new File(raw"C:\Users\matfed\Desktop\cora-all")
    val outFile = new File(raw"C:\Users\matfed\Desktop\coraSvmTraining.txt")
    val bibReferenceParser = new CRFBibReferenceParser(
      this.getClass.getResourceAsStream("/pl/edu/icm/cermine/bibref/acrf-small.ser.gz"))

    val citations =
      dataset_readers.getTaggedReferenceListFromCorarefSource(Source.fromFile(citationsFile)(Codec.ISO8859))

    val entities = citations.map {case (id, text) =>
      MatchableEntity.fromUnparsedReference(
        bibReferenceParser, id, xml.removeTags(text, " ").split(raw"\s+").mkString(" "))}

    for (writer <- managed(new BufferedWriter(new FileWriter(outFile)))) {
      for {
        e1 <- entities
        e2 <- entities
      }  {
        val fv = SimilarityMeasurer.advancedFvBuilder.calculateFeatureVectorValues((e1, e2))
        val line = SvmClassifier.featureVectorValuesToLibSvmLine(fv, if (e1.id == e2.id) 1 else 0)
        writer.write(line + "\n")
      }
    }


  }
}
