/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.util

import org.testng.Assert._
import org.testng.annotations.Test
import pl.edu.icm.coansys.citations.util.misc.normalizedAuthorTokensFromAuthorList
import pl.edu.icm.coansys.importers.models.DocumentProtos.{BasicMetadata, Author}

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class normalizedAuthorTokensFromAuthorListTest {
  @Test(groups = Array("fast"))
  def noAuthorsTest() {
    val meta = BasicMetadata.newBuilder().build()
    assertEquals(normalizedAuthorTokensFromAuthorList(meta), Set())
  }

  @Test(groups = Array("fast"))
  def emptyAuthorTest() {
    val author = Author.newBuilder().setKey("1").build()
    val meta = BasicMetadata.newBuilder().addAuthor(author).build()
    assertEquals(author.getName, "")
    assertEquals(normalizedAuthorTokensFromAuthorList(meta), Set())
  }

  @Test(groups = Array("fast"))
  def authorWithSomeDataTest() {
    val author = Author.newBuilder().setKey("1").setName("Kowalski").build()
    val meta = BasicMetadata.newBuilder().addAuthor(author).build()
    assertEquals(normalizedAuthorTokensFromAuthorList(meta), Set("kowalski"))
  }

  @Test(groups = Array("fast"))
  def specialCharsInNameTest() {
    val author = Author.newBuilder().setKey("1").setName("Kowalski, Jan").build()
    val meta = BasicMetadata.newBuilder().addAuthor(author).build()
    assertEquals(normalizedAuthorTokensFromAuthorList(meta), Set("kowalski", "jan"))
  }

  @Test(groups = Array("fast"))
  def abbreviationsTest() {
    val author1 = Author.newBuilder().setKey("1").setName("Qi Lu").build()
    val author2 = Author.newBuilder().setKey("1").setName("J. Kowalski").build()
    val meta = BasicMetadata.newBuilder().addAuthor(author1).addAuthor(author2).build()
    assertEquals(normalizedAuthorTokensFromAuthorList(meta), Set("kowalski", "qi", "lu"))
  }
}
