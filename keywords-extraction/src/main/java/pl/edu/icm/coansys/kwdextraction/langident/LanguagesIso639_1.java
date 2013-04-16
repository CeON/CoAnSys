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
        if (langCode!=null) {
            langCode = normalize(langCode.trim());
        }
        if (! isValid(langCode))
			throw new CategorizationException("["+langCode+"] is not valid ISO 639-1 language code");
        return langCode;
    }

    private static String normalize(final String code) {
        return code==null ? null : code.toLowerCase();
    }
}
