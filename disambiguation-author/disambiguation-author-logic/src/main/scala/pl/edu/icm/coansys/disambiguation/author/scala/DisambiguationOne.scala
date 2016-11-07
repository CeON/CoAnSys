
package pl.edu.icm.coansys.disambiguation.author.scala

import org.apache.pig.builtin.TOBAG


import org.apache.pig.data.TupleFactory
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import pl.edu.icm.coansys.disambiguation.author.pig.GenUUID
import pl.edu.icm.coansys.disambiguation.model.ContributorWithExtractedFeatures
import scala.collection.JavaConverters._

object DisambiguationOne {

 
  def process (input:RDD[(Int, List[ContributorWithExtractedFeatures], Int)] ):RDD[(String,String)]= {
      input.flatMap{
       case (cid, list,count) =>{
           val genuuid = new GenUUID
           list.map(x=> ((x.getContributorId,genuuid.exec(List(x).asJava))))
       }
      }
  }
  
 
  
  /**
   * @param args the command line arguments
   */
  def main(args: Array[String]): Unit = {
    // %DEFAULT and_inputDocsData workflows/pl.edu.icm.coansys-disambiguation-author-workflow/results/splitted/apr-no-sim
    var and_inputDocsData = "workflows/pl.edu.icm.coansys-disambiguation-author-workflow/results/splitted/apr-no-sim"
    // %DEFAULT and_threshold '-0.8'
 
    var and_outputContribs="workflows/pl.edu.icm.coansys-disambiguation-author-workflow/results/outputContribs/apr-no-sim"
   
    
   // parser.parse returns Option[C]
    parser.parse(args, Config()) match {
      case Some(config) =>
      // do stuff
        and_inputDocsData=config.and_inputDocsData
       
        and_outputContribs=config.and_outputContribs
      case None =>
      // arguments are bad, error message will have been displayed
      return
    }

   //  DEFINE GenUUID pl.edu.icm.coansys.disambiguation.author.pig.GenUUID();
     

//D1 = LOAD '$and_inputDocsData' as (sname: int, datagroup: {(cId:chararray, sname:int, data:map[{(int)}])}, count: long);
 val conf = new SparkConf().setAppName("DisambiguationOne")
    val sc = new SparkContext(conf)

    val a = sc.textFile(and_inputDocsData)

    //tuples == D0

    val tuples = a.map(x => {
      val tupleSchema = "a:(sname:int, datagroup:{(cId:chararray, sname:int, data:map[{(int)}])}, count:long)"

      val schema = org.apache.pig.impl.util.Utils.parseSchema(tupleSchema)
      val converter = new org.apache.pig.builtin.Utf8StorageConverter()
      val fieldSchema = new org.apache.pig.ResourceSchema.ResourceFieldSchema(schema.getField("a"))
      if (!x.contains("[")) {
        Console.err.println("Problematic string :"+x)
      }
      converter.bytesToTuple(("(" + x.replace('\t', ',') + ")").getBytes("UTF-8"), fieldSchema)

    })
  
    
    
//-- -----------------------------------------------------
//-- SINGLE CONTRIBUTORS ---------------------------------
//-- -----------------------------------------------------
//D1A = foreach D1 generate flatten( datagroup );-- as (cId:chararray, sname:int, metadata:map);
//-- E1: {cId: chararray,uuid: chararray}
//E1 = foreach D1A generate cId as cId, FLATTEN(GenUUID(TOBAG(cId))) as uuid;
val e1=tuples.flatMap(x=>{
        val datags=x.get(1)
        datags.asInstanceOf[org.apache.pig.data.DataBag].iterator.asScala
      }).map( x=> {
       val genuuid = new GenUUID
      val tfac = TupleFactory.getInstance
      val cid = x.get(0).asInstanceOf[String]
     
         (cid,genuuid.exec(tfac.newTuple(new TOBAG().exec(tfac.newTuple(cid)))))
      })

//store E1 into '$and_outputContribs';
  
    
    
    
    
    
    e1.map{
      case (x,y)=> {
        x+"\t"+y
      }
    }.saveAsTextFile(and_outputContribs)
  }
   case class Config(
    and_inputDocsData: String = "workflows/pl.edu.icm.coansys-disambiguation-author-workflow/results/splitted/apr-no-sim",
    and_outputContribs:String= "workflows/pl.edu.icm.coansys-disambiguation-author-workflow/results/outputContribs/apr-no-sim"
  )

  val parser = new scopt.OptionParser[Config]("disambiguationApr") {
    head("disambiguationOne", "1.x")

    opt[String]('i', "and-inputDocsData").action((x, c) =>
      c.copy(and_inputDocsData = x)).text("and_inputDocsData")
   opt[String]('o', "and-outputContribs").action((x, c) => c.copy(and_outputContribs = x)).
      text("and_outputContribs")

    

    help( "help").text("prints this usage text")

    
  }
  
  
  
}
