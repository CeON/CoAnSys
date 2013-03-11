/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.logsanalysis.models;

import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import pl.edu.icm.synat.api.services.audit.model.AuditEntry;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public final class AuditEntryHelper {

    private AuditEntryHelper() {
    }
        
    public static AuditEntry getAuditEntry(final String eventId, final AuditEntry.Level level,
            final Date timestamp, final String serviceId, final String eventType,
            final Map<String, String> argsMap) {
        
        String[] args = new String[argsMap.size()];
        
        int i = 0;
        for (String argKey : argsMap.keySet()) {
            args[i] = "[" + argKey + "=" + argsMap.get(argKey) + "]";
            i++;
        }
        
        return new AuditEntry(eventId, level, timestamp, serviceId, eventType, args);
    }
    
    public static String getArg(final AuditEntry auditEntry, final String argKey) {
        
        Pattern paramPattern = Pattern.compile("\\[" + argKey + "=(.*)\\]");
        Matcher m;
        
        for (String arg : auditEntry.getArgs()) {
            m = paramPattern.matcher(arg);
            if (m.matches()) {
                return m.group(1);
            }
        }
        return "";
    }
}