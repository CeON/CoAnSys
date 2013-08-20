/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2013 ICM-UW
 * 
 * CoAnSys is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * CoAnSys is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with CoAnSys. If not, see <http://www.gnu.org/licenses/>.
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
