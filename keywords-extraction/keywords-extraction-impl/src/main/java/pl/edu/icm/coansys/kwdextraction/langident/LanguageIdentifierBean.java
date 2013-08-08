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

package pl.edu.icm.coansys.kwdextraction.langident;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Map.Entry;
import java.util.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
* Language identification bean.  Guesses the language of a provided text
* by performing N-gram profile comparison.
* 
* @author Lukasz Bolikowski (bolo@icm.edu.pl)
* @author Gra <Gołębiewski Radosław A.> r.golebiewski@icm.edu.pl
*/
public class LanguageIdentifierBean implements LanguageIdentifier {
    private static final double UNCERTAINTY_THRESHOLD_INIT_VALUE = 0.05;

    private static final Logger LOG = LoggerFactory.getLogger(LanguageIdentifierBean.class);

    private static final String LOG_WITH_OVERLAP = " with overlap ";
    private static final String LOG_IDENTIFIED = "Identified ";
    private static final String LANGUAGE_IDENTIFIER_BEAN_CREATED_WITH_FOLLOWING_PROFILES
        = "LanguageIdentifierBean created with following profiles: ";
    private static final String LOG_SEPARATOR = ", ";
    private static final String WILL_NOT_BE_CREATED_BECAUSE_ITS_LANGUAGE_DOES_NOT_BELONG_TO_SPECIFIED_LANGUAGE_SET
        = "] will not be created because its language does not belong to specified language set";
    private static final String PROFILE_S = "Profile(s) [";
    private static final String IS_NOT_AVAILABLE_FOR_LANGUAGE_IDENTIFICATION
        = "] is not available for language identification";
    private static final String LANGUAGE = "Language [";
    private static final String IS_NOT_VALID_ISO_639_1_LANGUAGE_CODE = "] is not valid ISO 639-1 language code";
    private static final String LANGUAGES_PROPERTY_NAME = "languages";
    private static final String CANNOT_FIND_RESOURCE = "Cannot find resource ";
    private static final String PROFILE_PROPERTIES
        = "pl/edu/icm/coansys/kwdextraction/langident/profiles.properties";
    private static final String PROFILE_PREFIX = "pl/edu/icm/coansys/kwdextraction/langident/profile/";
    private static final String PROFILE_SUFFIX = ".txt";

    public static final String LANG_NONE = "**";

    /** Language profiles.  Language codes as key, profiles as value. */
    private Map<LangVariant, Profile> profiles = new HashMap<LangVariant, Profile>();

    /** Profile overlap lower than this threshold will be ignored. */
    private double uncertaintyThreshold = UNCERTAINTY_THRESHOLD_INIT_VALUE;

    public void setUncertaintyThreshold(final double uncertaintyThreshold) {
        this.uncertaintyThreshold = uncertaintyThreshold;
    }

    public double getUncertaintyThreshold() {
        return uncertaintyThreshold;
    }

    public LanguageIdentifierBean() throws IOException {
        initialize(null);
    }

    /**
     * Creates language identifier bean with profiles created only for specified languages.
     */
    public LanguageIdentifierBean(final String[] availableLanguages) throws IOException {
        initialize(availableLanguages);
    }

    /**
     * Loads language profiles.
     * 
     * @throws IOException
     */
    private void initialize(final String[] predefinedLanguages) throws IOException {
        final InputStream indexStream
            = LanguageIdentifierBean.class.getClassLoader().getResourceAsStream(PROFILE_PROPERTIES);

        if (indexStream == null)
			throw new IOException(CANNOT_FIND_RESOURCE + PROFILE_PROPERTIES);

        final Properties props = new Properties(); 
        props.load(indexStream);

        final String tmp = props.getProperty(LANGUAGES_PROPERTY_NAME);
        String[] languages = StringUtils.split(tmp, ' ');

        // check languages
        validateLanguages(languages);

        // remove elements from language array which don't belong to predefined languages
        if (null != predefinedLanguages) {
            languages = prepareLanguagesArray(predefinedLanguages, languages);
        }

        makeProfilesMap(languages);
        Arrays.sort(languages);
        LOG.info(LANGUAGE_IDENTIFIER_BEAN_CREATED_WITH_FOLLOWING_PROFILES
                +StringUtils.join(languages, LOG_SEPARATOR));
    }

    @Override
    public Set<String> getRegisteredLanguages() {
        final Set<String> langs = new HashSet<String>();
        for (final LangVariant lv : profiles.keySet()) {
            langs.add(lv.getLang());
        }
        return langs;
    }

    @Override
    public String classify(final String text) {
        final LangVariant lv = classifyVariant(text);
        if (null == lv)
			return LANG_NONE;
        return lv.getLang();
    }

    @Override
    public LangVariant classifyVariant(final String text) {
        if (null == text)
			return null;

        final Profile textProfile = new Profile(text);

        return findArgMin(textProfile);
    }

    @Override
    public String classify(final String text, final String defaultLanguage) {
        final String lang = classify(text);
        return LANG_NONE.equals(lang) ? defaultLanguage : lang;
    }

    public static class LangVariant implements Serializable {
        private static final String LANG_VARIANT_SPLIT_PATTERN = "\\.";

        private static final long serialVersionUID = 4522179133152273109L;

        private String lang;
        private String variant;

        public LangVariant(final String langVariant) {
            if (null == langVariant)
				throw new CategorizationException("Null pointer exception");
            lang = langVariant;
            variant = null;

            final String[] tab = langVariant.split(LANG_VARIANT_SPLIT_PATTERN, 2);
            if (2 == tab.length) {
                lang = tab[0];
                variant = tab[1];
            }
        }

        public LangVariant(final String lang, final String variant) {
            this.lang = lang;
            this.variant = variant;
        }

        public String getLang() {
            return lang;
        }

        public String getVariant() {
            return variant;
        }

        @Override
        public int hashCode() {
        	return HashCodeBuilder.reflectionHashCode(this);
        }

        @Override
        public boolean equals(final Object obj) {
            return EqualsBuilder.reflectionEquals(this, obj);
        }

        @Override
        public String toString() {
            if (null == variant)
				return lang;
            return lang + '.' + variant;
        }
    }

    protected void validateLanguages(String[] languages) {
        for (final String lv : languages) {
            final String lang = new LangVariant(lv).getLang();
            if ( ! LanguagesIso639_1.isValid(lang))
				throw new CategorizationException("["+lang+IS_NOT_VALID_ISO_639_1_LANGUAGE_CODE);
        }
    }

    protected String[] prepareLanguagesArray(final String[] predefinedLanguages, String[] languages) {
        final Map<String, List<String>> langVariantMap = new HashMap<String, List<String>>();
        checkAndNormalize(languages, langVariantMap);

        final Set<String> predefinedLangSet = new HashSet<String>();
        checkAndNormalize(predefinedLanguages, langVariantMap, predefinedLangSet);

        final Set<String> profileSet = new HashSet<String>();
        for (final Entry<String, List<String>> entry : langVariantMap.entrySet()) {
            final String lang = entry.getKey();
            final List<String> profilesList = entry.getValue();
            if (predefinedLangSet.contains(lang)) {
                for (final String profile : profilesList) {
                    profileSet.add(profile);
                }
            }
            else {
                String profilesStr = StringUtils.join(profilesList, LOG_SEPARATOR);
                LOG.info(PROFILE_S+profilesStr
                        +WILL_NOT_BE_CREATED_BECAUSE_ITS_LANGUAGE_DOES_NOT_BELONG_TO_SPECIFIED_LANGUAGE_SET);
                langVariantMap.remove(entry.getKey());
            }
        }
        return profileSet.toArray(new String[profiles.size()]);
    }

    protected void checkAndNormalize(final String[] predefinedLanguages,
            final Map<String, List<String>> langVariantMap, final Set<String> predefinedLangSet) {
        for (final String language : predefinedLanguages) {
            final String lang = LanguagesIso639_1.checkAndNormalize(language);
            predefinedLangSet.add(lang);
            if( ! langVariantMap.containsKey(lang)) {
                LOG.warn(LANGUAGE+lang+IS_NOT_AVAILABLE_FOR_LANGUAGE_IDENTIFICATION);
            }
        }
    }

    protected void checkAndNormalize(String[] languages, final Map<String, List<String>> langVariantMap) {
        for (final String lv : languages) {
            String lang = new LangVariant(lv).getLang();
            lang = LanguagesIso639_1.checkAndNormalize(lang);
            List<String> profilesList = langVariantMap.get(lang);
            if (null == profilesList) {
                profilesList = new ArrayList<String>();
                langVariantMap.put(lang, profilesList);
            }
            profilesList.add(lv);
        }
    }

    protected void makeProfilesMap(String[] languages) throws IOException {
        profiles = new HashMap<LangVariant, Profile>();
        for (final String langVariant : languages) {
//            System.out.println(langVariant);
            final LangVariant lv = new LangVariant(langVariant);
            final String resource = PROFILE_PREFIX + langVariant.toLowerCase(Locale.ENGLISH) + PROFILE_SUFFIX;
            final InputStream profileStream = LanguageIdentifierBean.class.getClassLoader()
                    .getResourceAsStream(resource);
            if (profileStream == null)
				throw new IOException(CANNOT_FIND_RESOURCE + resource);
            profiles.put(lv, new Profile(profileStream));
        }
    }

    protected LangVariant findArgMin(final Profile textProfile) {
        LangVariant argMin = null;
        int min = Profile.PROFILE_CUTOFF * Profile.PROFILE_CUTOFF;

        for (final Map.Entry<LangVariant, Profile> entry : profiles.entrySet()) {
            final LangVariant lv = entry.getKey();
            final Profile profile = entry.getValue();
            final int dist = profile.distance(textProfile);
            if (min > dist) {
                min = dist;
                argMin = lv;
            }
        }

        final double overlap = 1.0 - 1.0 * min / (Profile.PROFILE_CUTOFF * Profile.PROFILE_CUTOFF);
        LOG.debug(LOG_IDENTIFIED + argMin + LOG_WITH_OVERLAP + overlap);

        if (overlap < uncertaintyThreshold) {
            argMin = null;
            // log.debug("This certainty is too low, returning 'unknown language'.");
        }
        return argMin;
    }
}
