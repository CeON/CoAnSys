/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.logsanalysis.transformers;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import pl.edu.icm.coansys.importers.models.LogsProtos;
import pl.edu.icm.coansys.importers.models.LogsProtos.EventData;
import pl.edu.icm.coansys.importers.models.LogsProtos.EventData.Builder;
import pl.edu.icm.synat.api.services.audit.model.AuditEntry;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class AuditEntry2Protos {

    private enum EventTypeConstants {

        /*
         * event source, event type, enum from protobuf
         */
        MARK_TO_READ("collectionsAssignElement", "addToSpecialCollection", LogsProtos.EventType.MARK_TO_READ),
        FETCH_CONTENT("repositoryFacade", "fetchContent", LogsProtos.EventType.FETCH_CONTENT),
        EXPORT_METADATA("metadataExportingService", "exportMetadata", LogsProtos.EventType.EXPORT_METADATA),
        RECOMMENDATION_EMAIL("mailSharingService", "sendRecommendationMail", LogsProtos.EventType.RECOMMENDATION_EMAIL),
        VIEW_REFERENCES("publicationsReferences", "render", LogsProtos.EventType.VIEW_REFERENCES);

        private String eventSource;
        private String eventType;
        private LogsProtos.EventType protosEventType;

        private EventTypeConstants(String eventSource, String eventType, LogsProtos.EventType protosEventType) {
            this.eventSource = eventSource;
            this.eventType = eventType;
            this.protosEventType = protosEventType;
        }
    }

    public static LogsProtos.LogsMessage serialize(AuditEntry entry) {
        LogsProtos.LogsMessage.Builder messageBuilder = LogsProtos.LogsMessage.newBuilder();

        messageBuilder.setEventId(entry.getEventId());
        messageBuilder.setLevel(LogsProtos.LogsLevel.valueOf(entry.getLevel().name()));
        messageBuilder.setTimestamp(entry.getTimestamp().getTime());

        //event_type
        messageBuilder.setEventType(LogsProtos.EventType.CUSTOM);
        for (EventTypeConstants b : EventTypeConstants.values()) {
            if (b.eventSource.equals(entry.getServiceId()) && b.eventType.equals(entry.getEventType())) {
                messageBuilder.setEventType(b.protosEventType);
            }
        }
        if (messageBuilder.getEventType().equals(LogsProtos.EventType.CUSTOM)) {
            messageBuilder.setCustomEventSource(entry.getServiceId());
            messageBuilder.setCustomEventType(entry.getEventType());
        }

        //args
        Pattern paramPattern = Pattern.compile("\\[([^=]+)=(.*)\\]");
        Matcher m;

        for (String arg : entry.getArgs()) {
            m = paramPattern.matcher(arg);
            if (m.matches()) {
                Builder evtDataBuilder = LogsProtos.EventData.newBuilder();
                evtDataBuilder.setParamName(m.group(1));
                evtDataBuilder.setParamValue(m.group(2));

                messageBuilder.addArg(evtDataBuilder);
            }
        }

        return messageBuilder.build();
    }

    public static AuditEntry deserialize(LogsProtos.LogsMessage proto) {
        String serviceId = "";
        String eventType = "";
        String[] args = new String[proto.getArgCount()];

        if (proto.getEventType().equals(LogsProtos.EventType.CUSTOM)) {
            serviceId = proto.getCustomEventSource();
            eventType = proto.getCustomEventType();
        } else {
            for (EventTypeConstants etcons : EventTypeConstants.values()) {
                if (proto.getEventType().equals(etcons.protosEventType)) {
                    serviceId = etcons.eventSource;
                    eventType = etcons.eventType;
                    break;
                }
            }
        }

        for (int i = 0; i < proto.getArgCount(); i ++) {
            EventData arg = proto.getArg(i);
            args[i] = "[" + arg.getParamName() + "=" + arg.getParamValue() + "]";
        }

        return new AuditEntry(proto.getEventId(), AuditEntry.Level.valueOf(proto.getLevel().name()),
                new Date(proto.getTimestamp()), serviceId, eventType, args);
    }
}
