/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.mappers

import org.apache.hadoop.io.{Text, BytesWritable, Writable}
import org.apache.hadoop.mapreduce.Mapper
import pl.edu.icm.coansys.citations.util.{nlm, XPathEvaluator}
import org.apache.commons.io.IOUtils

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class CitationsFromMixedCitations extends Mapper[Text, Writable, BytesWritable, BytesWritable] {
  type Context = Mapper[Text, Writable, BytesWritable, BytesWritable]#Context
  val writable = new BytesWritable()
  val emptyWritable = new BytesWritable()

  override def map(key: Text, value: Writable, context: Context) {
    val eval = XPathEvaluator.fromInputStream(IOUtils.toInputStream(key.toString))
    val refBuilder = nlm.referenceMetadataBuilderFromNode(eval.asNode("/ref"))
    refBuilder.setSourceDocKey(eval( """/ref//pub-id[@pub-id-type='pmid']"""))
    val ref = refBuilder.build()
    val bytes = ref.toByteArray
    writable.set(bytes, 0, bytes.length)
    context.write(writable, emptyWritable)
  }
}
