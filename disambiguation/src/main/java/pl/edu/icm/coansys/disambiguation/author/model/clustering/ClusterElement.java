/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.disambiguation.author.model.clustering;

/**
 * A container gathering all informations about a similarity matrix cell 
 * 
 * @author pdendek
 * @version 1.0
 * @since 2012-08-07
 */

public class ClusterElement implements Comparable<Object>{
	public double sim;
	public int index;
	
	public ClusterElement(double sim, int index){
		this.sim=sim;
		this.index=index;
	}
	
	@Override
	public int compareTo(Object o2) {
		if(o2==null) return 1;
		if(!(o2 instanceof ClusterElement)) throw new ClassCastException("" +
				"Comparison between "+this.getClass()+" and "+o2.getClass()+" is illegal!");
		double count = this.sim-((ClusterElement)o2).sim; 
		if(count>0) return 1;
		else if(count==0) return 0;
		else return -1;
	}
}