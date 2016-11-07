/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.icm.coansys.disambiguation.author.scala

import java.util.Collections
import org.apache.hadoop.io.{ BytesWritable, Text }
import org.apache.pig.data.{ DataBag, Tuple, TupleFactory, DataByteArray }
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import pl.edu.icm.coansys.disambiguation.author.pig.extractor.EXTRACT_CONTRIBDATA_GIVENDATA

import pl.edu.icm.coansys.disambiguation.model.ContributorWithExtractedFeatures
import scala.collection.JavaConversions._
import scala.util.Random

object ANDAll {

  

  val parser = new scopt.OptionParser[Config]("splitter") {
    head("splitter", "1.x")
    opt[Double]("and-sample").action((x, c) =>
      c.copy(and_sample = x)).text("and_sample")

    opt[String]('i', "and-inputDocsData").action((x, c) =>
      c.copy(and_inputDocsData = x)).text("and_inputDocsData")
  /*  opt[String]("and-splitted-output-one").action((x, c) =>
      c.copy(and_splitted_output_one = x)).text("and_splitted_output_one")
    opt[String]("and-splitted-output-exh").action((x, c) =>
      c.copy(and_splitted_output_exh = x)).text("and_splitted_output_exh")
    opt[String]("and-splitted-output-apr-sim").action((x, c) =>
      c.copy(and_splitted_output_apr_sim = x)).text("and_splitted_output_apr_sim")
    opt[String]("and-splitted-output-apr-no-sim").action((x, c) =>
      c.copy(and_splitted_output_apr_no_sim = x)).text("and_splitted_output_apr_no_sim")*/
    opt[String]("and-temp-dir").action((x, c) =>
      c.copy(and_temp_dir = x)).text("and_temp_dir")
    opt[String]("and-cid-dockey").action((x, c) =>
      c.copy(and_cid_dockey = x)).text("and_cid_dockey")
    opt[String]("and-cid-sname").action((x, c) =>
      c.copy(and_cid_sname = x)).text("and_cid_sname")
    opt[Int]("and-aproximate-sim-limit").action((x, c) =>
      c.copy(and_aproximate_sim_limit = x)).text(" and_aproximate_sim_limit")
    opt[Int]("and-exhaustive-limit").action((x, c) =>
      c.copy(and_exhaustive_limit = x)).text("and_exhaustive_limit")
    //			<!-- UDF config -->
    opt[String]("and-skip-empty-features").action((x, c) =>
      c.copy(and_skip_empty_features = x)).text("and_skip_empty_features")
     opt[String]("and-outputPB").action((x, c) =>
      c.copy(and_outputPB = x)).text("and_outputPB")

    opt[String]("and-output-unserialized").action((x, c) =>
      c.copy(and_output_unserialized = x)).text("and_output_unserialized")
    
    opt[String]('f', "and-feature-info").action((x, c) =>
      c.copy(and_feature_info = { (z: String) =>
          {
            if (z.startsWith("\"")) {
              z.substring(1)
            } else {
              z
            }
          }
        }.apply({ (z: String) =>
          {
            if (z.endsWith("\"")) {
              z.substring(0, z.length - 1)
            } else {
              z
            }
          }
        }.apply(x)))).text("and_feature_info")
    opt[String]("and-outputContribs").action((x, c) =>{
      c.copy(and_outputContribs = x).copy(and_outputContribs_one = x+"/one")
      .copy(and_outputContribs_exh = x+"/exh")
      .copy(and_outputContribs_apr_sim= x+"/apr-sim")
      .copy(and_outputContribs_apr_no_sim = x+"/apr-no-sim")
    }
    ).text("and_outputContribs")
      
      
      
      
     
//    opt[String]("and_outputContribs_one").action((x, c) =>:String= "workflows/pl.edu.icm.coansys-disambiguation-author-workflow/results/outputContribs/one",
//    opt[String]("and_outputContribs_exh").action((x, c) =>:String= "workflows/pl.edu.icm.coansys-disambiguation-author-workflow/results/outputContribs/exh",
//    opt[String]("and_outputContribs_apr_sim").action((x, c) =>:String= "workflows/pl.edu.icm.coansys-disambiguation-author-workflow/results/outputContribs/apr_sim",
//    opt[String]("and_outputContribs_apr_no_sim").action((x, c) =>:String= "workflows/pl.edu.icm.coansys-disambiguation-author-workflow/results/outputContribs/apr_no_sim"
    opt[String]("and-lang").action((x, c) =>
      c.copy(and_lang = x)).text("and_lang")
    opt[String]('s', "and-statistics").action((x, c) =>
      c.copy(and_statistics = x)).text("and_statistics")
    opt[String]('t', "and-threshold").action((x, c) =>
      c.copy(and_threshold = x)).text("and_threshold")
    opt[String]('e', "and-use-extractor-id-instead-name").action((x, c) =>
      c.copy(and_use_extractor_id_instead_name = x)).text("and_use_extractor_id_instead_name")

    help("help").text("prints this usage text")

  }

  /**
   * @param args the command line arguments
   */
  def main(args: Array[String]): Unit = {
   
    
    // parser.parse returns Option[C]
    val config= parser.parse(args, Config()) match {
      case Some(config) =>
        config
      case None =>
        // arguments are bad, error message will have been displayed
        return
    }
    
    
    //-- -----------------------------------------------------
    //-- READING SQ, FIRST FILTERING
    //-- -----------------------------------------------------


    val conf = new SparkConf().setAppName("DisambiguationApr")
    val sc = new SparkContext(conf)

    //A1 = LOAD '$and_inputDocsData' USING pl.edu.icm.coansys.commons.pig.udf.RichSequenceFileLoader('org.apache.hadoop.io.Text', 'org.apache.hadoop.io.BytesWritable') as (key:chararray, value:bytearray);
    val a1 = sc.sequenceFile[Text, BytesWritable](config.and_inputDocsData).repartition(sc.defaultParallelism * 2);
    // A2 = sample A1 $and_sample;
    val a2 = if (config.and_sample == 1.0) a1 else a1.sample(false, config.and_sample, (new Random()).nextLong())

    //B1 = foreach A2 generate flatten(snameDocumentMetaExtractor($1)) as (dockey:chararray, cId:chararray, sname:int, metadata:map[{(int)}],str_sname:chararray);


    val edgdParams = List("-featureinfo", config.and_feature_info,
      "-lang", config.and_lang, "-skipEmptyFeatures", config.and_skip_empty_features,
      "-useIdsForExtractors", config.and_use_extractor_id_instead_name).mkString(" ")
    //DEFINE snameDocumentMetaExtractor pl.edu.icm.coansys.disambiguation.author.pig.extractor.
    //EXTRACT_CONTRIBDATA_GIVENDATA
    //('-featureinfo $and_feature_info 
    //  -lang $and_lang 
    //  -skipEmptyFeatures $and_skip_empty_features 
    //  -useIdsForExtractors $and_use_extractor_id_instead_name');

    val b1 = a2
      .flatMap[ContributorWithExtractedFeatures] {
        case (t: Text, bw: BytesWritable) =>
          {
            val extractor = EXTRACT_CONTRIBDATA_GIVENDATA.get_EXTRACT_CONTRIBDATA_GIVENDATA(edgdParams)
            collectionAsScalaIterable(extractor.extract(new DataByteArray(bw.copyBytes)))
          }
      }

    //-- debug data, for later results inspection
    //B2 =  FILTER B1 BY (dockey is not null);
    val b2t = b1.filter(null != _.getDocKey)

    b2t.saveAsObjectFile(config.and_temp_dir + "/splitted_1_temp")

    val b2 = sc.objectFile[ContributorWithExtractedFeatures](config.and_temp_dir + "/splitted_1_temp")
    //
    //B3 = FOREACH B2 generate dockey, cId, str_sname, sname;
    //STORE B3 INTO '$and_cid_sname'; 
    //
    b2.map {
      t => t.getDocKey + "\t" + t.getContributorId + "\t" + t.getSurnameString + "\t" + (if (t.isSurnameNotNull) t.getSurnameInt else "")
    }.saveAsTextFile(config.and_cid_sname)

    //
    //-- storing relation contributor id - document id, which we need in future during serialization
    //-- storing also external person ID for optional accuracy checking after basic AND workfloaw
    //-- note, that for optimization, external person ID might be under shorter name of EX_PERSON_ID feature, which is "8"
    //
    //B = foreach B2 generate dockey, cId, sname, metadata;
    //Q = foreach B generate cId, dockey, FLATTEN(((metadata#'EX_PERSON_ID' is not null) ? metadata#'EX_PERSON_ID' : metadata#'8')) as ex_person_id, sname;
    //store Q into '$and_cid_dockey';

    val q = b2.map(x => {
      (x, if (x.getMetadata.get("EX_PERSON_ID") != null) x.getMetadata.get("EX_PERSON_ID") else x.getMetadata.get("8"))
    })

    q.flatMap {
      case (x, ex_id_list) => {
        if (ex_id_list == null || ex_id_list.isEmpty) {
          List((x, ""))
        } else {
          if (ex_id_list.isEmpty) {
            List((x, ""))
          } else {
            ex_id_list.map(t => { (x, t.toString()) })
          }

        }
      }
    }.map {
      case (x, exId) => {
        x.getContributorId + "\t" + x.getDocKey + "\t" + exId + "\t" + (if (x.getSurnameInt==null) 0 else x.getSurnameInt)
      }

    }.saveAsTextFile(config.and_cid_dockey)

    //B = foreach B2 generate dockey, cId, sname, metadata;
    //
    //-- check if sname exists
    //split B into
    //        CORRECT if (sname is not null),
    //        NOSNAME if (sname is null);
    //
    // divide contribInfoRdd into Correct/Uncorrect classes
    //CORRECT
    val correctCotrinbInfoRdd = b2.filter(_.getSurnameString != null)
    //UNCORRECT
    val noSurName = b2.filter(_.getSurnameString == null)
    ///**
    //-- -----------------------------------------------------
    //-- PROCESSING CONTRIBUTORS WITHOUT SNAME
    //-- -----------------------------------------------------
    //
    //-- simulating grouping ( 'by sname' ) and counting ( = 1 )
    //-- after all we have to assign UUID to every contributor, even for those who do not have sname
    //-- put them into separate clusters size 1
    //D1A = foreach NOSNAME generate null as sname, {(cId,null,metadata)} as datagroup, 1 as count;
    //**/
    // TODO : not known what to do with trhem 
    //val d1a=noSurName.map(x =>{
    //    
    //})

    //


    //-- -----------------------------------------------------
    //-- PROCESSING CONTRIBUTORS DISIMILAR TO THEMSELVES
    //-- -----------------------------------------------------
    //
    //-- removing docId column 
    //-- add bool - true when contributor is similar to himself (has got enough data)
    //FC = foreach CORRECT generate cId as cId, sname as sname, metadata as metadata, featuresCheck(cId, sname, metadata) as gooddata;

    val fc = correctCotrinbInfoRdd.map(x => {
      val checker =
        pl.edu.icm.coansys.disambiguation.author.pig.FeaturesCheck.getFeaturesCheck(config.and_threshold, config.and_feature_info, config.and_use_extractor_id_instead_name, config.and_statistics)
      (x, checker.exec(x))

    })

    //split FC into
    //	BAD if gooddata == false,
    //	GOOD if gooddata == true;

    val bad = fc.filter {
      case (x, f) => {
        !f
      }
    }

    val good = fc.filter {
      case (x, f) => {
        f
      }
    }.map {
      case (x, f) => {
        (x.getSurnameInt, (x))
      }
    }

    //-- simulating grouping ( 'by sname' ) and counting ( = 1 )
    //-- in fact we will get different groups with the same sname - and that is what we need in that case
    //-- because each contributor with bad data need to be in separate cluster size 1
    //D1B = foreach BAD generate sname as sname, {(cId,sname,metadata)} as datagroup, 1 as count;

    val d1b = bad.map {
      case (x, f) => {
        (if (x.getSurnameInt!=null) (x.getSurnameInt.intValue) else (0), List(x), 1)
      }
    }
    //
    //-- -----------------------------------------------------
    //-- PROCESSING CONTRIBUTORS SIMILAR TO THEMSELVES
    //-- -----------------------------------------------------
    //
    //C = group GOOD by sname;
    //-- D: {sname: chararray, datagroup: {(cId: chararray,sname: int,metadata: map[{(val_0: int)}])}, count: long}
    //-- TODO: remove sname from datagroup. Then in UDFs as well..
    //D = foreach C generate group as sname, GOOD as datagroup, COUNT(GOOD) as count;
    //
    val dt = good.groupByKey.map {
      case (s, it) => {
        val li = it.toList.sortWith {
          case (null, null) => false
          case (null, _) => true
          case (_, null) => false
          case (x, y) => { x.getContributorId.compareTo(y.getContributorId) < 0 }

        }
        (s, li, li.size)
      }
    }

    dt.saveAsObjectFile(config.and_temp_dir + "/splitted_d_temp")

    val d = sc.objectFile[(Int, List[ContributorWithExtractedFeatures], Int)](config.and_temp_dir + "/splitted_d_temp")
    //
    //split D into
    //        D1C if count == 1,
    //        D100 if (count > 1 and count <= $and_exhaustive_limit),
    //        DX if (count > $and_exhaustive_limit and count <= $and_aproximate_sim_limit),
    //        D1000 if count > $and_aproximate_sim_limit;
    //        
    val d1c = d.filter {
      case (s, li, c) => { c == 1 }
    }.coalesce(sc.defaultParallelism * 10)
    val d100 = d.filter {
      case (s, li, c) => { (c > 1) && (c <= config.and_exhaustive_limit) }
    }.coalesce(sc.defaultParallelism * 10)
    val dx = d.filter {
      case (s, li, c) => { (c > config.and_exhaustive_limit) && (c <= config.and_aproximate_sim_limit) }
    }.coalesce(sc.defaultParallelism * 10)
    val d1000 = d.filter {
      case (s, li, c) => { c > config.and_aproximate_sim_limit }
    }.coalesce(sc.defaultParallelism * 10)

    //              
    //              
    //              
    //                          
    //
    //-- -----------------------------------------------------
    //-- STORING DATA READY TO DISAMBIGUATION
    //-- -----------------------------------------------------

    //-- add contributors with bad data to table D (single contributors)
    //D1 = union /*D1A,*/ D1B, D1C;
    val d1 = d1b.union(d1c)
    //store D1 into '$and_splitted_output_one';
    //d1.saveAsObjectFile(config.and_splitted_output_one)
    val oneRes=DisambiguationOne.process(d1)

    oneRes.map{
      case (x,y) => { x+"\t"+y} 
    }.saveAsTextFile(config.and_outputContribs_one)
    
    
    
    //store D100 into '$and_splitted_output_exh';
    //
    val exhRes=DisambiguationExh.process(d100, config)
    
    exhRes.map{
      case (x,y) => { x+"\t"+y} 
    }.saveAsTextFile(config.and_outputContribs_exh)
    
    //store D1000 into '$and_splitted_output_apr_sim';

     
    val apr_sim_res=DisambiguationApr.process(d1000, config, true, sc)
    
    apr_sim_res.map{
      case (x,y) => { x+"\t"+y} 
    }.saveAsTextFile(config.and_outputContribs_apr_sim)
    
    //store DX into '$and_splitted_output_apr_no_sim';
    //
    //dx.saveAsObjectFile(config.and_splitted_output_apr_no_sim)
    val apr_no_sim_res=DisambiguationApr.process(dx, config, false, sc)
    
    apr_no_sim_res.map{
      case (x,y) => { x+"\t"+y} 
    }.saveAsTextFile(config.and_outputContribs_apr_no_sim)
    
    Serialize.process(config, sc)
    
  }


}
