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
package pl.edu.icm.coansys.disambiguation.idgenerators;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * The method to generate uuid from a list of strings using {@link UUID}.
 *
 * @author pdendek
 * @version 1.0
 * @since 2012-08-07
 */
public class UuIdGenerator implements IdGenerator {

    @Override
    public String genetareId(List<String> args) {
        Collections.sort(args);
        StringBuilder builder = new StringBuilder();
        for (String contributionId : args) {
            builder.append(contributionId).append('\n');
        }
        try {
            String ret = UUID.nameUUIDFromBytes(builder.toString().getBytes("UTF-8")).toString();
            if ("".equals(ret)) {
                throw new IllegalStateException("UUID not generated");
            }
            return ret;
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Cannot happen", e);
        }
    }
}
