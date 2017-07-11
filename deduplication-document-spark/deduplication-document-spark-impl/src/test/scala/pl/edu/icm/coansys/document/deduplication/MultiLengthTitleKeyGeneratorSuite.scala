/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.icm.coansys.document.deduplication

import org.scalatest.FunSuite
import org.scalatest._

class MultiLengthTitleKeyGeneratorSuite extends FunSuite with GivenWhenThen {
  test("cleaning the string") {
    Given("an empty instance")
    val instance = new MultiLengthTitleKeyGenerator(3 to 7)
    When("empty string is given")
    val empty = ""
    Then("result should be empty")
    assertResult("")(instance.cleanUpString(empty))

    When("String has varied case")
    val varcas = "SomeCaseS"
    Then("result should be lowercase")
    assertResult("somecases")(instance.cleanUpString(varcas))

    When("String has spaces")
    val spc = "Some spaces"
    Then("result should be lowercase, no spaces")
    assertResult("somespaces")(instance.cleanUpString(spc))

    When("String has punctuation")
    val pct = "String with \"so called\" - phy - punctuation!"
    Then("result have no punctuation nor spaces")
    assertResult("stringwithsocalledphypunctuation")(instance.cleanUpString(pct))

    When("String has some stopwords")
    val stopwords = "A the long! of short and tall"
    Then("result should contain no stopwords")
    assertResult("longshorttall")(instance.cleanUpString(stopwords))
    info("That's all folks!")
  }

  test("Building the key set") {
    Given("An empty instance with sequence keyset from 1 to 6")
    val instance = new MultiLengthTitleKeyGenerator(1 to 6)
    When("empty string is given")
    val empty = ""
    Then("result should be list with single, empty string element.")
    assert(instance.generateKeys(empty).size==1)
    assert(instance.generateKeys(empty)(0).isEmpty)

    When("Normal string is given")
    val normal = "abcdefghijklmnopqr"
    Then("result array has appropriate lengths")
    val normalRes = instance.generateKeys(normal)
    assert(normalRes.map(_.size).toList == (1 to 6).toList)
    And("result arrray has proper contents.")
    assertResult(List("a", "ac", "ace", "aceg", "acegi", "acegik"))(normalRes.toList)
    
    When("Short string is given")
    val short = "abcdef"
    Then("result array has appropriate lengths")
    val shortRes = instance.generateKeys(short)
    assert(shortRes.map(_.size).toList == (1 to 3).toList)
    assertResult(List("a", "ac", "ace"))(shortRes.toList)
  }
}
