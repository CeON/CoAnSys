/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.kwdextraction.langident;

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