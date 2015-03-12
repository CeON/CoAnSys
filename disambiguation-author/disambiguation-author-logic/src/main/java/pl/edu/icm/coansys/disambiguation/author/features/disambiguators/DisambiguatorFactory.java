/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2015 ICM-UW
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

package pl.edu.icm.coansys.disambiguation.author.features.disambiguators;

import java.util.logging.Level;
import java.util.logging.Logger;

import pl.edu.icm.coansys.disambiguation.features.FeatureInfo;

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
		Disambiguator res = null;
		try {
            res = (Disambiguator) Class.forName(THIS_PACKAGE + "." + fi.getDisambiguatorName()).newInstance();
    		res.setMaxVal(fi.getMaxValue());
    		res.setWeight(fi.getWeight());
		} catch (Exception ex) {
            Logger.getLogger(DisambiguatorFactory.class.getName()).log(Level.SEVERE, null, ex);
        }

        return res;
	}
}
