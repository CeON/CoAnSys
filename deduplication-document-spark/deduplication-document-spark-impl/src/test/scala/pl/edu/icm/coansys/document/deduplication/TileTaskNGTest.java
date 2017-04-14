/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.icm.coansys.document.deduplication;

import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Range;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import static org.testng.Assert.*;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pl.edu.icm.coansys.models.DocumentProtos;
import pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper;
import scala.Tuple2;

/**
 *
 * @author Aleksander Nowinski <aleksander.nowinski@gmail.com>
 */
public class TileTaskNGTest {

    public TileTaskNGTest() {
    }

    @BeforeMethod
    public void setUpMethod() throws Exception {
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
    }

    public DocumentWrapper createDocument(String key, String title) {
        return DocumentWrapper.newBuilder().setDocumentMetadata(
                DocumentMetadata.newBuilder().setKey(key).setBasicMetadata(
                        BasicMetadata.newBuilder().addTitle(
                                DocumentProtos.TextWithLanguage.newBuilder().setText(title)))
        ).setRowId(key).build();
    }

    protected List<DocumentWrapper> createDocumentList(int size) {
        return ContiguousSet.create(Range.closed(1, size), DiscreteDomain.integers()).stream()
                .map(i -> createDocument(String.format("key_%03d", i), String.format("title_%03d", i)))
                .collect(Collectors.toList());
    }

    /**
     * Test of processPairs method, of class TileTask.
     */
    @Test
    public void testProcessPairs() {
        System.out.println("processPairs");
        int size = 500;
        List<DocumentWrapper> documents = createDocumentList(size);
        System.out.println("Generated documents:");
        System.out.println(documents.stream().limit(10).map(v -> v.toString()).reduce(((s1, s2) -> s1 + "\n" + s2)));

        TileTask instance = new TileTask("test", documents, documents);
        List<Tuple2<String, List<String>>> res = instance.processPairs((a, b) -> false);
        
        assertEquals(res.size(), size);
        assertTrue(res.stream().anyMatch(p->p._2.size()==1));
        assertEquals(res.stream().map(p->p._2.get(0)).distinct().count(), size);

        
        res = instance.processPairs((a, b) -> true);
        assertEquals(res.size(), 1);
        assertEquals(res.get(0)._2.size(), size);
        
        
        
        assertTrue(res.stream().allMatch(x->x._1().equals(instance.getClusterId())));

        res = instance.processPairs(
                (a, b) -> {
                    String k1 = a.getDocumentMetadata().getKey();
                    String k2 = b.getDocumentMetadata().getKey();
                    String k1s = k1.substring(5);
                    String k2s = k2.substring(5);
                    return k1s.equals(k2s);
                });
//        System.out.println("Generated pairs:");
//        System.out.println(res.stream().map(v -> v.toString()).reduce(((s1, s2) -> s1 + ", " + s2)));
        assertTrue(res.stream().allMatch(x->x._1().equals(instance.getClusterId())));
        assertEquals(res.size(), 100);
        
        res.stream().map(x->x._2).forEach(
                x->{ 
                    String ke = x.get(0).substring(5);
                    assertTrue(x.stream().allMatch(key->key.endsWith(ke)));
                }
        );

    }

    
    private Set<String> toKeySet(List<DocumentWrapper> wrappers) {
        return wrappers.stream().map(x->x.getDocumentMetadata().getKey()).collect(Collectors.toSet());
    }
    
    /**
     * Test of parallelize method, of class TileTask.
     */
    @Test
    public void testParallelize() {
        System.out.println("parallelize");
        List<DocumentWrapper> docs = createDocumentList(9);
        String clusterId = "cluster";
        List<TileTask> res = TileTask.parallelize(clusterId, docs, 200);
        assertEquals(res.size(), 1, "Created too many tasks.");
        
        res = TileTask.parallelize(clusterId, docs, 5);
        assertEquals(res.size(), 3, "Created invalid number of tasks.");
        Set<String> tile0r = toKeySet(res.get(0).rows);
        Set<String> tile0c = toKeySet(res.get(0).columns);
        assertEquals(tile0r, tile0c);
        
        Set<String> tile1r = toKeySet(res.get(1).rows);
        Set<String> tile1c = toKeySet(res.get(1).columns);
        tile1r.stream().forEach(
                key->assertFalse(tile1c.contains(key),"In tile 1 key "+key+" from row appears in columns.")
        );
        
        res = TileTask.parallelize(clusterId, docs, 2);
        assertEquals(res.size(), 15, "Created invalid number tasks.");
        res = TileTask.parallelize(clusterId, docs, 1);
        assertEquals(res.size(), 45, "Created too many tasks.");
    }

    /**
     * Test of coalesceResult method, of class TileTask.
     */
    @Test
    public void testCoalesceResult() {
        System.out.println("coalesceResult");
        List<List<String>> r1 = Arrays.asList(new List[] {
            Arrays.asList(new String[]{"a", "b"}),
            Arrays.asList(new String[]{"c", "d"}),
            Arrays.asList(new String[]{"e", "f"}),
        });
        List<List<String>> r2 = Arrays.asList(new List[] {
            Arrays.asList(new String[]{"a", "c"}),
        });
        
        List<List<String>> result = TileTask.coalesceResult(r1, r2);
        assertEquals(2, result.size());
        List<String> l1 = result.get(0);
        Collections.sort(l1);
        assertEquals(l1, 
                Arrays.asList("a", "b", "c", "d"));
    }


}
