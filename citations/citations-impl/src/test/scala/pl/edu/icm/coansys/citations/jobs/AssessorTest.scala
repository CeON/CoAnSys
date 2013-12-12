package pl.edu.icm.coansys.citations.jobs

import pl.edu.icm.coansys.citations.data.{BytesPairWritable, MatchableEntity}
import pl.edu.icm.coansys.citations.data.CitationMatchingProtos.MatchableEntityData
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver
import org.apache.hadoop.io.{Text, BytesWritable}
import pl.edu.icm.coansys.citations.mappers.{EntityAssesor, IdExtractor}
import pl.edu.icm.coansys.citations.reducers.{BestSelector, Distinctor}
import org.testng.Assert._
import org.testng.annotations.Test


/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class AssessorTest {
  val cit1 = MatchableEntity.fromParameters(
    id = "cit1",
    author = "Kogut, J. Ciurej, H.",
    source = "Mechanika",
    title = "Numerical modelling of the train-track axle forces",
    pages = "209--218",
    rawText = "Kogut, J. and Ciurej, H. (2005a). Numerical modelling of the train-track axle forces, Mechanika 222(65): 209-218."
  ).data.toByteArray
  val doc1 = MatchableEntity.fromParameters(
    id = "doc1",
    author = "Kogut, J. Ciurej, H.",
    source = "Zeszyty Naukowe Politechniki Rzeszowskiej. Mechanika",
    title = "Numerical modelling of the train-track axle forces",
    pages = "209-218",
    year = "2005"
  ).data.toByteArray
  val doc0 = MatchableEntity.fromParameters(
    id = "doc0",
    author = "Kogut, J. Ciurej, H.",
    source = "Czasopismo Techniczne. Budownictwo",
    title = "The numerical study of the dynamic train axle forces induced on an uneven railway track " +
      "Modelowanie numeryczne sil dynamicznych wzbudzonych przez nierownosci szyn na styku pojazd szynowy-tor jazdy",
    pages = "35-49",
    year = "2005"
  ).data.toByteArray

  @Test(groups = Array("fast"))
  def jobTest() {

    val driver = new MapReduceDriver[BytesWritable, BytesWritable, Text, Text, Text, Text]()
      .withMapper(new EntityAssesor)
      .withInput(new BytesWritable(cit1), new BytesWritable(doc0))
      .withInput(new BytesWritable(cit1), new BytesWritable(doc1))
      .withInput(new BytesWritable(cit1), new BytesWritable(doc0))
      .withReducer(new BestSelector)
      .withCombiner(new BestSelector)

    val results = driver.run()
    assertEquals(results.size(), 1)
    val elem = results.get(0)
    assertEquals(elem.getFirst.toString, "cit1")
    assertEquals(elem.getSecond.toString.split(":", 2)(1), "doc1")
  }
}
