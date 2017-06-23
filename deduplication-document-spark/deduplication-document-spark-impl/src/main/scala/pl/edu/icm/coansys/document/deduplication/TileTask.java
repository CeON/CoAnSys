/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.icm.coansys.document.deduplication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper;
import scala.Tuple2;

/**
 *
 * @author Aleksander Nowinski <aleksander.nowinski@gmail.com>
 */
public class TileTask implements Serializable {

    String clusterId;
    String taskId;
    List<DocumentWrapper> rows;
    List<DocumentWrapper> columns;

    public TileTask(String clusterId, List<DocumentWrapper> rows, List<DocumentWrapper> columns) {
        this.clusterId = clusterId;
        this.rows = rows;
        this.columns = columns;
    }

    public String getClusterId() {
        return clusterId;
    }

    public String getTaskId() {
        return taskId;
    }

    @Override
    public String toString() {
        return "TileTask{" + "clusterId=" + clusterId + ", taskId=" + taskId + ", rows[" + rows.size() + "], columns[" + columns.size() + "]}";
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.clusterId);
        hash = 89 * hash + Objects.hashCode(this.taskId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TileTask other = (TileTask) obj;
        if (!Objects.equals(this.clusterId, other.clusterId)) {
            return false;
        }
        if (!Objects.equals(this.taskId, other.taskId)) {
            return false;
        }
        return true;
    }

    /**
     * Generate list of pairs of the documents, where predicate is conformed, ie
     * function passed returned true. The predicate is assumed to be
     * symmetrical, so it is executed only once on each pair.
     *
     * @param equalityTest predicate which defines whether or no two elements
     * are considered matching (typically equal)
     * @return list of pairs of keys of equal documents (documents where
     * equalityTest returned true), wrappet into a tuple with cluster id.
     */
    public List<Tuple2<String, List<String>>> processPairs(BiPredicate<DocumentWrapper, DocumentWrapper> equalityTest) {
        List<List<String>> raw = new ArrayList<>();
        for (DocumentWrapper row : rows) {
            List<String> equalColums = columns.stream()
                    .filter(
                            column -> (row.getDocumentMetadata().getKey().compareTo(column.getDocumentMetadata().getKey()) < 0)
                            && (equalityTest.test(row, column))
                    ).map(column -> column.getDocumentMetadata().getKey())
                    .collect(Collectors.toList());
            if (!equalColums.isEmpty()) {
                final List<String> rlist = new ArrayList<>(equalColums);
                rlist.add(row.getDocumentMetadata().getKey());
                raw.add(rlist);
            }
        }
        raw = coalesceResult(raw);
        return raw.stream().map(list -> new Tuple2<>(clusterId, list)).collect(Collectors.toList());
    }

    /**
     * Prepare set of tile tasks representing task of comparing the cluster
     * cartesian. It is natural to use this as a flatMap operator.
     *
     * @param clusterId id of the cluster added to the tasks
     * @param docs list of documents to be cross-compared
     * @param tileSize desired size of the single tile task
     * @return list of tasks to be executed in parallel.
     */
    public static List<TileTask> parallelize(String clusterId, Collection<DocumentWrapper> docs, int tileSize) {
        List<DocumentWrapper> d = new ArrayList<>(docs);
        Collections.sort(d, (d1, d2) -> d1.getDocumentMetadata().getKey().compareTo(d2.getDocumentMetadata().getKey()));

        int ntiles = docs.size() / tileSize + (docs.size() % tileSize == 0 ? 0 : 1);
        List portions[] = new List[ntiles];
        for (int i = 0; i < d.size(); i++) {
            int idx = i % ntiles;
            if (portions[idx] == null) {
                portions[idx] = new ArrayList();
            }
            portions[idx].add(d.get(i));
        }
        List<TileTask> res = new ArrayList<>();

        for (int i = 0; i < portions.length; i++) {
            List rows = portions[i];
            for (int j = i; j < portions.length; j++) {
                List columns = portions[j];
                if (rows != null && columns != null) {
                    final TileTask ntask = new TileTask(clusterId, rows, columns);
                    ntask.taskId = String.format("%s_%04d:%04d", clusterId, i, j);
                    res.add(ntask);
                }
            }

        }
        return res;
    }

    /**
     * Combine clusters which have non-empty intersection, so result will be
     * only separate lists.
     *
     * @param clusters lists to combine
     * @return list of the separate clusters, as
     */
    public static List<List<String>> coalesceResult(List<List<String>> clusters) {
        List<Set<String>> all = new ArrayList<>();
        all.addAll(remapToSets(clusters));
        List<List<String>> res = new ArrayList<>();
        while (!all.isEmpty()) {
            Set<String> current = all.remove(0);
            boolean anyChange;
            do {
                anyChange = false;
                ListIterator<Set<String>> li = all.listIterator();
                while (li.hasNext()) {
                    Set<String> next = li.next();
                    if (next.stream().anyMatch(f -> current.contains(f))) {
                        current.addAll(next);
                        li.remove();
                        anyChange = true;
                    }
                }
            } while (anyChange); //necessary, as there may be chain of induced joins
            res.add(new ArrayList(current));
        }
        return res;
    }

    /**
     * Method which merges clusters of the identifiers ensuring that afterwards
     * all clusters where the same element appears are joined into one. It is
     * intended to be used as a reduce operator.
     *
     * @param r1 first list of clusters
     * @param r2 second list of clusters.
     * @return list of clusters.
     */
    public static List<List<String>> coalesceResult(List<List<String>> r1, List<List<String>> r2) {
        List<List<String>> all = new ArrayList<>();
        all.addAll(r1);
        all.addAll(r2);
        return coalesceResult(all);
    }

    protected static List<Set<String>> remapToSets(List<List<String>> r2) {
        return r2.stream().map(x -> new HashSet<>(x)).collect(Collectors.toList());
    }

}
