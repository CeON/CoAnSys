
package pl.edu.icm.coansys.deduplication.organization

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import java.util.Locale
import org.apache.hadoop.io.BytesWritable
import org.apache.spark.SparkConf
import scala.collection.JavaConversions._
import pl.edu.icm.coansys.models.OrganizationProtos.OrganizationWrapper
import com.google.common.hash.HashCode
import org.apache.spark.graphx._



object DoDeduplication {

  def hash(name: String): String = {
    name.toLowerCase(Locale.ENGLISH).replaceAll("[^a-z0-9]", "");
  }

  /**
   * @param args the command line arguments
   */
  def main(args: Array[String]): Unit = {
    val organizationFile = "/mnt/win/space/comac/ORGANIZATION/" // Should be some file on your system
    val conf = new SparkConf().setAppName("Simple Application").setMaster("local");
    val sc = new SparkContext(conf)
    val logData = sc.sequenceFile[String, BytesWritable](organizationFile);
    val vertexesPrep= logData.map{
      case (key, bw)=> {
          OrganizationWrapper.parseFrom(bw.copyBytes);
        }
    }
    val toEdges = vertexesPrep.flatMap[(String, OrganizationWrapper)] {
       record => {
        ((record.getOrganizationMetadata.getOriginalNameList.map(
          (name: String) => (hash(name), record)
        )) ++ (record.getOrganizationMetadata.getEnglishNameList.map(
            (name: String) => (hash(name), record)
          )));
      }
    }

    val edgesPrep = toEdges.groupByKey().flatMap {
      case (hash: String, records: Iterable[OrganizationWrapper]) => {
        val recWithMinKey = records.min(Ordering.by(((_: OrganizationWrapper).getRowId())));
        records.map { rec => (rec, recWithMinKey) }
      }
    }

    val vertexes=vertexesPrep.map{
         record:OrganizationWrapper => (HashCode.fromString(record.getRowId()).asLong(),record);
    }
    
    val edges=edgesPrep.map{
      case (rec1:OrganizationWrapper, rec2:OrganizationWrapper) => {
        Edge(HashCode.fromString(rec1.getRowId()).asLong(),HashCode.fromString(rec2.getRowId()).asLong())
      }
    }
    val graph = Graph(vertexes, edges)
    
    
    val numAs = logData.filter(line => line.contains("a")).count()
    val numBs = logData.filter(line => line.contains("b")).count()
    println("Lines with a: %s, Lines with b: %s".format(numAs, numBs))
  }

}
