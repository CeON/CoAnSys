/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.disambiguation.author.features.extractors;

import java.util.logging.Level;
import java.util.logging.Logger;

import pl.edu.icm.coansys.disambiguation.author.features.Disambiguator;
import pl.edu.icm.coansys.disambiguation.author.features.Extractor;
import pl.edu.icm.coansys.disambiguation.author.features.FeatureInfo;

import com.sun.el.util.ReflectionUtil;

/**
 * The factory for building {@link Extractor}s from the package "pl.edu.icm.coansys.disambiguation.author.model.feature.extractor"
 * using description {@link FeatureInfo}.
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
