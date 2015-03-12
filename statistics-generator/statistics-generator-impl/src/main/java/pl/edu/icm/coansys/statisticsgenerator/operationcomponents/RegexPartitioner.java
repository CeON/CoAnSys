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
package pl.edu.icm.coansys.statisticsgenerator.operationcomponents;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class RegexPartitioner implements Partitioner {

    private static Logger logger = LoggerFactory.getLogger(RegexPartitioner.class);
    private Pattern pattern;
    private int groupNb = 1;

    @Override
    public String[] partition(String inputField) {
        Matcher matcher = pattern.matcher(inputField);
        if (matcher.matches()) {
            if (matcher.groupCount() >= groupNb) {
                String[] result = new String[1];
                result[0] = matcher.group(groupNb);
                return result;
            } else {
                logger.info("insufficient groupCount: " + matcher.groupCount() + " < " + groupNb);
            }
        } else {
            logger.info(inputField + " doesn't match pattern " + pattern);
        }
        return new String[0];
    }

    @Override
    public void setup(String... params) {
        if (params.length < 1) {
            throw new IllegalArgumentException(this.getClass().getName() + " requires a regular expression pattern as first parameter and (optionally) a group number");
        }
        pattern = Pattern.compile(params[0]);
        if (params.length > 1) {
            try {
                groupNb = Integer.parseInt(params[1]);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException(ex);
            }
        }
    }
}
