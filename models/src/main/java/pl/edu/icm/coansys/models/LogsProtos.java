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

package pl.edu.icm.coansys.models;

public final class LogsProtos {
  private LogsProtos() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public enum LogsLevel
      implements com.google.protobuf.ProtocolMessageEnum {
    FATAL(0, 0),
    ERROR(1, 1),
    WARN(2, 2),
    INFO(3, 3),
    DEBUG(4, 4),
    TRACE(5, 5),
    ;
    
    public static final int FATAL_VALUE = 0;
    public static final int ERROR_VALUE = 1;
    public static final int WARN_VALUE = 2;
    public static final int INFO_VALUE = 3;
    public static final int DEBUG_VALUE = 4;
    public static final int TRACE_VALUE = 5;
    
    
    public final int getNumber() { return value; }
    
    public static LogsLevel valueOf(int value) {
      switch (value) {
        case 0: return FATAL;
        case 1: return ERROR;
        case 2: return WARN;
        case 3: return INFO;
        case 4: return DEBUG;
        case 5: return TRACE;
        default: return null;
      }
    }
    
    public static com.google.protobuf.Internal.EnumLiteMap<LogsLevel>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static com.google.protobuf.Internal.EnumLiteMap<LogsLevel>
        internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<LogsLevel>() {
            public LogsLevel findValueByNumber(int number) {
              return LogsLevel.valueOf(number);
            }
          };
    
    public final com.google.protobuf.Descriptors.EnumValueDescriptor
        getValueDescriptor() {
      return getDescriptor().getValues().get(index);
    }
    public final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptorForType() {
      return getDescriptor();
    }
    public static final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptor() {
      return pl.edu.icm.coansys.models.LogsProtos.getDescriptor().getEnumTypes().get(0);
    }
    
    private static final LogsLevel[] VALUES = {
      FATAL, ERROR, WARN, INFO, DEBUG, TRACE, 
    };
    
    public static LogsLevel valueOf(
        com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
      if (desc.getType() != getDescriptor()) {
        throw new java.lang.IllegalArgumentException(
          "EnumValueDescriptor is not for this type.");
      }
      return VALUES[desc.getIndex()];
    }
    
    private final int index;
    private final int value;
    
    private LogsLevel(int index, int value) {
      this.index = index;
      this.value = value;
    }
    
    // @@protoc_insertion_point(enum_scope:pl.edu.icm.coansys.models.LogsLevel)
  }
  
  public enum EventType
      implements com.google.protobuf.ProtocolMessageEnum {
    MARK_TO_READ(0, 0),
    FETCH_CONTENT(1, 1),
    EXPORT_METADATA(2, 2),
    RECOMMENDATION_EMAIL(3, 3),
    VIEW_REFERENCES(4, 4),
    CUSTOM(5, 5),
    ;
    
    public static final int MARK_TO_READ_VALUE = 0;
    public static final int FETCH_CONTENT_VALUE = 1;
    public static final int EXPORT_METADATA_VALUE = 2;
    public static final int RECOMMENDATION_EMAIL_VALUE = 3;
    public static final int VIEW_REFERENCES_VALUE = 4;
    public static final int CUSTOM_VALUE = 5;
    
    
    public final int getNumber() { return value; }
    
    public static EventType valueOf(int value) {
      switch (value) {
        case 0: return MARK_TO_READ;
        case 1: return FETCH_CONTENT;
        case 2: return EXPORT_METADATA;
        case 3: return RECOMMENDATION_EMAIL;
        case 4: return VIEW_REFERENCES;
        case 5: return CUSTOM;
        default: return null;
      }
    }
    
    public static com.google.protobuf.Internal.EnumLiteMap<EventType>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static com.google.protobuf.Internal.EnumLiteMap<EventType>
        internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<EventType>() {
            public EventType findValueByNumber(int number) {
              return EventType.valueOf(number);
            }
          };
    
    public final com.google.protobuf.Descriptors.EnumValueDescriptor
        getValueDescriptor() {
      return getDescriptor().getValues().get(index);
    }
    public final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptorForType() {
      return getDescriptor();
    }
    public static final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptor() {
      return pl.edu.icm.coansys.models.LogsProtos.getDescriptor().getEnumTypes().get(1);
    }
    
    private static final EventType[] VALUES = {
      MARK_TO_READ, FETCH_CONTENT, EXPORT_METADATA, RECOMMENDATION_EMAIL, VIEW_REFERENCES, CUSTOM, 
    };
    
    public static EventType valueOf(
        com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
      if (desc.getType() != getDescriptor()) {
        throw new java.lang.IllegalArgumentException(
          "EnumValueDescriptor is not for this type.");
      }
      return VALUES[desc.getIndex()];
    }
    
    private final int index;
    private final int value;
    
    private EventType(int index, int value) {
      this.index = index;
      this.value = value;
    }
    
    // @@protoc_insertion_point(enum_scope:pl.edu.icm.coansys.models.EventType)
  }
  
  public interface LogsMessageOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // required string event_id = 1;
    boolean hasEventId();
    String getEventId();
    
    // required .pl.edu.icm.coansys.models.EventType event_type = 2;
    boolean hasEventType();
    pl.edu.icm.coansys.models.LogsProtos.EventType getEventType();
    
    // required .pl.edu.icm.coansys.models.LogsLevel level = 3;
    boolean hasLevel();
    pl.edu.icm.coansys.models.LogsProtos.LogsLevel getLevel();
    
    // required int64 timestamp = 4;
    boolean hasTimestamp();
    long getTimestamp();
    
    // optional string custom_event_source = 5;
    boolean hasCustomEventSource();
    String getCustomEventSource();
    
    // optional string custom_event_type = 6;
    boolean hasCustomEventType();
    String getCustomEventType();
    
    // repeated .pl.edu.icm.coansys.models.EventData arg = 7;
    java.util.List<pl.edu.icm.coansys.models.LogsProtos.EventData> 
        getArgList();
    pl.edu.icm.coansys.models.LogsProtos.EventData getArg(int index);
    int getArgCount();
    java.util.List<? extends pl.edu.icm.coansys.models.LogsProtos.EventDataOrBuilder> 
        getArgOrBuilderList();
    pl.edu.icm.coansys.models.LogsProtos.EventDataOrBuilder getArgOrBuilder(
        int index);
  }
  public static final class LogsMessage extends
      com.google.protobuf.GeneratedMessage
      implements LogsMessageOrBuilder {
    // Use LogsMessage.newBuilder() to construct.
    private LogsMessage(Builder builder) {
      super(builder);
    }
    private LogsMessage(boolean noInit) {}
    
    private static final LogsMessage defaultInstance;
    public static LogsMessage getDefaultInstance() {
      return defaultInstance;
    }
    
    public LogsMessage getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return pl.edu.icm.coansys.models.LogsProtos.internal_static_pl_edu_icm_coansys_models_LogsMessage_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return pl.edu.icm.coansys.models.LogsProtos.internal_static_pl_edu_icm_coansys_models_LogsMessage_fieldAccessorTable;
    }
    
    private int bitField0_;
    // required string event_id = 1;
    public static final int EVENT_ID_FIELD_NUMBER = 1;
    private java.lang.Object eventId_;
    public boolean hasEventId() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    public String getEventId() {
      java.lang.Object ref = eventId_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          eventId_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getEventIdBytes() {
      java.lang.Object ref = eventId_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        eventId_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // required .pl.edu.icm.coansys.models.EventType event_type = 2;
    public static final int EVENT_TYPE_FIELD_NUMBER = 2;
    private pl.edu.icm.coansys.models.LogsProtos.EventType eventType_;
    public boolean hasEventType() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    public pl.edu.icm.coansys.models.LogsProtos.EventType getEventType() {
      return eventType_;
    }
    
    // required .pl.edu.icm.coansys.models.LogsLevel level = 3;
    public static final int LEVEL_FIELD_NUMBER = 3;
    private pl.edu.icm.coansys.models.LogsProtos.LogsLevel level_;
    public boolean hasLevel() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    public pl.edu.icm.coansys.models.LogsProtos.LogsLevel getLevel() {
      return level_;
    }
    
    // required int64 timestamp = 4;
    public static final int TIMESTAMP_FIELD_NUMBER = 4;
    private long timestamp_;
    public boolean hasTimestamp() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    public long getTimestamp() {
      return timestamp_;
    }
    
    // optional string custom_event_source = 5;
    public static final int CUSTOM_EVENT_SOURCE_FIELD_NUMBER = 5;
    private java.lang.Object customEventSource_;
    public boolean hasCustomEventSource() {
      return ((bitField0_ & 0x00000010) == 0x00000010);
    }
    public String getCustomEventSource() {
      java.lang.Object ref = customEventSource_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          customEventSource_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getCustomEventSourceBytes() {
      java.lang.Object ref = customEventSource_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        customEventSource_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // optional string custom_event_type = 6;
    public static final int CUSTOM_EVENT_TYPE_FIELD_NUMBER = 6;
    private java.lang.Object customEventType_;
    public boolean hasCustomEventType() {
      return ((bitField0_ & 0x00000020) == 0x00000020);
    }
    public String getCustomEventType() {
      java.lang.Object ref = customEventType_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          customEventType_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getCustomEventTypeBytes() {
      java.lang.Object ref = customEventType_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        customEventType_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // repeated .pl.edu.icm.coansys.models.EventData arg = 7;
    public static final int ARG_FIELD_NUMBER = 7;
    private java.util.List<pl.edu.icm.coansys.models.LogsProtos.EventData> arg_;
    public java.util.List<pl.edu.icm.coansys.models.LogsProtos.EventData> getArgList() {
      return arg_;
    }
    public java.util.List<? extends pl.edu.icm.coansys.models.LogsProtos.EventDataOrBuilder> 
        getArgOrBuilderList() {
      return arg_;
    }
    public int getArgCount() {
      return arg_.size();
    }
    public pl.edu.icm.coansys.models.LogsProtos.EventData getArg(int index) {
      return arg_.get(index);
    }
    public pl.edu.icm.coansys.models.LogsProtos.EventDataOrBuilder getArgOrBuilder(
        int index) {
      return arg_.get(index);
    }
    
    private void initFields() {
      eventId_ = "";
      eventType_ = pl.edu.icm.coansys.models.LogsProtos.EventType.MARK_TO_READ;
      level_ = pl.edu.icm.coansys.models.LogsProtos.LogsLevel.FATAL;
      timestamp_ = 0L;
      customEventSource_ = "";
      customEventType_ = "";
      arg_ = java.util.Collections.emptyList();
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
      if (!hasEventId()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasEventType()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasLevel()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasTimestamp()) {
        memoizedIsInitialized = 0;
        return false;
      }
      for (int i = 0; i < getArgCount(); i++) {
        if (!getArg(i).isInitialized()) {
          memoizedIsInitialized = 0;
          return false;
        }
      }
      memoizedIsInitialized = 1;
      return true;
    }
    
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeBytes(1, getEventIdBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeEnum(2, eventType_.getNumber());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeEnum(3, level_.getNumber());
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeInt64(4, timestamp_);
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        output.writeBytes(5, getCustomEventSourceBytes());
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        output.writeBytes(6, getCustomEventTypeBytes());
      }
      for (int i = 0; i < arg_.size(); i++) {
        output.writeMessage(7, arg_.get(i));
      }
      getUnknownFields().writeTo(output);
    }
    
    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;
    
      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(1, getEventIdBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeEnumSize(2, eventType_.getNumber());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeEnumSize(3, level_.getNumber());
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt64Size(4, timestamp_);
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(5, getCustomEventSourceBytes());
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(6, getCustomEventTypeBytes());
      }
      for (int i = 0; i < arg_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(7, arg_.get(i));
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }
    
    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }
    
    public static pl.edu.icm.coansys.models.LogsProtos.LogsMessage parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static pl.edu.icm.coansys.models.LogsProtos.LogsMessage parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.models.LogsProtos.LogsMessage parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static pl.edu.icm.coansys.models.LogsProtos.LogsMessage parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.models.LogsProtos.LogsMessage parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static pl.edu.icm.coansys.models.LogsProtos.LogsMessage parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.models.LogsProtos.LogsMessage parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static pl.edu.icm.coansys.models.LogsProtos.LogsMessage parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input, extensionRegistry)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static pl.edu.icm.coansys.models.LogsProtos.LogsMessage parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static pl.edu.icm.coansys.models.LogsProtos.LogsMessage parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(pl.edu.icm.coansys.models.LogsProtos.LogsMessage prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }
    
    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder>
       implements pl.edu.icm.coansys.models.LogsProtos.LogsMessageOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return pl.edu.icm.coansys.models.LogsProtos.internal_static_pl_edu_icm_coansys_models_LogsMessage_descriptor;
      }
      
      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return pl.edu.icm.coansys.models.LogsProtos.internal_static_pl_edu_icm_coansys_models_LogsMessage_fieldAccessorTable;
      }
      
      // Construct using pl.edu.icm.coansys.models.LogsProtos.LogsMessage.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }
      
      private Builder(BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getArgFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }
      
      public Builder clear() {
        super.clear();
        eventId_ = "";
        bitField0_ = (bitField0_ & ~0x00000001);
        eventType_ = pl.edu.icm.coansys.models.LogsProtos.EventType.MARK_TO_READ;
        bitField0_ = (bitField0_ & ~0x00000002);
        level_ = pl.edu.icm.coansys.models.LogsProtos.LogsLevel.FATAL;
        bitField0_ = (bitField0_ & ~0x00000004);
        timestamp_ = 0L;
        bitField0_ = (bitField0_ & ~0x00000008);
        customEventSource_ = "";
        bitField0_ = (bitField0_ & ~0x00000010);
        customEventType_ = "";
        bitField0_ = (bitField0_ & ~0x00000020);
        if (argBuilder_ == null) {
          arg_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000040);
        } else {
          argBuilder_.clear();
        }
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return pl.edu.icm.coansys.models.LogsProtos.LogsMessage.getDescriptor();
      }
      
      public pl.edu.icm.coansys.models.LogsProtos.LogsMessage getDefaultInstanceForType() {
        return pl.edu.icm.coansys.models.LogsProtos.LogsMessage.getDefaultInstance();
      }
      
      public pl.edu.icm.coansys.models.LogsProtos.LogsMessage build() {
        pl.edu.icm.coansys.models.LogsProtos.LogsMessage result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private pl.edu.icm.coansys.models.LogsProtos.LogsMessage buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        pl.edu.icm.coansys.models.LogsProtos.LogsMessage result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public pl.edu.icm.coansys.models.LogsProtos.LogsMessage buildPartial() {
        pl.edu.icm.coansys.models.LogsProtos.LogsMessage result = new pl.edu.icm.coansys.models.LogsProtos.LogsMessage(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.eventId_ = eventId_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.eventType_ = eventType_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.level_ = level_;
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000008;
        }
        result.timestamp_ = timestamp_;
        if (((from_bitField0_ & 0x00000010) == 0x00000010)) {
          to_bitField0_ |= 0x00000010;
        }
        result.customEventSource_ = customEventSource_;
        if (((from_bitField0_ & 0x00000020) == 0x00000020)) {
          to_bitField0_ |= 0x00000020;
        }
        result.customEventType_ = customEventType_;
        if (argBuilder_ == null) {
          if (((bitField0_ & 0x00000040) == 0x00000040)) {
            arg_ = java.util.Collections.unmodifiableList(arg_);
            bitField0_ = (bitField0_ & ~0x00000040);
          }
          result.arg_ = arg_;
        } else {
          result.arg_ = argBuilder_.build();
        }
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof pl.edu.icm.coansys.models.LogsProtos.LogsMessage) {
          return mergeFrom((pl.edu.icm.coansys.models.LogsProtos.LogsMessage)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(pl.edu.icm.coansys.models.LogsProtos.LogsMessage other) {
        if (other == pl.edu.icm.coansys.models.LogsProtos.LogsMessage.getDefaultInstance()) return this;
        if (other.hasEventId()) {
          setEventId(other.getEventId());
        }
        if (other.hasEventType()) {
          setEventType(other.getEventType());
        }
        if (other.hasLevel()) {
          setLevel(other.getLevel());
        }
        if (other.hasTimestamp()) {
          setTimestamp(other.getTimestamp());
        }
        if (other.hasCustomEventSource()) {
          setCustomEventSource(other.getCustomEventSource());
        }
        if (other.hasCustomEventType()) {
          setCustomEventType(other.getCustomEventType());
        }
        if (argBuilder_ == null) {
          if (!other.arg_.isEmpty()) {
            if (arg_.isEmpty()) {
              arg_ = other.arg_;
              bitField0_ = (bitField0_ & ~0x00000040);
            } else {
              ensureArgIsMutable();
              arg_.addAll(other.arg_);
            }
            onChanged();
          }
        } else {
          if (!other.arg_.isEmpty()) {
            if (argBuilder_.isEmpty()) {
              argBuilder_.dispose();
              argBuilder_ = null;
              arg_ = other.arg_;
              bitField0_ = (bitField0_ & ~0x00000040);
              argBuilder_ = 
                com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                   getArgFieldBuilder() : null;
            } else {
              argBuilder_.addAllMessages(other.arg_);
            }
          }
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
        if (!hasEventId()) {
          
          return false;
        }
        if (!hasEventType()) {
          
          return false;
        }
        if (!hasLevel()) {
          
          return false;
        }
        if (!hasTimestamp()) {
          
          return false;
        }
        for (int i = 0; i < getArgCount(); i++) {
          if (!getArg(i).isInitialized()) {
            
            return false;
          }
        }
        return true;
      }
      
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder(
            this.getUnknownFields());
        while (true) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              this.setUnknownFields(unknownFields.build());
              onChanged();
              return this;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                this.setUnknownFields(unknownFields.build());
                onChanged();
                return this;
              }
              break;
            }
            case 10: {
              bitField0_ |= 0x00000001;
              eventId_ = input.readBytes();
              break;
            }
            case 16: {
              int rawValue = input.readEnum();
              pl.edu.icm.coansys.models.LogsProtos.EventType value = pl.edu.icm.coansys.models.LogsProtos.EventType.valueOf(rawValue);
              if (value == null) {
                unknownFields.mergeVarintField(2, rawValue);
              } else {
                bitField0_ |= 0x00000002;
                eventType_ = value;
              }
              break;
            }
            case 24: {
              int rawValue = input.readEnum();
              pl.edu.icm.coansys.models.LogsProtos.LogsLevel value = pl.edu.icm.coansys.models.LogsProtos.LogsLevel.valueOf(rawValue);
              if (value == null) {
                unknownFields.mergeVarintField(3, rawValue);
              } else {
                bitField0_ |= 0x00000004;
                level_ = value;
              }
              break;
            }
            case 32: {
              bitField0_ |= 0x00000008;
              timestamp_ = input.readInt64();
              break;
            }
            case 42: {
              bitField0_ |= 0x00000010;
              customEventSource_ = input.readBytes();
              break;
            }
            case 50: {
              bitField0_ |= 0x00000020;
              customEventType_ = input.readBytes();
              break;
            }
            case 58: {
              pl.edu.icm.coansys.models.LogsProtos.EventData.Builder subBuilder = pl.edu.icm.coansys.models.LogsProtos.EventData.newBuilder();
              input.readMessage(subBuilder, extensionRegistry);
              addArg(subBuilder.buildPartial());
              break;
            }
          }
        }
      }
      
      private int bitField0_;
      
      // required string event_id = 1;
      private java.lang.Object eventId_ = "";
      public boolean hasEventId() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      public String getEventId() {
        java.lang.Object ref = eventId_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          eventId_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setEventId(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        eventId_ = value;
        onChanged();
        return this;
      }
      public Builder clearEventId() {
        bitField0_ = (bitField0_ & ~0x00000001);
        eventId_ = getDefaultInstance().getEventId();
        onChanged();
        return this;
      }
      void setEventId(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000001;
        eventId_ = value;
        onChanged();
      }
      
      // required .pl.edu.icm.coansys.models.EventType event_type = 2;
      private pl.edu.icm.coansys.models.LogsProtos.EventType eventType_ = pl.edu.icm.coansys.models.LogsProtos.EventType.MARK_TO_READ;
      public boolean hasEventType() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      public pl.edu.icm.coansys.models.LogsProtos.EventType getEventType() {
        return eventType_;
      }
      public Builder setEventType(pl.edu.icm.coansys.models.LogsProtos.EventType value) {
        if (value == null) {
          throw new NullPointerException();
        }
        bitField0_ |= 0x00000002;
        eventType_ = value;
        onChanged();
        return this;
      }
      public Builder clearEventType() {
        bitField0_ = (bitField0_ & ~0x00000002);
        eventType_ = pl.edu.icm.coansys.models.LogsProtos.EventType.MARK_TO_READ;
        onChanged();
        return this;
      }
      
      // required .pl.edu.icm.coansys.models.LogsLevel level = 3;
      private pl.edu.icm.coansys.models.LogsProtos.LogsLevel level_ = pl.edu.icm.coansys.models.LogsProtos.LogsLevel.FATAL;
      public boolean hasLevel() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      public pl.edu.icm.coansys.models.LogsProtos.LogsLevel getLevel() {
        return level_;
      }
      public Builder setLevel(pl.edu.icm.coansys.models.LogsProtos.LogsLevel value) {
        if (value == null) {
          throw new NullPointerException();
        }
        bitField0_ |= 0x00000004;
        level_ = value;
        onChanged();
        return this;
      }
      public Builder clearLevel() {
        bitField0_ = (bitField0_ & ~0x00000004);
        level_ = pl.edu.icm.coansys.models.LogsProtos.LogsLevel.FATAL;
        onChanged();
        return this;
      }
      
      // required int64 timestamp = 4;
      private long timestamp_ ;
      public boolean hasTimestamp() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      public long getTimestamp() {
        return timestamp_;
      }
      public Builder setTimestamp(long value) {
        bitField0_ |= 0x00000008;
        timestamp_ = value;
        onChanged();
        return this;
      }
      public Builder clearTimestamp() {
        bitField0_ = (bitField0_ & ~0x00000008);
        timestamp_ = 0L;
        onChanged();
        return this;
      }
      
      // optional string custom_event_source = 5;
      private java.lang.Object customEventSource_ = "";
      public boolean hasCustomEventSource() {
        return ((bitField0_ & 0x00000010) == 0x00000010);
      }
      public String getCustomEventSource() {
        java.lang.Object ref = customEventSource_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          customEventSource_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setCustomEventSource(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000010;
        customEventSource_ = value;
        onChanged();
        return this;
      }
      public Builder clearCustomEventSource() {
        bitField0_ = (bitField0_ & ~0x00000010);
        customEventSource_ = getDefaultInstance().getCustomEventSource();
        onChanged();
        return this;
      }
      void setCustomEventSource(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000010;
        customEventSource_ = value;
        onChanged();
      }
      
      // optional string custom_event_type = 6;
      private java.lang.Object customEventType_ = "";
      public boolean hasCustomEventType() {
        return ((bitField0_ & 0x00000020) == 0x00000020);
      }
      public String getCustomEventType() {
        java.lang.Object ref = customEventType_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          customEventType_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setCustomEventType(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000020;
        customEventType_ = value;
        onChanged();
        return this;
      }
      public Builder clearCustomEventType() {
        bitField0_ = (bitField0_ & ~0x00000020);
        customEventType_ = getDefaultInstance().getCustomEventType();
        onChanged();
        return this;
      }
      void setCustomEventType(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000020;
        customEventType_ = value;
        onChanged();
      }
      
      // repeated .pl.edu.icm.coansys.models.EventData arg = 7;
      private java.util.List<pl.edu.icm.coansys.models.LogsProtos.EventData> arg_ =
        java.util.Collections.emptyList();
      private void ensureArgIsMutable() {
        if (!((bitField0_ & 0x00000040) == 0x00000040)) {
          arg_ = new java.util.ArrayList<pl.edu.icm.coansys.models.LogsProtos.EventData>(arg_);
          bitField0_ |= 0x00000040;
         }
      }
      
      private com.google.protobuf.RepeatedFieldBuilder<
          pl.edu.icm.coansys.models.LogsProtos.EventData, pl.edu.icm.coansys.models.LogsProtos.EventData.Builder, pl.edu.icm.coansys.models.LogsProtos.EventDataOrBuilder> argBuilder_;
      
      public java.util.List<pl.edu.icm.coansys.models.LogsProtos.EventData> getArgList() {
        if (argBuilder_ == null) {
          return java.util.Collections.unmodifiableList(arg_);
        } else {
          return argBuilder_.getMessageList();
        }
      }
      public int getArgCount() {
        if (argBuilder_ == null) {
          return arg_.size();
        } else {
          return argBuilder_.getCount();
        }
      }
      public pl.edu.icm.coansys.models.LogsProtos.EventData getArg(int index) {
        if (argBuilder_ == null) {
          return arg_.get(index);
        } else {
          return argBuilder_.getMessage(index);
        }
      }
      public Builder setArg(
          int index, pl.edu.icm.coansys.models.LogsProtos.EventData value) {
        if (argBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureArgIsMutable();
          arg_.set(index, value);
          onChanged();
        } else {
          argBuilder_.setMessage(index, value);
        }
        return this;
      }
      public Builder setArg(
          int index, pl.edu.icm.coansys.models.LogsProtos.EventData.Builder builderForValue) {
        if (argBuilder_ == null) {
          ensureArgIsMutable();
          arg_.set(index, builderForValue.build());
          onChanged();
        } else {
          argBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }
      public Builder addArg(pl.edu.icm.coansys.models.LogsProtos.EventData value) {
        if (argBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureArgIsMutable();
          arg_.add(value);
          onChanged();
        } else {
          argBuilder_.addMessage(value);
        }
        return this;
      }
      public Builder addArg(
          int index, pl.edu.icm.coansys.models.LogsProtos.EventData value) {
        if (argBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureArgIsMutable();
          arg_.add(index, value);
          onChanged();
        } else {
          argBuilder_.addMessage(index, value);
        }
        return this;
      }
      public Builder addArg(
          pl.edu.icm.coansys.models.LogsProtos.EventData.Builder builderForValue) {
        if (argBuilder_ == null) {
          ensureArgIsMutable();
          arg_.add(builderForValue.build());
          onChanged();
        } else {
          argBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }
      public Builder addArg(
          int index, pl.edu.icm.coansys.models.LogsProtos.EventData.Builder builderForValue) {
        if (argBuilder_ == null) {
          ensureArgIsMutable();
          arg_.add(index, builderForValue.build());
          onChanged();
        } else {
          argBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }
      public Builder addAllArg(
          java.lang.Iterable<? extends pl.edu.icm.coansys.models.LogsProtos.EventData> values) {
        if (argBuilder_ == null) {
          ensureArgIsMutable();
          super.addAll(values, arg_);
          onChanged();
        } else {
          argBuilder_.addAllMessages(values);
        }
        return this;
      }
      public Builder clearArg() {
        if (argBuilder_ == null) {
          arg_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000040);
          onChanged();
        } else {
          argBuilder_.clear();
        }
        return this;
      }
      public Builder removeArg(int index) {
        if (argBuilder_ == null) {
          ensureArgIsMutable();
          arg_.remove(index);
          onChanged();
        } else {
          argBuilder_.remove(index);
        }
        return this;
      }
      public pl.edu.icm.coansys.models.LogsProtos.EventData.Builder getArgBuilder(
          int index) {
        return getArgFieldBuilder().getBuilder(index);
      }
      public pl.edu.icm.coansys.models.LogsProtos.EventDataOrBuilder getArgOrBuilder(
          int index) {
        if (argBuilder_ == null) {
          return arg_.get(index);  } else {
          return argBuilder_.getMessageOrBuilder(index);
        }
      }
      public java.util.List<? extends pl.edu.icm.coansys.models.LogsProtos.EventDataOrBuilder> 
           getArgOrBuilderList() {
        if (argBuilder_ != null) {
          return argBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(arg_);
        }
      }
      public pl.edu.icm.coansys.models.LogsProtos.EventData.Builder addArgBuilder() {
        return getArgFieldBuilder().addBuilder(
            pl.edu.icm.coansys.models.LogsProtos.EventData.getDefaultInstance());
      }
      public pl.edu.icm.coansys.models.LogsProtos.EventData.Builder addArgBuilder(
          int index) {
        return getArgFieldBuilder().addBuilder(
            index, pl.edu.icm.coansys.models.LogsProtos.EventData.getDefaultInstance());
      }
      public java.util.List<pl.edu.icm.coansys.models.LogsProtos.EventData.Builder> 
           getArgBuilderList() {
        return getArgFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilder<
          pl.edu.icm.coansys.models.LogsProtos.EventData, pl.edu.icm.coansys.models.LogsProtos.EventData.Builder, pl.edu.icm.coansys.models.LogsProtos.EventDataOrBuilder> 
          getArgFieldBuilder() {
        if (argBuilder_ == null) {
          argBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
              pl.edu.icm.coansys.models.LogsProtos.EventData, pl.edu.icm.coansys.models.LogsProtos.EventData.Builder, pl.edu.icm.coansys.models.LogsProtos.EventDataOrBuilder>(
                  arg_,
                  ((bitField0_ & 0x00000040) == 0x00000040),
                  getParentForChildren(),
                  isClean());
          arg_ = null;
        }
        return argBuilder_;
      }
      
      // @@protoc_insertion_point(builder_scope:pl.edu.icm.coansys.models.LogsMessage)
    }
    
    static {
      defaultInstance = new LogsMessage(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:pl.edu.icm.coansys.models.LogsMessage)
  }
  
  public interface EventDataOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // required string param_name = 1;
    boolean hasParamName();
    String getParamName();
    
    // required string param_value = 2;
    boolean hasParamValue();
    String getParamValue();
  }
  public static final class EventData extends
      com.google.protobuf.GeneratedMessage
      implements EventDataOrBuilder {
    // Use EventData.newBuilder() to construct.
    private EventData(Builder builder) {
      super(builder);
    }
    private EventData(boolean noInit) {}
    
    private static final EventData defaultInstance;
    public static EventData getDefaultInstance() {
      return defaultInstance;
    }
    
    public EventData getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return pl.edu.icm.coansys.models.LogsProtos.internal_static_pl_edu_icm_coansys_models_EventData_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return pl.edu.icm.coansys.models.LogsProtos.internal_static_pl_edu_icm_coansys_models_EventData_fieldAccessorTable;
    }
    
    private int bitField0_;
    // required string param_name = 1;
    public static final int PARAM_NAME_FIELD_NUMBER = 1;
    private java.lang.Object paramName_;
    public boolean hasParamName() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    public String getParamName() {
      java.lang.Object ref = paramName_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          paramName_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getParamNameBytes() {
      java.lang.Object ref = paramName_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        paramName_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // required string param_value = 2;
    public static final int PARAM_VALUE_FIELD_NUMBER = 2;
    private java.lang.Object paramValue_;
    public boolean hasParamValue() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    public String getParamValue() {
      java.lang.Object ref = paramValue_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          paramValue_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getParamValueBytes() {
      java.lang.Object ref = paramValue_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        paramValue_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    private void initFields() {
      paramName_ = "";
      paramValue_ = "";
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
      if (!hasParamName()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasParamValue()) {
        memoizedIsInitialized = 0;
        return false;
      }
      memoizedIsInitialized = 1;
      return true;
    }
    
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeBytes(1, getParamNameBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeBytes(2, getParamValueBytes());
      }
      getUnknownFields().writeTo(output);
    }
    
    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;
    
      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(1, getParamNameBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(2, getParamValueBytes());
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }
    
    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }
    
    public static pl.edu.icm.coansys.models.LogsProtos.EventData parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static pl.edu.icm.coansys.models.LogsProtos.EventData parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.models.LogsProtos.EventData parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static pl.edu.icm.coansys.models.LogsProtos.EventData parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.models.LogsProtos.EventData parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static pl.edu.icm.coansys.models.LogsProtos.EventData parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.models.LogsProtos.EventData parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static pl.edu.icm.coansys.models.LogsProtos.EventData parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input, extensionRegistry)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static pl.edu.icm.coansys.models.LogsProtos.EventData parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static pl.edu.icm.coansys.models.LogsProtos.EventData parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(pl.edu.icm.coansys.models.LogsProtos.EventData prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }
    
    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder>
       implements pl.edu.icm.coansys.models.LogsProtos.EventDataOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return pl.edu.icm.coansys.models.LogsProtos.internal_static_pl_edu_icm_coansys_models_EventData_descriptor;
      }
      
      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return pl.edu.icm.coansys.models.LogsProtos.internal_static_pl_edu_icm_coansys_models_EventData_fieldAccessorTable;
      }
      
      // Construct using pl.edu.icm.coansys.models.LogsProtos.EventData.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }
      
      private Builder(BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
        }
      }
      private static Builder create() {
        return new Builder();
      }
      
      public Builder clear() {
        super.clear();
        paramName_ = "";
        bitField0_ = (bitField0_ & ~0x00000001);
        paramValue_ = "";
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return pl.edu.icm.coansys.models.LogsProtos.EventData.getDescriptor();
      }
      
      public pl.edu.icm.coansys.models.LogsProtos.EventData getDefaultInstanceForType() {
        return pl.edu.icm.coansys.models.LogsProtos.EventData.getDefaultInstance();
      }
      
      public pl.edu.icm.coansys.models.LogsProtos.EventData build() {
        pl.edu.icm.coansys.models.LogsProtos.EventData result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private pl.edu.icm.coansys.models.LogsProtos.EventData buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        pl.edu.icm.coansys.models.LogsProtos.EventData result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public pl.edu.icm.coansys.models.LogsProtos.EventData buildPartial() {
        pl.edu.icm.coansys.models.LogsProtos.EventData result = new pl.edu.icm.coansys.models.LogsProtos.EventData(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.paramName_ = paramName_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.paramValue_ = paramValue_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof pl.edu.icm.coansys.models.LogsProtos.EventData) {
          return mergeFrom((pl.edu.icm.coansys.models.LogsProtos.EventData)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(pl.edu.icm.coansys.models.LogsProtos.EventData other) {
        if (other == pl.edu.icm.coansys.models.LogsProtos.EventData.getDefaultInstance()) return this;
        if (other.hasParamName()) {
          setParamName(other.getParamName());
        }
        if (other.hasParamValue()) {
          setParamValue(other.getParamValue());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
        if (!hasParamName()) {
          
          return false;
        }
        if (!hasParamValue()) {
          
          return false;
        }
        return true;
      }
      
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder(
            this.getUnknownFields());
        while (true) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              this.setUnknownFields(unknownFields.build());
              onChanged();
              return this;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                this.setUnknownFields(unknownFields.build());
                onChanged();
                return this;
              }
              break;
            }
            case 10: {
              bitField0_ |= 0x00000001;
              paramName_ = input.readBytes();
              break;
            }
            case 18: {
              bitField0_ |= 0x00000002;
              paramValue_ = input.readBytes();
              break;
            }
          }
        }
      }
      
      private int bitField0_;
      
      // required string param_name = 1;
      private java.lang.Object paramName_ = "";
      public boolean hasParamName() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      public String getParamName() {
        java.lang.Object ref = paramName_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          paramName_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setParamName(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        paramName_ = value;
        onChanged();
        return this;
      }
      public Builder clearParamName() {
        bitField0_ = (bitField0_ & ~0x00000001);
        paramName_ = getDefaultInstance().getParamName();
        onChanged();
        return this;
      }
      void setParamName(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000001;
        paramName_ = value;
        onChanged();
      }
      
      // required string param_value = 2;
      private java.lang.Object paramValue_ = "";
      public boolean hasParamValue() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      public String getParamValue() {
        java.lang.Object ref = paramValue_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          paramValue_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setParamValue(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        paramValue_ = value;
        onChanged();
        return this;
      }
      public Builder clearParamValue() {
        bitField0_ = (bitField0_ & ~0x00000002);
        paramValue_ = getDefaultInstance().getParamValue();
        onChanged();
        return this;
      }
      void setParamValue(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000002;
        paramValue_ = value;
        onChanged();
      }
      
      // @@protoc_insertion_point(builder_scope:pl.edu.icm.coansys.models.EventData)
    }
    
    static {
      defaultInstance = new EventData(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:pl.edu.icm.coansys.models.EventData)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_pl_edu_icm_coansys_models_LogsMessage_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_pl_edu_icm_coansys_models_LogsMessage_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_pl_edu_icm_coansys_models_EventData_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_pl_edu_icm_coansys_models_EventData_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\021logsmessage.proto\022\031pl.edu.icm.coansys." +
      "models\"\214\002\n\013LogsMessage\022\020\n\010event_id\030\001 \002(\t" +
      "\0228\n\nevent_type\030\002 \002(\0162$.pl.edu.icm.coansy" +
      "s.models.EventType\0223\n\005level\030\003 \002(\0162$.pl.e" +
      "du.icm.coansys.models.LogsLevel\022\021\n\ttimes" +
      "tamp\030\004 \002(\003\022\033\n\023custom_event_source\030\005 \001(\t\022" +
      "\031\n\021custom_event_type\030\006 \001(\t\0221\n\003arg\030\007 \003(\0132" +
      "$.pl.edu.icm.coansys.models.EventData\"4\n" +
      "\tEventData\022\022\n\nparam_name\030\001 \002(\t\022\023\n\013param_" +
      "value\030\002 \002(\t*K\n\tLogsLevel\022\t\n\005FATAL\020\000\022\t\n\005E",
      "RROR\020\001\022\010\n\004WARN\020\002\022\010\n\004INFO\020\003\022\t\n\005DEBUG\020\004\022\t\n" +
      "\005TRACE\020\005*\200\001\n\tEventType\022\020\n\014MARK_TO_READ\020\000" +
      "\022\021\n\rFETCH_CONTENT\020\001\022\023\n\017EXPORT_METADATA\020\002" +
      "\022\030\n\024RECOMMENDATION_EMAIL\020\003\022\023\n\017VIEW_REFER" +
      "ENCES\020\004\022\n\n\006CUSTOM\020\005B\'\n\031pl.edu.icm.coansy" +
      "s.modelsB\nLogsProtos"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_pl_edu_icm_coansys_models_LogsMessage_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_pl_edu_icm_coansys_models_LogsMessage_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_pl_edu_icm_coansys_models_LogsMessage_descriptor,
              new java.lang.String[] { "EventId", "EventType", "Level", "Timestamp", "CustomEventSource", "CustomEventType", "Arg", },
              pl.edu.icm.coansys.models.LogsProtos.LogsMessage.class,
              pl.edu.icm.coansys.models.LogsProtos.LogsMessage.Builder.class);
          internal_static_pl_edu_icm_coansys_models_EventData_descriptor =
            getDescriptor().getMessageTypes().get(1);
          internal_static_pl_edu_icm_coansys_models_EventData_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_pl_edu_icm_coansys_models_EventData_descriptor,
              new java.lang.String[] { "ParamName", "ParamValue", },
              pl.edu.icm.coansys.models.LogsProtos.EventData.class,
              pl.edu.icm.coansys.models.LogsProtos.EventData.Builder.class);
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
  }
  
  // @@protoc_insertion_point(outer_class_scope)
}
