/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.logsanalysis.metrics;

import pl.edu.icm.coansys.models.LogsProtos;
import pl.edu.icm.coansys.models.LogsProtos.LogsMessage;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class SimpleUsageWeight implements UsageWeight {

    @Override
    public long getWeight(LogsMessage message) {
        if (message.getEventType().equals(LogsProtos.EventType.FETCH_CONTENT)) {
            return 1;
        } else {
            return 0;
        }
    }
}
