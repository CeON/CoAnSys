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

package pl.edu.icm.coansys.statisticsgenerator.filters;

import java.util.Map;
import pl.edu.icm.coansys.models.StatisticsProtos;
import pl.edu.icm.coansys.statisticsgenerator.operationcomponents.FilterComponent;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class LogicalEvaluator extends AbstractLogicalEvaluator {
    
    private Map<String, FilterComponent> filterComponents;
    private StatisticsProtos.InputEntry inputEntry;

    public LogicalEvaluator(Map<String, FilterComponent> filterComponents) {
        this.filterComponents = filterComponents;
    }

    public void setInputEntry(StatisticsProtos.InputEntry inputEntry) {
        this.inputEntry = inputEntry;
    }
    
    @Override
    public boolean evaluateLiteral(String literal) {
        if (filterComponents.containsKey(literal)) {
            FilterComponent fc = filterComponents.get(literal);
            return fc.filter(inputEntry);
        }
        throw new IllegalArgumentException(literal + " is not a label of filter component");
    }
}
