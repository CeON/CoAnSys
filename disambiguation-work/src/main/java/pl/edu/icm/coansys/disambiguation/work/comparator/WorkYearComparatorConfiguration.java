/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2013 ICM-UW
 * 
 * CoAnSys is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * CoAnSys is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with CoAnSys. If not, see <http://www.gnu.org/licenses/>.
 */

package pl.edu.icm.coansys.disambiguation.work.comparator;

import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper;

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
