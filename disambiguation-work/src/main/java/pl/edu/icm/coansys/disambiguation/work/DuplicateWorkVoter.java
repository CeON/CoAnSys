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
     * Tells whether the given documents are duplicates. The result depends on the {@link DuplicateWorkVoterConfiguration} 
     */
    public boolean isDuplicate(DocumentWrapper doc1, DocumentWrapper doc2) {
        
        if (!sameTitles(doc1, doc2)) {
            return false;
        }
        
        if (!sameFirstAuthors(doc1, doc2)) {
            return false;
        }
        
        return sameYears(doc1, doc2);
    }
    


    //******************** PRIVATE ********************

    
    private boolean sameTitles(DocumentWrapper doc1, DocumentWrapper doc2) {
        
        String title1 = normalizeTitle(doc1);
        String title2 = normalizeTitle(doc2);
        
       
        
        if (!titlesInLevenshteinDistance(title1, title2)) {
            return false;
        }
        
        if (!titleEndsInLevenshteinDistance(title1, title2)) {
            return false;
        }
        
        
        return sameTrailingNumbers(title1, title2);
        
    }



    
    private String normalizeTitle(DocumentWrapper doc1) {
        String title = normalize(DocumentWrapperUtils.getMainTitle(doc1));
        title = replaceLastRomanNumberToDecimal(title);
        title = replaceLastWordNumberToDecimal(title);
        return title;
        
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
    
    
    private boolean sameYears(DocumentWrapper doc1, DocumentWrapper doc2) {
        String doc1year = DocumentWrapperUtils.getPublicationYear(doc1);
        String doc2year = DocumentWrapperUtils.getPublicationYear(doc2);

        // if one of the years is not known then i treat these years as equal
        if (doc1year == null || doc2year == null) {
            return true;
        }
        
        return StringUtils.equalsIgnoreCase(doc1year, doc2year);
        
    }
    


    private boolean titlesInLevenshteinDistance(String title1, String title2) {
        int maxDistance = config.getTitleMaxLevenshteinDistance();
        int distance = StringUtils.getLevenshteinDistance(title1, title2);
        if (distance>maxDistance) {
            return false;
        }
        return true;
    }
   
    
    private boolean titleEndsInLevenshteinDistance(String title1, String title2) {
        if (title1.length() > config.getTitleMostMeaningfulEndLength() &&
            title2.length() > config.getTitleMostMeaningfulEndLength()) {    
            String doc1TitleEnd = title1.substring(title1.length()+1-config.getTitleMostMeaningfulEndLength());
            String doc2TitleEnd = title2.substring(title2.length()+1-config.getTitleMostMeaningfulEndLength());
            if (StringUtils.getLevenshteinDistance(doc1TitleEnd, doc2TitleEnd) > config.getTitleEndMaxLevenshteinDistance()) {
                return false;
            }
        }
        return true;
    }



    // if the title ends with number, the numbers must be the same
    // this way the 'Alice has got a cat part 1' will not be considered the same as
    // 'Alice has got a cat part 2'
    private boolean sameTrailingNumbers(String title1, String title2) {
        
        String doc1TitleTrailingInteger = getTrailingInteger(title1);
        String doc2TitleTrailingInteger = getTrailingInteger(title2);
        
        
        if (doc1TitleTrailingInteger != null && doc2TitleTrailingInteger != null) {
            return doc1TitleTrailingInteger.equals(doc2TitleTrailingInteger);
        
        } else if (doc1TitleTrailingInteger == null && doc2TitleTrailingInteger == null) {
            return true;
        
        } else {
            return false;
        }
    }
    
    

}
