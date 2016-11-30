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

package pl.edu.icm.coansys.disambiguation.author.pig;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.pig.data.DataBag;
import org.apache.pig.data.DefaultDataBag;
import org.apache.pig.data.DefaultTuple;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.tools.pigstats.PigStatusReporter;
import org.slf4j.LoggerFactory;

import pl.edu.icm.coansys.commons.java.StackTraceExtractor;
import pl.edu.icm.coansys.disambiguation.author.benchmark.TimerSyso;
import pl.edu.icm.coansys.disambiguation.model.ContributorWithExtractedFeatures;

public class AproximateAND_BFS extends AND<DataBag> {

    public static class ClusterData {
        public ArrayList<ContributorWithExtractedFeatures> contList=new ArrayList<>();
        public ArrayList<ClusterSimTriple> clusterSimilarities=new ArrayList<>();
    }
    
    
    
	private Tuple datain[];
	private int N;
	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(AproximateAND_BFS.class);

	private boolean rememberSim = true;

	// benchmark staff
	private boolean isStatistics = false;
	private TimerSyso timer = new TimerSyso();
	private int calculatedSimCounter;
	private int timerPlayId = 0;
	private List<Integer> clustersSizes;

	public AproximateAND_BFS(String threshold, String featureDescription,
			String rememberSim, String useIdsForExtractors,
			String printStatistics) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException {
		super(logger, threshold, featureDescription, useIdsForExtractors);
		this.rememberSim = Boolean.parseBoolean(rememberSim);

		this.isStatistics = Boolean.parseBoolean(printStatistics);
		if (this.isStatistics) {
			// alg is sim id N cl no sim cntr big clst time list of clusters'
			// sizes
			timer.addMonit("#NOTSTAT#", "sname", "alg", "is sim", "id", "N",
					"cl no", "sim cntr", "big clst", "time",
					"list of clusters' sizes");
		}

	}

    public List<ClusterData> exec(List<ContributorWithExtractedFeatures> li) /* throws IOException */{
        if (li == null || li.size() == 0) {
			return null;
		}
        return MBFSTyped(li);
        
    }
  
    private  List<Map<String, List<Integer>>> createContribsT(List<ContributorWithExtractedFeatures> li){
          List<Map<String, List<Integer>>> contribsT = new ArrayList<>(
					li.size());
          for (ContributorWithExtractedFeatures c:li) {
              Map<String,List<Integer>> map=new HashMap<>();
              for (Map.Entry<String,Collection<Integer>> ent:c.getMetadata().entrySet()) {
                  ArrayList<Integer> list=new ArrayList<>(ent.getValue());
                  Collections.sort(list);
                  map.put(ent.getKey(), list);
              } 
              contribsT.add( map);
          }
          return contribsT;
    }
    
    	private List<ClusterData> MBFSTyped(List<ContributorWithExtractedFeatures> contribs) {
            int n=contribs.size();
		int[] simIdToClusterId = new int[n];
		final int guard = Integer.MIN_VALUE;
		Deque<Integer> toCluster = new ArrayDeque<Integer>(n + 1);
		List<Integer> clustered = new ArrayList<Integer>(n);
		int idToCluster[] = new int[n];
		int presentClusterId = 0;
		int presentClusterSize = 0;
		int p = 0; // index in clustered of processing node (contributor)

		
        ArrayList<ClusterData> ret=new ArrayList<>();
        
        ClusterData currentClusterData = null;
		List<SimTriple> otherSimilaritiesTriples = new ArrayList<SimTriple>(n);
		SimTriple clusterTriple = null;

		// init
		for (int i = 0; i < n; i++) {
			toCluster.add(i);
		}
		toCluster.add(guard);
        List<Map<String, List<Integer>>>  contribsT=createContribsT(contribs);
		// iterating through all nodes (contributors) to cluster - have not been
		// clustered so far; (>1) because of GUARD
		while (toCluster.size() > 1) {
			// if there are already clustered nodes, we are going to their
			// adjacent nodes, where "adjacent nodes" - all nodes which have not
			// been clustered so far (on toCluster list).
			if (p < clustered.size()) {
				// taking clustered parent, for which we are going to find
				// children
				int v = clustered.get(p++);
				// while exist some unvisited adjacent node
				while (toCluster.getFirst() != guard) {
					// removing node from not clustered nodes queue
					int u = toCluster.pollFirst();

					float simil = calculateContribsAffinityForAllFeaturesaOnSortedLists(
							contribsT, v, u, !rememberSim);

					// creating similarity triple
					if (rememberSim) {
						clusterTriple = new SimTriple(u, v, simil);
					}

					// potentially the same contributors
					if (simil >= 0) {
						clustered.add(u);
						idToCluster[u] = presentClusterId;
						simIdToClusterId[u] = presentClusterSize++;
						currentClusterData.contList.add(contribs.get(u));

						// here we have sure that nodes v and u are in one
						// cluster so we can add sim value to result bag
						if (rememberSim) {
                            
							currentClusterData.clusterSimilarities.add(clusterTriple.toClusterSimTriple(simIdToClusterId));
						}
					} else {
						// putting back the node, because it it has no
						// connection with examined cluster
						toCluster.addLast(u);

						// Nodes v and u are different for now, but we want to
						// remember their similarity in case of adding u to
						// cluster with v in future.
						if (rememberSim) {
							otherSimilaritiesTriples.add(clusterTriple);
						}
					}
				}
				// putting GUARD to the end
				toCluster.add(toCluster.pollFirst());

			} else {
				// there is no possibility to enlarge present cluster, 
				// adding this to result, starting new one
				addClusterToResultBag(idToCluster, presentClusterSize, ret,
						currentClusterData,
						otherSimilaritiesTriples,simIdToClusterId);

				// next cluster begin
                currentClusterData=new  ClusterData();
                otherSimilaritiesTriples.clear();

				int v = toCluster.pollFirst();
				clustered.add(v);
				idToCluster[v] = ++presentClusterId;
				simIdToClusterId[v] = 0;
				presentClusterSize = 1;
				currentClusterData.contList.add(contribs.get(v));
			}
		}

		// add last cluster to result bag
		addClusterToResultBag(idToCluster, presentClusterSize, ret,
				currentClusterData, 
				otherSimilaritiesTriples, simIdToClusterId);

		return ret;
	}
    
    private void addClusterToResultBag(int[] idToCluster,
			int presentClusterSize, List<ClusterData> ret, ClusterData currentCluster,
			List<SimTriple> otherSimilaritiesTriples,int[] simIdToClusterId) {
		if (currentCluster != null) {

			// adding similarities for nodes which had not been
			// connected in first time ( similarity < 0 )
			for (SimTriple t : otherSimilaritiesTriples) {
				// checking if clusters of both nodes are identical.
				// One of them is in examined cluster.
				if (idToCluster[t.v] == idToCluster[t.u]) {
					currentCluster.clusterSimilarities.add(t.toClusterSimTriple(simIdToClusterId));
				}
			}
			pigReporterSizeInfo( "AproximateAND output cluster", currentCluster.contList.size() );
			
			ret.add(currentCluster);

			// benchmark
			if (isStatistics) {
				calculatedSimCounter += currentCluster.clusterSimilarities.size();
				if (presentClusterSize > 1) {
					clustersSizes.add(presentClusterSize);
				}
			}
		}
	}

    
	/**
	 * @param Tuple
	 *            with bag: {(contribId:chararray, sname:chararray or int,
	 *            metadata:map[{(chararray or int)}])}
	 * @see org.apache.pig.EvalFunc#exec(org.apache.pig.data.Tuple)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public DataBag exec(Tuple input) /* throws IOException */{
		if (input == null || input.size() == 0) {
			return null;
		}
		try {
			// instance of reporter may change in each exec(...) run
			// so each time we need to take
			myreporter = PigStatusReporter.getInstance();

			// taking bag with contribs
			DataBag contribs = (DataBag) input.get(0); 

			if (contribs == null || contribs.size() == 0) {
				return null;
			}
			// start benchmark
			if (isStatistics) {
				timer.play();
				timer.addMonit(" ");
				calculatedSimCounter = 0;
				timerPlayId++;
				clustersSizes = new ArrayList<Integer>();
			}

			Iterator<Tuple> it = contribs.iterator();
			N = (int) contribs.size();

			datain = new DefaultTuple[N];

			List<Map<String, Object>> contribsT = new ArrayList<Map<String, Object>>(
					N);

			int k = 0;
			// iterating through bag, dumping bug to Tuple array
			Object sname = null;
			while (it.hasNext()) {
				Tuple t = it.next();
				datain[k++] = t;
				// map with features
				contribsT.add((Map<String, Object>) t.get(2));
				// benchmark
				sname = t.get(1);
			}

			pigReporterSizeInfo("AproximateAND input block", contribsT.size());

			// 1. clustering ( and similarities calculating )
			// 2. creating records for each cluster: contribs in cluster,
			// calculated sims
			DataBag ret = MBFS(contribsT);

			// this action will add some informations to timer monit
			if (isStatistics) {
				Collections.sort(clustersSizes, Collections.reverseOrder());
				int biggestCluster = clustersSizes.isEmpty() ? 1
						: clustersSizes.get(0);

				// stopping timer for current play (not thread)
				/*
				 * STATISTICS DESCRIPTION: ## #STAT# ## smame ## tag for parser
				 * ## this algorithm name ## is sim matrix created and some sim
				 * values stored ## aproximate execution id ## number of
				 * contribs ## clusters number after aproximate ## calculated
				 * sim values which are stored (note that it doesn't count all
				 * calculated - only stored, e.g. if 2 contributors are not in
				 * the same cluster, their sim value would not be stored.) ##
				 * size of biggest cluster after aproximate ## clusters' sizes
				 * list ## time [s]
				 */
				timer.stop("#STAT#", sname, "APR", rememberSim, timerPlayId, N,
						ret.size(), calculatedSimCounter, biggestCluster,
						"#time", clustersSizes.toString());
			}

			return ret;

		} catch (Exception e) {
			// Throwing an exception would cause the task to fail.
			logger.error("Caught exception processing input row:\n"
					+ StackTraceExtractor.getStackTrace(e));
			return null;
		}
	}

	// calculating affinity, clustering and creating result bag
	// N^2 / 2
	// simIdToClusterId[ contrib input index ]= contrib index in his cluster
	//private int simIdToClusterId[];

	private DataBag MBFS(List<Map<String, Object>> contribsT) {

		int simIdToClusterId[] = new int[N];
		final int guard = Integer.MIN_VALUE;
		Deque<Integer> toCluster = new ArrayDeque<Integer>(N + 1);
		List<Integer> clustered = new ArrayList<Integer>(N);
		int idToCluster[] = new int[N];
		int presentClusterId = 0;
		int presentClusterSize = 0;
		int p = 0; // index in clustered of processing node (contributor)

		DataBag ret = new DefaultDataBag();
		DataBag clusterContribDatas = null;
		DataBag clusterSimilarities = null;
		List<SimTriple> otherSimilaritiesTriples = new ArrayList<SimTriple>(N);
		SimTriple clusterTriple = null;

		// init
		for (int i = 0; i < N; i++) {
			toCluster.add(i);
		}
		toCluster.add(guard);

		// iterating through all nodes (contributors) to cluster - have not been
		// clustered so far; (>1) because of GUARD
		while (toCluster.size() > 1) {
			// if there are already clustered nodes, we are going to their
			// adjacent nodes, where "adjacent nodes" - all nodes which have not
			// been clustered so far (on toCluster list).
			if (p < clustered.size()) {
				// taking clustered parent, for which we are going to find
				// children
				int v = clustered.get(p++);
				// while exist some unvisited adjacent node
				while (toCluster.getFirst() != guard) {
					// removing node from not clustered nodes queue
					int u = toCluster.pollFirst();

					float simil = calculateContribsAffinityForAllFeatures(
							contribsT, v, u, !rememberSim);

					// creating similarity triple
					if (rememberSim) {
						clusterTriple = new SimTriple(u, v, simil);
					}

					// potentially the same contributors
					if (simil >= 0) {
						clustered.add(u);
						idToCluster[u] = presentClusterId;
						simIdToClusterId[u] = presentClusterSize++;
						clusterContribDatas.add(datain[u]);

						// here we have sure that nodes v and u are in one
						// cluster so we can add sim value to result bag
						if (rememberSim) {
							clusterSimilarities.add(clusterTriple
									.toClusterTuple(simIdToClusterId));
						}
					} else {
						// putting back the node, because it it has no
						// connection with examined cluster
						toCluster.addLast(u);

						// Nodes v and u are different for now, but we want to
						// remember their similarity in case of adding u to
						// cluster with v in future.
						if (rememberSim) {
							otherSimilaritiesTriples.add(clusterTriple);
						}
					}
				}
				// putting GUARD to the end
				toCluster.add(toCluster.pollFirst());

			} else {
				// there is no possibility to enlarge present cluster, 
				// adding this to result, starting new one
				addClusterToResultBag(idToCluster, presentClusterSize, ret,
						clusterContribDatas, clusterSimilarities,
						otherSimilaritiesTriples,simIdToClusterId);

				// next cluster begin
				clusterContribDatas = new DefaultDataBag();
				clusterSimilarities = new DefaultDataBag();
				otherSimilaritiesTriples.clear();

				int v = toCluster.pollFirst();
				clustered.add(v);
				idToCluster[v] = ++presentClusterId;
				simIdToClusterId[v] = 0;
				presentClusterSize = 1;
				clusterContribDatas.add(datain[v]);
			}
		}

		// add last cluster to result bag
		addClusterToResultBag(idToCluster, presentClusterSize, ret,
				clusterContribDatas, clusterSimilarities,
				otherSimilaritiesTriples,simIdToClusterId);

		return ret;
	}

	private void addClusterToResultBag(int[] idToCluster,
			int presentClusterSize, DataBag ret, DataBag clusterContribDatas,
			DataBag clusterSimilarities,
			List<SimTriple> otherSimilaritiesTriples,
            int simIdToClusterId[]) {
		if (clusterContribDatas != null) {

			// adding similarities for nodes which had not been
			// connected in first time ( similarity < 0 )
			for (SimTriple t : otherSimilaritiesTriples) {
				// checking if clusters of both nodes are identical.
				// One of them is in examined cluster.
				if (idToCluster[t.v] == idToCluster[t.u]) {
					clusterSimilarities.add(t.toClusterTuple(simIdToClusterId));
				}
			}
			pigReporterSizeInfo( "AproximateAND output cluster", clusterContribDatas.size() );
			Object[] to = new Object[] { clusterContribDatas,
					clusterSimilarities };
			ret.add(TupleFactory.getInstance().newTuple(Arrays.asList(to)));

			// benchmark
			if (isStatistics) {
				calculatedSimCounter += clusterSimilarities.size();
				if (presentClusterSize > 1) {
					clustersSizes.add(presentClusterSize);
				}
			}
		}
	}

    
    
	public static class SimTriple {
		private final int v, u;
		private final float sim;

		SimTriple(int v, int u, float sim) {
			this.v = v;
			this.u = u;
			this.sim = sim;
		}

        ClusterSimTriple toClusterSimTriple(int[] simIdToClusterId) {
			int a = Math.min(simIdToClusterId[v], simIdToClusterId[u]);
			int b = Math.max(simIdToClusterId[v], simIdToClusterId[u]);
			return new ClusterSimTriple( b, a, sim );
		}
        
		Object[] toClusterObjectArray(int[] simIdToClusterId) {
			int a = Math.min(simIdToClusterId[v], simIdToClusterId[u]);
			int b = Math.max(simIdToClusterId[v], simIdToClusterId[u]);
			return new Object[] { b, a, sim };
		}

		Tuple toClusterTuple(int[] simIdToClusterId) {
			return TupleFactory.getInstance().newTuple(
					Arrays.asList(toClusterObjectArray(simIdToClusterId)));
		}
	}
}
