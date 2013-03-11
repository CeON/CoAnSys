/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.logsanalysis.metrics;

import pl.edu.icm.coansys.logsanalysis.constants.ServicesEventsWeights;
import pl.edu.icm.synat.api.services.audit.model.AuditEntry;

/**
 *
 * @author acz
 */
public class ComplexUsageWeight implements UsageWeight {

    @Override
    public long getWeight(AuditEntry entry) {

        long result = 0;

        String serviceId = entry.getServiceId();
        String eventType = entry.getEventType();

        for (ServicesEventsWeights m : ServicesEventsWeights.values()) {
            if (m.getServiceId().equals(serviceId) && m.getEventType().equals(eventType)) {
                result += m.getDefaultWeight();
            }
        }

        return result;
    }
}
