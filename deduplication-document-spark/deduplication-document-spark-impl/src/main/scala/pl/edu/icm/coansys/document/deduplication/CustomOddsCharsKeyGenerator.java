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
package pl.edu.icm.coansys.document.deduplication;

import java.util.Arrays;
import pl.edu.icm.coansys.commons.java.DocumentWrapperUtils;
import pl.edu.icm.coansys.commons.java.StringTools;
import pl.edu.icm.coansys.deduplication.document.keygenerator.WorkKeyGenerator;
import pl.edu.icm.coansys.models.DocumentProtos;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper;

/**
 * An early deduplication phase key used to group works into clusters. This is
 * an extended version, which allows to define requested key size
 *
 * @author Łukasz Dumiszewski
 * @author Aleksander Nowiński
 *
 */
public class CustomOddsCharsKeyGenerator implements WorkKeyGenerator {

    public static final int DEFAULT_KEY_SIZE = 5;

    private int keySize = DEFAULT_KEY_SIZE;

    private int[] keySizes = {DEFAULT_KEY_SIZE};

    public CustomOddsCharsKeyGenerator() {
    }

    public CustomOddsCharsKeyGenerator(int keySize) {
        this.keySize = keySize;
    }

    /**
     * Generates reasonable deduplication key for the given
     * {@link DocumentWrapper}. The key has size as defined by keySize param.
     * The key is created using title of the work, with removed punctuation and
     * basic english stopwords, and then keySize odd letters are taken. The
     * method is thread-safe.
     *
     * @param doc document, which key is generated for
     * @return an reasonable key for starting deduplication of size keySize
     */
    @Override
    public String generateKey(DocumentProtos.DocumentMetadata doc) {
        String title = DocumentWrapperUtils.getMainTitle(doc);
        return generateKey(title);
    }

    /**
     * Generate a collection of keys of predefined sizes.
     *
     * @param doc
     * @return
     */
    public String[] generateKeyList(DocumentProtos.DocumentMetadata doc) {
        String title = DocumentWrapperUtils.getMainTitle(doc);
        return generateKeys(title);
    }

    protected String[] generateKeys(String title) {
        title = cleanUpString(title);
        String[] res = new String[keySizes.length];
        for (int k = 0; k < keySizes.length; k++) {
            int kl = keySizes[k];
            StringBuilder oddCharsSB = new StringBuilder();
            for (int i = 0; i < title.length() && oddCharsSB.length() < kl; i += 2) {
                oddCharsSB.append(title.charAt(i));
            }
            res[k] = oddCharsSB.toString();
        }
        return res;
    }

    protected String generateKey(String title) {
        title = cleanUpString(title);

        StringBuilder oddCharsSB = new StringBuilder();
        for (int i = 0; i < title.length() && oddCharsSB.length() < keySize; i += 2) {
            oddCharsSB.append(title.charAt(i));
        }
        return oddCharsSB.toString();
    }

    protected String cleanUpString(String title) {
        title = StringTools.normalize(title); //fixme: it seems that normalize, despite javadocs has stopword removal already
        title = StringTools.removeStopWords(title);
        title = title.replaceAll("\\s", "");
        return title;
    }

    public int getKeySize() {
        return keySize;
    }

    public void setKeySize(int keySize) {
        this.keySize = keySize;
    }

    public int[] getKeySizes() {
        return Arrays.copyOf(keySizes, keySizes.length);
    }

    public void setKeySizes(int[] keySizes) {
        if (keySizes == null) {
            throw new IllegalArgumentException("Null sizes not premitted");
        }
        if (keySizes.length < 1) {
            throw new IllegalArgumentException("Non empty array required");
        }
        for (int i = 0; i < keySizes.length - 1; i++) {
            if (keySizes[i] >= keySizes[i + 1]) {
                throw new IllegalArgumentException("Array must be sorted in growing order and no equal sizes present.");
            }

        }
        this.keySizes = Arrays.copyOf(keySizes, keySizes.length);
    }

}
