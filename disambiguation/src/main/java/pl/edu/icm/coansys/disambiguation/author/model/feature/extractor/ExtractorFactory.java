/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.disambiguation.author.model.feature.extractor;

import java.util.logging.Level;
import java.util.logging.Logger;

import pl.edu.icm.coansys.disambiguation.author.model.feature.Extractor;
import pl.edu.icm.coansys.disambiguation.author.model.feature.FeatureInfo;

import com.sun.el.util.ReflectionUtil;

/**
 * 
 * @author pdendek
 * @version 1.0
 * @since 2012-08-07
 */
public class ExtractorFactory {
	
    private static final String THIS_PACKAGE = new ExtractorFactory().getClass().getPackage().getName();
    
	public Extractor create(FeatureInfo fi){
		try {
            return (Extractor) ReflectionUtil.forName(THIS_PACKAGE + "." + fi.getFeatureExtractorName()).newInstance();
        } catch (Exception ex) {
            Logger.getLogger(ExtractorFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
	}
}
