/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.util

import java.io.{FileInputStream, InputStream, File}
import pl.edu.icm.coansys.importers.models.DocumentProtos._
import org.w3c.dom.Node

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object nlm {
  def pubmedNlmToProtoBuf(f: File): DocumentWrapper =
    pubmedNlmToProtoBuf(new FileInputStream(f))

  def authorBuilderFromNameNode(nameNode: Node): Author.Builder = {
    val nameEval = new XPathEvaluator(nameNode)
    val author = Author.newBuilder()
    author.setKey("TODO: FAKE KEY")
    author.setSurname(nameEval( """./surname"""))
    author.setForenames(nameEval( """./given-names"""))
    author
  }

  def referenceMetadataBuilderFromNode(node: Node): ReferenceMetadata.Builder = {
    def orIfEmpty[A](col: TraversableOnce[A], alternative: => TraversableOnce[A]): TraversableOnce[A] = {
      if (!col.isEmpty)
        col
      else
        alternative
    }
    val refEval = new XPathEvaluator(node)
    val refBuilder = ReferenceMetadata.newBuilder()
    refBuilder.setRawCitationText(refEval("."))
    val basicMeta = BasicMetadata.newBuilder()
    basicMeta.addTitle(TextWithLanguage.newBuilder().setText(refEval( """.//article-title""")))
    basicMeta.setDoi(refEval( """.//pub-id[@pub-id-type='doi']"""))
    basicMeta.setJournal(refEval( """.//source"""))
    basicMeta.setYear(refEval( """.//year"""))
    basicMeta.setVolume(refEval( """.//volume"""))
    val fpage = refEval( """.//fpage""")
    val lpage = refEval( """.//lpage""")
    basicMeta.setPages(refEval(fpage + "-" + lpage))
    for (nameNode <- orIfEmpty(refEval.asNodes( """.//person-group[@person-group-type='author']/name"""), refEval.asNodes( """.//name"""))) {
      basicMeta.addAuthor(authorBuilderFromNameNode(nameNode))
    }
    refBuilder.setBasicMetadata(basicMeta)
    refBuilder
  }

  def extIdBuilderFromArticleIdNode(node: Node): KeyValue.Builder = {
    val idEval = new XPathEvaluator(node)
    val idBuilder = KeyValue.newBuilder()
    idBuilder.setKey(idEval("./@pub-id-type"))
    idBuilder.setValue(idEval("."))
    idBuilder
  }

  def abstractBuilderFromNode(node: Node): TextWithLanguage.Builder = {
    val abstractEval = new XPathEvaluator(node)
    val abstractBuilder = TextWithLanguage.newBuilder()
    abstractBuilder.setLanguage(abstractEval("./@lang"))
    abstractBuilder.setText(abstractEval("."))
    abstractBuilder
  }

  def pubmedNlmToProtoBuf(is: InputStream): DocumentWrapper = {
    val meta = DocumentMetadata.newBuilder()
    val eval = XPathEvaluator.fromInputStream(is)
    for (idNode <- eval.asNodes( """/article/front/article-meta/article-id""")) {
      meta.addExtId(extIdBuilderFromArticleIdNode(idNode))
    }
    for (abstractNode <- eval.asNodes( """/article/front/article-meta/abstract""")) {
      meta.addDocumentAbstract(abstractBuilderFromNode(abstractNode))
    }

    val basicMeta = BasicMetadata.newBuilder()
    basicMeta.setDoi(eval( """/article/front/article-meta/article-id[@pub-id-type='doi']"""))
    basicMeta.setPages(eval( """/article/front/article-meta/fpage""") + "-" + eval( """/article/front/article-meta/lpage"""))
    basicMeta.setVolume(eval( """/article/front/article-meta/volume"""))
    basicMeta.setIssue(eval( """/article/front/article-meta/issue"""))
    basicMeta.setJournal(eval( """/article/front/journal-meta/journal-title-group/journal-title"""))
    basicMeta.setYear(eval( """/article/front/article-meta/pub-date/year"""))
    basicMeta.addTitle(TextWithLanguage.newBuilder().setText(eval( """/article/front/article-meta/title-group/article-title""")))

    for (authorNode <- eval.asNodes( """/article/front/article-meta/contrib-group/contrib[@contrib-type='author']""")) {
      val authorEval = new XPathEvaluator(authorNode)
      val nameNode = authorEval.asNode("./name")
      if (nameNode != null)
        basicMeta.addAuthor(authorBuilderFromNameNode(nameNode))
    }
    meta.setBasicMetadata(basicMeta)

    meta.setKey(eval( """/article/front/article-meta/article-id[@pub-id-type='pmid']"""))

    for ((refNode, idx) <- eval.asNodes( """/article/back/ref-list/ref""").zipWithIndex) {
      val refBuilder = referenceMetadataBuilderFromNode(refNode)
      refBuilder.setSourceDocKey(meta.getKey)
      refBuilder.setPosition(idx + 1)
      meta.addReference(refBuilder)
    }

    val wrapper = DocumentWrapper.newBuilder()
    wrapper.setRowId(meta.getKey)
    wrapper.setDocumentMetadata(meta)
    wrapper.build()
  }

  def main(args: Array[String]) {
    val path = """C:\Users\matfed\Desktop\Zookeys_2012_Nov_28_(245)_1-1722.nxml"""
    val is = new FileInputStream(new File(path))
    val meta = pubmedNlmToProtoBuf(is)
    println(meta.toString)
  }
}
