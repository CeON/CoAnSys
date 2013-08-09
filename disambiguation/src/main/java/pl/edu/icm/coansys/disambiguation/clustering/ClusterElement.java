/*
 * (C) 2010-2012 ICM UW. All rights reserved.
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
		double count = this.sim-((ClusterElement)o2).sim; 
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