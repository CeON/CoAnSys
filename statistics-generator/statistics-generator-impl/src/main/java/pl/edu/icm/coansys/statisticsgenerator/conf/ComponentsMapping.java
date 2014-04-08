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
import pl.edu.icm.coansys.statisticsgenerator.operationcomponents.CountSummary;
import pl.edu.icm.coansys.statisticsgenerator.operationcomponents.DateRangesPartitioner;
import pl.edu.icm.coansys.statisticsgenerator.operationcomponents.EqualsPartitioner;
import pl.edu.icm.coansys.statisticsgenerator.operationcomponents.FilterEq;
import pl.edu.icm.coansys.statisticsgenerator.operationcomponents.FirstCharsPartitioner;
import pl.edu.icm.coansys.statisticsgenerator.operationcomponents.LastCharsPartitioner;
import pl.edu.icm.coansys.statisticsgenerator.operationcomponents.OperationComponent;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public final class ComponentsMapping {
    
    private ComponentsMapping() {}
    
    public static final Map<String, Class<? extends OperationComponent>> mapping = new HashMap<String, Class<? extends OperationComponent>>();

    static {
        mapping.put("EQFILTER", FilterEq.class);
        mapping.put("EQUALS", EqualsPartitioner.class);
        mapping.put("FIRSTCHARS", FirstCharsPartitioner.class);
        mapping.put("LASTCHARS", LastCharsPartitioner.class);
        mapping.put("DATERANGES", DateRangesPartitioner.class);
        mapping.put("COUNT", CountSummary.class);
    }
}
