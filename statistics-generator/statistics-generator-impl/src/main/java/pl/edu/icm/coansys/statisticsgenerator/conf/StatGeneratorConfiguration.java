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
package pl.edu.icm.coansys.statisticsgenerator.conf;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.hadoop.conf.Configuration;
import pl.edu.icm.coansys.statisticsgenerator.filters.Filter;
import pl.edu.icm.coansys.statisticsgenerator.filters.InputFilter;
import pl.edu.icm.coansys.statisticsgenerator.operationcomponents.FilterComponent;
import pl.edu.icm.coansys.statisticsgenerator.operationcomponents.OperationComponent;
import pl.edu.icm.coansys.statisticsgenerator.operationcomponents.Partitioner;
import pl.edu.icm.coansys.statisticsgenerator.operationcomponents.StatisticCalculator;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
@SuppressWarnings({ "unchecked" })
public class StatGeneratorConfiguration {

    private Map<String, FilterComponent> inputFilterComponents;
    private Filter inputFilter;
    private Map<String, Partitioner> partitioners;
    private Map<String, StatisticCalculator> statisticCalculators;
    private String[] groupKeys;
    private String sortStat;
    private String sortOrder;
    private int limit = 0;

    public StatGeneratorConfiguration(Configuration conf) {
        registerUserComponents(conf);
        inputFilterComponents = readConfPartStat(conf, ConfigurationConstants.INPUT_FILTER_PREFIX);
        inputFilter = new InputFilter(conf.get(ConfigurationConstants.INPUT_FILTER_FORMULA), inputFilterComponents);
        partitioners = readConfPartStat(conf, ConfigurationConstants.PARTITIONS_PREFIX);
        statisticCalculators = readConfPartStat(conf, ConfigurationConstants.STATISTICS_PREFIX);
        groupKeys = readGroupKeys(conf);
        sortStat = readSortStat(conf);
        sortOrder = readSortOrder(conf);
        limit = readLimit(conf);
    }

    public Map<String, FilterComponent> getInputFilterComponents() {
        return inputFilterComponents;
    }

    public Filter getInputFilter() {
        return inputFilter;
    }

    public Map<String, Partitioner> getPartitioners() {
        return partitioners;
    }

    public Map<String, StatisticCalculator> getStatisticCalculators() {
        return statisticCalculators;
    }

    public String[] getGroupKeys() {
        return groupKeys;
    }

    public String getSortStat() {
        return sortStat;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public int getLimit() {
        return limit;
    }

    /**
     * ** private methods responsible for reading parts of configuration **
     */
    
    private static void registerUserComponents(Configuration conf) {
        String userComponentsLabelsStr = conf.get(ConfigurationConstants.USER_OPERATIONS_LABELS);
        String userComponentsClassesStr = conf.get(ConfigurationConstants.USER_OPERATIONS_CLASSES);
        
        if (userComponentsLabelsStr == null && userComponentsClassesStr == null) {
            return;
        }
        
        if (userComponentsLabelsStr == null || userComponentsClassesStr == null) {
            throw new IllegalArgumentException("Both " + ConfigurationConstants.USER_OPERATIONS_LABELS + " and " +
                    ConfigurationConstants.USER_OPERATIONS_CLASSES + " must be set or unset");
        }
        
        String[] userComponentsLabels = userComponentsLabelsStr.split(ConfigurationConstants.CONF_FIELDS_SEPARATOR);
        String[] userComponentsClasses = userComponentsClassesStr.split(ConfigurationConstants.CONF_FIELDS_SEPARATOR);
        
        if (userComponentsLabels.length != userComponentsClasses.length) {
            throw new IllegalArgumentException("Unequal fields count in " + 
                    ConfigurationConstants.USER_OPERATIONS_LABELS + " and " +
                    ConfigurationConstants.USER_OPERATIONS_CLASSES);
        }
        
        for (int i = 0; i < userComponentsLabels.length; i++) {
            Class<? extends OperationComponent> cls;
            try {
                cls = (Class<? extends OperationComponent>) Class.forName(userComponentsClasses[i]);
            } catch (ClassNotFoundException ex) {
                throw new IllegalArgumentException("Bad configuration: " + ex);
            }
            ComponentsMapping.registerOperationComponent(userComponentsLabels[i], cls);
        }
    }
    
    // partitions and statistics
    private static <T extends OperationComponent> SortedMap<String, T> readConfPartStat(Configuration conf, String confPrefix) {
        SortedMap<String, T> result = new TreeMap<String, T>();


        String operationsNamesStr = conf.get(confPrefix + ConfigurationConstants.NAMES);
        String operationsClassesStr = conf.get(confPrefix + ConfigurationConstants.CLASSES);
        String operationsClassesArgsStr = conf.get(confPrefix + ConfigurationConstants.CLASSES_ARGS);

        if (operationsNamesStr == null) {
            return null;
        }
        String[] operationsNames = operationsNamesStr.split(ConfigurationConstants.CONF_FIELDS_SEPARATOR);
        String[] operationsClasses;
        String[][] operationsClassesArgs;

        int operationsNb = operationsNames.length;

        if (operationsClassesStr != null) {
            operationsClasses = operationsClassesStr.split(ConfigurationConstants.CONF_FIELDS_SEPARATOR);
            if (operationsNb != operationsClasses.length) {
                throw new IllegalArgumentException("configuration error -- number of fields in "
                        + confPrefix + ConfigurationConstants.NAMES + " and "
                        + confPrefix + ConfigurationConstants.CLASSES + " doesn't match");
            }
        } else {
            operationsClasses = new String[operationsNb];
            for (int i = 0; i < operationsClasses.length; i++) {
                operationsClasses[i] = ConfigurationConstants.DEFAULT_CLASSES.get(confPrefix);
            }
        }

        operationsClassesArgs = new String[operationsNb][];
        if (operationsClassesArgsStr != null) {
            String[] groupedArgs = operationsClassesArgsStr.split(ConfigurationConstants.CONF_FIELDS_SEPARATOR);
            if (operationsNb != groupedArgs.length) {
                throw new IllegalArgumentException("configuration error -- number of fields in "
                        + confPrefix + ConfigurationConstants.CLASSES + " and "
                        + confPrefix + ConfigurationConstants.CLASSES_ARGS + " doesn't match");
            }
            operationsClassesArgs = new String[operationsNb][];
            for (int i = 0; i < operationsNb; i++) {
                operationsClassesArgs[i] = groupedArgs[i].split(ConfigurationConstants.CONF_PARAMS_SEPARATOR);
            }
        } else {
            for (int i = 0; i < operationsNb; i++) {
                operationsClassesArgs[i] = new String[0];
            }
        }


        for (int i = 0; i < operationsNb; i++) {
            String classLabel = operationsClasses[i];
            try {
                Class<? extends OperationComponent> pClass = ComponentsMapping.getOperationComponent(classLabel);
                T component = (T) pClass.newInstance();
                component.setup(operationsClassesArgs[i]);
                result.put(operationsNames[i], component);
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

    private static String[] readGroupKeys(Configuration conf) {
        String groupKeysStr = conf.get(ConfigurationConstants.GROUP_KEYS);
        if (groupKeysStr != null) {
            return groupKeysStr.split(ConfigurationConstants.CONF_FIELDS_SEPARATOR);
        } else {
            return null;
        }
    }

    private static String readSortStat(Configuration conf) {
        return conf.get(ConfigurationConstants.SORT_STAT);
    }

    private static String readSortOrder(Configuration conf) {
        String order = conf.get(ConfigurationConstants.SORT_ORDER);
        if (order == null || order.equals(ConfigurationConstants.SORT_ASC)) {
            return ConfigurationConstants.SORT_ASC;
        } else if (order.equals(ConfigurationConstants.SORT_DESC)) {
            return ConfigurationConstants.SORT_DESC;
        } else {
            throw new IllegalArgumentException("configuration error -- " + ConfigurationConstants.SORT_ORDER
                    + " must be " + ConfigurationConstants.SORT_ASC + " (default) or "
                    + ConfigurationConstants.SORT_DESC);
        }
    }

    private static int readLimit(Configuration conf) {
        String limitStr = conf.get(ConfigurationConstants.LIMIT);

        if (limitStr == null) {
            return 0;
        } else {
            int limit;
            try {
                limit = new Integer(limitStr);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("configuration error -- " + ConfigurationConstants.LIMIT + " " + limitStr + " is not a valid integer");
            }
            if (limit < 0) {
                throw new IllegalArgumentException("configuration error -- " + ConfigurationConstants.LIMIT + " cannot be negative");
            }
            return limit;
        }
    }
}