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

package pl.edu.icm.coansys.disambiguation.features;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link FeatureInfo} is an information container binding data about associated {@link Extractor} and {@link Disambiguator}.
 * {@link FeatureInfo} may be passed either 
 * to {@link ExtractorFactory} (to build class implementing {@link Extractor}, 
 * which name is specified in the field "featureExtractorName") or 
 * to {@link DisambiguatorFactory} (to build class extending {@link Disambiguator}, 
 * which name is specified in the field "disambigutorName").
 * 
 * Finally, {@link FeatureInfo} contains data about a {@link Disambiguator}'s importance (weight) and additional auxiliary values, like a maxValue. 
 *  
 * @author pdendek
 * @version 1.0
 * @since 2012-08-07
 *
 */
public class FeatureInfo {
	
	private static Logger logger = LoggerFactory.getLogger(FeatureInfo.class);
	
	/**
	 * A name of a {@link Disambiguator}.
	 */
	private String disambiguatorName;
	/**
	 * A name of a {@link Extractor}.
	 */
	private String featureExtractorName;
	/**
	 * A weight/importance factor of a {@link Disambiguator}.
	 */
	private double weight;
	/**
	 * A value for scaling integer result of a {@link Disambiguator} to the range [0,1].
	 */
	private int maxValue;
	
	/**
	 * Build a list of {@link FeatureInfo} objects.  
	 * @param info String of the following structure: 
	 * disambiguatorName#featureExtractorName#Weight#MaxValue[,disambiguatorName#FeatureExtractorName#Weight#MaxValue]
	 * where: 
	 * (1) the comma sing (",") separates one feature info string from another.  
	 * (2) the hash sign ("#") is used as a separating character. 
	 * (3) all values between separators follows the "([A-Za-z])+" regular expression.
	 * (4) values from one feature info string ("disambiguatorName#featureExtractorName#Weight#MaxValue") 
	 * will be assigned to a proper {@link FeatureInfo} fields via the constructor. 
	 * @return A list of {@link FeatureInfo} objects, sorted by the "weight" field in a descending manner.  
	 */
	public static List<FeatureInfo> parseFeatureInfoString(String info){
		List<FeatureInfo> ret = new ArrayList<FeatureInfo>(); 
    	String[] finfos = info.split(",");
    	for(String finfo : finfos){
    		String[] details = finfo.split("#");
    		if(details.length != 4){
    			logger.error("Feature info does not contains enought data. " +
    					"It should follow the pattern disambiguatorName#FeatureExtractorName" +
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
    	Collections.sort(ret, new Comparator<Object>() {
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
	
	/**
	 * @param disambiguatorName a string corresponding to classes from the package "pl.edu.icm.coansys.disambiguation.author.features.disambiguators".
	 * @param featureExtractorName a string corresponding to classes from the package "pl.edu.icm.coansys.disambiguation.author.features.extractors".
	 * @param weight a string in the double format "[0-9]+\\.[0-9]+d". Indicates importance of a described feature.
	 * @param maxValue  a string in the integer format "[0-9]+". Indicates maximum value yield on a training set by a described feature.
	 */
	public FeatureInfo(String featureName, String featureExtractorName,
			double weight, int maxValue){
		this.disambiguatorName = featureName;
		this.featureExtractorName = featureExtractorName;
		this.weight = weight;
		this.maxValue = maxValue;
	}
	
	public String getDisambiguatorName() {
		return disambiguatorName;
	}
	public void setDisambiguatorName(String disambiguatorName) {
		this.disambiguatorName = disambiguatorName;
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
