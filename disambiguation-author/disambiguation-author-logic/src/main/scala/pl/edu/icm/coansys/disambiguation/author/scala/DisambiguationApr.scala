/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.icm.coansys.disambiguation.author.scala
import java.io.File
import org.apache.hadoop.io.BytesWritable
import org.apache.pig.builtin.TOBAG
import org.apache.pig.data.DefaultDataBag
import org.apache.pig.data.Tuple
import org.apache.pig.data.TupleFactory
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import pl.edu.icm.coansys.disambiguation.author.pig.GenUUID
import scala.collection.JavaConverters._

object DisambiguationApr {

  case class Config(
    and_inputDocsData: String = "workflows/pl.edu.icm.coansys-disambiguation-author-workflow/results/splitted/apr-no-sim",
    out: File = new File("."),
    and_threshold: String = "-0.8",
    and_feature_info: String = "IntersectionPerMaxval#EX_DOC_AUTHS_SNAMES#1.0#1",
    and_aproximate_remember_sim: String = "false",
    and_use_extractor_id_instead_name: String = "true",
    and_statistics: String = "false",
    and_exhaustive_limit: Int = 6627,
    libName: String = "", maxCount: Int = -1, verbose: Boolean = false, debug: Boolean = false,
    mode: String = "", files: Seq[File] = Seq(), keepalive: Boolean = false,
    jars: Seq[File] = Seq(), kwargs: Map[String, String] = Map()
  )

  val parser = new scopt.OptionParser[Config]("disambiguationApr") {
    head("disambiguationApr", "1.x")

    opt[String]('i', "input").action((x, c) =>
      c.copy(and_inputDocsData = x)).text("and_inputDocsData")

    opt[String]('t', "and-threshold").action((x, c) =>
      c.copy(and_threshold = x)).text("and_threshold")
    opt[String]('f', "and-feature-info").action((x, c) =>
      c.copy(and_feature_info = x)).text("and_feature_info")
    opt[String]('a', "and-aproximate-remember-sim").action((x, c) =>
      c.copy(and_aproximate_remember_sim = x)).text("and_aproximate_remember_sim")
    opt[String]('e', "and-use-extractor-id-instead-name").action((x, c) =>
      c.copy(and_use_extractor_id_instead_name = x)).text("and_use_extractor_id_instead_name")
    opt[String]('s', "and_statistics").action((x, c) =>
      c.copy(and_statistics = x)).text("and_statistics")
    opt[String]('l', "and-exhaustive-limit").action((x, c) =>
      c.copy(and_statistics = x)).text("and_exhaustive_limit")

    opt[File]('o', "out").required().valueName("<file>").
      action((x, c) => c.copy(out = x)).
      text("out is a required file property")

    opt[(String, Int)]("max").action({
      case ((k, v), c) => c.copy(libName = k, maxCount = v)
    }).
      validate(x =>
        if (x._2 > 0) success
        else failure("Value <max> must be >0")).
      keyValueName("<libname>", "<max>").
      text("maximum count for <libname>")

    opt[Seq[File]]('j', "jars").valueName("<jar1>,<jar2>...").action((x, c) =>
      c.copy(jars = x)).text("jars to include")

    opt[Map[String, String]]("kwargs").valueName("k1=v1,k2=v2...").action((x, c) =>
      c.copy(kwargs = x)).text("other arguments")

    opt[Unit]("verbose").action((_, c) =>
      c.copy(verbose = true)).text("verbose is a flag")

    opt[Unit]("debug").hidden().action((_, c) =>
      c.copy(debug = true)).text("this option is hidden in the usage text")

    help("help").text("prints this usage text")

    arg[File]("<file>...").unbounded().optional().action((x, c) =>
      c.copy(files = c.files :+ x)).text("optional unbounded args")

    note("some notes.".newline)

    cmd("update").action((_, c) => c.copy(mode = "update")).
      text("update is a command.").
      children(
        opt[Unit]("not-keepalive").abbr("nk").action((_, c) =>
          c.copy(keepalive = false)).text("disable keepalive"),
        opt[Boolean]("xyz").action((x, c) =>
          c.copy(xyz = x)).text("xyz is a boolean property"),
        opt[Unit]("debug-update").hidden().action((_, c) =>
          c.copy(debug = true)).text("this option is hidden in the usage text"),
        checkConfig(c =>
          if (c.keepalive && c.xyz) failure("xyz cannot keep alive")
          else success)
      )
  }

  /**
   * @param args the command line arguments
   */
  def main(args: Array[String]): Unit = {
    // parser.parse returns Option[C]
    parser.parse(args, Config()) match {
      case Some(config) =>
      // do stuff

      case None =>
      // arguments are bad, error message will have been displayed
    }

    // %DEFAULT and_inputDocsData workflows/pl.edu.icm.coansys-disambiguation-author-workflow/results/splitted/apr-no-sim
    def and_inputDocsData = "workflows/pl.edu.icm.coansys-disambiguation-author-workflow/results/splitted/apr-no-sim"
    // %DEFAULT and_threshold '-0.8'
    def and_threshold = "-0.8"
    // %DEFAULT and_feature_info 'IntersectionPerMaxval#EX_DOC_AUTHS_SNAMES#1.0#1'
    def and_feature_info = "IntersectionPerMaxval#EX_DOC_AUTHS_SNAMES#1.0#1"

    // %DEFAULT and_aproximate_remember_sim 'false'
    def and_aproximate_remember_sim = "false"
    // %DEFAULT and_use_extractor_id_instead_name 'true'

    def and_use_extractor_id_instead_name = "true"
    // %DEFAULT and_statistics 'false'

    def and_statistics = "false"
    // %DEFAULT and_time ''
    // %DEFAULT and_outputContribs disambiguation/outputContribs$and_time
    // %DEFAULT and_failedContribs disambiguation/failedContribs$and_time
    // %DEFAULT and_exhaustive_limit 6627
    def and_exhaustive_limit = 6627
    // DEFINE exhaustiveAND pl.edu.icm.coansys.disambiguation.author.pig.ExhaustiveAND('$and_threshold','$and_feature_info','$and_use_extractor_id_instead_name','$and_statistics');
    //DEFINE aproximateAND pl.edu.icm.coansys.disambiguation.author.pig.AproximateAND_BFS('$and_threshold', '$and_feature_info','$and_aproximate_remember_sim','$and_use_extractor_id_instead_name','$and_statistics');
    //DEFINE GenUUID pl.edu.icm.coansys.disambiguation.author.pig.GenUUID();
    //
    //-- -----------------------------------------------------
    //-- -----------------------------------------------------
    //-- set section
    //-- -----------------------------------------------------
    //-- -----------------------------------------------------
    //%DEFAULT and_sample 1.0
    //%DEFAULT and_parallel_param 16
    //%DEFAULT pig_tmpfilecompression_param true
    //%DEFAULT pig_tmpfilecompression_codec_param gz
    //%DEFAULT job_priority normal
    //%DEFAULT pig_cachedbag_mem_usage 0.1
    //%DEFAULT pig_skewedjoin_reduce_memusage 0.3
    //%DEFAULT mapredChildJavaOpts -Xmx8000m
    //
    //set default_parallel $and_parallel_param
    //set pig.tmpfilecompression $pig_tmpfilecompression_param
    //set pig.tmpfilecompression.codec $pig_tmpfilecompression_codec_param
    //set job.priority $job_priority
    //set pig.cachedbag.memusage $pig_cachedbag_mem_usage
    //set pig.skewedjoin.reduce.memusage $pig_skewedjoin_reduce_memusage
    //set mapred.child.java.opts $mapredChildJavaOpts
    //-- ulimit must be more than two times the heap size value !
    //-- set mapred.child.ulimit unlimited
    //set dfs.client.socket-timeout 60000
    //%DEFAULT and_scheduler default
    //SET mapred.fairscheduler.pool $and_scheduler
    //-- -----------------------------------------------------
    //-- -----------------------------------------------------
    //-- code section
    //-- -----------------------------------------------------
    //-- -----------------------------------------------------
    //

    //D0 = LOAD '$and_inputDocsData' as (sname:int, datagroup:{(cId:chararray, sname:int, data:map[{(int)}])}, count:long);
    val conf = new SparkConf().setAppName("DisambiguationApr")
    val sc = new SparkContext(conf)

    val a = sc.textFile(and_inputDocsData)

    //tuples == D0

    val tuples = a.map(x => {
      val tupleSchema = "a:(sname:int, datagroup:{(cId:chararray, sname:int, data:map[{(int)}])}, count:long)"

      val schema = org.apache.pig.impl.util.Utils.parseSchema(tupleSchema)
      val converter = new org.apache.pig.builtin.Utf8StorageConverter()
      val fieldSchema = new org.apache.pig.ResourceSchema.ResourceFieldSchema(schema.getField("a"))
      converter.bytesToTuple(("(" + x.replace('\t', ',') + ")").getBytes("UTF-8"), fieldSchema)

    })

    //
    //
    //D1 = foreach D0 generate *, COUNT(datagroup) as cnt;                  

    //D2 = filter D1 by (cnt>0);
    //D = foreach D2 generate sname, datagroup, count;
    // d=D
    val d = tuples.filter(z =>
      {
        z.get(1).asInstanceOf[org.apache.pig.data.DataBag].size > 0
      })
    //
    //-- -----------------------------------------------------
    //-- BIG GRUPS OF CONTRIBUTORS 
    //-- -----------------------------------------------------
    //
    //-- D1000A: {datagroup: NULL,simTriples: NULL}
    //E1 = foreach D generate flatten( aproximateAND( datagroup ) ) as (datagroup:{ ( cId:chararray, sname:int, data:map[{(int)}] ) }, simTriples:{});
    val e1 = d.flatMap(x => {
      val abfs = new pl.edu.icm.coansys.disambiguation.author.pig.AproximateAND_BFS(and_threshold, and_feature_info, and_aproximate_remember_sim, and_use_extractor_id_instead_name, and_statistics)
      val tfac = TupleFactory.getInstance
      val tempT = tfac.newTuple
      tempT.append(x.get(1))
      abfs.exec(tempT).iterator.asInstanceOf[java.util.Iterator[Tuple]].asScala.toList
    })

    //
    //E2 = foreach E1 generate datagroup, simTriples, COUNT( datagroup ) as count;
    e1.cache
    //split E2 into
    //	ESINGLE if count <= 2,
    //	EEXH if ( count > 2 and count <= $and_exhaustive_limit ),
    //	EBIG if count > $and_exhaustive_limit;
    val esingle = e1.filter(x => { x.get(0).asInstanceOf[org.apache.pig.data.DataBag].size <= 2 })
    val eexh = e1.filter(x => {
      val s = x.get(0).asInstanceOf[org.apache.pig.data.DataBag].size
      s > 2 && s <= and_exhaustive_limit
    })
    val ebig = e1.filter(x => { x.get(0).asInstanceOf[org.apache.pig.data.DataBag].size > and_exhaustive_limit })

    //
    //-- -----------------------------------------------------
    //-- TOO BIG CLUSTERS FOR EXHAUSTIVE
    //-- -----------------------------------------------------
    //-- TODO maybe MagicAND for such big clusters in future
    //-- then storing data below and add new node in workflow after aproximates:
    //-- store EBIG into '$and_failedContribs';
    //--
    //-- For now: each contributor from too big cluster is going to get his own UUID
    //-- so we need to "ungroup by sname".
    //
    //I = foreach EBIG generate flatten(datagroup);
    //BIG = foreach I generate cId as cId, GenUUID( TOBAG(cId) ) as uuid;
    //
    val big = ebig.flatMap(x => {
      x.get(0).asInstanceOf[org.apache.pig.data.DataBag].iterator.asScala
    }).map(x => {
      val genuuid = new GenUUID
      val tfac = TupleFactory.getInstance
      val cid = x.get(0)
      val t = tfac.newTuple
      t.append(cid)
      t.append(genuuid.exec(tfac.newTuple(new TOBAG().exec(tfac.newTuple(cid)))))
      t

    })

    //
    //-- -----------------------------------------------------
    //-- CLUSTERS WITH ONE CONTRIBUTOR
    //-- -----------------------------------------------------
    //
    //F = foreach ESINGLE generate datagroup.cId as cIds, GenUUID( datagroup.cId ) as uuid;
    //SINGLE = foreach F generate flatten( cIds ) as cId, uuid as uuid;
    //

    //-- -----------------------------------------------------
    //-- CLUSTERS FOR EXHAUSTIVE
    //-- -----------------------------------------------------
    //
    //G1 = foreach EEXH generate flatten( exhaustiveAND( datagroup, simTriples ) ) as (uuid:chararray, cIds:{(chararray)});
    //G2 = foreach G1 generate *, COUNT(cIds) as cnt;
    //G3 = filter G2 by (uuid is not null and cnt>0);
    //-- H: {cId: chararray,uuid: chararray}
    //H = foreach G3 generate flatten( cIds ) as cId, uuid;
    //
    //-- -----------------------------------------------------
    //-- STORING RESULTS
    //-- -----------------------------------------------------
    //
    //R = union SINGLE, BIG, H;
    //store R into '$and_outputContribs';

  }
}
