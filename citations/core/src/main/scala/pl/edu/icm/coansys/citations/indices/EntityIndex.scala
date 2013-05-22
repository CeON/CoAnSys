/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.indices

import org.apache.hadoop.io.BytesWritable
import pl.edu.icm.coansys.citations.data.MatchableEntity
import pl.edu.icm.coansys.citations.data.CitationMatchingProtos.MatchableEntityData

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class EntityIndex(indexFileUri: String, useDistributedCache: Boolean = true) extends SimpleTextIndex[BytesWritable](indexFileUri, useDistributedCache) {
  def getEntityById(id: String): MatchableEntity = {
    new MatchableEntity(MatchableEntityData.parseFrom(get(id).copyBytes()))
  }
}
