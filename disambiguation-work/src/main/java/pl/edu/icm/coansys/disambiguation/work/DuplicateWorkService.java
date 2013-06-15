package pl.edu.icm.coansys.disambiguation.work;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentWrapper;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

@Service("duplicateWorkService")
public class DuplicateWorkService {

    
    @Autowired
    private DuplicateWorkVoter duplicateWorkVoter;
     
    
    
    //******************** LOGIC ********************
    
    public Map<Integer, Set<DocumentWrapper>> findDuplicates(List<DocumentWrapper> documents) {
        Map<Integer, Set<DocumentWrapper>> sameWorksMap = Maps.newHashMap();
        
        List<DocumentWrapper> documentsCopy = Lists.newArrayList(documents);
        
        int i=0;
        for (DocumentWrapper document : documents) {
           
           for (DocumentWrapper other : new ArrayList<DocumentWrapper>(documentsCopy)) {
                
                if (document.getRowId().equals(other.getRowId())) {
                    documentsCopy.remove(other);
                } else {
                    if (duplicateWorkVoter.isDuplicate(document, other, DuplicateWorkVoterConfiguration.create())) {
                        addSameWorks(sameWorksMap, i++, document, other);
                        documentsCopy.remove(other);
                    }
                }
            }    
        }
        return sameWorksMap;
    }
    

    //******************** PRIVATE ********************
    
    private void addSameWorks(Map<Integer, Set<DocumentWrapper>> sameWorksMap, int i, DocumentWrapper document, DocumentWrapper other) {
        Set<DocumentWrapper> sameWorks = sameWorksMap.get(i);
        if (sameWorks==null) {
            sameWorks = Sets.newHashSet();
            sameWorksMap.put(i, sameWorks);
        }
        sameWorks.add(document);
        sameWorks.add(other);
    }
}
