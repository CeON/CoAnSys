/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.indices

import pl.edu.icm.coansys.citations.util.{misc, BytesIterable}

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class AuthorIndex(val indexFileUri: String, val useDistributedCache: Boolean = true) {
  private val index = new ApproximateIndex[BytesIterable](indexFileUri, useDistributedCache)

  def getDocumentsByAuthor(author: String): Iterable[String] = {
    index.getApproximate(author).flatMap(_.iterable map misc.uuidDecode).toSet
  }

  def close() {
    index.close()
  }

}
