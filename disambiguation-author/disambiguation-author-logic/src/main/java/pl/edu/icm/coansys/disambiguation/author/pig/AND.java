package pl.edu.icm.coansys.disambiguation.author.pig;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.Tuple;
import org.slf4j.LoggerFactory;

import pl.edu.icm.coansys.disambiguation.author.benchmark.TimerSyso;
import pl.edu.icm.coansys.disambiguation.author.features.disambiguators.DisambiguatorFactory;
import pl.edu.icm.coansys.disambiguation.author.pig.extractor.DisambiguationExtractorFactory;
import pl.edu.icm.coansys.disambiguation.features.Disambiguator;
import pl.edu.icm.coansys.disambiguation.features.FeatureInfo;

public abstract class AND<T> extends EvalFunc<T> {

	
	protected float threshold;
	protected PigDisambiguator[] features;
	protected FeatureInfo[] featureInfos;
	
    private org.slf4j.Logger logger = null;
    private DisambiguationExtractorFactory extrFactory = new DisambiguationExtractorFactory();
    private boolean useIdsForExtractors = false;

	//private float sim[][];
	//private Tuple datain[];
	//private int N;
    
    //benchmark staff
    /*
    protected boolean isStatistics = false;
    private TimerSyso timer = new TimerSyso();
    private int calculatedSimCounter; 
    private int timerPlayId = 0;
    private int finalClusterNumber = 0;
    private List<Integer>clustersSizes;*/
    
	public AND( org.slf4j.Logger logger, String threshold, String featureDescription, String useIdsForExtractors ) throws Exception{
		this.logger = logger;
		this.threshold = Float.parseFloat(threshold);
		this.useIdsForExtractors = Boolean.parseBoolean( useIdsForExtractors );

		
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
	}
 
	
	protected Object getFeatureFromContribFeatureList( List< Map<String,Object> > contribsT, 
			int contribIndex, int featureIndex ) {
		Map<String,Object> featuresMap;
		featuresMap = contribsT.get( contribIndex );
		
		//probably map is empty for some contrib
		if ( featuresMap == null ){
			return null;
		}
		
		String featureName = featureInfos[ featureIndex ].getFeatureExtractorName();
		Object o = featuresMap.get( featureName );
		
		//converting extractor name to opposite type
		//if ( o == null ) {
		//	o = featuresMap.get( exFactory.convertExtractorName( featureName ) );
		//}
		
		//if still - probably feature does not exist for this contrib
		if ( o == null ){
			return null;
		}
		return o;
	}
	
}
