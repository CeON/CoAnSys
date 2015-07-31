///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package pl.edu.icm.coansys.deduplication.organization
//
//import org.apache.spark.SparkConf
//import org.apache.spark.SparkContext
//import org.testng._
//import org.testng.annotations._
//import Assert._
//import pl.edu.icm.coansys.models.OrganizationProtos.OrganizationWrapper
//
//
//class TestDeduplication {
//
//
//  @Test
//  def example = {
//     val builder1=OrganizationWrapper.newBuilder;
//     builder1.setRowId("1");
//     builder1.getOrganizationMetadataBuilder.setKey("1");
//     builder1.getOrganizationMetadataBuilder.addOriginalName("ala");
//     builder1.getOrganizationMetadataBuilder.addEnglishName("la"); 
//     val builder2=OrganizationWrapper.newBuilder;
//     builder2.setRowId("2");
//     builder2.getOrganizationMetadataBuilder.setKey("2");
//     builder2.getOrganizationMetadataBuilder.addOriginalName("la");
//     builder2.getOrganizationMetadataBuilder.addEnglishName("zla"); 
//     
//    
//     
//    val conf = new SparkConf().setAppName("test1").setMaster("local");
//    val context=new SparkContext(conf)
//    val org=Array(builder1.build,builder2.build)
//    val organizations=context.parallelize(org);
//    val dedup=DoDeduplication.dedupOrganizations(organizations)
//    assertEquals(dedup.count, 1)
//  }
//  
//
//}
