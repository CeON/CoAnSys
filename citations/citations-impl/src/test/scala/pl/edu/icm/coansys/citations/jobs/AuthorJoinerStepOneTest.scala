package pl.edu.icm.coansys.citations.jobs

import collection.JavaConversions._
import org.testng.annotations.Test
import org.apache.hadoop.mrunit.mapreduce.{ReduceDriver, MapReduceDriver}
import org.apache.hadoop.mrunit.types.Pair
import pl.edu.icm.coansys.citations.data.{MarkedBytesWritable, MarkedText}
import org.apache.hadoop.io.BytesWritable
import pl.edu.icm.coansys.citations.reducers.AuthorJoinerStepOne

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class AuthorJoinerStepOneTest {

  val charset: String = "utf8"

  @Test(groups = Array("fast"))
  def reducerTest() {
    val key = new MarkedText("key", marked = true)
    val bytesList = List(
      new MarkedBytesWritable("1*".getBytes(charset), marked = true),
      new MarkedBytesWritable("2*".getBytes(charset), marked = true),
      new MarkedBytesWritable("3*".getBytes(charset), marked = true),
      new MarkedBytesWritable("1".getBytes(charset)),
      new MarkedBytesWritable("2".getBytes(charset)),
      new MarkedBytesWritable("3".getBytes(charset)),
      new MarkedBytesWritable("4".getBytes(charset))
    )
    val output = List(
      new Pair(new MarkedText("key_0", marked = true), new BytesWritable("1*".getBytes(charset))),
      new Pair(new MarkedText("key_1", marked = true), new BytesWritable("2*".getBytes(charset))),
      new Pair(new MarkedText("key_2", marked = true), new BytesWritable("3*".getBytes(charset))),
      new Pair(new MarkedText("key_0"), new BytesWritable("1".getBytes(charset))),
      new Pair(new MarkedText("key_1"), new BytesWritable("1".getBytes(charset))),
      new Pair(new MarkedText("key_2"), new BytesWritable("1".getBytes(charset))),
      new Pair(new MarkedText("key_0"), new BytesWritable("2".getBytes(charset))),
      new Pair(new MarkedText("key_1"), new BytesWritable("2".getBytes(charset))),
      new Pair(new MarkedText("key_2"), new BytesWritable("2".getBytes(charset))),
      new Pair(new MarkedText("key_0"), new BytesWritable("3".getBytes(charset))),
      new Pair(new MarkedText("key_1"), new BytesWritable("3".getBytes(charset))),
      new Pair(new MarkedText("key_2"), new BytesWritable("3".getBytes(charset))),
      new Pair(new MarkedText("key_0"), new BytesWritable("4".getBytes(charset))),
      new Pair(new MarkedText("key_1"), new BytesWritable("4".getBytes(charset))),
      new Pair(new MarkedText("key_2"), new BytesWritable("4".getBytes(charset)))
    )
    new ReduceDriver[MarkedText, MarkedBytesWritable, MarkedText, BytesWritable]()
      .withReducer(new AuthorJoinerStepOne())
      .withInput(key, bytesList)
      .withAllOutput(output)
      .runTest()
  }
}
