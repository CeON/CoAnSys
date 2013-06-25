package pl.edu.icm.coansys.disambiguation.work;

import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentWrapper;

/**
 * A set of configuration options influencing the result of 
 * {@link DuplicateWorkVoter#isDuplicate(DocumentWrapper, DocumentWrapper, DuplicateWorkVoterConfiguration)} 
 * 
 * @author lukdumi
 *
 */
public class DuplicateWorkVoterConfiguration {

    private int titleMaxLevenshteinDistance = 5;
    private int titleMostMeaningfulEndLength = 7;
    private int titleEndMaxLevenshteinDistance = 3;

    //******************** GETTERS ********************
    
    
    
    /** 
     * Max levenshtein distance of the publication title
     * Defaults to 5 
     * 
     * */
    public int getTitleMaxLevenshteinDistance() {
        return titleMaxLevenshteinDistance;
    }

    /**
     * The end of title is of special meaning when considering the equality of the publication.
     * For the end of the title it will be applied additionally {@link #getTitleEndMaxLevenshteinDistance()}
     * 
     * Think about it: <br/>
     * Aspiracje integracyjne państw śródziemnomorskich - Turcja <br/>
     * and <br/>
     * Aspiracje integracyjne państw śródziemnomorskich - Malta <br/>
     * are probably not the duplicates of the same article
     */
    public int getTitleMostMeaningfulEndLength() {
        return titleMostMeaningfulEndLength;
    }
    
    /**
     * The max levenshtein distance of the title end specified by {@link #getTitleMostMeaningfulEndLength()}
     */
    public int getTitleEndMaxLevenshteinDistance() {
        return titleEndMaxLevenshteinDistance;
    }

    


    
    //******************** LOGIC ********************
    
    
    
    
    //******** quasi builder methods */
    
    public static DuplicateWorkVoterConfiguration create() {
        return new DuplicateWorkVoterConfiguration();
    }
    
    public DuplicateWorkVoterConfiguration byTitleMaxLevenshteinDistance(int titleMaxLevenshteinDistance) {
        this.setTitleMaxLevenshteinDistance(titleMaxLevenshteinDistance);
        return this;
    }
    
    
    
    
    //******************** SETTERS ********************
    
    public void setTitleMaxLevenshteinDistance(int titleMaxLevenshteinDistance) {
        this.titleMaxLevenshteinDistance = titleMaxLevenshteinDistance;
    }

    public void setTitleMostMeaningfulEndLength(int titleMostMeaningfulEndLength) {
        this.titleMostMeaningfulEndLength = titleMostMeaningfulEndLength;
    }
    
    public void setTitleEndMaxLevenshteinDistance(int titleEndMaxLevenshteinDistance) {
        this.titleEndMaxLevenshteinDistance = titleEndMaxLevenshteinDistance;
    }
    

    
}
