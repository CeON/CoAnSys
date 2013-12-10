package pl.edu.icm.coansys.citations.jobs

import org.testng.annotations.Test
import pl.edu.icm.coansys.citations.data.CitationMatchingProtos.MatchableEntityData
import org.apache.hadoop.mrunit.mapreduce.{MapDriver, MapReduceDriver}
import org.apache.hadoop.io.{RawComparator, Text, BytesWritable}
import pl.edu.icm.coansys.citations.data._
import pl.edu.icm.coansys.citations.mappers.{PreAssesor, IdExtractor}
import pl.edu.icm.coansys.citations.reducers.{TopSelector, Distinctor}

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class PreassessorTest {
  @Test(groups = Array("fast"))
  def mapperTest() {
    val cit1 = MatchableEntity.fromParameters(id = "cit1", rawText = "Jan Kowalski").data.toByteArray
    val doc0 = MatchableEntity.fromParameters(id = "doc0", author = "Alojzy Krasinski").data.toByteArray
    val doc1 = MatchableEntity.fromParameters(id = "doc1", author = "Jan Kowalski").data.toByteArray
    val doc2 = MatchableEntity.fromParameters(id = "doc2", author = "Maciej Nowak").data.toByteArray

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
  def jobTest() {
    val cit1 = MatchableEntity.fromParameters(id = "cit1", rawText = "Jan Kowalski").data.toByteArray
    val doc0 = MatchableEntity.fromParameters(id = "doc0", author = "Alojzy Krasinski").data.toByteArray
    val doc1 = MatchableEntity.fromParameters(id = "doc1", author = "Jan Kowalski").data.toByteArray
    val doc2 = MatchableEntity.fromParameters(id = "doc2", author = "Maciej Nowak").data.toByteArray

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