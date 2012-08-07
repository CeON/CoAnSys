/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.disambiguation.author.model.feature.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import pl.edu.icm.coansys.disambiguation.author.model.feature.Feature;
import pl.edu.icm.coansys.disambiguation.author.model.feature.FeatureInfo;

import com.sun.el.util.ReflectionUtil;

/**
 * 
 * @author pdendek
 *
 */
public class FeatureFactory {
	
    private static final String THIS_PACKAGE = new FeatureFactory().getClass().getPackage().getName();
    
	public Feature create(FeatureInfo fi){
		try {
            return (Feature) ReflectionUtil.forName(THIS_PACKAGE + "." + fi.getFeatureName()).newInstance();
        } catch (Exception ex) {
            Logger.getLogger(FeatureFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
	}
}
