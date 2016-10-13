
package pl.edu.icm.coansys.disambiguation.author.scala
import java.io.File
import org.apache.hadoop.io.BytesWritable
import org.apache.pig.builtin.TOBAG

import org.apache.pig.data.Tuple
import org.apache.pig.data.TupleFactory
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import pl.edu.icm.coansys.disambiguation.author.pig.GenUUID
import pl.edu.icm.coansys.models.DisambiguationAuthorProtos.ContributorDescription
import pl.edu.icm.coansys.models.DisambiguationAuthorProtos.DisambiguationAuthorOut
import scala.collection.JavaConverters._

object Serialize {

  case class Config(

    and_outputContribs: String = "workflows/pl.edu.icm.coansys-disambiguation-author-workflow/results/outputContribs/*/*",
    and_cid_dockey: String = "cid_dockey",
    and_outputPB: String = "workflows/pl.edu.icm.coansys-disambiguation-author-workflow/results/outputPB",
    and_output_unserialized: String = "workflows/pl.edu.icm.coansys-disambiguation-author-workflow/results/unserializad"

  )

  val parser = new scopt.OptionParser[Config]("disambiguationApr") {
    head("disambiguationApr", "1.x")

    opt[String]("and-outputContribs").action((x, c) => c.copy(and_outputContribs = x)).
      text("and_outputContribs")

    opt[String]("and-cid-dockey").action((x, c) =>
      c.copy(and_cid_dockey = x)).text("and_cid_dockey")

    opt[String]("and-outputPB").action((x, c) =>
      c.copy(and_outputPB = x)).text("and_outputPB")

    opt[String]("and-output-unserialized").action((x, c) =>
      c.copy(and_output_unserialized = x)).text("and_output_unserialized")

    help("help").text("prints this usage text")

  }

  /**
   * @param args the command line arguments
   */
  def main(args: Array[String]): Unit = {

    var and_outputContribs = "workflows/pl.edu.icm.coansys-disambiguation-author-workflow/results/outputContribs/apr-no-sim"
    // %DEFAULT and_failedContribs disambiguation/failedContribs$and_time
    // %DEFAULT and_exhaustive_limit 6627
    var and_cid_dockey: String = "cid_dockey"
    var and_outputPB: String = "workflows/pl.edu.icm.coansys-disambiguation-author-workflow/results/outputPB"
    var and_output_unserialized: String = "workflows/pl.edu.icm.coansys-disambiguation-author-workflow/results/unserializad"
    // parser.parse returns Option[C]
    parser.parse(args, Config()) match {
      case Some(config) =>
        // do stuff
        and_cid_dockey = config.and_cid_dockey
        and_outputPB = config.and_outputPB
        and_output_unserialized = config.and_output_unserialized
        and_outputContribs = config.and_outputContribs
      case None =>
        // arguments are bad, error message will have been displayed
        return
    }
    val conf = new SparkConf().setAppName("DisambiguationApr")
    val sc = new SparkContext(conf)

    //CidDkey = LOAD '$and_cid_dockey' as (cId:chararray, docKey:chararray, ex_person_id_unused, sname_unused);

    val cidDocKey = sc.textFile(and_cid_dockey).map(x => {
      val line = x.split("\t")
      (line(0), (line(1), line(2), line(3)))
    })
    //CidAuuid = LOAD '$and_outputContribs/*' as (cId:chararray, uuid:chararray);
    val contribs = sc.textFile(and_outputContribs).map(x => {
      val line = x.split("\t")
      (line(0), line(1))
    })


    //A = JOIN CidDkey BY cId, CidAuuid BY cId;
    //

    val a = cidDocKey.join(contribs)

    //B = FOREACH A generate CidDkey::cId as cId, uuid as uuid, docKey as docKey, ex_person_id_unused, sname_unused;

    /* val b=a.map{
   case (cid,((docKey,ex_Peson_id, sname),uuid)) => {
   //    (dockeycid,)
   }
 }*/

    //-- store unserialized results for optional accuracy checking
    //-- (cId, coansysId, dockKey, externalId, sname)
    //-- MAY BE COMMENTED IN PRODUCTION WHEN CHECKING NODE IN WORKFLOW IS TURNED OFF
    //STORE B into '$and_output_unserialized'; 
    a.map {
      case (cid, ((docKey, ex_Peson_id, sname), uuid)) => {
        cid + "\t" + uuid + "\t" + docKey + "\t" + ex_Peson_id + "\t" + sname
      }
    }.saveAsTextFile(and_output_unserialized)

    //-- prepare and store serialized, final AND output
    //C = group B by docKey;
    //-- D = FOREACH C generate group as docKey, B as trio;
    //DEFINE serialize pl.edu.icm.coansys.disambiguation.author.pig.serialization.SERIALIZE_RESULTS();
    //E = FOREACH C generate FLATTEN(serialize(*));
    //STORE E into '$and_outputPB' USING pl.edu.icm.coansys.commons.pig.udf.RichSequenceFileLoader('org.apache.hadoop.io.Text', 'org.apache.hadoop.io.BytesWritable');
    a.map {
      case (cid, ((docKey, ex_Peson_id, sname), uuid)) => {
        (docKey, (cid, uuid))
      }
    }.groupByKey().map {
      case (docKey, iter) => {
        getProtocolBuffersByteAFrom(docKey, iter)
      }
    }.saveAsSequenceFile(and_outputPB)

  }

  def getProtocolBuffersByteAFrom(docKey: String, iter: Iterable[(String, String)]): (String, Array[Byte]) = {

    val outb = DisambiguationAuthorOut.newBuilder();
    outb.setDocId(docKey);

    iter.foreach {
      case (cid, uuid) => {
        outb.addContributorDescription(ContributorDescription
          .newBuilder().setContribId(cid).setClusterId(uuid))
      }
    }

    (docKey, outb.build().toByteArray())
  }

}
