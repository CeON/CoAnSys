/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.disambiguation.author.features.disambiguators;

import java.util.logging.Level;
import java.util.logging.Logger;

import pl.edu.icm.coansys.disambiguation.features.Disambiguator;
import pl.edu.icm.coansys.disambiguation.features.FeatureInfo;

import com.sun.el.util.ReflectionUtil;

/**
 * The factory for building {@link Disambiguator}s from the package "pl.edu.icm.coansys.disambiguation.author.features.disambiguators"
 * using description {@link FeatureInfo}.
 *  
 * @author pdendek
 * @version 1.0
 * @since 2012-08-07
 */
public class DisambiguatorFactory {
	
    private static final String THIS_PACKAGE = new DisambiguatorFactory().getClass().getPackage().getName();
    
	public Disambiguator create(FeatureInfo fi){
		try {
            return (Disambiguator) ReflectionUtil.forName(THIS_PACKAGE + "." + fi.getDisambiguatorName()).newInstance();
        } catch (Exception ex) {
            Logger.getLogger(DisambiguatorFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
	}
}
