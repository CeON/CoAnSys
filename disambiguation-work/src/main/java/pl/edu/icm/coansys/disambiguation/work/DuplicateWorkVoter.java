package pl.edu.icm.coansys.disambiguation.work;



import static pl.edu.icm.coansys.disambiguation.work.tool.StringUtils.getTrailingInteger;
import static pl.edu.icm.coansys.disambiguation.work.tool.StringUtils.normalize;
import static pl.edu.icm.coansys.disambiguation.work.tool.StringUtils.replaceLastRomanNumberToDecimal;
import static pl.edu.icm.coansys.disambiguation.work.tool.StringUtils.replaceLastWordNumberToDecimal;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.coansys.importers.models.DocumentProtos.Author;
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentWrapper;

@Service("duplicateWorkVoter")
public class DuplicateWorkVoter {

    private DuplicateWorkVoterConfiguration config;
    
    @Autowired 
    public DuplicateWorkVoter(DuplicateWorkVoterConfiguration config) {
        this.config = config;
    }
    
    
    
    /**
     * Tells whether the given documents are duplicates. The result depends on the {@link DuplicateWorkVoterConfiguration} of the voter
     */
    public boolean isDuplicate(DocumentWrapper doc1, DocumentWrapper doc2) {
        
        boolean isDuplicate = sameTitles(doc1, doc2);
        if (!isDuplicate) {return false;}
        
        isDuplicate = sameFirstAuthors(doc1, doc2);
        if (!isDuplicate) {return false;}
        
        isDuplicate = sameYears(doc1, doc2);
        
        return isDuplicate;
    }


    //******************** PRIVATE ********************

    private boolean sameTitles(DocumentWrapper doc1, DocumentWrapper doc2) {
        int maxDistance = config.getMaxLevenshteinDistance();
        
        String doc1Title = DocumentWrapperUtils.getMainTitle(doc1);
        String doc2Title = DocumentWrapperUtils.getMainTitle(doc2);
        
        doc1Title = normalize(doc1Title);
        doc2Title = normalize(doc2Title);
        
        // 1. check the levenshtein distance < maxDistance
        int distance = StringUtils.getLevenshteinDistance(doc1Title, doc2Title);
        if (distance>maxDistance) {
            return false;
        }
        
        // 2. if the title ends with number, the numbers must be the same
        // this way the 'Alice has got a cat part 1' will not be considered the same as
        // 'Alice has got a cat part 2'
        return sameTrailingNumbers(doc1Title, doc2Title);
        
        
    }

    private boolean sameTrailingNumbers(String doc1Title, String doc2Title) {
        
        doc1Title = replaceLastRomanNumberToDecimal(doc1Title);
        doc2Title = replaceLastRomanNumberToDecimal(doc2Title);
        
        doc1Title = replaceLastWordNumberToDecimal(doc1Title);
        doc2Title = replaceLastWordNumberToDecimal(doc2Title);
        
        Integer doc1TitleTrailingInteger = getTrailingInteger(doc1Title);
        Integer doc2TitleTrailingInteger = getTrailingInteger(doc2Title);
        
        
        if (doc1TitleTrailingInteger != null && doc2TitleTrailingInteger != null) {
            return doc1TitleTrailingInteger.equals(doc2TitleTrailingInteger);
        
        } else if (doc1TitleTrailingInteger == null && doc2TitleTrailingInteger == null) {
            return true;
        
        } else {
            return false;
        }
    }
    
    
    private boolean sameFirstAuthors(DocumentWrapper doc1, DocumentWrapper doc2) {
        Author doc1FirstAuthor = DocumentWrapperUtils.getAuthor(doc1, 1);
        Author doc2FirstAuthor = DocumentWrapperUtils.getAuthor(doc2, 1);
        if (doc1FirstAuthor==null || doc2FirstAuthor==null) {
            return false;
        }
        String doc1FirstAuthorLastName = doc1FirstAuthor.getSurname();
        String doc2FirstAuthorLastName = doc2FirstAuthor.getSurname();
        if (StringUtils.isBlank(doc1FirstAuthorLastName) || StringUtils.isBlank(doc2FirstAuthorLastName)) {
            return false;
        }
        return StringUtils.equalsIgnoreCase(doc1FirstAuthorLastName.trim(), doc2FirstAuthorLastName.trim());
        
    }
    
    private boolean sameYears(DocumentWrapper doc1, DocumentWrapper doc2) {
        String doc1year = DocumentWrapperUtils.getPublicationYear(doc1);
        String doc2year = DocumentWrapperUtils.getPublicationYear(doc2);
        return StringUtils.equalsIgnoreCase(doc1year, doc2year);
        
    }
}
