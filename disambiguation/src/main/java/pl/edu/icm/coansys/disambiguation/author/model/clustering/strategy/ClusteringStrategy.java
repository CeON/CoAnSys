/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.disambiguation.author.model.clustering.strategy;


/**
 * 
 * @author pdendek
 * @version 1.0
 * @since 2012-08-07
 */
public interface ClusteringStrategy extends Cloneable{

    int[] clusterize(double[][] similarities);
    
}
