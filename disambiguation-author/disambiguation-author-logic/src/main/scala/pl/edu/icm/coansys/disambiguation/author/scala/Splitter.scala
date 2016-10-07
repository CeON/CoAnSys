/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.icm.coansys.disambiguation.author.scala

import java.util.Collections
import org.apache.hadoop.io.{BytesWritable, Text}
import org.apache.pig.data.{DataBag, Tuple, TupleFactory, DataByteArray}
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import pl.edu.icm.coansys.disambiguation.author.pig.extractor.EXTRACT_CONTRIBDATA_GIVENDATA

import scala.collection.JavaConversions._
import scala.util.Random


object Splitter {
//<script>splitter.pig</script>
//			<!--pig parameters: data paths, cluster config, etc -->
//			<param>and_sample=${and_sample}</param>
//			<param>and_inputDocsData=${and_inputDocsData}</param>
//			<param>and_splitted_output_one=${and_splitted_output_one}</param>
//			<param>and_splitted_output_exh=${and_splitted_output_exh}</param>
//			<param>and_splitted_output_apr_sim=${and_splitted_output_apr_sim}
//			</param>
//			<param>and_splitted_output_apr_no_sim=${and_splitted_output_apr_no_sim}
//			</param>
//			<param>and_cid_dockey=${and_cid_dockey}</param>
//			<param>and_cid_sname=${and_cid_sname}</param>
//			<param>and_aproximate_sim_limit=${and_aproximate_sim_limit}</param>
//			<param>and_exhaustive_limit=${and_exhaustive_limit}</param>
//			<!-- UDF config -->
//			<param>and_skip_empty_features=${and_skip_empty_features}</param>
//			<param>and_feature_info=${and_feature_info}</param>
//			<param>and_lang=${and_lang}</param>
//			<param>and_statistics=${and_statistics}</param>
//			<param>and_threshold=${and_threshold}</param>
//			<!--commons -->
  case class Config(
    and_sample: Double = 1.0,
    and_inputDocsData: String = "workflows/pl.edu.icm.coansys-disambiguation-author-workflow/results/splitted/apr-no-sim",
    and_splitted_output_one: String ="workflows/pl.edu.icm.coansys-disambiguation-author-workflow/results/splitted/apr-no-sim",
    and_splitted_output_exh: String ="workflows/pl.edu.icm.coansys-disambiguation-author-workflow/results/splitted/apr-no-sim",
    and_splitted_output_apr_sim: String ="workflows/pl.edu.icm.coansys-disambiguation-author-workflow/results/splitted/apr-no-sim",
    and_splitted_output_apr_no_sim: String ="workflows/pl.edu.icm.coansys-disambiguation-author-workflow/results/splitted/apr-no-sim",
    and_temp_dir: String ="workflows/pl.edu.icm.coansys-disambiguation-author-workflow/results/temp/",
    and_cid_dockey: String ="cid_dockey",
    and_cid_sname: String ="cid_sname",
    
    and_aproximate_sim_limit:Int =1000000,
    and_exhaustive_limit: Int = 6627,
     
//			<!-- UDF config -->
    and_skip_empty_features:String= "true",
    and_feature_info: String = "IntersectionPerMaxval#EX_DOC_AUTHS_SNAMES#1.0#1",
    and_lang:String="all",
    and_statistics: String = "false",
    and_threshold: String = "-0.8",
    and_use_extractor_id_instead_name: String = "true"
   
   
  )

  val parser = new scopt.OptionParser[Config]("splitter") {
    head("splitter", "1.x")
     opt[Double]("and-sample").action((x, c) =>
      c.copy(and_sample = x)).text("and_sample")
    
    opt[String]('i', "and-inputDocsData").action((x, c) =>
      c.copy(and_inputDocsData = x)).text("and_inputDocsData")
    opt[String]( "and-splitted-output-one").action((x, c) =>
      c.copy(and_splitted_output_one = x)).text("and_splitted_output_one")
     opt[String]( "and-splitted-output-exh").action((x, c) =>
      c.copy(and_splitted_output_exh = x)).text("and_splitted_output_exh")
     opt[String]( "and-splitted-output-apr-sim").action((x, c) =>
      c.copy(and_splitted_output_apr_sim = x)).text("and_splitted_output_apr_sim")
     opt[String]( "and-splitted-output-apr-no-sim").action((x, c) =>
      c.copy(and_splitted_output_apr_no_sim = x)).text("and_splitted_output_apr_no_sim")
    opt[String]( "and-temp-dir").action((x, c) =>
      c.copy(and_temp_dir = x)).text("and_temp_dir")
     opt[String]( "and-cid-dockey").action((x, c) =>
      c.copy(and_cid_dockey = x)).text("and_cid_dockey")
    opt[String]( "and-cid-sname").action((x, c) =>
      c.copy(and_cid_sname = x)).text("and_cid_sname")
    opt[Int]( "and-aproximate-sim-limit").action((x, c) =>
      c.copy( and_aproximate_sim_limit = x)).text(" and_aproximate_sim_limit")
    opt[Int]( "and-exhaustive-limit").action((x, c) =>
      c.copy(and_exhaustive_limit = x)).text("and_exhaustive_limit") 
//			<!-- UDF config -->
   opt[String]( "and-skip-empty-features").action((x, c) =>
      c.copy(and_skip_empty_features = x)).text("and_skip_empty_features")
    
    opt[String]('f', "and-feature-info").action((x, c) =>
      c.copy(and_feature_info = x)).text("and_feature_info")
    opt[String]("and-lang").action((x, c) =>
      c.copy(and_lang = x)).text("and_lang")
    opt[String]('s', "and-statistics").action((x, c) =>
      c.copy(and_statistics = x)).text("and_statistics")
    opt[String]('t', "and-threshold").action((x, c) =>
      c.copy(and_threshold = x)).text("and_threshold")
    opt[String]('e', "and-use-extractor-id-instead-name").action((x, c) =>
      c.copy(and_use_extractor_id_instead_name = x)).text("and_use_extractor_id_instead_name") 
    

    help( "help").text("prints this usage text")

    
  }

  /**
   * @param args the command line arguments
   */
  def main(args: Array[String]): Unit = {
    var and_sample: Double = 1.0
    var and_inputDocsData: String = "workflows/pl.edu.icm.coansys-disambiguation-author-workflow/results/splitted/apr-no-sim"
    var and_splitted_output_one: String ="workflows/pl.edu.icm.coansys-disambiguation-author-workflow/results/splitted/apr-no-sim"
    var and_splitted_output_exh: String ="workflows/pl.edu.icm.coansys-disambiguation-author-workflow/results/splitted/apr-no-sim"
    var and_splitted_output_apr_sim: String ="workflows/pl.edu.icm.coansys-disambiguation-author-workflow/results/splitted/apr-no-sim"
    var and_splitted_output_apr_no_sim: String ="workflows/pl.edu.icm.coansys-disambiguation-author-workflow/results/splitted/apr-no-sim"
    var and_temp_dir: String ="workflows/pl.edu.icm.coansys-disambiguation-author-workflow/results/temp/"
    var and_cid_dockey: String ="cid_dockey"
    var and_cid_sname: String ="cid_sname"
    
    var and_aproximate_sim_limit:Int =1000000
    var and_exhaustive_limit: Int = 6627
     
    //			<!-- UDF config -->
    var and_skip_empty_features:String= "true"
    var and_feature_info: String = "IntersectionPerMaxval#EX_DOC_AUTHS_SNAMES#1.0#1"
    var and_lang:String="all"
    var and_statistics: String = "false"
    var and_threshold: String = "-0.8"
    var and_use_extractor_id_instead_name: String = "true"
    
   // parser.parse returns Option[C]
    parser.parse(args, Config()) match {
      case Some(config) =>
      // do stuff
        and_sample = config.and_sample
    and_inputDocsData = config.and_inputDocsData
    and_splitted_output_one = config.and_splitted_output_one
    and_splitted_output_exh = config.and_splitted_output_exh
    and_splitted_output_apr_sim = config.and_splitted_output_apr_sim
    and_splitted_output_apr_no_sim = config.and_splitted_output_apr_no_sim
    and_temp_dir=config.and_temp_dir
    and_cid_dockey = config.and_cid_dockey
    and_cid_sname = config.and_cid_sname
    
    and_aproximate_sim_limit = config.and_aproximate_sim_limit
    and_exhaustive_limit = config.and_exhaustive_limit
    
//			<!-- UDF config -->
    and_skip_empty_features = config.and_skip_empty_features
    and_feature_info =  {(z:String) => { if (z.startsWith("\"")) {
             z.substring(1)
          }  else {
            z
          } }}.apply({(z:String) => { if (z.endsWith("\"")) {
             z.substring(0, z.length-1)
          }  else {
            z
          } }}.apply(config.and_feature_info))
    and_lang = config.and_lang
    and_statistics = config.and_statistics
    and_threshold = config.and_threshold
    and_use_extractor_id_instead_name = config.and_use_extractor_id_instead_name
        
        
      
       
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
    val a1= sc.sequenceFile[Text, BytesWritable](and_inputDocsData).repartition(sc.defaultParallelism*2);
    
   // A2 = sample A1 $and_sample;
   val a2=if (and_sample==1.0) a1 else a1.sample(false, and_sample, (new Random()).nextLong())
   
    
   //B1 = foreach A2 generate flatten(snameDocumentMetaExtractor($1)) as (dockey:chararray, cId:chararray, sname:int, metadata:map[{(int)}],str_sname:chararray);

    val edgdParams = List("-featureinfo", and_feature_info,
          "-lang", and_lang, "-skipEmptyFeatures", and_skip_empty_features,
          "-useIdsForExtractors", and_use_extractor_id_instead_name).mkString(" ") 
  //DEFINE snameDocumentMetaExtractor pl.edu.icm.coansys.disambiguation.author.pig.extractor.
  //EXTRACT_CONTRIBDATA_GIVENDATA
  //('-featureinfo $and_feature_info 
  //  -lang $and_lang 
  //  -skipEmptyFeatures $and_skip_empty_features 
  //  -useIdsForExtractors $and_use_extractor_id_instead_name');
 
   val b1=a2
      .flatMap[Tuple] {
      case (t: Text, bw: BytesWritable) =>
       {
        val extractor= EXTRACT_CONTRIBDATA_GIVENDATA.get_EXTRACT_CONTRIBDATA_GIVENDATA(edgdParams)
        val t=TupleFactory.getInstance().newTuple;
        t.append(new DataByteArray(bw.copyBytes))
        val results: DataBag =extractor.exec(t)
        results.iterator()
       }
    }
      .map(extractFirstTuple(_))
     
    
    
//-- debug data, for later results inspection
//B2 =  FILTER B1 BY (dockey is not null);
  val b2t=b1.filter(null != _.docKey)
    
  b2t.saveAsObjectFile(and_temp_dir+"/splitted_1_temp")

    
  val b2=sc.objectFile[ContribInfoTuple](and_temp_dir+"/splitted_1_temp")
//
//B3 = FOREACH B2 generate dockey, cId, str_sname, sname;
//STORE B3 INTO '$and_cid_sname'; 
//
    b2.map {
      t => t.docKey+"\t"+t.contribId+"\t"+t.surnameStr+"\t"+ t.surnameInt;
    }.saveAsTextFile(and_cid_sname)

//
//-- storing relation contributor id - document id, which we need in future during serialization
//-- storing also external person ID for optional accuracy checking after basic AND workfloaw
//-- note, that for optimization, external person ID might be under shorter name of EX_PERSON_ID feature, which is "8"
//
//B = foreach B2 generate dockey, cId, sname, metadata;
//Q = foreach B generate cId, dockey, FLATTEN(((metadata#'EX_PERSON_ID' is not null) ? metadata#'EX_PERSON_ID' : metadata#'8')) as ex_person_id, sname;
//store Q into '$and_cid_dockey';
 val q = b2.map(x=> {
     (x.contribId,x.docKey, if (x.metadata.get("EX_PERSON_ID")!=null)  x.metadata.get("EX_PERSON_ID") else x.metadata.get("8"),x.surnameInt)
   })
 q.flatMap{
     case (x,y,z,v) => {
         if (z==null || z.isEmpty)  {
           List((x,y,"",v))
         } else {
           val db=z.get
           if (db.isEmpty) {
             List((x,y,"",v))
           } else {
             db.iterator.flatMap(t=>{t.iterator.map(n=>(x,y,n.toString(),v))})
           }
           
         }
     }
   }.map{
     case (x,y,z,v) => {
        x+"\t"+y+"\t"+z+"\t"+v
         }
      
   }.saveAsTextFile(and_cid_dockey)
   
//B = foreach B2 generate dockey, cId, sname, metadata;
//
//-- check if sname exists
//split B into
//        CORRECT if (sname is not null),
//        NOSNAME if (sname is null);
//
  // divide contribInfoRdd into Correct/Uncorrect classes
    //CORRECT
    val correctCotrinbInfoRdd = b2.filter(_.surnameStr!=null)
    //UNCORRECT
    val noSurName = b2.filter(_.surnameStr==null)



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

 val fc=correctCotrinbInfoRdd.map ( x => {
       val checker=
         pl.edu.icm.coansys.disambiguation.author.pig.FeaturesCheck.getFeaturesCheck(and_threshold,and_feature_info,and_use_extractor_id_instead_name,and_statistics)
         val tfac = TupleFactory.getInstance
         val tempT = tfac.newTuple
         tempT.append(x.contribId)
         tempT.append(x.surnameInt)
         tempT.append(mapAsJavaMap(x.metadata))
         (x,checker.exec(tempT))
         
      })
    
    
    
//split FC into
//	BAD if gooddata == false,
//	GOOD if gooddata == true;

val bad=fc.filter {case (x,f) => { 
      !f }
  }

val good=fc.filter {case (x,f) => {
   f}}.map{case (x,f) => {
   (x.surnameInt,(x))}}    
    
//-- simulating grouping ( 'by sname' ) and counting ( = 1 )
//-- in fact we will get different groups with the same sname - and that is what we need in that case
//-- because each contributor with bad data need to be in separate cluster size 1
//D1B = foreach BAD generate sname as sname, {(cId,sname,metadata)} as datagroup, 1 as count;

val d1b=bad.map{
  case (x,f) => {
   (x.surnameInt,List(x),1)}
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
val dt = good.groupByKey.map{
   case (s, it) => {
       val li=it.toList
       (s,li,li.size)
   }
}

dt.saveAsObjectFile(and_temp_dir+"/splitted_d_temp")

    
  val d=sc.objectFile[(Int,List[ContribInfoTuple],Int)](and_temp_dir+"/splitted_d_temp")
//
//split D into
//        D1C if count == 1,
//        D100 if (count > 1 and count <= $and_exhaustive_limit),
//        DX if (count > $and_exhaustive_limit and count <= $and_aproximate_sim_limit),
//        D1000 if count > $and_aproximate_sim_limit;
//        
val d1c=d.filter{ case 
  (s,li,c) => {c==1}}.coalesce(sc.defaultParallelism*2)
val d100=d.filter{ case 
  (s,li,c) => {(c>1) && (c<=and_exhaustive_limit) }}.coalesce(sc.defaultParallelism*2)
val dx=d.filter{ case 
  (s,li,c) => {(c>and_exhaustive_limit) && (c <= and_aproximate_sim_limit)}}.coalesce(sc.defaultParallelism*2)
val d1000=d.filter{ case 
  (s,li,c) => {c>and_aproximate_sim_limit}}.coalesce(sc.defaultParallelism*2)
    
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
val d1=d1b.union(d1c)
    
    
//store D1 into '$and_splitted_output_one';
prepStrings(d1).saveAsTextFile(and_splitted_output_one)

//store D100 into '$and_splitted_output_exh';
//
prepStrings(d100) .saveAsTextFile(and_splitted_output_exh)

//store D1000 into '$and_splitted_output_apr_sim';

prepStrings(d1000) .saveAsTextFile(and_splitted_output_apr_sim)   
    
//store DX into '$and_splitted_output_apr_no_sim';
//
prepStrings(dx) .saveAsTextFile(and_splitted_output_apr_no_sim)  


    
    
  }

def prepStrings(x:RDD[(Int,List[ContribInfoTuple],Int)]) :RDD[String] ={
  x.map{case 
  (s,li,c) => {
     s+"\t"+ li.map( x => {
        x.contribId+","+x.surnameInt+",["+
          ((x.metadata.iterator.map{
             case (i,mapValIt) =>  {
                i+"#"+(mapValIt.toList.mkString("{", ",", "}"))
               
                        
              }
          }).mkString(","))+"]"
         })
       .mkString("{(", "),(", ")}")+"\t"+c
     } 
 }
}  
  
  
def extractFirstTuple(tuple: Tuple): ContribInfoTuple = {
    ContribInfoTuple(
      tuple.get(0).asInstanceOf[String],
      tuple.get(1).asInstanceOf[String],
      tuple.get(2).asInstanceOf[Int],
      tuple.get(3).asInstanceOf[java.util.Map[Object, DataBag]].toMap,
      tuple.get(4).asInstanceOf[String])
  }
  
  case class ContribInfoTuple(docKey: String,
                              contribId: String,
                              surnameInt: Int,
                              metadata: Map[Object, DataBag],
                              surnameStr: String)

  
}
