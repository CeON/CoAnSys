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

import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.hadoop.conf.Configuration;
import pl.edu.icm.coansys.statisticsgenerator.operationcomponents.OperationComponent;

/**
 *
 * @author acz
 */
public class ConfigReader {

    private ConfigReader() {}

    public static <T extends OperationComponent> SortedMap<String, T> readConf(Configuration conf, String confPrefix) {
        SortedMap<String, T> result = new TreeMap<String, T>();


        String partitionsNamesStr = conf.get(confPrefix + ConfigurationConstants.NAMES);
        String partitionsClassesStr = conf.get(confPrefix + ConfigurationConstants.CLASSES);
        String partitionsClassesArgsStr = conf.get(confPrefix + ConfigurationConstants.CLASSES_ARGS);

        if (partitionsNamesStr == null) {
            throw new IllegalArgumentException("configuration error -- " + confPrefix + ConfigurationConstants.NAMES + " must be set");
        }
        String[] partitionsNames = partitionsNamesStr.split(ConfigurationConstants.CONF_FIELDS_SEPARATOR);
        String[] partitionsClasses;
        String[][] partitionsClassesArgs;

        int partitionsNb = partitionsNames.length;

        if (partitionsClassesStr != null) {
            partitionsClasses = partitionsClassesStr.split(ConfigurationConstants.CONF_FIELDS_SEPARATOR);
            if (partitionsNb != partitionsClasses.length) {
                throw new IllegalArgumentException("configuration error -- number of partitions and classes doesn't match");
            }
        } else {
            partitionsClasses = new String[partitionsNb];
            for (int i = 0; i < partitionsClasses.length; i++) {
                partitionsClasses[i] = ConfigurationConstants.DEFAULT_CLASSES.get(confPrefix);
            }
        }

        partitionsClassesArgs = new String[partitionsNb][];
        if (partitionsClassesArgsStr != null) {
            String[] groupedArgs = partitionsClassesArgsStr.split(ConfigurationConstants.CONF_FIELDS_SEPARATOR);
            if (partitionsNb != groupedArgs.length) {
                throw new IllegalArgumentException("configuration error -- number of partitions and classes arguments doesn't match");
            }
            partitionsClassesArgs = new String[partitionsNb][];
            for (int i = 0; i < partitionsNb; i++) {
                partitionsClassesArgs[i] = groupedArgs[i].split(ConfigurationConstants.CONF_PARAMS_SEPARATOR);
            }
        } else {
            for (int i = 0; i < partitionsNb; i++) {
                partitionsClassesArgs[i] = new String[0];
            }
        }


        for (int i = 0; i < partitionsNb; i++) {
            String classLabel = partitionsClasses[i];
            try {
                Class<? extends OperationComponent> pClass = ComponentsMapping.mapping.get(classLabel);
                T component = (T) pClass.newInstance();
                component.setup(partitionsClassesArgs[i]);
                result.put(partitionsNames[i], component);
            } catch (InstantiationException ex) {
                throw new IllegalArgumentException("configuration error -- " + ex);
            } catch (IllegalAccessException ex) {
                throw new IllegalArgumentException("configuration error -- " + ex);
            } catch (NullPointerException ex) {
                throw new IllegalArgumentException("configuration error -- cannot find class for label " + classLabel);
            }
        }

        return result;
    }
}
