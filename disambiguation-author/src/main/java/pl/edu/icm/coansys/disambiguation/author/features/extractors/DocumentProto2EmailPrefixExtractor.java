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

package pl.edu.icm.coansys.disambiguation.author.features.extractors;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.icm.coansys.disambiguation.author.features.extractors.indicators.AuthorBased;
import pl.edu.icm.coansys.disambiguation.features.Extractor;
import pl.edu.icm.coansys.models.DocumentProtos.Author;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata;

/**
 * The {@link Extractor} from {@link DocumentProtos.DocumentMetadata} (commit
 * 33ad120f11eb430d450) to a feature value list. Please read the description of
 * the {@link AuthorBased} interface.
 *
 * @author pdendek
 * @version 1.0
 * @since 2012-08-07
 */
public class DocumentProto2EmailPrefixExtractor implements Extractor<DocumentMetadata>, AuthorBased {

    private static Logger logger = LoggerFactory.getLogger(DocumentProto2EmailPrefixExtractor.class);

    @Override
    public List<String> extract(DocumentMetadata input, String... auxil) {
        String authId = auxil[0];
        DocumentMetadata dm = (DocumentMetadata) input;
        List<String> ret = new ArrayList<String>();

        for (Author a : dm.getBasicMetadata().getAuthorList()) {
            if (a.getKey().equals(authId)) {
                String email = a.getEmail();
                Matcher matcher = Pattern.compile("[^a-zA-Z]_\\.\\-0-9").matcher(email);
                if (matcher.find()) {
                    ret.add(email.substring(0, matcher.start()));
                    return ret;
                } else {
                    logger.error("Email \"" + a.getEmail() + "\" could not be parsed");
                }
            }
        }
        return ret;

    }
}
