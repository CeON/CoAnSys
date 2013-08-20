package pl.edu.icm.coansys.citations.tools.pic

import java.io.{FileWriter, BufferedWriter, File}
import pl.edu.icm.coansys.citations.data.{MatchableEntity, SimilarityMeasurer}
import pl.edu.icm.coansys.citations.util.classification.svm.SvmClassifier
import pl.edu.icm.coansys.commons.scala.automatic_resource_management.using

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object SvmFilesOnHeur {
  val dbUrl = "file:///C:/Users/matfed/Desktop/pic-eval/db.seq"
  val parsedUrl = "file:///C:/Users/matfed/Desktop/pic-eval/parsed.seq"
  val unparsedUrl = "file:///C:/Users/matfed/Desktop/pic-eval/unparsed.seq"

  val db = TempCommons.readDocumentsDatabase(dbUrl)
  val parsed = TempCommons.readParsedCitations(parsedUrl, unparsedUrl)

  def writeFile(dbEntities: Iterable[MatchableEntity], gold: Iterable[(String, String)], file: File) {
    val heuristic = new NewHeuristic(dbEntities)
    using(new BufferedWriter(new FileWriter(file))) {
      writer =>
        for {
          (src, dst) <- gold if db.contains(dst) && parsed.contains(src)
          srcEnt = parsed(src)
          cand <- heuristic.getHeuristiclyMatching(srcEnt)
          fv = SimilarityMeasurer.advancedFvBuilder.calculateFeatureVectorValues((srcEnt, db(cand.substring(4))))
          line = SvmClassifier.featureVectorValuesToLibSvmLine(fv, if (cand.substring(4) == dst) 1 else 0)
        } writer.append(line + "\n")
    }
  }

  def main(args: Array[String]) {
    val goldPath = "C:\\Users\\matfed\\Desktop\\pic-eval\\citations-rewritten.csv"
    val trainingFile = "C:\\Users\\matfed\\Desktop\\pic-eval\\svmPicHeurTrain.txt"
    val testingFile = "C:\\Users\\matfed\\Desktop\\pic-eval\\svmPicHeurTest.txt"

    val gold = TempCommons.readPicGroundTruth(new File(goldPath))


    val (trainGold, testGold) = gold.filter(x => parsed.contains(x._1) && db.contains(x._2)).zipWithIndex.partition(_._2 % 2 == 0)


    val (trainU, testU) = (db.keySet -- gold.unzip._2).toIterable.zipWithIndex.partition(_._2 % 2 == 0)

    val trainDb = (trainGold.unzip._1.unzip._2 ++ trainU.unzip._1).map(db)
    val testDb = (testGold.unzip._1.unzip._2 ++ testU.unzip._1).map(db)

    writeFile(trainDb, trainGold.unzip._1, new File(trainingFile))
    writeFile(testDb, testGold.unzip._1, new File(testingFile))
  }
}

