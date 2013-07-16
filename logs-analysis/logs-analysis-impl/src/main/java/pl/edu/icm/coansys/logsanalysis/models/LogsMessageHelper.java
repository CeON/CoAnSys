/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.logsanalysis.models;

import pl.edu.icm.coansys.models.LogsProtos;
import java.util.Date;
import java.util.Map;
import java.util.Set;

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

        for (Map.Entry<String,String> entry : paramsMap.entrySet()) {
            LogsProtos.EventData.Builder paramBuilder = LogsProtos.EventData.newBuilder();
            paramBuilder.setParamName(entry.getKey());
            paramBuilder.setParamValue(entry.getValue());
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