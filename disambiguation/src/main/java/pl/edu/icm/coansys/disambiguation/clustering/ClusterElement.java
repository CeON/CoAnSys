/*
 * This file is part of CoAnSys project.
 * Copyright (c) 20012-2013 ICM-UW
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

package pl.edu.icm.coansys.disambiguation.clustering;

/**
 * A container gathering all informations about a similarity matrix cell 
 * 
 * @author pdendek
 * @version 1.0
 * @since 2012-08-07
 */

public class ClusterElement implements Comparable<Object>{
	private float sim;
	private int index;
	
	public ClusterElement(float sim, int index){
		this.sim=sim;
		this.index=index;
	}
        
        public int getIndex() {
            return index;
        }
	
        public void setIndex(int index) {
            this.index = index;
        }
        
        public float getSim() {
            return sim;
        }

        public void setSim(float sim) {
            this.sim = sim;
        }
        
	@Override
	public int compareTo(Object o2) {
		if(o2==null) {
                    return 1;
                }
		if(!(o2 instanceof ClusterElement)) {
                    throw new ClassCastException("" +
				"Comparison between "+this.getClass()+" and "+o2.getClass()+" is illegal!");
                }
		float count = this.sim-((ClusterElement)o2).sim; 
		if(count>0) {
                    return 1;
                }
		else if(count==0) {
                    return 0;
                }
		else {
                    return -1;
                }
	}
}