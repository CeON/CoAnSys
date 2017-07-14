/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
        println(f"Document count: ${documents.size}, tile size $tileSize")
        val ntiles = documents.size/tileSize + (if(documents.size % tileSize>0) 1 else 0)
        println(f"ntiles: $ntiles")
        
        val sdoc = documents.toSeq.sorted(Ordering.by[DocumentWrapper, String](_.getDocumentMetadata.getKey))
        val groupedDocs = sdoc.zipWithIndex.map(docidx => (docidx._2%ntiles, docidx._1)).groupBy[Int](_._1).mapValues(_.map(_._2))
        val res = groupedDocs.flatMap(kv => 
                groupedDocs.map(kvin => new CartesianTaskSplit(
                        clusterId, f"${clusterId}_${kv._1}:${kv._2}",kv._2, kvin._2
                    )
                )
            )
        res.toSeq
    }
}
