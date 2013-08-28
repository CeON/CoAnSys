package pl.edu.icm.coansys.disambiguation.auxil;

import java.util.Arrays;

/**
 * 
 * @author pdendek
 * @since 2013-08-26
 */
public class RankedUnionFindPathCompressed {
	
	int[] cluster = null;
	int[] rank = null; 
	
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
		while(p!=cluster[p]){
			p = cluster[p]; 
		}
		return p;
	}
	
	protected int getRoot(int p){
		int r = getRootWithoutChange(p);
		int next = -1;
		while(p!=r){
			next=cluster[p];
			cluster[p]=r;
			p=next;
		}
		return p;
	}
	
	protected int getRoot(int p, boolean[] touched){
		int r = getRootWithoutChange(p);
		int next = -1;
		while(p!=r){
			next=cluster[p];
			cluster[p]=r;
			touched[p]=true;
			p=next;
		}
		return p;
	}
	
	public boolean find(int p, int q){
		return find(p,q);
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
