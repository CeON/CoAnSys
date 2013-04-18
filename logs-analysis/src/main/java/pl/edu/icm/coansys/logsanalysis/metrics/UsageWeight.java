/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.logsanalysis.metrics;

import pl.edu.icm.coansys.importers.models.LogsProtos;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public interface UsageWeight {
    public long getWeight(LogsProtos.LogsMessage message);
    
}
