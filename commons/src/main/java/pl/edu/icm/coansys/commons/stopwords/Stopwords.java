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
package pl.edu.icm.coansys.commons.stopwords;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class Stopwords {

    public enum Lang {

        // language code, stopwords path
        DE("de", "stopwords/stopwords_de.txt"),
        DK("dk", "stopwords/stopwords_dk.txt"),
        EN("en", "stopwords/stopwords_en.txt"),
        ES("es", "stopwords/stopwords_es.txt"),
        FI("fi", "stopwords/stopwords_fi.txt"),
        FR("fr", "stopwords/stopwords_fr.txt"),
        HU("hu", "stopwords/stopwords_hu.txt"),
        IT("it", "stopwords/stopwords_it.txt"),
        NL("nl", "stopwords/stopwords_nl.txt"),
        NO("no", "stopwords/stopwords_no.txt"),
        PL("pl", "stopwords/stopwords_pl.txt"),
        PT("pt", "stopwords/stopwords_pt.txt"),
        RU("ru", "stopwords/stopwords_ru.txt"),
        SE("se", "stopwords/stopwords_se.txt"),
        TR("tr", "stopwords/stopwords_tr.txt");
        private String langCode;
        private String stopwordsPath;

        Lang(String langCode, String stopwordsPath) {
            this.langCode = langCode;
            this.stopwordsPath = stopwordsPath;
        }

        public String getLangCode() {
            return langCode;
        }
        
        
    }

    /**
     * Loading stopwords from a file
     *
     * @param lang Stopwords language
     * @return Set of stopwords
     * @throws IOException
     */
    public static Set<String> loadStopwords(Lang lang) throws IOException {
        Set<String> result = new HashSet<String>();

        InputStream stopwordsStream;
        InputStreamReader isr;
        BufferedReader br = null;

        stopwordsStream = Stopwords.class.getClassLoader().getResourceAsStream(lang.stopwordsPath);

        try {
            isr = new InputStreamReader(stopwordsStream, Charset.forName("UTF-8"));
            br = new BufferedReader(isr);

            String stopword = br.readLine();
            while (stopword != null) {
                stopword = stopword.trim();
                if (!stopword.isEmpty()) {
                    result.add(stopword);
                }
                stopword = br.readLine();
            }
        } finally {
            IOUtils.closeQuietly(br);
        }
        return result;
    }
}
