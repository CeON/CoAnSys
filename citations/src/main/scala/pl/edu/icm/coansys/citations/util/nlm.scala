/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.util

import java.io.{StringReader, FileInputStream, InputStream, File}
import pl.edu.icm.coansys.importers.models.DocumentProtos._
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.{XPathConstants, XPathFactory}
import org.xml.sax.{InputSource, EntityResolver}
import org.w3c.dom.{Node, NodeList}

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object nlm {
  def pubmedNlmToProtoBuf(f: File): DocumentWrapper =
    pubmedNlmToProtoBuf(new FileInputStream(f))

  def pubmedNlmToProtoBuf(is: InputStream): DocumentWrapper = {
    class XPathEvaluator(private val obj: Any) extends (String => String) {

      private val evaluator = XPathFactory.newInstance().newXPath()

      def apply(xpath: String): String =
        evaluator.evaluate(xpath, obj)

      def asNode(xpath: String) =
        evaluator.evaluate(xpath, obj, XPathConstants.NODE).asInstanceOf[Node]

      def asNodes(xpath: String) = {
        val nodes = evaluator.evaluate(xpath, obj, XPathConstants.NODESET).asInstanceOf[NodeList]
        for (i <- 0 until nodes.getLength)
        yield nodes.item(i)
      }
    }

    object XPathEvaluator {
      def fromInputStream(is: InputStream) = {
        val docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        docBuilder.setEntityResolver(new EntityResolver {
          def resolveEntity(publicId: String, systemId: String) = new InputSource(new StringReader(""))
        })
        val doc = docBuilder.parse(is)
        new XPathEvaluator(doc)
      }
    }

    def authorBuilderFromNameNode(nameNode: Node): Author.Builder = {
      val nameEval = new XPathEvaluator(nameNode)
      val author = Author.newBuilder()
      author.setKey("TODO: FAKE KEY")
      author.setSurname(nameEval( """./surname"""))
      author.setForenames(nameEval( """./given-names"""))
      author
    }

    def referenceMetadataBuilderFromNode(node: Node): ReferenceMetadata.Builder = {
      val refEval = new XPathEvaluator(node)
      val refBuilder = ReferenceMetadata.newBuilder()
      refBuilder.setRawCitationText(refEval("."))
      val basicMeta = BasicMetadata.newBuilder()
      basicMeta.addTitle(TextWithLanguage.newBuilder().setText(refEval( """./mixed-citation/article-title""")))
      basicMeta.setDoi(refEval( """./mixed-citation/pub-id[@pub-id-type='doi']"""))
      basicMeta.setJournal(refEval( """./mixed-citation/source"""))
      basicMeta.setYear(refEval( """./mixed-citation/year"""))
      basicMeta.setVolume(refEval( """./mixed-citation/volume"""))
      basicMeta.setPages(refEval( """./mixed-citation/fpage""") + "-" + refEval( """./mixed-citation/lpage"""))
      for (nameNode <- refEval.asNodes( """./mixed-citation/person-group[@person-group-type='author']/name""")) {
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

    for ((refNode, idx) <- eval.asNodes( """/article/back/ref-list/ref""").zipWithIndex) {
      val refBuilder = referenceMetadataBuilderFromNode(refNode)
      refBuilder.setSourceDocKey(meta.getKey)
      refBuilder.setPosition(idx + 1)
      meta.addReference(refBuilder)
    }

    meta.setKey(basicMeta.getDoi)

    val wrapper = DocumentWrapper.newBuilder()
    wrapper.setRowId(meta.getKey)
    wrapper.setDocumentMetadata(meta)
    wrapper.build()
  }
}
