/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.logsanalysis.models;

import java.util.Date;
import pl.edu.icm.synat.api.services.audit.model.AuditEntry;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class AuditEntryHelper {
    private AuditEntryHelper() {
    }
    
    public static AuditEntry getAuditEntry(final String eventId, final AuditEntry.Level level, 
            final Date timestamp, final String serviceId, final String eventType, 
            final String ipAddress, final String url, final String httpReferrer, final String sessionId, final String userId,
            final String... otherArgs) {
        /* usually, otherArgs[0] is a resource id */
        
        String[] args;
        
        if (otherArgs != null) {
            args = new String[5 + otherArgs.length];
            System.arraycopy(otherArgs, 0, args, 5, otherArgs.length);
        } else {
            args = new String[5];
        }
        args[0] = ipAddress;
        args[1] = url;
        args[2] = httpReferrer;
        args[3] = sessionId;
        args[4] = userId;

        return new AuditEntry(eventId, level, timestamp, serviceId, eventType, args);
    }

    public static String getIpAddress(final AuditEntry auditEntry) {
        return auditEntry.getArgs()[0];
    }

    public static String getUrl(final AuditEntry auditEntry) {
        return auditEntry.getArgs()[1];
    }

    public static String getHttpReferrer(final AuditEntry auditEntry) {
        return auditEntry.getArgs()[2];
    }

    public static String getSessionId(final AuditEntry auditEntry) {
        return auditEntry.getArgs()[3];
    }

    public static String getUserId(final AuditEntry auditEntry) {
        return auditEntry.getArgs()[4];
    }

     public static String getResourceId(final AuditEntry auditEntry) {
        return auditEntry.getArgs()[5];
    }
}