package pl.edu.icm.coansys.disambiguation.work.comparator;

import static pl.edu.icm.coansys.disambiguation.work.tool.StringTools.normalize;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.coansys.disambiguation.work.DocumentWrapperUtils;
import pl.edu.icm.coansys.importers.models.DocumentProtos.Author;
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentWrapper;

import com.google.common.collect.Lists;

@Service("workAuthorComparator")
public class WorkAuthorComparator {

    public static final String NONAME_SURNAME = "XNONAME";
    
    private WorkJournalComparator workJournalComparator;
        
   
    public boolean sameAuthors(DocumentWrapper doc1, DocumentWrapper doc2) {
        if (sameFirstAuthors(doc1, doc2)) {
            return true;
        }
        
        if (sameNumberOfAuthors(doc1, doc2)) {
            if (!workJournalComparator.sameJournals(doc1, doc2)) {
                return false;
            }
            return commonAuthorNames(doc1, doc2);
        }
        
        return false;
        
    }
    
    
    //******************** PRIVATE ********************
    
    boolean commonAuthorNames(DocumentWrapper doc1, DocumentWrapper doc2) {
        List<String> doc1AuthorNames = extractAuthorNames(doc1);
        List<String> doc2AuthorNames = extractAuthorNames(doc2);
        
        int authorsNumber = Math.min(doc1AuthorNames.size(), doc2AuthorNames.size());
        int commonNamesNumber = CollectionUtils.intersection(doc1AuthorNames, doc2AuthorNames).size();
        if (commonNamesNumber==authorsNumber) {
            return true;
        }
        return false;
    }



    private List<String> extractAuthorNames(DocumentWrapper doc1) {
        List<String> doc1AuthorNames = Lists.newArrayList();
        for (Author author : DocumentWrapperUtils.getAuthors(doc1)) {
            if (!StringUtils.equalsIgnoreCase(author.getSurname(), NONAME_SURNAME)) {
                doc1AuthorNames.add(normalize(author.getSurname()));
            }
        }
        return doc1AuthorNames;
    }


    private boolean sameNumberOfAuthors(DocumentWrapper doc1, DocumentWrapper doc2) {
        if (doc1.getDocumentMetadata().getBasicMetadata().getAuthorCount()==doc2.getDocumentMetadata().getBasicMetadata().getAuthorCount()) {
            return true;
        }
        return false;
    }
    

    private boolean sameFirstAuthors(DocumentWrapper doc1, DocumentWrapper doc2) {
        Author doc1FirstAuthor = DocumentWrapperUtils.getAuthor(doc1, 1);
        Author doc2FirstAuthor = DocumentWrapperUtils.getAuthor(doc2, 1);
        if (doc1FirstAuthor==null || doc2FirstAuthor==null) {
            return false;
        }
        String doc1FirstAuthorLastName = normalize(doc1FirstAuthor.getSurname());
        String doc2FirstAuthorLastName = normalize(doc2FirstAuthor.getSurname());
        if (StringUtils.isBlank(doc1FirstAuthorLastName) || StringUtils.isBlank(doc2FirstAuthorLastName)) {
            return false;
        }
        return StringUtils.equalsIgnoreCase(doc1FirstAuthorLastName.trim(), doc2FirstAuthorLastName.trim());
    }
    

    //******************** SETTERS ********************
    
    @Autowired
    public void setWorkJournalComparator(WorkJournalComparator workJournalComparator) {
        this.workJournalComparator = workJournalComparator;
    }



}
