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

package pl.edu.icm.coansys.kwdextraction.langident;

import java.util.HashSet;
import java.util.Set;

/**
 * Enumeration of ISO 639-1 language codes.
 * 
 * @author whury
 * @author Gra <Gołębiewski Radosław A.> r.golebiewski@icm.edu.pl
 */
public enum LanguagesIso639_1 {
    Afar("aa"),
    Abkhazian("ab"),
    Afrikaans("af"),
    Amharic("am"),
    Arabic("ar"),
    Assamese("as"),
    Aymara("ay"),
    Azerbaijani("az"),

    Bashkir("ba"),
    Byelorussian("be"),
    Bulgarian("bg"),
    Bihari("bh"),
    Bislama("bi"),
    Bengali("bn"),
    Tibetan("bo"),
    Breton("br"),

    Catalan("ca"),
    Corsican("co"),
    Czech("cs"),
    Welsh("cy"),

    Danish("da"),
    German("de"),
    Bhutani("dz"),

    Greek("el"),
    English("en"),
    Esperanto("eo"),
    Spanish("es"),
    Estonian("et"),
    Basque("eu"),

    Persian("fa"),
    Finnish("fi"),
    Fiji("fj"),
    Faroese("fo"),
    French("fr"),
    Frisian("fy"),

    Irish("ga"),
    ScotsGaelic("gd"),
    Galician("gl"),
    Guarani("gn"),
    Gujarati("gu"),

    Hausa("ha"),
    Hebrew("he"),
    Hindi("hi"),
    Croatian("hr"),
    Hungarian("hu"),
    Armenian("hy"),

    Interlingua("ia"),
    Indonesian("id"),
    Interlingue("ie"),
    Inupiak("ik"),
    Icelandic("is"),
    Italian("it"),
    Inuktitut("iu"),

    Japanese("ja"),
    Javanese("jw"),

    Georgian("ka"),
    Kazakh("kk"),
    Greenlandic("kl"),
    Cambodian("km"),
    Kannada("kn"),
    Korean("ko"),
    Kashmiri("ks"),
    Kurdish("ku"),
    Kirghiz("ky"),

    Latin("la"),
    Lingala("ln"),
    Laothian("lo"),
    Lithuanian("lt"),
    Latvian("lv"),

    Malagasy("mg"),
    Maori("mi"),
    Macedonian("mk"),
    Malayalam("ml"),
    Mongolian("mn"),
    Moldavian("mo"),
    Marathi("mr"),
    Malay("ms"),
    Maltese("mt"),
    Burmese("my"),

    Nauru("na"),
    Nepali("ne"),
    Dutch("nl"),
    Norwegian("no"),

    Occitan("oc"),
    Oromo("om"),
    Oriya("or"),

    Punjabi("pa"),
    Polish("pl"),
    Pashto("ps"),
    Portuguese("pt"),

    Quechua("qu"),

    RhaetoRomance("rm"),
    Kirundi("rn"),
    Romanian("ro"),
    Russian("ru"),
    Kinyarwanda("rw"),

    Sanskrit("sa"),
    Sindhi("sd"),
    Sangho("sg"),
    SerboCroatian("sh"),
    Sinhalese("si"),
    Slovak("sk"),
    Slovenian("sl"),
    Samoan("sm"),
    Shona("sn"),
    Somali("so"),
    Albanian("sq"),
    Serbian("sr"),
    Siswati("ss"),
    Sesotho("st"),
    Sundanese("su"),
    Swedish("sv"),
    Swahili("sw"),

    Tamil("ta"),
    Telugu("te"),
    Tajik("tg"),
    Thai("th"),
    Tigrinya("ti"),
    Turkmen("tk"),
    Tagalog("tl"),
    Setswana("tn"),
    Tonga("to"),
    Turkish("tr"),
    Tsonga("ts"),
    Tatar("tt"),
    Twi("tw"),

    Uighur("ug"),
    Ukrainian("uk"),
    Urdu("ur"),
    Uzbek("uz"),

    Vietnamese("vi"),
    Volapuk("vo"),

    Wolof("wo"),

    Xhosa("xh"),

    Yiddish("yi"),
    Yoruba("yo"),

    Zhuang("za"),
    Chinese("zh"),
    Zulu("zu");

    private String code;
    private static final Set<String> VALID_CODES = new HashSet<String>();

    static {
        for (final LanguagesIso639_1 lang : values()) {
            VALID_CODES.add(normalize(lang.getCode()));
        }
    }

    private LanguagesIso639_1(final String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static boolean isValid(final String langCode) {
        return VALID_CODES.contains(normalize(langCode));
    }

    /**
     * Checks if specified language is valid iso 639-1 language code and returns
     * normalized language code. CategorizationException is thrown if language code is not valid.
     */
    public static String checkAndNormalize(String langCode) throws CategorizationException {
        String result = null;
        if (langCode!=null) {
            result = normalize(langCode.trim());
        }
        if (! isValid(result))
			throw new CategorizationException("["+result+"] is not valid ISO 639-1 language code");
        return result;
    }

    private static String normalize(final String code) {
        return code==null ? null : code.toLowerCase();
    }
}
