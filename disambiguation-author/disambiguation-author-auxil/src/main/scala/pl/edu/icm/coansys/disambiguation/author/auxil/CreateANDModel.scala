package pl.edu.icm.coansys.disambiguation.author.auxil

import org.apache.spark.SparkContext._
import org.apache.spark.mllib.classification.{SVMModel, SVMWithSGD}
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object CreateANDModel {

  case class UUID(id:String)
  case class Feature(name: String, vals:Array[String])
  case class UserInfo(docId:UUID, authId:UUID, hashAuth:String, features:List[Feature], sname:String)

  /**
   * @param args the command line arguments
   */
  def main(args: Array[String]) {


    var test = ""
    if(args.length==0){
      test = "TEST"
    }else{
      test = args(0)
    }

    println("execute this class with the following param:" +
      "\n" +
      "\n(empty arg list)\ttest run" +
      "\nTEST [<any other args>*]\ttest run" +
      "\n<raw data path> <labeled points path> <name of decision Field>"
    )

    /*
     * CREATE SC
     */
    var conf = new SparkConf()
    if(test == "TEST") {
      conf.setAppName("Parsing data for AND model")
          .setMaster("local")
    }
    else{
      conf.setAppName("Parsing data for AND model")
    }
    val sc = new SparkContext(conf)
    /*
     * READ DATA
     */
    val PATH = "disambiguation-author/disambiguation-author-auxil/src/main/resources/"
    var prevLinesRDD = sc.textFile(PATH + "/primer.tsv")
    var linesRDD = sc.textFile(PATH + "/svmInput.array.file")
    var decField = "EX_PERSON_ID_ORCID"
    var outputFilePath = ""

    if(!test.startsWith("TEST")){
      val inputRawData = args(0)
      val inputSvmPoints = args(1)
      decField = args(2)
      prevLinesRDD = sc.textFile(inputRawData)
      linesRDD = sc.textFile(inputSvmPoints)
    }
    val featureNameToIdxMap : collection.Map[String, Long] = createFeatureMap(prevLinesRDD, decField)
    val points = linesRDD.map(LabeledPoint.parse(_))

    /*
     * CREATE MODEL
     */
    val dataSplits = points.randomSplit(Array(0.8,0.2), seed=11L)
    val model : SVMModel = SVMWithSGD.train(dataSplits(0), 20)

    val predsAndLabels = dataSplits(1).map { p =>
      (model.predict(p.features),p.label)
    }
    val acc = 1.0 * predsAndLabels.filter(x=> x._1==x._2).count/ dataSplits(1).count

    /*
     * RETURN RESULTS
     */

    println("Acc :" + acc)
    println("Model: ")
    val weights = model.weights
    for(kv <- featureNameToIdxMap.iterator){
      print(kv._1+"\t")
      println(weights(kv._2.toInt))
    }
    sc.stop()
  }

  def createFeatureMap(linesRDD : RDD[String], decField : String): collection.Map[String, Long] = {
    val usersRDD = linesRDD.map { l =>
      val ui = parseInputLine(l)
      (ui.sname,ui)
    }
    val featureNamesRDD = usersRDD.flatMap(kv => kv._2.features.map(t => (t.name))).distinct().filter { x => x!=decField }
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
