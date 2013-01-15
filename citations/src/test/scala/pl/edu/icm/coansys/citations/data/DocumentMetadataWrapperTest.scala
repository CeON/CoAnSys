/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.data

import org.testng.Assert._
import org.testng.annotations.Test
import pl.edu.icm.coansys.importers.models.DocumentProtos.{BasicMetadata, Author, DocumentMetadata}

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class DocumentMetadataWrapperTest {
  @Test(groups = Array("fast"))
  def noAuthorsTest() {
    val meta = DocumentMetadata.newBuilder().setKey("1").setBasicMetadata(BasicMetadata.newBuilder()).build()
    assertEquals(new DocumentMetadataWrapper(meta).normalisedAuthorTokens, Set())
  }

  @Test(groups = Array("fast"))
  def emptyAuthorTest() {
    val author = Author.newBuilder().setKey("1").build()
    val meta = DocumentMetadata.newBuilder().setKey("1")
      .setBasicMetadata(BasicMetadata.newBuilder().addAuthor(author)).build()
    assertEquals(author.getName, "")
    assertEquals(new DocumentMetadataWrapper(meta).normalisedAuthorTokens, Set())
  }

  @Test(groups = Array("fast"))
  def authorWithSomeDataTest() {
    val author = Author.newBuilder().setKey("1").setName("Kowalski").build()
    val meta = DocumentMetadata.newBuilder().setKey("1")
      .setBasicMetadata(BasicMetadata.newBuilder().addAuthor(author)).build()
    assertEquals(new DocumentMetadataWrapper(meta).normalisedAuthorTokens, Set("kowalski"))
  }

  @Test(groups = Array("fast"))
  def specialCharsInNameTest() {
    val author = Author.newBuilder().setKey("1").setName("Kowalski, Jan").build()
    val meta = DocumentMetadata.newBuilder().setKey("1")
      .setBasicMetadata(BasicMetadata.newBuilder().addAuthor(author)).build()
    assertEquals(new DocumentMetadataWrapper(meta).normalisedAuthorTokens, Set("kowalski", "jan"))
  }

  @Test(groups = Array("fast"))
  def abbreviationsTest() {
    val author1 = Author.newBuilder().setKey("1").setName("Qi Lu").build()
    val author2 = Author.newBuilder().setKey("1").setName("J. Kowalski").build()
    val meta = DocumentMetadata.newBuilder().setKey("1")
      .setBasicMetadata(BasicMetadata.newBuilder().addAuthor(author1).addAuthor(author2)).build()
    assertEquals(new DocumentMetadataWrapper(meta).normalisedAuthorTokens, Set("kowalski", "qi", "lu"))
  }
}
