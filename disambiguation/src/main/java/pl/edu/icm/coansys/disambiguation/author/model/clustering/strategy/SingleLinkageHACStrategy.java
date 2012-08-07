/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.disambiguation.author.model.clustering.strategy;

import pl.edu.icm.coansys.disambiguation.author.model.clustering.ClusterElement;

/**
 * 
 * @author pdendek
 *
 */
public abstract class SingleLinkageHACStrategy implements ClusteringStrategy{
	

	@Override
	public int[] clusterize(double sim[][]){
		int[] I = new int[sim.length];
		ClusterElement[] nearBestMatch = new ClusterElement[sim.length]; 
		
		ClusterElement[][] C = new ClusterElement[sim.length][];
		//N
		for(int n=0;n<sim.length;n++){
			C[n] = new ClusterElement[n];
			
			double maxSim = -1;
			if(C[n].length!=0) maxSim = sim[n][0];
			//if all sims are under 0, no of them will be chosen, 
			//so the first is as good as any other
			int maxIndex = 0;
			//N
			for(int i=0;i<n;i++){
				C[n][i] = new ClusterElement(sim[n][i],i);
				if(Math.max(maxSim, sim[n][i])!=maxSim){
					maxSim=sim[n][i];
					maxIndex=i;
				}
			}
			if(C[n].length!=0) nearBestMatch[n]=C[n][maxIndex];
			else nearBestMatch[n]=null;
			I[n]=n;
		}
		
		int i1=-1,i2=-1;
		//N
		for(int n=1;n<sim.length;n++){
			//N
			i1=argMaxSequenceIndexExcludeSame(nearBestMatch,I);
			if(i1==-1) continue;
			i2=I[nearBestMatch[i1].index];
			if(i1==i2) continue;
			double simil = (i1 > i2) ? C[i1][i2].sim : C[i2][i1].sim; 
			if(simil<0) return I;
			//N
			for(int i =0; i<I.length;i++){
				if(I[i]==i && i!=i1 && i!=i2)
					if(i1>i && i2>i)
						C[i1][i].sim=SIM(C[i1][i].sim, C[i2][i].sim);
					else if(i1>i && i2<i)
						C[i1][i].sim=SIM(C[i1][i].sim, C[i][i2].sim);
					else if(i1<i && i2>i)
						C[i][i1].sim=SIM(C[i][i1].sim, C[i2][i].sim);
					else //if(i1<i && i2<i)
						C[i][i1].sim=SIM(C[i][i1].sim, C[i][i2].sim);
				if(I[i]==i2) I[i]=i1;
			}
			//N
			nearBestMatch[i1] = argMaxElementWithConstraints(C[n], I, n);
		}
		return I;
	}

	protected int argMaxSequenceIndexExcludeSame(ClusterElement[] nearBestMatch, int[] I) {
		double maxval=Double.NEGATIVE_INFINITY;
		int maxvalindex=-1;
		
		for(int i=0;i<nearBestMatch.length;i++){
			if(I[i]!=i)continue;
			if(nearBestMatch[i]==null)continue;
			if(i==I[nearBestMatch[i].index])continue;
			if(maxval<nearBestMatch[i].sim){
				maxval=nearBestMatch[i].sim;
				maxvalindex=i;
			}
		}
		return maxvalindex;
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
