package pl.edu.icm.coansys.citations.mappers

import org.testng.annotations.Test
import org.apache.hadoop.mrunit.mapreduce.MapDriver
import org.apache.hadoop.io.{Text, BytesWritable}
import pl.edu.icm.coansys.citations.data.{MatchableEntity, BytesPairWritable, TextNumericWritable}

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class LeftEntityExtractorTest {
  val cit1 = MatchableEntity.fromParameters(id = "cit1", rawText = "Jan Kowalski").data.toByteArray
  val doc0 = MatchableEntity.fromParameters(id = "doc0", author = "Alojzy Krasinski").data.toByteArray
  val doc1 = MatchableEntity.fromParameters(id = "doc1", author = "Jan Kowalski").data.toByteArray
  val doc2 = MatchableEntity.fromParameters(id = "doc2", author = "Maciej Nowak").data.toByteArray

  @Test(groups = Array("fast"))
  def mapperByteCopyTest() {
    val driver = MapDriver.newMapDriver(new LeftEntityExtractor)

    driver.addInput(new BytesWritable(cit1), new BytesWritable(doc0))
    driver.addOutput(new Text("cit1"), new BytesWritable(cit1))
    driver.addInput(new BytesWritable(cit1), new BytesWritable(doc1))
    driver.addOutput(new Text("cit1"), new BytesWritable(cit1))
    driver.addInput(new BytesWritable(cit1), new BytesWritable(doc2))
    driver.addOutput(new Text("cit1"), new BytesWritable(cit1))

    driver.runTest()
  }
}
