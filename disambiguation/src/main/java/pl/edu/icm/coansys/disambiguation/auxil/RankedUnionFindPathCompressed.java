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
