/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2015 ICM-UW
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

package pl.edu.icm.coansys.disambiguation.auxil;

import java.util.Arrays;

/**
 * 
 * @author pdendek
 * @since 2013-08-26
 */
public class RankedUnionFindPathCompressed {
	
	private int[] cluster = null;
	private int[] rank = null; 
	
	public RankedUnionFindPathCompressed(int elementNumber){
		cluster = new int[elementNumber];
		for(int i=0;i<elementNumber;i++){
			cluster[i]=i;
		}
		rank = new int[elementNumber];
		Arrays.fill(rank, 0);
	}
	
	public void union(int p,int q){
		int k1 = getRoot(p);
		int k2 = getRoot(q);
		
		if(rank[k1]==rank[k2]){
			rank[k1]++;
			cluster[k2]=k1;
		}else if(rank[k1]>rank[k2]){
			cluster[k1]=k2;
		}else{
			cluster[k2]=k1;
		}
	}
	
	protected int getRootWithoutChange(int p){
	int _p = p;	
            while(_p!=cluster[_p]){
			_p = cluster[_p]; 
		}
		return _p;
	}
	
	protected int getRoot(int p){
		int r = getRootWithoutChange(p);
		int next, _p = p;
		while(_p!=r){
			next=cluster[_p];
			cluster[_p]=r;
			_p=next;
		}
		return _p;
	}
	
	protected int getRoot(int p, boolean[] touched){
		int r = getRootWithoutChange(p);
		int next, _p = p;
		while(_p!=r){
			next=cluster[_p];
			cluster[_p]=r;
			touched[_p]=true;
			_p=next;
		}
		return _p;
	}
	
	public boolean find(int p, int q){
		return getRoot(p)==getRoot(q);
	}

	public int[] getAssignmentArray() {
		return cluster;
	}

	public void finalUnite() {
		boolean[] touched = new boolean[cluster.length];
		Arrays.fill(touched, false);
		for(int i=0;i<cluster.length;i++){
			if(touched[i]) continue;
			getRoot(i,touched);
		}
	}
}
