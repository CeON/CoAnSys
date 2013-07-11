package pl.edu.icm.coansys.disambiguation.author.pig;

import java.util.List;
import java.util.Vector;

import org.apache.hadoop.conf.Configuration;
import org.apache.pig.EvalFunc;
import org.apache.pig.backend.executionengine.ExecException;
import org.apache.pig.data.Tuple;

import pl.edu.icm.coansys.disambiguation.author.features.disambiguators.DisambiguatorFactory;
import pl.edu.icm.coansys.disambiguation.features.Disambiguator;
import pl.edu.icm.coansys.disambiguation.features.FeatureInfo;


public class Udf2  extends EvalFunc<Integer>{

	private Vector <Integer> colToSum = null;
	private AuxilI auxil;
    private double threshold;
    private List<FeatureInfo> featureInfos;
    private Disambiguator[] features;
	
	private void init() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		auxil = (Auxil) Class.forName( "pl.edu.icm.coansys.disambiguation.author.pig.Auxil" ).newInstance();
	}
	
	public Udf2() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		init();
	}
	
	public Udf2( String param ) throws InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		Configuration conf = context.getConfiguration();

        //TO DO: threshold look unnecessary?
		threshold = Double.parseDouble(conf.getStrings("THRESHOLD")[0]);
        featureInfos = FeatureInfo.parseFeatureInfoString(conf.get("FEATURE_DESCRIPTION"));

        features = new Disambiguator[featureInfos.size()];

        DisambiguatorFactory ff = new DisambiguatorFactory();
        int index = -1;
        for (FeatureInfo fi : featureInfos) {
            index++;
            features[index] = ff.create(fi);
        }
		
		init();
	}
	
	public Integer exec( Tuple input ) throws ExecException {
		
		int sum = 0;
		
		if ( colToSum == null )
			for ( int i = 0; i < input.size(); i++ )
				sum = auxil.execute( sum, (Integer) input.get(i) );
		else
			for ( int i = 0; i < colToSum.size(); i++ )
				sum = auxil.execute( sum, (Integer) input.get( colToSum.get(i) ) );
				
		return sum;
	}
}
