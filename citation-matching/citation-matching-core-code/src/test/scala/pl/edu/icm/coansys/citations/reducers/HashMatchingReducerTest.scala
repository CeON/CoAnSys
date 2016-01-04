package pl.edu.icm.coansys.citations.reducers

import scala.collection.JavaConversions.seqAsJavaList

import org.apache.hadoop.io.Text
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver
import org.apache.hadoop.mrunit.types.Pair
import org.testng.annotations.Test

import pl.edu.icm.coansys.citations.data.MarkedText

/**
 * @author madryk
 */
class HashMatchingReducerTest {

  @Test(groups = Array("fast"))
  def basicTest() {
    val key = new MarkedText("hash", marked = true)
    val valuesList = List(
      new MarkedText("doc_1", marked = true),
      new MarkedText("doc_2", marked = true),
      new MarkedText("cit_1"),
      new MarkedText("cit_2"),
      new MarkedText("cit_3")
    )

    val output = List(
      new Pair(new Text("cit_1"), new Text("doc_1")),
      new Pair(new Text("cit_2"), new Text("doc_1")),
      new Pair(new Text("cit_3"), new Text("doc_1")),
      new Pair(new Text("cit_1"), new Text("doc_2")),
      new Pair(new Text("cit_2"), new Text("doc_2")),
      new Pair(new Text("cit_3"), new Text("doc_2"))
    )
    new ReduceDriver[MarkedText, MarkedText, Text, Text]()
      .withReducer(new HashMatchingReducer())
      .withInput(key, valuesList)
      .withAllOutput(output)
      .runTest(false)
  }
  
  @Test(groups = Array("fast"))
  def exceeded_bucket_size() {
    val key = new MarkedText("hash", marked = true)
    val valuesList = List(
      new MarkedText("doc_1", marked = true),
      new MarkedText("doc_2", marked = true),
      new MarkedText("doc_3", marked = true),
      new MarkedText("cit_1"),
      new MarkedText("cit_2"),
      new MarkedText("cit_3"),
      new MarkedText("cit_4")
    )

    val driver = new ReduceDriver[MarkedText, MarkedText, Text, Text]()
    driver.getConfiguration.setInt("max.bucket.size", 10)
    driver
      .withReducer(new HashMatchingReducer())
      .withInput(key, valuesList)
      .withAllOutput(List())
      .runTest(false)
  }
  
}