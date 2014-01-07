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

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import pl.edu.icm.coansys.disambiguation.auxil.RankedUnionFindPathCompressed;
import pl.edu.icm.coansys.disambiguation.auxil.RelaxedPair;
import pl.edu.icm.coansys.disambiguation.clustering.ClusterElement;

/**
 *
 * @author pdendek
 * @version 1.0
 * @since 2012-08-07
 */
public abstract class CompleteLinkageHACStrategy implements ClusteringStrategy {

    private Comparator<ClusterElement> mainComparator = new Comparator<ClusterElement>() {
        @Override
        public int compare(ClusterElement r1,
                ClusterElement r2) {
            return (int) r2.compareTo(r1);
        }
    };

    /**
     * The method proceeding complete-linkage hierarchical agglomerative
     * clustering over an objects' affinity matrix with the O(N^2*logN)
     * complexity {
     *
     * @see <a
     * href="http://nlp.stanford.edu/IR-book/html/htmledition/time-complexity-of-hac-1.html">the
     * description of the Complete-Linkage Clustering</a>, particulary the
     * figure 17.5}.
     *
     * @param similarities A lower triangular matrix contains affinities between
     * objects, where a positive value inclines similarity and a negative value
     * reflects dissimilarity level.
     * @return An array containing cluster numbers assigned to objects. Two
     * objects sharing the same cluster number may be considered as the same
     * one.
     * @throws Exception
     */
    @Override
    public int[] clusterize(float sim[][]) {
    	
    	if (sim == null){
    		throw new IllegalArgumentException("You are kindly asked to provide similarity matrix that exists");
    	}
        if (sim.length == 1) {
            return new int[]{0};
        }
        if (sim.length == 2) {
            if (sim[1][0] > 0) {
                return new int[]{0, 0};
            }
            return new int[]{0, 1};
        }

        ClusterElement[][] C = new ClusterElement[sim.length][];
        PriorityQueue<ClusterElement> P[] = new PriorityQueue[sim.length];
        int[] I = new int[sim.length];
        List<RelaxedPair> A = new LinkedList<RelaxedPair>();

        //N
        for (int n = 0; n < sim.length; n++) {
            C[n] = new ClusterElement[n];
            //N
            for (int i = 0; i < n; i++) {
                C[n][i] = new ClusterElement(sim[n][i], i);
            }
            //NlogN
            if (n != 0) {
                P[n] = new PriorityQueue<ClusterElement>(4, mainComparator);
                P[n].addAll(Arrays.asList(C[n]));
            }
            I[n] = 1;
        }

        int i1 = -1, i2 = -1;
        //N
        for (int n = 1; n < sim.length; n++) {
            //N
            RelaxedPair rp = argMaxSequenceIndexExcludeSame(P, I);
            if (rp == null) {
                break;
            }
            i1 = rp.a;
            i2 = rp.b;

            if (i1 == i2) {
                throw new InternalError("Self-similarity detected! "
                        + "As it is considered impossible please investigate code for inconsistencies.");
            }

            A.add(new RelaxedPair(i2, i1));
            I[i2] = 0;
            P[i2] = null;

            for (int i = 0; i < P.length; i++) {
                if (I[i] != 1) {
                    continue;
                }
                if (i == i1) {
                    continue;
                }

                if (i > i1) {
                    P[i].remove(C[i][i1]);
                }
                if (i > i2) {
                    P[i].remove(C[i][i2]);
                }

                if (i1 > i) {
                    P[i1].add(c_i_i1_recalc(C, i, i1, i2));
                } else {
                    P[i].add(c_i_i1_recalc(C, i, i1, i2));
                }
            }
        }
        
        RankedUnionFindPathCompressed rufpc = new RankedUnionFindPathCompressed(I.length);
        
        for (RelaxedPair p : A) {
        	rufpc.union(p.a, p.b);
        }
        
        rufpc.finalUnite();
        return rufpc.getAssignmentArray();
    }

    /*private int getFinalClusterId(int[] I, int i) {
        if (I[i] == i) {
            return I[i];
        }
        return getFinalClusterId(I, I[i]);
    }*/

    private ClusterElement c_i_i1_recalc(ClusterElement[][] C, int i, int i1, int i2) {
        ClusterElement el;
        if (i1 > i && i2 > i) {
            el = C[i1][i];
            el.setSim(SIM(C[i1][i].getSim(), C[i2][i].getSim()));
        } else if (i1 > i && i2 < i) {
            el = C[i1][i];
            el.setSim(SIM(C[i1][i].getSim(), C[i][i2].getSim()));
        } else if (i1 < i && i2 > i) {
            el = C[i][i1];
            el.setSim(SIM(C[i][i1].getSim(), C[i2][i].getSim()));
        } else { //if(i1<i && i2<i)
            el = C[i][i1];
            el.setSim(SIM(C[i][i1].getSim(), C[i][i2].getSim()));
        }
        return el;
    }

    protected RelaxedPair argMaxSequenceIndexExcludeSame(PriorityQueue[] priorityQueue, int[] I) {
        ClusterElement max = null;
        int maxTmp_index = -1;
        float maxSim = Float.MIN_VALUE;
        for (int i = 1; i < priorityQueue.length; i++) {
            if (I[i] != 1) {
                continue;
            }
            if (priorityQueue[i] == null || priorityQueue[i].size() == 0) {
                continue;
            }
            ClusterElement el = (ClusterElement) priorityQueue[i].peek();
            if (max == null || el.getSim() > max.getSim()) {
                maxSim = el.getSim();
                max = el;
                maxTmp_index = i;
            }
        }
        if (max == null) {
            throw new InternalError("No next pair have been selected. "
                    + "This situation should not occure, please inspect code");
        }
        if (maxSim < 0) {
            return null;
        }
        int maxEl_index = max.getIndex();
        RelaxedPair rp = maxTmp_index > maxEl_index
                ? new RelaxedPair(maxTmp_index, maxEl_index)
                : new RelaxedPair(maxEl_index, maxTmp_index);
        priorityQueue[rp.a].poll();
        return rp;
    }

    protected ClusterElement argMaxElementWithConstraints(ClusterElement[] Cn,
            int[] I, int forbidden) {
        float maxval = -1;
        ClusterElement retEl = null;
        for (int i = 0; i < Cn.length; i++) {
            if (i == forbidden) {
                continue;
            }
            if (I[i] != i) {
                continue;
            }
            if (Cn[i].getSim() > maxval) {
                maxval = Cn[i].getSim();
                retEl = Cn[i];
            }
        }
        return retEl;
    }

    protected abstract float SIM(float a, float b);
}
