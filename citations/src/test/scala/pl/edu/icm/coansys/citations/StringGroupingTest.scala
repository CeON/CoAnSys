package pl.edu.icm.coansys.citations

import org.testng.Assert._
import org.testng.annotations.Test

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class StringGroupingTest {
  @Test(groups = Array("fast"))
  def partitionTest() {
    assertEquals(0, StringGrouping.partition("", 4))
    assertEquals(3, StringGrouping.partition("z", 4))
    assertEquals(3, StringGrouping.partition("~", 4))
  }
}
