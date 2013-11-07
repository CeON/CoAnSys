/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2013 ICM-UW
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

public final class DocumentProtos {
  private DocumentProtos() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface ProvenanceInfoOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // required .ProvenanceInfo.SingleProvenanceInfo currentProvenance = 1;
    boolean hasCurrentProvenance();
    pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo getCurrentProvenance();
    pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfoOrBuilder getCurrentProvenanceOrBuilder();
    
    // repeated .ProvenanceInfo.SingleProvenanceInfo previousProvenances = 2;
    java.util.List<pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo> 
        getPreviousProvenancesList();
    pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo getPreviousProvenances(int index);
    int getPreviousProvenancesCount();
    java.util.List<? extends pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfoOrBuilder> 
        getPreviousProvenancesOrBuilderList();
    pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfoOrBuilder getPreviousProvenancesOrBuilder(
        int index);
  }
  public static final class ProvenanceInfo extends
      com.google.protobuf.GeneratedMessage
      implements ProvenanceInfoOrBuilder {
    // Use ProvenanceInfo.newBuilder() to construct.
    private ProvenanceInfo(Builder builder) {
      super(builder);
    }
    private ProvenanceInfo(boolean noInit) {}
    
    private static final ProvenanceInfo defaultInstance;
    public static ProvenanceInfo getDefaultInstance() {
      return defaultInstance;
    }
    
    public ProvenanceInfo getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return pl.edu.icm.coansys.models.DocumentProtos.internal_static_ProvenanceInfo_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return pl.edu.icm.coansys.models.DocumentProtos.internal_static_ProvenanceInfo_fieldAccessorTable;
    }
    
    public interface SingleProvenanceInfoOrBuilder
        extends com.google.protobuf.MessageOrBuilder {
      
      // required string lastModificationMarkerId = 1;
      boolean hasLastModificationMarkerId();
      String getLastModificationMarkerId();
      
      // required int64 lastModificationDate = 2;
      boolean hasLastModificationDate();
      long getLastModificationDate();
    }
    public static final class SingleProvenanceInfo extends
        com.google.protobuf.GeneratedMessage
        implements SingleProvenanceInfoOrBuilder {
      // Use SingleProvenanceInfo.newBuilder() to construct.
      private SingleProvenanceInfo(Builder builder) {
        super(builder);
      }
      private SingleProvenanceInfo(boolean noInit) {}
      
      private static final SingleProvenanceInfo defaultInstance;
      public static SingleProvenanceInfo getDefaultInstance() {
        return defaultInstance;
      }
      
      public SingleProvenanceInfo getDefaultInstanceForType() {
        return defaultInstance;
      }
      
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return pl.edu.icm.coansys.models.DocumentProtos.internal_static_ProvenanceInfo_SingleProvenanceInfo_descriptor;
      }
      
      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return pl.edu.icm.coansys.models.DocumentProtos.internal_static_ProvenanceInfo_SingleProvenanceInfo_fieldAccessorTable;
      }
      
      private int bitField0_;
      // required string lastModificationMarkerId = 1;
      public static final int LASTMODIFICATIONMARKERID_FIELD_NUMBER = 1;
      private java.lang.Object lastModificationMarkerId_;
      public boolean hasLastModificationMarkerId() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      public String getLastModificationMarkerId() {
        java.lang.Object ref = lastModificationMarkerId_;
        if (ref instanceof String) {
          return (String) ref;
        } else {
          com.google.protobuf.ByteString bs = 
              (com.google.protobuf.ByteString) ref;
          String s = bs.toStringUtf8();
          if (com.google.protobuf.Internal.isValidUtf8(bs)) {
            lastModificationMarkerId_ = s;
          }
          return s;
        }
      }
      private com.google.protobuf.ByteString getLastModificationMarkerIdBytes() {
        java.lang.Object ref = lastModificationMarkerId_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8((String) ref);
          lastModificationMarkerId_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      
      // required int64 lastModificationDate = 2;
      public static final int LASTMODIFICATIONDATE_FIELD_NUMBER = 2;
      private long lastModificationDate_;
      public boolean hasLastModificationDate() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      public long getLastModificationDate() {
        return lastModificationDate_;
      }
      
      private void initFields() {
        lastModificationMarkerId_ = "";
        lastModificationDate_ = 0L;
      }
      private byte memoizedIsInitialized = -1;
      public final boolean isInitialized() {
        byte isInitialized = memoizedIsInitialized;
        if (isInitialized != -1) return isInitialized == 1;
        
        if (!hasLastModificationMarkerId()) {
          memoizedIsInitialized = 0;
          return false;
        }
        if (!hasLastModificationDate()) {
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
          output.writeBytes(1, getLastModificationMarkerIdBytes());
        }
        if (((bitField0_ & 0x00000002) == 0x00000002)) {
          output.writeInt64(2, lastModificationDate_);
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
            .computeBytesSize(1, getLastModificationMarkerIdBytes());
        }
        if (((bitField0_ & 0x00000002) == 0x00000002)) {
          size += com.google.protobuf.CodedOutputStream
            .computeInt64Size(2, lastModificationDate_);
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
      
      public static pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo parseFrom(
          com.google.protobuf.ByteString data)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return newBuilder().mergeFrom(data).buildParsed();
      }
      public static pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo parseFrom(
          com.google.protobuf.ByteString data,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return newBuilder().mergeFrom(data, extensionRegistry)
                 .buildParsed();
      }
      public static pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo parseFrom(byte[] data)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return newBuilder().mergeFrom(data).buildParsed();
      }
      public static pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo parseFrom(
          byte[] data,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return newBuilder().mergeFrom(data, extensionRegistry)
                 .buildParsed();
      }
      public static pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo parseFrom(java.io.InputStream input)
          throws java.io.IOException {
        return newBuilder().mergeFrom(input).buildParsed();
      }
      public static pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo parseFrom(
          java.io.InputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return newBuilder().mergeFrom(input, extensionRegistry)
                 .buildParsed();
      }
      public static pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo parseDelimitedFrom(java.io.InputStream input)
          throws java.io.IOException {
        Builder builder = newBuilder();
        if (builder.mergeDelimitedFrom(input)) {
          return builder.buildParsed();
        } else {
          return null;
        }
      }
      public static pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo parseDelimitedFrom(
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
      public static pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo parseFrom(
          com.google.protobuf.CodedInputStream input)
          throws java.io.IOException {
        return newBuilder().mergeFrom(input).buildParsed();
      }
      public static pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo parseFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return newBuilder().mergeFrom(input, extensionRegistry)
                 .buildParsed();
      }
      
      public static Builder newBuilder() { return Builder.create(); }
      public Builder newBuilderForType() { return newBuilder(); }
      public static Builder newBuilder(pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo prototype) {
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
         implements pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfoOrBuilder {
        public static final com.google.protobuf.Descriptors.Descriptor
            getDescriptor() {
          return pl.edu.icm.coansys.models.DocumentProtos.internal_static_ProvenanceInfo_SingleProvenanceInfo_descriptor;
        }
        
        protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
            internalGetFieldAccessorTable() {
          return pl.edu.icm.coansys.models.DocumentProtos.internal_static_ProvenanceInfo_SingleProvenanceInfo_fieldAccessorTable;
        }
        
        // Construct using pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo.newBuilder()
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
          lastModificationMarkerId_ = "";
          bitField0_ = (bitField0_ & ~0x00000001);
          lastModificationDate_ = 0L;
          bitField0_ = (bitField0_ & ~0x00000002);
          return this;
        }
        
        public Builder clone() {
          return create().mergeFrom(buildPartial());
        }
        
        public com.google.protobuf.Descriptors.Descriptor
            getDescriptorForType() {
          return pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo.getDescriptor();
        }
        
        public pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo getDefaultInstanceForType() {
          return pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo.getDefaultInstance();
        }
        
        public pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo build() {
          pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo result = buildPartial();
          if (!result.isInitialized()) {
            throw newUninitializedMessageException(result);
          }
          return result;
        }
        
        private pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo buildParsed()
            throws com.google.protobuf.InvalidProtocolBufferException {
          pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo result = buildPartial();
          if (!result.isInitialized()) {
            throw newUninitializedMessageException(
              result).asInvalidProtocolBufferException();
          }
          return result;
        }
        
        public pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo buildPartial() {
          pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo result = new pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo(this);
          int from_bitField0_ = bitField0_;
          int to_bitField0_ = 0;
          if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
            to_bitField0_ |= 0x00000001;
          }
          result.lastModificationMarkerId_ = lastModificationMarkerId_;
          if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
            to_bitField0_ |= 0x00000002;
          }
          result.lastModificationDate_ = lastModificationDate_;
          result.bitField0_ = to_bitField0_;
          onBuilt();
          return result;
        }
        
        public Builder mergeFrom(com.google.protobuf.Message other) {
          if (other instanceof pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo) {
            return mergeFrom((pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo)other);
          } else {
            super.mergeFrom(other);
            return this;
          }
        }
        
        public Builder mergeFrom(pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo other) {
          if (other == pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo.getDefaultInstance()) return this;
          if (other.hasLastModificationMarkerId()) {
            setLastModificationMarkerId(other.getLastModificationMarkerId());
          }
          if (other.hasLastModificationDate()) {
            setLastModificationDate(other.getLastModificationDate());
          }
          this.mergeUnknownFields(other.getUnknownFields());
          return this;
        }
        
        public final boolean isInitialized() {
          if (!hasLastModificationMarkerId()) {
            
            return false;
          }
          if (!hasLastModificationDate()) {
            
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
                lastModificationMarkerId_ = input.readBytes();
                break;
              }
              case 16: {
                bitField0_ |= 0x00000002;
                lastModificationDate_ = input.readInt64();
                break;
              }
            }
          }
        }
        
        private int bitField0_;
        
        // required string lastModificationMarkerId = 1;
        private java.lang.Object lastModificationMarkerId_ = "";
        public boolean hasLastModificationMarkerId() {
          return ((bitField0_ & 0x00000001) == 0x00000001);
        }
        public String getLastModificationMarkerId() {
          java.lang.Object ref = lastModificationMarkerId_;
          if (!(ref instanceof String)) {
            String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
            lastModificationMarkerId_ = s;
            return s;
          } else {
            return (String) ref;
          }
        }
        public Builder setLastModificationMarkerId(String value) {
          if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
          lastModificationMarkerId_ = value;
          onChanged();
          return this;
        }
        public Builder clearLastModificationMarkerId() {
          bitField0_ = (bitField0_ & ~0x00000001);
          lastModificationMarkerId_ = getDefaultInstance().getLastModificationMarkerId();
          onChanged();
          return this;
        }
        void setLastModificationMarkerId(com.google.protobuf.ByteString value) {
          bitField0_ |= 0x00000001;
          lastModificationMarkerId_ = value;
          onChanged();
        }
        
        // required int64 lastModificationDate = 2;
        private long lastModificationDate_ ;
        public boolean hasLastModificationDate() {
          return ((bitField0_ & 0x00000002) == 0x00000002);
        }
        public long getLastModificationDate() {
          return lastModificationDate_;
        }
        public Builder setLastModificationDate(long value) {
          bitField0_ |= 0x00000002;
          lastModificationDate_ = value;
          onChanged();
          return this;
        }
        public Builder clearLastModificationDate() {
          bitField0_ = (bitField0_ & ~0x00000002);
          lastModificationDate_ = 0L;
          onChanged();
          return this;
        }
        
        // @@protoc_insertion_point(builder_scope:ProvenanceInfo.SingleProvenanceInfo)
      }
      
      static {
        defaultInstance = new SingleProvenanceInfo(true);
        defaultInstance.initFields();
      }
      
      // @@protoc_insertion_point(class_scope:ProvenanceInfo.SingleProvenanceInfo)
    }
    
    private int bitField0_;
    // required .ProvenanceInfo.SingleProvenanceInfo currentProvenance = 1;
    public static final int CURRENTPROVENANCE_FIELD_NUMBER = 1;
    private pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo currentProvenance_;
    public boolean hasCurrentProvenance() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    public pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo getCurrentProvenance() {
      return currentProvenance_;
    }
    public pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfoOrBuilder getCurrentProvenanceOrBuilder() {
      return currentProvenance_;
    }
    
    // repeated .ProvenanceInfo.SingleProvenanceInfo previousProvenances = 2;
    public static final int PREVIOUSPROVENANCES_FIELD_NUMBER = 2;
    private java.util.List<pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo> previousProvenances_;
    public java.util.List<pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo> getPreviousProvenancesList() {
      return previousProvenances_;
    }
    public java.util.List<? extends pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfoOrBuilder> 
        getPreviousProvenancesOrBuilderList() {
      return previousProvenances_;
    }
    public int getPreviousProvenancesCount() {
      return previousProvenances_.size();
    }
    public pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo getPreviousProvenances(int index) {
      return previousProvenances_.get(index);
    }
    public pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfoOrBuilder getPreviousProvenancesOrBuilder(
        int index) {
      return previousProvenances_.get(index);
    }
    
    private void initFields() {
      currentProvenance_ = pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo.getDefaultInstance();
      previousProvenances_ = java.util.Collections.emptyList();
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
      if (!hasCurrentProvenance()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!getCurrentProvenance().isInitialized()) {
        memoizedIsInitialized = 0;
        return false;
      }
      for (int i = 0; i < getPreviousProvenancesCount(); i++) {
        if (!getPreviousProvenances(i).isInitialized()) {
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
        output.writeMessage(1, currentProvenance_);
      }
      for (int i = 0; i < previousProvenances_.size(); i++) {
        output.writeMessage(2, previousProvenances_.get(i));
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
          .computeMessageSize(1, currentProvenance_);
      }
      for (int i = 0; i < previousProvenances_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(2, previousProvenances_.get(i));
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
    
    public static pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo parseDelimitedFrom(
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
    public static pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo prototype) {
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
       implements pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfoOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return pl.edu.icm.coansys.models.DocumentProtos.internal_static_ProvenanceInfo_descriptor;
      }
      
      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return pl.edu.icm.coansys.models.DocumentProtos.internal_static_ProvenanceInfo_fieldAccessorTable;
      }
      
      // Construct using pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }
      
      private Builder(BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getCurrentProvenanceFieldBuilder();
          getPreviousProvenancesFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }
      
      public Builder clear() {
        super.clear();
        if (currentProvenanceBuilder_ == null) {
          currentProvenance_ = pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo.getDefaultInstance();
        } else {
          currentProvenanceBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000001);
        if (previousProvenancesBuilder_ == null) {
          previousProvenances_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000002);
        } else {
          previousProvenancesBuilder_.clear();
        }
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.getDescriptor();
      }
      
      public pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo getDefaultInstanceForType() {
        return pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.getDefaultInstance();
      }
      
      public pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo build() {
        pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo buildPartial() {
        pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo result = new pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        if (currentProvenanceBuilder_ == null) {
          result.currentProvenance_ = currentProvenance_;
        } else {
          result.currentProvenance_ = currentProvenanceBuilder_.build();
        }
        if (previousProvenancesBuilder_ == null) {
          if (((bitField0_ & 0x00000002) == 0x00000002)) {
            previousProvenances_ = java.util.Collections.unmodifiableList(previousProvenances_);
            bitField0_ = (bitField0_ & ~0x00000002);
          }
          result.previousProvenances_ = previousProvenances_;
        } else {
          result.previousProvenances_ = previousProvenancesBuilder_.build();
        }
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo) {
          return mergeFrom((pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo other) {
        if (other == pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.getDefaultInstance()) return this;
        if (other.hasCurrentProvenance()) {
          mergeCurrentProvenance(other.getCurrentProvenance());
        }
        if (previousProvenancesBuilder_ == null) {
          if (!other.previousProvenances_.isEmpty()) {
            if (previousProvenances_.isEmpty()) {
              previousProvenances_ = other.previousProvenances_;
              bitField0_ = (bitField0_ & ~0x00000002);
            } else {
              ensurePreviousProvenancesIsMutable();
              previousProvenances_.addAll(other.previousProvenances_);
            }
            onChanged();
          }
        } else {
          if (!other.previousProvenances_.isEmpty()) {
            if (previousProvenancesBuilder_.isEmpty()) {
              previousProvenancesBuilder_.dispose();
              previousProvenancesBuilder_ = null;
              previousProvenances_ = other.previousProvenances_;
              bitField0_ = (bitField0_ & ~0x00000002);
              previousProvenancesBuilder_ = 
                com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                   getPreviousProvenancesFieldBuilder() : null;
            } else {
              previousProvenancesBuilder_.addAllMessages(other.previousProvenances_);
            }
          }
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
        if (!hasCurrentProvenance()) {
          
          return false;
        }
        if (!getCurrentProvenance().isInitialized()) {
          
          return false;
        }
        for (int i = 0; i < getPreviousProvenancesCount(); i++) {
          if (!getPreviousProvenances(i).isInitialized()) {
            
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
              pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo.Builder subBuilder = pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo.newBuilder();
              if (hasCurrentProvenance()) {
                subBuilder.mergeFrom(getCurrentProvenance());
              }
              input.readMessage(subBuilder, extensionRegistry);
              setCurrentProvenance(subBuilder.buildPartial());
              break;
            }
            case 18: {
              pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo.Builder subBuilder = pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo.newBuilder();
              input.readMessage(subBuilder, extensionRegistry);
              addPreviousProvenances(subBuilder.buildPartial());
              break;
            }
          }
        }
      }
      
      private int bitField0_;
      
      // required .ProvenanceInfo.SingleProvenanceInfo currentProvenance = 1;
      private pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo currentProvenance_ = pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo, pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo.Builder, pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfoOrBuilder> currentProvenanceBuilder_;
      public boolean hasCurrentProvenance() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      public pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo getCurrentProvenance() {
        if (currentProvenanceBuilder_ == null) {
          return currentProvenance_;
        } else {
          return currentProvenanceBuilder_.getMessage();
        }
      }
      public Builder setCurrentProvenance(pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo value) {
        if (currentProvenanceBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          currentProvenance_ = value;
          onChanged();
        } else {
          currentProvenanceBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      public Builder setCurrentProvenance(
          pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo.Builder builderForValue) {
        if (currentProvenanceBuilder_ == null) {
          currentProvenance_ = builderForValue.build();
          onChanged();
        } else {
          currentProvenanceBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      public Builder mergeCurrentProvenance(pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo value) {
        if (currentProvenanceBuilder_ == null) {
          if (((bitField0_ & 0x00000001) == 0x00000001) &&
              currentProvenance_ != pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo.getDefaultInstance()) {
            currentProvenance_ =
              pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo.newBuilder(currentProvenance_).mergeFrom(value).buildPartial();
          } else {
            currentProvenance_ = value;
          }
          onChanged();
        } else {
          currentProvenanceBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      public Builder clearCurrentProvenance() {
        if (currentProvenanceBuilder_ == null) {
          currentProvenance_ = pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo.getDefaultInstance();
          onChanged();
        } else {
          currentProvenanceBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000001);
        return this;
      }
      public pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo.Builder getCurrentProvenanceBuilder() {
        bitField0_ |= 0x00000001;
        onChanged();
        return getCurrentProvenanceFieldBuilder().getBuilder();
      }
      public pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfoOrBuilder getCurrentProvenanceOrBuilder() {
        if (currentProvenanceBuilder_ != null) {
          return currentProvenanceBuilder_.getMessageOrBuilder();
        } else {
          return currentProvenance_;
        }
      }
      private com.google.protobuf.SingleFieldBuilder<
          pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo, pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo.Builder, pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfoOrBuilder> 
          getCurrentProvenanceFieldBuilder() {
        if (currentProvenanceBuilder_ == null) {
          currentProvenanceBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo, pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo.Builder, pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfoOrBuilder>(
                  currentProvenance_,
                  getParentForChildren(),
                  isClean());
          currentProvenance_ = null;
        }
        return currentProvenanceBuilder_;
      }
      
      // repeated .ProvenanceInfo.SingleProvenanceInfo previousProvenances = 2;
      private java.util.List<pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo> previousProvenances_ =
        java.util.Collections.emptyList();
      private void ensurePreviousProvenancesIsMutable() {
        if (!((bitField0_ & 0x00000002) == 0x00000002)) {
          previousProvenances_ = new java.util.ArrayList<pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo>(previousProvenances_);
          bitField0_ |= 0x00000002;
         }
      }
      
      private com.google.protobuf.RepeatedFieldBuilder<
          pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo, pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo.Builder, pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfoOrBuilder> previousProvenancesBuilder_;
      
      public java.util.List<pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo> getPreviousProvenancesList() {
        if (previousProvenancesBuilder_ == null) {
          return java.util.Collections.unmodifiableList(previousProvenances_);
        } else {
          return previousProvenancesBuilder_.getMessageList();
        }
      }
      public int getPreviousProvenancesCount() {
        if (previousProvenancesBuilder_ == null) {
          return previousProvenances_.size();
        } else {
          return previousProvenancesBuilder_.getCount();
        }
      }
      public pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo getPreviousProvenances(int index) {
        if (previousProvenancesBuilder_ == null) {
          return previousProvenances_.get(index);
        } else {
          return previousProvenancesBuilder_.getMessage(index);
        }
      }
      public Builder setPreviousProvenances(
          int index, pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo value) {
        if (previousProvenancesBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensurePreviousProvenancesIsMutable();
          previousProvenances_.set(index, value);
          onChanged();
        } else {
          previousProvenancesBuilder_.setMessage(index, value);
        }
        return this;
      }
      public Builder setPreviousProvenances(
          int index, pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo.Builder builderForValue) {
        if (previousProvenancesBuilder_ == null) {
          ensurePreviousProvenancesIsMutable();
          previousProvenances_.set(index, builderForValue.build());
          onChanged();
        } else {
          previousProvenancesBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }
      public Builder addPreviousProvenances(pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo value) {
        if (previousProvenancesBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensurePreviousProvenancesIsMutable();
          previousProvenances_.add(value);
          onChanged();
        } else {
          previousProvenancesBuilder_.addMessage(value);
        }
        return this;
      }
      public Builder addPreviousProvenances(
          int index, pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo value) {
        if (previousProvenancesBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensurePreviousProvenancesIsMutable();
          previousProvenances_.add(index, value);
          onChanged();
        } else {
          previousProvenancesBuilder_.addMessage(index, value);
        }
        return this;
      }
      public Builder addPreviousProvenances(
          pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo.Builder builderForValue) {
        if (previousProvenancesBuilder_ == null) {
          ensurePreviousProvenancesIsMutable();
          previousProvenances_.add(builderForValue.build());
          onChanged();
        } else {
          previousProvenancesBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }
      public Builder addPreviousProvenances(
          int index, pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo.Builder builderForValue) {
        if (previousProvenancesBuilder_ == null) {
          ensurePreviousProvenancesIsMutable();
          previousProvenances_.add(index, builderForValue.build());
          onChanged();
        } else {
          previousProvenancesBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }
      public Builder addAllPreviousProvenances(
          java.lang.Iterable<? extends pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo> values) {
        if (previousProvenancesBuilder_ == null) {
          ensurePreviousProvenancesIsMutable();
          super.addAll(values, previousProvenances_);
          onChanged();
        } else {
          previousProvenancesBuilder_.addAllMessages(values);
        }
        return this;
      }
      public Builder clearPreviousProvenances() {
        if (previousProvenancesBuilder_ == null) {
          previousProvenances_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000002);
          onChanged();
        } else {
          previousProvenancesBuilder_.clear();
        }
        return this;
      }
      public Builder removePreviousProvenances(int index) {
        if (previousProvenancesBuilder_ == null) {
          ensurePreviousProvenancesIsMutable();
          previousProvenances_.remove(index);
          onChanged();
        } else {
          previousProvenancesBuilder_.remove(index);
        }
        return this;
      }
      public pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo.Builder getPreviousProvenancesBuilder(
          int index) {
        return getPreviousProvenancesFieldBuilder().getBuilder(index);
      }
      public pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfoOrBuilder getPreviousProvenancesOrBuilder(
          int index) {
        if (previousProvenancesBuilder_ == null) {
          return previousProvenances_.get(index);  } else {
          return previousProvenancesBuilder_.getMessageOrBuilder(index);
        }
      }
      public java.util.List<? extends pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfoOrBuilder> 
           getPreviousProvenancesOrBuilderList() {
        if (previousProvenancesBuilder_ != null) {
          return previousProvenancesBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(previousProvenances_);
        }
      }
      public pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo.Builder addPreviousProvenancesBuilder() {
        return getPreviousProvenancesFieldBuilder().addBuilder(
            pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo.getDefaultInstance());
      }
      public pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo.Builder addPreviousProvenancesBuilder(
          int index) {
        return getPreviousProvenancesFieldBuilder().addBuilder(
            index, pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo.getDefaultInstance());
      }
      public java.util.List<pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo.Builder> 
           getPreviousProvenancesBuilderList() {
        return getPreviousProvenancesFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilder<
          pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo, pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo.Builder, pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfoOrBuilder> 
          getPreviousProvenancesFieldBuilder() {
        if (previousProvenancesBuilder_ == null) {
          previousProvenancesBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
              pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo, pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo.Builder, pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfoOrBuilder>(
                  previousProvenances_,
                  ((bitField0_ & 0x00000002) == 0x00000002),
                  getParentForChildren(),
                  isClean());
          previousProvenances_ = null;
        }
        return previousProvenancesBuilder_;
      }
      
      // @@protoc_insertion_point(builder_scope:ProvenanceInfo)
    }
    
    static {
      defaultInstance = new ProvenanceInfo(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:ProvenanceInfo)
  }
  
  public interface KeyValueOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // required string key = 1;
    boolean hasKey();
    String getKey();
    
    // required string value = 2;
    boolean hasValue();
    String getValue();
    
    // optional string comment = 3;
    boolean hasComment();
    String getComment();
    
    // optional .ProvenanceInfo provenance = 4;
    boolean hasProvenance();
    pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo getProvenance();
    pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfoOrBuilder getProvenanceOrBuilder();
  }
  public static final class KeyValue extends
      com.google.protobuf.GeneratedMessage
      implements KeyValueOrBuilder {
    // Use KeyValue.newBuilder() to construct.
    private KeyValue(Builder builder) {
      super(builder);
    }
    private KeyValue(boolean noInit) {}
    
    private static final KeyValue defaultInstance;
    public static KeyValue getDefaultInstance() {
      return defaultInstance;
    }
    
    public KeyValue getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return pl.edu.icm.coansys.models.DocumentProtos.internal_static_KeyValue_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return pl.edu.icm.coansys.models.DocumentProtos.internal_static_KeyValue_fieldAccessorTable;
    }
    
    private int bitField0_;
    // required string key = 1;
    public static final int KEY_FIELD_NUMBER = 1;
    private java.lang.Object key_;
    public boolean hasKey() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    public String getKey() {
      java.lang.Object ref = key_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          key_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getKeyBytes() {
      java.lang.Object ref = key_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        key_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // required string value = 2;
    public static final int VALUE_FIELD_NUMBER = 2;
    private java.lang.Object value_;
    public boolean hasValue() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    public String getValue() {
      java.lang.Object ref = value_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          value_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getValueBytes() {
      java.lang.Object ref = value_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        value_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // optional string comment = 3;
    public static final int COMMENT_FIELD_NUMBER = 3;
    private java.lang.Object comment_;
    public boolean hasComment() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    public String getComment() {
      java.lang.Object ref = comment_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          comment_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getCommentBytes() {
      java.lang.Object ref = comment_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        comment_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // optional .ProvenanceInfo provenance = 4;
    public static final int PROVENANCE_FIELD_NUMBER = 4;
    private pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo provenance_;
    public boolean hasProvenance() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    public pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo getProvenance() {
      return provenance_;
    }
    public pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfoOrBuilder getProvenanceOrBuilder() {
      return provenance_;
    }
    
    private void initFields() {
      key_ = "";
      value_ = "";
      comment_ = "";
      provenance_ = pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.getDefaultInstance();
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
      if (!hasKey()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasValue()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (hasProvenance()) {
        if (!getProvenance().isInitialized()) {
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
        output.writeBytes(1, getKeyBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeBytes(2, getValueBytes());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeBytes(3, getCommentBytes());
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeMessage(4, provenance_);
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
          .computeBytesSize(1, getKeyBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(2, getValueBytes());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(3, getCommentBytes());
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(4, provenance_);
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
    
    public static pl.edu.icm.coansys.models.DocumentProtos.KeyValue parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.KeyValue parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.KeyValue parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.KeyValue parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.KeyValue parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.KeyValue parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.KeyValue parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.KeyValue parseDelimitedFrom(
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
    public static pl.edu.icm.coansys.models.DocumentProtos.KeyValue parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.KeyValue parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(pl.edu.icm.coansys.models.DocumentProtos.KeyValue prototype) {
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
       implements pl.edu.icm.coansys.models.DocumentProtos.KeyValueOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return pl.edu.icm.coansys.models.DocumentProtos.internal_static_KeyValue_descriptor;
      }
      
      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return pl.edu.icm.coansys.models.DocumentProtos.internal_static_KeyValue_fieldAccessorTable;
      }
      
      // Construct using pl.edu.icm.coansys.models.DocumentProtos.KeyValue.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }
      
      private Builder(BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getProvenanceFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }
      
      public Builder clear() {
        super.clear();
        key_ = "";
        bitField0_ = (bitField0_ & ~0x00000001);
        value_ = "";
        bitField0_ = (bitField0_ & ~0x00000002);
        comment_ = "";
        bitField0_ = (bitField0_ & ~0x00000004);
        if (provenanceBuilder_ == null) {
          provenance_ = pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.getDefaultInstance();
        } else {
          provenanceBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000008);
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return pl.edu.icm.coansys.models.DocumentProtos.KeyValue.getDescriptor();
      }
      
      public pl.edu.icm.coansys.models.DocumentProtos.KeyValue getDefaultInstanceForType() {
        return pl.edu.icm.coansys.models.DocumentProtos.KeyValue.getDefaultInstance();
      }
      
      public pl.edu.icm.coansys.models.DocumentProtos.KeyValue build() {
        pl.edu.icm.coansys.models.DocumentProtos.KeyValue result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private pl.edu.icm.coansys.models.DocumentProtos.KeyValue buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        pl.edu.icm.coansys.models.DocumentProtos.KeyValue result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public pl.edu.icm.coansys.models.DocumentProtos.KeyValue buildPartial() {
        pl.edu.icm.coansys.models.DocumentProtos.KeyValue result = new pl.edu.icm.coansys.models.DocumentProtos.KeyValue(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.key_ = key_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.value_ = value_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.comment_ = comment_;
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000008;
        }
        if (provenanceBuilder_ == null) {
          result.provenance_ = provenance_;
        } else {
          result.provenance_ = provenanceBuilder_.build();
        }
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof pl.edu.icm.coansys.models.DocumentProtos.KeyValue) {
          return mergeFrom((pl.edu.icm.coansys.models.DocumentProtos.KeyValue)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(pl.edu.icm.coansys.models.DocumentProtos.KeyValue other) {
        if (other == pl.edu.icm.coansys.models.DocumentProtos.KeyValue.getDefaultInstance()) return this;
        if (other.hasKey()) {
          setKey(other.getKey());
        }
        if (other.hasValue()) {
          setValue(other.getValue());
        }
        if (other.hasComment()) {
          setComment(other.getComment());
        }
        if (other.hasProvenance()) {
          mergeProvenance(other.getProvenance());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
        if (!hasKey()) {
          
          return false;
        }
        if (!hasValue()) {
          
          return false;
        }
        if (hasProvenance()) {
          if (!getProvenance().isInitialized()) {
            
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
              key_ = input.readBytes();
              break;
            }
            case 18: {
              bitField0_ |= 0x00000002;
              value_ = input.readBytes();
              break;
            }
            case 26: {
              bitField0_ |= 0x00000004;
              comment_ = input.readBytes();
              break;
            }
            case 34: {
              pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.Builder subBuilder = pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.newBuilder();
              if (hasProvenance()) {
                subBuilder.mergeFrom(getProvenance());
              }
              input.readMessage(subBuilder, extensionRegistry);
              setProvenance(subBuilder.buildPartial());
              break;
            }
          }
        }
      }
      
      private int bitField0_;
      
      // required string key = 1;
      private java.lang.Object key_ = "";
      public boolean hasKey() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      public String getKey() {
        java.lang.Object ref = key_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          key_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setKey(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        key_ = value;
        onChanged();
        return this;
      }
      public Builder clearKey() {
        bitField0_ = (bitField0_ & ~0x00000001);
        key_ = getDefaultInstance().getKey();
        onChanged();
        return this;
      }
      void setKey(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000001;
        key_ = value;
        onChanged();
      }
      
      // required string value = 2;
      private java.lang.Object value_ = "";
      public boolean hasValue() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      public String getValue() {
        java.lang.Object ref = value_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          value_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setValue(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        value_ = value;
        onChanged();
        return this;
      }
      public Builder clearValue() {
        bitField0_ = (bitField0_ & ~0x00000002);
        value_ = getDefaultInstance().getValue();
        onChanged();
        return this;
      }
      void setValue(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000002;
        value_ = value;
        onChanged();
      }
      
      // optional string comment = 3;
      private java.lang.Object comment_ = "";
      public boolean hasComment() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      public String getComment() {
        java.lang.Object ref = comment_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          comment_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setComment(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000004;
        comment_ = value;
        onChanged();
        return this;
      }
      public Builder clearComment() {
        bitField0_ = (bitField0_ & ~0x00000004);
        comment_ = getDefaultInstance().getComment();
        onChanged();
        return this;
      }
      void setComment(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000004;
        comment_ = value;
        onChanged();
      }
      
      // optional .ProvenanceInfo provenance = 4;
      private pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo provenance_ = pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo, pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.Builder, pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfoOrBuilder> provenanceBuilder_;
      public boolean hasProvenance() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      public pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo getProvenance() {
        if (provenanceBuilder_ == null) {
          return provenance_;
        } else {
          return provenanceBuilder_.getMessage();
        }
      }
      public Builder setProvenance(pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo value) {
        if (provenanceBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          provenance_ = value;
          onChanged();
        } else {
          provenanceBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000008;
        return this;
      }
      public Builder setProvenance(
          pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.Builder builderForValue) {
        if (provenanceBuilder_ == null) {
          provenance_ = builderForValue.build();
          onChanged();
        } else {
          provenanceBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000008;
        return this;
      }
      public Builder mergeProvenance(pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo value) {
        if (provenanceBuilder_ == null) {
          if (((bitField0_ & 0x00000008) == 0x00000008) &&
              provenance_ != pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.getDefaultInstance()) {
            provenance_ =
              pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.newBuilder(provenance_).mergeFrom(value).buildPartial();
          } else {
            provenance_ = value;
          }
          onChanged();
        } else {
          provenanceBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000008;
        return this;
      }
      public Builder clearProvenance() {
        if (provenanceBuilder_ == null) {
          provenance_ = pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.getDefaultInstance();
          onChanged();
        } else {
          provenanceBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000008);
        return this;
      }
      public pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.Builder getProvenanceBuilder() {
        bitField0_ |= 0x00000008;
        onChanged();
        return getProvenanceFieldBuilder().getBuilder();
      }
      public pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfoOrBuilder getProvenanceOrBuilder() {
        if (provenanceBuilder_ != null) {
          return provenanceBuilder_.getMessageOrBuilder();
        } else {
          return provenance_;
        }
      }
      private com.google.protobuf.SingleFieldBuilder<
          pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo, pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.Builder, pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfoOrBuilder> 
          getProvenanceFieldBuilder() {
        if (provenanceBuilder_ == null) {
          provenanceBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo, pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.Builder, pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfoOrBuilder>(
                  provenance_,
                  getParentForChildren(),
                  isClean());
          provenance_ = null;
        }
        return provenanceBuilder_;
      }
      
      // @@protoc_insertion_point(builder_scope:KeyValue)
    }
    
    static {
      defaultInstance = new KeyValue(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:KeyValue)
  }
  
  public interface TextWithLanguageOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // required string text = 1;
    boolean hasText();
    String getText();
    
    // optional string language = 2;
    boolean hasLanguage();
    String getLanguage();
    
    // optional string comment = 3;
    boolean hasComment();
    String getComment();
  }
  public static final class TextWithLanguage extends
      com.google.protobuf.GeneratedMessage
      implements TextWithLanguageOrBuilder {
    // Use TextWithLanguage.newBuilder() to construct.
    private TextWithLanguage(Builder builder) {
      super(builder);
    }
    private TextWithLanguage(boolean noInit) {}
    
    private static final TextWithLanguage defaultInstance;
    public static TextWithLanguage getDefaultInstance() {
      return defaultInstance;
    }
    
    public TextWithLanguage getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return pl.edu.icm.coansys.models.DocumentProtos.internal_static_TextWithLanguage_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return pl.edu.icm.coansys.models.DocumentProtos.internal_static_TextWithLanguage_fieldAccessorTable;
    }
    
    private int bitField0_;
    // required string text = 1;
    public static final int TEXT_FIELD_NUMBER = 1;
    private java.lang.Object text_;
    public boolean hasText() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    public String getText() {
      java.lang.Object ref = text_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          text_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getTextBytes() {
      java.lang.Object ref = text_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        text_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // optional string language = 2;
    public static final int LANGUAGE_FIELD_NUMBER = 2;
    private java.lang.Object language_;
    public boolean hasLanguage() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    public String getLanguage() {
      java.lang.Object ref = language_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          language_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getLanguageBytes() {
      java.lang.Object ref = language_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        language_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // optional string comment = 3;
    public static final int COMMENT_FIELD_NUMBER = 3;
    private java.lang.Object comment_;
    public boolean hasComment() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    public String getComment() {
      java.lang.Object ref = comment_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          comment_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getCommentBytes() {
      java.lang.Object ref = comment_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        comment_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    private void initFields() {
      text_ = "";
      language_ = "";
      comment_ = "";
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
      if (!hasText()) {
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
        output.writeBytes(1, getTextBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeBytes(2, getLanguageBytes());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeBytes(3, getCommentBytes());
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
          .computeBytesSize(1, getTextBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(2, getLanguageBytes());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(3, getCommentBytes());
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
    
    public static pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage parseDelimitedFrom(
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
    public static pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage prototype) {
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
       implements pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguageOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return pl.edu.icm.coansys.models.DocumentProtos.internal_static_TextWithLanguage_descriptor;
      }
      
      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return pl.edu.icm.coansys.models.DocumentProtos.internal_static_TextWithLanguage_fieldAccessorTable;
      }
      
      // Construct using pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage.newBuilder()
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
        text_ = "";
        bitField0_ = (bitField0_ & ~0x00000001);
        language_ = "";
        bitField0_ = (bitField0_ & ~0x00000002);
        comment_ = "";
        bitField0_ = (bitField0_ & ~0x00000004);
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage.getDescriptor();
      }
      
      public pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage getDefaultInstanceForType() {
        return pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage.getDefaultInstance();
      }
      
      public pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage build() {
        pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage buildPartial() {
        pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage result = new pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.text_ = text_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.language_ = language_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.comment_ = comment_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage) {
          return mergeFrom((pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage other) {
        if (other == pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage.getDefaultInstance()) return this;
        if (other.hasText()) {
          setText(other.getText());
        }
        if (other.hasLanguage()) {
          setLanguage(other.getLanguage());
        }
        if (other.hasComment()) {
          setComment(other.getComment());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
        if (!hasText()) {
          
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
              text_ = input.readBytes();
              break;
            }
            case 18: {
              bitField0_ |= 0x00000002;
              language_ = input.readBytes();
              break;
            }
            case 26: {
              bitField0_ |= 0x00000004;
              comment_ = input.readBytes();
              break;
            }
          }
        }
      }
      
      private int bitField0_;
      
      // required string text = 1;
      private java.lang.Object text_ = "";
      public boolean hasText() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      public String getText() {
        java.lang.Object ref = text_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          text_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setText(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        text_ = value;
        onChanged();
        return this;
      }
      public Builder clearText() {
        bitField0_ = (bitField0_ & ~0x00000001);
        text_ = getDefaultInstance().getText();
        onChanged();
        return this;
      }
      void setText(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000001;
        text_ = value;
        onChanged();
      }
      
      // optional string language = 2;
      private java.lang.Object language_ = "";
      public boolean hasLanguage() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      public String getLanguage() {
        java.lang.Object ref = language_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          language_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setLanguage(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        language_ = value;
        onChanged();
        return this;
      }
      public Builder clearLanguage() {
        bitField0_ = (bitField0_ & ~0x00000002);
        language_ = getDefaultInstance().getLanguage();
        onChanged();
        return this;
      }
      void setLanguage(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000002;
        language_ = value;
        onChanged();
      }
      
      // optional string comment = 3;
      private java.lang.Object comment_ = "";
      public boolean hasComment() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      public String getComment() {
        java.lang.Object ref = comment_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          comment_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setComment(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000004;
        comment_ = value;
        onChanged();
        return this;
      }
      public Builder clearComment() {
        bitField0_ = (bitField0_ & ~0x00000004);
        comment_ = getDefaultInstance().getComment();
        onChanged();
        return this;
      }
      void setComment(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000004;
        comment_ = value;
        onChanged();
      }
      
      // @@protoc_insertion_point(builder_scope:TextWithLanguage)
    }
    
    static {
      defaultInstance = new TextWithLanguage(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:TextWithLanguage)
  }
  
  public interface ClassifCodeOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // required string source = 1;
    boolean hasSource();
    String getSource();
    
    // repeated string value = 2;
    java.util.List<String> getValueList();
    int getValueCount();
    String getValue(int index);
    
    // optional .ProvenanceInfo provenance = 3;
    boolean hasProvenance();
    pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo getProvenance();
    pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfoOrBuilder getProvenanceOrBuilder();
  }
  public static final class ClassifCode extends
      com.google.protobuf.GeneratedMessage
      implements ClassifCodeOrBuilder {
    // Use ClassifCode.newBuilder() to construct.
    private ClassifCode(Builder builder) {
      super(builder);
    }
    private ClassifCode(boolean noInit) {}
    
    private static final ClassifCode defaultInstance;
    public static ClassifCode getDefaultInstance() {
      return defaultInstance;
    }
    
    public ClassifCode getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return pl.edu.icm.coansys.models.DocumentProtos.internal_static_ClassifCode_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return pl.edu.icm.coansys.models.DocumentProtos.internal_static_ClassifCode_fieldAccessorTable;
    }
    
    private int bitField0_;
    // required string source = 1;
    public static final int SOURCE_FIELD_NUMBER = 1;
    private java.lang.Object source_;
    public boolean hasSource() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    public String getSource() {
      java.lang.Object ref = source_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          source_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getSourceBytes() {
      java.lang.Object ref = source_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        source_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // repeated string value = 2;
    public static final int VALUE_FIELD_NUMBER = 2;
    private com.google.protobuf.LazyStringList value_;
    public java.util.List<String>
        getValueList() {
      return value_;
    }
    public int getValueCount() {
      return value_.size();
    }
    public String getValue(int index) {
      return value_.get(index);
    }
    
    // optional .ProvenanceInfo provenance = 3;
    public static final int PROVENANCE_FIELD_NUMBER = 3;
    private pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo provenance_;
    public boolean hasProvenance() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    public pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo getProvenance() {
      return provenance_;
    }
    public pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfoOrBuilder getProvenanceOrBuilder() {
      return provenance_;
    }
    
    private void initFields() {
      source_ = "";
      value_ = com.google.protobuf.LazyStringArrayList.EMPTY;
      provenance_ = pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.getDefaultInstance();
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
      if (!hasSource()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (hasProvenance()) {
        if (!getProvenance().isInitialized()) {
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
        output.writeBytes(1, getSourceBytes());
      }
      for (int i = 0; i < value_.size(); i++) {
        output.writeBytes(2, value_.getByteString(i));
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeMessage(3, provenance_);
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
          .computeBytesSize(1, getSourceBytes());
      }
      {
        int dataSize = 0;
        for (int i = 0; i < value_.size(); i++) {
          dataSize += com.google.protobuf.CodedOutputStream
            .computeBytesSizeNoTag(value_.getByteString(i));
        }
        size += dataSize;
        size += 1 * getValueList().size();
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(3, provenance_);
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
    
    public static pl.edu.icm.coansys.models.DocumentProtos.ClassifCode parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.ClassifCode parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.ClassifCode parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.ClassifCode parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.ClassifCode parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.ClassifCode parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.ClassifCode parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.ClassifCode parseDelimitedFrom(
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
    public static pl.edu.icm.coansys.models.DocumentProtos.ClassifCode parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.ClassifCode parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(pl.edu.icm.coansys.models.DocumentProtos.ClassifCode prototype) {
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
       implements pl.edu.icm.coansys.models.DocumentProtos.ClassifCodeOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return pl.edu.icm.coansys.models.DocumentProtos.internal_static_ClassifCode_descriptor;
      }
      
      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return pl.edu.icm.coansys.models.DocumentProtos.internal_static_ClassifCode_fieldAccessorTable;
      }
      
      // Construct using pl.edu.icm.coansys.models.DocumentProtos.ClassifCode.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }
      
      private Builder(BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getProvenanceFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }
      
      public Builder clear() {
        super.clear();
        source_ = "";
        bitField0_ = (bitField0_ & ~0x00000001);
        value_ = com.google.protobuf.LazyStringArrayList.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000002);
        if (provenanceBuilder_ == null) {
          provenance_ = pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.getDefaultInstance();
        } else {
          provenanceBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000004);
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return pl.edu.icm.coansys.models.DocumentProtos.ClassifCode.getDescriptor();
      }
      
      public pl.edu.icm.coansys.models.DocumentProtos.ClassifCode getDefaultInstanceForType() {
        return pl.edu.icm.coansys.models.DocumentProtos.ClassifCode.getDefaultInstance();
      }
      
      public pl.edu.icm.coansys.models.DocumentProtos.ClassifCode build() {
        pl.edu.icm.coansys.models.DocumentProtos.ClassifCode result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private pl.edu.icm.coansys.models.DocumentProtos.ClassifCode buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        pl.edu.icm.coansys.models.DocumentProtos.ClassifCode result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public pl.edu.icm.coansys.models.DocumentProtos.ClassifCode buildPartial() {
        pl.edu.icm.coansys.models.DocumentProtos.ClassifCode result = new pl.edu.icm.coansys.models.DocumentProtos.ClassifCode(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.source_ = source_;
        if (((bitField0_ & 0x00000002) == 0x00000002)) {
          value_ = new com.google.protobuf.UnmodifiableLazyStringList(
              value_);
          bitField0_ = (bitField0_ & ~0x00000002);
        }
        result.value_ = value_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000002;
        }
        if (provenanceBuilder_ == null) {
          result.provenance_ = provenance_;
        } else {
          result.provenance_ = provenanceBuilder_.build();
        }
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof pl.edu.icm.coansys.models.DocumentProtos.ClassifCode) {
          return mergeFrom((pl.edu.icm.coansys.models.DocumentProtos.ClassifCode)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(pl.edu.icm.coansys.models.DocumentProtos.ClassifCode other) {
        if (other == pl.edu.icm.coansys.models.DocumentProtos.ClassifCode.getDefaultInstance()) return this;
        if (other.hasSource()) {
          setSource(other.getSource());
        }
        if (!other.value_.isEmpty()) {
          if (value_.isEmpty()) {
            value_ = other.value_;
            bitField0_ = (bitField0_ & ~0x00000002);
          } else {
            ensureValueIsMutable();
            value_.addAll(other.value_);
          }
          onChanged();
        }
        if (other.hasProvenance()) {
          mergeProvenance(other.getProvenance());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
        if (!hasSource()) {
          
          return false;
        }
        if (hasProvenance()) {
          if (!getProvenance().isInitialized()) {
            
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
              source_ = input.readBytes();
              break;
            }
            case 18: {
              ensureValueIsMutable();
              value_.add(input.readBytes());
              break;
            }
            case 26: {
              pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.Builder subBuilder = pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.newBuilder();
              if (hasProvenance()) {
                subBuilder.mergeFrom(getProvenance());
              }
              input.readMessage(subBuilder, extensionRegistry);
              setProvenance(subBuilder.buildPartial());
              break;
            }
          }
        }
      }
      
      private int bitField0_;
      
      // required string source = 1;
      private java.lang.Object source_ = "";
      public boolean hasSource() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      public String getSource() {
        java.lang.Object ref = source_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          source_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setSource(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        source_ = value;
        onChanged();
        return this;
      }
      public Builder clearSource() {
        bitField0_ = (bitField0_ & ~0x00000001);
        source_ = getDefaultInstance().getSource();
        onChanged();
        return this;
      }
      void setSource(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000001;
        source_ = value;
        onChanged();
      }
      
      // repeated string value = 2;
      private com.google.protobuf.LazyStringList value_ = com.google.protobuf.LazyStringArrayList.EMPTY;
      private void ensureValueIsMutable() {
        if (!((bitField0_ & 0x00000002) == 0x00000002)) {
          value_ = new com.google.protobuf.LazyStringArrayList(value_);
          bitField0_ |= 0x00000002;
         }
      }
      public java.util.List<String>
          getValueList() {
        return java.util.Collections.unmodifiableList(value_);
      }
      public int getValueCount() {
        return value_.size();
      }
      public String getValue(int index) {
        return value_.get(index);
      }
      public Builder setValue(
          int index, String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  ensureValueIsMutable();
        value_.set(index, value);
        onChanged();
        return this;
      }
      public Builder addValue(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  ensureValueIsMutable();
        value_.add(value);
        onChanged();
        return this;
      }
      public Builder addAllValue(
          java.lang.Iterable<String> values) {
        ensureValueIsMutable();
        super.addAll(values, value_);
        onChanged();
        return this;
      }
      public Builder clearValue() {
        value_ = com.google.protobuf.LazyStringArrayList.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000002);
        onChanged();
        return this;
      }
      void addValue(com.google.protobuf.ByteString value) {
        ensureValueIsMutable();
        value_.add(value);
        onChanged();
      }
      
      // optional .ProvenanceInfo provenance = 3;
      private pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo provenance_ = pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo, pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.Builder, pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfoOrBuilder> provenanceBuilder_;
      public boolean hasProvenance() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      public pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo getProvenance() {
        if (provenanceBuilder_ == null) {
          return provenance_;
        } else {
          return provenanceBuilder_.getMessage();
        }
      }
      public Builder setProvenance(pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo value) {
        if (provenanceBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          provenance_ = value;
          onChanged();
        } else {
          provenanceBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000004;
        return this;
      }
      public Builder setProvenance(
          pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.Builder builderForValue) {
        if (provenanceBuilder_ == null) {
          provenance_ = builderForValue.build();
          onChanged();
        } else {
          provenanceBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000004;
        return this;
      }
      public Builder mergeProvenance(pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo value) {
        if (provenanceBuilder_ == null) {
          if (((bitField0_ & 0x00000004) == 0x00000004) &&
              provenance_ != pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.getDefaultInstance()) {
            provenance_ =
              pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.newBuilder(provenance_).mergeFrom(value).buildPartial();
          } else {
            provenance_ = value;
          }
          onChanged();
        } else {
          provenanceBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000004;
        return this;
      }
      public Builder clearProvenance() {
        if (provenanceBuilder_ == null) {
          provenance_ = pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.getDefaultInstance();
          onChanged();
        } else {
          provenanceBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000004);
        return this;
      }
      public pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.Builder getProvenanceBuilder() {
        bitField0_ |= 0x00000004;
        onChanged();
        return getProvenanceFieldBuilder().getBuilder();
      }
      public pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfoOrBuilder getProvenanceOrBuilder() {
        if (provenanceBuilder_ != null) {
          return provenanceBuilder_.getMessageOrBuilder();
        } else {
          return provenance_;
        }
      }
      private com.google.protobuf.SingleFieldBuilder<
          pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo, pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.Builder, pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfoOrBuilder> 
          getProvenanceFieldBuilder() {
        if (provenanceBuilder_ == null) {
          provenanceBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo, pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.Builder, pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfoOrBuilder>(
                  provenance_,
                  getParentForChildren(),
                  isClean());
          provenance_ = null;
        }
        return provenanceBuilder_;
      }
      
      // @@protoc_insertion_point(builder_scope:ClassifCode)
    }
    
    static {
      defaultInstance = new ClassifCode(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:ClassifCode)
  }
  
  public interface AffiliationOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // required string key = 1;
    boolean hasKey();
    String getKey();
    
    // required string affiliationId = 2;
    boolean hasAffiliationId();
    String getAffiliationId();
    
    // required string text = 3;
    boolean hasText();
    String getText();
    
    // optional .ProvenanceInfo provenance = 4;
    boolean hasProvenance();
    pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo getProvenance();
    pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfoOrBuilder getProvenanceOrBuilder();
  }
  public static final class Affiliation extends
      com.google.protobuf.GeneratedMessage
      implements AffiliationOrBuilder {
    // Use Affiliation.newBuilder() to construct.
    private Affiliation(Builder builder) {
      super(builder);
    }
    private Affiliation(boolean noInit) {}
    
    private static final Affiliation defaultInstance;
    public static Affiliation getDefaultInstance() {
      return defaultInstance;
    }
    
    public Affiliation getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return pl.edu.icm.coansys.models.DocumentProtos.internal_static_Affiliation_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return pl.edu.icm.coansys.models.DocumentProtos.internal_static_Affiliation_fieldAccessorTable;
    }
    
    private int bitField0_;
    // required string key = 1;
    public static final int KEY_FIELD_NUMBER = 1;
    private java.lang.Object key_;
    public boolean hasKey() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    public String getKey() {
      java.lang.Object ref = key_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          key_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getKeyBytes() {
      java.lang.Object ref = key_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        key_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // required string affiliationId = 2;
    public static final int AFFILIATIONID_FIELD_NUMBER = 2;
    private java.lang.Object affiliationId_;
    public boolean hasAffiliationId() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    public String getAffiliationId() {
      java.lang.Object ref = affiliationId_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          affiliationId_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getAffiliationIdBytes() {
      java.lang.Object ref = affiliationId_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        affiliationId_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // required string text = 3;
    public static final int TEXT_FIELD_NUMBER = 3;
    private java.lang.Object text_;
    public boolean hasText() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    public String getText() {
      java.lang.Object ref = text_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          text_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getTextBytes() {
      java.lang.Object ref = text_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        text_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // optional .ProvenanceInfo provenance = 4;
    public static final int PROVENANCE_FIELD_NUMBER = 4;
    private pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo provenance_;
    public boolean hasProvenance() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    public pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo getProvenance() {
      return provenance_;
    }
    public pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfoOrBuilder getProvenanceOrBuilder() {
      return provenance_;
    }
    
    private void initFields() {
      key_ = "";
      affiliationId_ = "";
      text_ = "";
      provenance_ = pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.getDefaultInstance();
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
      if (!hasKey()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasAffiliationId()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasText()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (hasProvenance()) {
        if (!getProvenance().isInitialized()) {
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
        output.writeBytes(1, getKeyBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeBytes(2, getAffiliationIdBytes());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeBytes(3, getTextBytes());
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeMessage(4, provenance_);
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
          .computeBytesSize(1, getKeyBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(2, getAffiliationIdBytes());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(3, getTextBytes());
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(4, provenance_);
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
    
    public static pl.edu.icm.coansys.models.DocumentProtos.Affiliation parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.Affiliation parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.Affiliation parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.Affiliation parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.Affiliation parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.Affiliation parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.Affiliation parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.Affiliation parseDelimitedFrom(
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
    public static pl.edu.icm.coansys.models.DocumentProtos.Affiliation parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.Affiliation parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(pl.edu.icm.coansys.models.DocumentProtos.Affiliation prototype) {
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
       implements pl.edu.icm.coansys.models.DocumentProtos.AffiliationOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return pl.edu.icm.coansys.models.DocumentProtos.internal_static_Affiliation_descriptor;
      }
      
      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return pl.edu.icm.coansys.models.DocumentProtos.internal_static_Affiliation_fieldAccessorTable;
      }
      
      // Construct using pl.edu.icm.coansys.models.DocumentProtos.Affiliation.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }
      
      private Builder(BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getProvenanceFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }
      
      public Builder clear() {
        super.clear();
        key_ = "";
        bitField0_ = (bitField0_ & ~0x00000001);
        affiliationId_ = "";
        bitField0_ = (bitField0_ & ~0x00000002);
        text_ = "";
        bitField0_ = (bitField0_ & ~0x00000004);
        if (provenanceBuilder_ == null) {
          provenance_ = pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.getDefaultInstance();
        } else {
          provenanceBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000008);
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return pl.edu.icm.coansys.models.DocumentProtos.Affiliation.getDescriptor();
      }
      
      public pl.edu.icm.coansys.models.DocumentProtos.Affiliation getDefaultInstanceForType() {
        return pl.edu.icm.coansys.models.DocumentProtos.Affiliation.getDefaultInstance();
      }
      
      public pl.edu.icm.coansys.models.DocumentProtos.Affiliation build() {
        pl.edu.icm.coansys.models.DocumentProtos.Affiliation result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private pl.edu.icm.coansys.models.DocumentProtos.Affiliation buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        pl.edu.icm.coansys.models.DocumentProtos.Affiliation result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public pl.edu.icm.coansys.models.DocumentProtos.Affiliation buildPartial() {
        pl.edu.icm.coansys.models.DocumentProtos.Affiliation result = new pl.edu.icm.coansys.models.DocumentProtos.Affiliation(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.key_ = key_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.affiliationId_ = affiliationId_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.text_ = text_;
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000008;
        }
        if (provenanceBuilder_ == null) {
          result.provenance_ = provenance_;
        } else {
          result.provenance_ = provenanceBuilder_.build();
        }
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof pl.edu.icm.coansys.models.DocumentProtos.Affiliation) {
          return mergeFrom((pl.edu.icm.coansys.models.DocumentProtos.Affiliation)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(pl.edu.icm.coansys.models.DocumentProtos.Affiliation other) {
        if (other == pl.edu.icm.coansys.models.DocumentProtos.Affiliation.getDefaultInstance()) return this;
        if (other.hasKey()) {
          setKey(other.getKey());
        }
        if (other.hasAffiliationId()) {
          setAffiliationId(other.getAffiliationId());
        }
        if (other.hasText()) {
          setText(other.getText());
        }
        if (other.hasProvenance()) {
          mergeProvenance(other.getProvenance());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
        if (!hasKey()) {
          
          return false;
        }
        if (!hasAffiliationId()) {
          
          return false;
        }
        if (!hasText()) {
          
          return false;
        }
        if (hasProvenance()) {
          if (!getProvenance().isInitialized()) {
            
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
              key_ = input.readBytes();
              break;
            }
            case 18: {
              bitField0_ |= 0x00000002;
              affiliationId_ = input.readBytes();
              break;
            }
            case 26: {
              bitField0_ |= 0x00000004;
              text_ = input.readBytes();
              break;
            }
            case 34: {
              pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.Builder subBuilder = pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.newBuilder();
              if (hasProvenance()) {
                subBuilder.mergeFrom(getProvenance());
              }
              input.readMessage(subBuilder, extensionRegistry);
              setProvenance(subBuilder.buildPartial());
              break;
            }
          }
        }
      }
      
      private int bitField0_;
      
      // required string key = 1;
      private java.lang.Object key_ = "";
      public boolean hasKey() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      public String getKey() {
        java.lang.Object ref = key_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          key_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setKey(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        key_ = value;
        onChanged();
        return this;
      }
      public Builder clearKey() {
        bitField0_ = (bitField0_ & ~0x00000001);
        key_ = getDefaultInstance().getKey();
        onChanged();
        return this;
      }
      void setKey(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000001;
        key_ = value;
        onChanged();
      }
      
      // required string affiliationId = 2;
      private java.lang.Object affiliationId_ = "";
      public boolean hasAffiliationId() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      public String getAffiliationId() {
        java.lang.Object ref = affiliationId_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          affiliationId_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setAffiliationId(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        affiliationId_ = value;
        onChanged();
        return this;
      }
      public Builder clearAffiliationId() {
        bitField0_ = (bitField0_ & ~0x00000002);
        affiliationId_ = getDefaultInstance().getAffiliationId();
        onChanged();
        return this;
      }
      void setAffiliationId(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000002;
        affiliationId_ = value;
        onChanged();
      }
      
      // required string text = 3;
      private java.lang.Object text_ = "";
      public boolean hasText() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      public String getText() {
        java.lang.Object ref = text_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          text_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setText(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000004;
        text_ = value;
        onChanged();
        return this;
      }
      public Builder clearText() {
        bitField0_ = (bitField0_ & ~0x00000004);
        text_ = getDefaultInstance().getText();
        onChanged();
        return this;
      }
      void setText(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000004;
        text_ = value;
        onChanged();
      }
      
      // optional .ProvenanceInfo provenance = 4;
      private pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo provenance_ = pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo, pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.Builder, pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfoOrBuilder> provenanceBuilder_;
      public boolean hasProvenance() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      public pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo getProvenance() {
        if (provenanceBuilder_ == null) {
          return provenance_;
        } else {
          return provenanceBuilder_.getMessage();
        }
      }
      public Builder setProvenance(pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo value) {
        if (provenanceBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          provenance_ = value;
          onChanged();
        } else {
          provenanceBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000008;
        return this;
      }
      public Builder setProvenance(
          pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.Builder builderForValue) {
        if (provenanceBuilder_ == null) {
          provenance_ = builderForValue.build();
          onChanged();
        } else {
          provenanceBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000008;
        return this;
      }
      public Builder mergeProvenance(pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo value) {
        if (provenanceBuilder_ == null) {
          if (((bitField0_ & 0x00000008) == 0x00000008) &&
              provenance_ != pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.getDefaultInstance()) {
            provenance_ =
              pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.newBuilder(provenance_).mergeFrom(value).buildPartial();
          } else {
            provenance_ = value;
          }
          onChanged();
        } else {
          provenanceBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000008;
        return this;
      }
      public Builder clearProvenance() {
        if (provenanceBuilder_ == null) {
          provenance_ = pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.getDefaultInstance();
          onChanged();
        } else {
          provenanceBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000008);
        return this;
      }
      public pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.Builder getProvenanceBuilder() {
        bitField0_ |= 0x00000008;
        onChanged();
        return getProvenanceFieldBuilder().getBuilder();
      }
      public pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfoOrBuilder getProvenanceOrBuilder() {
        if (provenanceBuilder_ != null) {
          return provenanceBuilder_.getMessageOrBuilder();
        } else {
          return provenance_;
        }
      }
      private com.google.protobuf.SingleFieldBuilder<
          pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo, pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.Builder, pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfoOrBuilder> 
          getProvenanceFieldBuilder() {
        if (provenanceBuilder_ == null) {
          provenanceBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo, pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.Builder, pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfoOrBuilder>(
                  provenance_,
                  getParentForChildren(),
                  isClean());
          provenance_ = null;
        }
        return provenanceBuilder_;
      }
      
      // @@protoc_insertion_point(builder_scope:Affiliation)
    }
    
    static {
      defaultInstance = new Affiliation(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:Affiliation)
  }
  
  public interface AuthorOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // required string key = 1;
    boolean hasKey();
    String getKey();
    
    // optional string forenames = 2;
    boolean hasForenames();
    String getForenames();
    
    // optional string surname = 3;
    boolean hasSurname();
    String getSurname();
    
    // optional string name = 4;
    boolean hasName();
    String getName();
    
    // optional string email = 5;
    boolean hasEmail();
    String getEmail();
    
    // repeated .KeyValue affiliationRef = 6;
    java.util.List<pl.edu.icm.coansys.models.DocumentProtos.KeyValue> 
        getAffiliationRefList();
    pl.edu.icm.coansys.models.DocumentProtos.KeyValue getAffiliationRef(int index);
    int getAffiliationRefCount();
    java.util.List<? extends pl.edu.icm.coansys.models.DocumentProtos.KeyValueOrBuilder> 
        getAffiliationRefOrBuilderList();
    pl.edu.icm.coansys.models.DocumentProtos.KeyValueOrBuilder getAffiliationRefOrBuilder(
        int index);
    
    // optional string docId = 7;
    boolean hasDocId();
    String getDocId();
    
    // optional int32 positionNumber = 8;
    boolean hasPositionNumber();
    int getPositionNumber();
    
    // repeated .KeyValue extId = 9;
    java.util.List<pl.edu.icm.coansys.models.DocumentProtos.KeyValue> 
        getExtIdList();
    pl.edu.icm.coansys.models.DocumentProtos.KeyValue getExtId(int index);
    int getExtIdCount();
    java.util.List<? extends pl.edu.icm.coansys.models.DocumentProtos.KeyValueOrBuilder> 
        getExtIdOrBuilderList();
    pl.edu.icm.coansys.models.DocumentProtos.KeyValueOrBuilder getExtIdOrBuilder(
        int index);
    
    // repeated .KeyValue auxiliarInfo = 10;
    java.util.List<pl.edu.icm.coansys.models.DocumentProtos.KeyValue> 
        getAuxiliarInfoList();
    pl.edu.icm.coansys.models.DocumentProtos.KeyValue getAuxiliarInfo(int index);
    int getAuxiliarInfoCount();
    java.util.List<? extends pl.edu.icm.coansys.models.DocumentProtos.KeyValueOrBuilder> 
        getAuxiliarInfoOrBuilderList();
    pl.edu.icm.coansys.models.DocumentProtos.KeyValueOrBuilder getAuxiliarInfoOrBuilder(
        int index);
  }
  public static final class Author extends
      com.google.protobuf.GeneratedMessage
      implements AuthorOrBuilder {
    // Use Author.newBuilder() to construct.
    private Author(Builder builder) {
      super(builder);
    }
    private Author(boolean noInit) {}
    
    private static final Author defaultInstance;
    public static Author getDefaultInstance() {
      return defaultInstance;
    }
    
    public Author getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return pl.edu.icm.coansys.models.DocumentProtos.internal_static_Author_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return pl.edu.icm.coansys.models.DocumentProtos.internal_static_Author_fieldAccessorTable;
    }
    
    private int bitField0_;
    // required string key = 1;
    public static final int KEY_FIELD_NUMBER = 1;
    private java.lang.Object key_;
    public boolean hasKey() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    public String getKey() {
      java.lang.Object ref = key_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          key_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getKeyBytes() {
      java.lang.Object ref = key_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        key_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // optional string forenames = 2;
    public static final int FORENAMES_FIELD_NUMBER = 2;
    private java.lang.Object forenames_;
    public boolean hasForenames() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    public String getForenames() {
      java.lang.Object ref = forenames_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          forenames_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getForenamesBytes() {
      java.lang.Object ref = forenames_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        forenames_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // optional string surname = 3;
    public static final int SURNAME_FIELD_NUMBER = 3;
    private java.lang.Object surname_;
    public boolean hasSurname() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    public String getSurname() {
      java.lang.Object ref = surname_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          surname_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getSurnameBytes() {
      java.lang.Object ref = surname_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        surname_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // optional string name = 4;
    public static final int NAME_FIELD_NUMBER = 4;
    private java.lang.Object name_;
    public boolean hasName() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    public String getName() {
      java.lang.Object ref = name_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          name_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getNameBytes() {
      java.lang.Object ref = name_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        name_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // optional string email = 5;
    public static final int EMAIL_FIELD_NUMBER = 5;
    private java.lang.Object email_;
    public boolean hasEmail() {
      return ((bitField0_ & 0x00000010) == 0x00000010);
    }
    public String getEmail() {
      java.lang.Object ref = email_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          email_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getEmailBytes() {
      java.lang.Object ref = email_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        email_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // repeated .KeyValue affiliationRef = 6;
    public static final int AFFILIATIONREF_FIELD_NUMBER = 6;
    private java.util.List<pl.edu.icm.coansys.models.DocumentProtos.KeyValue> affiliationRef_;
    public java.util.List<pl.edu.icm.coansys.models.DocumentProtos.KeyValue> getAffiliationRefList() {
      return affiliationRef_;
    }
    public java.util.List<? extends pl.edu.icm.coansys.models.DocumentProtos.KeyValueOrBuilder> 
        getAffiliationRefOrBuilderList() {
      return affiliationRef_;
    }
    public int getAffiliationRefCount() {
      return affiliationRef_.size();
    }
    public pl.edu.icm.coansys.models.DocumentProtos.KeyValue getAffiliationRef(int index) {
      return affiliationRef_.get(index);
    }
    public pl.edu.icm.coansys.models.DocumentProtos.KeyValueOrBuilder getAffiliationRefOrBuilder(
        int index) {
      return affiliationRef_.get(index);
    }
    
    // optional string docId = 7;
    public static final int DOCID_FIELD_NUMBER = 7;
    private java.lang.Object docId_;
    public boolean hasDocId() {
      return ((bitField0_ & 0x00000020) == 0x00000020);
    }
    public String getDocId() {
      java.lang.Object ref = docId_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          docId_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getDocIdBytes() {
      java.lang.Object ref = docId_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        docId_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // optional int32 positionNumber = 8;
    public static final int POSITIONNUMBER_FIELD_NUMBER = 8;
    private int positionNumber_;
    public boolean hasPositionNumber() {
      return ((bitField0_ & 0x00000040) == 0x00000040);
    }
    public int getPositionNumber() {
      return positionNumber_;
    }
    
    // repeated .KeyValue extId = 9;
    public static final int EXTID_FIELD_NUMBER = 9;
    private java.util.List<pl.edu.icm.coansys.models.DocumentProtos.KeyValue> extId_;
    public java.util.List<pl.edu.icm.coansys.models.DocumentProtos.KeyValue> getExtIdList() {
      return extId_;
    }
    public java.util.List<? extends pl.edu.icm.coansys.models.DocumentProtos.KeyValueOrBuilder> 
        getExtIdOrBuilderList() {
      return extId_;
    }
    public int getExtIdCount() {
      return extId_.size();
    }
    public pl.edu.icm.coansys.models.DocumentProtos.KeyValue getExtId(int index) {
      return extId_.get(index);
    }
    public pl.edu.icm.coansys.models.DocumentProtos.KeyValueOrBuilder getExtIdOrBuilder(
        int index) {
      return extId_.get(index);
    }
    
    // repeated .KeyValue auxiliarInfo = 10;
    public static final int AUXILIARINFO_FIELD_NUMBER = 10;
    private java.util.List<pl.edu.icm.coansys.models.DocumentProtos.KeyValue> auxiliarInfo_;
    public java.util.List<pl.edu.icm.coansys.models.DocumentProtos.KeyValue> getAuxiliarInfoList() {
      return auxiliarInfo_;
    }
    public java.util.List<? extends pl.edu.icm.coansys.models.DocumentProtos.KeyValueOrBuilder> 
        getAuxiliarInfoOrBuilderList() {
      return auxiliarInfo_;
    }
    public int getAuxiliarInfoCount() {
      return auxiliarInfo_.size();
    }
    public pl.edu.icm.coansys.models.DocumentProtos.KeyValue getAuxiliarInfo(int index) {
      return auxiliarInfo_.get(index);
    }
    public pl.edu.icm.coansys.models.DocumentProtos.KeyValueOrBuilder getAuxiliarInfoOrBuilder(
        int index) {
      return auxiliarInfo_.get(index);
    }
    
    private void initFields() {
      key_ = "";
      forenames_ = "";
      surname_ = "";
      name_ = "";
      email_ = "";
      affiliationRef_ = java.util.Collections.emptyList();
      docId_ = "";
      positionNumber_ = 0;
      extId_ = java.util.Collections.emptyList();
      auxiliarInfo_ = java.util.Collections.emptyList();
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
      if (!hasKey()) {
        memoizedIsInitialized = 0;
        return false;
      }
      for (int i = 0; i < getAffiliationRefCount(); i++) {
        if (!getAffiliationRef(i).isInitialized()) {
          memoizedIsInitialized = 0;
          return false;
        }
      }
      for (int i = 0; i < getExtIdCount(); i++) {
        if (!getExtId(i).isInitialized()) {
          memoizedIsInitialized = 0;
          return false;
        }
      }
      for (int i = 0; i < getAuxiliarInfoCount(); i++) {
        if (!getAuxiliarInfo(i).isInitialized()) {
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
        output.writeBytes(1, getKeyBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeBytes(2, getForenamesBytes());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeBytes(3, getSurnameBytes());
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeBytes(4, getNameBytes());
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        output.writeBytes(5, getEmailBytes());
      }
      for (int i = 0; i < affiliationRef_.size(); i++) {
        output.writeMessage(6, affiliationRef_.get(i));
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        output.writeBytes(7, getDocIdBytes());
      }
      if (((bitField0_ & 0x00000040) == 0x00000040)) {
        output.writeInt32(8, positionNumber_);
      }
      for (int i = 0; i < extId_.size(); i++) {
        output.writeMessage(9, extId_.get(i));
      }
      for (int i = 0; i < auxiliarInfo_.size(); i++) {
        output.writeMessage(10, auxiliarInfo_.get(i));
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
          .computeBytesSize(1, getKeyBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(2, getForenamesBytes());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(3, getSurnameBytes());
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(4, getNameBytes());
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(5, getEmailBytes());
      }
      for (int i = 0; i < affiliationRef_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(6, affiliationRef_.get(i));
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(7, getDocIdBytes());
      }
      if (((bitField0_ & 0x00000040) == 0x00000040)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(8, positionNumber_);
      }
      for (int i = 0; i < extId_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(9, extId_.get(i));
      }
      for (int i = 0; i < auxiliarInfo_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(10, auxiliarInfo_.get(i));
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
    
    public static pl.edu.icm.coansys.models.DocumentProtos.Author parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.Author parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.Author parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.Author parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.Author parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.Author parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.Author parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.Author parseDelimitedFrom(
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
    public static pl.edu.icm.coansys.models.DocumentProtos.Author parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.Author parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(pl.edu.icm.coansys.models.DocumentProtos.Author prototype) {
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
       implements pl.edu.icm.coansys.models.DocumentProtos.AuthorOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return pl.edu.icm.coansys.models.DocumentProtos.internal_static_Author_descriptor;
      }
      
      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return pl.edu.icm.coansys.models.DocumentProtos.internal_static_Author_fieldAccessorTable;
      }
      
      // Construct using pl.edu.icm.coansys.models.DocumentProtos.Author.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }
      
      private Builder(BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getAffiliationRefFieldBuilder();
          getExtIdFieldBuilder();
          getAuxiliarInfoFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }
      
      public Builder clear() {
        super.clear();
        key_ = "";
        bitField0_ = (bitField0_ & ~0x00000001);
        forenames_ = "";
        bitField0_ = (bitField0_ & ~0x00000002);
        surname_ = "";
        bitField0_ = (bitField0_ & ~0x00000004);
        name_ = "";
        bitField0_ = (bitField0_ & ~0x00000008);
        email_ = "";
        bitField0_ = (bitField0_ & ~0x00000010);
        if (affiliationRefBuilder_ == null) {
          affiliationRef_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000020);
        } else {
          affiliationRefBuilder_.clear();
        }
        docId_ = "";
        bitField0_ = (bitField0_ & ~0x00000040);
        positionNumber_ = 0;
        bitField0_ = (bitField0_ & ~0x00000080);
        if (extIdBuilder_ == null) {
          extId_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000100);
        } else {
          extIdBuilder_.clear();
        }
        if (auxiliarInfoBuilder_ == null) {
          auxiliarInfo_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000200);
        } else {
          auxiliarInfoBuilder_.clear();
        }
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return pl.edu.icm.coansys.models.DocumentProtos.Author.getDescriptor();
      }
      
      public pl.edu.icm.coansys.models.DocumentProtos.Author getDefaultInstanceForType() {
        return pl.edu.icm.coansys.models.DocumentProtos.Author.getDefaultInstance();
      }
      
      public pl.edu.icm.coansys.models.DocumentProtos.Author build() {
        pl.edu.icm.coansys.models.DocumentProtos.Author result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private pl.edu.icm.coansys.models.DocumentProtos.Author buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        pl.edu.icm.coansys.models.DocumentProtos.Author result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public pl.edu.icm.coansys.models.DocumentProtos.Author buildPartial() {
        pl.edu.icm.coansys.models.DocumentProtos.Author result = new pl.edu.icm.coansys.models.DocumentProtos.Author(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.key_ = key_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.forenames_ = forenames_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.surname_ = surname_;
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000008;
        }
        result.name_ = name_;
        if (((from_bitField0_ & 0x00000010) == 0x00000010)) {
          to_bitField0_ |= 0x00000010;
        }
        result.email_ = email_;
        if (affiliationRefBuilder_ == null) {
          if (((bitField0_ & 0x00000020) == 0x00000020)) {
            affiliationRef_ = java.util.Collections.unmodifiableList(affiliationRef_);
            bitField0_ = (bitField0_ & ~0x00000020);
          }
          result.affiliationRef_ = affiliationRef_;
        } else {
          result.affiliationRef_ = affiliationRefBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000040) == 0x00000040)) {
          to_bitField0_ |= 0x00000020;
        }
        result.docId_ = docId_;
        if (((from_bitField0_ & 0x00000080) == 0x00000080)) {
          to_bitField0_ |= 0x00000040;
        }
        result.positionNumber_ = positionNumber_;
        if (extIdBuilder_ == null) {
          if (((bitField0_ & 0x00000100) == 0x00000100)) {
            extId_ = java.util.Collections.unmodifiableList(extId_);
            bitField0_ = (bitField0_ & ~0x00000100);
          }
          result.extId_ = extId_;
        } else {
          result.extId_ = extIdBuilder_.build();
        }
        if (auxiliarInfoBuilder_ == null) {
          if (((bitField0_ & 0x00000200) == 0x00000200)) {
            auxiliarInfo_ = java.util.Collections.unmodifiableList(auxiliarInfo_);
            bitField0_ = (bitField0_ & ~0x00000200);
          }
          result.auxiliarInfo_ = auxiliarInfo_;
        } else {
          result.auxiliarInfo_ = auxiliarInfoBuilder_.build();
        }
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof pl.edu.icm.coansys.models.DocumentProtos.Author) {
          return mergeFrom((pl.edu.icm.coansys.models.DocumentProtos.Author)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(pl.edu.icm.coansys.models.DocumentProtos.Author other) {
        if (other == pl.edu.icm.coansys.models.DocumentProtos.Author.getDefaultInstance()) return this;
        if (other.hasKey()) {
          setKey(other.getKey());
        }
        if (other.hasForenames()) {
          setForenames(other.getForenames());
        }
        if (other.hasSurname()) {
          setSurname(other.getSurname());
        }
        if (other.hasName()) {
          setName(other.getName());
        }
        if (other.hasEmail()) {
          setEmail(other.getEmail());
        }
        if (affiliationRefBuilder_ == null) {
          if (!other.affiliationRef_.isEmpty()) {
            if (affiliationRef_.isEmpty()) {
              affiliationRef_ = other.affiliationRef_;
              bitField0_ = (bitField0_ & ~0x00000020);
            } else {
              ensureAffiliationRefIsMutable();
              affiliationRef_.addAll(other.affiliationRef_);
            }
            onChanged();
          }
        } else {
          if (!other.affiliationRef_.isEmpty()) {
            if (affiliationRefBuilder_.isEmpty()) {
              affiliationRefBuilder_.dispose();
              affiliationRefBuilder_ = null;
              affiliationRef_ = other.affiliationRef_;
              bitField0_ = (bitField0_ & ~0x00000020);
              affiliationRefBuilder_ = 
                com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                   getAffiliationRefFieldBuilder() : null;
            } else {
              affiliationRefBuilder_.addAllMessages(other.affiliationRef_);
            }
          }
        }
        if (other.hasDocId()) {
          setDocId(other.getDocId());
        }
        if (other.hasPositionNumber()) {
          setPositionNumber(other.getPositionNumber());
        }
        if (extIdBuilder_ == null) {
          if (!other.extId_.isEmpty()) {
            if (extId_.isEmpty()) {
              extId_ = other.extId_;
              bitField0_ = (bitField0_ & ~0x00000100);
            } else {
              ensureExtIdIsMutable();
              extId_.addAll(other.extId_);
            }
            onChanged();
          }
        } else {
          if (!other.extId_.isEmpty()) {
            if (extIdBuilder_.isEmpty()) {
              extIdBuilder_.dispose();
              extIdBuilder_ = null;
              extId_ = other.extId_;
              bitField0_ = (bitField0_ & ~0x00000100);
              extIdBuilder_ = 
                com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                   getExtIdFieldBuilder() : null;
            } else {
              extIdBuilder_.addAllMessages(other.extId_);
            }
          }
        }
        if (auxiliarInfoBuilder_ == null) {
          if (!other.auxiliarInfo_.isEmpty()) {
            if (auxiliarInfo_.isEmpty()) {
              auxiliarInfo_ = other.auxiliarInfo_;
              bitField0_ = (bitField0_ & ~0x00000200);
            } else {
              ensureAuxiliarInfoIsMutable();
              auxiliarInfo_.addAll(other.auxiliarInfo_);
            }
            onChanged();
          }
        } else {
          if (!other.auxiliarInfo_.isEmpty()) {
            if (auxiliarInfoBuilder_.isEmpty()) {
              auxiliarInfoBuilder_.dispose();
              auxiliarInfoBuilder_ = null;
              auxiliarInfo_ = other.auxiliarInfo_;
              bitField0_ = (bitField0_ & ~0x00000200);
              auxiliarInfoBuilder_ = 
                com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                   getAuxiliarInfoFieldBuilder() : null;
            } else {
              auxiliarInfoBuilder_.addAllMessages(other.auxiliarInfo_);
            }
          }
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
        if (!hasKey()) {
          
          return false;
        }
        for (int i = 0; i < getAffiliationRefCount(); i++) {
          if (!getAffiliationRef(i).isInitialized()) {
            
            return false;
          }
        }
        for (int i = 0; i < getExtIdCount(); i++) {
          if (!getExtId(i).isInitialized()) {
            
            return false;
          }
        }
        for (int i = 0; i < getAuxiliarInfoCount(); i++) {
          if (!getAuxiliarInfo(i).isInitialized()) {
            
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
              key_ = input.readBytes();
              break;
            }
            case 18: {
              bitField0_ |= 0x00000002;
              forenames_ = input.readBytes();
              break;
            }
            case 26: {
              bitField0_ |= 0x00000004;
              surname_ = input.readBytes();
              break;
            }
            case 34: {
              bitField0_ |= 0x00000008;
              name_ = input.readBytes();
              break;
            }
            case 42: {
              bitField0_ |= 0x00000010;
              email_ = input.readBytes();
              break;
            }
            case 50: {
              pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder subBuilder = pl.edu.icm.coansys.models.DocumentProtos.KeyValue.newBuilder();
              input.readMessage(subBuilder, extensionRegistry);
              addAffiliationRef(subBuilder.buildPartial());
              break;
            }
            case 58: {
              bitField0_ |= 0x00000040;
              docId_ = input.readBytes();
              break;
            }
            case 64: {
              bitField0_ |= 0x00000080;
              positionNumber_ = input.readInt32();
              break;
            }
            case 74: {
              pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder subBuilder = pl.edu.icm.coansys.models.DocumentProtos.KeyValue.newBuilder();
              input.readMessage(subBuilder, extensionRegistry);
              addExtId(subBuilder.buildPartial());
              break;
            }
            case 82: {
              pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder subBuilder = pl.edu.icm.coansys.models.DocumentProtos.KeyValue.newBuilder();
              input.readMessage(subBuilder, extensionRegistry);
              addAuxiliarInfo(subBuilder.buildPartial());
              break;
            }
          }
        }
      }
      
      private int bitField0_;
      
      // required string key = 1;
      private java.lang.Object key_ = "";
      public boolean hasKey() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      public String getKey() {
        java.lang.Object ref = key_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          key_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setKey(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        key_ = value;
        onChanged();
        return this;
      }
      public Builder clearKey() {
        bitField0_ = (bitField0_ & ~0x00000001);
        key_ = getDefaultInstance().getKey();
        onChanged();
        return this;
      }
      void setKey(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000001;
        key_ = value;
        onChanged();
      }
      
      // optional string forenames = 2;
      private java.lang.Object forenames_ = "";
      public boolean hasForenames() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      public String getForenames() {
        java.lang.Object ref = forenames_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          forenames_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setForenames(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        forenames_ = value;
        onChanged();
        return this;
      }
      public Builder clearForenames() {
        bitField0_ = (bitField0_ & ~0x00000002);
        forenames_ = getDefaultInstance().getForenames();
        onChanged();
        return this;
      }
      void setForenames(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000002;
        forenames_ = value;
        onChanged();
      }
      
      // optional string surname = 3;
      private java.lang.Object surname_ = "";
      public boolean hasSurname() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      public String getSurname() {
        java.lang.Object ref = surname_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          surname_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setSurname(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000004;
        surname_ = value;
        onChanged();
        return this;
      }
      public Builder clearSurname() {
        bitField0_ = (bitField0_ & ~0x00000004);
        surname_ = getDefaultInstance().getSurname();
        onChanged();
        return this;
      }
      void setSurname(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000004;
        surname_ = value;
        onChanged();
      }
      
      // optional string name = 4;
      private java.lang.Object name_ = "";
      public boolean hasName() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      public String getName() {
        java.lang.Object ref = name_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          name_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setName(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000008;
        name_ = value;
        onChanged();
        return this;
      }
      public Builder clearName() {
        bitField0_ = (bitField0_ & ~0x00000008);
        name_ = getDefaultInstance().getName();
        onChanged();
        return this;
      }
      void setName(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000008;
        name_ = value;
        onChanged();
      }
      
      // optional string email = 5;
      private java.lang.Object email_ = "";
      public boolean hasEmail() {
        return ((bitField0_ & 0x00000010) == 0x00000010);
      }
      public String getEmail() {
        java.lang.Object ref = email_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          email_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setEmail(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000010;
        email_ = value;
        onChanged();
        return this;
      }
      public Builder clearEmail() {
        bitField0_ = (bitField0_ & ~0x00000010);
        email_ = getDefaultInstance().getEmail();
        onChanged();
        return this;
      }
      void setEmail(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000010;
        email_ = value;
        onChanged();
      }
      
      // repeated .KeyValue affiliationRef = 6;
      private java.util.List<pl.edu.icm.coansys.models.DocumentProtos.KeyValue> affiliationRef_ =
        java.util.Collections.emptyList();
      private void ensureAffiliationRefIsMutable() {
        if (!((bitField0_ & 0x00000020) == 0x00000020)) {
          affiliationRef_ = new java.util.ArrayList<pl.edu.icm.coansys.models.DocumentProtos.KeyValue>(affiliationRef_);
          bitField0_ |= 0x00000020;
         }
      }
      
      private com.google.protobuf.RepeatedFieldBuilder<
          pl.edu.icm.coansys.models.DocumentProtos.KeyValue, pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder, pl.edu.icm.coansys.models.DocumentProtos.KeyValueOrBuilder> affiliationRefBuilder_;
      
      public java.util.List<pl.edu.icm.coansys.models.DocumentProtos.KeyValue> getAffiliationRefList() {
        if (affiliationRefBuilder_ == null) {
          return java.util.Collections.unmodifiableList(affiliationRef_);
        } else {
          return affiliationRefBuilder_.getMessageList();
        }
      }
      public int getAffiliationRefCount() {
        if (affiliationRefBuilder_ == null) {
          return affiliationRef_.size();
        } else {
          return affiliationRefBuilder_.getCount();
        }
      }
      public pl.edu.icm.coansys.models.DocumentProtos.KeyValue getAffiliationRef(int index) {
        if (affiliationRefBuilder_ == null) {
          return affiliationRef_.get(index);
        } else {
          return affiliationRefBuilder_.getMessage(index);
        }
      }
      public Builder setAffiliationRef(
          int index, pl.edu.icm.coansys.models.DocumentProtos.KeyValue value) {
        if (affiliationRefBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureAffiliationRefIsMutable();
          affiliationRef_.set(index, value);
          onChanged();
        } else {
          affiliationRefBuilder_.setMessage(index, value);
        }
        return this;
      }
      public Builder setAffiliationRef(
          int index, pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder builderForValue) {
        if (affiliationRefBuilder_ == null) {
          ensureAffiliationRefIsMutable();
          affiliationRef_.set(index, builderForValue.build());
          onChanged();
        } else {
          affiliationRefBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }
      public Builder addAffiliationRef(pl.edu.icm.coansys.models.DocumentProtos.KeyValue value) {
        if (affiliationRefBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureAffiliationRefIsMutable();
          affiliationRef_.add(value);
          onChanged();
        } else {
          affiliationRefBuilder_.addMessage(value);
        }
        return this;
      }
      public Builder addAffiliationRef(
          int index, pl.edu.icm.coansys.models.DocumentProtos.KeyValue value) {
        if (affiliationRefBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureAffiliationRefIsMutable();
          affiliationRef_.add(index, value);
          onChanged();
        } else {
          affiliationRefBuilder_.addMessage(index, value);
        }
        return this;
      }
      public Builder addAffiliationRef(
          pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder builderForValue) {
        if (affiliationRefBuilder_ == null) {
          ensureAffiliationRefIsMutable();
          affiliationRef_.add(builderForValue.build());
          onChanged();
        } else {
          affiliationRefBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }
      public Builder addAffiliationRef(
          int index, pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder builderForValue) {
        if (affiliationRefBuilder_ == null) {
          ensureAffiliationRefIsMutable();
          affiliationRef_.add(index, builderForValue.build());
          onChanged();
        } else {
          affiliationRefBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }
      public Builder addAllAffiliationRef(
          java.lang.Iterable<? extends pl.edu.icm.coansys.models.DocumentProtos.KeyValue> values) {
        if (affiliationRefBuilder_ == null) {
          ensureAffiliationRefIsMutable();
          super.addAll(values, affiliationRef_);
          onChanged();
        } else {
          affiliationRefBuilder_.addAllMessages(values);
        }
        return this;
      }
      public Builder clearAffiliationRef() {
        if (affiliationRefBuilder_ == null) {
          affiliationRef_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000020);
          onChanged();
        } else {
          affiliationRefBuilder_.clear();
        }
        return this;
      }
      public Builder removeAffiliationRef(int index) {
        if (affiliationRefBuilder_ == null) {
          ensureAffiliationRefIsMutable();
          affiliationRef_.remove(index);
          onChanged();
        } else {
          affiliationRefBuilder_.remove(index);
        }
        return this;
      }
      public pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder getAffiliationRefBuilder(
          int index) {
        return getAffiliationRefFieldBuilder().getBuilder(index);
      }
      public pl.edu.icm.coansys.models.DocumentProtos.KeyValueOrBuilder getAffiliationRefOrBuilder(
          int index) {
        if (affiliationRefBuilder_ == null) {
          return affiliationRef_.get(index);  } else {
          return affiliationRefBuilder_.getMessageOrBuilder(index);
        }
      }
      public java.util.List<? extends pl.edu.icm.coansys.models.DocumentProtos.KeyValueOrBuilder> 
           getAffiliationRefOrBuilderList() {
        if (affiliationRefBuilder_ != null) {
          return affiliationRefBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(affiliationRef_);
        }
      }
      public pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder addAffiliationRefBuilder() {
        return getAffiliationRefFieldBuilder().addBuilder(
            pl.edu.icm.coansys.models.DocumentProtos.KeyValue.getDefaultInstance());
      }
      public pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder addAffiliationRefBuilder(
          int index) {
        return getAffiliationRefFieldBuilder().addBuilder(
            index, pl.edu.icm.coansys.models.DocumentProtos.KeyValue.getDefaultInstance());
      }
      public java.util.List<pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder> 
           getAffiliationRefBuilderList() {
        return getAffiliationRefFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilder<
          pl.edu.icm.coansys.models.DocumentProtos.KeyValue, pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder, pl.edu.icm.coansys.models.DocumentProtos.KeyValueOrBuilder> 
          getAffiliationRefFieldBuilder() {
        if (affiliationRefBuilder_ == null) {
          affiliationRefBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
              pl.edu.icm.coansys.models.DocumentProtos.KeyValue, pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder, pl.edu.icm.coansys.models.DocumentProtos.KeyValueOrBuilder>(
                  affiliationRef_,
                  ((bitField0_ & 0x00000020) == 0x00000020),
                  getParentForChildren(),
                  isClean());
          affiliationRef_ = null;
        }
        return affiliationRefBuilder_;
      }
      
      // optional string docId = 7;
      private java.lang.Object docId_ = "";
      public boolean hasDocId() {
        return ((bitField0_ & 0x00000040) == 0x00000040);
      }
      public String getDocId() {
        java.lang.Object ref = docId_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          docId_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setDocId(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000040;
        docId_ = value;
        onChanged();
        return this;
      }
      public Builder clearDocId() {
        bitField0_ = (bitField0_ & ~0x00000040);
        docId_ = getDefaultInstance().getDocId();
        onChanged();
        return this;
      }
      void setDocId(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000040;
        docId_ = value;
        onChanged();
      }
      
      // optional int32 positionNumber = 8;
      private int positionNumber_ ;
      public boolean hasPositionNumber() {
        return ((bitField0_ & 0x00000080) == 0x00000080);
      }
      public int getPositionNumber() {
        return positionNumber_;
      }
      public Builder setPositionNumber(int value) {
        bitField0_ |= 0x00000080;
        positionNumber_ = value;
        onChanged();
        return this;
      }
      public Builder clearPositionNumber() {
        bitField0_ = (bitField0_ & ~0x00000080);
        positionNumber_ = 0;
        onChanged();
        return this;
      }
      
      // repeated .KeyValue extId = 9;
      private java.util.List<pl.edu.icm.coansys.models.DocumentProtos.KeyValue> extId_ =
        java.util.Collections.emptyList();
      private void ensureExtIdIsMutable() {
        if (!((bitField0_ & 0x00000100) == 0x00000100)) {
          extId_ = new java.util.ArrayList<pl.edu.icm.coansys.models.DocumentProtos.KeyValue>(extId_);
          bitField0_ |= 0x00000100;
         }
      }
      
      private com.google.protobuf.RepeatedFieldBuilder<
          pl.edu.icm.coansys.models.DocumentProtos.KeyValue, pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder, pl.edu.icm.coansys.models.DocumentProtos.KeyValueOrBuilder> extIdBuilder_;
      
      public java.util.List<pl.edu.icm.coansys.models.DocumentProtos.KeyValue> getExtIdList() {
        if (extIdBuilder_ == null) {
          return java.util.Collections.unmodifiableList(extId_);
        } else {
          return extIdBuilder_.getMessageList();
        }
      }
      public int getExtIdCount() {
        if (extIdBuilder_ == null) {
          return extId_.size();
        } else {
          return extIdBuilder_.getCount();
        }
      }
      public pl.edu.icm.coansys.models.DocumentProtos.KeyValue getExtId(int index) {
        if (extIdBuilder_ == null) {
          return extId_.get(index);
        } else {
          return extIdBuilder_.getMessage(index);
        }
      }
      public Builder setExtId(
          int index, pl.edu.icm.coansys.models.DocumentProtos.KeyValue value) {
        if (extIdBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureExtIdIsMutable();
          extId_.set(index, value);
          onChanged();
        } else {
          extIdBuilder_.setMessage(index, value);
        }
        return this;
      }
      public Builder setExtId(
          int index, pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder builderForValue) {
        if (extIdBuilder_ == null) {
          ensureExtIdIsMutable();
          extId_.set(index, builderForValue.build());
          onChanged();
        } else {
          extIdBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }
      public Builder addExtId(pl.edu.icm.coansys.models.DocumentProtos.KeyValue value) {
        if (extIdBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureExtIdIsMutable();
          extId_.add(value);
          onChanged();
        } else {
          extIdBuilder_.addMessage(value);
        }
        return this;
      }
      public Builder addExtId(
          int index, pl.edu.icm.coansys.models.DocumentProtos.KeyValue value) {
        if (extIdBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureExtIdIsMutable();
          extId_.add(index, value);
          onChanged();
        } else {
          extIdBuilder_.addMessage(index, value);
        }
        return this;
      }
      public Builder addExtId(
          pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder builderForValue) {
        if (extIdBuilder_ == null) {
          ensureExtIdIsMutable();
          extId_.add(builderForValue.build());
          onChanged();
        } else {
          extIdBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }
      public Builder addExtId(
          int index, pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder builderForValue) {
        if (extIdBuilder_ == null) {
          ensureExtIdIsMutable();
          extId_.add(index, builderForValue.build());
          onChanged();
        } else {
          extIdBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }
      public Builder addAllExtId(
          java.lang.Iterable<? extends pl.edu.icm.coansys.models.DocumentProtos.KeyValue> values) {
        if (extIdBuilder_ == null) {
          ensureExtIdIsMutable();
          super.addAll(values, extId_);
          onChanged();
        } else {
          extIdBuilder_.addAllMessages(values);
        }
        return this;
      }
      public Builder clearExtId() {
        if (extIdBuilder_ == null) {
          extId_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000100);
          onChanged();
        } else {
          extIdBuilder_.clear();
        }
        return this;
      }
      public Builder removeExtId(int index) {
        if (extIdBuilder_ == null) {
          ensureExtIdIsMutable();
          extId_.remove(index);
          onChanged();
        } else {
          extIdBuilder_.remove(index);
        }
        return this;
      }
      public pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder getExtIdBuilder(
          int index) {
        return getExtIdFieldBuilder().getBuilder(index);
      }
      public pl.edu.icm.coansys.models.DocumentProtos.KeyValueOrBuilder getExtIdOrBuilder(
          int index) {
        if (extIdBuilder_ == null) {
          return extId_.get(index);  } else {
          return extIdBuilder_.getMessageOrBuilder(index);
        }
      }
      public java.util.List<? extends pl.edu.icm.coansys.models.DocumentProtos.KeyValueOrBuilder> 
           getExtIdOrBuilderList() {
        if (extIdBuilder_ != null) {
          return extIdBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(extId_);
        }
      }
      public pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder addExtIdBuilder() {
        return getExtIdFieldBuilder().addBuilder(
            pl.edu.icm.coansys.models.DocumentProtos.KeyValue.getDefaultInstance());
      }
      public pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder addExtIdBuilder(
          int index) {
        return getExtIdFieldBuilder().addBuilder(
            index, pl.edu.icm.coansys.models.DocumentProtos.KeyValue.getDefaultInstance());
      }
      public java.util.List<pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder> 
           getExtIdBuilderList() {
        return getExtIdFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilder<
          pl.edu.icm.coansys.models.DocumentProtos.KeyValue, pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder, pl.edu.icm.coansys.models.DocumentProtos.KeyValueOrBuilder> 
          getExtIdFieldBuilder() {
        if (extIdBuilder_ == null) {
          extIdBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
              pl.edu.icm.coansys.models.DocumentProtos.KeyValue, pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder, pl.edu.icm.coansys.models.DocumentProtos.KeyValueOrBuilder>(
                  extId_,
                  ((bitField0_ & 0x00000100) == 0x00000100),
                  getParentForChildren(),
                  isClean());
          extId_ = null;
        }
        return extIdBuilder_;
      }
      
      // repeated .KeyValue auxiliarInfo = 10;
      private java.util.List<pl.edu.icm.coansys.models.DocumentProtos.KeyValue> auxiliarInfo_ =
        java.util.Collections.emptyList();
      private void ensureAuxiliarInfoIsMutable() {
        if (!((bitField0_ & 0x00000200) == 0x00000200)) {
          auxiliarInfo_ = new java.util.ArrayList<pl.edu.icm.coansys.models.DocumentProtos.KeyValue>(auxiliarInfo_);
          bitField0_ |= 0x00000200;
         }
      }
      
      private com.google.protobuf.RepeatedFieldBuilder<
          pl.edu.icm.coansys.models.DocumentProtos.KeyValue, pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder, pl.edu.icm.coansys.models.DocumentProtos.KeyValueOrBuilder> auxiliarInfoBuilder_;
      
      public java.util.List<pl.edu.icm.coansys.models.DocumentProtos.KeyValue> getAuxiliarInfoList() {
        if (auxiliarInfoBuilder_ == null) {
          return java.util.Collections.unmodifiableList(auxiliarInfo_);
        } else {
          return auxiliarInfoBuilder_.getMessageList();
        }
      }
      public int getAuxiliarInfoCount() {
        if (auxiliarInfoBuilder_ == null) {
          return auxiliarInfo_.size();
        } else {
          return auxiliarInfoBuilder_.getCount();
        }
      }
      public pl.edu.icm.coansys.models.DocumentProtos.KeyValue getAuxiliarInfo(int index) {
        if (auxiliarInfoBuilder_ == null) {
          return auxiliarInfo_.get(index);
        } else {
          return auxiliarInfoBuilder_.getMessage(index);
        }
      }
      public Builder setAuxiliarInfo(
          int index, pl.edu.icm.coansys.models.DocumentProtos.KeyValue value) {
        if (auxiliarInfoBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureAuxiliarInfoIsMutable();
          auxiliarInfo_.set(index, value);
          onChanged();
        } else {
          auxiliarInfoBuilder_.setMessage(index, value);
        }
        return this;
      }
      public Builder setAuxiliarInfo(
          int index, pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder builderForValue) {
        if (auxiliarInfoBuilder_ == null) {
          ensureAuxiliarInfoIsMutable();
          auxiliarInfo_.set(index, builderForValue.build());
          onChanged();
        } else {
          auxiliarInfoBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }
      public Builder addAuxiliarInfo(pl.edu.icm.coansys.models.DocumentProtos.KeyValue value) {
        if (auxiliarInfoBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureAuxiliarInfoIsMutable();
          auxiliarInfo_.add(value);
          onChanged();
        } else {
          auxiliarInfoBuilder_.addMessage(value);
        }
        return this;
      }
      public Builder addAuxiliarInfo(
          int index, pl.edu.icm.coansys.models.DocumentProtos.KeyValue value) {
        if (auxiliarInfoBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureAuxiliarInfoIsMutable();
          auxiliarInfo_.add(index, value);
          onChanged();
        } else {
          auxiliarInfoBuilder_.addMessage(index, value);
        }
        return this;
      }
      public Builder addAuxiliarInfo(
          pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder builderForValue) {
        if (auxiliarInfoBuilder_ == null) {
          ensureAuxiliarInfoIsMutable();
          auxiliarInfo_.add(builderForValue.build());
          onChanged();
        } else {
          auxiliarInfoBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }
      public Builder addAuxiliarInfo(
          int index, pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder builderForValue) {
        if (auxiliarInfoBuilder_ == null) {
          ensureAuxiliarInfoIsMutable();
          auxiliarInfo_.add(index, builderForValue.build());
          onChanged();
        } else {
          auxiliarInfoBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }
      public Builder addAllAuxiliarInfo(
          java.lang.Iterable<? extends pl.edu.icm.coansys.models.DocumentProtos.KeyValue> values) {
        if (auxiliarInfoBuilder_ == null) {
          ensureAuxiliarInfoIsMutable();
          super.addAll(values, auxiliarInfo_);
          onChanged();
        } else {
          auxiliarInfoBuilder_.addAllMessages(values);
        }
        return this;
      }
      public Builder clearAuxiliarInfo() {
        if (auxiliarInfoBuilder_ == null) {
          auxiliarInfo_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000200);
          onChanged();
        } else {
          auxiliarInfoBuilder_.clear();
        }
        return this;
      }
      public Builder removeAuxiliarInfo(int index) {
        if (auxiliarInfoBuilder_ == null) {
          ensureAuxiliarInfoIsMutable();
          auxiliarInfo_.remove(index);
          onChanged();
        } else {
          auxiliarInfoBuilder_.remove(index);
        }
        return this;
      }
      public pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder getAuxiliarInfoBuilder(
          int index) {
        return getAuxiliarInfoFieldBuilder().getBuilder(index);
      }
      public pl.edu.icm.coansys.models.DocumentProtos.KeyValueOrBuilder getAuxiliarInfoOrBuilder(
          int index) {
        if (auxiliarInfoBuilder_ == null) {
          return auxiliarInfo_.get(index);  } else {
          return auxiliarInfoBuilder_.getMessageOrBuilder(index);
        }
      }
      public java.util.List<? extends pl.edu.icm.coansys.models.DocumentProtos.KeyValueOrBuilder> 
           getAuxiliarInfoOrBuilderList() {
        if (auxiliarInfoBuilder_ != null) {
          return auxiliarInfoBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(auxiliarInfo_);
        }
      }
      public pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder addAuxiliarInfoBuilder() {
        return getAuxiliarInfoFieldBuilder().addBuilder(
            pl.edu.icm.coansys.models.DocumentProtos.KeyValue.getDefaultInstance());
      }
      public pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder addAuxiliarInfoBuilder(
          int index) {
        return getAuxiliarInfoFieldBuilder().addBuilder(
            index, pl.edu.icm.coansys.models.DocumentProtos.KeyValue.getDefaultInstance());
      }
      public java.util.List<pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder> 
           getAuxiliarInfoBuilderList() {
        return getAuxiliarInfoFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilder<
          pl.edu.icm.coansys.models.DocumentProtos.KeyValue, pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder, pl.edu.icm.coansys.models.DocumentProtos.KeyValueOrBuilder> 
          getAuxiliarInfoFieldBuilder() {
        if (auxiliarInfoBuilder_ == null) {
          auxiliarInfoBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
              pl.edu.icm.coansys.models.DocumentProtos.KeyValue, pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder, pl.edu.icm.coansys.models.DocumentProtos.KeyValueOrBuilder>(
                  auxiliarInfo_,
                  ((bitField0_ & 0x00000200) == 0x00000200),
                  getParentForChildren(),
                  isClean());
          auxiliarInfo_ = null;
        }
        return auxiliarInfoBuilder_;
      }
      
      // @@protoc_insertion_point(builder_scope:Author)
    }
    
    static {
      defaultInstance = new Author(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:Author)
  }
  
  public interface BasicMetadataOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // repeated .TextWithLanguage title = 1;
    java.util.List<pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage> 
        getTitleList();
    pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage getTitle(int index);
    int getTitleCount();
    java.util.List<? extends pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguageOrBuilder> 
        getTitleOrBuilderList();
    pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguageOrBuilder getTitleOrBuilder(
        int index);
    
    // repeated .Author author = 2;
    java.util.List<pl.edu.icm.coansys.models.DocumentProtos.Author> 
        getAuthorList();
    pl.edu.icm.coansys.models.DocumentProtos.Author getAuthor(int index);
    int getAuthorCount();
    java.util.List<? extends pl.edu.icm.coansys.models.DocumentProtos.AuthorOrBuilder> 
        getAuthorOrBuilderList();
    pl.edu.icm.coansys.models.DocumentProtos.AuthorOrBuilder getAuthorOrBuilder(
        int index);
    
    // optional string doi = 3;
    boolean hasDoi();
    String getDoi();
    
    // optional string journal = 4;
    boolean hasJournal();
    String getJournal();
    
    // optional string isbn = 5;
    boolean hasIsbn();
    String getIsbn();
    
    // optional string issn = 6;
    boolean hasIssn();
    String getIssn();
    
    // optional string year = 7;
    boolean hasYear();
    String getYear();
    
    // optional string issue = 8;
    boolean hasIssue();
    String getIssue();
    
    // optional string volume = 9;
    boolean hasVolume();
    String getVolume();
    
    // optional string pages = 10;
    boolean hasPages();
    String getPages();
    
    // repeated .ClassifCode classifCode = 11;
    java.util.List<pl.edu.icm.coansys.models.DocumentProtos.ClassifCode> 
        getClassifCodeList();
    pl.edu.icm.coansys.models.DocumentProtos.ClassifCode getClassifCode(int index);
    int getClassifCodeCount();
    java.util.List<? extends pl.edu.icm.coansys.models.DocumentProtos.ClassifCodeOrBuilder> 
        getClassifCodeOrBuilderList();
    pl.edu.icm.coansys.models.DocumentProtos.ClassifCodeOrBuilder getClassifCodeOrBuilder(
        int index);
  }
  public static final class BasicMetadata extends
      com.google.protobuf.GeneratedMessage
      implements BasicMetadataOrBuilder {
    // Use BasicMetadata.newBuilder() to construct.
    private BasicMetadata(Builder builder) {
      super(builder);
    }
    private BasicMetadata(boolean noInit) {}
    
    private static final BasicMetadata defaultInstance;
    public static BasicMetadata getDefaultInstance() {
      return defaultInstance;
    }
    
    public BasicMetadata getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return pl.edu.icm.coansys.models.DocumentProtos.internal_static_BasicMetadata_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return pl.edu.icm.coansys.models.DocumentProtos.internal_static_BasicMetadata_fieldAccessorTable;
    }
    
    private int bitField0_;
    // repeated .TextWithLanguage title = 1;
    public static final int TITLE_FIELD_NUMBER = 1;
    private java.util.List<pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage> title_;
    public java.util.List<pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage> getTitleList() {
      return title_;
    }
    public java.util.List<? extends pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguageOrBuilder> 
        getTitleOrBuilderList() {
      return title_;
    }
    public int getTitleCount() {
      return title_.size();
    }
    public pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage getTitle(int index) {
      return title_.get(index);
    }
    public pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguageOrBuilder getTitleOrBuilder(
        int index) {
      return title_.get(index);
    }
    
    // repeated .Author author = 2;
    public static final int AUTHOR_FIELD_NUMBER = 2;
    private java.util.List<pl.edu.icm.coansys.models.DocumentProtos.Author> author_;
    public java.util.List<pl.edu.icm.coansys.models.DocumentProtos.Author> getAuthorList() {
      return author_;
    }
    public java.util.List<? extends pl.edu.icm.coansys.models.DocumentProtos.AuthorOrBuilder> 
        getAuthorOrBuilderList() {
      return author_;
    }
    public int getAuthorCount() {
      return author_.size();
    }
    public pl.edu.icm.coansys.models.DocumentProtos.Author getAuthor(int index) {
      return author_.get(index);
    }
    public pl.edu.icm.coansys.models.DocumentProtos.AuthorOrBuilder getAuthorOrBuilder(
        int index) {
      return author_.get(index);
    }
    
    // optional string doi = 3;
    public static final int DOI_FIELD_NUMBER = 3;
    private java.lang.Object doi_;
    public boolean hasDoi() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    public String getDoi() {
      java.lang.Object ref = doi_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          doi_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getDoiBytes() {
      java.lang.Object ref = doi_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        doi_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // optional string journal = 4;
    public static final int JOURNAL_FIELD_NUMBER = 4;
    private java.lang.Object journal_;
    public boolean hasJournal() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    public String getJournal() {
      java.lang.Object ref = journal_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          journal_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getJournalBytes() {
      java.lang.Object ref = journal_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        journal_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // optional string isbn = 5;
    public static final int ISBN_FIELD_NUMBER = 5;
    private java.lang.Object isbn_;
    public boolean hasIsbn() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    public String getIsbn() {
      java.lang.Object ref = isbn_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          isbn_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getIsbnBytes() {
      java.lang.Object ref = isbn_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        isbn_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // optional string issn = 6;
    public static final int ISSN_FIELD_NUMBER = 6;
    private java.lang.Object issn_;
    public boolean hasIssn() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    public String getIssn() {
      java.lang.Object ref = issn_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          issn_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getIssnBytes() {
      java.lang.Object ref = issn_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        issn_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // optional string year = 7;
    public static final int YEAR_FIELD_NUMBER = 7;
    private java.lang.Object year_;
    public boolean hasYear() {
      return ((bitField0_ & 0x00000010) == 0x00000010);
    }
    public String getYear() {
      java.lang.Object ref = year_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          year_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getYearBytes() {
      java.lang.Object ref = year_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        year_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // optional string issue = 8;
    public static final int ISSUE_FIELD_NUMBER = 8;
    private java.lang.Object issue_;
    public boolean hasIssue() {
      return ((bitField0_ & 0x00000020) == 0x00000020);
    }
    public String getIssue() {
      java.lang.Object ref = issue_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          issue_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getIssueBytes() {
      java.lang.Object ref = issue_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        issue_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // optional string volume = 9;
    public static final int VOLUME_FIELD_NUMBER = 9;
    private java.lang.Object volume_;
    public boolean hasVolume() {
      return ((bitField0_ & 0x00000040) == 0x00000040);
    }
    public String getVolume() {
      java.lang.Object ref = volume_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          volume_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getVolumeBytes() {
      java.lang.Object ref = volume_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        volume_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // optional string pages = 10;
    public static final int PAGES_FIELD_NUMBER = 10;
    private java.lang.Object pages_;
    public boolean hasPages() {
      return ((bitField0_ & 0x00000080) == 0x00000080);
    }
    public String getPages() {
      java.lang.Object ref = pages_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          pages_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getPagesBytes() {
      java.lang.Object ref = pages_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        pages_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // repeated .ClassifCode classifCode = 11;
    public static final int CLASSIFCODE_FIELD_NUMBER = 11;
    private java.util.List<pl.edu.icm.coansys.models.DocumentProtos.ClassifCode> classifCode_;
    public java.util.List<pl.edu.icm.coansys.models.DocumentProtos.ClassifCode> getClassifCodeList() {
      return classifCode_;
    }
    public java.util.List<? extends pl.edu.icm.coansys.models.DocumentProtos.ClassifCodeOrBuilder> 
        getClassifCodeOrBuilderList() {
      return classifCode_;
    }
    public int getClassifCodeCount() {
      return classifCode_.size();
    }
    public pl.edu.icm.coansys.models.DocumentProtos.ClassifCode getClassifCode(int index) {
      return classifCode_.get(index);
    }
    public pl.edu.icm.coansys.models.DocumentProtos.ClassifCodeOrBuilder getClassifCodeOrBuilder(
        int index) {
      return classifCode_.get(index);
    }
    
    private void initFields() {
      title_ = java.util.Collections.emptyList();
      author_ = java.util.Collections.emptyList();
      doi_ = "";
      journal_ = "";
      isbn_ = "";
      issn_ = "";
      year_ = "";
      issue_ = "";
      volume_ = "";
      pages_ = "";
      classifCode_ = java.util.Collections.emptyList();
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
      for (int i = 0; i < getTitleCount(); i++) {
        if (!getTitle(i).isInitialized()) {
          memoizedIsInitialized = 0;
          return false;
        }
      }
      for (int i = 0; i < getAuthorCount(); i++) {
        if (!getAuthor(i).isInitialized()) {
          memoizedIsInitialized = 0;
          return false;
        }
      }
      for (int i = 0; i < getClassifCodeCount(); i++) {
        if (!getClassifCode(i).isInitialized()) {
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
      for (int i = 0; i < title_.size(); i++) {
        output.writeMessage(1, title_.get(i));
      }
      for (int i = 0; i < author_.size(); i++) {
        output.writeMessage(2, author_.get(i));
      }
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeBytes(3, getDoiBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeBytes(4, getJournalBytes());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeBytes(5, getIsbnBytes());
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeBytes(6, getIssnBytes());
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        output.writeBytes(7, getYearBytes());
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        output.writeBytes(8, getIssueBytes());
      }
      if (((bitField0_ & 0x00000040) == 0x00000040)) {
        output.writeBytes(9, getVolumeBytes());
      }
      if (((bitField0_ & 0x00000080) == 0x00000080)) {
        output.writeBytes(10, getPagesBytes());
      }
      for (int i = 0; i < classifCode_.size(); i++) {
        output.writeMessage(11, classifCode_.get(i));
      }
      getUnknownFields().writeTo(output);
    }
    
    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;
    
      size = 0;
      for (int i = 0; i < title_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(1, title_.get(i));
      }
      for (int i = 0; i < author_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(2, author_.get(i));
      }
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(3, getDoiBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(4, getJournalBytes());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(5, getIsbnBytes());
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(6, getIssnBytes());
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(7, getYearBytes());
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(8, getIssueBytes());
      }
      if (((bitField0_ & 0x00000040) == 0x00000040)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(9, getVolumeBytes());
      }
      if (((bitField0_ & 0x00000080) == 0x00000080)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(10, getPagesBytes());
      }
      for (int i = 0; i < classifCode_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(11, classifCode_.get(i));
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
    
    public static pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata parseDelimitedFrom(
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
    public static pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata prototype) {
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
       implements pl.edu.icm.coansys.models.DocumentProtos.BasicMetadataOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return pl.edu.icm.coansys.models.DocumentProtos.internal_static_BasicMetadata_descriptor;
      }
      
      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return pl.edu.icm.coansys.models.DocumentProtos.internal_static_BasicMetadata_fieldAccessorTable;
      }
      
      // Construct using pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }
      
      private Builder(BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getTitleFieldBuilder();
          getAuthorFieldBuilder();
          getClassifCodeFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }
      
      public Builder clear() {
        super.clear();
        if (titleBuilder_ == null) {
          title_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000001);
        } else {
          titleBuilder_.clear();
        }
        if (authorBuilder_ == null) {
          author_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000002);
        } else {
          authorBuilder_.clear();
        }
        doi_ = "";
        bitField0_ = (bitField0_ & ~0x00000004);
        journal_ = "";
        bitField0_ = (bitField0_ & ~0x00000008);
        isbn_ = "";
        bitField0_ = (bitField0_ & ~0x00000010);
        issn_ = "";
        bitField0_ = (bitField0_ & ~0x00000020);
        year_ = "";
        bitField0_ = (bitField0_ & ~0x00000040);
        issue_ = "";
        bitField0_ = (bitField0_ & ~0x00000080);
        volume_ = "";
        bitField0_ = (bitField0_ & ~0x00000100);
        pages_ = "";
        bitField0_ = (bitField0_ & ~0x00000200);
        if (classifCodeBuilder_ == null) {
          classifCode_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000400);
        } else {
          classifCodeBuilder_.clear();
        }
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata.getDescriptor();
      }
      
      public pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata getDefaultInstanceForType() {
        return pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata.getDefaultInstance();
      }
      
      public pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata build() {
        pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata buildPartial() {
        pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata result = new pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (titleBuilder_ == null) {
          if (((bitField0_ & 0x00000001) == 0x00000001)) {
            title_ = java.util.Collections.unmodifiableList(title_);
            bitField0_ = (bitField0_ & ~0x00000001);
          }
          result.title_ = title_;
        } else {
          result.title_ = titleBuilder_.build();
        }
        if (authorBuilder_ == null) {
          if (((bitField0_ & 0x00000002) == 0x00000002)) {
            author_ = java.util.Collections.unmodifiableList(author_);
            bitField0_ = (bitField0_ & ~0x00000002);
          }
          result.author_ = author_;
        } else {
          result.author_ = authorBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000001;
        }
        result.doi_ = doi_;
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000002;
        }
        result.journal_ = journal_;
        if (((from_bitField0_ & 0x00000010) == 0x00000010)) {
          to_bitField0_ |= 0x00000004;
        }
        result.isbn_ = isbn_;
        if (((from_bitField0_ & 0x00000020) == 0x00000020)) {
          to_bitField0_ |= 0x00000008;
        }
        result.issn_ = issn_;
        if (((from_bitField0_ & 0x00000040) == 0x00000040)) {
          to_bitField0_ |= 0x00000010;
        }
        result.year_ = year_;
        if (((from_bitField0_ & 0x00000080) == 0x00000080)) {
          to_bitField0_ |= 0x00000020;
        }
        result.issue_ = issue_;
        if (((from_bitField0_ & 0x00000100) == 0x00000100)) {
          to_bitField0_ |= 0x00000040;
        }
        result.volume_ = volume_;
        if (((from_bitField0_ & 0x00000200) == 0x00000200)) {
          to_bitField0_ |= 0x00000080;
        }
        result.pages_ = pages_;
        if (classifCodeBuilder_ == null) {
          if (((bitField0_ & 0x00000400) == 0x00000400)) {
            classifCode_ = java.util.Collections.unmodifiableList(classifCode_);
            bitField0_ = (bitField0_ & ~0x00000400);
          }
          result.classifCode_ = classifCode_;
        } else {
          result.classifCode_ = classifCodeBuilder_.build();
        }
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata) {
          return mergeFrom((pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata other) {
        if (other == pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata.getDefaultInstance()) return this;
        if (titleBuilder_ == null) {
          if (!other.title_.isEmpty()) {
            if (title_.isEmpty()) {
              title_ = other.title_;
              bitField0_ = (bitField0_ & ~0x00000001);
            } else {
              ensureTitleIsMutable();
              title_.addAll(other.title_);
            }
            onChanged();
          }
        } else {
          if (!other.title_.isEmpty()) {
            if (titleBuilder_.isEmpty()) {
              titleBuilder_.dispose();
              titleBuilder_ = null;
              title_ = other.title_;
              bitField0_ = (bitField0_ & ~0x00000001);
              titleBuilder_ = 
                com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                   getTitleFieldBuilder() : null;
            } else {
              titleBuilder_.addAllMessages(other.title_);
            }
          }
        }
        if (authorBuilder_ == null) {
          if (!other.author_.isEmpty()) {
            if (author_.isEmpty()) {
              author_ = other.author_;
              bitField0_ = (bitField0_ & ~0x00000002);
            } else {
              ensureAuthorIsMutable();
              author_.addAll(other.author_);
            }
            onChanged();
          }
        } else {
          if (!other.author_.isEmpty()) {
            if (authorBuilder_.isEmpty()) {
              authorBuilder_.dispose();
              authorBuilder_ = null;
              author_ = other.author_;
              bitField0_ = (bitField0_ & ~0x00000002);
              authorBuilder_ = 
                com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                   getAuthorFieldBuilder() : null;
            } else {
              authorBuilder_.addAllMessages(other.author_);
            }
          }
        }
        if (other.hasDoi()) {
          setDoi(other.getDoi());
        }
        if (other.hasJournal()) {
          setJournal(other.getJournal());
        }
        if (other.hasIsbn()) {
          setIsbn(other.getIsbn());
        }
        if (other.hasIssn()) {
          setIssn(other.getIssn());
        }
        if (other.hasYear()) {
          setYear(other.getYear());
        }
        if (other.hasIssue()) {
          setIssue(other.getIssue());
        }
        if (other.hasVolume()) {
          setVolume(other.getVolume());
        }
        if (other.hasPages()) {
          setPages(other.getPages());
        }
        if (classifCodeBuilder_ == null) {
          if (!other.classifCode_.isEmpty()) {
            if (classifCode_.isEmpty()) {
              classifCode_ = other.classifCode_;
              bitField0_ = (bitField0_ & ~0x00000400);
            } else {
              ensureClassifCodeIsMutable();
              classifCode_.addAll(other.classifCode_);
            }
            onChanged();
          }
        } else {
          if (!other.classifCode_.isEmpty()) {
            if (classifCodeBuilder_.isEmpty()) {
              classifCodeBuilder_.dispose();
              classifCodeBuilder_ = null;
              classifCode_ = other.classifCode_;
              bitField0_ = (bitField0_ & ~0x00000400);
              classifCodeBuilder_ = 
                com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                   getClassifCodeFieldBuilder() : null;
            } else {
              classifCodeBuilder_.addAllMessages(other.classifCode_);
            }
          }
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
        for (int i = 0; i < getTitleCount(); i++) {
          if (!getTitle(i).isInitialized()) {
            
            return false;
          }
        }
        for (int i = 0; i < getAuthorCount(); i++) {
          if (!getAuthor(i).isInitialized()) {
            
            return false;
          }
        }
        for (int i = 0; i < getClassifCodeCount(); i++) {
          if (!getClassifCode(i).isInitialized()) {
            
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
              pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage.Builder subBuilder = pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage.newBuilder();
              input.readMessage(subBuilder, extensionRegistry);
              addTitle(subBuilder.buildPartial());
              break;
            }
            case 18: {
              pl.edu.icm.coansys.models.DocumentProtos.Author.Builder subBuilder = pl.edu.icm.coansys.models.DocumentProtos.Author.newBuilder();
              input.readMessage(subBuilder, extensionRegistry);
              addAuthor(subBuilder.buildPartial());
              break;
            }
            case 26: {
              bitField0_ |= 0x00000004;
              doi_ = input.readBytes();
              break;
            }
            case 34: {
              bitField0_ |= 0x00000008;
              journal_ = input.readBytes();
              break;
            }
            case 42: {
              bitField0_ |= 0x00000010;
              isbn_ = input.readBytes();
              break;
            }
            case 50: {
              bitField0_ |= 0x00000020;
              issn_ = input.readBytes();
              break;
            }
            case 58: {
              bitField0_ |= 0x00000040;
              year_ = input.readBytes();
              break;
            }
            case 66: {
              bitField0_ |= 0x00000080;
              issue_ = input.readBytes();
              break;
            }
            case 74: {
              bitField0_ |= 0x00000100;
              volume_ = input.readBytes();
              break;
            }
            case 82: {
              bitField0_ |= 0x00000200;
              pages_ = input.readBytes();
              break;
            }
            case 90: {
              pl.edu.icm.coansys.models.DocumentProtos.ClassifCode.Builder subBuilder = pl.edu.icm.coansys.models.DocumentProtos.ClassifCode.newBuilder();
              input.readMessage(subBuilder, extensionRegistry);
              addClassifCode(subBuilder.buildPartial());
              break;
            }
          }
        }
      }
      
      private int bitField0_;
      
      // repeated .TextWithLanguage title = 1;
      private java.util.List<pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage> title_ =
        java.util.Collections.emptyList();
      private void ensureTitleIsMutable() {
        if (!((bitField0_ & 0x00000001) == 0x00000001)) {
          title_ = new java.util.ArrayList<pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage>(title_);
          bitField0_ |= 0x00000001;
         }
      }
      
      private com.google.protobuf.RepeatedFieldBuilder<
          pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage, pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage.Builder, pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguageOrBuilder> titleBuilder_;
      
      public java.util.List<pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage> getTitleList() {
        if (titleBuilder_ == null) {
          return java.util.Collections.unmodifiableList(title_);
        } else {
          return titleBuilder_.getMessageList();
        }
      }
      public int getTitleCount() {
        if (titleBuilder_ == null) {
          return title_.size();
        } else {
          return titleBuilder_.getCount();
        }
      }
      public pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage getTitle(int index) {
        if (titleBuilder_ == null) {
          return title_.get(index);
        } else {
          return titleBuilder_.getMessage(index);
        }
      }
      public Builder setTitle(
          int index, pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage value) {
        if (titleBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureTitleIsMutable();
          title_.set(index, value);
          onChanged();
        } else {
          titleBuilder_.setMessage(index, value);
        }
        return this;
      }
      public Builder setTitle(
          int index, pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage.Builder builderForValue) {
        if (titleBuilder_ == null) {
          ensureTitleIsMutable();
          title_.set(index, builderForValue.build());
          onChanged();
        } else {
          titleBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }
      public Builder addTitle(pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage value) {
        if (titleBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureTitleIsMutable();
          title_.add(value);
          onChanged();
        } else {
          titleBuilder_.addMessage(value);
        }
        return this;
      }
      public Builder addTitle(
          int index, pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage value) {
        if (titleBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureTitleIsMutable();
          title_.add(index, value);
          onChanged();
        } else {
          titleBuilder_.addMessage(index, value);
        }
        return this;
      }
      public Builder addTitle(
          pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage.Builder builderForValue) {
        if (titleBuilder_ == null) {
          ensureTitleIsMutable();
          title_.add(builderForValue.build());
          onChanged();
        } else {
          titleBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }
      public Builder addTitle(
          int index, pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage.Builder builderForValue) {
        if (titleBuilder_ == null) {
          ensureTitleIsMutable();
          title_.add(index, builderForValue.build());
          onChanged();
        } else {
          titleBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }
      public Builder addAllTitle(
          java.lang.Iterable<? extends pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage> values) {
        if (titleBuilder_ == null) {
          ensureTitleIsMutable();
          super.addAll(values, title_);
          onChanged();
        } else {
          titleBuilder_.addAllMessages(values);
        }
        return this;
      }
      public Builder clearTitle() {
        if (titleBuilder_ == null) {
          title_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000001);
          onChanged();
        } else {
          titleBuilder_.clear();
        }
        return this;
      }
      public Builder removeTitle(int index) {
        if (titleBuilder_ == null) {
          ensureTitleIsMutable();
          title_.remove(index);
          onChanged();
        } else {
          titleBuilder_.remove(index);
        }
        return this;
      }
      public pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage.Builder getTitleBuilder(
          int index) {
        return getTitleFieldBuilder().getBuilder(index);
      }
      public pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguageOrBuilder getTitleOrBuilder(
          int index) {
        if (titleBuilder_ == null) {
          return title_.get(index);  } else {
          return titleBuilder_.getMessageOrBuilder(index);
        }
      }
      public java.util.List<? extends pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguageOrBuilder> 
           getTitleOrBuilderList() {
        if (titleBuilder_ != null) {
          return titleBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(title_);
        }
      }
      public pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage.Builder addTitleBuilder() {
        return getTitleFieldBuilder().addBuilder(
            pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage.getDefaultInstance());
      }
      public pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage.Builder addTitleBuilder(
          int index) {
        return getTitleFieldBuilder().addBuilder(
            index, pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage.getDefaultInstance());
      }
      public java.util.List<pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage.Builder> 
           getTitleBuilderList() {
        return getTitleFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilder<
          pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage, pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage.Builder, pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguageOrBuilder> 
          getTitleFieldBuilder() {
        if (titleBuilder_ == null) {
          titleBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
              pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage, pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage.Builder, pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguageOrBuilder>(
                  title_,
                  ((bitField0_ & 0x00000001) == 0x00000001),
                  getParentForChildren(),
                  isClean());
          title_ = null;
        }
        return titleBuilder_;
      }
      
      // repeated .Author author = 2;
      private java.util.List<pl.edu.icm.coansys.models.DocumentProtos.Author> author_ =
        java.util.Collections.emptyList();
      private void ensureAuthorIsMutable() {
        if (!((bitField0_ & 0x00000002) == 0x00000002)) {
          author_ = new java.util.ArrayList<pl.edu.icm.coansys.models.DocumentProtos.Author>(author_);
          bitField0_ |= 0x00000002;
         }
      }
      
      private com.google.protobuf.RepeatedFieldBuilder<
          pl.edu.icm.coansys.models.DocumentProtos.Author, pl.edu.icm.coansys.models.DocumentProtos.Author.Builder, pl.edu.icm.coansys.models.DocumentProtos.AuthorOrBuilder> authorBuilder_;
      
      public java.util.List<pl.edu.icm.coansys.models.DocumentProtos.Author> getAuthorList() {
        if (authorBuilder_ == null) {
          return java.util.Collections.unmodifiableList(author_);
        } else {
          return authorBuilder_.getMessageList();
        }
      }
      public int getAuthorCount() {
        if (authorBuilder_ == null) {
          return author_.size();
        } else {
          return authorBuilder_.getCount();
        }
      }
      public pl.edu.icm.coansys.models.DocumentProtos.Author getAuthor(int index) {
        if (authorBuilder_ == null) {
          return author_.get(index);
        } else {
          return authorBuilder_.getMessage(index);
        }
      }
      public Builder setAuthor(
          int index, pl.edu.icm.coansys.models.DocumentProtos.Author value) {
        if (authorBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureAuthorIsMutable();
          author_.set(index, value);
          onChanged();
        } else {
          authorBuilder_.setMessage(index, value);
        }
        return this;
      }
      public Builder setAuthor(
          int index, pl.edu.icm.coansys.models.DocumentProtos.Author.Builder builderForValue) {
        if (authorBuilder_ == null) {
          ensureAuthorIsMutable();
          author_.set(index, builderForValue.build());
          onChanged();
        } else {
          authorBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }
      public Builder addAuthor(pl.edu.icm.coansys.models.DocumentProtos.Author value) {
        if (authorBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureAuthorIsMutable();
          author_.add(value);
          onChanged();
        } else {
          authorBuilder_.addMessage(value);
        }
        return this;
      }
      public Builder addAuthor(
          int index, pl.edu.icm.coansys.models.DocumentProtos.Author value) {
        if (authorBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureAuthorIsMutable();
          author_.add(index, value);
          onChanged();
        } else {
          authorBuilder_.addMessage(index, value);
        }
        return this;
      }
      public Builder addAuthor(
          pl.edu.icm.coansys.models.DocumentProtos.Author.Builder builderForValue) {
        if (authorBuilder_ == null) {
          ensureAuthorIsMutable();
          author_.add(builderForValue.build());
          onChanged();
        } else {
          authorBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }
      public Builder addAuthor(
          int index, pl.edu.icm.coansys.models.DocumentProtos.Author.Builder builderForValue) {
        if (authorBuilder_ == null) {
          ensureAuthorIsMutable();
          author_.add(index, builderForValue.build());
          onChanged();
        } else {
          authorBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }
      public Builder addAllAuthor(
          java.lang.Iterable<? extends pl.edu.icm.coansys.models.DocumentProtos.Author> values) {
        if (authorBuilder_ == null) {
          ensureAuthorIsMutable();
          super.addAll(values, author_);
          onChanged();
        } else {
          authorBuilder_.addAllMessages(values);
        }
        return this;
      }
      public Builder clearAuthor() {
        if (authorBuilder_ == null) {
          author_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000002);
          onChanged();
        } else {
          authorBuilder_.clear();
        }
        return this;
      }
      public Builder removeAuthor(int index) {
        if (authorBuilder_ == null) {
          ensureAuthorIsMutable();
          author_.remove(index);
          onChanged();
        } else {
          authorBuilder_.remove(index);
        }
        return this;
      }
      public pl.edu.icm.coansys.models.DocumentProtos.Author.Builder getAuthorBuilder(
          int index) {
        return getAuthorFieldBuilder().getBuilder(index);
      }
      public pl.edu.icm.coansys.models.DocumentProtos.AuthorOrBuilder getAuthorOrBuilder(
          int index) {
        if (authorBuilder_ == null) {
          return author_.get(index);  } else {
          return authorBuilder_.getMessageOrBuilder(index);
        }
      }
      public java.util.List<? extends pl.edu.icm.coansys.models.DocumentProtos.AuthorOrBuilder> 
           getAuthorOrBuilderList() {
        if (authorBuilder_ != null) {
          return authorBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(author_);
        }
      }
      public pl.edu.icm.coansys.models.DocumentProtos.Author.Builder addAuthorBuilder() {
        return getAuthorFieldBuilder().addBuilder(
            pl.edu.icm.coansys.models.DocumentProtos.Author.getDefaultInstance());
      }
      public pl.edu.icm.coansys.models.DocumentProtos.Author.Builder addAuthorBuilder(
          int index) {
        return getAuthorFieldBuilder().addBuilder(
            index, pl.edu.icm.coansys.models.DocumentProtos.Author.getDefaultInstance());
      }
      public java.util.List<pl.edu.icm.coansys.models.DocumentProtos.Author.Builder> 
           getAuthorBuilderList() {
        return getAuthorFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilder<
          pl.edu.icm.coansys.models.DocumentProtos.Author, pl.edu.icm.coansys.models.DocumentProtos.Author.Builder, pl.edu.icm.coansys.models.DocumentProtos.AuthorOrBuilder> 
          getAuthorFieldBuilder() {
        if (authorBuilder_ == null) {
          authorBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
              pl.edu.icm.coansys.models.DocumentProtos.Author, pl.edu.icm.coansys.models.DocumentProtos.Author.Builder, pl.edu.icm.coansys.models.DocumentProtos.AuthorOrBuilder>(
                  author_,
                  ((bitField0_ & 0x00000002) == 0x00000002),
                  getParentForChildren(),
                  isClean());
          author_ = null;
        }
        return authorBuilder_;
      }
      
      // optional string doi = 3;
      private java.lang.Object doi_ = "";
      public boolean hasDoi() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      public String getDoi() {
        java.lang.Object ref = doi_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          doi_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setDoi(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000004;
        doi_ = value;
        onChanged();
        return this;
      }
      public Builder clearDoi() {
        bitField0_ = (bitField0_ & ~0x00000004);
        doi_ = getDefaultInstance().getDoi();
        onChanged();
        return this;
      }
      void setDoi(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000004;
        doi_ = value;
        onChanged();
      }
      
      // optional string journal = 4;
      private java.lang.Object journal_ = "";
      public boolean hasJournal() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      public String getJournal() {
        java.lang.Object ref = journal_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          journal_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setJournal(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000008;
        journal_ = value;
        onChanged();
        return this;
      }
      public Builder clearJournal() {
        bitField0_ = (bitField0_ & ~0x00000008);
        journal_ = getDefaultInstance().getJournal();
        onChanged();
        return this;
      }
      void setJournal(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000008;
        journal_ = value;
        onChanged();
      }
      
      // optional string isbn = 5;
      private java.lang.Object isbn_ = "";
      public boolean hasIsbn() {
        return ((bitField0_ & 0x00000010) == 0x00000010);
      }
      public String getIsbn() {
        java.lang.Object ref = isbn_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          isbn_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setIsbn(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000010;
        isbn_ = value;
        onChanged();
        return this;
      }
      public Builder clearIsbn() {
        bitField0_ = (bitField0_ & ~0x00000010);
        isbn_ = getDefaultInstance().getIsbn();
        onChanged();
        return this;
      }
      void setIsbn(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000010;
        isbn_ = value;
        onChanged();
      }
      
      // optional string issn = 6;
      private java.lang.Object issn_ = "";
      public boolean hasIssn() {
        return ((bitField0_ & 0x00000020) == 0x00000020);
      }
      public String getIssn() {
        java.lang.Object ref = issn_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          issn_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setIssn(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000020;
        issn_ = value;
        onChanged();
        return this;
      }
      public Builder clearIssn() {
        bitField0_ = (bitField0_ & ~0x00000020);
        issn_ = getDefaultInstance().getIssn();
        onChanged();
        return this;
      }
      void setIssn(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000020;
        issn_ = value;
        onChanged();
      }
      
      // optional string year = 7;
      private java.lang.Object year_ = "";
      public boolean hasYear() {
        return ((bitField0_ & 0x00000040) == 0x00000040);
      }
      public String getYear() {
        java.lang.Object ref = year_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          year_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setYear(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000040;
        year_ = value;
        onChanged();
        return this;
      }
      public Builder clearYear() {
        bitField0_ = (bitField0_ & ~0x00000040);
        year_ = getDefaultInstance().getYear();
        onChanged();
        return this;
      }
      void setYear(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000040;
        year_ = value;
        onChanged();
      }
      
      // optional string issue = 8;
      private java.lang.Object issue_ = "";
      public boolean hasIssue() {
        return ((bitField0_ & 0x00000080) == 0x00000080);
      }
      public String getIssue() {
        java.lang.Object ref = issue_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          issue_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setIssue(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000080;
        issue_ = value;
        onChanged();
        return this;
      }
      public Builder clearIssue() {
        bitField0_ = (bitField0_ & ~0x00000080);
        issue_ = getDefaultInstance().getIssue();
        onChanged();
        return this;
      }
      void setIssue(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000080;
        issue_ = value;
        onChanged();
      }
      
      // optional string volume = 9;
      private java.lang.Object volume_ = "";
      public boolean hasVolume() {
        return ((bitField0_ & 0x00000100) == 0x00000100);
      }
      public String getVolume() {
        java.lang.Object ref = volume_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          volume_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setVolume(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000100;
        volume_ = value;
        onChanged();
        return this;
      }
      public Builder clearVolume() {
        bitField0_ = (bitField0_ & ~0x00000100);
        volume_ = getDefaultInstance().getVolume();
        onChanged();
        return this;
      }
      void setVolume(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000100;
        volume_ = value;
        onChanged();
      }
      
      // optional string pages = 10;
      private java.lang.Object pages_ = "";
      public boolean hasPages() {
        return ((bitField0_ & 0x00000200) == 0x00000200);
      }
      public String getPages() {
        java.lang.Object ref = pages_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          pages_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setPages(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000200;
        pages_ = value;
        onChanged();
        return this;
      }
      public Builder clearPages() {
        bitField0_ = (bitField0_ & ~0x00000200);
        pages_ = getDefaultInstance().getPages();
        onChanged();
        return this;
      }
      void setPages(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000200;
        pages_ = value;
        onChanged();
      }
      
      // repeated .ClassifCode classifCode = 11;
      private java.util.List<pl.edu.icm.coansys.models.DocumentProtos.ClassifCode> classifCode_ =
        java.util.Collections.emptyList();
      private void ensureClassifCodeIsMutable() {
        if (!((bitField0_ & 0x00000400) == 0x00000400)) {
          classifCode_ = new java.util.ArrayList<pl.edu.icm.coansys.models.DocumentProtos.ClassifCode>(classifCode_);
          bitField0_ |= 0x00000400;
         }
      }
      
      private com.google.protobuf.RepeatedFieldBuilder<
          pl.edu.icm.coansys.models.DocumentProtos.ClassifCode, pl.edu.icm.coansys.models.DocumentProtos.ClassifCode.Builder, pl.edu.icm.coansys.models.DocumentProtos.ClassifCodeOrBuilder> classifCodeBuilder_;
      
      public java.util.List<pl.edu.icm.coansys.models.DocumentProtos.ClassifCode> getClassifCodeList() {
        if (classifCodeBuilder_ == null) {
          return java.util.Collections.unmodifiableList(classifCode_);
        } else {
          return classifCodeBuilder_.getMessageList();
        }
      }
      public int getClassifCodeCount() {
        if (classifCodeBuilder_ == null) {
          return classifCode_.size();
        } else {
          return classifCodeBuilder_.getCount();
        }
      }
      public pl.edu.icm.coansys.models.DocumentProtos.ClassifCode getClassifCode(int index) {
        if (classifCodeBuilder_ == null) {
          return classifCode_.get(index);
        } else {
          return classifCodeBuilder_.getMessage(index);
        }
      }
      public Builder setClassifCode(
          int index, pl.edu.icm.coansys.models.DocumentProtos.ClassifCode value) {
        if (classifCodeBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureClassifCodeIsMutable();
          classifCode_.set(index, value);
          onChanged();
        } else {
          classifCodeBuilder_.setMessage(index, value);
        }
        return this;
      }
      public Builder setClassifCode(
          int index, pl.edu.icm.coansys.models.DocumentProtos.ClassifCode.Builder builderForValue) {
        if (classifCodeBuilder_ == null) {
          ensureClassifCodeIsMutable();
          classifCode_.set(index, builderForValue.build());
          onChanged();
        } else {
          classifCodeBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }
      public Builder addClassifCode(pl.edu.icm.coansys.models.DocumentProtos.ClassifCode value) {
        if (classifCodeBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureClassifCodeIsMutable();
          classifCode_.add(value);
          onChanged();
        } else {
          classifCodeBuilder_.addMessage(value);
        }
        return this;
      }
      public Builder addClassifCode(
          int index, pl.edu.icm.coansys.models.DocumentProtos.ClassifCode value) {
        if (classifCodeBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureClassifCodeIsMutable();
          classifCode_.add(index, value);
          onChanged();
        } else {
          classifCodeBuilder_.addMessage(index, value);
        }
        return this;
      }
      public Builder addClassifCode(
          pl.edu.icm.coansys.models.DocumentProtos.ClassifCode.Builder builderForValue) {
        if (classifCodeBuilder_ == null) {
          ensureClassifCodeIsMutable();
          classifCode_.add(builderForValue.build());
          onChanged();
        } else {
          classifCodeBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }
      public Builder addClassifCode(
          int index, pl.edu.icm.coansys.models.DocumentProtos.ClassifCode.Builder builderForValue) {
        if (classifCodeBuilder_ == null) {
          ensureClassifCodeIsMutable();
          classifCode_.add(index, builderForValue.build());
          onChanged();
        } else {
          classifCodeBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }
      public Builder addAllClassifCode(
          java.lang.Iterable<? extends pl.edu.icm.coansys.models.DocumentProtos.ClassifCode> values) {
        if (classifCodeBuilder_ == null) {
          ensureClassifCodeIsMutable();
          super.addAll(values, classifCode_);
          onChanged();
        } else {
          classifCodeBuilder_.addAllMessages(values);
        }
        return this;
      }
      public Builder clearClassifCode() {
        if (classifCodeBuilder_ == null) {
          classifCode_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000400);
          onChanged();
        } else {
          classifCodeBuilder_.clear();
        }
        return this;
      }
      public Builder removeClassifCode(int index) {
        if (classifCodeBuilder_ == null) {
          ensureClassifCodeIsMutable();
          classifCode_.remove(index);
          onChanged();
        } else {
          classifCodeBuilder_.remove(index);
        }
        return this;
      }
      public pl.edu.icm.coansys.models.DocumentProtos.ClassifCode.Builder getClassifCodeBuilder(
          int index) {
        return getClassifCodeFieldBuilder().getBuilder(index);
      }
      public pl.edu.icm.coansys.models.DocumentProtos.ClassifCodeOrBuilder getClassifCodeOrBuilder(
          int index) {
        if (classifCodeBuilder_ == null) {
          return classifCode_.get(index);  } else {
          return classifCodeBuilder_.getMessageOrBuilder(index);
        }
      }
      public java.util.List<? extends pl.edu.icm.coansys.models.DocumentProtos.ClassifCodeOrBuilder> 
           getClassifCodeOrBuilderList() {
        if (classifCodeBuilder_ != null) {
          return classifCodeBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(classifCode_);
        }
      }
      public pl.edu.icm.coansys.models.DocumentProtos.ClassifCode.Builder addClassifCodeBuilder() {
        return getClassifCodeFieldBuilder().addBuilder(
            pl.edu.icm.coansys.models.DocumentProtos.ClassifCode.getDefaultInstance());
      }
      public pl.edu.icm.coansys.models.DocumentProtos.ClassifCode.Builder addClassifCodeBuilder(
          int index) {
        return getClassifCodeFieldBuilder().addBuilder(
            index, pl.edu.icm.coansys.models.DocumentProtos.ClassifCode.getDefaultInstance());
      }
      public java.util.List<pl.edu.icm.coansys.models.DocumentProtos.ClassifCode.Builder> 
           getClassifCodeBuilderList() {
        return getClassifCodeFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilder<
          pl.edu.icm.coansys.models.DocumentProtos.ClassifCode, pl.edu.icm.coansys.models.DocumentProtos.ClassifCode.Builder, pl.edu.icm.coansys.models.DocumentProtos.ClassifCodeOrBuilder> 
          getClassifCodeFieldBuilder() {
        if (classifCodeBuilder_ == null) {
          classifCodeBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
              pl.edu.icm.coansys.models.DocumentProtos.ClassifCode, pl.edu.icm.coansys.models.DocumentProtos.ClassifCode.Builder, pl.edu.icm.coansys.models.DocumentProtos.ClassifCodeOrBuilder>(
                  classifCode_,
                  ((bitField0_ & 0x00000400) == 0x00000400),
                  getParentForChildren(),
                  isClean());
          classifCode_ = null;
        }
        return classifCodeBuilder_;
      }
      
      // @@protoc_insertion_point(builder_scope:BasicMetadata)
    }
    
    static {
      defaultInstance = new BasicMetadata(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:BasicMetadata)
  }
  
  public interface KeywordsListOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // optional string type = 1;
    boolean hasType();
    String getType();
    
    // optional string language = 2;
    boolean hasLanguage();
    String getLanguage();
    
    // repeated string keywords = 3;
    java.util.List<String> getKeywordsList();
    int getKeywordsCount();
    String getKeywords(int index);
    
    // optional string comment = 4;
    boolean hasComment();
    String getComment();
    
    // optional .ProvenanceInfo provenance = 5;
    boolean hasProvenance();
    pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo getProvenance();
    pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfoOrBuilder getProvenanceOrBuilder();
  }
  public static final class KeywordsList extends
      com.google.protobuf.GeneratedMessage
      implements KeywordsListOrBuilder {
    // Use KeywordsList.newBuilder() to construct.
    private KeywordsList(Builder builder) {
      super(builder);
    }
    private KeywordsList(boolean noInit) {}
    
    private static final KeywordsList defaultInstance;
    public static KeywordsList getDefaultInstance() {
      return defaultInstance;
    }
    
    public KeywordsList getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return pl.edu.icm.coansys.models.DocumentProtos.internal_static_KeywordsList_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return pl.edu.icm.coansys.models.DocumentProtos.internal_static_KeywordsList_fieldAccessorTable;
    }
    
    private int bitField0_;
    // optional string type = 1;
    public static final int TYPE_FIELD_NUMBER = 1;
    private java.lang.Object type_;
    public boolean hasType() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    public String getType() {
      java.lang.Object ref = type_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          type_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getTypeBytes() {
      java.lang.Object ref = type_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        type_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // optional string language = 2;
    public static final int LANGUAGE_FIELD_NUMBER = 2;
    private java.lang.Object language_;
    public boolean hasLanguage() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    public String getLanguage() {
      java.lang.Object ref = language_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          language_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getLanguageBytes() {
      java.lang.Object ref = language_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        language_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // repeated string keywords = 3;
    public static final int KEYWORDS_FIELD_NUMBER = 3;
    private com.google.protobuf.LazyStringList keywords_;
    public java.util.List<String>
        getKeywordsList() {
      return keywords_;
    }
    public int getKeywordsCount() {
      return keywords_.size();
    }
    public String getKeywords(int index) {
      return keywords_.get(index);
    }
    
    // optional string comment = 4;
    public static final int COMMENT_FIELD_NUMBER = 4;
    private java.lang.Object comment_;
    public boolean hasComment() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    public String getComment() {
      java.lang.Object ref = comment_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          comment_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getCommentBytes() {
      java.lang.Object ref = comment_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        comment_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // optional .ProvenanceInfo provenance = 5;
    public static final int PROVENANCE_FIELD_NUMBER = 5;
    private pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo provenance_;
    public boolean hasProvenance() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    public pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo getProvenance() {
      return provenance_;
    }
    public pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfoOrBuilder getProvenanceOrBuilder() {
      return provenance_;
    }
    
    private void initFields() {
      type_ = "";
      language_ = "";
      keywords_ = com.google.protobuf.LazyStringArrayList.EMPTY;
      comment_ = "";
      provenance_ = pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.getDefaultInstance();
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
      if (hasProvenance()) {
        if (!getProvenance().isInitialized()) {
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
        output.writeBytes(1, getTypeBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeBytes(2, getLanguageBytes());
      }
      for (int i = 0; i < keywords_.size(); i++) {
        output.writeBytes(3, keywords_.getByteString(i));
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeBytes(4, getCommentBytes());
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeMessage(5, provenance_);
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
          .computeBytesSize(1, getTypeBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(2, getLanguageBytes());
      }
      {
        int dataSize = 0;
        for (int i = 0; i < keywords_.size(); i++) {
          dataSize += com.google.protobuf.CodedOutputStream
            .computeBytesSizeNoTag(keywords_.getByteString(i));
        }
        size += dataSize;
        size += 1 * getKeywordsList().size();
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(4, getCommentBytes());
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(5, provenance_);
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
    
    public static pl.edu.icm.coansys.models.DocumentProtos.KeywordsList parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.KeywordsList parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.KeywordsList parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.KeywordsList parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.KeywordsList parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.KeywordsList parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.KeywordsList parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.KeywordsList parseDelimitedFrom(
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
    public static pl.edu.icm.coansys.models.DocumentProtos.KeywordsList parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.KeywordsList parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(pl.edu.icm.coansys.models.DocumentProtos.KeywordsList prototype) {
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
       implements pl.edu.icm.coansys.models.DocumentProtos.KeywordsListOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return pl.edu.icm.coansys.models.DocumentProtos.internal_static_KeywordsList_descriptor;
      }
      
      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return pl.edu.icm.coansys.models.DocumentProtos.internal_static_KeywordsList_fieldAccessorTable;
      }
      
      // Construct using pl.edu.icm.coansys.models.DocumentProtos.KeywordsList.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }
      
      private Builder(BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getProvenanceFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }
      
      public Builder clear() {
        super.clear();
        type_ = "";
        bitField0_ = (bitField0_ & ~0x00000001);
        language_ = "";
        bitField0_ = (bitField0_ & ~0x00000002);
        keywords_ = com.google.protobuf.LazyStringArrayList.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000004);
        comment_ = "";
        bitField0_ = (bitField0_ & ~0x00000008);
        if (provenanceBuilder_ == null) {
          provenance_ = pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.getDefaultInstance();
        } else {
          provenanceBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000010);
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return pl.edu.icm.coansys.models.DocumentProtos.KeywordsList.getDescriptor();
      }
      
      public pl.edu.icm.coansys.models.DocumentProtos.KeywordsList getDefaultInstanceForType() {
        return pl.edu.icm.coansys.models.DocumentProtos.KeywordsList.getDefaultInstance();
      }
      
      public pl.edu.icm.coansys.models.DocumentProtos.KeywordsList build() {
        pl.edu.icm.coansys.models.DocumentProtos.KeywordsList result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private pl.edu.icm.coansys.models.DocumentProtos.KeywordsList buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        pl.edu.icm.coansys.models.DocumentProtos.KeywordsList result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public pl.edu.icm.coansys.models.DocumentProtos.KeywordsList buildPartial() {
        pl.edu.icm.coansys.models.DocumentProtos.KeywordsList result = new pl.edu.icm.coansys.models.DocumentProtos.KeywordsList(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.type_ = type_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.language_ = language_;
        if (((bitField0_ & 0x00000004) == 0x00000004)) {
          keywords_ = new com.google.protobuf.UnmodifiableLazyStringList(
              keywords_);
          bitField0_ = (bitField0_ & ~0x00000004);
        }
        result.keywords_ = keywords_;
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000004;
        }
        result.comment_ = comment_;
        if (((from_bitField0_ & 0x00000010) == 0x00000010)) {
          to_bitField0_ |= 0x00000008;
        }
        if (provenanceBuilder_ == null) {
          result.provenance_ = provenance_;
        } else {
          result.provenance_ = provenanceBuilder_.build();
        }
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof pl.edu.icm.coansys.models.DocumentProtos.KeywordsList) {
          return mergeFrom((pl.edu.icm.coansys.models.DocumentProtos.KeywordsList)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(pl.edu.icm.coansys.models.DocumentProtos.KeywordsList other) {
        if (other == pl.edu.icm.coansys.models.DocumentProtos.KeywordsList.getDefaultInstance()) return this;
        if (other.hasType()) {
          setType(other.getType());
        }
        if (other.hasLanguage()) {
          setLanguage(other.getLanguage());
        }
        if (!other.keywords_.isEmpty()) {
          if (keywords_.isEmpty()) {
            keywords_ = other.keywords_;
            bitField0_ = (bitField0_ & ~0x00000004);
          } else {
            ensureKeywordsIsMutable();
            keywords_.addAll(other.keywords_);
          }
          onChanged();
        }
        if (other.hasComment()) {
          setComment(other.getComment());
        }
        if (other.hasProvenance()) {
          mergeProvenance(other.getProvenance());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
        if (hasProvenance()) {
          if (!getProvenance().isInitialized()) {
            
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
              type_ = input.readBytes();
              break;
            }
            case 18: {
              bitField0_ |= 0x00000002;
              language_ = input.readBytes();
              break;
            }
            case 26: {
              ensureKeywordsIsMutable();
              keywords_.add(input.readBytes());
              break;
            }
            case 34: {
              bitField0_ |= 0x00000008;
              comment_ = input.readBytes();
              break;
            }
            case 42: {
              pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.Builder subBuilder = pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.newBuilder();
              if (hasProvenance()) {
                subBuilder.mergeFrom(getProvenance());
              }
              input.readMessage(subBuilder, extensionRegistry);
              setProvenance(subBuilder.buildPartial());
              break;
            }
          }
        }
      }
      
      private int bitField0_;
      
      // optional string type = 1;
      private java.lang.Object type_ = "";
      public boolean hasType() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      public String getType() {
        java.lang.Object ref = type_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          type_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setType(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        type_ = value;
        onChanged();
        return this;
      }
      public Builder clearType() {
        bitField0_ = (bitField0_ & ~0x00000001);
        type_ = getDefaultInstance().getType();
        onChanged();
        return this;
      }
      void setType(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000001;
        type_ = value;
        onChanged();
      }
      
      // optional string language = 2;
      private java.lang.Object language_ = "";
      public boolean hasLanguage() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      public String getLanguage() {
        java.lang.Object ref = language_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          language_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setLanguage(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        language_ = value;
        onChanged();
        return this;
      }
      public Builder clearLanguage() {
        bitField0_ = (bitField0_ & ~0x00000002);
        language_ = getDefaultInstance().getLanguage();
        onChanged();
        return this;
      }
      void setLanguage(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000002;
        language_ = value;
        onChanged();
      }
      
      // repeated string keywords = 3;
      private com.google.protobuf.LazyStringList keywords_ = com.google.protobuf.LazyStringArrayList.EMPTY;
      private void ensureKeywordsIsMutable() {
        if (!((bitField0_ & 0x00000004) == 0x00000004)) {
          keywords_ = new com.google.protobuf.LazyStringArrayList(keywords_);
          bitField0_ |= 0x00000004;
         }
      }
      public java.util.List<String>
          getKeywordsList() {
        return java.util.Collections.unmodifiableList(keywords_);
      }
      public int getKeywordsCount() {
        return keywords_.size();
      }
      public String getKeywords(int index) {
        return keywords_.get(index);
      }
      public Builder setKeywords(
          int index, String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  ensureKeywordsIsMutable();
        keywords_.set(index, value);
        onChanged();
        return this;
      }
      public Builder addKeywords(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  ensureKeywordsIsMutable();
        keywords_.add(value);
        onChanged();
        return this;
      }
      public Builder addAllKeywords(
          java.lang.Iterable<String> values) {
        ensureKeywordsIsMutable();
        super.addAll(values, keywords_);
        onChanged();
        return this;
      }
      public Builder clearKeywords() {
        keywords_ = com.google.protobuf.LazyStringArrayList.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000004);
        onChanged();
        return this;
      }
      void addKeywords(com.google.protobuf.ByteString value) {
        ensureKeywordsIsMutable();
        keywords_.add(value);
        onChanged();
      }
      
      // optional string comment = 4;
      private java.lang.Object comment_ = "";
      public boolean hasComment() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      public String getComment() {
        java.lang.Object ref = comment_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          comment_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setComment(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000008;
        comment_ = value;
        onChanged();
        return this;
      }
      public Builder clearComment() {
        bitField0_ = (bitField0_ & ~0x00000008);
        comment_ = getDefaultInstance().getComment();
        onChanged();
        return this;
      }
      void setComment(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000008;
        comment_ = value;
        onChanged();
      }
      
      // optional .ProvenanceInfo provenance = 5;
      private pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo provenance_ = pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo, pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.Builder, pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfoOrBuilder> provenanceBuilder_;
      public boolean hasProvenance() {
        return ((bitField0_ & 0x00000010) == 0x00000010);
      }
      public pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo getProvenance() {
        if (provenanceBuilder_ == null) {
          return provenance_;
        } else {
          return provenanceBuilder_.getMessage();
        }
      }
      public Builder setProvenance(pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo value) {
        if (provenanceBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          provenance_ = value;
          onChanged();
        } else {
          provenanceBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000010;
        return this;
      }
      public Builder setProvenance(
          pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.Builder builderForValue) {
        if (provenanceBuilder_ == null) {
          provenance_ = builderForValue.build();
          onChanged();
        } else {
          provenanceBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000010;
        return this;
      }
      public Builder mergeProvenance(pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo value) {
        if (provenanceBuilder_ == null) {
          if (((bitField0_ & 0x00000010) == 0x00000010) &&
              provenance_ != pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.getDefaultInstance()) {
            provenance_ =
              pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.newBuilder(provenance_).mergeFrom(value).buildPartial();
          } else {
            provenance_ = value;
          }
          onChanged();
        } else {
          provenanceBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000010;
        return this;
      }
      public Builder clearProvenance() {
        if (provenanceBuilder_ == null) {
          provenance_ = pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.getDefaultInstance();
          onChanged();
        } else {
          provenanceBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000010);
        return this;
      }
      public pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.Builder getProvenanceBuilder() {
        bitField0_ |= 0x00000010;
        onChanged();
        return getProvenanceFieldBuilder().getBuilder();
      }
      public pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfoOrBuilder getProvenanceOrBuilder() {
        if (provenanceBuilder_ != null) {
          return provenanceBuilder_.getMessageOrBuilder();
        } else {
          return provenance_;
        }
      }
      private com.google.protobuf.SingleFieldBuilder<
          pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo, pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.Builder, pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfoOrBuilder> 
          getProvenanceFieldBuilder() {
        if (provenanceBuilder_ == null) {
          provenanceBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo, pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.Builder, pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfoOrBuilder>(
                  provenance_,
                  getParentForChildren(),
                  isClean());
          provenance_ = null;
        }
        return provenanceBuilder_;
      }
      
      // @@protoc_insertion_point(builder_scope:KeywordsList)
    }
    
    static {
      defaultInstance = new KeywordsList(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:KeywordsList)
  }
  
  public interface DocumentMetadataOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // required string key = 1;
    boolean hasKey();
    String getKey();
    
    // required .BasicMetadata basicMetadata = 2;
    boolean hasBasicMetadata();
    pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata getBasicMetadata();
    pl.edu.icm.coansys.models.DocumentProtos.BasicMetadataOrBuilder getBasicMetadataOrBuilder();
    
    // repeated .TextWithLanguage documentAbstract = 3;
    java.util.List<pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage> 
        getDocumentAbstractList();
    pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage getDocumentAbstract(int index);
    int getDocumentAbstractCount();
    java.util.List<? extends pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguageOrBuilder> 
        getDocumentAbstractOrBuilderList();
    pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguageOrBuilder getDocumentAbstractOrBuilder(
        int index);
    
    // repeated .KeywordsList keywords = 10;
    java.util.List<pl.edu.icm.coansys.models.DocumentProtos.KeywordsList> 
        getKeywordsList();
    pl.edu.icm.coansys.models.DocumentProtos.KeywordsList getKeywords(int index);
    int getKeywordsCount();
    java.util.List<? extends pl.edu.icm.coansys.models.DocumentProtos.KeywordsListOrBuilder> 
        getKeywordsOrBuilderList();
    pl.edu.icm.coansys.models.DocumentProtos.KeywordsListOrBuilder getKeywordsOrBuilder(
        int index);
    
    // repeated .KeyValue extId = 5;
    java.util.List<pl.edu.icm.coansys.models.DocumentProtos.KeyValue> 
        getExtIdList();
    pl.edu.icm.coansys.models.DocumentProtos.KeyValue getExtId(int index);
    int getExtIdCount();
    java.util.List<? extends pl.edu.icm.coansys.models.DocumentProtos.KeyValueOrBuilder> 
        getExtIdOrBuilderList();
    pl.edu.icm.coansys.models.DocumentProtos.KeyValueOrBuilder getExtIdOrBuilder(
        int index);
    
    // repeated .KeyValue auxiliarInfo = 6;
    java.util.List<pl.edu.icm.coansys.models.DocumentProtos.KeyValue> 
        getAuxiliarInfoList();
    pl.edu.icm.coansys.models.DocumentProtos.KeyValue getAuxiliarInfo(int index);
    int getAuxiliarInfoCount();
    java.util.List<? extends pl.edu.icm.coansys.models.DocumentProtos.KeyValueOrBuilder> 
        getAuxiliarInfoOrBuilderList();
    pl.edu.icm.coansys.models.DocumentProtos.KeyValueOrBuilder getAuxiliarInfoOrBuilder(
        int index);
    
    // repeated .Affiliation affiliations = 12;
    java.util.List<pl.edu.icm.coansys.models.DocumentProtos.Affiliation> 
        getAffiliationsList();
    pl.edu.icm.coansys.models.DocumentProtos.Affiliation getAffiliations(int index);
    int getAffiliationsCount();
    java.util.List<? extends pl.edu.icm.coansys.models.DocumentProtos.AffiliationOrBuilder> 
        getAffiliationsOrBuilderList();
    pl.edu.icm.coansys.models.DocumentProtos.AffiliationOrBuilder getAffiliationsOrBuilder(
        int index);
    
    // repeated .ReferenceMetadata reference = 7;
    java.util.List<pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata> 
        getReferenceList();
    pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata getReference(int index);
    int getReferenceCount();
    java.util.List<? extends pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadataOrBuilder> 
        getReferenceOrBuilderList();
    pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadataOrBuilder getReferenceOrBuilder(
        int index);
    
    // repeated string collection = 8;
    java.util.List<String> getCollectionList();
    int getCollectionCount();
    String getCollection(int index);
    
    // optional string sourcePath = 9;
    boolean hasSourcePath();
    String getSourcePath();
    
    // repeated string origKey = 11;
    java.util.List<String> getOrigKeyList();
    int getOrigKeyCount();
    String getOrigKey(int index);
  }
  public static final class DocumentMetadata extends
      com.google.protobuf.GeneratedMessage
      implements DocumentMetadataOrBuilder {
    // Use DocumentMetadata.newBuilder() to construct.
    private DocumentMetadata(Builder builder) {
      super(builder);
    }
    private DocumentMetadata(boolean noInit) {}
    
    private static final DocumentMetadata defaultInstance;
    public static DocumentMetadata getDefaultInstance() {
      return defaultInstance;
    }
    
    public DocumentMetadata getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return pl.edu.icm.coansys.models.DocumentProtos.internal_static_DocumentMetadata_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return pl.edu.icm.coansys.models.DocumentProtos.internal_static_DocumentMetadata_fieldAccessorTable;
    }
    
    private int bitField0_;
    // required string key = 1;
    public static final int KEY_FIELD_NUMBER = 1;
    private java.lang.Object key_;
    public boolean hasKey() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    public String getKey() {
      java.lang.Object ref = key_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          key_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getKeyBytes() {
      java.lang.Object ref = key_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        key_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // required .BasicMetadata basicMetadata = 2;
    public static final int BASICMETADATA_FIELD_NUMBER = 2;
    private pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata basicMetadata_;
    public boolean hasBasicMetadata() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    public pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata getBasicMetadata() {
      return basicMetadata_;
    }
    public pl.edu.icm.coansys.models.DocumentProtos.BasicMetadataOrBuilder getBasicMetadataOrBuilder() {
      return basicMetadata_;
    }
    
    // repeated .TextWithLanguage documentAbstract = 3;
    public static final int DOCUMENTABSTRACT_FIELD_NUMBER = 3;
    private java.util.List<pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage> documentAbstract_;
    public java.util.List<pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage> getDocumentAbstractList() {
      return documentAbstract_;
    }
    public java.util.List<? extends pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguageOrBuilder> 
        getDocumentAbstractOrBuilderList() {
      return documentAbstract_;
    }
    public int getDocumentAbstractCount() {
      return documentAbstract_.size();
    }
    public pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage getDocumentAbstract(int index) {
      return documentAbstract_.get(index);
    }
    public pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguageOrBuilder getDocumentAbstractOrBuilder(
        int index) {
      return documentAbstract_.get(index);
    }
    
    // repeated .KeywordsList keywords = 10;
    public static final int KEYWORDS_FIELD_NUMBER = 10;
    private java.util.List<pl.edu.icm.coansys.models.DocumentProtos.KeywordsList> keywords_;
    public java.util.List<pl.edu.icm.coansys.models.DocumentProtos.KeywordsList> getKeywordsList() {
      return keywords_;
    }
    public java.util.List<? extends pl.edu.icm.coansys.models.DocumentProtos.KeywordsListOrBuilder> 
        getKeywordsOrBuilderList() {
      return keywords_;
    }
    public int getKeywordsCount() {
      return keywords_.size();
    }
    public pl.edu.icm.coansys.models.DocumentProtos.KeywordsList getKeywords(int index) {
      return keywords_.get(index);
    }
    public pl.edu.icm.coansys.models.DocumentProtos.KeywordsListOrBuilder getKeywordsOrBuilder(
        int index) {
      return keywords_.get(index);
    }
    
    // repeated .KeyValue extId = 5;
    public static final int EXTID_FIELD_NUMBER = 5;
    private java.util.List<pl.edu.icm.coansys.models.DocumentProtos.KeyValue> extId_;
    public java.util.List<pl.edu.icm.coansys.models.DocumentProtos.KeyValue> getExtIdList() {
      return extId_;
    }
    public java.util.List<? extends pl.edu.icm.coansys.models.DocumentProtos.KeyValueOrBuilder> 
        getExtIdOrBuilderList() {
      return extId_;
    }
    public int getExtIdCount() {
      return extId_.size();
    }
    public pl.edu.icm.coansys.models.DocumentProtos.KeyValue getExtId(int index) {
      return extId_.get(index);
    }
    public pl.edu.icm.coansys.models.DocumentProtos.KeyValueOrBuilder getExtIdOrBuilder(
        int index) {
      return extId_.get(index);
    }
    
    // repeated .KeyValue auxiliarInfo = 6;
    public static final int AUXILIARINFO_FIELD_NUMBER = 6;
    private java.util.List<pl.edu.icm.coansys.models.DocumentProtos.KeyValue> auxiliarInfo_;
    public java.util.List<pl.edu.icm.coansys.models.DocumentProtos.KeyValue> getAuxiliarInfoList() {
      return auxiliarInfo_;
    }
    public java.util.List<? extends pl.edu.icm.coansys.models.DocumentProtos.KeyValueOrBuilder> 
        getAuxiliarInfoOrBuilderList() {
      return auxiliarInfo_;
    }
    public int getAuxiliarInfoCount() {
      return auxiliarInfo_.size();
    }
    public pl.edu.icm.coansys.models.DocumentProtos.KeyValue getAuxiliarInfo(int index) {
      return auxiliarInfo_.get(index);
    }
    public pl.edu.icm.coansys.models.DocumentProtos.KeyValueOrBuilder getAuxiliarInfoOrBuilder(
        int index) {
      return auxiliarInfo_.get(index);
    }
    
    // repeated .Affiliation affiliations = 12;
    public static final int AFFILIATIONS_FIELD_NUMBER = 12;
    private java.util.List<pl.edu.icm.coansys.models.DocumentProtos.Affiliation> affiliations_;
    public java.util.List<pl.edu.icm.coansys.models.DocumentProtos.Affiliation> getAffiliationsList() {
      return affiliations_;
    }
    public java.util.List<? extends pl.edu.icm.coansys.models.DocumentProtos.AffiliationOrBuilder> 
        getAffiliationsOrBuilderList() {
      return affiliations_;
    }
    public int getAffiliationsCount() {
      return affiliations_.size();
    }
    public pl.edu.icm.coansys.models.DocumentProtos.Affiliation getAffiliations(int index) {
      return affiliations_.get(index);
    }
    public pl.edu.icm.coansys.models.DocumentProtos.AffiliationOrBuilder getAffiliationsOrBuilder(
        int index) {
      return affiliations_.get(index);
    }
    
    // repeated .ReferenceMetadata reference = 7;
    public static final int REFERENCE_FIELD_NUMBER = 7;
    private java.util.List<pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata> reference_;
    public java.util.List<pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata> getReferenceList() {
      return reference_;
    }
    public java.util.List<? extends pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadataOrBuilder> 
        getReferenceOrBuilderList() {
      return reference_;
    }
    public int getReferenceCount() {
      return reference_.size();
    }
    public pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata getReference(int index) {
      return reference_.get(index);
    }
    public pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadataOrBuilder getReferenceOrBuilder(
        int index) {
      return reference_.get(index);
    }
    
    // repeated string collection = 8;
    public static final int COLLECTION_FIELD_NUMBER = 8;
    private com.google.protobuf.LazyStringList collection_;
    public java.util.List<String>
        getCollectionList() {
      return collection_;
    }
    public int getCollectionCount() {
      return collection_.size();
    }
    public String getCollection(int index) {
      return collection_.get(index);
    }
    
    // optional string sourcePath = 9;
    public static final int SOURCEPATH_FIELD_NUMBER = 9;
    private java.lang.Object sourcePath_;
    public boolean hasSourcePath() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    public String getSourcePath() {
      java.lang.Object ref = sourcePath_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          sourcePath_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getSourcePathBytes() {
      java.lang.Object ref = sourcePath_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        sourcePath_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // repeated string origKey = 11;
    public static final int ORIGKEY_FIELD_NUMBER = 11;
    private com.google.protobuf.LazyStringList origKey_;
    public java.util.List<String>
        getOrigKeyList() {
      return origKey_;
    }
    public int getOrigKeyCount() {
      return origKey_.size();
    }
    public String getOrigKey(int index) {
      return origKey_.get(index);
    }
    
    private void initFields() {
      key_ = "";
      basicMetadata_ = pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata.getDefaultInstance();
      documentAbstract_ = java.util.Collections.emptyList();
      keywords_ = java.util.Collections.emptyList();
      extId_ = java.util.Collections.emptyList();
      auxiliarInfo_ = java.util.Collections.emptyList();
      affiliations_ = java.util.Collections.emptyList();
      reference_ = java.util.Collections.emptyList();
      collection_ = com.google.protobuf.LazyStringArrayList.EMPTY;
      sourcePath_ = "";
      origKey_ = com.google.protobuf.LazyStringArrayList.EMPTY;
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
      if (!hasKey()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasBasicMetadata()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!getBasicMetadata().isInitialized()) {
        memoizedIsInitialized = 0;
        return false;
      }
      for (int i = 0; i < getDocumentAbstractCount(); i++) {
        if (!getDocumentAbstract(i).isInitialized()) {
          memoizedIsInitialized = 0;
          return false;
        }
      }
      for (int i = 0; i < getKeywordsCount(); i++) {
        if (!getKeywords(i).isInitialized()) {
          memoizedIsInitialized = 0;
          return false;
        }
      }
      for (int i = 0; i < getExtIdCount(); i++) {
        if (!getExtId(i).isInitialized()) {
          memoizedIsInitialized = 0;
          return false;
        }
      }
      for (int i = 0; i < getAuxiliarInfoCount(); i++) {
        if (!getAuxiliarInfo(i).isInitialized()) {
          memoizedIsInitialized = 0;
          return false;
        }
      }
      for (int i = 0; i < getAffiliationsCount(); i++) {
        if (!getAffiliations(i).isInitialized()) {
          memoizedIsInitialized = 0;
          return false;
        }
      }
      for (int i = 0; i < getReferenceCount(); i++) {
        if (!getReference(i).isInitialized()) {
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
        output.writeBytes(1, getKeyBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeMessage(2, basicMetadata_);
      }
      for (int i = 0; i < documentAbstract_.size(); i++) {
        output.writeMessage(3, documentAbstract_.get(i));
      }
      for (int i = 0; i < extId_.size(); i++) {
        output.writeMessage(5, extId_.get(i));
      }
      for (int i = 0; i < auxiliarInfo_.size(); i++) {
        output.writeMessage(6, auxiliarInfo_.get(i));
      }
      for (int i = 0; i < reference_.size(); i++) {
        output.writeMessage(7, reference_.get(i));
      }
      for (int i = 0; i < collection_.size(); i++) {
        output.writeBytes(8, collection_.getByteString(i));
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeBytes(9, getSourcePathBytes());
      }
      for (int i = 0; i < keywords_.size(); i++) {
        output.writeMessage(10, keywords_.get(i));
      }
      for (int i = 0; i < origKey_.size(); i++) {
        output.writeBytes(11, origKey_.getByteString(i));
      }
      for (int i = 0; i < affiliations_.size(); i++) {
        output.writeMessage(12, affiliations_.get(i));
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
          .computeBytesSize(1, getKeyBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(2, basicMetadata_);
      }
      for (int i = 0; i < documentAbstract_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(3, documentAbstract_.get(i));
      }
      for (int i = 0; i < extId_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(5, extId_.get(i));
      }
      for (int i = 0; i < auxiliarInfo_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(6, auxiliarInfo_.get(i));
      }
      for (int i = 0; i < reference_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(7, reference_.get(i));
      }
      {
        int dataSize = 0;
        for (int i = 0; i < collection_.size(); i++) {
          dataSize += com.google.protobuf.CodedOutputStream
            .computeBytesSizeNoTag(collection_.getByteString(i));
        }
        size += dataSize;
        size += 1 * getCollectionList().size();
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(9, getSourcePathBytes());
      }
      for (int i = 0; i < keywords_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(10, keywords_.get(i));
      }
      {
        int dataSize = 0;
        for (int i = 0; i < origKey_.size(); i++) {
          dataSize += com.google.protobuf.CodedOutputStream
            .computeBytesSizeNoTag(origKey_.getByteString(i));
        }
        size += dataSize;
        size += 1 * getOrigKeyList().size();
      }
      for (int i = 0; i < affiliations_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(12, affiliations_.get(i));
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
    
    public static pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata parseDelimitedFrom(
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
    public static pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata prototype) {
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
       implements pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadataOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return pl.edu.icm.coansys.models.DocumentProtos.internal_static_DocumentMetadata_descriptor;
      }
      
      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return pl.edu.icm.coansys.models.DocumentProtos.internal_static_DocumentMetadata_fieldAccessorTable;
      }
      
      // Construct using pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }
      
      private Builder(BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getBasicMetadataFieldBuilder();
          getDocumentAbstractFieldBuilder();
          getKeywordsFieldBuilder();
          getExtIdFieldBuilder();
          getAuxiliarInfoFieldBuilder();
          getAffiliationsFieldBuilder();
          getReferenceFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }
      
      public Builder clear() {
        super.clear();
        key_ = "";
        bitField0_ = (bitField0_ & ~0x00000001);
        if (basicMetadataBuilder_ == null) {
          basicMetadata_ = pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata.getDefaultInstance();
        } else {
          basicMetadataBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000002);
        if (documentAbstractBuilder_ == null) {
          documentAbstract_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000004);
        } else {
          documentAbstractBuilder_.clear();
        }
        if (keywordsBuilder_ == null) {
          keywords_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000008);
        } else {
          keywordsBuilder_.clear();
        }
        if (extIdBuilder_ == null) {
          extId_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000010);
        } else {
          extIdBuilder_.clear();
        }
        if (auxiliarInfoBuilder_ == null) {
          auxiliarInfo_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000020);
        } else {
          auxiliarInfoBuilder_.clear();
        }
        if (affiliationsBuilder_ == null) {
          affiliations_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000040);
        } else {
          affiliationsBuilder_.clear();
        }
        if (referenceBuilder_ == null) {
          reference_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000080);
        } else {
          referenceBuilder_.clear();
        }
        collection_ = com.google.protobuf.LazyStringArrayList.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000100);
        sourcePath_ = "";
        bitField0_ = (bitField0_ & ~0x00000200);
        origKey_ = com.google.protobuf.LazyStringArrayList.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000400);
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata.getDescriptor();
      }
      
      public pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata getDefaultInstanceForType() {
        return pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata.getDefaultInstance();
      }
      
      public pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata build() {
        pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata buildPartial() {
        pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata result = new pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.key_ = key_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        if (basicMetadataBuilder_ == null) {
          result.basicMetadata_ = basicMetadata_;
        } else {
          result.basicMetadata_ = basicMetadataBuilder_.build();
        }
        if (documentAbstractBuilder_ == null) {
          if (((bitField0_ & 0x00000004) == 0x00000004)) {
            documentAbstract_ = java.util.Collections.unmodifiableList(documentAbstract_);
            bitField0_ = (bitField0_ & ~0x00000004);
          }
          result.documentAbstract_ = documentAbstract_;
        } else {
          result.documentAbstract_ = documentAbstractBuilder_.build();
        }
        if (keywordsBuilder_ == null) {
          if (((bitField0_ & 0x00000008) == 0x00000008)) {
            keywords_ = java.util.Collections.unmodifiableList(keywords_);
            bitField0_ = (bitField0_ & ~0x00000008);
          }
          result.keywords_ = keywords_;
        } else {
          result.keywords_ = keywordsBuilder_.build();
        }
        if (extIdBuilder_ == null) {
          if (((bitField0_ & 0x00000010) == 0x00000010)) {
            extId_ = java.util.Collections.unmodifiableList(extId_);
            bitField0_ = (bitField0_ & ~0x00000010);
          }
          result.extId_ = extId_;
        } else {
          result.extId_ = extIdBuilder_.build();
        }
        if (auxiliarInfoBuilder_ == null) {
          if (((bitField0_ & 0x00000020) == 0x00000020)) {
            auxiliarInfo_ = java.util.Collections.unmodifiableList(auxiliarInfo_);
            bitField0_ = (bitField0_ & ~0x00000020);
          }
          result.auxiliarInfo_ = auxiliarInfo_;
        } else {
          result.auxiliarInfo_ = auxiliarInfoBuilder_.build();
        }
        if (affiliationsBuilder_ == null) {
          if (((bitField0_ & 0x00000040) == 0x00000040)) {
            affiliations_ = java.util.Collections.unmodifiableList(affiliations_);
            bitField0_ = (bitField0_ & ~0x00000040);
          }
          result.affiliations_ = affiliations_;
        } else {
          result.affiliations_ = affiliationsBuilder_.build();
        }
        if (referenceBuilder_ == null) {
          if (((bitField0_ & 0x00000080) == 0x00000080)) {
            reference_ = java.util.Collections.unmodifiableList(reference_);
            bitField0_ = (bitField0_ & ~0x00000080);
          }
          result.reference_ = reference_;
        } else {
          result.reference_ = referenceBuilder_.build();
        }
        if (((bitField0_ & 0x00000100) == 0x00000100)) {
          collection_ = new com.google.protobuf.UnmodifiableLazyStringList(
              collection_);
          bitField0_ = (bitField0_ & ~0x00000100);
        }
        result.collection_ = collection_;
        if (((from_bitField0_ & 0x00000200) == 0x00000200)) {
          to_bitField0_ |= 0x00000004;
        }
        result.sourcePath_ = sourcePath_;
        if (((bitField0_ & 0x00000400) == 0x00000400)) {
          origKey_ = new com.google.protobuf.UnmodifiableLazyStringList(
              origKey_);
          bitField0_ = (bitField0_ & ~0x00000400);
        }
        result.origKey_ = origKey_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata) {
          return mergeFrom((pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata other) {
        if (other == pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata.getDefaultInstance()) return this;
        if (other.hasKey()) {
          setKey(other.getKey());
        }
        if (other.hasBasicMetadata()) {
          mergeBasicMetadata(other.getBasicMetadata());
        }
        if (documentAbstractBuilder_ == null) {
          if (!other.documentAbstract_.isEmpty()) {
            if (documentAbstract_.isEmpty()) {
              documentAbstract_ = other.documentAbstract_;
              bitField0_ = (bitField0_ & ~0x00000004);
            } else {
              ensureDocumentAbstractIsMutable();
              documentAbstract_.addAll(other.documentAbstract_);
            }
            onChanged();
          }
        } else {
          if (!other.documentAbstract_.isEmpty()) {
            if (documentAbstractBuilder_.isEmpty()) {
              documentAbstractBuilder_.dispose();
              documentAbstractBuilder_ = null;
              documentAbstract_ = other.documentAbstract_;
              bitField0_ = (bitField0_ & ~0x00000004);
              documentAbstractBuilder_ = 
                com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                   getDocumentAbstractFieldBuilder() : null;
            } else {
              documentAbstractBuilder_.addAllMessages(other.documentAbstract_);
            }
          }
        }
        if (keywordsBuilder_ == null) {
          if (!other.keywords_.isEmpty()) {
            if (keywords_.isEmpty()) {
              keywords_ = other.keywords_;
              bitField0_ = (bitField0_ & ~0x00000008);
            } else {
              ensureKeywordsIsMutable();
              keywords_.addAll(other.keywords_);
            }
            onChanged();
          }
        } else {
          if (!other.keywords_.isEmpty()) {
            if (keywordsBuilder_.isEmpty()) {
              keywordsBuilder_.dispose();
              keywordsBuilder_ = null;
              keywords_ = other.keywords_;
              bitField0_ = (bitField0_ & ~0x00000008);
              keywordsBuilder_ = 
                com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                   getKeywordsFieldBuilder() : null;
            } else {
              keywordsBuilder_.addAllMessages(other.keywords_);
            }
          }
        }
        if (extIdBuilder_ == null) {
          if (!other.extId_.isEmpty()) {
            if (extId_.isEmpty()) {
              extId_ = other.extId_;
              bitField0_ = (bitField0_ & ~0x00000010);
            } else {
              ensureExtIdIsMutable();
              extId_.addAll(other.extId_);
            }
            onChanged();
          }
        } else {
          if (!other.extId_.isEmpty()) {
            if (extIdBuilder_.isEmpty()) {
              extIdBuilder_.dispose();
              extIdBuilder_ = null;
              extId_ = other.extId_;
              bitField0_ = (bitField0_ & ~0x00000010);
              extIdBuilder_ = 
                com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                   getExtIdFieldBuilder() : null;
            } else {
              extIdBuilder_.addAllMessages(other.extId_);
            }
          }
        }
        if (auxiliarInfoBuilder_ == null) {
          if (!other.auxiliarInfo_.isEmpty()) {
            if (auxiliarInfo_.isEmpty()) {
              auxiliarInfo_ = other.auxiliarInfo_;
              bitField0_ = (bitField0_ & ~0x00000020);
            } else {
              ensureAuxiliarInfoIsMutable();
              auxiliarInfo_.addAll(other.auxiliarInfo_);
            }
            onChanged();
          }
        } else {
          if (!other.auxiliarInfo_.isEmpty()) {
            if (auxiliarInfoBuilder_.isEmpty()) {
              auxiliarInfoBuilder_.dispose();
              auxiliarInfoBuilder_ = null;
              auxiliarInfo_ = other.auxiliarInfo_;
              bitField0_ = (bitField0_ & ~0x00000020);
              auxiliarInfoBuilder_ = 
                com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                   getAuxiliarInfoFieldBuilder() : null;
            } else {
              auxiliarInfoBuilder_.addAllMessages(other.auxiliarInfo_);
            }
          }
        }
        if (affiliationsBuilder_ == null) {
          if (!other.affiliations_.isEmpty()) {
            if (affiliations_.isEmpty()) {
              affiliations_ = other.affiliations_;
              bitField0_ = (bitField0_ & ~0x00000040);
            } else {
              ensureAffiliationsIsMutable();
              affiliations_.addAll(other.affiliations_);
            }
            onChanged();
          }
        } else {
          if (!other.affiliations_.isEmpty()) {
            if (affiliationsBuilder_.isEmpty()) {
              affiliationsBuilder_.dispose();
              affiliationsBuilder_ = null;
              affiliations_ = other.affiliations_;
              bitField0_ = (bitField0_ & ~0x00000040);
              affiliationsBuilder_ = 
                com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                   getAffiliationsFieldBuilder() : null;
            } else {
              affiliationsBuilder_.addAllMessages(other.affiliations_);
            }
          }
        }
        if (referenceBuilder_ == null) {
          if (!other.reference_.isEmpty()) {
            if (reference_.isEmpty()) {
              reference_ = other.reference_;
              bitField0_ = (bitField0_ & ~0x00000080);
            } else {
              ensureReferenceIsMutable();
              reference_.addAll(other.reference_);
            }
            onChanged();
          }
        } else {
          if (!other.reference_.isEmpty()) {
            if (referenceBuilder_.isEmpty()) {
              referenceBuilder_.dispose();
              referenceBuilder_ = null;
              reference_ = other.reference_;
              bitField0_ = (bitField0_ & ~0x00000080);
              referenceBuilder_ = 
                com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                   getReferenceFieldBuilder() : null;
            } else {
              referenceBuilder_.addAllMessages(other.reference_);
            }
          }
        }
        if (!other.collection_.isEmpty()) {
          if (collection_.isEmpty()) {
            collection_ = other.collection_;
            bitField0_ = (bitField0_ & ~0x00000100);
          } else {
            ensureCollectionIsMutable();
            collection_.addAll(other.collection_);
          }
          onChanged();
        }
        if (other.hasSourcePath()) {
          setSourcePath(other.getSourcePath());
        }
        if (!other.origKey_.isEmpty()) {
          if (origKey_.isEmpty()) {
            origKey_ = other.origKey_;
            bitField0_ = (bitField0_ & ~0x00000400);
          } else {
            ensureOrigKeyIsMutable();
            origKey_.addAll(other.origKey_);
          }
          onChanged();
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
        if (!hasKey()) {
          
          return false;
        }
        if (!hasBasicMetadata()) {
          
          return false;
        }
        if (!getBasicMetadata().isInitialized()) {
          
          return false;
        }
        for (int i = 0; i < getDocumentAbstractCount(); i++) {
          if (!getDocumentAbstract(i).isInitialized()) {
            
            return false;
          }
        }
        for (int i = 0; i < getKeywordsCount(); i++) {
          if (!getKeywords(i).isInitialized()) {
            
            return false;
          }
        }
        for (int i = 0; i < getExtIdCount(); i++) {
          if (!getExtId(i).isInitialized()) {
            
            return false;
          }
        }
        for (int i = 0; i < getAuxiliarInfoCount(); i++) {
          if (!getAuxiliarInfo(i).isInitialized()) {
            
            return false;
          }
        }
        for (int i = 0; i < getAffiliationsCount(); i++) {
          if (!getAffiliations(i).isInitialized()) {
            
            return false;
          }
        }
        for (int i = 0; i < getReferenceCount(); i++) {
          if (!getReference(i).isInitialized()) {
            
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
              key_ = input.readBytes();
              break;
            }
            case 18: {
              pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata.Builder subBuilder = pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata.newBuilder();
              if (hasBasicMetadata()) {
                subBuilder.mergeFrom(getBasicMetadata());
              }
              input.readMessage(subBuilder, extensionRegistry);
              setBasicMetadata(subBuilder.buildPartial());
              break;
            }
            case 26: {
              pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage.Builder subBuilder = pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage.newBuilder();
              input.readMessage(subBuilder, extensionRegistry);
              addDocumentAbstract(subBuilder.buildPartial());
              break;
            }
            case 42: {
              pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder subBuilder = pl.edu.icm.coansys.models.DocumentProtos.KeyValue.newBuilder();
              input.readMessage(subBuilder, extensionRegistry);
              addExtId(subBuilder.buildPartial());
              break;
            }
            case 50: {
              pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder subBuilder = pl.edu.icm.coansys.models.DocumentProtos.KeyValue.newBuilder();
              input.readMessage(subBuilder, extensionRegistry);
              addAuxiliarInfo(subBuilder.buildPartial());
              break;
            }
            case 58: {
              pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata.Builder subBuilder = pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata.newBuilder();
              input.readMessage(subBuilder, extensionRegistry);
              addReference(subBuilder.buildPartial());
              break;
            }
            case 66: {
              ensureCollectionIsMutable();
              collection_.add(input.readBytes());
              break;
            }
            case 74: {
              bitField0_ |= 0x00000200;
              sourcePath_ = input.readBytes();
              break;
            }
            case 82: {
              pl.edu.icm.coansys.models.DocumentProtos.KeywordsList.Builder subBuilder = pl.edu.icm.coansys.models.DocumentProtos.KeywordsList.newBuilder();
              input.readMessage(subBuilder, extensionRegistry);
              addKeywords(subBuilder.buildPartial());
              break;
            }
            case 90: {
              ensureOrigKeyIsMutable();
              origKey_.add(input.readBytes());
              break;
            }
            case 98: {
              pl.edu.icm.coansys.models.DocumentProtos.Affiliation.Builder subBuilder = pl.edu.icm.coansys.models.DocumentProtos.Affiliation.newBuilder();
              input.readMessage(subBuilder, extensionRegistry);
              addAffiliations(subBuilder.buildPartial());
              break;
            }
          }
        }
      }
      
      private int bitField0_;
      
      // required string key = 1;
      private java.lang.Object key_ = "";
      public boolean hasKey() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      public String getKey() {
        java.lang.Object ref = key_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          key_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setKey(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        key_ = value;
        onChanged();
        return this;
      }
      public Builder clearKey() {
        bitField0_ = (bitField0_ & ~0x00000001);
        key_ = getDefaultInstance().getKey();
        onChanged();
        return this;
      }
      void setKey(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000001;
        key_ = value;
        onChanged();
      }
      
      // required .BasicMetadata basicMetadata = 2;
      private pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata basicMetadata_ = pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata, pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata.Builder, pl.edu.icm.coansys.models.DocumentProtos.BasicMetadataOrBuilder> basicMetadataBuilder_;
      public boolean hasBasicMetadata() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      public pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata getBasicMetadata() {
        if (basicMetadataBuilder_ == null) {
          return basicMetadata_;
        } else {
          return basicMetadataBuilder_.getMessage();
        }
      }
      public Builder setBasicMetadata(pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata value) {
        if (basicMetadataBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          basicMetadata_ = value;
          onChanged();
        } else {
          basicMetadataBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000002;
        return this;
      }
      public Builder setBasicMetadata(
          pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata.Builder builderForValue) {
        if (basicMetadataBuilder_ == null) {
          basicMetadata_ = builderForValue.build();
          onChanged();
        } else {
          basicMetadataBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000002;
        return this;
      }
      public Builder mergeBasicMetadata(pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata value) {
        if (basicMetadataBuilder_ == null) {
          if (((bitField0_ & 0x00000002) == 0x00000002) &&
              basicMetadata_ != pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata.getDefaultInstance()) {
            basicMetadata_ =
              pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata.newBuilder(basicMetadata_).mergeFrom(value).buildPartial();
          } else {
            basicMetadata_ = value;
          }
          onChanged();
        } else {
          basicMetadataBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000002;
        return this;
      }
      public Builder clearBasicMetadata() {
        if (basicMetadataBuilder_ == null) {
          basicMetadata_ = pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata.getDefaultInstance();
          onChanged();
        } else {
          basicMetadataBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }
      public pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata.Builder getBasicMetadataBuilder() {
        bitField0_ |= 0x00000002;
        onChanged();
        return getBasicMetadataFieldBuilder().getBuilder();
      }
      public pl.edu.icm.coansys.models.DocumentProtos.BasicMetadataOrBuilder getBasicMetadataOrBuilder() {
        if (basicMetadataBuilder_ != null) {
          return basicMetadataBuilder_.getMessageOrBuilder();
        } else {
          return basicMetadata_;
        }
      }
      private com.google.protobuf.SingleFieldBuilder<
          pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata, pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata.Builder, pl.edu.icm.coansys.models.DocumentProtos.BasicMetadataOrBuilder> 
          getBasicMetadataFieldBuilder() {
        if (basicMetadataBuilder_ == null) {
          basicMetadataBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata, pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata.Builder, pl.edu.icm.coansys.models.DocumentProtos.BasicMetadataOrBuilder>(
                  basicMetadata_,
                  getParentForChildren(),
                  isClean());
          basicMetadata_ = null;
        }
        return basicMetadataBuilder_;
      }
      
      // repeated .TextWithLanguage documentAbstract = 3;
      private java.util.List<pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage> documentAbstract_ =
        java.util.Collections.emptyList();
      private void ensureDocumentAbstractIsMutable() {
        if (!((bitField0_ & 0x00000004) == 0x00000004)) {
          documentAbstract_ = new java.util.ArrayList<pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage>(documentAbstract_);
          bitField0_ |= 0x00000004;
         }
      }
      
      private com.google.protobuf.RepeatedFieldBuilder<
          pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage, pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage.Builder, pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguageOrBuilder> documentAbstractBuilder_;
      
      public java.util.List<pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage> getDocumentAbstractList() {
        if (documentAbstractBuilder_ == null) {
          return java.util.Collections.unmodifiableList(documentAbstract_);
        } else {
          return documentAbstractBuilder_.getMessageList();
        }
      }
      public int getDocumentAbstractCount() {
        if (documentAbstractBuilder_ == null) {
          return documentAbstract_.size();
        } else {
          return documentAbstractBuilder_.getCount();
        }
      }
      public pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage getDocumentAbstract(int index) {
        if (documentAbstractBuilder_ == null) {
          return documentAbstract_.get(index);
        } else {
          return documentAbstractBuilder_.getMessage(index);
        }
      }
      public Builder setDocumentAbstract(
          int index, pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage value) {
        if (documentAbstractBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureDocumentAbstractIsMutable();
          documentAbstract_.set(index, value);
          onChanged();
        } else {
          documentAbstractBuilder_.setMessage(index, value);
        }
        return this;
      }
      public Builder setDocumentAbstract(
          int index, pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage.Builder builderForValue) {
        if (documentAbstractBuilder_ == null) {
          ensureDocumentAbstractIsMutable();
          documentAbstract_.set(index, builderForValue.build());
          onChanged();
        } else {
          documentAbstractBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }
      public Builder addDocumentAbstract(pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage value) {
        if (documentAbstractBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureDocumentAbstractIsMutable();
          documentAbstract_.add(value);
          onChanged();
        } else {
          documentAbstractBuilder_.addMessage(value);
        }
        return this;
      }
      public Builder addDocumentAbstract(
          int index, pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage value) {
        if (documentAbstractBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureDocumentAbstractIsMutable();
          documentAbstract_.add(index, value);
          onChanged();
        } else {
          documentAbstractBuilder_.addMessage(index, value);
        }
        return this;
      }
      public Builder addDocumentAbstract(
          pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage.Builder builderForValue) {
        if (documentAbstractBuilder_ == null) {
          ensureDocumentAbstractIsMutable();
          documentAbstract_.add(builderForValue.build());
          onChanged();
        } else {
          documentAbstractBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }
      public Builder addDocumentAbstract(
          int index, pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage.Builder builderForValue) {
        if (documentAbstractBuilder_ == null) {
          ensureDocumentAbstractIsMutable();
          documentAbstract_.add(index, builderForValue.build());
          onChanged();
        } else {
          documentAbstractBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }
      public Builder addAllDocumentAbstract(
          java.lang.Iterable<? extends pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage> values) {
        if (documentAbstractBuilder_ == null) {
          ensureDocumentAbstractIsMutable();
          super.addAll(values, documentAbstract_);
          onChanged();
        } else {
          documentAbstractBuilder_.addAllMessages(values);
        }
        return this;
      }
      public Builder clearDocumentAbstract() {
        if (documentAbstractBuilder_ == null) {
          documentAbstract_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000004);
          onChanged();
        } else {
          documentAbstractBuilder_.clear();
        }
        return this;
      }
      public Builder removeDocumentAbstract(int index) {
        if (documentAbstractBuilder_ == null) {
          ensureDocumentAbstractIsMutable();
          documentAbstract_.remove(index);
          onChanged();
        } else {
          documentAbstractBuilder_.remove(index);
        }
        return this;
      }
      public pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage.Builder getDocumentAbstractBuilder(
          int index) {
        return getDocumentAbstractFieldBuilder().getBuilder(index);
      }
      public pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguageOrBuilder getDocumentAbstractOrBuilder(
          int index) {
        if (documentAbstractBuilder_ == null) {
          return documentAbstract_.get(index);  } else {
          return documentAbstractBuilder_.getMessageOrBuilder(index);
        }
      }
      public java.util.List<? extends pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguageOrBuilder> 
           getDocumentAbstractOrBuilderList() {
        if (documentAbstractBuilder_ != null) {
          return documentAbstractBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(documentAbstract_);
        }
      }
      public pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage.Builder addDocumentAbstractBuilder() {
        return getDocumentAbstractFieldBuilder().addBuilder(
            pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage.getDefaultInstance());
      }
      public pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage.Builder addDocumentAbstractBuilder(
          int index) {
        return getDocumentAbstractFieldBuilder().addBuilder(
            index, pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage.getDefaultInstance());
      }
      public java.util.List<pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage.Builder> 
           getDocumentAbstractBuilderList() {
        return getDocumentAbstractFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilder<
          pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage, pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage.Builder, pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguageOrBuilder> 
          getDocumentAbstractFieldBuilder() {
        if (documentAbstractBuilder_ == null) {
          documentAbstractBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
              pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage, pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage.Builder, pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguageOrBuilder>(
                  documentAbstract_,
                  ((bitField0_ & 0x00000004) == 0x00000004),
                  getParentForChildren(),
                  isClean());
          documentAbstract_ = null;
        }
        return documentAbstractBuilder_;
      }
      
      // repeated .KeywordsList keywords = 10;
      private java.util.List<pl.edu.icm.coansys.models.DocumentProtos.KeywordsList> keywords_ =
        java.util.Collections.emptyList();
      private void ensureKeywordsIsMutable() {
        if (!((bitField0_ & 0x00000008) == 0x00000008)) {
          keywords_ = new java.util.ArrayList<pl.edu.icm.coansys.models.DocumentProtos.KeywordsList>(keywords_);
          bitField0_ |= 0x00000008;
         }
      }
      
      private com.google.protobuf.RepeatedFieldBuilder<
          pl.edu.icm.coansys.models.DocumentProtos.KeywordsList, pl.edu.icm.coansys.models.DocumentProtos.KeywordsList.Builder, pl.edu.icm.coansys.models.DocumentProtos.KeywordsListOrBuilder> keywordsBuilder_;
      
      public java.util.List<pl.edu.icm.coansys.models.DocumentProtos.KeywordsList> getKeywordsList() {
        if (keywordsBuilder_ == null) {
          return java.util.Collections.unmodifiableList(keywords_);
        } else {
          return keywordsBuilder_.getMessageList();
        }
      }
      public int getKeywordsCount() {
        if (keywordsBuilder_ == null) {
          return keywords_.size();
        } else {
          return keywordsBuilder_.getCount();
        }
      }
      public pl.edu.icm.coansys.models.DocumentProtos.KeywordsList getKeywords(int index) {
        if (keywordsBuilder_ == null) {
          return keywords_.get(index);
        } else {
          return keywordsBuilder_.getMessage(index);
        }
      }
      public Builder setKeywords(
          int index, pl.edu.icm.coansys.models.DocumentProtos.KeywordsList value) {
        if (keywordsBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureKeywordsIsMutable();
          keywords_.set(index, value);
          onChanged();
        } else {
          keywordsBuilder_.setMessage(index, value);
        }
        return this;
      }
      public Builder setKeywords(
          int index, pl.edu.icm.coansys.models.DocumentProtos.KeywordsList.Builder builderForValue) {
        if (keywordsBuilder_ == null) {
          ensureKeywordsIsMutable();
          keywords_.set(index, builderForValue.build());
          onChanged();
        } else {
          keywordsBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }
      public Builder addKeywords(pl.edu.icm.coansys.models.DocumentProtos.KeywordsList value) {
        if (keywordsBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureKeywordsIsMutable();
          keywords_.add(value);
          onChanged();
        } else {
          keywordsBuilder_.addMessage(value);
        }
        return this;
      }
      public Builder addKeywords(
          int index, pl.edu.icm.coansys.models.DocumentProtos.KeywordsList value) {
        if (keywordsBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureKeywordsIsMutable();
          keywords_.add(index, value);
          onChanged();
        } else {
          keywordsBuilder_.addMessage(index, value);
        }
        return this;
      }
      public Builder addKeywords(
          pl.edu.icm.coansys.models.DocumentProtos.KeywordsList.Builder builderForValue) {
        if (keywordsBuilder_ == null) {
          ensureKeywordsIsMutable();
          keywords_.add(builderForValue.build());
          onChanged();
        } else {
          keywordsBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }
      public Builder addKeywords(
          int index, pl.edu.icm.coansys.models.DocumentProtos.KeywordsList.Builder builderForValue) {
        if (keywordsBuilder_ == null) {
          ensureKeywordsIsMutable();
          keywords_.add(index, builderForValue.build());
          onChanged();
        } else {
          keywordsBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }
      public Builder addAllKeywords(
          java.lang.Iterable<? extends pl.edu.icm.coansys.models.DocumentProtos.KeywordsList> values) {
        if (keywordsBuilder_ == null) {
          ensureKeywordsIsMutable();
          super.addAll(values, keywords_);
          onChanged();
        } else {
          keywordsBuilder_.addAllMessages(values);
        }
        return this;
      }
      public Builder clearKeywords() {
        if (keywordsBuilder_ == null) {
          keywords_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000008);
          onChanged();
        } else {
          keywordsBuilder_.clear();
        }
        return this;
      }
      public Builder removeKeywords(int index) {
        if (keywordsBuilder_ == null) {
          ensureKeywordsIsMutable();
          keywords_.remove(index);
          onChanged();
        } else {
          keywordsBuilder_.remove(index);
        }
        return this;
      }
      public pl.edu.icm.coansys.models.DocumentProtos.KeywordsList.Builder getKeywordsBuilder(
          int index) {
        return getKeywordsFieldBuilder().getBuilder(index);
      }
      public pl.edu.icm.coansys.models.DocumentProtos.KeywordsListOrBuilder getKeywordsOrBuilder(
          int index) {
        if (keywordsBuilder_ == null) {
          return keywords_.get(index);  } else {
          return keywordsBuilder_.getMessageOrBuilder(index);
        }
      }
      public java.util.List<? extends pl.edu.icm.coansys.models.DocumentProtos.KeywordsListOrBuilder> 
           getKeywordsOrBuilderList() {
        if (keywordsBuilder_ != null) {
          return keywordsBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(keywords_);
        }
      }
      public pl.edu.icm.coansys.models.DocumentProtos.KeywordsList.Builder addKeywordsBuilder() {
        return getKeywordsFieldBuilder().addBuilder(
            pl.edu.icm.coansys.models.DocumentProtos.KeywordsList.getDefaultInstance());
      }
      public pl.edu.icm.coansys.models.DocumentProtos.KeywordsList.Builder addKeywordsBuilder(
          int index) {
        return getKeywordsFieldBuilder().addBuilder(
            index, pl.edu.icm.coansys.models.DocumentProtos.KeywordsList.getDefaultInstance());
      }
      public java.util.List<pl.edu.icm.coansys.models.DocumentProtos.KeywordsList.Builder> 
           getKeywordsBuilderList() {
        return getKeywordsFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilder<
          pl.edu.icm.coansys.models.DocumentProtos.KeywordsList, pl.edu.icm.coansys.models.DocumentProtos.KeywordsList.Builder, pl.edu.icm.coansys.models.DocumentProtos.KeywordsListOrBuilder> 
          getKeywordsFieldBuilder() {
        if (keywordsBuilder_ == null) {
          keywordsBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
              pl.edu.icm.coansys.models.DocumentProtos.KeywordsList, pl.edu.icm.coansys.models.DocumentProtos.KeywordsList.Builder, pl.edu.icm.coansys.models.DocumentProtos.KeywordsListOrBuilder>(
                  keywords_,
                  ((bitField0_ & 0x00000008) == 0x00000008),
                  getParentForChildren(),
                  isClean());
          keywords_ = null;
        }
        return keywordsBuilder_;
      }
      
      // repeated .KeyValue extId = 5;
      private java.util.List<pl.edu.icm.coansys.models.DocumentProtos.KeyValue> extId_ =
        java.util.Collections.emptyList();
      private void ensureExtIdIsMutable() {
        if (!((bitField0_ & 0x00000010) == 0x00000010)) {
          extId_ = new java.util.ArrayList<pl.edu.icm.coansys.models.DocumentProtos.KeyValue>(extId_);
          bitField0_ |= 0x00000010;
         }
      }
      
      private com.google.protobuf.RepeatedFieldBuilder<
          pl.edu.icm.coansys.models.DocumentProtos.KeyValue, pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder, pl.edu.icm.coansys.models.DocumentProtos.KeyValueOrBuilder> extIdBuilder_;
      
      public java.util.List<pl.edu.icm.coansys.models.DocumentProtos.KeyValue> getExtIdList() {
        if (extIdBuilder_ == null) {
          return java.util.Collections.unmodifiableList(extId_);
        } else {
          return extIdBuilder_.getMessageList();
        }
      }
      public int getExtIdCount() {
        if (extIdBuilder_ == null) {
          return extId_.size();
        } else {
          return extIdBuilder_.getCount();
        }
      }
      public pl.edu.icm.coansys.models.DocumentProtos.KeyValue getExtId(int index) {
        if (extIdBuilder_ == null) {
          return extId_.get(index);
        } else {
          return extIdBuilder_.getMessage(index);
        }
      }
      public Builder setExtId(
          int index, pl.edu.icm.coansys.models.DocumentProtos.KeyValue value) {
        if (extIdBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureExtIdIsMutable();
          extId_.set(index, value);
          onChanged();
        } else {
          extIdBuilder_.setMessage(index, value);
        }
        return this;
      }
      public Builder setExtId(
          int index, pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder builderForValue) {
        if (extIdBuilder_ == null) {
          ensureExtIdIsMutable();
          extId_.set(index, builderForValue.build());
          onChanged();
        } else {
          extIdBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }
      public Builder addExtId(pl.edu.icm.coansys.models.DocumentProtos.KeyValue value) {
        if (extIdBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureExtIdIsMutable();
          extId_.add(value);
          onChanged();
        } else {
          extIdBuilder_.addMessage(value);
        }
        return this;
      }
      public Builder addExtId(
          int index, pl.edu.icm.coansys.models.DocumentProtos.KeyValue value) {
        if (extIdBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureExtIdIsMutable();
          extId_.add(index, value);
          onChanged();
        } else {
          extIdBuilder_.addMessage(index, value);
        }
        return this;
      }
      public Builder addExtId(
          pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder builderForValue) {
        if (extIdBuilder_ == null) {
          ensureExtIdIsMutable();
          extId_.add(builderForValue.build());
          onChanged();
        } else {
          extIdBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }
      public Builder addExtId(
          int index, pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder builderForValue) {
        if (extIdBuilder_ == null) {
          ensureExtIdIsMutable();
          extId_.add(index, builderForValue.build());
          onChanged();
        } else {
          extIdBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }
      public Builder addAllExtId(
          java.lang.Iterable<? extends pl.edu.icm.coansys.models.DocumentProtos.KeyValue> values) {
        if (extIdBuilder_ == null) {
          ensureExtIdIsMutable();
          super.addAll(values, extId_);
          onChanged();
        } else {
          extIdBuilder_.addAllMessages(values);
        }
        return this;
      }
      public Builder clearExtId() {
        if (extIdBuilder_ == null) {
          extId_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000010);
          onChanged();
        } else {
          extIdBuilder_.clear();
        }
        return this;
      }
      public Builder removeExtId(int index) {
        if (extIdBuilder_ == null) {
          ensureExtIdIsMutable();
          extId_.remove(index);
          onChanged();
        } else {
          extIdBuilder_.remove(index);
        }
        return this;
      }
      public pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder getExtIdBuilder(
          int index) {
        return getExtIdFieldBuilder().getBuilder(index);
      }
      public pl.edu.icm.coansys.models.DocumentProtos.KeyValueOrBuilder getExtIdOrBuilder(
          int index) {
        if (extIdBuilder_ == null) {
          return extId_.get(index);  } else {
          return extIdBuilder_.getMessageOrBuilder(index);
        }
      }
      public java.util.List<? extends pl.edu.icm.coansys.models.DocumentProtos.KeyValueOrBuilder> 
           getExtIdOrBuilderList() {
        if (extIdBuilder_ != null) {
          return extIdBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(extId_);
        }
      }
      public pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder addExtIdBuilder() {
        return getExtIdFieldBuilder().addBuilder(
            pl.edu.icm.coansys.models.DocumentProtos.KeyValue.getDefaultInstance());
      }
      public pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder addExtIdBuilder(
          int index) {
        return getExtIdFieldBuilder().addBuilder(
            index, pl.edu.icm.coansys.models.DocumentProtos.KeyValue.getDefaultInstance());
      }
      public java.util.List<pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder> 
           getExtIdBuilderList() {
        return getExtIdFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilder<
          pl.edu.icm.coansys.models.DocumentProtos.KeyValue, pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder, pl.edu.icm.coansys.models.DocumentProtos.KeyValueOrBuilder> 
          getExtIdFieldBuilder() {
        if (extIdBuilder_ == null) {
          extIdBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
              pl.edu.icm.coansys.models.DocumentProtos.KeyValue, pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder, pl.edu.icm.coansys.models.DocumentProtos.KeyValueOrBuilder>(
                  extId_,
                  ((bitField0_ & 0x00000010) == 0x00000010),
                  getParentForChildren(),
                  isClean());
          extId_ = null;
        }
        return extIdBuilder_;
      }
      
      // repeated .KeyValue auxiliarInfo = 6;
      private java.util.List<pl.edu.icm.coansys.models.DocumentProtos.KeyValue> auxiliarInfo_ =
        java.util.Collections.emptyList();
      private void ensureAuxiliarInfoIsMutable() {
        if (!((bitField0_ & 0x00000020) == 0x00000020)) {
          auxiliarInfo_ = new java.util.ArrayList<pl.edu.icm.coansys.models.DocumentProtos.KeyValue>(auxiliarInfo_);
          bitField0_ |= 0x00000020;
         }
      }
      
      private com.google.protobuf.RepeatedFieldBuilder<
          pl.edu.icm.coansys.models.DocumentProtos.KeyValue, pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder, pl.edu.icm.coansys.models.DocumentProtos.KeyValueOrBuilder> auxiliarInfoBuilder_;
      
      public java.util.List<pl.edu.icm.coansys.models.DocumentProtos.KeyValue> getAuxiliarInfoList() {
        if (auxiliarInfoBuilder_ == null) {
          return java.util.Collections.unmodifiableList(auxiliarInfo_);
        } else {
          return auxiliarInfoBuilder_.getMessageList();
        }
      }
      public int getAuxiliarInfoCount() {
        if (auxiliarInfoBuilder_ == null) {
          return auxiliarInfo_.size();
        } else {
          return auxiliarInfoBuilder_.getCount();
        }
      }
      public pl.edu.icm.coansys.models.DocumentProtos.KeyValue getAuxiliarInfo(int index) {
        if (auxiliarInfoBuilder_ == null) {
          return auxiliarInfo_.get(index);
        } else {
          return auxiliarInfoBuilder_.getMessage(index);
        }
      }
      public Builder setAuxiliarInfo(
          int index, pl.edu.icm.coansys.models.DocumentProtos.KeyValue value) {
        if (auxiliarInfoBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureAuxiliarInfoIsMutable();
          auxiliarInfo_.set(index, value);
          onChanged();
        } else {
          auxiliarInfoBuilder_.setMessage(index, value);
        }
        return this;
      }
      public Builder setAuxiliarInfo(
          int index, pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder builderForValue) {
        if (auxiliarInfoBuilder_ == null) {
          ensureAuxiliarInfoIsMutable();
          auxiliarInfo_.set(index, builderForValue.build());
          onChanged();
        } else {
          auxiliarInfoBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }
      public Builder addAuxiliarInfo(pl.edu.icm.coansys.models.DocumentProtos.KeyValue value) {
        if (auxiliarInfoBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureAuxiliarInfoIsMutable();
          auxiliarInfo_.add(value);
          onChanged();
        } else {
          auxiliarInfoBuilder_.addMessage(value);
        }
        return this;
      }
      public Builder addAuxiliarInfo(
          int index, pl.edu.icm.coansys.models.DocumentProtos.KeyValue value) {
        if (auxiliarInfoBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureAuxiliarInfoIsMutable();
          auxiliarInfo_.add(index, value);
          onChanged();
        } else {
          auxiliarInfoBuilder_.addMessage(index, value);
        }
        return this;
      }
      public Builder addAuxiliarInfo(
          pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder builderForValue) {
        if (auxiliarInfoBuilder_ == null) {
          ensureAuxiliarInfoIsMutable();
          auxiliarInfo_.add(builderForValue.build());
          onChanged();
        } else {
          auxiliarInfoBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }
      public Builder addAuxiliarInfo(
          int index, pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder builderForValue) {
        if (auxiliarInfoBuilder_ == null) {
          ensureAuxiliarInfoIsMutable();
          auxiliarInfo_.add(index, builderForValue.build());
          onChanged();
        } else {
          auxiliarInfoBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }
      public Builder addAllAuxiliarInfo(
          java.lang.Iterable<? extends pl.edu.icm.coansys.models.DocumentProtos.KeyValue> values) {
        if (auxiliarInfoBuilder_ == null) {
          ensureAuxiliarInfoIsMutable();
          super.addAll(values, auxiliarInfo_);
          onChanged();
        } else {
          auxiliarInfoBuilder_.addAllMessages(values);
        }
        return this;
      }
      public Builder clearAuxiliarInfo() {
        if (auxiliarInfoBuilder_ == null) {
          auxiliarInfo_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000020);
          onChanged();
        } else {
          auxiliarInfoBuilder_.clear();
        }
        return this;
      }
      public Builder removeAuxiliarInfo(int index) {
        if (auxiliarInfoBuilder_ == null) {
          ensureAuxiliarInfoIsMutable();
          auxiliarInfo_.remove(index);
          onChanged();
        } else {
          auxiliarInfoBuilder_.remove(index);
        }
        return this;
      }
      public pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder getAuxiliarInfoBuilder(
          int index) {
        return getAuxiliarInfoFieldBuilder().getBuilder(index);
      }
      public pl.edu.icm.coansys.models.DocumentProtos.KeyValueOrBuilder getAuxiliarInfoOrBuilder(
          int index) {
        if (auxiliarInfoBuilder_ == null) {
          return auxiliarInfo_.get(index);  } else {
          return auxiliarInfoBuilder_.getMessageOrBuilder(index);
        }
      }
      public java.util.List<? extends pl.edu.icm.coansys.models.DocumentProtos.KeyValueOrBuilder> 
           getAuxiliarInfoOrBuilderList() {
        if (auxiliarInfoBuilder_ != null) {
          return auxiliarInfoBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(auxiliarInfo_);
        }
      }
      public pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder addAuxiliarInfoBuilder() {
        return getAuxiliarInfoFieldBuilder().addBuilder(
            pl.edu.icm.coansys.models.DocumentProtos.KeyValue.getDefaultInstance());
      }
      public pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder addAuxiliarInfoBuilder(
          int index) {
        return getAuxiliarInfoFieldBuilder().addBuilder(
            index, pl.edu.icm.coansys.models.DocumentProtos.KeyValue.getDefaultInstance());
      }
      public java.util.List<pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder> 
           getAuxiliarInfoBuilderList() {
        return getAuxiliarInfoFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilder<
          pl.edu.icm.coansys.models.DocumentProtos.KeyValue, pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder, pl.edu.icm.coansys.models.DocumentProtos.KeyValueOrBuilder> 
          getAuxiliarInfoFieldBuilder() {
        if (auxiliarInfoBuilder_ == null) {
          auxiliarInfoBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
              pl.edu.icm.coansys.models.DocumentProtos.KeyValue, pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder, pl.edu.icm.coansys.models.DocumentProtos.KeyValueOrBuilder>(
                  auxiliarInfo_,
                  ((bitField0_ & 0x00000020) == 0x00000020),
                  getParentForChildren(),
                  isClean());
          auxiliarInfo_ = null;
        }
        return auxiliarInfoBuilder_;
      }
      
      // repeated .Affiliation affiliations = 12;
      private java.util.List<pl.edu.icm.coansys.models.DocumentProtos.Affiliation> affiliations_ =
        java.util.Collections.emptyList();
      private void ensureAffiliationsIsMutable() {
        if (!((bitField0_ & 0x00000040) == 0x00000040)) {
          affiliations_ = new java.util.ArrayList<pl.edu.icm.coansys.models.DocumentProtos.Affiliation>(affiliations_);
          bitField0_ |= 0x00000040;
         }
      }
      
      private com.google.protobuf.RepeatedFieldBuilder<
          pl.edu.icm.coansys.models.DocumentProtos.Affiliation, pl.edu.icm.coansys.models.DocumentProtos.Affiliation.Builder, pl.edu.icm.coansys.models.DocumentProtos.AffiliationOrBuilder> affiliationsBuilder_;
      
      public java.util.List<pl.edu.icm.coansys.models.DocumentProtos.Affiliation> getAffiliationsList() {
        if (affiliationsBuilder_ == null) {
          return java.util.Collections.unmodifiableList(affiliations_);
        } else {
          return affiliationsBuilder_.getMessageList();
        }
      }
      public int getAffiliationsCount() {
        if (affiliationsBuilder_ == null) {
          return affiliations_.size();
        } else {
          return affiliationsBuilder_.getCount();
        }
      }
      public pl.edu.icm.coansys.models.DocumentProtos.Affiliation getAffiliations(int index) {
        if (affiliationsBuilder_ == null) {
          return affiliations_.get(index);
        } else {
          return affiliationsBuilder_.getMessage(index);
        }
      }
      public Builder setAffiliations(
          int index, pl.edu.icm.coansys.models.DocumentProtos.Affiliation value) {
        if (affiliationsBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureAffiliationsIsMutable();
          affiliations_.set(index, value);
          onChanged();
        } else {
          affiliationsBuilder_.setMessage(index, value);
        }
        return this;
      }
      public Builder setAffiliations(
          int index, pl.edu.icm.coansys.models.DocumentProtos.Affiliation.Builder builderForValue) {
        if (affiliationsBuilder_ == null) {
          ensureAffiliationsIsMutable();
          affiliations_.set(index, builderForValue.build());
          onChanged();
        } else {
          affiliationsBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }
      public Builder addAffiliations(pl.edu.icm.coansys.models.DocumentProtos.Affiliation value) {
        if (affiliationsBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureAffiliationsIsMutable();
          affiliations_.add(value);
          onChanged();
        } else {
          affiliationsBuilder_.addMessage(value);
        }
        return this;
      }
      public Builder addAffiliations(
          int index, pl.edu.icm.coansys.models.DocumentProtos.Affiliation value) {
        if (affiliationsBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureAffiliationsIsMutable();
          affiliations_.add(index, value);
          onChanged();
        } else {
          affiliationsBuilder_.addMessage(index, value);
        }
        return this;
      }
      public Builder addAffiliations(
          pl.edu.icm.coansys.models.DocumentProtos.Affiliation.Builder builderForValue) {
        if (affiliationsBuilder_ == null) {
          ensureAffiliationsIsMutable();
          affiliations_.add(builderForValue.build());
          onChanged();
        } else {
          affiliationsBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }
      public Builder addAffiliations(
          int index, pl.edu.icm.coansys.models.DocumentProtos.Affiliation.Builder builderForValue) {
        if (affiliationsBuilder_ == null) {
          ensureAffiliationsIsMutable();
          affiliations_.add(index, builderForValue.build());
          onChanged();
        } else {
          affiliationsBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }
      public Builder addAllAffiliations(
          java.lang.Iterable<? extends pl.edu.icm.coansys.models.DocumentProtos.Affiliation> values) {
        if (affiliationsBuilder_ == null) {
          ensureAffiliationsIsMutable();
          super.addAll(values, affiliations_);
          onChanged();
        } else {
          affiliationsBuilder_.addAllMessages(values);
        }
        return this;
      }
      public Builder clearAffiliations() {
        if (affiliationsBuilder_ == null) {
          affiliations_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000040);
          onChanged();
        } else {
          affiliationsBuilder_.clear();
        }
        return this;
      }
      public Builder removeAffiliations(int index) {
        if (affiliationsBuilder_ == null) {
          ensureAffiliationsIsMutable();
          affiliations_.remove(index);
          onChanged();
        } else {
          affiliationsBuilder_.remove(index);
        }
        return this;
      }
      public pl.edu.icm.coansys.models.DocumentProtos.Affiliation.Builder getAffiliationsBuilder(
          int index) {
        return getAffiliationsFieldBuilder().getBuilder(index);
      }
      public pl.edu.icm.coansys.models.DocumentProtos.AffiliationOrBuilder getAffiliationsOrBuilder(
          int index) {
        if (affiliationsBuilder_ == null) {
          return affiliations_.get(index);  } else {
          return affiliationsBuilder_.getMessageOrBuilder(index);
        }
      }
      public java.util.List<? extends pl.edu.icm.coansys.models.DocumentProtos.AffiliationOrBuilder> 
           getAffiliationsOrBuilderList() {
        if (affiliationsBuilder_ != null) {
          return affiliationsBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(affiliations_);
        }
      }
      public pl.edu.icm.coansys.models.DocumentProtos.Affiliation.Builder addAffiliationsBuilder() {
        return getAffiliationsFieldBuilder().addBuilder(
            pl.edu.icm.coansys.models.DocumentProtos.Affiliation.getDefaultInstance());
      }
      public pl.edu.icm.coansys.models.DocumentProtos.Affiliation.Builder addAffiliationsBuilder(
          int index) {
        return getAffiliationsFieldBuilder().addBuilder(
            index, pl.edu.icm.coansys.models.DocumentProtos.Affiliation.getDefaultInstance());
      }
      public java.util.List<pl.edu.icm.coansys.models.DocumentProtos.Affiliation.Builder> 
           getAffiliationsBuilderList() {
        return getAffiliationsFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilder<
          pl.edu.icm.coansys.models.DocumentProtos.Affiliation, pl.edu.icm.coansys.models.DocumentProtos.Affiliation.Builder, pl.edu.icm.coansys.models.DocumentProtos.AffiliationOrBuilder> 
          getAffiliationsFieldBuilder() {
        if (affiliationsBuilder_ == null) {
          affiliationsBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
              pl.edu.icm.coansys.models.DocumentProtos.Affiliation, pl.edu.icm.coansys.models.DocumentProtos.Affiliation.Builder, pl.edu.icm.coansys.models.DocumentProtos.AffiliationOrBuilder>(
                  affiliations_,
                  ((bitField0_ & 0x00000040) == 0x00000040),
                  getParentForChildren(),
                  isClean());
          affiliations_ = null;
        }
        return affiliationsBuilder_;
      }
      
      // repeated .ReferenceMetadata reference = 7;
      private java.util.List<pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata> reference_ =
        java.util.Collections.emptyList();
      private void ensureReferenceIsMutable() {
        if (!((bitField0_ & 0x00000080) == 0x00000080)) {
          reference_ = new java.util.ArrayList<pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata>(reference_);
          bitField0_ |= 0x00000080;
         }
      }
      
      private com.google.protobuf.RepeatedFieldBuilder<
          pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata, pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata.Builder, pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadataOrBuilder> referenceBuilder_;
      
      public java.util.List<pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata> getReferenceList() {
        if (referenceBuilder_ == null) {
          return java.util.Collections.unmodifiableList(reference_);
        } else {
          return referenceBuilder_.getMessageList();
        }
      }
      public int getReferenceCount() {
        if (referenceBuilder_ == null) {
          return reference_.size();
        } else {
          return referenceBuilder_.getCount();
        }
      }
      public pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata getReference(int index) {
        if (referenceBuilder_ == null) {
          return reference_.get(index);
        } else {
          return referenceBuilder_.getMessage(index);
        }
      }
      public Builder setReference(
          int index, pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata value) {
        if (referenceBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureReferenceIsMutable();
          reference_.set(index, value);
          onChanged();
        } else {
          referenceBuilder_.setMessage(index, value);
        }
        return this;
      }
      public Builder setReference(
          int index, pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata.Builder builderForValue) {
        if (referenceBuilder_ == null) {
          ensureReferenceIsMutable();
          reference_.set(index, builderForValue.build());
          onChanged();
        } else {
          referenceBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }
      public Builder addReference(pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata value) {
        if (referenceBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureReferenceIsMutable();
          reference_.add(value);
          onChanged();
        } else {
          referenceBuilder_.addMessage(value);
        }
        return this;
      }
      public Builder addReference(
          int index, pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata value) {
        if (referenceBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureReferenceIsMutable();
          reference_.add(index, value);
          onChanged();
        } else {
          referenceBuilder_.addMessage(index, value);
        }
        return this;
      }
      public Builder addReference(
          pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata.Builder builderForValue) {
        if (referenceBuilder_ == null) {
          ensureReferenceIsMutable();
          reference_.add(builderForValue.build());
          onChanged();
        } else {
          referenceBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }
      public Builder addReference(
          int index, pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata.Builder builderForValue) {
        if (referenceBuilder_ == null) {
          ensureReferenceIsMutable();
          reference_.add(index, builderForValue.build());
          onChanged();
        } else {
          referenceBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }
      public Builder addAllReference(
          java.lang.Iterable<? extends pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata> values) {
        if (referenceBuilder_ == null) {
          ensureReferenceIsMutable();
          super.addAll(values, reference_);
          onChanged();
        } else {
          referenceBuilder_.addAllMessages(values);
        }
        return this;
      }
      public Builder clearReference() {
        if (referenceBuilder_ == null) {
          reference_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000080);
          onChanged();
        } else {
          referenceBuilder_.clear();
        }
        return this;
      }
      public Builder removeReference(int index) {
        if (referenceBuilder_ == null) {
          ensureReferenceIsMutable();
          reference_.remove(index);
          onChanged();
        } else {
          referenceBuilder_.remove(index);
        }
        return this;
      }
      public pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata.Builder getReferenceBuilder(
          int index) {
        return getReferenceFieldBuilder().getBuilder(index);
      }
      public pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadataOrBuilder getReferenceOrBuilder(
          int index) {
        if (referenceBuilder_ == null) {
          return reference_.get(index);  } else {
          return referenceBuilder_.getMessageOrBuilder(index);
        }
      }
      public java.util.List<? extends pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadataOrBuilder> 
           getReferenceOrBuilderList() {
        if (referenceBuilder_ != null) {
          return referenceBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(reference_);
        }
      }
      public pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata.Builder addReferenceBuilder() {
        return getReferenceFieldBuilder().addBuilder(
            pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata.getDefaultInstance());
      }
      public pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata.Builder addReferenceBuilder(
          int index) {
        return getReferenceFieldBuilder().addBuilder(
            index, pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata.getDefaultInstance());
      }
      public java.util.List<pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata.Builder> 
           getReferenceBuilderList() {
        return getReferenceFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilder<
          pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata, pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata.Builder, pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadataOrBuilder> 
          getReferenceFieldBuilder() {
        if (referenceBuilder_ == null) {
          referenceBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
              pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata, pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata.Builder, pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadataOrBuilder>(
                  reference_,
                  ((bitField0_ & 0x00000080) == 0x00000080),
                  getParentForChildren(),
                  isClean());
          reference_ = null;
        }
        return referenceBuilder_;
      }
      
      // repeated string collection = 8;
      private com.google.protobuf.LazyStringList collection_ = com.google.protobuf.LazyStringArrayList.EMPTY;
      private void ensureCollectionIsMutable() {
        if (!((bitField0_ & 0x00000100) == 0x00000100)) {
          collection_ = new com.google.protobuf.LazyStringArrayList(collection_);
          bitField0_ |= 0x00000100;
         }
      }
      public java.util.List<String>
          getCollectionList() {
        return java.util.Collections.unmodifiableList(collection_);
      }
      public int getCollectionCount() {
        return collection_.size();
      }
      public String getCollection(int index) {
        return collection_.get(index);
      }
      public Builder setCollection(
          int index, String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  ensureCollectionIsMutable();
        collection_.set(index, value);
        onChanged();
        return this;
      }
      public Builder addCollection(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  ensureCollectionIsMutable();
        collection_.add(value);
        onChanged();
        return this;
      }
      public Builder addAllCollection(
          java.lang.Iterable<String> values) {
        ensureCollectionIsMutable();
        super.addAll(values, collection_);
        onChanged();
        return this;
      }
      public Builder clearCollection() {
        collection_ = com.google.protobuf.LazyStringArrayList.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000100);
        onChanged();
        return this;
      }
      void addCollection(com.google.protobuf.ByteString value) {
        ensureCollectionIsMutable();
        collection_.add(value);
        onChanged();
      }
      
      // optional string sourcePath = 9;
      private java.lang.Object sourcePath_ = "";
      public boolean hasSourcePath() {
        return ((bitField0_ & 0x00000200) == 0x00000200);
      }
      public String getSourcePath() {
        java.lang.Object ref = sourcePath_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          sourcePath_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setSourcePath(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000200;
        sourcePath_ = value;
        onChanged();
        return this;
      }
      public Builder clearSourcePath() {
        bitField0_ = (bitField0_ & ~0x00000200);
        sourcePath_ = getDefaultInstance().getSourcePath();
        onChanged();
        return this;
      }
      void setSourcePath(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000200;
        sourcePath_ = value;
        onChanged();
      }
      
      // repeated string origKey = 11;
      private com.google.protobuf.LazyStringList origKey_ = com.google.protobuf.LazyStringArrayList.EMPTY;
      private void ensureOrigKeyIsMutable() {
        if (!((bitField0_ & 0x00000400) == 0x00000400)) {
          origKey_ = new com.google.protobuf.LazyStringArrayList(origKey_);
          bitField0_ |= 0x00000400;
         }
      }
      public java.util.List<String>
          getOrigKeyList() {
        return java.util.Collections.unmodifiableList(origKey_);
      }
      public int getOrigKeyCount() {
        return origKey_.size();
      }
      public String getOrigKey(int index) {
        return origKey_.get(index);
      }
      public Builder setOrigKey(
          int index, String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  ensureOrigKeyIsMutable();
        origKey_.set(index, value);
        onChanged();
        return this;
      }
      public Builder addOrigKey(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  ensureOrigKeyIsMutable();
        origKey_.add(value);
        onChanged();
        return this;
      }
      public Builder addAllOrigKey(
          java.lang.Iterable<String> values) {
        ensureOrigKeyIsMutable();
        super.addAll(values, origKey_);
        onChanged();
        return this;
      }
      public Builder clearOrigKey() {
        origKey_ = com.google.protobuf.LazyStringArrayList.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000400);
        onChanged();
        return this;
      }
      void addOrigKey(com.google.protobuf.ByteString value) {
        ensureOrigKeyIsMutable();
        origKey_.add(value);
        onChanged();
      }
      
      // @@protoc_insertion_point(builder_scope:DocumentMetadata)
    }
    
    static {
      defaultInstance = new DocumentMetadata(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:DocumentMetadata)
  }
  
  public interface ReferenceMetadataOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // required .BasicMetadata basicMetadata = 1;
    boolean hasBasicMetadata();
    pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata getBasicMetadata();
    pl.edu.icm.coansys.models.DocumentProtos.BasicMetadataOrBuilder getBasicMetadataOrBuilder();
    
    // optional string sourceDocKey = 2;
    boolean hasSourceDocKey();
    String getSourceDocKey();
    
    // optional int32 position = 3;
    boolean hasPosition();
    int getPosition();
    
    // optional string rawCitationText = 4;
    boolean hasRawCitationText();
    String getRawCitationText();
    
    // repeated .KeyValue extId = 5;
    java.util.List<pl.edu.icm.coansys.models.DocumentProtos.KeyValue> 
        getExtIdList();
    pl.edu.icm.coansys.models.DocumentProtos.KeyValue getExtId(int index);
    int getExtIdCount();
    java.util.List<? extends pl.edu.icm.coansys.models.DocumentProtos.KeyValueOrBuilder> 
        getExtIdOrBuilderList();
    pl.edu.icm.coansys.models.DocumentProtos.KeyValueOrBuilder getExtIdOrBuilder(
        int index);
  }
  public static final class ReferenceMetadata extends
      com.google.protobuf.GeneratedMessage
      implements ReferenceMetadataOrBuilder {
    // Use ReferenceMetadata.newBuilder() to construct.
    private ReferenceMetadata(Builder builder) {
      super(builder);
    }
    private ReferenceMetadata(boolean noInit) {}
    
    private static final ReferenceMetadata defaultInstance;
    public static ReferenceMetadata getDefaultInstance() {
      return defaultInstance;
    }
    
    public ReferenceMetadata getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return pl.edu.icm.coansys.models.DocumentProtos.internal_static_ReferenceMetadata_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return pl.edu.icm.coansys.models.DocumentProtos.internal_static_ReferenceMetadata_fieldAccessorTable;
    }
    
    private int bitField0_;
    // required .BasicMetadata basicMetadata = 1;
    public static final int BASICMETADATA_FIELD_NUMBER = 1;
    private pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata basicMetadata_;
    public boolean hasBasicMetadata() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    public pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata getBasicMetadata() {
      return basicMetadata_;
    }
    public pl.edu.icm.coansys.models.DocumentProtos.BasicMetadataOrBuilder getBasicMetadataOrBuilder() {
      return basicMetadata_;
    }
    
    // optional string sourceDocKey = 2;
    public static final int SOURCEDOCKEY_FIELD_NUMBER = 2;
    private java.lang.Object sourceDocKey_;
    public boolean hasSourceDocKey() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    public String getSourceDocKey() {
      java.lang.Object ref = sourceDocKey_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          sourceDocKey_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getSourceDocKeyBytes() {
      java.lang.Object ref = sourceDocKey_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        sourceDocKey_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // optional int32 position = 3;
    public static final int POSITION_FIELD_NUMBER = 3;
    private int position_;
    public boolean hasPosition() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    public int getPosition() {
      return position_;
    }
    
    // optional string rawCitationText = 4;
    public static final int RAWCITATIONTEXT_FIELD_NUMBER = 4;
    private java.lang.Object rawCitationText_;
    public boolean hasRawCitationText() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    public String getRawCitationText() {
      java.lang.Object ref = rawCitationText_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          rawCitationText_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getRawCitationTextBytes() {
      java.lang.Object ref = rawCitationText_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        rawCitationText_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // repeated .KeyValue extId = 5;
    public static final int EXTID_FIELD_NUMBER = 5;
    private java.util.List<pl.edu.icm.coansys.models.DocumentProtos.KeyValue> extId_;
    public java.util.List<pl.edu.icm.coansys.models.DocumentProtos.KeyValue> getExtIdList() {
      return extId_;
    }
    public java.util.List<? extends pl.edu.icm.coansys.models.DocumentProtos.KeyValueOrBuilder> 
        getExtIdOrBuilderList() {
      return extId_;
    }
    public int getExtIdCount() {
      return extId_.size();
    }
    public pl.edu.icm.coansys.models.DocumentProtos.KeyValue getExtId(int index) {
      return extId_.get(index);
    }
    public pl.edu.icm.coansys.models.DocumentProtos.KeyValueOrBuilder getExtIdOrBuilder(
        int index) {
      return extId_.get(index);
    }
    
    private void initFields() {
      basicMetadata_ = pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata.getDefaultInstance();
      sourceDocKey_ = "";
      position_ = 0;
      rawCitationText_ = "";
      extId_ = java.util.Collections.emptyList();
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
      if (!hasBasicMetadata()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!getBasicMetadata().isInitialized()) {
        memoizedIsInitialized = 0;
        return false;
      }
      for (int i = 0; i < getExtIdCount(); i++) {
        if (!getExtId(i).isInitialized()) {
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
        output.writeMessage(1, basicMetadata_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeBytes(2, getSourceDocKeyBytes());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeInt32(3, position_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeBytes(4, getRawCitationTextBytes());
      }
      for (int i = 0; i < extId_.size(); i++) {
        output.writeMessage(5, extId_.get(i));
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
          .computeMessageSize(1, basicMetadata_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(2, getSourceDocKeyBytes());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(3, position_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(4, getRawCitationTextBytes());
      }
      for (int i = 0; i < extId_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(5, extId_.get(i));
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
    
    public static pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata parseDelimitedFrom(
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
    public static pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata prototype) {
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
       implements pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadataOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return pl.edu.icm.coansys.models.DocumentProtos.internal_static_ReferenceMetadata_descriptor;
      }
      
      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return pl.edu.icm.coansys.models.DocumentProtos.internal_static_ReferenceMetadata_fieldAccessorTable;
      }
      
      // Construct using pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }
      
      private Builder(BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getBasicMetadataFieldBuilder();
          getExtIdFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }
      
      public Builder clear() {
        super.clear();
        if (basicMetadataBuilder_ == null) {
          basicMetadata_ = pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata.getDefaultInstance();
        } else {
          basicMetadataBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000001);
        sourceDocKey_ = "";
        bitField0_ = (bitField0_ & ~0x00000002);
        position_ = 0;
        bitField0_ = (bitField0_ & ~0x00000004);
        rawCitationText_ = "";
        bitField0_ = (bitField0_ & ~0x00000008);
        if (extIdBuilder_ == null) {
          extId_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000010);
        } else {
          extIdBuilder_.clear();
        }
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata.getDescriptor();
      }
      
      public pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata getDefaultInstanceForType() {
        return pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata.getDefaultInstance();
      }
      
      public pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata build() {
        pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata buildPartial() {
        pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata result = new pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        if (basicMetadataBuilder_ == null) {
          result.basicMetadata_ = basicMetadata_;
        } else {
          result.basicMetadata_ = basicMetadataBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.sourceDocKey_ = sourceDocKey_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.position_ = position_;
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000008;
        }
        result.rawCitationText_ = rawCitationText_;
        if (extIdBuilder_ == null) {
          if (((bitField0_ & 0x00000010) == 0x00000010)) {
            extId_ = java.util.Collections.unmodifiableList(extId_);
            bitField0_ = (bitField0_ & ~0x00000010);
          }
          result.extId_ = extId_;
        } else {
          result.extId_ = extIdBuilder_.build();
        }
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata) {
          return mergeFrom((pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata other) {
        if (other == pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata.getDefaultInstance()) return this;
        if (other.hasBasicMetadata()) {
          mergeBasicMetadata(other.getBasicMetadata());
        }
        if (other.hasSourceDocKey()) {
          setSourceDocKey(other.getSourceDocKey());
        }
        if (other.hasPosition()) {
          setPosition(other.getPosition());
        }
        if (other.hasRawCitationText()) {
          setRawCitationText(other.getRawCitationText());
        }
        if (extIdBuilder_ == null) {
          if (!other.extId_.isEmpty()) {
            if (extId_.isEmpty()) {
              extId_ = other.extId_;
              bitField0_ = (bitField0_ & ~0x00000010);
            } else {
              ensureExtIdIsMutable();
              extId_.addAll(other.extId_);
            }
            onChanged();
          }
        } else {
          if (!other.extId_.isEmpty()) {
            if (extIdBuilder_.isEmpty()) {
              extIdBuilder_.dispose();
              extIdBuilder_ = null;
              extId_ = other.extId_;
              bitField0_ = (bitField0_ & ~0x00000010);
              extIdBuilder_ = 
                com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                   getExtIdFieldBuilder() : null;
            } else {
              extIdBuilder_.addAllMessages(other.extId_);
            }
          }
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
        if (!hasBasicMetadata()) {
          
          return false;
        }
        if (!getBasicMetadata().isInitialized()) {
          
          return false;
        }
        for (int i = 0; i < getExtIdCount(); i++) {
          if (!getExtId(i).isInitialized()) {
            
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
              pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata.Builder subBuilder = pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata.newBuilder();
              if (hasBasicMetadata()) {
                subBuilder.mergeFrom(getBasicMetadata());
              }
              input.readMessage(subBuilder, extensionRegistry);
              setBasicMetadata(subBuilder.buildPartial());
              break;
            }
            case 18: {
              bitField0_ |= 0x00000002;
              sourceDocKey_ = input.readBytes();
              break;
            }
            case 24: {
              bitField0_ |= 0x00000004;
              position_ = input.readInt32();
              break;
            }
            case 34: {
              bitField0_ |= 0x00000008;
              rawCitationText_ = input.readBytes();
              break;
            }
            case 42: {
              pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder subBuilder = pl.edu.icm.coansys.models.DocumentProtos.KeyValue.newBuilder();
              input.readMessage(subBuilder, extensionRegistry);
              addExtId(subBuilder.buildPartial());
              break;
            }
          }
        }
      }
      
      private int bitField0_;
      
      // required .BasicMetadata basicMetadata = 1;
      private pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata basicMetadata_ = pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata, pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata.Builder, pl.edu.icm.coansys.models.DocumentProtos.BasicMetadataOrBuilder> basicMetadataBuilder_;
      public boolean hasBasicMetadata() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      public pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata getBasicMetadata() {
        if (basicMetadataBuilder_ == null) {
          return basicMetadata_;
        } else {
          return basicMetadataBuilder_.getMessage();
        }
      }
      public Builder setBasicMetadata(pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata value) {
        if (basicMetadataBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          basicMetadata_ = value;
          onChanged();
        } else {
          basicMetadataBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      public Builder setBasicMetadata(
          pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata.Builder builderForValue) {
        if (basicMetadataBuilder_ == null) {
          basicMetadata_ = builderForValue.build();
          onChanged();
        } else {
          basicMetadataBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      public Builder mergeBasicMetadata(pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata value) {
        if (basicMetadataBuilder_ == null) {
          if (((bitField0_ & 0x00000001) == 0x00000001) &&
              basicMetadata_ != pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata.getDefaultInstance()) {
            basicMetadata_ =
              pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata.newBuilder(basicMetadata_).mergeFrom(value).buildPartial();
          } else {
            basicMetadata_ = value;
          }
          onChanged();
        } else {
          basicMetadataBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      public Builder clearBasicMetadata() {
        if (basicMetadataBuilder_ == null) {
          basicMetadata_ = pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata.getDefaultInstance();
          onChanged();
        } else {
          basicMetadataBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000001);
        return this;
      }
      public pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata.Builder getBasicMetadataBuilder() {
        bitField0_ |= 0x00000001;
        onChanged();
        return getBasicMetadataFieldBuilder().getBuilder();
      }
      public pl.edu.icm.coansys.models.DocumentProtos.BasicMetadataOrBuilder getBasicMetadataOrBuilder() {
        if (basicMetadataBuilder_ != null) {
          return basicMetadataBuilder_.getMessageOrBuilder();
        } else {
          return basicMetadata_;
        }
      }
      private com.google.protobuf.SingleFieldBuilder<
          pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata, pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata.Builder, pl.edu.icm.coansys.models.DocumentProtos.BasicMetadataOrBuilder> 
          getBasicMetadataFieldBuilder() {
        if (basicMetadataBuilder_ == null) {
          basicMetadataBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata, pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata.Builder, pl.edu.icm.coansys.models.DocumentProtos.BasicMetadataOrBuilder>(
                  basicMetadata_,
                  getParentForChildren(),
                  isClean());
          basicMetadata_ = null;
        }
        return basicMetadataBuilder_;
      }
      
      // optional string sourceDocKey = 2;
      private java.lang.Object sourceDocKey_ = "";
      public boolean hasSourceDocKey() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      public String getSourceDocKey() {
        java.lang.Object ref = sourceDocKey_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          sourceDocKey_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setSourceDocKey(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        sourceDocKey_ = value;
        onChanged();
        return this;
      }
      public Builder clearSourceDocKey() {
        bitField0_ = (bitField0_ & ~0x00000002);
        sourceDocKey_ = getDefaultInstance().getSourceDocKey();
        onChanged();
        return this;
      }
      void setSourceDocKey(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000002;
        sourceDocKey_ = value;
        onChanged();
      }
      
      // optional int32 position = 3;
      private int position_ ;
      public boolean hasPosition() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      public int getPosition() {
        return position_;
      }
      public Builder setPosition(int value) {
        bitField0_ |= 0x00000004;
        position_ = value;
        onChanged();
        return this;
      }
      public Builder clearPosition() {
        bitField0_ = (bitField0_ & ~0x00000004);
        position_ = 0;
        onChanged();
        return this;
      }
      
      // optional string rawCitationText = 4;
      private java.lang.Object rawCitationText_ = "";
      public boolean hasRawCitationText() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      public String getRawCitationText() {
        java.lang.Object ref = rawCitationText_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          rawCitationText_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setRawCitationText(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000008;
        rawCitationText_ = value;
        onChanged();
        return this;
      }
      public Builder clearRawCitationText() {
        bitField0_ = (bitField0_ & ~0x00000008);
        rawCitationText_ = getDefaultInstance().getRawCitationText();
        onChanged();
        return this;
      }
      void setRawCitationText(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000008;
        rawCitationText_ = value;
        onChanged();
      }
      
      // repeated .KeyValue extId = 5;
      private java.util.List<pl.edu.icm.coansys.models.DocumentProtos.KeyValue> extId_ =
        java.util.Collections.emptyList();
      private void ensureExtIdIsMutable() {
        if (!((bitField0_ & 0x00000010) == 0x00000010)) {
          extId_ = new java.util.ArrayList<pl.edu.icm.coansys.models.DocumentProtos.KeyValue>(extId_);
          bitField0_ |= 0x00000010;
         }
      }
      
      private com.google.protobuf.RepeatedFieldBuilder<
          pl.edu.icm.coansys.models.DocumentProtos.KeyValue, pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder, pl.edu.icm.coansys.models.DocumentProtos.KeyValueOrBuilder> extIdBuilder_;
      
      public java.util.List<pl.edu.icm.coansys.models.DocumentProtos.KeyValue> getExtIdList() {
        if (extIdBuilder_ == null) {
          return java.util.Collections.unmodifiableList(extId_);
        } else {
          return extIdBuilder_.getMessageList();
        }
      }
      public int getExtIdCount() {
        if (extIdBuilder_ == null) {
          return extId_.size();
        } else {
          return extIdBuilder_.getCount();
        }
      }
      public pl.edu.icm.coansys.models.DocumentProtos.KeyValue getExtId(int index) {
        if (extIdBuilder_ == null) {
          return extId_.get(index);
        } else {
          return extIdBuilder_.getMessage(index);
        }
      }
      public Builder setExtId(
          int index, pl.edu.icm.coansys.models.DocumentProtos.KeyValue value) {
        if (extIdBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureExtIdIsMutable();
          extId_.set(index, value);
          onChanged();
        } else {
          extIdBuilder_.setMessage(index, value);
        }
        return this;
      }
      public Builder setExtId(
          int index, pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder builderForValue) {
        if (extIdBuilder_ == null) {
          ensureExtIdIsMutable();
          extId_.set(index, builderForValue.build());
          onChanged();
        } else {
          extIdBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }
      public Builder addExtId(pl.edu.icm.coansys.models.DocumentProtos.KeyValue value) {
        if (extIdBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureExtIdIsMutable();
          extId_.add(value);
          onChanged();
        } else {
          extIdBuilder_.addMessage(value);
        }
        return this;
      }
      public Builder addExtId(
          int index, pl.edu.icm.coansys.models.DocumentProtos.KeyValue value) {
        if (extIdBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureExtIdIsMutable();
          extId_.add(index, value);
          onChanged();
        } else {
          extIdBuilder_.addMessage(index, value);
        }
        return this;
      }
      public Builder addExtId(
          pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder builderForValue) {
        if (extIdBuilder_ == null) {
          ensureExtIdIsMutable();
          extId_.add(builderForValue.build());
          onChanged();
        } else {
          extIdBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }
      public Builder addExtId(
          int index, pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder builderForValue) {
        if (extIdBuilder_ == null) {
          ensureExtIdIsMutable();
          extId_.add(index, builderForValue.build());
          onChanged();
        } else {
          extIdBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }
      public Builder addAllExtId(
          java.lang.Iterable<? extends pl.edu.icm.coansys.models.DocumentProtos.KeyValue> values) {
        if (extIdBuilder_ == null) {
          ensureExtIdIsMutable();
          super.addAll(values, extId_);
          onChanged();
        } else {
          extIdBuilder_.addAllMessages(values);
        }
        return this;
      }
      public Builder clearExtId() {
        if (extIdBuilder_ == null) {
          extId_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000010);
          onChanged();
        } else {
          extIdBuilder_.clear();
        }
        return this;
      }
      public Builder removeExtId(int index) {
        if (extIdBuilder_ == null) {
          ensureExtIdIsMutable();
          extId_.remove(index);
          onChanged();
        } else {
          extIdBuilder_.remove(index);
        }
        return this;
      }
      public pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder getExtIdBuilder(
          int index) {
        return getExtIdFieldBuilder().getBuilder(index);
      }
      public pl.edu.icm.coansys.models.DocumentProtos.KeyValueOrBuilder getExtIdOrBuilder(
          int index) {
        if (extIdBuilder_ == null) {
          return extId_.get(index);  } else {
          return extIdBuilder_.getMessageOrBuilder(index);
        }
      }
      public java.util.List<? extends pl.edu.icm.coansys.models.DocumentProtos.KeyValueOrBuilder> 
           getExtIdOrBuilderList() {
        if (extIdBuilder_ != null) {
          return extIdBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(extId_);
        }
      }
      public pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder addExtIdBuilder() {
        return getExtIdFieldBuilder().addBuilder(
            pl.edu.icm.coansys.models.DocumentProtos.KeyValue.getDefaultInstance());
      }
      public pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder addExtIdBuilder(
          int index) {
        return getExtIdFieldBuilder().addBuilder(
            index, pl.edu.icm.coansys.models.DocumentProtos.KeyValue.getDefaultInstance());
      }
      public java.util.List<pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder> 
           getExtIdBuilderList() {
        return getExtIdFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilder<
          pl.edu.icm.coansys.models.DocumentProtos.KeyValue, pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder, pl.edu.icm.coansys.models.DocumentProtos.KeyValueOrBuilder> 
          getExtIdFieldBuilder() {
        if (extIdBuilder_ == null) {
          extIdBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
              pl.edu.icm.coansys.models.DocumentProtos.KeyValue, pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder, pl.edu.icm.coansys.models.DocumentProtos.KeyValueOrBuilder>(
                  extId_,
                  ((bitField0_ & 0x00000010) == 0x00000010),
                  getParentForChildren(),
                  isClean());
          extId_ = null;
        }
        return extIdBuilder_;
      }
      
      // @@protoc_insertion_point(builder_scope:ReferenceMetadata)
    }
    
    static {
      defaultInstance = new ReferenceMetadata(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:ReferenceMetadata)
  }
  
  public interface MediaOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // required string key = 1;
    boolean hasKey();
    String getKey();
    
    // required string mediaType = 2;
    boolean hasMediaType();
    String getMediaType();
    
    // required bytes content = 3;
    boolean hasContent();
    com.google.protobuf.ByteString getContent();
    
    // optional string collection = 4;
    boolean hasCollection();
    String getCollection();
    
    // optional string sourcePath = 5;
    boolean hasSourcePath();
    String getSourcePath();
    
    // optional int64 sourceFilesize = 6;
    boolean hasSourceFilesize();
    long getSourceFilesize();
    
    // optional string destinationPath = 7;
    boolean hasDestinationPath();
    String getDestinationPath();
    
    // optional .ProvenanceInfo provenance = 8;
    boolean hasProvenance();
    pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo getProvenance();
    pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfoOrBuilder getProvenanceOrBuilder();
  }
  public static final class Media extends
      com.google.protobuf.GeneratedMessage
      implements MediaOrBuilder {
    // Use Media.newBuilder() to construct.
    private Media(Builder builder) {
      super(builder);
    }
    private Media(boolean noInit) {}
    
    private static final Media defaultInstance;
    public static Media getDefaultInstance() {
      return defaultInstance;
    }
    
    public Media getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return pl.edu.icm.coansys.models.DocumentProtos.internal_static_Media_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return pl.edu.icm.coansys.models.DocumentProtos.internal_static_Media_fieldAccessorTable;
    }
    
    private int bitField0_;
    // required string key = 1;
    public static final int KEY_FIELD_NUMBER = 1;
    private java.lang.Object key_;
    public boolean hasKey() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    public String getKey() {
      java.lang.Object ref = key_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          key_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getKeyBytes() {
      java.lang.Object ref = key_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        key_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // required string mediaType = 2;
    public static final int MEDIATYPE_FIELD_NUMBER = 2;
    private java.lang.Object mediaType_;
    public boolean hasMediaType() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    public String getMediaType() {
      java.lang.Object ref = mediaType_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          mediaType_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getMediaTypeBytes() {
      java.lang.Object ref = mediaType_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        mediaType_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // required bytes content = 3;
    public static final int CONTENT_FIELD_NUMBER = 3;
    private com.google.protobuf.ByteString content_;
    public boolean hasContent() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    public com.google.protobuf.ByteString getContent() {
      return content_;
    }
    
    // optional string collection = 4;
    public static final int COLLECTION_FIELD_NUMBER = 4;
    private java.lang.Object collection_;
    public boolean hasCollection() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    public String getCollection() {
      java.lang.Object ref = collection_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          collection_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getCollectionBytes() {
      java.lang.Object ref = collection_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        collection_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // optional string sourcePath = 5;
    public static final int SOURCEPATH_FIELD_NUMBER = 5;
    private java.lang.Object sourcePath_;
    public boolean hasSourcePath() {
      return ((bitField0_ & 0x00000010) == 0x00000010);
    }
    public String getSourcePath() {
      java.lang.Object ref = sourcePath_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          sourcePath_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getSourcePathBytes() {
      java.lang.Object ref = sourcePath_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        sourcePath_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // optional int64 sourceFilesize = 6;
    public static final int SOURCEFILESIZE_FIELD_NUMBER = 6;
    private long sourceFilesize_;
    public boolean hasSourceFilesize() {
      return ((bitField0_ & 0x00000020) == 0x00000020);
    }
    public long getSourceFilesize() {
      return sourceFilesize_;
    }
    
    // optional string destinationPath = 7;
    public static final int DESTINATIONPATH_FIELD_NUMBER = 7;
    private java.lang.Object destinationPath_;
    public boolean hasDestinationPath() {
      return ((bitField0_ & 0x00000040) == 0x00000040);
    }
    public String getDestinationPath() {
      java.lang.Object ref = destinationPath_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          destinationPath_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getDestinationPathBytes() {
      java.lang.Object ref = destinationPath_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        destinationPath_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // optional .ProvenanceInfo provenance = 8;
    public static final int PROVENANCE_FIELD_NUMBER = 8;
    private pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo provenance_;
    public boolean hasProvenance() {
      return ((bitField0_ & 0x00000080) == 0x00000080);
    }
    public pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo getProvenance() {
      return provenance_;
    }
    public pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfoOrBuilder getProvenanceOrBuilder() {
      return provenance_;
    }
    
    private void initFields() {
      key_ = "";
      mediaType_ = "";
      content_ = com.google.protobuf.ByteString.EMPTY;
      collection_ = "";
      sourcePath_ = "";
      sourceFilesize_ = 0L;
      destinationPath_ = "";
      provenance_ = pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.getDefaultInstance();
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
      if (!hasKey()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasMediaType()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasContent()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (hasProvenance()) {
        if (!getProvenance().isInitialized()) {
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
        output.writeBytes(1, getKeyBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeBytes(2, getMediaTypeBytes());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeBytes(3, content_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeBytes(4, getCollectionBytes());
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        output.writeBytes(5, getSourcePathBytes());
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        output.writeInt64(6, sourceFilesize_);
      }
      if (((bitField0_ & 0x00000040) == 0x00000040)) {
        output.writeBytes(7, getDestinationPathBytes());
      }
      if (((bitField0_ & 0x00000080) == 0x00000080)) {
        output.writeMessage(8, provenance_);
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
          .computeBytesSize(1, getKeyBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(2, getMediaTypeBytes());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(3, content_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(4, getCollectionBytes());
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(5, getSourcePathBytes());
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt64Size(6, sourceFilesize_);
      }
      if (((bitField0_ & 0x00000040) == 0x00000040)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(7, getDestinationPathBytes());
      }
      if (((bitField0_ & 0x00000080) == 0x00000080)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(8, provenance_);
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
    
    public static pl.edu.icm.coansys.models.DocumentProtos.Media parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.Media parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.Media parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.Media parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.Media parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.Media parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.Media parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.Media parseDelimitedFrom(
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
    public static pl.edu.icm.coansys.models.DocumentProtos.Media parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.Media parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(pl.edu.icm.coansys.models.DocumentProtos.Media prototype) {
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
       implements pl.edu.icm.coansys.models.DocumentProtos.MediaOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return pl.edu.icm.coansys.models.DocumentProtos.internal_static_Media_descriptor;
      }
      
      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return pl.edu.icm.coansys.models.DocumentProtos.internal_static_Media_fieldAccessorTable;
      }
      
      // Construct using pl.edu.icm.coansys.models.DocumentProtos.Media.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }
      
      private Builder(BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getProvenanceFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }
      
      public Builder clear() {
        super.clear();
        key_ = "";
        bitField0_ = (bitField0_ & ~0x00000001);
        mediaType_ = "";
        bitField0_ = (bitField0_ & ~0x00000002);
        content_ = com.google.protobuf.ByteString.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000004);
        collection_ = "";
        bitField0_ = (bitField0_ & ~0x00000008);
        sourcePath_ = "";
        bitField0_ = (bitField0_ & ~0x00000010);
        sourceFilesize_ = 0L;
        bitField0_ = (bitField0_ & ~0x00000020);
        destinationPath_ = "";
        bitField0_ = (bitField0_ & ~0x00000040);
        if (provenanceBuilder_ == null) {
          provenance_ = pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.getDefaultInstance();
        } else {
          provenanceBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000080);
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return pl.edu.icm.coansys.models.DocumentProtos.Media.getDescriptor();
      }
      
      public pl.edu.icm.coansys.models.DocumentProtos.Media getDefaultInstanceForType() {
        return pl.edu.icm.coansys.models.DocumentProtos.Media.getDefaultInstance();
      }
      
      public pl.edu.icm.coansys.models.DocumentProtos.Media build() {
        pl.edu.icm.coansys.models.DocumentProtos.Media result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private pl.edu.icm.coansys.models.DocumentProtos.Media buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        pl.edu.icm.coansys.models.DocumentProtos.Media result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public pl.edu.icm.coansys.models.DocumentProtos.Media buildPartial() {
        pl.edu.icm.coansys.models.DocumentProtos.Media result = new pl.edu.icm.coansys.models.DocumentProtos.Media(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.key_ = key_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.mediaType_ = mediaType_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.content_ = content_;
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000008;
        }
        result.collection_ = collection_;
        if (((from_bitField0_ & 0x00000010) == 0x00000010)) {
          to_bitField0_ |= 0x00000010;
        }
        result.sourcePath_ = sourcePath_;
        if (((from_bitField0_ & 0x00000020) == 0x00000020)) {
          to_bitField0_ |= 0x00000020;
        }
        result.sourceFilesize_ = sourceFilesize_;
        if (((from_bitField0_ & 0x00000040) == 0x00000040)) {
          to_bitField0_ |= 0x00000040;
        }
        result.destinationPath_ = destinationPath_;
        if (((from_bitField0_ & 0x00000080) == 0x00000080)) {
          to_bitField0_ |= 0x00000080;
        }
        if (provenanceBuilder_ == null) {
          result.provenance_ = provenance_;
        } else {
          result.provenance_ = provenanceBuilder_.build();
        }
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof pl.edu.icm.coansys.models.DocumentProtos.Media) {
          return mergeFrom((pl.edu.icm.coansys.models.DocumentProtos.Media)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(pl.edu.icm.coansys.models.DocumentProtos.Media other) {
        if (other == pl.edu.icm.coansys.models.DocumentProtos.Media.getDefaultInstance()) return this;
        if (other.hasKey()) {
          setKey(other.getKey());
        }
        if (other.hasMediaType()) {
          setMediaType(other.getMediaType());
        }
        if (other.hasContent()) {
          setContent(other.getContent());
        }
        if (other.hasCollection()) {
          setCollection(other.getCollection());
        }
        if (other.hasSourcePath()) {
          setSourcePath(other.getSourcePath());
        }
        if (other.hasSourceFilesize()) {
          setSourceFilesize(other.getSourceFilesize());
        }
        if (other.hasDestinationPath()) {
          setDestinationPath(other.getDestinationPath());
        }
        if (other.hasProvenance()) {
          mergeProvenance(other.getProvenance());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
        if (!hasKey()) {
          
          return false;
        }
        if (!hasMediaType()) {
          
          return false;
        }
        if (!hasContent()) {
          
          return false;
        }
        if (hasProvenance()) {
          if (!getProvenance().isInitialized()) {
            
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
              key_ = input.readBytes();
              break;
            }
            case 18: {
              bitField0_ |= 0x00000002;
              mediaType_ = input.readBytes();
              break;
            }
            case 26: {
              bitField0_ |= 0x00000004;
              content_ = input.readBytes();
              break;
            }
            case 34: {
              bitField0_ |= 0x00000008;
              collection_ = input.readBytes();
              break;
            }
            case 42: {
              bitField0_ |= 0x00000010;
              sourcePath_ = input.readBytes();
              break;
            }
            case 48: {
              bitField0_ |= 0x00000020;
              sourceFilesize_ = input.readInt64();
              break;
            }
            case 58: {
              bitField0_ |= 0x00000040;
              destinationPath_ = input.readBytes();
              break;
            }
            case 66: {
              pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.Builder subBuilder = pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.newBuilder();
              if (hasProvenance()) {
                subBuilder.mergeFrom(getProvenance());
              }
              input.readMessage(subBuilder, extensionRegistry);
              setProvenance(subBuilder.buildPartial());
              break;
            }
          }
        }
      }
      
      private int bitField0_;
      
      // required string key = 1;
      private java.lang.Object key_ = "";
      public boolean hasKey() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      public String getKey() {
        java.lang.Object ref = key_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          key_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setKey(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        key_ = value;
        onChanged();
        return this;
      }
      public Builder clearKey() {
        bitField0_ = (bitField0_ & ~0x00000001);
        key_ = getDefaultInstance().getKey();
        onChanged();
        return this;
      }
      void setKey(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000001;
        key_ = value;
        onChanged();
      }
      
      // required string mediaType = 2;
      private java.lang.Object mediaType_ = "";
      public boolean hasMediaType() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      public String getMediaType() {
        java.lang.Object ref = mediaType_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          mediaType_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setMediaType(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        mediaType_ = value;
        onChanged();
        return this;
      }
      public Builder clearMediaType() {
        bitField0_ = (bitField0_ & ~0x00000002);
        mediaType_ = getDefaultInstance().getMediaType();
        onChanged();
        return this;
      }
      void setMediaType(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000002;
        mediaType_ = value;
        onChanged();
      }
      
      // required bytes content = 3;
      private com.google.protobuf.ByteString content_ = com.google.protobuf.ByteString.EMPTY;
      public boolean hasContent() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      public com.google.protobuf.ByteString getContent() {
        return content_;
      }
      public Builder setContent(com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000004;
        content_ = value;
        onChanged();
        return this;
      }
      public Builder clearContent() {
        bitField0_ = (bitField0_ & ~0x00000004);
        content_ = getDefaultInstance().getContent();
        onChanged();
        return this;
      }
      
      // optional string collection = 4;
      private java.lang.Object collection_ = "";
      public boolean hasCollection() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      public String getCollection() {
        java.lang.Object ref = collection_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          collection_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setCollection(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000008;
        collection_ = value;
        onChanged();
        return this;
      }
      public Builder clearCollection() {
        bitField0_ = (bitField0_ & ~0x00000008);
        collection_ = getDefaultInstance().getCollection();
        onChanged();
        return this;
      }
      void setCollection(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000008;
        collection_ = value;
        onChanged();
      }
      
      // optional string sourcePath = 5;
      private java.lang.Object sourcePath_ = "";
      public boolean hasSourcePath() {
        return ((bitField0_ & 0x00000010) == 0x00000010);
      }
      public String getSourcePath() {
        java.lang.Object ref = sourcePath_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          sourcePath_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setSourcePath(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000010;
        sourcePath_ = value;
        onChanged();
        return this;
      }
      public Builder clearSourcePath() {
        bitField0_ = (bitField0_ & ~0x00000010);
        sourcePath_ = getDefaultInstance().getSourcePath();
        onChanged();
        return this;
      }
      void setSourcePath(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000010;
        sourcePath_ = value;
        onChanged();
      }
      
      // optional int64 sourceFilesize = 6;
      private long sourceFilesize_ ;
      public boolean hasSourceFilesize() {
        return ((bitField0_ & 0x00000020) == 0x00000020);
      }
      public long getSourceFilesize() {
        return sourceFilesize_;
      }
      public Builder setSourceFilesize(long value) {
        bitField0_ |= 0x00000020;
        sourceFilesize_ = value;
        onChanged();
        return this;
      }
      public Builder clearSourceFilesize() {
        bitField0_ = (bitField0_ & ~0x00000020);
        sourceFilesize_ = 0L;
        onChanged();
        return this;
      }
      
      // optional string destinationPath = 7;
      private java.lang.Object destinationPath_ = "";
      public boolean hasDestinationPath() {
        return ((bitField0_ & 0x00000040) == 0x00000040);
      }
      public String getDestinationPath() {
        java.lang.Object ref = destinationPath_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          destinationPath_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setDestinationPath(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000040;
        destinationPath_ = value;
        onChanged();
        return this;
      }
      public Builder clearDestinationPath() {
        bitField0_ = (bitField0_ & ~0x00000040);
        destinationPath_ = getDefaultInstance().getDestinationPath();
        onChanged();
        return this;
      }
      void setDestinationPath(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000040;
        destinationPath_ = value;
        onChanged();
      }
      
      // optional .ProvenanceInfo provenance = 8;
      private pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo provenance_ = pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo, pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.Builder, pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfoOrBuilder> provenanceBuilder_;
      public boolean hasProvenance() {
        return ((bitField0_ & 0x00000080) == 0x00000080);
      }
      public pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo getProvenance() {
        if (provenanceBuilder_ == null) {
          return provenance_;
        } else {
          return provenanceBuilder_.getMessage();
        }
      }
      public Builder setProvenance(pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo value) {
        if (provenanceBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          provenance_ = value;
          onChanged();
        } else {
          provenanceBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000080;
        return this;
      }
      public Builder setProvenance(
          pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.Builder builderForValue) {
        if (provenanceBuilder_ == null) {
          provenance_ = builderForValue.build();
          onChanged();
        } else {
          provenanceBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000080;
        return this;
      }
      public Builder mergeProvenance(pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo value) {
        if (provenanceBuilder_ == null) {
          if (((bitField0_ & 0x00000080) == 0x00000080) &&
              provenance_ != pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.getDefaultInstance()) {
            provenance_ =
              pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.newBuilder(provenance_).mergeFrom(value).buildPartial();
          } else {
            provenance_ = value;
          }
          onChanged();
        } else {
          provenanceBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000080;
        return this;
      }
      public Builder clearProvenance() {
        if (provenanceBuilder_ == null) {
          provenance_ = pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.getDefaultInstance();
          onChanged();
        } else {
          provenanceBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000080);
        return this;
      }
      public pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.Builder getProvenanceBuilder() {
        bitField0_ |= 0x00000080;
        onChanged();
        return getProvenanceFieldBuilder().getBuilder();
      }
      public pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfoOrBuilder getProvenanceOrBuilder() {
        if (provenanceBuilder_ != null) {
          return provenanceBuilder_.getMessageOrBuilder();
        } else {
          return provenance_;
        }
      }
      private com.google.protobuf.SingleFieldBuilder<
          pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo, pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.Builder, pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfoOrBuilder> 
          getProvenanceFieldBuilder() {
        if (provenanceBuilder_ == null) {
          provenanceBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo, pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.Builder, pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfoOrBuilder>(
                  provenance_,
                  getParentForChildren(),
                  isClean());
          provenance_ = null;
        }
        return provenanceBuilder_;
      }
      
      // @@protoc_insertion_point(builder_scope:Media)
    }
    
    static {
      defaultInstance = new Media(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:Media)
  }
  
  public interface MediaContainerOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // repeated .Media media = 1;
    java.util.List<pl.edu.icm.coansys.models.DocumentProtos.Media> 
        getMediaList();
    pl.edu.icm.coansys.models.DocumentProtos.Media getMedia(int index);
    int getMediaCount();
    java.util.List<? extends pl.edu.icm.coansys.models.DocumentProtos.MediaOrBuilder> 
        getMediaOrBuilderList();
    pl.edu.icm.coansys.models.DocumentProtos.MediaOrBuilder getMediaOrBuilder(
        int index);
  }
  public static final class MediaContainer extends
      com.google.protobuf.GeneratedMessage
      implements MediaContainerOrBuilder {
    // Use MediaContainer.newBuilder() to construct.
    private MediaContainer(Builder builder) {
      super(builder);
    }
    private MediaContainer(boolean noInit) {}
    
    private static final MediaContainer defaultInstance;
    public static MediaContainer getDefaultInstance() {
      return defaultInstance;
    }
    
    public MediaContainer getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return pl.edu.icm.coansys.models.DocumentProtos.internal_static_MediaContainer_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return pl.edu.icm.coansys.models.DocumentProtos.internal_static_MediaContainer_fieldAccessorTable;
    }
    
    // repeated .Media media = 1;
    public static final int MEDIA_FIELD_NUMBER = 1;
    private java.util.List<pl.edu.icm.coansys.models.DocumentProtos.Media> media_;
    public java.util.List<pl.edu.icm.coansys.models.DocumentProtos.Media> getMediaList() {
      return media_;
    }
    public java.util.List<? extends pl.edu.icm.coansys.models.DocumentProtos.MediaOrBuilder> 
        getMediaOrBuilderList() {
      return media_;
    }
    public int getMediaCount() {
      return media_.size();
    }
    public pl.edu.icm.coansys.models.DocumentProtos.Media getMedia(int index) {
      return media_.get(index);
    }
    public pl.edu.icm.coansys.models.DocumentProtos.MediaOrBuilder getMediaOrBuilder(
        int index) {
      return media_.get(index);
    }
    
    private void initFields() {
      media_ = java.util.Collections.emptyList();
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
      for (int i = 0; i < getMediaCount(); i++) {
        if (!getMedia(i).isInitialized()) {
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
      for (int i = 0; i < media_.size(); i++) {
        output.writeMessage(1, media_.get(i));
      }
      getUnknownFields().writeTo(output);
    }
    
    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;
    
      size = 0;
      for (int i = 0; i < media_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(1, media_.get(i));
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
    
    public static pl.edu.icm.coansys.models.DocumentProtos.MediaContainer parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.MediaContainer parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.MediaContainer parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.MediaContainer parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.MediaContainer parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.MediaContainer parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.MediaContainer parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.MediaContainer parseDelimitedFrom(
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
    public static pl.edu.icm.coansys.models.DocumentProtos.MediaContainer parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.MediaContainer parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(pl.edu.icm.coansys.models.DocumentProtos.MediaContainer prototype) {
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
       implements pl.edu.icm.coansys.models.DocumentProtos.MediaContainerOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return pl.edu.icm.coansys.models.DocumentProtos.internal_static_MediaContainer_descriptor;
      }
      
      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return pl.edu.icm.coansys.models.DocumentProtos.internal_static_MediaContainer_fieldAccessorTable;
      }
      
      // Construct using pl.edu.icm.coansys.models.DocumentProtos.MediaContainer.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }
      
      private Builder(BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getMediaFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }
      
      public Builder clear() {
        super.clear();
        if (mediaBuilder_ == null) {
          media_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000001);
        } else {
          mediaBuilder_.clear();
        }
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return pl.edu.icm.coansys.models.DocumentProtos.MediaContainer.getDescriptor();
      }
      
      public pl.edu.icm.coansys.models.DocumentProtos.MediaContainer getDefaultInstanceForType() {
        return pl.edu.icm.coansys.models.DocumentProtos.MediaContainer.getDefaultInstance();
      }
      
      public pl.edu.icm.coansys.models.DocumentProtos.MediaContainer build() {
        pl.edu.icm.coansys.models.DocumentProtos.MediaContainer result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private pl.edu.icm.coansys.models.DocumentProtos.MediaContainer buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        pl.edu.icm.coansys.models.DocumentProtos.MediaContainer result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public pl.edu.icm.coansys.models.DocumentProtos.MediaContainer buildPartial() {
        pl.edu.icm.coansys.models.DocumentProtos.MediaContainer result = new pl.edu.icm.coansys.models.DocumentProtos.MediaContainer(this);
        int from_bitField0_ = bitField0_;
        if (mediaBuilder_ == null) {
          if (((bitField0_ & 0x00000001) == 0x00000001)) {
            media_ = java.util.Collections.unmodifiableList(media_);
            bitField0_ = (bitField0_ & ~0x00000001);
          }
          result.media_ = media_;
        } else {
          result.media_ = mediaBuilder_.build();
        }
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof pl.edu.icm.coansys.models.DocumentProtos.MediaContainer) {
          return mergeFrom((pl.edu.icm.coansys.models.DocumentProtos.MediaContainer)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(pl.edu.icm.coansys.models.DocumentProtos.MediaContainer other) {
        if (other == pl.edu.icm.coansys.models.DocumentProtos.MediaContainer.getDefaultInstance()) return this;
        if (mediaBuilder_ == null) {
          if (!other.media_.isEmpty()) {
            if (media_.isEmpty()) {
              media_ = other.media_;
              bitField0_ = (bitField0_ & ~0x00000001);
            } else {
              ensureMediaIsMutable();
              media_.addAll(other.media_);
            }
            onChanged();
          }
        } else {
          if (!other.media_.isEmpty()) {
            if (mediaBuilder_.isEmpty()) {
              mediaBuilder_.dispose();
              mediaBuilder_ = null;
              media_ = other.media_;
              bitField0_ = (bitField0_ & ~0x00000001);
              mediaBuilder_ = 
                com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                   getMediaFieldBuilder() : null;
            } else {
              mediaBuilder_.addAllMessages(other.media_);
            }
          }
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
        for (int i = 0; i < getMediaCount(); i++) {
          if (!getMedia(i).isInitialized()) {
            
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
              pl.edu.icm.coansys.models.DocumentProtos.Media.Builder subBuilder = pl.edu.icm.coansys.models.DocumentProtos.Media.newBuilder();
              input.readMessage(subBuilder, extensionRegistry);
              addMedia(subBuilder.buildPartial());
              break;
            }
          }
        }
      }
      
      private int bitField0_;
      
      // repeated .Media media = 1;
      private java.util.List<pl.edu.icm.coansys.models.DocumentProtos.Media> media_ =
        java.util.Collections.emptyList();
      private void ensureMediaIsMutable() {
        if (!((bitField0_ & 0x00000001) == 0x00000001)) {
          media_ = new java.util.ArrayList<pl.edu.icm.coansys.models.DocumentProtos.Media>(media_);
          bitField0_ |= 0x00000001;
         }
      }
      
      private com.google.protobuf.RepeatedFieldBuilder<
          pl.edu.icm.coansys.models.DocumentProtos.Media, pl.edu.icm.coansys.models.DocumentProtos.Media.Builder, pl.edu.icm.coansys.models.DocumentProtos.MediaOrBuilder> mediaBuilder_;
      
      public java.util.List<pl.edu.icm.coansys.models.DocumentProtos.Media> getMediaList() {
        if (mediaBuilder_ == null) {
          return java.util.Collections.unmodifiableList(media_);
        } else {
          return mediaBuilder_.getMessageList();
        }
      }
      public int getMediaCount() {
        if (mediaBuilder_ == null) {
          return media_.size();
        } else {
          return mediaBuilder_.getCount();
        }
      }
      public pl.edu.icm.coansys.models.DocumentProtos.Media getMedia(int index) {
        if (mediaBuilder_ == null) {
          return media_.get(index);
        } else {
          return mediaBuilder_.getMessage(index);
        }
      }
      public Builder setMedia(
          int index, pl.edu.icm.coansys.models.DocumentProtos.Media value) {
        if (mediaBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureMediaIsMutable();
          media_.set(index, value);
          onChanged();
        } else {
          mediaBuilder_.setMessage(index, value);
        }
        return this;
      }
      public Builder setMedia(
          int index, pl.edu.icm.coansys.models.DocumentProtos.Media.Builder builderForValue) {
        if (mediaBuilder_ == null) {
          ensureMediaIsMutable();
          media_.set(index, builderForValue.build());
          onChanged();
        } else {
          mediaBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }
      public Builder addMedia(pl.edu.icm.coansys.models.DocumentProtos.Media value) {
        if (mediaBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureMediaIsMutable();
          media_.add(value);
          onChanged();
        } else {
          mediaBuilder_.addMessage(value);
        }
        return this;
      }
      public Builder addMedia(
          int index, pl.edu.icm.coansys.models.DocumentProtos.Media value) {
        if (mediaBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureMediaIsMutable();
          media_.add(index, value);
          onChanged();
        } else {
          mediaBuilder_.addMessage(index, value);
        }
        return this;
      }
      public Builder addMedia(
          pl.edu.icm.coansys.models.DocumentProtos.Media.Builder builderForValue) {
        if (mediaBuilder_ == null) {
          ensureMediaIsMutable();
          media_.add(builderForValue.build());
          onChanged();
        } else {
          mediaBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }
      public Builder addMedia(
          int index, pl.edu.icm.coansys.models.DocumentProtos.Media.Builder builderForValue) {
        if (mediaBuilder_ == null) {
          ensureMediaIsMutable();
          media_.add(index, builderForValue.build());
          onChanged();
        } else {
          mediaBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }
      public Builder addAllMedia(
          java.lang.Iterable<? extends pl.edu.icm.coansys.models.DocumentProtos.Media> values) {
        if (mediaBuilder_ == null) {
          ensureMediaIsMutable();
          super.addAll(values, media_);
          onChanged();
        } else {
          mediaBuilder_.addAllMessages(values);
        }
        return this;
      }
      public Builder clearMedia() {
        if (mediaBuilder_ == null) {
          media_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000001);
          onChanged();
        } else {
          mediaBuilder_.clear();
        }
        return this;
      }
      public Builder removeMedia(int index) {
        if (mediaBuilder_ == null) {
          ensureMediaIsMutable();
          media_.remove(index);
          onChanged();
        } else {
          mediaBuilder_.remove(index);
        }
        return this;
      }
      public pl.edu.icm.coansys.models.DocumentProtos.Media.Builder getMediaBuilder(
          int index) {
        return getMediaFieldBuilder().getBuilder(index);
      }
      public pl.edu.icm.coansys.models.DocumentProtos.MediaOrBuilder getMediaOrBuilder(
          int index) {
        if (mediaBuilder_ == null) {
          return media_.get(index);  } else {
          return mediaBuilder_.getMessageOrBuilder(index);
        }
      }
      public java.util.List<? extends pl.edu.icm.coansys.models.DocumentProtos.MediaOrBuilder> 
           getMediaOrBuilderList() {
        if (mediaBuilder_ != null) {
          return mediaBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(media_);
        }
      }
      public pl.edu.icm.coansys.models.DocumentProtos.Media.Builder addMediaBuilder() {
        return getMediaFieldBuilder().addBuilder(
            pl.edu.icm.coansys.models.DocumentProtos.Media.getDefaultInstance());
      }
      public pl.edu.icm.coansys.models.DocumentProtos.Media.Builder addMediaBuilder(
          int index) {
        return getMediaFieldBuilder().addBuilder(
            index, pl.edu.icm.coansys.models.DocumentProtos.Media.getDefaultInstance());
      }
      public java.util.List<pl.edu.icm.coansys.models.DocumentProtos.Media.Builder> 
           getMediaBuilderList() {
        return getMediaFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilder<
          pl.edu.icm.coansys.models.DocumentProtos.Media, pl.edu.icm.coansys.models.DocumentProtos.Media.Builder, pl.edu.icm.coansys.models.DocumentProtos.MediaOrBuilder> 
          getMediaFieldBuilder() {
        if (mediaBuilder_ == null) {
          mediaBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
              pl.edu.icm.coansys.models.DocumentProtos.Media, pl.edu.icm.coansys.models.DocumentProtos.Media.Builder, pl.edu.icm.coansys.models.DocumentProtos.MediaOrBuilder>(
                  media_,
                  ((bitField0_ & 0x00000001) == 0x00000001),
                  getParentForChildren(),
                  isClean());
          media_ = null;
        }
        return mediaBuilder_;
      }
      
      // @@protoc_insertion_point(builder_scope:MediaContainer)
    }
    
    static {
      defaultInstance = new MediaContainer(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:MediaContainer)
  }
  
  public interface DocumentWrapperOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // required string rowId = 1;
    boolean hasRowId();
    String getRowId();
    
    // optional .DocumentMetadata documentMetadata = 2;
    boolean hasDocumentMetadata();
    pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata getDocumentMetadata();
    pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadataOrBuilder getDocumentMetadataOrBuilder();
    
    // optional .MediaContainer mediaContainer = 3;
    boolean hasMediaContainer();
    pl.edu.icm.coansys.models.DocumentProtos.MediaContainer getMediaContainer();
    pl.edu.icm.coansys.models.DocumentProtos.MediaContainerOrBuilder getMediaContainerOrBuilder();
  }
  public static final class DocumentWrapper extends
      com.google.protobuf.GeneratedMessage
      implements DocumentWrapperOrBuilder {
    // Use DocumentWrapper.newBuilder() to construct.
    private DocumentWrapper(Builder builder) {
      super(builder);
    }
    private DocumentWrapper(boolean noInit) {}
    
    private static final DocumentWrapper defaultInstance;
    public static DocumentWrapper getDefaultInstance() {
      return defaultInstance;
    }
    
    public DocumentWrapper getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return pl.edu.icm.coansys.models.DocumentProtos.internal_static_DocumentWrapper_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return pl.edu.icm.coansys.models.DocumentProtos.internal_static_DocumentWrapper_fieldAccessorTable;
    }
    
    private int bitField0_;
    // required string rowId = 1;
    public static final int ROWID_FIELD_NUMBER = 1;
    private java.lang.Object rowId_;
    public boolean hasRowId() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    public String getRowId() {
      java.lang.Object ref = rowId_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          rowId_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getRowIdBytes() {
      java.lang.Object ref = rowId_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        rowId_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // optional .DocumentMetadata documentMetadata = 2;
    public static final int DOCUMENTMETADATA_FIELD_NUMBER = 2;
    private pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata documentMetadata_;
    public boolean hasDocumentMetadata() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    public pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata getDocumentMetadata() {
      return documentMetadata_;
    }
    public pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadataOrBuilder getDocumentMetadataOrBuilder() {
      return documentMetadata_;
    }
    
    // optional .MediaContainer mediaContainer = 3;
    public static final int MEDIACONTAINER_FIELD_NUMBER = 3;
    private pl.edu.icm.coansys.models.DocumentProtos.MediaContainer mediaContainer_;
    public boolean hasMediaContainer() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    public pl.edu.icm.coansys.models.DocumentProtos.MediaContainer getMediaContainer() {
      return mediaContainer_;
    }
    public pl.edu.icm.coansys.models.DocumentProtos.MediaContainerOrBuilder getMediaContainerOrBuilder() {
      return mediaContainer_;
    }
    
    private void initFields() {
      rowId_ = "";
      documentMetadata_ = pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata.getDefaultInstance();
      mediaContainer_ = pl.edu.icm.coansys.models.DocumentProtos.MediaContainer.getDefaultInstance();
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
      if (!hasRowId()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (hasDocumentMetadata()) {
        if (!getDocumentMetadata().isInitialized()) {
          memoizedIsInitialized = 0;
          return false;
        }
      }
      if (hasMediaContainer()) {
        if (!getMediaContainer().isInitialized()) {
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
        output.writeBytes(1, getRowIdBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeMessage(2, documentMetadata_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeMessage(3, mediaContainer_);
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
          .computeBytesSize(1, getRowIdBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(2, documentMetadata_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(3, mediaContainer_);
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
    
    public static pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper parseDelimitedFrom(
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
    public static pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper prototype) {
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
       implements pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapperOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return pl.edu.icm.coansys.models.DocumentProtos.internal_static_DocumentWrapper_descriptor;
      }
      
      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return pl.edu.icm.coansys.models.DocumentProtos.internal_static_DocumentWrapper_fieldAccessorTable;
      }
      
      // Construct using pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }
      
      private Builder(BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getDocumentMetadataFieldBuilder();
          getMediaContainerFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }
      
      public Builder clear() {
        super.clear();
        rowId_ = "";
        bitField0_ = (bitField0_ & ~0x00000001);
        if (documentMetadataBuilder_ == null) {
          documentMetadata_ = pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata.getDefaultInstance();
        } else {
          documentMetadataBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000002);
        if (mediaContainerBuilder_ == null) {
          mediaContainer_ = pl.edu.icm.coansys.models.DocumentProtos.MediaContainer.getDefaultInstance();
        } else {
          mediaContainerBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000004);
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper.getDescriptor();
      }
      
      public pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper getDefaultInstanceForType() {
        return pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper.getDefaultInstance();
      }
      
      public pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper build() {
        pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper buildPartial() {
        pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper result = new pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.rowId_ = rowId_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        if (documentMetadataBuilder_ == null) {
          result.documentMetadata_ = documentMetadata_;
        } else {
          result.documentMetadata_ = documentMetadataBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        if (mediaContainerBuilder_ == null) {
          result.mediaContainer_ = mediaContainer_;
        } else {
          result.mediaContainer_ = mediaContainerBuilder_.build();
        }
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper) {
          return mergeFrom((pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper other) {
        if (other == pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper.getDefaultInstance()) return this;
        if (other.hasRowId()) {
          setRowId(other.getRowId());
        }
        if (other.hasDocumentMetadata()) {
          mergeDocumentMetadata(other.getDocumentMetadata());
        }
        if (other.hasMediaContainer()) {
          mergeMediaContainer(other.getMediaContainer());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
        if (!hasRowId()) {
          
          return false;
        }
        if (hasDocumentMetadata()) {
          if (!getDocumentMetadata().isInitialized()) {
            
            return false;
          }
        }
        if (hasMediaContainer()) {
          if (!getMediaContainer().isInitialized()) {
            
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
              rowId_ = input.readBytes();
              break;
            }
            case 18: {
              pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata.Builder subBuilder = pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata.newBuilder();
              if (hasDocumentMetadata()) {
                subBuilder.mergeFrom(getDocumentMetadata());
              }
              input.readMessage(subBuilder, extensionRegistry);
              setDocumentMetadata(subBuilder.buildPartial());
              break;
            }
            case 26: {
              pl.edu.icm.coansys.models.DocumentProtos.MediaContainer.Builder subBuilder = pl.edu.icm.coansys.models.DocumentProtos.MediaContainer.newBuilder();
              if (hasMediaContainer()) {
                subBuilder.mergeFrom(getMediaContainer());
              }
              input.readMessage(subBuilder, extensionRegistry);
              setMediaContainer(subBuilder.buildPartial());
              break;
            }
          }
        }
      }
      
      private int bitField0_;
      
      // required string rowId = 1;
      private java.lang.Object rowId_ = "";
      public boolean hasRowId() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      public String getRowId() {
        java.lang.Object ref = rowId_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          rowId_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setRowId(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        rowId_ = value;
        onChanged();
        return this;
      }
      public Builder clearRowId() {
        bitField0_ = (bitField0_ & ~0x00000001);
        rowId_ = getDefaultInstance().getRowId();
        onChanged();
        return this;
      }
      void setRowId(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000001;
        rowId_ = value;
        onChanged();
      }
      
      // optional .DocumentMetadata documentMetadata = 2;
      private pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata documentMetadata_ = pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata, pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata.Builder, pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadataOrBuilder> documentMetadataBuilder_;
      public boolean hasDocumentMetadata() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      public pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata getDocumentMetadata() {
        if (documentMetadataBuilder_ == null) {
          return documentMetadata_;
        } else {
          return documentMetadataBuilder_.getMessage();
        }
      }
      public Builder setDocumentMetadata(pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata value) {
        if (documentMetadataBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          documentMetadata_ = value;
          onChanged();
        } else {
          documentMetadataBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000002;
        return this;
      }
      public Builder setDocumentMetadata(
          pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata.Builder builderForValue) {
        if (documentMetadataBuilder_ == null) {
          documentMetadata_ = builderForValue.build();
          onChanged();
        } else {
          documentMetadataBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000002;
        return this;
      }
      public Builder mergeDocumentMetadata(pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata value) {
        if (documentMetadataBuilder_ == null) {
          if (((bitField0_ & 0x00000002) == 0x00000002) &&
              documentMetadata_ != pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata.getDefaultInstance()) {
            documentMetadata_ =
              pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata.newBuilder(documentMetadata_).mergeFrom(value).buildPartial();
          } else {
            documentMetadata_ = value;
          }
          onChanged();
        } else {
          documentMetadataBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000002;
        return this;
      }
      public Builder clearDocumentMetadata() {
        if (documentMetadataBuilder_ == null) {
          documentMetadata_ = pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata.getDefaultInstance();
          onChanged();
        } else {
          documentMetadataBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }
      public pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata.Builder getDocumentMetadataBuilder() {
        bitField0_ |= 0x00000002;
        onChanged();
        return getDocumentMetadataFieldBuilder().getBuilder();
      }
      public pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadataOrBuilder getDocumentMetadataOrBuilder() {
        if (documentMetadataBuilder_ != null) {
          return documentMetadataBuilder_.getMessageOrBuilder();
        } else {
          return documentMetadata_;
        }
      }
      private com.google.protobuf.SingleFieldBuilder<
          pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata, pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata.Builder, pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadataOrBuilder> 
          getDocumentMetadataFieldBuilder() {
        if (documentMetadataBuilder_ == null) {
          documentMetadataBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata, pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata.Builder, pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadataOrBuilder>(
                  documentMetadata_,
                  getParentForChildren(),
                  isClean());
          documentMetadata_ = null;
        }
        return documentMetadataBuilder_;
      }
      
      // optional .MediaContainer mediaContainer = 3;
      private pl.edu.icm.coansys.models.DocumentProtos.MediaContainer mediaContainer_ = pl.edu.icm.coansys.models.DocumentProtos.MediaContainer.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          pl.edu.icm.coansys.models.DocumentProtos.MediaContainer, pl.edu.icm.coansys.models.DocumentProtos.MediaContainer.Builder, pl.edu.icm.coansys.models.DocumentProtos.MediaContainerOrBuilder> mediaContainerBuilder_;
      public boolean hasMediaContainer() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      public pl.edu.icm.coansys.models.DocumentProtos.MediaContainer getMediaContainer() {
        if (mediaContainerBuilder_ == null) {
          return mediaContainer_;
        } else {
          return mediaContainerBuilder_.getMessage();
        }
      }
      public Builder setMediaContainer(pl.edu.icm.coansys.models.DocumentProtos.MediaContainer value) {
        if (mediaContainerBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          mediaContainer_ = value;
          onChanged();
        } else {
          mediaContainerBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000004;
        return this;
      }
      public Builder setMediaContainer(
          pl.edu.icm.coansys.models.DocumentProtos.MediaContainer.Builder builderForValue) {
        if (mediaContainerBuilder_ == null) {
          mediaContainer_ = builderForValue.build();
          onChanged();
        } else {
          mediaContainerBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000004;
        return this;
      }
      public Builder mergeMediaContainer(pl.edu.icm.coansys.models.DocumentProtos.MediaContainer value) {
        if (mediaContainerBuilder_ == null) {
          if (((bitField0_ & 0x00000004) == 0x00000004) &&
              mediaContainer_ != pl.edu.icm.coansys.models.DocumentProtos.MediaContainer.getDefaultInstance()) {
            mediaContainer_ =
              pl.edu.icm.coansys.models.DocumentProtos.MediaContainer.newBuilder(mediaContainer_).mergeFrom(value).buildPartial();
          } else {
            mediaContainer_ = value;
          }
          onChanged();
        } else {
          mediaContainerBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000004;
        return this;
      }
      public Builder clearMediaContainer() {
        if (mediaContainerBuilder_ == null) {
          mediaContainer_ = pl.edu.icm.coansys.models.DocumentProtos.MediaContainer.getDefaultInstance();
          onChanged();
        } else {
          mediaContainerBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000004);
        return this;
      }
      public pl.edu.icm.coansys.models.DocumentProtos.MediaContainer.Builder getMediaContainerBuilder() {
        bitField0_ |= 0x00000004;
        onChanged();
        return getMediaContainerFieldBuilder().getBuilder();
      }
      public pl.edu.icm.coansys.models.DocumentProtos.MediaContainerOrBuilder getMediaContainerOrBuilder() {
        if (mediaContainerBuilder_ != null) {
          return mediaContainerBuilder_.getMessageOrBuilder();
        } else {
          return mediaContainer_;
        }
      }
      private com.google.protobuf.SingleFieldBuilder<
          pl.edu.icm.coansys.models.DocumentProtos.MediaContainer, pl.edu.icm.coansys.models.DocumentProtos.MediaContainer.Builder, pl.edu.icm.coansys.models.DocumentProtos.MediaContainerOrBuilder> 
          getMediaContainerFieldBuilder() {
        if (mediaContainerBuilder_ == null) {
          mediaContainerBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              pl.edu.icm.coansys.models.DocumentProtos.MediaContainer, pl.edu.icm.coansys.models.DocumentProtos.MediaContainer.Builder, pl.edu.icm.coansys.models.DocumentProtos.MediaContainerOrBuilder>(
                  mediaContainer_,
                  getParentForChildren(),
                  isClean());
          mediaContainer_ = null;
        }
        return mediaContainerBuilder_;
      }
      
      // @@protoc_insertion_point(builder_scope:DocumentWrapper)
    }
    
    static {
      defaultInstance = new DocumentWrapper(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:DocumentWrapper)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_ProvenanceInfo_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_ProvenanceInfo_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_ProvenanceInfo_SingleProvenanceInfo_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_ProvenanceInfo_SingleProvenanceInfo_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_KeyValue_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_KeyValue_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_TextWithLanguage_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_TextWithLanguage_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_ClassifCode_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_ClassifCode_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_Affiliation_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_Affiliation_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_Author_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_Author_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_BasicMetadata_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_BasicMetadata_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_KeywordsList_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_KeywordsList_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_DocumentMetadata_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_DocumentMetadata_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_ReferenceMetadata_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_ReferenceMetadata_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_Media_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_Media_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_MediaContainer_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_MediaContainer_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_DocumentWrapper_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_DocumentWrapper_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\tbw2.proto\"\354\001\n\016ProvenanceInfo\022?\n\021curren" +
      "tProvenance\030\001 \002(\0132$.ProvenanceInfo.Singl" +
      "eProvenanceInfo\022A\n\023previousProvenances\030\002" +
      " \003(\0132$.ProvenanceInfo.SingleProvenanceIn" +
      "fo\032V\n\024SingleProvenanceInfo\022 \n\030lastModifi" +
      "cationMarkerId\030\001 \002(\t\022\034\n\024lastModification" +
      "Date\030\002 \002(\003\"\\\n\010KeyValue\022\013\n\003key\030\001 \002(\t\022\r\n\005v" +
      "alue\030\002 \002(\t\022\017\n\007comment\030\003 \001(\t\022#\n\nprovenanc" +
      "e\030\004 \001(\0132\017.ProvenanceInfo\"C\n\020TextWithLang" +
      "uage\022\014\n\004text\030\001 \002(\t\022\020\n\010language\030\002 \001(\t\022\017\n\007",
      "comment\030\003 \001(\t\"Q\n\013ClassifCode\022\016\n\006source\030\001" +
      " \002(\t\022\r\n\005value\030\002 \003(\t\022#\n\nprovenance\030\003 \001(\0132" +
      "\017.ProvenanceInfo\"d\n\013Affiliation\022\013\n\003key\030\001" +
      " \002(\t\022\025\n\raffiliationId\030\002 \002(\t\022\014\n\004text\030\003 \002(" +
      "\t\022#\n\nprovenance\030\004 \001(\0132\017.ProvenanceInfo\"\333" +
      "\001\n\006Author\022\013\n\003key\030\001 \002(\t\022\021\n\tforenames\030\002 \001(" +
      "\t\022\017\n\007surname\030\003 \001(\t\022\014\n\004name\030\004 \001(\t\022\r\n\005emai" +
      "l\030\005 \001(\t\022!\n\016affiliationRef\030\006 \003(\0132\t.KeyVal" +
      "ue\022\r\n\005docId\030\007 \001(\t\022\026\n\016positionNumber\030\010 \001(" +
      "\005\022\030\n\005extId\030\t \003(\0132\t.KeyValue\022\037\n\014auxiliarI",
      "nfo\030\n \003(\0132\t.KeyValue\"\343\001\n\rBasicMetadata\022 " +
      "\n\005title\030\001 \003(\0132\021.TextWithLanguage\022\027\n\006auth" +
      "or\030\002 \003(\0132\007.Author\022\013\n\003doi\030\003 \001(\t\022\017\n\007journa" +
      "l\030\004 \001(\t\022\014\n\004isbn\030\005 \001(\t\022\014\n\004issn\030\006 \001(\t\022\014\n\004y" +
      "ear\030\007 \001(\t\022\r\n\005issue\030\010 \001(\t\022\016\n\006volume\030\t \001(\t" +
      "\022\r\n\005pages\030\n \001(\t\022!\n\013classifCode\030\013 \003(\0132\014.C" +
      "lassifCode\"v\n\014KeywordsList\022\014\n\004type\030\001 \001(\t" +
      "\022\020\n\010language\030\002 \001(\t\022\020\n\010keywords\030\003 \003(\t\022\017\n\007" +
      "comment\030\004 \001(\t\022#\n\nprovenance\030\005 \001(\0132\017.Prov" +
      "enanceInfo\"\323\002\n\020DocumentMetadata\022\013\n\003key\030\001",
      " \002(\t\022%\n\rbasicMetadata\030\002 \002(\0132\016.BasicMetad" +
      "ata\022+\n\020documentAbstract\030\003 \003(\0132\021.TextWith" +
      "Language\022\037\n\010keywords\030\n \003(\0132\r.KeywordsLis" +
      "t\022\030\n\005extId\030\005 \003(\0132\t.KeyValue\022\037\n\014auxiliarI" +
      "nfo\030\006 \003(\0132\t.KeyValue\022\"\n\014affiliations\030\014 \003" +
      "(\0132\014.Affiliation\022%\n\treference\030\007 \003(\0132\022.Re" +
      "ferenceMetadata\022\022\n\ncollection\030\010 \003(\t\022\022\n\ns" +
      "ourcePath\030\t \001(\t\022\017\n\007origKey\030\013 \003(\t\"\225\001\n\021Ref" +
      "erenceMetadata\022%\n\rbasicMetadata\030\001 \002(\0132\016." +
      "BasicMetadata\022\024\n\014sourceDocKey\030\002 \001(\t\022\020\n\010p",
      "osition\030\003 \001(\005\022\027\n\017rawCitationText\030\004 \001(\t\022\030" +
      "\n\005extId\030\005 \003(\0132\t.KeyValue\"\266\001\n\005Media\022\013\n\003ke" +
      "y\030\001 \002(\t\022\021\n\tmediaType\030\002 \002(\t\022\017\n\007content\030\003 " +
      "\002(\014\022\022\n\ncollection\030\004 \001(\t\022\022\n\nsourcePath\030\005 " +
      "\001(\t\022\026\n\016sourceFilesize\030\006 \001(\003\022\027\n\017destinati" +
      "onPath\030\007 \001(\t\022#\n\nprovenance\030\010 \001(\0132\017.Prove" +
      "nanceInfo\"\'\n\016MediaContainer\022\025\n\005media\030\001 \003" +
      "(\0132\006.Media\"v\n\017DocumentWrapper\022\r\n\005rowId\030\001" +
      " \002(\t\022+\n\020documentMetadata\030\002 \001(\0132\021.Documen" +
      "tMetadata\022\'\n\016mediaContainer\030\003 \001(\0132\017.Medi",
      "aContainerB+\n\031pl.edu.icm.coansys.modelsB" +
      "\016DocumentProtos"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_ProvenanceInfo_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_ProvenanceInfo_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_ProvenanceInfo_descriptor,
              new java.lang.String[] { "CurrentProvenance", "PreviousProvenances", },
              pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.class,
              pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.Builder.class);
          internal_static_ProvenanceInfo_SingleProvenanceInfo_descriptor =
            internal_static_ProvenanceInfo_descriptor.getNestedTypes().get(0);
          internal_static_ProvenanceInfo_SingleProvenanceInfo_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_ProvenanceInfo_SingleProvenanceInfo_descriptor,
              new java.lang.String[] { "LastModificationMarkerId", "LastModificationDate", },
              pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo.class,
              pl.edu.icm.coansys.models.DocumentProtos.ProvenanceInfo.SingleProvenanceInfo.Builder.class);
          internal_static_KeyValue_descriptor =
            getDescriptor().getMessageTypes().get(1);
          internal_static_KeyValue_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_KeyValue_descriptor,
              new java.lang.String[] { "Key", "Value", "Comment", "Provenance", },
              pl.edu.icm.coansys.models.DocumentProtos.KeyValue.class,
              pl.edu.icm.coansys.models.DocumentProtos.KeyValue.Builder.class);
          internal_static_TextWithLanguage_descriptor =
            getDescriptor().getMessageTypes().get(2);
          internal_static_TextWithLanguage_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_TextWithLanguage_descriptor,
              new java.lang.String[] { "Text", "Language", "Comment", },
              pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage.class,
              pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage.Builder.class);
          internal_static_ClassifCode_descriptor =
            getDescriptor().getMessageTypes().get(3);
          internal_static_ClassifCode_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_ClassifCode_descriptor,
              new java.lang.String[] { "Source", "Value", "Provenance", },
              pl.edu.icm.coansys.models.DocumentProtos.ClassifCode.class,
              pl.edu.icm.coansys.models.DocumentProtos.ClassifCode.Builder.class);
          internal_static_Affiliation_descriptor =
            getDescriptor().getMessageTypes().get(4);
          internal_static_Affiliation_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_Affiliation_descriptor,
              new java.lang.String[] { "Key", "AffiliationId", "Text", "Provenance", },
              pl.edu.icm.coansys.models.DocumentProtos.Affiliation.class,
              pl.edu.icm.coansys.models.DocumentProtos.Affiliation.Builder.class);
          internal_static_Author_descriptor =
            getDescriptor().getMessageTypes().get(5);
          internal_static_Author_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_Author_descriptor,
              new java.lang.String[] { "Key", "Forenames", "Surname", "Name", "Email", "AffiliationRef", "DocId", "PositionNumber", "ExtId", "AuxiliarInfo", },
              pl.edu.icm.coansys.models.DocumentProtos.Author.class,
              pl.edu.icm.coansys.models.DocumentProtos.Author.Builder.class);
          internal_static_BasicMetadata_descriptor =
            getDescriptor().getMessageTypes().get(6);
          internal_static_BasicMetadata_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_BasicMetadata_descriptor,
              new java.lang.String[] { "Title", "Author", "Doi", "Journal", "Isbn", "Issn", "Year", "Issue", "Volume", "Pages", "ClassifCode", },
              pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata.class,
              pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata.Builder.class);
          internal_static_KeywordsList_descriptor =
            getDescriptor().getMessageTypes().get(7);
          internal_static_KeywordsList_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_KeywordsList_descriptor,
              new java.lang.String[] { "Type", "Language", "Keywords", "Comment", "Provenance", },
              pl.edu.icm.coansys.models.DocumentProtos.KeywordsList.class,
              pl.edu.icm.coansys.models.DocumentProtos.KeywordsList.Builder.class);
          internal_static_DocumentMetadata_descriptor =
            getDescriptor().getMessageTypes().get(8);
          internal_static_DocumentMetadata_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_DocumentMetadata_descriptor,
              new java.lang.String[] { "Key", "BasicMetadata", "DocumentAbstract", "Keywords", "ExtId", "AuxiliarInfo", "Affiliations", "Reference", "Collection", "SourcePath", "OrigKey", },
              pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata.class,
              pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata.Builder.class);
          internal_static_ReferenceMetadata_descriptor =
            getDescriptor().getMessageTypes().get(9);
          internal_static_ReferenceMetadata_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_ReferenceMetadata_descriptor,
              new java.lang.String[] { "BasicMetadata", "SourceDocKey", "Position", "RawCitationText", "ExtId", },
              pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata.class,
              pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata.Builder.class);
          internal_static_Media_descriptor =
            getDescriptor().getMessageTypes().get(10);
          internal_static_Media_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_Media_descriptor,
              new java.lang.String[] { "Key", "MediaType", "Content", "Collection", "SourcePath", "SourceFilesize", "DestinationPath", "Provenance", },
              pl.edu.icm.coansys.models.DocumentProtos.Media.class,
              pl.edu.icm.coansys.models.DocumentProtos.Media.Builder.class);
          internal_static_MediaContainer_descriptor =
            getDescriptor().getMessageTypes().get(11);
          internal_static_MediaContainer_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_MediaContainer_descriptor,
              new java.lang.String[] { "Media", },
              pl.edu.icm.coansys.models.DocumentProtos.MediaContainer.class,
              pl.edu.icm.coansys.models.DocumentProtos.MediaContainer.Builder.class);
          internal_static_DocumentWrapper_descriptor =
            getDescriptor().getMessageTypes().get(12);
          internal_static_DocumentWrapper_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_DocumentWrapper_descriptor,
              new java.lang.String[] { "RowId", "DocumentMetadata", "MediaContainer", },
              pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper.class,
              pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper.Builder.class);
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
