/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.disambiguation.author.clustering.strategies;


/**
 * A method for clustering objects, which similarity is described in affinity matrix.
 *   
 * @author pdendek
 * @version 1.0
 * @since 2012-08-07
 */
public interface ClusteringStrategy extends Cloneable{
	/**
	 * 
	 * @param similarities A lower triangular matrix contains affinities between objects, 
	 * where a positive value inclines similarity and a negative value reflects dissimilarity level.   
	 * @return An array containing cluster numbers assigned to objects. 
	 * Two objects sharing the same cluster number may be considered as the same one.  
	 */
    int[] clusterize(double[][] similarities);
    
}
