/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.logsanalysis.metrics;

import pl.edu.icm.synat.api.services.audit.model.AuditEntry;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class SimpleUsageWeight implements UsageWeight {

    @Override
    public long getWeight(AuditEntry entry) {
        String eventType = entry.getEventType();
        if ("SAVE_TO_DISK".equals(eventType)) {
            return 1;
        } else {
            return 0;
        }
    }
}
