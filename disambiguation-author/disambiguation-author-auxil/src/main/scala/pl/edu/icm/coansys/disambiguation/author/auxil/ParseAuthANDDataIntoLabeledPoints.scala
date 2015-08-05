package pl.edu.icm.coansys.disambiguation.author.auxil

import org.apache.spark.SparkContext._
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.{SparkConf, SparkContext}
import pl.edu.icm.coansys.disambiguation.author.features.disambiguators.CosineSimilarity

import scala.collection.JavaConversions._

object ParseAuthANDDataIntoLabeledPoints {

  case class UUID(id:String)
  case class Feature(name: String, vals:Array[String])
  case class UserInfo(docId:UUID, authId:UUID, hashAuth:String, features:List[Feature], sname:String) 
  
  /**
   * @param args the command line arguments
   */
  def main(args: Array[String]) {

    val conf = new SparkConf()
      .setAppName("Parsing data for AND model")
//      .setMaster("local")
    val sc = new SparkContext(conf)
    val test = false

    var linesRDD = sc.parallelize(List(
      "6a80a1a7-3071-3d7e-98e7-f1a07348f17a\t64da331f-1e9d-3dfe-ab04-cb1c71d67218\t92905994\t[EX_AUTH_FNAME_FST_LETTER#{(98)},EX_DOC_AUTHS_FNAME_FST_LETTER#{(115),(98)},EX_PERSON_ID_ORCID#{(0000-0003-4285-6256)},EX_DOC_AUTHS_SNAMES#{(-535238152),(92905994)},EX_TITLE#{(-510284725)},EX_TITLE_SPLIT#{(98291),(1745201474),(3151786),(1147603242),(1943748620),(563698677),(112905370),(3365),(103910395),(-982210431),(96727),(3095218),(-455789922)},EX_AUTH_FNAMES_FST_LETTER#{(98)}]\tallen",
      "6a80a1a7-3071-3d7e-98e7-f1a07348f17a\t64da331f-1e9d-3dfe-ab04-cb1c71d67218\t92905994\t[EX_AUTH_FNAME_FST_LETTER#{(98)},EX_DOC_AUTHS_FNAME_FST_LETTER#{(115),(98)},EX_PERSON_ID_ORCID#{(0000-0003-4285-6256)},EX_DOC_AUTHS_SNAMES#{(-535238152),(92905994)},EX_TITLE#{(-510284725)},EX_TITLE_SPLIT#{(98291),(1745201474),(3151786),(1147603242),(1943748620),(563698677),(112905370),(3365),(103910395),(-982210431),(96727),(3095218),(-455789922)},EX_AUTH_FNAMES_FST_LETTER#{(98)}]\tallen"
    ))
    var decField = "EX_PERSON_ID_ORCID"
    if(!test){
      val inputFilePath = args(0)
      val outputFilePath = args(1)
      decField = args(2)
      linesRDD = sc.textFile(inputFilePath)
    }


    val usersRDD = linesRDD.map { l =>
      val ui = parseInputLine(l)
      (ui.sname,ui)
    }


    val featureNamesRDD = usersRDD.flatMap(kv => kv._2.features.map(t => (t.name))).distinct().filter { x => x!=decField }
    val featureNameTofIdxMap = featureNamesRDD.zipWithIndex().map{ kv =>
      (kv._1,kv._2+1)
    }.collectAsMap()
    val bcFeatureNameToIdxMap= sc.broadcast(featureNameTofIdxMap)

    val userGroupsByKeyRDD = usersRDD.groupByKey()
    val pairTrainingPointsRDD = userGroupsByKeyRDD.flatMap{in =>
      val listOfAuthors = in._2.toList
      val sizeOfList = listOfAuthors.size
      val setOfIndices: Set[Int] = 0 until sizeOfList toSet
      val legalPairsOfIndices = (for (x<-setOfIndices; y<-setOfIndices) yield (x,y)) filter (t => t._1 < t._2)
      val pairTrainingItems = legalPairsOfIndices map { t=>
        val aFeat = listOfAuthors(t._1).features.map(f => (f.name, f.vals))
        val bFeat = listOfAuthors(t._2).features.map(f => (f.name, f.vals))
        val pairTrainingItem = ((aFeat ++ bFeat).groupBy(_._1).toList)
          .filter(kv => kv._2.toList.size==2)
          .map{ kv =>
            val name = kv._1
            scala.collection.mutable.Seq(kv._2(0)._2.toSeq)
            val fA : java.util.List[java.lang.Object] = scala.collection.mutable.Seq(kv._2(0)._2)
            val fB : java.util.List[java.lang.Object] = scala.collection.mutable.Seq(kv._2(1)._2)
            val idx = bcFeatureNameToIdxMap.value.getOrDefault(name,0)
            val v = new CosineSimilarity().calculateAffinity(fA, fB)
            (idx.toInt,v)
          }.sortBy(kv => kv._1)

        new LabeledPoint(
          pairTrainingItem(0)._2,
          Vectors.sparse(
            bcFeatureNameToIdxMap.value.size+1,
            pairTrainingItem.slice(1,pairTrainingItem.length)
          )
        )
      }
      pairTrainingItems
    }
    //pairTrainingPointsRDD.collect().foreach(println(_))
    pairTrainingPointsRDD.saveAsTextFile(outputFilePath)
  }
  
  def parseInputLine(line:String): UserInfo = {
    val arr = line.split("\t")
    val docId = UUID(arr(0));
    val authId = UUID(arr(1))
    val hashAuth = arr(2)
    val features : List[Feature] = parseManyFeatures(arr(3))
    val sname = arr(4)
    return UserInfo(docId,authId,hashAuth,features,sname)
  }
  def findIndices(strA: String, subStr:String) : List[Int] = {
    val idx = strA.indexOf(subStr)
    if(idx == -1){
      return List()
    }
    val len = subStr.length()
    val shift = len + idx
    val strB = strA.substring(shift);
    return List(idx) ++ findIndices(strB, subStr).map(i => i+shift)
  }

  def parseManyFeatures(featuresStr:String): List[Feature] = {
    val sA = featuresStr
    val sB = sA.substring(1)
    val staInds = findIndices(sB, "EX_")
    val stoInds = staInds.slice(1, staInds.size) ++ List(sB.length())
    val staStoInds : List[(Int,Int)] = staInds.zip(stoInds)
    val feStrs : List[String] = staStoInds.map(se => sB.substring(se._1,se._2-1))
    val feEls : List[Feature] = feStrs.map(s => parseOneFeature(s)) 
    return feEls
  }

  def parseOneFeature(feStr : String): Feature = {
    val genArr = feStr.split("#")
    val feName = genArr(0) //feature name
    val feValsStr = genArr(1).substring(1,genArr(1).length()-1)
    val feValsArr = feValsStr.split(",") 
    val feValsFin = feValsArr.map(s =>  s.substring(1,s.length()-1)) //feature values
    return new Feature(feName, feValsFin)
  }
}
