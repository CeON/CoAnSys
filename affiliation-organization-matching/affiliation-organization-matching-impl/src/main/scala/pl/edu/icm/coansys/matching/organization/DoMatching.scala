package pl.edu.icm.coansys.matching.organization

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import com.google.common.hash.Hashing
import java.nio.charset.Charset
import java.util.Locale
import org.apache.hadoop.io.BytesWritable
import org.apache.spark.SparkConf
import scala.collection.JavaConversions._
import org.apache.spark.graphx.lib.ConnectedComponents
import org.apache.spark.rdd.RDD
import pl.edu.icm.coansys.models.AffiliationMatchedProtos.OrganizationMatchingOut
import pl.edu.icm.coansys.models.DocumentProtos.Affiliation
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper
import pl.edu.icm.coansys.models.OrganizationProtos.OrganizationWrapper
import com.google.common.hash.HashCode
import org.apache.spark.graphx._



object DoMatching {

  def simplify(name: String): String = {
    name.toLowerCase(Locale.ENGLISH).replaceAll("[^a-z0-9]", "");
  }

  def hf = Hashing.md5
  
  def longHash(str:String) :Long = {
    val hc=hf.newHasher.putString(str, Charset.forName("UTF-8")).hash();
    hc.asLong();
  }
  
  def getOrganizationName(org:OrganizationWrapper) : String = {
    if (org.getOrganizationMetadata.getOriginalNameCount>0) {
       org.getOrganizationMetadata.getOriginalName(0)
    } else if (org.getOrganizationMetadata.getEnglishNameCount>0) {
       org.getOrganizationMetadata.getEnglishName(0)
    } else {
      ""
    }
  }
  
  def trimOrganizationNamesForHash(nam:String) :String = {
    val common=Array("university","institute","of ", " of");
    var nn=nam.toLowerCase;
    for (s <- common) {
     nn=nn.replaceAll(s+" ", " ").replaceAll(" "+s, " ");
    }
    nn
  }
  
  
  def doMatching(orgData:RDD[(String,Array[Byte])],docData :RDD[(String,Array[Byte])]):RDD[(String,Array[Byte])] ={
 val organizationsNames= orgData.flatMap{
      case (key, bw)=> {
          val org=OrganizationWrapper.parseFrom(bw);
          org.getOrganizationMetadata.getEnglishNameList.map(
            (s:String) => {
              (simplify(s),org.getRowId)
            }
          )++org.getOrganizationMetadata.getOriginalNameList.map(
            (s:String) => {
              (simplify(s),org.getRowId)
            }
          )
        }
    }
  
    val hashSize=3
    
    val organizationsHash= organizationsNames.map{
       case (orgName,orgId) => {
         val sn=trimOrganizationNamesForHash(orgName).replaceAll(" ", "");
         val s=if (sn.size<=hashSize) sn else sn.substring(0, hashSize);
         (s,(orgName,orgId))
       }
    } 
  
    val docAffHash=docData.flatMap{
      case (docId,docContent) => {
         val doc=DocumentWrapper.parseFrom(docContent);
         doc.getDocumentMetadata.getAffiliationsList.flatMap((a:Affiliation) => {
              val nn=trimOrganizationNamesForHash(a.getText)
              
              if (nn.size<=hashSize) {
                (1 to nn.length).map(i=>{(nn.substring(0, i),(docId,docContent))})
              } else {
              (0 to nn.length-hashSize).flatMap(i => {
                val t=nn.substring(i, i+hashSize);
                (1 to t.length).map(j=>{(t.substring(0, j),(docId,docContent))})
              }) ++ (1 to hashSize-1).map (i=> {
                  (nn.substring(nn.length-i),(docId,docContent))
              })
              }
         }
         )
      }
    }
      println("organizations hash count: "+organizationsHash.count);
      println("documents hash count: "+docAffHash.count);
    println("documents keys:")
    docAffHash.foreach{
      case (key:String, t )=>{
          println(key)
      }
    }
   val matched=docAffHash.join(organizationsHash).filter{
     
  case (key:String,((docId:String,docContent:Array[Byte]),(orgName:String ,orgId:String))) =>{
          val doc=DocumentWrapper.parseFrom(docContent);
          !(doc.getDocumentMetadata.getAffiliationsList.filter((a :Affiliation ) =>{
              simplify(a.getText).contains(orgName)
          }).isEmpty)
         
      }
   }
   
   /* val matched=organizationsNames.cartesian(docData).filter{
      case ((orgName,orgId),(docId,docContent)) =>{
          val doc=DocumentWrapper.parseFrom(docContent);
          !(doc.getDocumentMetadata.getAffiliationsList.filter((a :Affiliation ) =>{
              simplify(a.getText).contains(orgName)
          }).isEmpty)
      }
    }*/
     println("matched count: "+matched.count);
    val ready=matched.flatMap[(String,Array[Byte])]{
      case (key,((docId,docContent),(orgName,orgId))) =>{
          val doc=DocumentWrapper.parseFrom(docContent);
          doc.getDocumentMetadata.getAffiliationsList.filter((a :Affiliation ) =>{
              simplify(a.getText).contains(orgName)
          }).map((a :Affiliation )=>{
            (docId,OrganizationMatchingOut.
                                           newBuilder.setAffiliationId(a.getAffiliationId)
                                                     .setDocId(doc.getRowId)
                                                     .setOrganizationId(orgId)
                                           .build.toByteArray)
          })
      }
    };
    ready
    
    
  }
  
  
  /**
   * @param args the command line arguments
   */
  def main(args: Array[String]): Unit = {
    val organizationFile =args(0) // Should be some file on your system
    val documentsFile =args(1);
    val conf = new SparkConf().setAppName("Organization matching")
    val sc = new SparkContext(conf)
    val orgData = sc.sequenceFile[String, BytesWritable](organizationFile).map{case (a,b) =>{
        (a,b.copyBytes)
      }
    };
   
    val docData = sc.sequenceFile[String, BytesWritable](documentsFile).map{case (a,b) =>{
        (a,b.copyBytes)
      }
    };
    
    
    val results=doMatching(orgData, docData);
    println("results count: "+results.count);
    results.saveAsSequenceFile(args(2));
    
     
    
    
 }

}
