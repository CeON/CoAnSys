/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.indices

import org.apache.hadoop.io.BytesWritable
import pl.edu.icm.coansys.citations.data.Entity

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class EntityIndex(indexFileUri: String) extends SimpleTextIndex[BytesWritable](indexFileUri) {
  def getEntityById(id: String): Entity = {
    Entity.fromTypedBytes(get(id).copyBytes())
  }
}
