package pl.edu.icm.coansys.disambiguation.work.comparator;

import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentWrapper;

/**
 * A set of configuration options influencing the result of 
 * {@link WorkYearComparator#sameYears(DocumentWrapper, DocumentWrapper)} 
 * 
 * @author lukdumi
 *
 */
public class WorkYearComparatorConfiguration {

    private int publicationYearMaxDistance = 0;

   
    
    //******************** GETTERS ********************
    
    /**
     * Max difference between publication years <br/>
     * eg. <br/>
     * 0 -> the years must be the same, 2012 will not be considered the same as 2013 <br/>
     * 10 -> 2013 will be considered the same as 2003
     */
    public int getPublicationYearMaxDistance() {
        return publicationYearMaxDistance;
    }

        

    //******************** SETTERS ********************
    
    public void setPublicationYearMaxDistance(int publicationYearMaxDistance) {
        this.publicationYearMaxDistance = publicationYearMaxDistance;
    } 
    

    
}
