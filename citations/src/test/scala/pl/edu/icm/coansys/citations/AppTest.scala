package pl.edu.icm.coansys.citations

import org.testng.Assert._
import org.testng.annotations.Test

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class AppTest {
  @Test(groups = Array("fast"))
  def fooTest() {
    assertEquals("alamakota", App.foo(Array("ala", "ma", "kota")))
  }
}
