/*
 * This file is part of CoAnSys project.
 * Copyright (c) 20012-2013 ICM-UW
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

package pl.edu.icm.coansys.citations.tools.cermine

import pl.edu.icm.coansys.citations.tools.cermine.util._
import java.io.File
import scala.util.Random
import org.jdom.output.XMLOutputter

/**
 * Generates Mallet files for n-fold x-validation from XML-s containing mixed citations.
 *
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object CermineTestGenerator {
  /**
   * Extracts mixed citations from XML files, shuffles them and creates folds.
   */
  def xmlsToFolds(inFiles: TraversableOnce[File], outDir: File, foldsNo: Int = 5) {
    val shuffledCitations = Random.shuffle(extractMixedCitationsFromXmls(inFiles)).toList

    for (i <- 0 until foldsNo) {
      val (test, train) = shuffledCitations.zipWithIndex.partition(_._2 % foldsNo == i)
      val testDir = new File(outDir, "fold" + i + "-test")
      val trainDir = new File(outDir, "fold" + i + "-train")
      testDir.mkdirs()
      trainDir.mkdirs()

      writeCitationsToXml(new File(testDir, "mixed-citations.xml"), test.unzip._1.map(new XMLOutputter().outputString))
      writeCitationsToXml(new File(trainDir, "mixed-citations.xml"), train.unzip._1.map(new XMLOutputter().outputString))
    }

  }

  def main(args: Array[String]) {
    val inDir = """C:\Users\matfed\Desktop\parser-training"""
    val outDir = """C:\Users\matfed\Desktop\cermine-testing"""
    xmlsToFolds(new File(inDir).listFiles(), new File(outDir), foldsNo = 5)
  }
}