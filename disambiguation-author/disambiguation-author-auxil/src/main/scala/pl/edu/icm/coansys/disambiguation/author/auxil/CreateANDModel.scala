package pl.edu.icm.coansys.disambiguation.author.auxil

import org.apache.spark.mllib.classification.{SVMModel, SVMWithSGD}
import org.apache.spark.mllib.regression.LabeledPoint
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
    val featureNameToIdxMap : collection.Map[String, Long] = ParseAuthANDDataIntoLabeledPoints.createFeatureMap(prevLinesRDD, decField)
    val points = linesRDD.map(LabeledPoint.parse(_))

    /*
     * CREATE MODEL
     */
    points.cache()
    val dataSplits = points.randomSplit(Array(0.8,0.2), seed=11L)
    val trainSet = dataSplits(0)
    val testSet = dataSplits(1)
    trainSet.cache()
    testSet.cache()
    val model : SVMModel = SVMWithSGD.train(trainSet, 20)

    val predsAndLabels = testSet.map { p =>
      (model.predict(p.features),p.label)
    }
    val acc = 1.0 * predsAndLabels.filter(x=> x._1==x._2).count/ testSet.count

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
    println("Intercept: " + model.intercept)
    sc.stop()
  }

}
