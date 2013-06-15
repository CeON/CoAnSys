package pl.edu.icm.coansys.disambiguation.work;

public class DuplicateWorkVoterConfiguration {

    private int maxLevenshteinDistance = 5;

    //******************** GETTERS ********************
    
    /** Defaults to 5 */
    public int getMaxLevenshteinDistance() {
        return maxLevenshteinDistance;
    }

    
    //******************** LOGIC ********************
    
    
    
    
    //******** quasi builder methods */
    
    public static DuplicateWorkVoterConfiguration create() {
        return new DuplicateWorkVoterConfiguration();
    }
    
    public DuplicateWorkVoterConfiguration byMaxLevenshteinDistance(int maxLevenshteinDistance) {
        this.setMaxLevenshteinDistance(maxLevenshteinDistance);
        return this;
    }
    
    
    
    
    //******************** SETTERS ********************
    public void setMaxLevenshteinDistance(int maxLevenshteinDistance) {
        this.maxLevenshteinDistance = maxLevenshteinDistance;
    }
    
}
