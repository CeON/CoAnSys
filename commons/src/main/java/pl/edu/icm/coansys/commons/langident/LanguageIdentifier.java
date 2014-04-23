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

package pl.edu.icm.coansys.commons.langident;

import java.util.Set;
import pl.edu.icm.coansys.commons.langident.LanguageIdentifierBean.LangVariant;

/**
* @author Gra <Gołębiewski Radosław A.> r.golebiewski@icm.edu.pl
*/
public interface LanguageIdentifier {
    /**
     * Returns the set of languages registered in the bean.
     * @return Languages registered in the bean.
     */
	Set<String> getRegisteredLanguages();

    /**
     * Guesses the language of a text.
     *
     * @param text Text to analyze.
     * @return Text's language.
     */
	String classify(String text);

    /** Guesses the language of a text. If language is not recognized,
     * defaultLanguage is returned.
     * @param text
     * @param defaultLanguage
     * @return
     */
    String classify(String text, String defaultLanguage);

    /**
     * Classifies language variant of a text.
     * @return
     */
    LangVariant classifyVariant(String text);
}