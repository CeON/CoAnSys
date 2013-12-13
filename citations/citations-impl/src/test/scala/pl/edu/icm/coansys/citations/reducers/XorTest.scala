package pl.edu.icm.coansys.citations.reducers

import org.testng.annotations.Test
import org.apache.hadoop.mrunit.mapreduce.{MapReduceDriver, ReduceDriver}
import pl.edu.icm.coansys.citations.data.{MatchableEntity, TextNumericWritable, BytesPairWritable}
import org.apache.hadoop.io.{Text, BytesWritable}
import org.apache.hadoop.mapreduce.Mapper

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class XorTest {
  val cit0 = MatchableEntity.fromParameters(id = "cit0", rawText = "Alojzy Krasinski").data.toByteArray
  val cit1 = MatchableEntity.fromParameters(id = "cit1", rawText = "Jan Kowalski").data.toByteArray
  val cit2 = MatchableEntity.fromParameters(id = "cit2", rawText = "Maciej Nowak").data.toByteArray

  @Test(groups = Array("fast"))
  def basicTest() {
    val driver = MapReduceDriver.newMapReduceDriver(new Mapper[Text, BytesWritable, Text, BytesWritable], new Xor[Text, BytesWritable])

    driver.addInput(new Text("cit0"), new BytesWritable(cit0))
    driver.addInput(new Text("cit1"), new BytesWritable(cit1))
    driver.addInput(new Text("cit2"), new BytesWritable(cit2))
    driver.addInput(new Text("cit0"), new BytesWritable(cit0))
    driver.addInput(new Text("cit2"), new BytesWritable(cit2))
    driver.addInput(new Text("cit0"), new BytesWritable(cit0))
    driver.addOutput(new Text("cit1"), new BytesWritable(cit1))

    driver.runTest()
  }
}
