/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.tools.cermine

import pl.edu.icm.coansys.citations.tools.cermine.util._
import java.io.File
import scala.util.Random

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

      writeCitationsToXml(new File(testDir, "mixed-citations.xml"), test.unzip._1)
      writeCitationsToXml(new File(trainDir, "mixed-citations.xml"), train.unzip._1)
    }

  }

  def main(args: Array[String]) {
    val inDir = """C:\Users\matfed\Desktop\parser-training"""
    val outDir = """C:\Users\matfed\Desktop\cermine-testing"""
    xmlsToFolds(new File(inDir).listFiles(), new File(outDir), foldsNo = 5)
  }
}