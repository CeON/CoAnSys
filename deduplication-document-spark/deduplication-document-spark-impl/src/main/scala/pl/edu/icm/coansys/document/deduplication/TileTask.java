/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.icm.coansys.document.deduplication;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper;
import scala.Tuple2;

/**
 *
 * @author Aleksander Nowinski <aleksander.nowinski@gmail.com>
 */
public class TileTask {
    String clusterId;
    List<DocumentWrapper> rows;
    List<DocumentWrapper> columns;

    public TileTask(String clusterId, List<DocumentWrapper> rows, List<DocumentWrapper> columns) {
        this.clusterId = clusterId;
        this.rows = rows;
        this.columns = columns;
    }
    
    
            
    public List<Tuple2<String, String>> processPairs(BiPredicate<DocumentWrapper, DocumentWrapper> x) {
        List<Tuple2<String, String>> res = new ArrayList<>();
        

        for (DocumentWrapper row : rows) {
            for (DocumentWrapper column : columns) {
                if(row.getDocumentMetadata().getKey().compareTo(column.getDocumentMetadata().getKey())<0){
                    if(x.test(row, column)) {
                        res.add(new Tuple2<>(row.getDocumentMetadata().getKey(), column.getDocumentMetadata().getKey()));
                    }
                }
            }
        }
        
        return res;
    }
    
    /** Prepare set of tile tasks representing task of comparing the cluster cartesian. It is 
     * natural to use this as a flatMap operator.
     * 
     * @param clusterId id of the cluster added to the tasks
     * @param docs list of documents to be cross-compared
     * @param tileSize desired size of the single tile task
     * @return list of tasks to be executed in parallel.
     */
    public static List<TileTask> parallelize(String clusterId, Collection<DocumentWrapper> docs,int tileSize) {
        List<DocumentWrapper> d = new ArrayList<>(docs);
        Collections.sort(d, (d1, d2) -> d1.getDocumentMetadata().getKey().compareTo(d2.getDocumentMetadata().getKey()));
        
        int ntiles = docs.size()/tileSize+(docs.size()%tileSize==0?0:1);
        List portions[] = new List[ntiles];
        for (int i = 0; i < d.size(); i++) {
            int idx = i%ntiles;
            if(portions[idx]==null) {
                portions[idx]=new ArrayList();
            }
            portions[idx].add(d.get(i));
        }
        List<TileTask> res = new ArrayList<>();
        
        
        for (int i = 0; i < portions.length; i++) {
            List rows = portions[i];
            for (int j = i; j < portions.length; j++) {
                List columns = portions[j];
                if(rows!=null && columns!=null) {
                    res.add(new TileTask(clusterId, rows, columns));
                }
            }
            
        }
        return res;
    }
    
    /**
     * Method which merges clusters of the identifiers ensuring that afterwards
     * all clusters where the same element appears are joined into one. It is 
     * intended to be used as a reduce operator.
     * @param r1 first list of clusters
     * @param r2 second list of clusters.
     * @return list of  clusters.
     */
    public static List<List<String>> coalesceResult(List<List<String>> r1, List<List<String>>r2) {
        List<Set<String>> all = new ArrayList<>();
        all.addAll(remapToSets(r1));
        all.addAll(remapToSets(r2));
        List<List<String>> res = new ArrayList<>();
        while(!all.isEmpty()) {
            Set<String> current = all.remove(0);
            boolean anyChange = false;
            do {
                anyChange=false;
                ListIterator<Set<String>> li = all.listIterator();
                while(li.hasNext()) {
                    Set<String> next = li.next();
                    if(next.stream().anyMatch(f->current.contains(f))) {
                        current.addAll(next);
                        li.remove();
                        anyChange=true;
                    }
                }
            } while(anyChange); //necessary, as there may be chain of induced joins
            res.add(new ArrayList(current));
        }
        return res;
    }

    protected static List<Set<String>> remapToSets(List<List<String>> r2) {
        return r2.stream().map(x->new HashSet<String>(x)).collect(Collectors.toList());
    }
    
}
