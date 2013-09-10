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

package pl.edu.icm.coansys.disambiguation.author.pig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DefaultDataBag;
import org.apache.pig.data.DefaultTuple;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.slf4j.LoggerFactory;

import pl.edu.icm.coansys.commons.java.StackTraceExtractor;
import pl.edu.icm.coansys.disambiguation.author.features.disambiguators.DisambiguatorFactory;
import pl.edu.icm.coansys.disambiguation.author.pig.extractor.DisambiguationExtractorFactory;
import pl.edu.icm.coansys.disambiguation.features.Disambiguator;
import pl.edu.icm.coansys.disambiguation.features.FeatureInfo;
import pl.edu.icm.coansys.disambiguation.author.benchmark.TimerSyso;

public class AproximateAND extends EvalFunc<DataBag> {

	private float threshold;
	private PigDisambiguator[] features;
	private FeatureInfo[] featureInfos;
	private float sim[][];
	private Tuple datain[];
	private int N;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AproximateAND.class);
    private boolean rememberSim = true;
    private boolean useIdsForExtractors = false;
    private DisambiguationExtractorFactory extrFactory = new DisambiguationExtractorFactory();
    //benchmark staff
    private boolean isStatistics = false;
    private TimerSyso timer = new TimerSyso();
    private int calculatedSimCounter; 
    private int timerPlayId = 0;
    private int finalClusterNumber = 0;
    private List<Integer>clustersSizes;
    
	public AproximateAND( String threshold, String featureDescription, String rememberSim, String useIdsForExtractors, String printStatistics ) throws Exception{
		this.threshold = Float.parseFloat(threshold);
		this.rememberSim = Boolean.parseBoolean( rememberSim );
		this.useIdsForExtractors = Boolean.parseBoolean( useIdsForExtractors );
		this.isStatistics = Boolean.parseBoolean( printStatistics );
		
		List<FeatureInfo> FIwithEmpties 
			= FeatureInfo.parseFeatureInfoString(featureDescription);
		List<FeatureInfo> FIFinall = new LinkedList<FeatureInfo>();
		List<PigDisambiguator> FeaturesFinall = new LinkedList<PigDisambiguator>();
		
        DisambiguatorFactory ff = new DisambiguatorFactory();
        Disambiguator d;
        
        //separate features which are fully described and able to use
        for ( FeatureInfo fi : FIwithEmpties ) {
            if ( fi.getDisambiguatorName().equals("") ) {
                //creating default disambugiator
            	d = new Disambiguator();
            	logger.info("Empty disambiguator name. Creating default disambiguator for this feature.");
            }
            if ( fi.getFeatureExtractorName().equals("") ) {
            	logger.error("Empty extractor name in feature info. Leaving this feature.");
            	throw new Exception("Empty extractor name.");
            	//continue;
            }
            d = ff.create( fi );
            if ( d == null ) {
            	//creating default disambugiator
            	//d = new Disambiguator();
            	logger.error("Cannot create disambugiator from given feature info.");
            	throw new Exception("Cannot create disambugiator from given feature info.");
            }
			//wrong max value (would cause dividing by zero)
			if ( fi.getMaxValue() == 0 ){
				logger.warn( "Incorrect max value for feature: " + fi.getFeatureExtractorName() + ". Max value cannot equal 0." );
				throw new Exception("Incorrect max value for feature: " + fi.getFeatureExtractorName() + ". Max value cannot equal 0.");
			}
            
			if ( this.useIdsForExtractors ) {
				fi.setFeatureExtractorName( 
						extrFactory.toExId( fi.getFeatureExtractorName() ) );
			}
			
            FIFinall.add( fi );
            FeaturesFinall.add( new PigDisambiguator( d ) );
        }
        
		this.featureInfos = FIFinall.toArray( new FeatureInfo[ FIFinall.size() ] );
        this.features = 
        		FeaturesFinall.toArray( new PigDisambiguator[ FIFinall.size() ] );
        
        if ( this.isStatistics ) {
        	timer.addMonit( "#NOTSTAT#", "alg", "is sim", "id", "N", "cl no",
        			"sim cntr", "big clst", "time", "list of clusters' sizes" );
        }
	}
	
	/**
	 * @param Tuple with bag: 
	 * {(contribId:chararray, sname:chararray or int, metadata:map[{(chararray or int)}])}
	 * @see org.apache.pig.EvalFunc#exec(org.apache.pig.data.Tuple)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public DataBag exec( Tuple input ) /*throws IOException*/ {

		if ( input == null || input.size() == 0 ) return null;
		try {
			//TODO optional:
			//it would be enough to take as argument only map bag with datagroup.
			//In that case this  function would be proof against table changes.
			//This change should be done during generating tables in pig script.

			DataBag contribs = (DataBag) input.get(0);  //taking bag with contribs

			if ( contribs == null || contribs.size() == 0 ) return null;

			//start benchmark
			if ( isStatistics ) {
				timer.play();
				timer.addMonit(" ");
				calculatedSimCounter = 0;
				finalClusterNumber = 0;
				timerPlayId++;
				clustersSizes = new ArrayList<Integer>();
			}
			
			Iterator<Tuple> it = contribs.iterator();
			N = (int) contribs.size();

			datain = new DefaultTuple[ N ];

			List< Map<String,Object> > contribsT = 
					new ArrayList< Map<String,Object> > ();

			int k = 0;
			//iterating through bag, dumping bug to Tuple array
			while ( it.hasNext() ) { 
				Tuple t = it.next();
				datain[ k++ ] = t;
				contribsT.add( (Map<String, Object>) t.get(2) ); //map with features
				//TODO: ?change map to list (disambiguators are created one by one 
				//as feature does, so both of them will be iterated in the same order).
				//change map to databag in pig script? (memory for keys with 
				//strings of extractors' names would be saved)
				//Note, that now some disambiguators may be omitted. In that case
				//we'll need boolean ( which feature is used, which not ).
				// UP: rather do not do this, because we will lose 
				// universality of data ability (for example, if EXTRACT_CONTRIB_GIVENDATA
				// extracts more features than we use in aproximate
				// OR in some records some features will be omitted (because of e.g. being empty) 
				// there would be crash)
				
				// the thing we can do for sure is to speed up extractors name (keys) 
				// searching in map by: getting hashCodes of extractors names
				// and do map<Int, Object> OR 
				// use identifiers for extractors 
				// (starting in extract_contribdata_givendata), what would be even better
				// (because one character get 1B, hashCode 4b).
			}

			//sim[][] init
			if ( rememberSim ) {
				sim = new float[ N ][];
				for ( int i = 1; i < N; i++ ) {
					sim[i] = new float[i];
	
					for ( int j = 0; j < i; j++ ) {
						sim[i][j] = threshold;
	                }
				}
			}
			
			//clustering ( and sim[][] calculating )
			calculateAffinityAndClustering( contribsT );

			//clusterList[ cluster_id ] = { contribs in cluster.. }
			int[][] clustersArray = splitIntoClusters();
			
			//creating records for each cluster: contribs in cluster, calculated sims
			DataBag ret = createResultingTuples( clustersArray );
			
	        //this action will    A D D    S O M E    I N F O R M A T I O N    T O    T I M E R    M O N I T
			if ( isStatistics ) {				
				Collections.sort( clustersSizes );
				int biggestCluster = clustersSizes.get( clustersSizes.size()-1 );

				//stopping timer for current play (not thread)
				/* STATISTICS DESCRIPTION:
				 * ## #STAT# tag for parser
				 * ## this algorithm name, 
				 * ## is sim matrix created and some sim values stored , 
				 * ## aproximate execution id,
				 * ## number of contribs, 
				 * ## clusters number after aproximate,
				 * ## calculated sim values which are stored 
				 * (note that it doesn't count all calculated - only stored, e.g. 
				 * if 2 contributors are not in the same cluster, 
				 * their sim value would not be stored.)
				 * ## size of biggest cluster after aproximate
				 * ## clusters' sizes list
				 * ## time [s]
				 */
				timer.stop( "#STAT#", "APR", rememberSim, timerPlayId, N, finalClusterNumber,
		        		calculatedSimCounter, biggestCluster, "#time", clustersSizes.toString() );
			}
			
	        //bag: Tuple with (Object with (String (UUID), bag: { Tuple with ( String (contrib ID) ) } ) )
	        return ret;

		}catch(Exception e){
			// Throwing an exception would cause the task to fail.
			logger.error("Caught exception processing input row:\n" 
						+ StackTraceExtractor.getStackTrace(e));
			return null;
		}
	}

	
	
	//Find & Union
	//USE find() to call cluster id for each contributors from clusterAssicuiations tab!
	private int clusterAssociations[], clusterSize[];

	//finding cluster associations
	// o( log*( n ) ) ~ o( 1 )
	private int find( int a ) {
		if ( clusterAssociations[a] == a ) return a;
		int fa = find( clusterAssociations[a] );
		//Correcting cluster associations to the top one during traversing along the path.
		//Path is splited and straight connection with representative of union (cluster id) is made.
		clusterAssociations[a] = fa;
		return fa;
	}

	//cluster representatives union
	//o( 1 )
	private boolean union( int a, int b ) {
		int fa = find( a );
		int fb = find( b );

		if ( fa == fb ) return false;
		//choosing bigger cluster, union representatives in one cluster
		if (clusterSize[fa] <= clusterSize[fb]) {
			clusterSize[fb] += clusterSize[fa];
			clusterSize[fa] = 0; //because cluster with id 'fa' does not exist anymore
			clusterAssociations[fa] = fb;
		}
		else {
			clusterSize[fa] += clusterSize[fb];
			clusterSize[fb] = 0; 
			clusterAssociations[fb] = fa;
		}

		return true;
	}
	
	private void calculateAffinityAndClustering( List< Map<String,Object> > contribsT ) {
		//Find & Union init:		
		clusterAssociations = new int[N];
		clusterSize = new int[N];

		for ( int i = 0; i < N; i++ ) {
			clusterAssociations[i] = i;
			clusterSize[i] = 1;
		}

		//o( n^2 * features.length )
		//Skipping complexity of find because of its low complexity
		//The heuristic is that o( features.length ) would executed less frequently.
		double partial, simil;
		Map<String,Object>mA,mB;
		
		for ( int i = 1; i < N; i++ ) {
			for ( int j = 0; j < i; j++ ) {

				//if i,j are already in one union, we say they are identical
				//and do not calculate precise similarity value
				if ( find( i ) == find( j ) ) {
					if ( rememberSim ) {
						sim[i][j] = Float.POSITIVE_INFINITY;						
					}
					continue;
				}
				
				simil = threshold;
				
				for ( int d = 0; d < features.length; d++ ) {
					//Taking features from each keys (name of extractor = feature name)
					//In contribsT.get(i) there is map we need.
					//From this map (collection of i'th contributor's features)
					//we take Bag with value of given feature.
					//Here we have sure that following Object = DateBag.
					
					mA = contribsT.get(i);
					mB = contribsT.get(j);
					
					//probably map is empty for some contrib
					if ( mA == null || mB == null ){
						continue;
					}
				
					Object oA = mA.get( featureInfos[d].getFeatureExtractorName() );
					Object oB = mB.get( featureInfos[d].getFeatureExtractorName() );
					
					//probably feature does not exist for some contrib
					if ( oA == null || oB == null ){
						continue;
					}
					
					partial = features[d].calculateAffinity( oA, oB );
					partial = partial * featureInfos[d].getWeight();
					
					simil += partial;

        			if ( simil >= 0 && !rememberSim ) {
        				//because we do not remember sim values this time
        				//we can break calculations
        				break;
        			}
				}
				
				//potentially the same contributors
				if ( simil >= 0 ) {
					union( i, j );

					//benchmark
					if ( rememberSim ) calculatedSimCounter++;
				}
				
				if ( rememberSim ) {
					sim[i][j] = (float) simil;
				}
			}
		}
		
		//features = null;
		//featureInfos = null;
		//contribsT = null;
	}

	// o( N )
	private int simIdToClusterId[];
	protected int[][] splitIntoClusters() {
		// cluster[ cluster id ] =  array with contributors' simIds 		
		simIdToClusterId = new int[N];
		int[][] clusters = new int[N][];
		int index[] = new int[N];
		
		for( int i = 0; i < N; i++ ) {
			clusters[i] = new int[ clusterSize[i] ];			
			index[i] = 0;
			//benchmark
			if (clusterSize[i] > 0 && isStatistics ) {
				finalClusterNumber++;
				clustersSizes.add( clusterSize[i] );
			}

		}
		
		int id;
        for ( int i = 0; i < N; i++ ) {
        	id = find( i );
        	clusters[ id ][ index[id] ] = i;
            simIdToClusterId[ i ] = index[id];
            index[id]++;
        }

		return clusters;
	}

	//o ( N * max_cluster_size )
	protected DataBag createResultingTuples( int[][] clusters ) {
		//IdGenerator idgenerator = new UuIdGenerator();
    	DataBag ret = new DefaultDataBag();
    	DataBag contribDatas;
    	DataBag similarities;
    	
    	//iterating through clusters
    	for ( int[] cluster: clusters ) {
    		//skipping empty clusters
    		if ( cluster == null || cluster.length == 0 ) continue;
    		
        	contribDatas = new DefaultDataBag();
        	similarities = new DefaultDataBag();

        	//iterating through contribs (theirs simId) in cluster
        	for ( int i = 0; i < cluster.length; i++ ) {

        		int sidX = cluster[ i ];

        		//simIdToClusterId[ sidX ] = i;
        		contribDatas.add( datain[ sidX ] );

        		if( rememberSim ) {
	        		//adding precise calculated similarity values
	        		//o( cluster_size )
	        		for ( int j = 0; j < i; j++ ) {
	        			int sidY = cluster[ j ];
	
	        			if ( sidX <= sidY ||  simIdToClusterId[ sidX ] <= simIdToClusterId[ sidY ] ) {
	        				String m = "Trying to write wrong data during create tuple: ";
	        				m += ", sidX: " + sidX + ", sidY: " + sidY + ", simIdToClusterId[ sidX ]: " + simIdToClusterId[ sidX ] + ", simIdToClusterId[ sidY ]: " + simIdToClusterId[ sidY ];
	        				throw new IllegalArgumentException( m );
	        			}
	
	        			//if ( sim[ sidX ][ sidY ] != Float.NEGATIVE_INFINITY 
	        			if ( sim[ sidX ][ sidY ] != Float.POSITIVE_INFINITY 
	        					&& sim[ sidX ][ sidY ] != threshold ) {
	        				Object[] clusterTriple = 
	        						new Object[]{ simIdToClusterId[ sidX ], simIdToClusterId[ sidY ], sim[ sidX ][ sidY ] };
	        				similarities.add( TupleFactory.getInstance().newTuple( 
	        						Arrays.asList( clusterTriple ) ) );
	        			}
	        		}
        		}
        	}

        	Object[] to = new Object[]{ contribDatas, similarities };
	        ret.add(TupleFactory.getInstance().newTuple(Arrays.asList(to)));
        }
 
    	//bag with: { bag with date as in input but ordered by clusters, bag with triple similarities }
    	return ret;
	}
}
