/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.icm.coansys.matching.organization

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.testng._
import org.testng.annotations._
import Assert._
import pl.edu.icm.coansys.models.AffiliationMatchedProtos.AllOrganizationFromDocMatchingOut
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper
import pl.edu.icm.coansys.models.OrganizationProtos.OrganizationWrapper


object TestMatching {


  @Test
  def example = {
     val builder1=OrganizationWrapper.newBuilder;
     builder1.setRowId("1");
     builder1.getOrganizationMetadataBuilder.setKey("1");
     builder1.getOrganizationMetadataBuilder.addOriginalName("ala");
     builder1.getOrganizationMetadataBuilder.addEnglishName("la"); 
     val builder2=DocumentWrapper.newBuilder;
     builder2.setRowId("2");
     builder2.getDocumentMetadataBuilder.setKey("2");
     val aff=builder2.getDocumentMetadataBuilder.addAffiliationsBuilder
     aff.setKey("2");
     aff.setAffiliationId("a1");
     aff.setText("dept of cs ala");
     builder2.getDocumentMetadataBuilder.getBasicMetadataBuilder.addTitleBuilder.setText("test").setLanguage("und")
     
    
     
    val conf = new SparkConf().setAppName("test1").setMaster("local");
    val context=new SparkContext(conf)
    val org=Array(("1",builder1.build.toByteArray));
    val doc=Array(("2",builder2.build.toByteArray));
    val organizations=context.parallelize(org);
    val documents=context.parallelize(doc)
    val dedup=DoMatching.doMatching(organizations,documents);
    val matched=AllOrganizationFromDocMatchingOut.parseFrom(dedup.first._2)
    assertEquals(matched.getSingleMatchCount, 2)
  }
  
  /**
   * @param args the command line arguments
   */
  def main(args: Array[String]): Unit = {
    example
  }
}
