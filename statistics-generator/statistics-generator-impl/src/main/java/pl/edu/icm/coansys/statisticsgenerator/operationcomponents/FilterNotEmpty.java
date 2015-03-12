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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.icm.coansys.models.StatisticsProtos;
import pl.edu.icm.coansys.models.StatisticsProtos.KeyValue;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class FilterNotEmpty implements FilterComponent {
    
    private static final Logger logger = LoggerFactory.getLogger(FilterNotEmpty.class);
    
    private String fieldName;

    @Override
    public boolean filter(StatisticsProtos.InputEntry entry) {
        for (KeyValue kv : entry.getFieldList()) {
            if (kv.getKey().equals(fieldName)) {
                return !kv.getValue().isEmpty();
            }
        }
        return false;
    }

    @Override
    public void setup(String... params) {
        if (params.length != 1) {
            throw new IllegalArgumentException("setup requires 1 parameters: field name");
        }
        fieldName = params[0];
    }

}
