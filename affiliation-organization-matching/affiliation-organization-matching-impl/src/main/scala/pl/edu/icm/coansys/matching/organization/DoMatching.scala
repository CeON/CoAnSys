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
  
  
  def dedupOrganizations(organizations:RDD[OrganizationWrapper]):RDD[Array[Byte]] = {
     val toEdges = organizations.flatMap[(String, Array[Byte])] {
       record => {
        ((record.getOrganizationMetadata.getOriginalNameList.map(
          (name: String) => (simplify(name), record.toByteArray)
        )) ++ (record.getOrganizationMetadata.getEnglishNameList.map(
            (name: String) => (simplify(name), record.toByteArray)
          )));
      }
    }
    
    println("edges stage 1 ready: "+toEdges.count);   
     val edgesPrep = toEdges.groupByKey.flatMap[(OrganizationWrapper,OrganizationWrapper)] {
      case (hash: String, recordsB: Iterable[Array[Byte]/*OrganizationWrapper*/]) => {
        val records=recordsB.map{case (r:Array[Byte])=>{
            OrganizationWrapper.parseFrom(r)
          }};
         val recWithMinKey = records.min(Ordering.by(((_: OrganizationWrapper).getRowId())));
        records.map { rec => (rec, recWithMinKey) }
      }
    }
    val vertexes=organizations.map{
         record:OrganizationWrapper => (longHash(record.getRowId()),record.toByteArray);
    }
     val edges=edgesPrep.map{
      case (rec1:OrganizationWrapper, rec2:OrganizationWrapper) => {
        Edge(longHash(rec1.getRowId()),longHash(rec2.getRowId()),getOrganizationName(rec2));
      }
    }
    
    val graph = Graph(vertexes, edges)
    
     println("graph ready: "+edges.count);   
    val components=ConnectedComponents.run(graph);
    
    
    val  grouped=graph.vertices.cogroup(components.vertices).flatMap{ 
      case (k:Long, (oi:Iterable[Array[Byte]], ki:Iterable[Long])) => 
        {
          oi.flatMap{ ow:Array[Byte] => {
                ki.map{
                  a:Long=>
                   (a,ow)
                }
            }
          }
            
          
        }
    }
    
    val ret=grouped.groupByKey.map{
      case (k:Long,it:Iterable[Array[Byte]]) =>
      {
        it.reduce(
          (ow1b:Array[Byte], ow2b:Array[Byte]) => {
//              println(getOrganizationName(ow1));
//              println(ow1.getRowId);
//              println(getOrganizationName(ow2));
//              println(ow2.getRowId);
              val ow1=OrganizationWrapper.parseFrom(ow1b)
              val ow2=OrganizationWrapper.parseFrom(ow2b)
             
              val builder=ow1.toBuilder
              val onameslist=ow2.getOrganizationMetadata.getOriginalNameList;
              onameslist.removeAll(ow1.getOrganizationMetadata.getOriginalNameList);
              builder.getOrganizationMetadataBuilder.addAllOriginalName(onameslist);
              
             
              builder.build.toByteArray
          })
        
          
        
      }
    }
    ret
  }
  
  
  
  
  /**
   * @param args the command line arguments
   */
  def main(args: Array[String]): Unit = {
    val organizationFile =args(0) // Should be some file on your system
    val documentsFile =args(1);
    val conf = new SparkConf().setAppName("Organization matching")
    val sc = new SparkContext(conf)
    val orgData = sc.sequenceFile[String, BytesWritable](organizationFile);
    val organizationsNames= orgData.flatMap{
      case (key, bw)=> {
          val org=OrganizationWrapper.parseFrom(bw.copyBytes);
          org.getOrganizationMetadata.getEnglishNameList.map(
            (s:String) => {
              (s.toLowerCase.trim,org.getRowId)
            }
          )++org.getOrganizationMetadata.getOriginalNameList.map(
            (s:String) => {
              (s.toLowerCase.trim,org.getRowId)
            }
          )
        }
    }
    val docData = sc.sequenceFile[String, BytesWritable](documentsFile);
    val matched=organizationsNames.cartesian(docData).filter{
      case ((orgName,orgId),(docId,docContent)) =>{
          val doc=DocumentWrapper.parseFrom(docContent.copyBytes);
          !(doc.getDocumentMetadata.getAffiliationsList.filter((a :Affiliation ) =>{
              a.getText.toLowerCase().contains(orgName)
          }).isEmpty)
      }
    }
    val ready=matched.flatMap[(String,Array[Byte])]{
      case ((orgName,orgId),(docId,docContent)) =>{
          val doc=DocumentWrapper.parseFrom(docContent.copyBytes);
          doc.getDocumentMetadata.getAffiliationsList.filter((a :Affiliation ) =>{
              a.getText.toLowerCase().contains(orgName)
          }).map((a :Affiliation )=>{
            (docId,OrganizationMatchingOut.
                                           newBuilder.setAffiliationId(a.getAffiliationId)
                                                     .setDocId(doc.getRowId)
                                                     .setOrganizationId(orgId)
                                           .build.toByteArray)
          })
      }
    }
   
    ready.s
    
     
    
    
 }

}
