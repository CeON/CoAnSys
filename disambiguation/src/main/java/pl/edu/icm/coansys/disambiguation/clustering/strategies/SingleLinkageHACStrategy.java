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

import pl.edu.icm.coansys.disambiguation.clustering.ClusterElement;

/**
 *
 * @author pdendek
 * @version 1.0
 * @since 2012-08-07
 */
public abstract class SingleLinkageHACStrategy implements ClusteringStrategy {

    /**
     * The method proceeding single-linkage hierarchical agglomerative
     * clustering over an objects' affinity matrix with the O(N^2) complexity {
     *
     * @see <a
     * href="http://nlp.stanford.edu/IR-book/html/htmledition/time-complexity-of-hac-1.html">the
     * description of the Single-Linkage Clustering</a>, particulary the figure
     * 17.6}.
     *
     * @param similarities A lower triangular matrix contains affinities between
     * objects, where a positive value inclines similarity and a negative value
     * reflects dissimilarity level.
     * @return An array containing cluster numbers assigned to objects. Two
     * objects sharing the same cluster number may be considered as the same
     * one.
     */
    @Override
    public int[] clusterize(double sim[][]) {
        int[] I = new int[sim.length];
        ClusterElement[] nearBestMatch = new ClusterElement[sim.length];

        ClusterElement[][] C = new ClusterElement[sim.length][];
        //N
        for (int n = 0; n < sim.length; n++) {
            C[n] = new ClusterElement[n];

            double maxSim = -1;
            if (C[n].length != 0) {
                maxSim = sim[n][0];
            }
            //if all sims are under 0, no of them will be chosen, 
            //so the first is as good as any other
            int maxIndex = 0;
            //N
            for (int i = 0; i < n; i++) {
                C[n][i] = new ClusterElement(sim[n][i], i);
                if (Math.max(maxSim, sim[n][i]) != maxSim) {
                    maxSim = sim[n][i];
                    maxIndex = i;
                }
            }
            if (C[n].length != 0) {
                nearBestMatch[n] = C[n][maxIndex];
            } else {
                nearBestMatch[n] = null;
            }
            I[n] = n;
        }

        int i1 = -1, i2 = -1;
        //N
        for (int n = 1; n < sim.length; n++) {
            //N
            i1 = argMaxSequenceIndexExcludeSame(nearBestMatch, I);
            if (i1 == -1) {
                continue;
            }
            i2 = I[nearBestMatch[i1].getIndex()];
            if (i1 == i2) {
                continue;
            }
            double simil = (i1 > i2) ? C[i1][i2].getSim() : C[i2][i1].getSim();
            if (simil < 0) {
                return I;
            }
            //N
            for (int i = 0; i < I.length; i++) {
                if (I[i] == i && i != i1 && i != i2) {
                    if (i1 > i && i2 > i) {
                        C[i1][i].setSim(SIM(C[i1][i].getSim(), C[i2][i].getSim()));
                    } else if (i1 > i && i2 < i) {
                        C[i1][i].setSim(SIM(C[i1][i].getSim(), C[i][i2].getSim()));
                    } else if (i1 < i && i2 > i) {
                        C[i][i1].setSim(SIM(C[i][i1].getSim(), C[i2][i].getSim()));
                    } else //if(i1<i && i2<i)
                    {
                        C[i][i1].setSim(SIM(C[i][i1].getSim(), C[i][i2].getSim()));
                    }
                }
                if (I[i] == i2) {
                    I[i] = i1;
                }
            }
            //N
            nearBestMatch[i1] = argMaxElementWithConstraints(C[n], I, n);
        }
        return I;
    }

    protected int argMaxSequenceIndexExcludeSame(ClusterElement[] nearBestMatch, int[] I) {
        double maxval = Double.NEGATIVE_INFINITY;
        int maxvalindex = -1;

        for (int i = 0; i < nearBestMatch.length; i++) {
            if (I[i] != i) {
                continue;
            }
            if (nearBestMatch[i] == null) {
                continue;
            }
            if (i == I[nearBestMatch[i].getIndex()]) {
                continue;
            }
            if (maxval < nearBestMatch[i].getSim()) {
                maxval = nearBestMatch[i].getSim();
                maxvalindex = i;
            }
        }
        return maxvalindex;
    }

    protected ClusterElement argMaxElementWithConstraints(ClusterElement[] Cn,
            int[] I, int forbidden) {
        double maxval = -1;
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

    protected abstract double SIM(double a, double b);
}
