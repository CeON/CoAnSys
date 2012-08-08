/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.disambiguation.author.model.clustering.strategy;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import pl.edu.icm.coansys.disambiguation.author.model.clustering.ClusterElement;
import pl.edu.icm.coansys.disambiguation.auxil.Pair;

/**
 * 
 * @author pdendek
 * @version 1.0
 * @since 2012-08-07
 */
public abstract class CompliteLinkageHACStrategy implements ClusteringStrategy{
	
	/**
	 * method giving hierarchical agglomerative clustering among contributors
	 * @param sim distances between contributors (level of similarity)
	 * 
	 * @return  clusterId assignments to particular groups
	 * 
	 * @author pdendek
	 */
	@Override
	public int[] clusterize(double sim[][]){
		ReversedClusterElement[][] C = new ReversedClusterElement[sim.length][];
		PriorityQueue<ReversedClusterElement> P[] = new PriorityQueue[sim.length];
		int[] I = new int[sim.length];
		List<Pair<Integer,Integer>> A = new LinkedList<Pair<Integer,Integer>>(); 
		
		//N
		for(int n=0;n<sim.length;n++){
			C[n] = new ReversedClusterElement[n];
			//N
			for(int i=0;i<n;i++) C[n][i] = new ReversedClusterElement(sim[n][i],i);
			//NlogN
			P[n].addAll(Arrays.asList(C[n])); 
			I[n]=1;
		}
		
		int i1=-1,i2=-1;
		//N
		for(int n=1;n<sim.length;n++){
			//N
			i1=argMaxSequenceIndexExcludeSame(P,I);
			if(i1==-1) continue;
			i2=I[P[i1].poll().index];
			if(i1==i2) continue;
			
			A.add(new Pair<Integer, Integer>(Math.min(i1,i2),Math.max(i1,i2)));
			I[i2] = 0;
			P[i1] = new PriorityQueue<ReversedClusterElement>();
			
			for(int i = 0; i < P.length;i++){
				if(I[i]!=1) continue;
				if(i==i1) continue;
				
				P[i].remove(C[i][i1]);
				P[i].remove(C[i][i2]);
				
				if(i1>i) P[i1].add(c_i_i1_recalc(C,i,i1,i2));
				else P[i].add(c_i_i1_recalc(C,i,i1,i2));
			}
		}
		
		for(int i = 0;i<I.length;i++) I[i]=i;
		for(Pair<Integer, Integer> p : A) I[p.getY()]=p.getX();
		for(int i = I.length-1;i>=0;i--){
			I[i] = getFinalClusterId(I,i);
		}
		 
		return I;
	}

	private int getFinalClusterId(int[] I, int i) {
		if(I[i]==i) return I[i];
		return getFinalClusterId(I, I[i]);
	}

	private ReversedClusterElement c_i_i1_recalc(ReversedClusterElement[][] C, int i, int i1, int i2) {
		ReversedClusterElement el;
		if(i1>i && i2>i){
			el = C[i1][i];
			el.sim=SIM(C[i1][i].sim, C[i2][i].sim);
		}
		else if(i1>i && i2<i){
			el = C[i1][i];
			el.sim=SIM(C[i1][i].sim, C[i][i2].sim);
		}
		else if(i1<i && i2>i){
			el = C[i][i1];
			el.sim=SIM(C[i][i1].sim, C[i2][i].sim);
		}
		else{ //if(i1<i && i2<i)
			el = C[i][i1];
			el.sim=SIM(C[i][i1].sim, C[i][i2].sim);
		}
		return el;
	}

	protected int argMaxSequenceIndexExcludeSame(PriorityQueue[] priorityQueue, int[] I) {
		if(priorityQueue.length<=1) return -1;
		if(priorityQueue.length==2) return ((ReversedClusterElement)priorityQueue[1].peek()).index;
				
		ReversedClusterElement max = (ReversedClusterElement)priorityQueue[1].peek();
		
		for(int i=1;i<priorityQueue.length;i++){
			if(I[i]!=1) continue;
			ReversedClusterElement el = (ReversedClusterElement) priorityQueue[i].peek();
			if(el.sim > max.sim) max=el;
		}
		return max.index;
	}

	protected ClusterElement argMaxElementWithConstraints(ClusterElement[] Cn,
			int[] I, int forbidden) {
		double maxval = -1;
		ClusterElement retEl = null;
		for(int i=0;i<Cn.length;i++){
			if(i==forbidden)continue;
			if(I[i]!=i)continue;
			if(Cn[i].sim>maxval){
				maxval=Cn[i].sim;
				retEl=Cn[i];
			}
		}
		return retEl;
	}
	
	protected abstract double SIM(double a, double b);
}

class ReversedClusterElement extends ClusterElement{
	
	public ReversedClusterElement(double sim, int index) {
		super(sim, index);
	}

	
	@Override
	public int compareTo(Object o2) {
		if(o2==null) return -1;
		if(!(o2 instanceof ClusterElement)) throw new ClassCastException("" +
				"Comparison between "+this.getClass()+" and "+o2.getClass()+" is illegal!");
		double count = ((ClusterElement)o2).sim-this.sim; 
		if(count>0) return -1;
		else if(count==0) return 0;
		else return 1;
	}
}