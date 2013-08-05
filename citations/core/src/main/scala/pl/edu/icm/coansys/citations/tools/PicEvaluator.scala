package pl.edu.icm.coansys.citations.tools

import collection.JavaConversions._
import pl.edu.icm.coansys.citations.util.sequencefile.ConvertingSequenceFileIterator
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper
import pl.edu.icm.coansys.citations.util.{misc, BytesConverter}
import pl.edu.icm.coansys.citations.util.classification.svm.SvmClassifier.featureVectorValuesToLibSvmLine
import pl.edu.icm.coansys.citations.data.{MatchingResult, SimilarityMeasurer, MatchableEntity}
import scala.io.Source
import pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue
import pl.edu.icm.cermine.tools.classification.svm.SVMClassifier
import pl.edu.icm.cermine.tools.classification.general.TrainingSample
import java.util
import pl.edu.icm.coansys.commons.scala.automatic_resource_management.using
import pl.edu.icm.cermine.tools.classification.features.FeatureVector
import pl.edu.icm.coansys.citations.data.feature_calculators._
import java.io.{File, FileWriter}

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object PicEvaluator {
  val dbUrl = "file:///C:/Users/matfed/Desktop/pic-eval/db.seq"
  val parsedUrl = "file:///C:/Users/matfed/Desktop/pic-eval/parsed.seq"
  val unparsedUrl = "file:///C:/Users/matfed/Desktop/pic-eval/unparsed.seq"
  val heurUrl = "file:///C:/Users/matfed/Desktop/pic-eval/heur.seq"
  val goldPath = "C:\\Users\\matfed\\Desktop\\pic-eval\\citations-rewritten.csv"

  val gold = TempCommons.readPicGroundTruth(new File(goldPath))
  val db = TempCommons.readDocumentsDatabase(dbUrl)
  val parsed = TempCommons.readParsedCitations(parsedUrl, unparsedUrl)
  val heur = TempCommons.readHeuristic(heurUrl)

//  def getFeatures(fv: FeatureVector) = {
//    fv.getFeatureNames.map(x => x -> fv.getFeatureValue(x).asInstanceOf[Double]).toMap
//  }

  def default() {
    var all = 0
    var hgood = 0
    for ((src, dst) <- gold if parsed.contains(src) && db.contains(dst) && dst != "") {
      all += 1
      if (heur.getOrElse(src, Set()) contains "doc_" + dst)
        hgood += 1
      else {
        println("WRONG:")
        println(parsed(src).toDebugString)
        println(db(dst).toDebugString)
      }
    }

    println("hgood all")
    println((hgood, all))

    //    for ((src,dst) <- gold) {
    //      if (!parsed.contains(src)) {
    //        println("problem with" + src)
    //      }
    //      if (dst != "" && !db.contains(dst)) {
    //        println("problem with" + dst)
    //      }
    //    }

    val measurer = new SimilarityMeasurer

    val (trainingGold, testingGold) = gold.filter(_._2 != "").filter(x => parsed.contains(x._1)).filter(x => db.contains(x._2)).zipWithIndex.partition(_._2 % 2 != 0)

//    val classifier = new SVMClassifier[MatchableEntity, MatchableEntity, MatchingResult](measurer.featureVectorBuilder, classOf[MatchingResult]) {}
//
//    val training = new util.ArrayList[TrainingSample[MatchingResult]]()
//    for ((src1, dst1) <- trainingGold.unzip._1) {
//      for ((src2, dst2) <- trainingGold.unzip._1) {
//        val fv = measurer.featureVectorBuilder.getFeatureVector(parsed(src1), db(dst2))
//        val sample = new TrainingSample[MatchingResult](fv, if (dst1 == dst2) MatchingResult.Match else MatchingResult.NonMatch)
//        training.add(sample)
//      }
//    }
//
//    classifier.buildClassifier(training)

    val calcs = List(
      AuthorTrigramMatchFactor,
      AuthorTokenMatchFactor,
      PagesMatchFactor,
      SourceMatchFactor,
      TitleMatchFactor,
      YearMatchFactor)

    var my = 0
    var real = 0
    var isec = 0

    for ((src1, dst1) <- (trainingGold ++ testingGold).unzip._1) {
      for ((src2, dst2) <- (trainingGold ++ testingGold).unzip._1) {
        //        val fv = measurer.featureVectorBuilder.getFeatureVector(parsed(src1), db(dst2))
        //        val result = if (fv.getFeatures.reduce(_ + _) / fv.getFeatures.length > 0.5) MatchingResult.Match else MatchingResult.NonMatch
//        val result = classifier.predictLabel(parsed(src1), db(dst2))
        val result = if (measurer.similarity(parsed(src1), db(dst2)) > 0.5) MatchingResult.Match else MatchingResult.NonMatch
        //        val fv = calcs.map(_.calculateFeatureValue(parsed(src1), db(dst2))).toArray
        //        val result = if (classifier.predictProbabilities(fv)(1) > 0.5) MatchingResult.Match else MatchingResult.NonMatch
        if (dst1 == dst2)
          real += 1
        if (result == MatchingResult.Match)
          my += 1
        if ((dst1 == dst2) && (result == MatchingResult.Match))
          isec += 1
        if ((dst1 == dst2) != (result == MatchingResult.Match)) {
          println("WRONG: " + result)
          println(parsed(src1).toDebugString)
          println(db(dst2).toDebugString)
          //println (fv.dump())

        }
      }
    }

//    classifier.saveModel(raw"C:\Users\matfed\Desktop\pic.model")

    println("my real isec")
    println((my, real, isec))
    println(isec / my.toDouble)
    println(isec / real.toDouble)

    println("Okey")

  }

  def printMatching() {
    for ((src, dst) <- gold if dst != "") {
      if (parsed.contains(src) && db.contains(dst)) {
        println(parsed(src).toDebugString)
        println(db(dst).toDebugString)
//        val fv = measurer.featureVectorBuilder.getFeatureVector(parsed(src), db(dst))
//        println(fv.dump())
      }
    }
  }

//  def printAvgFVValues() {
//    val measurer = new AdvancedSimilarityMeasurer
//    val fvs =
//      for {(src, dst) <- gold if dst != "" && parsed.contains(src) && db.contains(dst)}
//      yield getFeatures(measurer.featureVectorBuilder.getFeatureVector(parsed(src), db(dst)))
//    val summed = fvs.reduce[Map[String, Double]] {
//      case (fv1, fv2) => fv1.keySet.map(x => x -> (fv1(x) + fv2(x))).toMap
//    }
//    val avged = summed.mapValues(_ / fvs.length)
//
//    avged.foreach(println)
//  }

//  def printWeak() {
//    val measurer = new AdvancedSimilarityMeasurer
//    for ((src, dst) <- gold if dst != "") {
//      if (parsed.contains(src) && db.contains(dst)) {
//        val fv = measurer.featureVectorBuilder.getFeatureVector(parsed(src), db(dst))
//
//        if (fv.getFeatureValue("PagesRawTextMatchFactor$") < 0.66) {
//          println(parsed(src).toDebugString)
//          println(db(dst).toDebugString)
//          println(fv.dump())
//        }
//      }
//    }
//  }

  def evalResults() {
    val resultsPath = "C:\\Users\\matfed\\Desktop\\pic-eval\\results-heur-model.txt"
    val results =
      using(Source.fromFile(resultsPath)) {
        source => source.getLines().toList
      }
        .map {
        line =>
          val splits = line.split(raw"\s")
          val src = splits(0)
          val dst = splits(1).split(":")(1).substring(4)
          (src, dst)
      }

    val resultSet = results.toSet
    val goldSet = gold.filterNot(_._2.isEmpty).toSet

    println("my real isec")
    println((resultSet.size, goldSet.size, (resultSet & goldSet).size))
    println(((resultSet & goldSet).size.toDouble/resultSet.size))
    println(((resultSet & goldSet).size.toDouble/goldSet.size))

    val goldMap = goldSet.toMap
    for ((src, dst) <- (goldSet diff resultSet)) {
      //println(src + " - was: " + dst + ", should be: " + goldMap.getOrElse(src, ""))
      println("haven't found " + src + " to " + dst)
      if (!parsed.contains(src)) {
        println("no entry for " + src)
      } else if (!db.contains(dst)) {
        println("no entry for " + dst)
      } else {
        println(parsed(src).toDebugString)
        println(db(dst).toDebugString)
        println(SimilarityMeasurer.advancedFvBuilder.calculateFeatureVectorValues((parsed(src), db(dst))).mkString(" "))
      }
    }
  }

  def generateTrainingFile() {
    val (trainingGold, testingGold) = gold.filter(_._2 != "").filter(x => parsed.contains(x._1)).filter(x => db.contains(x._2)).zipWithIndex.partition(_._2 % 2 == 0)
    val measurer = new SimilarityMeasurer
    val outFile = new File("C:\\Users\\matfed\\Desktop\\picSvmTraining.txt")

    using(new FileWriter(outFile)) {
      writer =>
        for ((src1, dst1) <- trainingGold.unzip._1) {
          for ((src2, dst2) <- trainingGold.unzip._1) {
            val fv = measurer.featureVectorBuilder.calculateFeatureVectorValues((parsed(src1), db(dst2)))
            writer.write(featureVectorValuesToLibSvmLine(fv, if (dst1 == dst2) 1 else 0) + "\n")
          }
        }
    }
  }

  def main(args: Array[String]) {
    evalResults()
  }
}
