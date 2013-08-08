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

package pl.edu.icm.coansys.disambiguation.clustering.strategies;


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
