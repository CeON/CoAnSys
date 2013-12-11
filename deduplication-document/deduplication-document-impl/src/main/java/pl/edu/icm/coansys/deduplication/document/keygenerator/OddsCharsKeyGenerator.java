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

package pl.edu.icm.coansys.deduplication.document.keygenerator;

import pl.edu.icm.coansys.commons.java.DocumentWrapperUtils;
import pl.edu.icm.coansys.commons.java.StringTools;
import pl.edu.icm.coansys.models.DocumentProtos;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper;


/**
 * A work deduplication map/reduce phase key generator
 * @author Łukasz Dumiszewski
 *
 */
public class OddsCharsKeyGenerator implements WorkKeyGenerator {
    
    private static final int KEY_PART_LENGTH = 5;
        
    /**
     * Generates key for the given {@link DocumentWrapper}
     * @param level influences the keyLength, the keyLength is a multiplication of the level and {@link #KEY_PART_LENGTH} 
     */
    @Override
    public String generateKey(DocumentProtos.DocumentMetadata doc, int level) {
        String docKey = DocumentWrapperUtils.getMainTitle(doc);
        docKey = StringTools.normalize(docKey);
        docKey = StringTools.removeStopWords(docKey);
        docKey = docKey.replaceAll("\\s", "");
        
        StringBuilder oddCharsSB = new StringBuilder();
        for (int i=0; i < docKey.length(); i += 2) {
            oddCharsSB.append(docKey.charAt(i));
        }
        docKey = oddCharsSB.toString();
        
        int keyLength = level*KEY_PART_LENGTH+KEY_PART_LENGTH;
        if (docKey.length() > keyLength) {
            docKey = docKey.substring(0, keyLength);
        }
        return docKey;
    }
}