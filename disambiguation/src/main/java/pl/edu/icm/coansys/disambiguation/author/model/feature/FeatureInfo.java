/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.disambiguation.author.model.feature;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;

import pl.edu.icm.coansys.disambiguation.auxil.LoggingInDisambiguation;

/**
 * 
 * @author pdendek
 *
 */
public class FeatureInfo {
	
	private static Logger logger = Logger.getLogger(LoggingInDisambiguation.class);
	
	protected String featureName;
	protected String featureExtractorName;
	protected double weight;
	protected int maxValue;
	
	public static List<FeatureInfo> parseFeatureInfoString(String info){
		ArrayList<FeatureInfo> ret = new ArrayList<FeatureInfo>(); 
    	String[] finfos = info.split(",");
    	for(String finfo : finfos){
    		String[] details = finfo.split("#");
    		if(details.length != 4){
    			logger.error("Feature info does not contains enought data. " +
    					"It should follow the pattern featureName#FeatureExtractorName" +
    					"#Weight#MaxValue");
    			logger.error("instead it contains: "+finfo);
    			logger.error("This feature info will be ignored");
    			continue;
    		}else{
    			ret.add(new FeatureInfo(details[0],details[1],
    					Double.parseDouble(details[2]),
    					Integer.parseInt(details[3])));
    		}
    	}
    	
    	//Descending sort
    	Collections.sort(ret, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                if (o1 == null && o2 == null) {
                    return 0;
                }
                if (o1 == null) {
                    return 1;
                }
                if (o2 == null) {
                    return -1;
                }
                return -(int) Math.signum(((FeatureInfo) o1).getWeight() 
                		- ((FeatureInfo) o2).getWeight());
            }
        });
    	
    	return ret;
	}
	
	public FeatureInfo(String featureName, String featureExtractorName,
			double weight, int maxValue){
		this.featureName = featureName;
		this.featureExtractorName = featureExtractorName;
		this.weight = weight;
		this.maxValue = maxValue;
	}
	
	public String getFeatureName() {
		return featureName;
	}
	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}
	public String getFeatureExtractorName() {
		return featureExtractorName;
	}
	public void setFeatureExtractorName(String featureExtractorName) {
		this.featureExtractorName = featureExtractorName;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public int getMaxValue() {
		return maxValue;
	}
	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}
}
