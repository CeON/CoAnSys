/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.icm.coansys.document.deduplication

import com.holdenkarau.spark.testing.SharedSparkContext
import org.scalatest.FunSuite
import org.scalatest.GivenWhenThen
import pl.edu.icm.coansys.models.DocumentProtos
import pl.edu.icm.coansys.models.DocumentProtos._

class DeduplicateDocumentTest extends FunSuite with GivenWhenThen with SharedSparkContext {

  test("docaument validation") {
    Given("Empty document")
    val doc = DocumentWrapper.newBuilder().setRowId("test").build;
    When("We validate")
    Then("Document is invalid")
    assert(!DeduplicateDocuments.isValidDocument(doc))
    Given("Doc with empty metadata")
    val doc2 = DocumentWrapper.newBuilder(doc).setDocumentMetadata(
      DocumentMetadata.newBuilder
      .setBasicMetadata(BasicMetadata.newBuilder.build)
      .setKey("Key")
      .build
    ).build
    When("We test if it is valid")

    Then("It is not valid")
    assert(!DeduplicateDocuments.isValidDocument(doc2))

    Given("Doc with title ")
    val doc3 = DocumentWrapper.newBuilder(doc2).setDocumentMetadata(
      DocumentMetadata.newBuilder()
      .setBasicMetadata(BasicMetadata.newBuilder().addTitle(TextWithLanguage.newBuilder.setText("Title")).build)
      .setKey("key")
      .build
    )
      .build
    When("We test if it is valid: ")
    assert(DeduplicateDocuments.isValidDocument(doc3))
    Then("It is not valid")
  }

  test("Sample with spark context") {
    Given("RDD of sequence 1 to n (n=100)")
    val n = 100
    val rdd = sc.parallelize(1 to n)
    When("We sum")
    val sum = rdd.sum
    Then("result is n*(n+1)/2")
    assertResult(n * (n + 1) / 2)(sum)

  }
  
  
    def createDocument(key:String ,  title:String):DocumentWrapper = {
        DocumentProtos.DocumentWrapper.newBuilder().setDocumentMetadata(
                DocumentProtos.DocumentMetadata.newBuilder().setKey(key).setBasicMetadata(
                        DocumentProtos.BasicMetadata.newBuilder().addTitle(
                                DocumentProtos.TextWithLanguage.newBuilder().setText(title)))
        ).setRowId(key).build();
    }

    
  test("Initial clustering test:") {
      Given("Data set has the same title begninnings")
      
      val d3 = (1 to 10).map(x=> createDocument("id_aaa"+x, "aaa"))
      val d4 = (1 to 10).map(x=> createDocument("id_aaaa"+x, "aaaa"))
      val d5 = (1 to 10).map(x=> createDocument("id_aaaaa"+x, "aaaaa"))
      val d12 = (1 to 10).map(x=> createDocument("id_aaaaaaaaaaa"+x, "aaaaaaaaaa"+x))
      val docs = List()++d3++d4++d5++d12;
      val input = sc.parallelize(docs).map(doc=> (doc.getRowId, doc))
      When("We build clustering with short key")
      val r1 = DeduplicateDocuments.prepareInitialClustering(input, 2,
      2, 20)
      Then("We get only one cluster, with all documents:")
      val r1c = r1.collect
      assert(r1c.size==1)
      assert(r1c(0)._2.size==40)
      And("Key is 1st and 3rd letter")
      assert(r1c(0)._1=="aa")
      When("We build clustering with variable key 2-3")
      val r2 = DeduplicateDocuments.prepareInitialClustering(input, 2,
      3, 10)
      Then("We get only two clusters:")
      val r2c = r2.collect
      assert(r2c.size==2)
      val r2cm = r2c.toMap
      assert(r2cm("aa").size==20)
      assert(r2cm("aaa").size==20)
//      r2c.flatMap(_._2).map(_.getKey())
//        
      When("We build clustering with variable key 2-5")
      val r3 = DeduplicateDocuments.prepareInitialClustering(input, 2,
      5, 10)
      Then("We get 3 clusters:")
      val r3c = r3.collect
      assert(r3c.size==3)
      val r3cm = r3c.toMap
      assert(r3cm("aa").size==20)
      assert(r3cm("aaa").size==10)
      assert(r3cm("aaaa").size==10)
        
      When("We build clustering with variable key 2-6")
      val r4 = DeduplicateDocuments.prepareInitialClustering(input, 2,
      6, 9)
      Then("We get 11 clusters:")
      val r4c = r4.collect
      assert(r4c.size==11)
      val r4cm = r4c.toMap
      assert(r4cm("aa").size==20)
      assert(r4cm("aaa").size==10)
      assert(r4cm("aaaaa2").size==1)
      assert(r4cm("aaaaa1").size==2)
        

  }

}
