package pl.edu.icm.coansys.disambiguation.author.auxil

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import com.google.common.hash.Hashing
import java.nio.charset.Charset
import java.util.Locale
import org.apache.hadoop.io.BytesWritable
import org.apache.spark.SparkConf
import scala.collection.JavaConversions._
import org.apache.spark.rdd.RDD
import pl.edu.icm.coansys.models.OrganizationProtos.OrganizationWrapper
import com.google.common.hash.HashCode
import pl.edu.icm.coansys.disambiguation.author.features.disambiguators.Intersection
import pl.edu.icm.coansys.disambiguation.author.features.disambiguators.CosineSimilarity

object ParseInputANDTrainingData {

  case class UUID(id:String)
  case class Feature(name: String, vals:Array[Long])
  case class UserInfo(docId:UUID, authId:UUID, hashAuth:String, features:List[Feature], sname:String) 
  
  /**
   * @param args the command line arguments
   */
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("Parsing data for AND model")
    val sc = new SparkContext(conf)
    val inputFilePath = args(0)
    val outputFilePath = args(1)
    val linesRDD = sc.textFile(inputFilePath)
    val usersRDD = linesRDD.map { l => 
      val ui = parseInputLine(l)
      (ui.sname,ui)
    }
    
    val groupsRDD = usersRDD.groupByKey()
    val pairs = groupRDD.map{in =>
      val sname = in._1
      val iter =  in._2
      val list = iter.toList
      val size = list.size
      val indsToProcess = (0 to (size-1)) cross (0 to (size-1)) filter (t => t._1 > t._2)
      val pairComp = indsToProcess map { t=>
        val csim = new CosineSimilarity
        val aUser : UserInfo = list(t._1)
        val bUser : UserInfo = list(t._2)
        val aFeat = aUser.features.map(f => (f.name, f.vals))
        val bFeat = bUser.features.map(f => (f.name, f.vals))
        val joined = aFeat.join(bFeat)
        val fvals = joined.map{(key,aF,bF) =>
          (name,csim(aF,bF))
        }.sortByKey()
        (aUser.docId,aUser.authId,bUser.docId,bUser.authId, fvals)
      }
    }
    pairs.saveAsTextFile(outputFilePath)
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
    return findIndices(strB, subStr).map(i => i+shift) ++ List(idx)
  }
  
  
  def parseManyFeatures(featuresStr:String): List[Feature] = {
    val sA = featuresStr
    val sB = sA.substring(1,sA.length()-1)
    val staInds = findIndices(sB, "EX_")
    val stoInds = staInds.slice(1, staInds.size) ++ List(sB.length())
    val staStoInds : List[(Int,Int)] = staInds.zip(stoInds)
    val feStrs : List[String] = staStoInds.map(se => sB.substring(se._1,se._2))
    val feEls : List[Feature] = feStrs.map(s => parseOneFeature(s)) 
    return feEls
  }

  def parseOneFeature(feStr : String): Feature = {
    val genArr = feStr.split("#")
    val feName = genArr(0) //feature name
    val feValsStr = genArr(1).substring(1,genArr(1).length()-1)
    val feValsArr = feValsStr.split(",") 
    val feValsFin = feValsArr.map{s => 
      val sstr = s.substring(0,s.length()-1)
      sstr.toLong
    } //feature values
      
    return new Feature(feName, feValsFin)
  }
}
