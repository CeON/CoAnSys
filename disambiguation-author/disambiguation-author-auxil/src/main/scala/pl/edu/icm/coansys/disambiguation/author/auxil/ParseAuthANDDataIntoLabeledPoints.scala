package pl.edu.icm.coansys.disambiguation.author.auxil

import org.apache.spark.SparkContext._
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.rdd.RDD
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
      .setMaster("local")
    val sc = new SparkContext(conf)
    val test = true

    var linesRDD = sc.parallelize(List(
      "0041f6a9-6834-3c44-ba03-4cb142918a9c\t9d9cce09-a5c7-3bd9-8497-2c14106523cb\t3641859\t[EX_PERSON_ID_PBN#{(0000-0002-5979-5544)},EX_PERSON_ID_COANSYS#{(0000-0002-5979-5544)},EX_AUTH_FNAME_FST_LETTER#{(122)},EX_AUTH_FNAMES#{(122)},EX_DOC_AUTHS_FNAME_FST_LETTER#{(112),(114),(106),(97),(122)},EX_PERSON_ID_ORCID#{(0000-0002-5979-5544)},EX_DOC_AUTHS_SNAMES#{(-1217173850),(-925564068),(1674970301),(-1338811464),(3641859)},EX_TITLE#{(1557727042)},EX_COAUTH_SNAME#{(-1217173850),(-925564068),(1674970301),(3641859)},EX_AUTH_FNAME#{(3828)},EX_TITLE_SPLIT#{(108401386),(3707),(109264607),(3522631),(283723001),(96727),(-1266509013)},EX_AUTH_FNAMES_FST_LETTER#{(122)}]\twang",
      "0041f6a9-6834-3c44-ba03-4cb142918a9c\t9d9cce09-a5c7-3bd9-8497-2c14106523cb\t3641859\t[EX_PERSON_ID_PBN#{(0000-0002-5979-5544)},EX_PERSON_ID_COANSYS#{(0000-0002-5979-5544)},EX_AUTH_FNAME_FST_LETTER#{(122)},EX_AUTH_FNAMES#{(122)},EX_DOC_AUTHS_FNAME_FST_LETTER#{(112),(114),(106),(97),(122)},EX_PERSON_ID_ORCID#{(0000-0002-5979-5544)},EX_DOC_AUTHS_SNAMES#{(-1217173850),(-925564068),(1674970301),(-1338811464),(3641859)},EX_TITLE#{(1557727042)},EX_COAUTH_SNAME#{(-1217173850),(-925564068),(1674970301),(3641859)},EX_AUTH_FNAME#{(3828)},EX_TITLE_SPLIT#{(108401386),(3707),(109264607),(3522631),(283723001),(96727),(-1266509013)},EX_AUTH_FNAMES_FST_LETTER#{(122)}]\twang"
    ))
    var decField = "EX_PERSON_ID_ORCID"
    var outputFilePath = ""
    if(!test){
      val inputFilePath = args(0)
      outputFilePath = args(1)
      decField = args(2)
      linesRDD = sc.textFile(inputFilePath)
    }


    val usersRDD = linesRDD.map { l =>
      val ui = parseInputLine(l)
      (ui.sname,ui)
    }

    val featureNameToIdxMap : collection.Map[String, Long] = createFeatureMap(linesRDD, decField, "EX_PERSON_*")
    val bcFeatureNameToIdxMap= sc.broadcast(featureNameToIdxMap)

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
            var idx = bcFeatureNameToIdxMap.value.getOrDefault(name,0)
            if(idx==0 && name!=decField){
              idx = -1;
            }
            val v = new CosineSimilarity().calculateAffinity(kv._2(0)._2.toList, kv._2(1)._2.toList)
            (idx.toInt,v)
          }.filter(kv => kv._1 != -1).sortBy(kv => kv._1)

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

    if(test){
      pairTrainingPointsRDD.collect().foreach(println(_))
    }else{
      pairTrainingPointsRDD.saveAsTextFile(outputFilePath)

    }
  }

  def createFeatureMap(linesRDD : RDD[String], decField : String, skippedFeatures : String): collection.Map[String, Long] = {
    val usersRDD = linesRDD.map { l =>
      val ui = parseInputLine(l)
      (ui.sname,ui)
    }
    val featureNamesRDD = usersRDD.flatMap(kv => kv._2.features.map(t => (t.name)))
                                  .distinct()
                                  .filter { x => x!=decField }
                                  .filter { x => x.replaceAll(skippedFeatures,"").length == x.length}
    val featureNameTofIdxMap = featureNamesRDD.zipWithIndex().map{ kv =>
      (kv._1,kv._2+1)
    }.collectAsMap()
    return featureNameTofIdxMap
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
      .filter(a => a.isInstanceOf[Feature])
      .map(a => a.asInstanceOf[Feature])
    return feEls
  }

  def parseOneFeature(feStr : String): Any = {
    try {
      val genArr = feStr.split("#")
      val feName = genArr(0) //feature name
      val feValsStr = genArr(1).substring(1, genArr(1).length() - 1)
      val feValsArr = feValsStr.split(",")
      val feValsFin = feValsArr.map(s => s.substring(1, s.length() - 1)) //feature values
      return new Feature(feName, feValsFin)
    }catch{
      case e: Exception => return None
    }
  }
}
