/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2017 ICM-UW
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
package pl.edu.icm.coansys.document.deduplication

import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper

class CartesianTaskSplit(
    val clusterId: String,
    val taskId: String,
    val rows: Seq[DocumentWrapper],
    val columns: Seq[DocumentWrapper]
) {
    /**
     * Generate list of clusters of the documents, where predicate is conformed, ie
     * function passed returned true. The predicate is assumed to be
     * symmetrical, so it is executed only once on each pair. Note, that as we
     * expect that all the tiles will appear within the task, and the comparison
     * operator may be expensive, only situations where row key is lesser than
     * column key are taken into account
     *
     * @param equalityTest predicate which defines whether or no two elements
     * are considered matching (typically equal)
     * @return list of lists of keys of equal documents (documents where
     * equalityTest returned true)
     */
    def processPairs(equalityTest: (DocumentWrapper, DocumentWrapper) => Boolean): Seq[Seq[String]] = {
        return List.empty

        val clusters: Seq[Seq[String]] = rows.map(row => {
                val rkey = row.getDocumentMetadata.getKey
                val equalColumnKeys = columns.filter(rkey < _.getDocumentMetadata.getKey)
                .filter(equalityTest(row, _))
                .map(_.getDocumentMetadata.getKey)
                equalColumnKeys :+ rkey
            }).filter(_.size > 1)
        CartesianTaskSplit.coalesceClusters(clusters)
    }

}

object CartesianTaskSplit {
  val log = org.slf4j.LoggerFactory.getLogger(getClass().getName())
    /**
     * Combine clusters which have non-empty intersection, so result will be
     * only separate lists.
     *
     * @param clusters lists to combine
     * @return list of the separate clusters, obtained from merging input clusters
     */
    def coalesceClusters(clusters: Seq[Seq[String]]): Seq[Seq[String]] = {
        var sets = clusters.map(_.toSet[String])
        var res = List.empty[Set[String]]
        while (!sets.isEmpty) {
            var current = sets.head
            sets = sets.tail
            var ps: (Seq[Set[String]], Seq[Set[String]]) = null
            do {
                ps = sets.partition(_.exists(current.contains(_)))
                current +: ps._1.flatMap(x => x)
                sets = ps._2
            } while (!ps._1.isEmpty)
            res :+ current
        }
        res.map(_.toSeq)
    }

    /** Split one large cluster into parallel tasks of the given size.
    */
    def parallelizeCluster(clusterId: String, documents: Iterable[DocumentWrapper], tileSize: Int): Seq[CartesianTaskSplit] = {
        log.info(f"Document count: ${documents.size}, tile size $tileSize")
        val ntiles = documents.size/tileSize + (if(documents.size % tileSize>0) 1 else 0)
        println(f"ntiles: $ntiles")
        
        val sdoc = documents.toVector.sorted(Ordering.by[DocumentWrapper, String](_.getDocumentMetadata.getKey))
        val groupedDocs = sdoc.zipWithIndex.map(docidx => (docidx._2%ntiles, docidx._1)).groupBy[Int](_._1).mapValues(_.map(_._2).toVector).toVector
        val res = groupedDocs.flatMap(kv => 
                groupedDocs.map(kvin => new CartesianTaskSplit(
                        clusterId, f"${clusterId}_${kv._1}:${kv._2}",kv._2, kvin._2
                    )
                )
            )
        res
    }
}
