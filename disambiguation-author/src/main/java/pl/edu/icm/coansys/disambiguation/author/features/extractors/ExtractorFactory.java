/*
 * This file is part of CoAnSys project.
 * Copyright (c) 20012-2013 ICM-UW
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

package pl.edu.icm.coansys.disambiguation.author.features.extractors;

import java.util.logging.Level;
import java.util.logging.Logger;

import pl.edu.icm.coansys.disambiguation.features.Extractor;
import pl.edu.icm.coansys.disambiguation.features.FeatureInfo;

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
