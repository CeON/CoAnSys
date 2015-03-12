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

import java.util.ArrayList;
import java.util.List;
import pl.edu.icm.coansys.models.StatisticsProtos;
import pl.edu.icm.coansys.models.StatisticsProtos.KeyValue;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class FilterTwoFieldsEqual implements FilterComponent {

    private List<String> fieldNames = new ArrayList<String>(2);

    @Override
    public boolean filter(StatisticsProtos.InputEntry entry) {
        String firstKey = null;
        String firstValue = null;
        
        for (KeyValue kv : entry.getFieldList()) {
            if (fieldNames.contains(kv.getKey()) && !kv.getKey().equals(firstKey)) {
                if (firstValue == null) {
                    firstKey = kv.getKey();
                    firstValue = kv.getValue();
                } else {
                    return firstValue.equals(kv.getValue());
                }
            }
        }
        return false;
    }

    @Override
    public void setup(String... params) {
        if (params.length != 2) {
            throw new IllegalArgumentException("setup requires 2 parameters: field name and value");
        }
        fieldNames.add(params[0]);
        fieldNames.add(params[1]);
    }
}
