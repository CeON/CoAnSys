/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.logsanalysis.constants;

/**
 *
 * @author acz
 */

public enum ServicesEventsWeights {
    
    /* serviceId, eventType, defaultWeight */
    
    MARK_TO_READ ("collectionsAssignElement", "addToSpecialCollection", 2),
    FETCH_CONTENT ("repositoryFacade", "fetchContent", 2),
    EXPORT_METADATA ("metadataExportingService", "exportMetadata", 1),
    RECOMMENDATION_MAIL ("mailSharingService", "sendRecommendationMail", 3),
    VIEW_REFERENCES ("publicationsReferences", "render", 1);
    
    private String serviceId;
    private String eventType;
    private long defaultWeight;
    
    private ServicesEventsWeights(String serviceId, String eventType, long defaultWeight) {
        this.serviceId = serviceId;
        this.eventType = eventType;
        this.defaultWeight = defaultWeight;
    }

    public String getEventType() {
        return eventType;
    }

    public String getServiceId() {
        return serviceId;
    }

    public long getDefaultWeight() {
        return defaultWeight;
    }
}