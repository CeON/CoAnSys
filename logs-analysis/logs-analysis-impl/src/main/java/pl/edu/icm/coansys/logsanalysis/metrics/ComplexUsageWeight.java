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

package pl.edu.icm.coansys.logsanalysis.metrics;

import java.util.EnumMap;
import java.util.Map;
import pl.edu.icm.coansys.models.LogsProtos;
import pl.edu.icm.coansys.models.LogsProtos.LogsMessage;

/**
 *
 * @author acz
 */
public class ComplexUsageWeight implements UsageWeight {

    private static final Map<LogsProtos.EventType, Long> weights = new EnumMap<LogsProtos.EventType, Long>(LogsProtos.EventType.class);
    static {
        weights.put(LogsProtos.EventType.EXPORT_METADATA, Long.valueOf(1));
        weights.put(LogsProtos.EventType.MARK_TO_READ, Long.valueOf(2));
        weights.put(LogsProtos.EventType.FETCH_CONTENT, Long.valueOf(2));
        weights.put(LogsProtos.EventType.RECOMMENDATION_EMAIL, Long.valueOf(3));
        weights.put(LogsProtos.EventType.VIEW_REFERENCES, Long.valueOf(1));

    }

    @Override
    public long getWeight(LogsMessage message) {
        for (LogsProtos.EventType et : weights.keySet()) {
            if (message.getEventType().equals(et)) {
                return weights.get(et);
            }
        }
        return 0;
    }
}
