/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.icm.coansys.document.deduplication

import org.scalatest.FunSuite
import org.scalatest._
import pl.edu.icm.coansys.models.DocumentProtos
import pl.edu.icm.coansys.models.DocumentProtos._

class CartesianTaskSplitSuite  extends FunSuite with GivenWhenThen {
    
    
    def createDocument(key:String ,  title:String):DocumentWrapper = {
        DocumentProtos.DocumentWrapper.newBuilder().setDocumentMetadata(
                DocumentProtos.DocumentMetadata.newBuilder().setKey(key).setBasicMetadata(
                        DocumentProtos.BasicMetadata.newBuilder().addTitle(
                                DocumentProtos.TextWithLanguage.newBuilder().setText(title)))
        ).setRowId(key).build();
    }

    def createDocumentList(size:Int):Seq[DocumentWrapper] = {
        (1 to size).map(idx => createDocument(f"key_$idx", f"title_$idx")).toSeq
    }
    
    def crossProduct[T](l1:Seq[T]):Seq[(T,T)] =  {
        crossProduct(l1, l1)
    }
    
    
    def crossProduct[T](l1:Seq[T], l2:Seq[T]):Seq[(T,T)] =  {
        l1.flatMap(x1=>l2.map((x1,_)))
    }
    
    
    
    test("Parallelize empty set") {
        Given("Empty task list")
        When("We parallelise")
        val res = CartesianTaskSplit.parallelizeCluster("testCluster", Seq.empty[DocumentWrapper], 10)
        Then("result is empty")
        assert(res.isEmpty)
    }
    
    test("Parallelize set") {
        Given("Set of 5 documents")
        val docs = createDocumentList(5)
        val clusterId = "testCluster"
        When("We parallelise with size equal to doc number")
        val res = CartesianTaskSplit.parallelizeCluster(clusterId, docs, docs.size)
        Then("result is single item")
        assertResult(1)(res.size)
        When("We parallelize with large tile size")
        val r2 = CartesianTaskSplit.parallelizeCluster(clusterId, docs, docs.size+3)
        Then("result is single item")
        assertResult(1)(r2.size)
        When("We parallelize with large 3")
        val r3 = CartesianTaskSplit.parallelizeCluster(clusterId, docs, 3)
        Then("result have 4 tasks")
        assertResult(4)(r3.size)
        And("Each task the same Given clusterId")
        assert(r3.forall(_.clusterId==clusterId))
    }
    
    
    
//    
//    test("All items present in result tasks") {
//        Given("Set of 5 documents")
//        val docs = createDocumentList(5)
//        val clusterId = "testCluster"
//        When("We parallelise to size 2")
//        val res = CartesianTaskSplit.parallelizeCluster(clusterId, docs,2)
//        Then("Expect 9 tasks")
//        assertResult(9)(res.size)
//        And("Each cartesian pair is present")
//        val allPairs = crossProduct(docs.map(_.getDocumentMetadata.getKey)).toSet
//        
//        val taskPairs = res.flatMap(task=> {crossProduct(task.rows, task.columns)}).toSet
//        assertResult(allPairs)(taskPairs)
//        
//    }
    
    
}
