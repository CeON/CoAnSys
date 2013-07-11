package pl.edu.icm.coansys.disambiguation.author.pig;

import org.apache.pig.EvalFunc;
import org.apache.pig.backend.executionengine.ExecException;
import org.apache.pig.data.Tuple;

//import pl.edu.icm.coansys.disambiguation.auxil.TextTextArrayMapWritable;
//import pl.edu.icm.coansys.disambiguation.features.Disambiguator;
//import pl.edu.icm.coansys.disambiguation.features.FeatureInfo;


public class Udf extends EvalFunc<Tuple>{


	public Udf() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
	
	}
	
	public Udf( String param ) throws InstantiationException, IllegalAccessException, ClassNotFoundException {

	}
	
	public Tuple exec( Tuple input ) throws ExecException {
		
		Tuple res = null;
		
		
		
		return res;
	}
	
	
/*	
	// straight from ClusterDisam.._toy
    protected double[][] calculateAffinity() {
        double[][] sim = new double[featuresMapsList.size()][];
        for (int i = 1; i < featuresMapsList.size(); i++) {
            sim[i] = new double[i];
            for (int j = 0; i < j; j++) {
                sim[i][j] = threshold;
                for (int findex = 0; findex < features.length; findex++) {
                    TextTextArrayMapWritable a = featuresMapsList.get(i);
                    TextTextArrayMapWritable b = featuresMapsList.get(j);

                    Disambiguator feature = features[findex];
                    FeatureInfo featureInfo = featureInfos.get(findex);

                    double partial = feature.calculateAffinity(
                            a.getStringList(feature.getName()),
                            b.getStringList(feature.getName()));
                    partial = partial / featureInfo.getMaxValue() * featureInfo.getWeight();
                    sim[i][j] += partial;
                    if (sim[i][j] > 0) {
                        break;
                    }
                }
            }
        }
        return sim;
    }
*/    
    
	
	
}
