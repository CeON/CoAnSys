/*
 * (C) 2010-2012 ICM UW. All rights reserved.
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
