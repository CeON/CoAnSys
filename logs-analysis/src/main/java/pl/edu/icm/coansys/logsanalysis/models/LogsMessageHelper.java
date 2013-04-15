/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.logsanalysis.models;

import pl.edu.icm.coansys.importers.models.LogsProtos;
import java.util.Date;
import java.util.Map;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public final class LogsMessageHelper {

    private LogsMessageHelper() {
    }

    public static LogsProtos.LogsMessage createLogsMessage(final String eventId, final LogsProtos.LogsLevel level,
            final Date timestamp, final LogsProtos.EventType evtType,
            final Map<String, String> paramsMap) {

        LogsProtos.LogsMessage.Builder messageBuilder = LogsProtos.LogsMessage.newBuilder();

        messageBuilder.setEventId(eventId);
        messageBuilder.setLevel(level);
        messageBuilder.setTimestamp(timestamp.getTime());
        messageBuilder.setEventType(evtType);

        for (String paramName : paramsMap.keySet()) {
            LogsProtos.EventData.Builder paramBuilder = LogsProtos.EventData.newBuilder();
            paramBuilder.setParamName(paramName);
            paramBuilder.setParamValue(paramsMap.get(paramName));
            messageBuilder.addArg(paramBuilder);
        }

        return messageBuilder.build();
    }
    
    public static String getParam(final LogsProtos.LogsMessage message, final String argKey) {
        
        for (LogsProtos.EventData ed : message.getArgList()) {
            if (ed.getParamName().equals(argKey)) {
                return ed.getParamValue();
            }
        }
        return "";
    }
}