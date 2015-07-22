
package pl.edu.icm.coansys.deduplication.organization

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
import pl.edu.icm.coansys.models.OrganizationProtos.OrganizationWrapper
import com.google.common.hash.HashCode
import org.apache.spark.graphx._



object DoDeduplication {

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
  
  
  def dedupOrganizations(organizations:RDD[OrganizationWrapper]):RDD[OrganizationWrapper] = {
     val toEdges = organizations.flatMap[(String, OrganizationWrapper)] {
       record => {
        ((record.getOrganizationMetadata.getOriginalNameList.map(
          (name: String) => (simplify(name), record)
        )) ++ (record.getOrganizationMetadata.getEnglishNameList.map(
            (name: String) => (simplify(name), record)
          )));
      }
    }
     val edgesPrep = toEdges.groupByKey().flatMap {
      case (hash: String, records: Iterable[OrganizationWrapper]) => {
        val recWithMinKey = records.min(Ordering.by(((_: OrganizationWrapper).getRowId())));
        records.map { rec => (rec, recWithMinKey) }
      }
    }
    val vertexes=organizations.map{
         record:OrganizationWrapper => (longHash(record.getRowId()),record);
    }
     val edges=edgesPrep.map{
      case (rec1:OrganizationWrapper, rec2:OrganizationWrapper) => {
        Edge(longHash(rec1.getRowId()),longHash(rec2.getRowId()),getOrganizationName(rec2));
      }
    }
    
    val graph = Graph(vertexes, edges)
    
    
    val components=ConnectedComponents.run(graph);
    
     
    
    val  grouped=graph.vertices.cogroup(components.vertices).flatMap{ 
      case (k:Long, (oi:Iterable[OrganizationWrapper], ki:Iterable[Long])) => 
        {
          oi.flatMap{ ow:OrganizationWrapper => {
                ki.map{
                  a:Long=>
                   (a,ow)
                }
            }
          }
            
          
        }
    }
    
    val ret=grouped.groupByKey.map{
      case (k:Long,it:Iterable[OrganizationWrapper]) =>
      {
        it.reduce(
          (ow1:OrganizationWrapper, ow2:OrganizationWrapper) => {
//              println(getOrganizationName(ow1));
//              println(ow1.getRowId);
//              println(getOrganizationName(ow2));
//              println(ow2.getRowId);
              val builder=ow1.toBuilder
              val onameslist=ow2.getOrganizationMetadata.getOriginalNameList;
              onameslist.removeAll(ow1.getOrganizationMetadata.getOriginalNameList);
              builder.getOrganizationMetadataBuilder.addAllOriginalName(onameslist);
              
             
              builder.build
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
    val conf = new SparkConf().setAppName("Organization deduplication")
    val sc = new SparkContext(conf)
    val logData = sc.sequenceFile[String, BytesWritable](organizationFile);
    val organizations= logData.map{
      case (key, bw)=> {
          OrganizationWrapper.parseFrom(bw.copyBytes);
        }
    }
    
    dedupOrganizations(organizations).map((o:OrganizationWrapper)=> {(o.getRowId,o.toByteArray)})
    .saveAsSequenceFile(args(1));
     
    
    
 }

}
