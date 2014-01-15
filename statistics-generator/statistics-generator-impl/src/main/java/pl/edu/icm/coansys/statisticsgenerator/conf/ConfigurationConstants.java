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

package pl.edu.icm.coansys.statisticsgenerator.conf;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public final class ConfigurationConstants {
    
    private ConfigurationConstants() {}
    
    public static final String PARTITIONS_PREFIX = "partitions_";
    public static final String STATISTICS_PREFIX = "statistics_";

    public static final String NAMES = "names";
    public static final String CLASSES = "classes";
    public static final String CLASSES_ARGS = "classes_args";
    
    public static final String CONF_FIELDS_SEPARATOR = ";";
    public static final String CONF_PARAMS_SEPARATOR = "#";
    
    public static final Map<String, String> DEFAULT_CLASSES = new HashMap<String, String>();
    static {
        DEFAULT_CLASSES.put(PARTITIONS_PREFIX, "EQUALS");
        DEFAULT_CLASSES.put(STATISTICS_PREFIX, "COUNT");
    }
}
