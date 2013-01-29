/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.tools.matcher

import io.{Codec, Source}
import pl.edu.icm.coansys.citations.util.dataset_readers
import java.io._
import util.Random
import org.jdom.output.XMLOutputter
import pl.edu.icm.cermine.bibref.parsing.tools.CitationUtils
import pl.edu.icm.coansys.commons.scala.automatic_resource_management._
import pl.edu.icm.coansys.commons.scala.xml

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object FirstTestSetGenerator {
  def main(args: Array[String]) {
    val inputDir = new File("C:\\Users\\matfed\\data\\cora-ref")
    val parserTrainingFile = new File("C:\\Users\\matfed\\Desktop\\matcher-test\\parserTraining.xml")
    val matcherTrainingFile = new File("C:\\Users\\matfed\\Desktop\\matcher-test\\matcherTraining.txt")
    val matcherTestFile = new File("C:\\Users\\matfed\\Desktop\\matcher-test\\matcherTesting.txt")
    val citations =
      inputDir.listFiles(new FilenameFilter {
        def accept(dir: File, name: String) = name.endsWith("labeled")
      })
        .flatMap(f => dataset_readers.getTaggedReferenceListFromCorarefSource(Source.fromFile(f)(Codec.ISO8859)))
    val clusters = citations.groupBy(_._1)
    val folds = Random.shuffle(clusters).zipWithIndex.groupBy(_._2 % 3).mapValues {
      _.map(_._1).flatMap(_._2)
    }

    val parserTrainingSet =
      folds(0).map(_._2) // remove cluster ids
    val parserTrainingBibEntries =
      parserTrainingSet.map(dataset_readers.taggedReferenceToBibEntry(_, dataset_readers.corarefTagMapping))
    val mixedCitations = parserTrainingBibEntries.map(entry => new XMLOutputter().outputString({
      val elem = CitationUtils.bibEntryToNLM(entry)
      dataset_readers.findAndCollapseStringName(elem)
      elem
    }))
    using(new BufferedWriter(new FileWriter(parserTrainingFile))) {
      writer =>
        mixedCitations foreach (l => writer.write(l + "\n"))
    }

    def removeTagsAndWrite(cits: TraversableOnce[(String, String)], outFile: File) {
      val untaggedCits = cits.map {
        case (id, taggedCit) =>
          (id, xml.removeTags(taggedCit))
      }
      using(new BufferedWriter(new FileWriter(outFile))) {
        writer =>
          untaggedCits foreach {
            case (id, cit) => writer.write(id.trim + " " + cit.trim + "\n")
          }
      }
    }

    removeTagsAndWrite(folds(1), matcherTrainingFile)
    removeTagsAndWrite(folds(2), matcherTestFile)
  }
}
