/*
 * This file is part of CoAnSys project.
 * Copyright (c) 20012-2013 ICM-UW
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