
package pl.edu.icm.coansys.disambiguation.author.scala
import java.io.File
import org.apache.hadoop.io.BytesWritable
import org.apache.pig.builtin.TOBAG

import org.apache.pig.data.Tuple
import org.apache.pig.data.TupleFactory
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import pl.edu.icm.coansys.disambiguation.author.pig.GenUUID
import scala.collection.JavaConverters._

object DisambiguationExh {

  case class Config(
    and_inputDocsData: String = "workflows/pl.edu.icm.coansys-disambiguation-author-workflow/results/splitted/apr-no-sim",
    and_threshold: String = "-0.8",
    and_feature_info: String = "IntersectionPerMaxval#EX_DOC_AUTHS_SNAMES#1.0#1",
    and_use_extractor_id_instead_name: String = "true",
    and_statistics: String = "false",
    and_exhaustive_limit: Int = 6627,
    and_outputContribs:String= "workflows/pl.edu.icm.coansys-disambiguation-author-workflow/results/outputContribs/apr-no-sim"
  )

  val parser = new scopt.OptionParser[Config]("disambiguationApr") {
    head("disambiguationApr", "1.x")

    opt[String]('i', "and-inputDocsData").action((x, c) =>
      c.copy(and_inputDocsData = x)).text("and_inputDocsData")

    opt[String]('t', "and-threshold").action((x, c) =>
      c.copy(and_threshold = x)).text("and_threshold")
    opt[String]('f', "and-feature-info").action((x, c) =>
      c.copy(and_feature_info = x)).text("and_feature_info")
    opt[String]('e', "and-use-extractor-id-instead-name").action((x, c) =>
      c.copy(and_use_extractor_id_instead_name = x)).text("and_use_extractor_id_instead_name")
    opt[String]('s', "and-statistics").action((x, c) =>
      c.copy(and_statistics = x)).text("and_statistics")
    opt[Int]('l', "and-exhaustive-limit").action((x, c) =>
      c.copy(and_exhaustive_limit = x)).text("and_exhaustive_limit")

    opt[String]('o', "and-outputContribs").action((x, c) => c.copy(and_outputContribs = x)).
      text("and_outputContribs")

    

    help( "help").text("prints this usage text")

    
  }

  /**
   * @param args the command line arguments
   */
  def main(args: Array[String]): Unit = {
    // %DEFAULT and_inputDocsData workflows/pl.edu.icm.coansys-disambiguation-author-workflow/results/splitted/apr-no-sim
    var and_inputDocsData = "workflows/pl.edu.icm.coansys-disambiguation-author-workflow/results/splitted/apr-no-sim"
    // %DEFAULT and_threshold '-0.8'
    var and_threshold = "-0.8"
    // %DEFAULT and_feature_info 'IntersectionPerMaxval#EX_DOC_AUTHS_SNAMES#1.0#1'
    var and_feature_info = "IntersectionPerMaxval#EX_DOC_AUTHS_SNAMES#1.0#1"

    // %DEFAULT and_use_extractor_id_instead_name 'true'

    var and_use_extractor_id_instead_name = "true"
    // %DEFAULT and_statistics 'false'

    var and_statistics = "false"
    // %DEFAULT and_time ''
    // %DEFAULT and_outputContribs disambiguation/outputContribs$and_time
    // 
    // 
    var and_outputContribs="workflows/pl.edu.icm.coansys-disambiguation-author-workflow/results/outputContribs/apr-no-sim"
     // %DEFAULT and_exhaustive_limit 6627
    var and_exhaustive_limit = 6627
    
   // parser.parse returns Option[C]
    parser.parse(args, Config()) match {
      case Some(config) =>
      // do stuff
        and_inputDocsData=config.and_inputDocsData
        and_threshold=config.and_threshold
        and_feature_info= 
         {(z:String) => { if (z.startsWith("\"")) {
             z.substring(1)
          }  else {
            z
          } }}.apply({(z:String) => { if (z.endsWith("\"")) {
             z.substring(0, z.length-1)
          }  else {
            z
          } }}.apply(config.and_feature_info))
        and_use_extractor_id_instead_name=config.and_use_extractor_id_instead_name
        and_statistics=config.and_statistics
        and_outputContribs=config.and_outputContribs
        and_exhaustive_limit=config.and_exhaustive_limit
      case None =>
      // arguments are bad, error message will have been displayed
      return
    }

//%DEFAULT and_inputDocsData workflows/pl.edu.icm.coansys-disambiguation-author-workflow/results/splitted/exh
//%DEFAULT and_outputContribs disambiguation/outputContribs$and_time
//%DEFAULT and_feature_info 'IntersectionPerMaxval#EX_DOC_AUTHS_SNAMES#1.0#1'
//%DEFAULT and_threshold '-0.8'
//%DEFAULT and_use_extractor_id_instead_name 'true'
//%DEFAULT and_statistics 'false'
//
//DEFINE exhaustiveAND pl.edu.icm.coansys.disambiguation.author.pig.ExhaustiveAND('$and_threshold','$and_feature_info','$and_use_extractor_id_instead_name','$and_statistics');
//-- -----------------------------------------------------
//-- -----------------------------------------------------
//-- code section
//-- -----------------------------------------------------
//-- -----------------------------------------------------
//D100 = LOAD '$and_inputDocsData' as (sname:int, datagroup:{(cId:chararray, sname:int, data:map[{(int)}])}, count:long);

   val conf = new SparkConf().setAppName("DisambiguationApr")
    val sc = new SparkContext(conf)

    val a = sc.textFile(and_inputDocsData)

    //tuples == D100

    val tuples = a.map(x => {
      val tupleSchema = "a:(sname:int, datagroup:{(cId:chararray, sname:int, data:map[{(int)}])}, count:long)"

      val schema = org.apache.pig.impl.util.Utils.parseSchema(tupleSchema)
      val converter = new org.apache.pig.builtin.Utf8StorageConverter()
      val fieldSchema = new org.apache.pig.ResourceSchema.ResourceFieldSchema(schema.getField("a"))
      converter.bytesToTuple(("(" + x.replace('\t', ',') + ")").getBytes("UTF-8"), fieldSchema)

    })
   
    
    
//-- -----------------------------------------------------
//-- SMALL GRUPS OF CONTRIBUTORS -------------------------
//-- -----------------------------------------------------
//D100A = foreach D100 generate flatten( exhaustiveAND( datagroup ) ) as (uuid:chararray, cIds:{(chararray)});
//
val d100a = tuples.flatMap{x => {
          val exhAnd= new pl.edu.icm.coansys.disambiguation.author.pig.ExhaustiveAND(and_threshold,and_feature_info,and_use_extractor_id_instead_name,and_statistics);
          val tfac = TupleFactory.getInstance
          val tempT = tfac.newTuple
          tempT.append(x.get(1))
          exhAnd.exec(tempT).iterator.asInstanceOf[java.util.Iterator[Tuple]].asScala
          
      
      }}

//D100X1 = foreach D100A generate *, COUNT(cIds) as cnt;

//D100X2 = filter D100X1 by (uuid is not null and cnt>0);
 val d100x2=
   d100a.filter{x => {
       x.get(0)!=null && x.get(1).asInstanceOf[org.apache.pig.data.DataBag].size > 0
        
        }}
//E100 = foreach D100X2 generate flatten( cIds ) as cId, uuid;
//val e100 = 
 d100x2.flatMap{
   x=> {
     val uuid=x.get(0)
     x.get(1).asInstanceOf[org.apache.pig.data.DataBag].iterator.asScala.map{
       z => {
         (z.get(0),uuid)
       }
     }
   }
}.map{
  case (a:Object,z:Object)=> {
    ""+a.toString+"\t"+z
  }
}.saveAsTextFile(and_outputContribs)
    
    
//store E100 into '$and_outputContribs';
  }
}
  
