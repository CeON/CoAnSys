package pl.edu.icm.coansys.disambiguation.author.pig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.pig.EvalFunc;
import org.apache.pig.backend.executionengine.ExecException;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DefaultDataBag;
import org.apache.pig.data.DefaultTuple;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.slf4j.LoggerFactory;

import pl.edu.icm.coansys.commons.java.StackTraceExtractor;
import pl.edu.icm.coansys.disambiguation.author.features.disambiguators.DisambiguatorFactory;
import pl.edu.icm.coansys.disambiguation.features.Disambiguator;
import pl.edu.icm.coansys.disambiguation.features.FeatureInfo;

// zmiany w stosunku do pierwotnego Exhaustive:
// - poslugiwanie sie nadanymi identyfikatorami int zamiast string dla kazdego kontrybutora
// zamiana mapy na tablice - zbicie log'a + porownywanie stringow / ich hashy, ktore trzeba generowac jest dluzsze


public class AproximateAND extends EvalFunc<DataBag> {

	private double threshold;
	private PigDisambiguator[] features;
	private List<FeatureInfo> featureInfos;
	private double sim[][];
	private Tuple datain[];
	private int N;
	//TODO: możliwie pozamieniac listy na tablice
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AproximateAND.class);

	public AproximateAND(String threshold, String featureDescription){
		this.threshold = Double.parseDouble(threshold);
        this.featureInfos = FeatureInfo.parseFeatureInfoString(featureDescription);
        DisambiguatorFactory ff = new DisambiguatorFactory();

        int featureNum = 0;
        for ( FeatureInfo fi : featureInfos ){
        	if(!fi.getDisambiguatorName().equals("")) featureNum++;
        }

        this.features = new PigDisambiguator[featureNum];

        int index = -1;
        for ( FeatureInfo fi : featureInfos ){
        	if ( fi.getDisambiguatorName().equals("") ) continue;
        	index++;
        	Disambiguator d = ff.create(fi);
        	features[index] = new PigDisambiguator(d);
        }
	}

	/*
	 * Tuple: sname,{(contribId:chararray,contribPos:int,sname:chararray, metadata:map[{(chararray)}])},count
	 */
	@SuppressWarnings("unchecked")
	@Override
	public DataBag exec( Tuple input ) throws IOException {

		if ( input == null || input.size() == 0 ) return null;
		try {
			//UWAGA: input aproximate musi byc taki sam jak exhaustive (bo z aproximate trafi do exhaustive wprost)
			//bag: contribId, pozycja - po co (nie potrzebne ani w aproximate ani w exhaustive)?,sname,mapa: <extractor,bag: tuple ze stringiem>,
			//TODO w razie nudy:
			//wystarczyloby wrzucac do udf'a tylko (mape z metadanymi) z datagroup
			//co daje odpornosc na zmiany struktury calej tabeli
			//ale to bym musial zmienic na poziomie generowania tabel (nie generowac z niepotrzebnymi danymi)


			DataBag contribs = (DataBag) input.get(0);  //biore bag'a z kontrybutorami

			if ( contribs == null || contribs.size() == 0 ) return null;

			Iterator<Tuple> it = contribs.iterator();	//iterator po bag'u
			datain = new DefaultTuple[ (int) contribs.size() ];

			List< Map<String,Object> > contribsT = new LinkedList< Map<String,Object> > ();

			int k = 0;
			while ( it.hasNext() ) { //iteruje sie po bag'u, zrzucam bag'a do tablicy Tupli
				Tuple t = it.next();
				datain[ k++ ] = t;
				contribsT.add( (Map<String, Object>) t.get(3) ); //do contribs wedruje mapa z featurami
				//TODO: zrzucic mape na liste, bo disambiguatory tworze po kolei tak jak sa w mapie, wiec po cechach tez bede iterowal sie po kolei
			}

			N = contribsT.size();

			//inicjuje sim[][]
			sim = new double[ N ][];
			for ( int i = 1; i < N; i++ ) {
				sim[i] = new double[i];

				for ( int j = 0; j < i; j++ ) {
					sim[i][j] = threshold;
                }
			}

			//obliczam sim[][]
			calculateAffinityAndClustering( contribsT );

			//clusterList[ cluster_id ] = { contribs in cluster.. }
	        List < ArrayList<Integer> >  clusterList = splitIntoClusters();
	        
	        //zwraca bag: Tuple z (Obiektem z (String (UUID) i bag: { Tuple z ( String (contrib ID) ) } ) )
	        return createResultingTuples( clusterList );

		}catch(Exception e){
			// Throwing an exception will cause the task to fail.
			logger.error("Caught exception processing input row:\n" + StackTraceExtractor.getStackTrace(e));
				return null;
		}

		//return new DefaultDataBag();
	}

	//Find & Union
	//To call cluster id for each contributors from clusterAssicuiations tab - use find()!
	private int clusterAssociations[], clusterSize[];

	//finding cluster associations
	// o( log*( n ) ) ~ o( 1 )
	private int find( int a ) {
		if ( clusterAssociations[a] == a ) return a;
		int fa = find( clusterAssociations[a] );
		//w trakcie wedrowania po sciezce poprawiam przedstawiciela na tego na samym szczycie
		//dzieki czemu sciezka jest rozbijana i tworzone sa bezposrednie polaczenia z przedstawicielem
		clusterAssociations[a] = fa;
		return fa;
	}

	//cluster reprezentants union
	//o( 1 )
	private boolean union( int a, int b ) {
		int fa = find( a );
		int fb = find( b );

		if ( fa == fb ) return false;
		//wybieram wieksza unie i lacze przedstawicieli w jedna unie
		if (clusterSize[fa] <= clusterSize[fb]) {
			clusterSize[fb] += clusterSize[fa];
			clusterAssociations[fa] = fb;
		}
		else {
			clusterSize[fa] += clusterSize[fb];
			clusterAssociations[fb] = fa;
		}

		return true;
	}

	private void calculateAffinityAndClustering( List< Map<String,Object> > contribsT ) throws ExecException {
		//Find & Union init:		
		clusterAssociations = new int[N];
		clusterSize = new int[N];

		for ( int i = 0; i < N; i++ ) {
			clusterAssociations[i] = i;
			clusterSize[i] = 1;
		}

		//o( n^2 * features.length )
		//pomijam zlozonosc find bo ~ 1
		//nadzieja jest taka, ze o( features.length ) wykona sie rzadko
		for ( int i = 1; i < N; i++ ) {
			for ( int j = 0; j < i; j++ ) {

				//jesli są już z sobą w uni, to zakladam, ze są to ci sami i nie wyliczam dokladnej wartosci dla tej pary:
				if ( find( i ) == find( j ) ) {
					sim[i][j] = Double.POSITIVE_INFINITY;
					continue;
				}

				for ( int d = 0; d < features.length; d++ ) {

					FeatureInfo featureInfo = featureInfos.get(d);

					//ponizsze implikuje nie stworzenie obiektu disambiguatora
					//pod features[d]
					if ( featureInfo.getDisambiguatorName().equals("") ) continue;

					//pobieranie wartosci (cech) spod danego klucza (nazwy ekstraktora = nazwa cechy)
					//w contribsT.get(i) siedzi interesujaca nas mapa
					//de facto Object = DataBag.
					//Biore z i'tej mapy (zbioru cech i'tego kontrybutora) Bag wartości danej cechy:
					Object oA = contribsT.get(i).get( featureInfo.getFeatureExtractorName() );
					Object oB = contribsT.get(j).get( featureInfo.getFeatureExtractorName() );

					double partial = features[d].calculateAffinity( oA, oB );
					partial = partial / featureInfo.getMaxValue() * featureInfo.getWeight();
					sim[i][j] += partial;

					//prawdopodobnie Ci sami
        			if ( sim[i][j] >= 0 ) {
        				//lacze przedstawicieli i, j w jedna unie
        				union( i, j );
        				break;
        			}
				}
			}
		}
	}

	// o( N )
	protected List < ArrayList<Integer> > splitIntoClusters() {
		
		//TODO: moge wyzylowac i zamiast Vector uzyc tablice, bo z gory znam rozmiary klastrow (clasterSize[])
		List < ArrayList<Integer> > clusters = new ArrayList < ArrayList< Integer > > ();
		// cluster[ id klastra ] = vector  simId kontrybutorow


		for( int i = 0; i < N; i++ )
			clusters.add( new ArrayList<Integer> () );
		
        for ( int i = 0; i < N; i++ ) {
            clusters.get( find( i ) ).add( i );
        }

        //pozbywam sie pustych klastrow
        List < ArrayList<Integer> > ret = new ArrayList < ArrayList< Integer > > ();
		for( int i = 0; i < N; i++ ) {
			if ( !clusters.get( i ).isEmpty() ) {
				ret.add( clusters.get( i ) );
                        }
                }
		return ret;
	}

	//o ( N * max_cluster_size )

	protected DataBag createResultingTuples( List < ArrayList<Integer> > clusters ) {
		//IdGenerator idgenerator = new UuIdGenerator();
    	DataBag ret = new DefaultDataBag();
    	int simIdToClusterId[] = new int[ sim.length ];

    	//iteruje po klastrach
    	for ( ArrayList<Integer> cluster: clusters ) {
        	DataBag contribDatas = new DefaultDataBag();
        	DataBag similarities = new DefaultDataBag();

        	// iteruje po kontrybutorach w klastrze (znajdują sie tam simId)
        	for ( int i = 0; i < cluster.size(); i++ ) {

        		int sidX = cluster.get( i );

        		simIdToClusterId[ sidX ] = i;
        		contribDatas.add( datain[ sidX ] );

        		//dodaje do wyniku wartosci podobienst dla faktycznie obliczonych
        		//o( cluster_size )
        		for ( int j = 0; j < i; j++ ) {
        			int sidY = cluster.get( j );

        			if ( sidX <= sidY ||  simIdToClusterId[ sidX ] <= simIdToClusterId[ sidY ] ) {
        				String m = "Trying to write wrong data during create tuple: ";
        				m += ", sidX: " + sidX + ", sidY: " + sidY + ", simIdToClusterId[ sidX ]: " + simIdToClusterId[ sidX ] + ", simIdToClusterId[ sidY ]: " + simIdToClusterId[ sidY ];
        				throw new IllegalArgumentException( m );
        			}

        			if ( sim[ sidX ][ sidY ] != Double.NEGATIVE_INFINITY && sim[ sidX ][ sidY ] != Double.POSITIVE_INFINITY ) {
        				Object[] clusterTriple = new Object[]{ simIdToClusterId[ sidX ], simIdToClusterId[ sidY ], sim[ sidX ][ sidY ] };
        				similarities.add( TupleFactory.getInstance().newTuple( Arrays.asList( clusterTriple ) ) );
        			}
        		}
        	}

        	Object[] to = new Object[]{ contribDatas, similarities };
	        ret.add(TupleFactory.getInstance().newTuple(Arrays.asList(to)));
        }

		//bag with: { bag z dane jak na wejsciu aproximate odpowiadające kontrybutorom z poszczegolnych klastrow, bag with triple similarities }
    	return ret;
	}
}
