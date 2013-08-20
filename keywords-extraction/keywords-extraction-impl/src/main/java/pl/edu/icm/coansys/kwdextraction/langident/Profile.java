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
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
* @author Lukasz Bolikowski (bolo@icm.edu.pl)
* @author Gra <Gołębiewski Radosław A.> r.golebiewski@icm.edu.pl
*/
public class Profile {
    protected static final char SEPARATION_CHAR = ' ';
    protected static final String UNTERLINE_STRING = "_";
    protected static final String SPACE_STRING = " ";
    protected static final String EMPTY_STRING = "";
    protected static final String LETTERS = "; letters: ";
    protected static final String PROCESSED_WORDS = "Processed words: ";

    private static final Logger LOG = LoggerFactory.getLogger(Profile.class);

    public static final int PROFILE_CUTOFF = 400;
    public static final int MAX_GRAM = 5;

    private List<String> data = new ArrayList<String>(PROFILE_CUTOFF);
    private Map<String, Integer> hash = new HashMap<String, Integer>(PROFILE_CUTOFF);

    private static class TreeElement implements Comparable<TreeElement> {
        private final String s;
        private final int c;

        TreeElement(final String s, final int c) {
            this.s = s;
            this.c = c;
        }

        @Override
		public int compareTo(final Profile.TreeElement o) {
            final TreeElement lhs = this;
            final TreeElement rhs = o;//(TreeElement)

            if (lhs.c > rhs.c)
				return -1;
            if (lhs.c < rhs.c)
				return 1;

            return lhs.s.compareTo(rhs.s);
        }

		@Override
		public int hashCode() {
			return HashCodeBuilder.reflectionHashCode(this);
		}

		@Override
		public boolean equals(Object obj) {
			return EqualsBuilder.reflectionEquals(this, obj);
		}

    }

    /**
     * Reads profile from a stream.
     * @param stream Stream to read profile from
     */
    public Profile(final InputStream stream) throws IOException {
        this(new InputStreamReader(stream, Charset.forName("UTF-8")));
    }

    /**
     * Reads profile from a reader.
     * @param reader Reader to read profile from
     */
    public Profile(final Reader reader) throws IOException {
        data = new ArrayList<String>(PROFILE_CUTOFF);
        hash = new HashMap<String,Integer>(PROFILE_CUTOFF);

        boolean noEOF = true;
        char buf[] = new char[1];

        int seq = 0;
        while (noEOF) {
            StringBuilder gram = new StringBuilder();
            // String gram = EMPTY_STRING;
            int r = reader.read(buf);
            while (r == 1 && ! new String(buf).matches("\\s")) {
                gram.append(buf[0]);
                r = reader.read(buf);
            }

			if (!EMPTY_STRING.equals(gram.toString())) {
                data.add(gram.toString());
                hash.put(gram.toString(), Integer.valueOf(seq++));
            }

            noEOF = noEOF && (r == 1);
            while (noEOF && buf[0] != '\n') {
                r = reader.read(buf);
                noEOF = noEOF && (r == 1);
            }
        }
    }

    public void store(final OutputStream stream) throws UnsupportedEncodingException {
        final PrintStream printStream = new PrintStream(stream, false, "UTF-8");
        for (final String gram : data) {
            printStream.println(gram);
        }
    }

    private void addGram(final Map<String, Integer> grams, final String g) {
        final Integer c = grams.get(g);
		grams.put(g, Integer.valueOf((c == null) ? 1 : c.intValue() + 1));
    }

    /**
     * Generates profile from a text.
     * @param text Text to profile
     */
    public Profile(final String txt) {
        final String text = txt.toLowerCase().replaceAll("[^\\p{L}']+", SPACE_STRING);
        final String[] words = text.split(SPACE_STRING);

        int processedWords = 0;
        int processedLetters = 0;
        final Map<String, Integer> grams = new HashMap<String, Integer>();
        for (final String word : words) {
            final int len = word.length();

            for (int n = 1; n <= MAX_GRAM - 1; n++) {
                if (len >= n) {
                    addGram(grams, UNTERLINE_STRING + word.substring(0, n));
                    addGram(grams, word.substring(len - n, len) + UNTERLINE_STRING);
                }
            }

            for (int n = 1; n <= MAX_GRAM; n++) {
                for (int i = 0; i < len - n + 1; i++) {
                    addGram(grams, word.substring(i, i + n));
                }
            }
            processedWords += 1;
            processedLetters += len;
        }

        LOG.debug(PROCESSED_WORDS + processedWords + LETTERS + processedLetters);

//        All the N-grams are in the grams map -- now we need
//        to sort them and put first PROFILE_CUTOFF most valuable
//        ones in the profile
        final TreeSet<TreeElement> ts = new TreeSet<TreeElement>();
        for (final Map.Entry<String, Integer> me : grams.entrySet()) {
            ts.add(new TreeElement(me.getKey(), me.getValue().intValue()));
        }

        int newSize = grams.entrySet().size();
        if (newSize > PROFILE_CUTOFF) {
            newSize = PROFILE_CUTOFF;
        }

        data = new ArrayList<String>(newSize);
        hash = new HashMap<String, Integer>(newSize);
        for (int i = 0; i < newSize; i++) {
            final TreeElement te = ts.first();
            data.add(te.s);
			hash.put(data.get(i), Integer.valueOf(i));
            ts.remove(te);
        }
    }

    public static int distance(final Profile p1, final Profile p2) {
        int distance = 0;
        for (int i = 0; i < p1.data.size(); i++) {
            final String s = p1.data.get(i);
            final Integer jj = p2.hash.get(s);
            if (null == jj) {
                distance += PROFILE_CUTOFF;
                continue;
            }
            final int j = jj.intValue();
            distance += Math.abs(i - j);
        }
        distance += (PROFILE_CUTOFF - p1.data.size()) * PROFILE_CUTOFF;
        return distance;
    }

    public int distance(final Profile other) {
        return distance(this, other);
    }

    @Override
	public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (final String gram : data) {
            builder.append(gram);
            builder.append(SEPARATION_CHAR);
        }
        return builder.toString();
    }
}
