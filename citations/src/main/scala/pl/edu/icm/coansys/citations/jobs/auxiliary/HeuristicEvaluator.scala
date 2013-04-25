/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.jobs.auxiliary

import com.nicta.scoobi.application.ScoobiApp
import com.nicta.scoobi.InputsOutputs._
import pl.edu.icm.coansys.citations.util.XPathEvaluator
import org.apache.commons.io.IOUtils
import org.apache.commons.lang.StringUtils
import com.nicta.scoobi.Persist._
import scala.Some

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object HeuristicEvaluator extends ScoobiApp {
  override lazy val upload = false

  def run() {
    val refsUri = args(0)
    val docsUri = args(1)
    val outUri = args(2)

    val refs = convertValueFromSequenceFile[String](refsUri).flatMap {
      xml =>
        val eval = XPathEvaluator.fromInputStream(IOUtils.toInputStream(xml))
        val id = eval( """.//pub-id[@pub-id-type='pmid']""")
        if (StringUtils.isNotEmpty(id))
          Some(id, Option.apply(xml))
        else
          None
    }
    val docs = convertValueFromSequenceFile[String](docsUri).flatMap {
      xml =>
        val eval = XPathEvaluator.fromInputStream(IOUtils.toInputStream(xml))
        val id = eval( """/article/front/article-meta/article-id[@pub-id-type='pmid']""")
        if (StringUtils.isNotEmpty(id))
          Some(id, Option.empty[String])
        else
          None
    }

    val matchable = (refs ++ docs).groupByKey[String, Option[String]].flatMap {
      case (id, iter) =>
        try {
          val (defined, undefined) = iter.partition(_.isDefined)
          if (!undefined.isEmpty) 
            Stream.continually(id) zip defined.flatten.toStream
          else
            Stream.empty[(String, String)]
        } catch {
          case e: Exception =>
            e.printStackTrace()
            Stream.empty[(String, String)]
        }
    }

    persist(convertToSequenceFile(matchable, outUri, overwrite = true))
  }
}
