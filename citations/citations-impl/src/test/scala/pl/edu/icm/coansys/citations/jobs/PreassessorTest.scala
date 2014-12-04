package pl.edu.icm.coansys.citations.jobs

import collection.JavaConversions._
import org.apache.hadoop.io.{RawComparator, BytesWritable}
import org.apache.hadoop.mrunit.mapreduce.{ReduceDriver, MapDriver, MapReduceDriver}
import org.testng.annotations.Test
import pl.edu.icm.coansys.citations.data._
import pl.edu.icm.coansys.citations.mappers.PreAssesor
import pl.edu.icm.coansys.citations.reducers.TopSelector

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class PreassessorTest {
  val cit1 = MatchableEntity.fromParameters(id = "cit1", rawText = "Jan Kowalski").data.toByteArray
  val doc0 = MatchableEntity.fromParameters(id = "doc0", author = "Alojzy Krasinski").data.toByteArray
  val doc1 = MatchableEntity.fromParameters(id = "doc1", author = "Jan Kowalski").data.toByteArray
  val doc2 = MatchableEntity.fromParameters(id = "doc2", author = "Maciej Nowak").data.toByteArray


  @Test(groups = Array("fast"))
  def mapperTest() {
    val driver = MapDriver.newMapDriver(new PreAssesor)

    for (i <- 0 until 50) {
      driver.addInput(new BytesWritable(cit1), new BytesWritable(doc0))
      driver.addInput(new BytesWritable(cit1), new BytesWritable(doc2))
      driver.addOutput(new TextNumericWritable("cit1", 0.0), new BytesPairWritable(cit1, doc0))
      driver.addOutput(new TextNumericWritable("cit1", 0.0), new BytesPairWritable(cit1, doc2))
    }
    for(i <- 0 until 2) {
      driver.addInput(new BytesWritable(cit1), new BytesWritable(doc0))
      driver.addOutput(new TextNumericWritable("cit1", 0.0), new BytesPairWritable(cit1, doc0))
      for(j <- 0 until 50) {
        driver.addInput(new BytesWritable(cit1), new BytesWritable(doc1))
        driver.addOutput(new TextNumericWritable("cit1", 1.0), new BytesPairWritable(cit1, doc1))
      }
      driver.addInput(new BytesWritable(cit1), new BytesWritable(doc2))
      driver.addOutput(new TextNumericWritable("cit1", 0.0), new BytesPairWritable(cit1, doc2))
    }
    for (i <- 0 until 50) {
      driver.addInput(new BytesWritable(cit1), new BytesWritable(doc0))
      driver.addInput(new BytesWritable(cit1), new BytesWritable(doc2))
      driver.addOutput(new TextNumericWritable("cit1", 0.0), new BytesPairWritable(cit1, doc0))
      driver.addOutput(new TextNumericWritable("cit1", 0.0), new BytesPairWritable(cit1, doc2))
    }
    driver.runTest()
  }

  @Test(groups = Array("fast"))
  def mapperByteCopyTest() {
    val driver = MapDriver.newMapDriver(new PreAssesor)

    for(i <- 0 until 2) {
      driver.addInput(new BytesWritable(cit1), new BytesWritable(doc0))
      driver.addOutput(new TextNumericWritable("cit1", 0.0), new BytesPairWritable(cit1, doc0))
      for(j <- 0 until 50) {
        driver.addInput(new BytesWritable(cit1), new BytesWritable(doc1))
        driver.addOutput(new TextNumericWritable("cit1", 1.0), new BytesPairWritable(cit1, doc1))
      }
      driver.addInput(new BytesWritable(cit1), new BytesWritable(doc2))
      driver.addOutput(new TextNumericWritable("cit1", 0.0), new BytesPairWritable(cit1, doc2))
    }

    driver.runTest()
  }

  @Test(groups = Array("fast"))
  def noVariableReusabilityTest() {
    val driver = ReduceDriver.newReduceDriver(new TopSelector)
    val values = List(
      new BytesPairWritable(cit1, doc0),
      new BytesPairWritable(cit1, doc1),
      new BytesPairWritable(cit1, doc2)
    )
    driver.addInput(new TextNumericWritable("cit1", 1.0), values)
    driver.addOutput(new BytesWritable(cit1), new BytesWritable(doc0))
    driver.addOutput(new BytesWritable(cit1), new BytesWritable(doc1))
    driver.addOutput(new BytesWritable(cit1), new BytesWritable(doc2))

    driver.runTest()
  }

  @Test(groups = Array("fast"))
  def jobTest() {
    val driver = new MapReduceDriver[BytesWritable, BytesWritable, TextNumericWritable, BytesPairWritable, BytesWritable, BytesWritable]()
    driver.withMapper(new PreAssesor)
    driver.setKeyGroupingComparator((new TextNumericWritableGroupComparator).asInstanceOf[RawComparator[TextNumericWritable]])
    driver.setKeyOrderComparator((new TextNumericWritableSortComparator).asInstanceOf[RawComparator[TextNumericWritable]])
    driver.withReducer(new TopSelector)

    for (i <- 0 until 50) {
      driver.addInput(new BytesWritable(cit1), new BytesWritable(doc0))
      driver.addInput(new BytesWritable(cit1), new BytesWritable(doc2))
    }
    for(i <- 0 until 2) {
      driver.addInput(new BytesWritable(cit1), new BytesWritable(doc0))
      for(j <- 0 until 50)
        driver.addInput(new BytesWritable(cit1), new BytesWritable(doc1))
      driver.addInput(new BytesWritable(cit1), new BytesWritable(doc2))
    }
    for (i <- 0 until 50) {
      driver.addInput(new BytesWritable(cit1), new BytesWritable(doc0))
      driver.addInput(new BytesWritable(cit1), new BytesWritable(doc2))
    }

    for (i <- 0 until 100) {
      driver.addOutput(new BytesWritable(cit1), new BytesWritable(doc1))
    }

    driver.runTest()
  }
}