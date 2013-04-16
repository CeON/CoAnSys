package pl.edu.icm.coansys.kwdextraction.langident;

/*
 * #%L
 * synat-content-impl
 * %%
 * Copyright (C) 2010 - 2013 ICM, Warsaw University
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.Set;
import pl.edu.icm.coansys.kwdextraction.langident.LanguageIdentifierBean.LangVariant;

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