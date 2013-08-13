/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2013 ICM-UW
 * 
 * CoAnSys is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * CoAnSys is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with CoAnSys. If not, see <http://www.gnu.org/licenses/>.
 */

package pl.edu.icm.coansys.citations.tools.matcher

import io.{Codec, Source}
import pl.edu.icm.coansys.citations.util.dataset_readers
import pl.edu.icm.coansys.citations.tools.cermine.util.writeCitationsToXml
import java.io._
import util.Random
import org.jdom.output.XMLOutputter
import pl.edu.icm.coansys.commons.scala.automatic_resource_management._
import pl.edu.icm.coansys.commons.scala.xml
import pl.edu.icm.cermine.bibref.transformers.BibEntryToNLMElementConverter

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object FirstTestSetGenerator {
  def main(args: Array[String]) {
    val inputDir = new File("C:\\Users\\matfed\\data\\cora-ref")
    val outputDir = new File("C:\\Users\\matfed\\Desktop\\matcher-test")
    val parserTrainingFileName = "parserTraining.xml"
    val matcherTrainingFileName = "matcherTraining.txt"
    val matcherTestFileName = "matcherTesting.txt"
    val foldsCount = 3

    val citations =
      inputDir.listFiles(new FilenameFilter {
        def accept(dir: File, name: String) = name.endsWith("labeled")
      })
        .flatMap(f => dataset_readers.getTaggedReferenceListFromCorarefSource(Source.fromFile(f)(Codec.ISO8859)))
    val clusters = citations.groupBy(_._1)
    val folds = Random.shuffle(clusters).zipWithIndex.groupBy(_._2 % foldsCount).mapValues {
      _.map(_._1).flatMap(_._2)
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

    for (i <- 0 until foldsCount) {
      val foldDir = new File(outputDir, "fold" + i)
      foldDir.mkdirs()
      val parserTrainingSet =
        folds(i).map(_._2) // remove cluster ids
      val parserTrainingBibEntries =
        parserTrainingSet.map(dataset_readers.taggedReferenceToBibEntry(_, dataset_readers.corarefTagMapping))
      val mixedCitations = parserTrainingBibEntries.map(entry => new XMLOutputter().outputString({
        val elem = new BibEntryToNLMElementConverter().convert(entry)
        dataset_readers.findAndCollapseStringName(elem)
        elem
      }))

      writeCitationsToXml(new File(foldDir, parserTrainingFileName), mixedCitations)
      removeTagsAndWrite(folds((i + 1) % foldsCount), new File(foldDir, matcherTrainingFileName))
      removeTagsAndWrite(folds((i + 2) % foldsCount), new File(foldDir, matcherTestFileName))
    }
  }
}
