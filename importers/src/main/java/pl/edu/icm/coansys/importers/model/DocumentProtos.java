/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */


package pl.edu.icm.coansys.importers.model;

public final class DocumentProtos {
  private DocumentProtos() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public static final class ClassifCode extends
      com.google.protobuf.GeneratedMessage {
    // Use ClassifCode.newBuilder() to construct.
    private ClassifCode() {
      initFields();
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
      return pl.edu.icm.coansys.importers.model.DocumentProtos.internal_static_ClassifCode_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return pl.edu.icm.coansys.importers.model.DocumentProtos.internal_static_ClassifCode_fieldAccessorTable;
    }
    
    // required string source = 1;
    public static final int SOURCE_FIELD_NUMBER = 1;
    private boolean hasSource;
    private java.lang.String source_ = "";
    public boolean hasSource() { return hasSource; }
    public java.lang.String getSource() { return source_; }
    
    // required string value = 2;
    public static final int VALUE_FIELD_NUMBER = 2;
    private boolean hasValue;
    private java.lang.String value_ = "";
    public boolean hasValue() { return hasValue; }
    public java.lang.String getValue() { return value_; }
    
    private void initFields() {
    }
    public final boolean isInitialized() {
      if (!hasSource) return false;
      if (!hasValue) return false;
      return true;
    }
    
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (hasSource()) {
        output.writeString(1, getSource());
      }
      if (hasValue()) {
        output.writeString(2, getValue());
      }
      getUnknownFields().writeTo(output);
    }
    
    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;
    
      size = 0;
      if (hasSource()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(1, getSource());
      }
      if (hasValue()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(2, getValue());
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }
    
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.ClassifCode parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.ClassifCode parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.ClassifCode parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.ClassifCode parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.ClassifCode parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.ClassifCode parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.ClassifCode parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.ClassifCode parseDelimitedFrom(
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
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.ClassifCode parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.ClassifCode parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(pl.edu.icm.coansys.importers.model.DocumentProtos.ClassifCode prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }
    
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> {
      private pl.edu.icm.coansys.importers.model.DocumentProtos.ClassifCode result;
      
      // Construct using pl.edu.icm.coansys.importers.model.DocumentProtos.ClassifCode.newBuilder()
      private Builder() {}
      
      private static Builder create() {
        Builder builder = new Builder();
        builder.result = new pl.edu.icm.coansys.importers.model.DocumentProtos.ClassifCode();
        return builder;
      }
      
      protected pl.edu.icm.coansys.importers.model.DocumentProtos.ClassifCode internalGetResult() {
        return result;
      }
      
      public Builder clear() {
        if (result == null) {
          throw new IllegalStateException(
            "Cannot call clear() after build().");
        }
        result = new pl.edu.icm.coansys.importers.model.DocumentProtos.ClassifCode();
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(result);
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return pl.edu.icm.coansys.importers.model.DocumentProtos.ClassifCode.getDescriptor();
      }
      
      public pl.edu.icm.coansys.importers.model.DocumentProtos.ClassifCode getDefaultInstanceForType() {
        return pl.edu.icm.coansys.importers.model.DocumentProtos.ClassifCode.getDefaultInstance();
      }
      
      public boolean isInitialized() {
        return result.isInitialized();
      }
      public pl.edu.icm.coansys.importers.model.DocumentProtos.ClassifCode build() {
        if (result != null && !isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return buildPartial();
      }
      
      private pl.edu.icm.coansys.importers.model.DocumentProtos.ClassifCode buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        if (!isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return buildPartial();
      }
      
      public pl.edu.icm.coansys.importers.model.DocumentProtos.ClassifCode buildPartial() {
        if (result == null) {
          throw new IllegalStateException(
            "build() has already been called on this Builder.");
        }
        pl.edu.icm.coansys.importers.model.DocumentProtos.ClassifCode returnMe = result;
        result = null;
        return returnMe;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof pl.edu.icm.coansys.importers.model.DocumentProtos.ClassifCode) {
          return mergeFrom((pl.edu.icm.coansys.importers.model.DocumentProtos.ClassifCode)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(pl.edu.icm.coansys.importers.model.DocumentProtos.ClassifCode other) {
        if (other == pl.edu.icm.coansys.importers.model.DocumentProtos.ClassifCode.getDefaultInstance()) return this;
        if (other.hasSource()) {
          setSource(other.getSource());
        }
        if (other.hasValue()) {
          setValue(other.getValue());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
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
              return this;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                this.setUnknownFields(unknownFields.build());
                return this;
              }
              break;
            }
            case 10: {
              setSource(input.readString());
              break;
            }
            case 18: {
              setValue(input.readString());
              break;
            }
          }
        }
      }
      
      
      // required string source = 1;
      public boolean hasSource() {
        return result.hasSource();
      }
      public java.lang.String getSource() {
        return result.getSource();
      }
      public Builder setSource(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasSource = true;
        result.source_ = value;
        return this;
      }
      public Builder clearSource() {
        result.hasSource = false;
        result.source_ = getDefaultInstance().getSource();
        return this;
      }
      
      // required string value = 2;
      public boolean hasValue() {
        return result.hasValue();
      }
      public java.lang.String getValue() {
        return result.getValue();
      }
      public Builder setValue(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasValue = true;
        result.value_ = value;
        return this;
      }
      public Builder clearValue() {
        result.hasValue = false;
        result.value_ = getDefaultInstance().getValue();
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:ClassifCode)
    }
    
    static {
      defaultInstance = new ClassifCode(true);
      pl.edu.icm.coansys.importers.model.DocumentProtos.internalForceInit();
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:ClassifCode)
  }
  
  public static final class ExtId extends
      com.google.protobuf.GeneratedMessage {
    // Use ExtId.newBuilder() to construct.
    private ExtId() {
      initFields();
    }
    private ExtId(boolean noInit) {}
    
    private static final ExtId defaultInstance;
    public static ExtId getDefaultInstance() {
      return defaultInstance;
    }
    
    public ExtId getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return pl.edu.icm.coansys.importers.model.DocumentProtos.internal_static_ExtId_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return pl.edu.icm.coansys.importers.model.DocumentProtos.internal_static_ExtId_fieldAccessorTable;
    }
    
    // required string source = 1;
    public static final int SOURCE_FIELD_NUMBER = 1;
    private boolean hasSource;
    private java.lang.String source_ = "";
    public boolean hasSource() { return hasSource; }
    public java.lang.String getSource() { return source_; }
    
    // required string value = 2;
    public static final int VALUE_FIELD_NUMBER = 2;
    private boolean hasValue;
    private java.lang.String value_ = "";
    public boolean hasValue() { return hasValue; }
    public java.lang.String getValue() { return value_; }
    
    private void initFields() {
    }
    public final boolean isInitialized() {
      if (!hasSource) return false;
      if (!hasValue) return false;
      return true;
    }
    
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (hasSource()) {
        output.writeString(1, getSource());
      }
      if (hasValue()) {
        output.writeString(2, getValue());
      }
      getUnknownFields().writeTo(output);
    }
    
    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;
    
      size = 0;
      if (hasSource()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(1, getSource());
      }
      if (hasValue()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(2, getValue());
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }
    
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId parseDelimitedFrom(
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
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }
    
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> {
      private pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId result;
      
      // Construct using pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId.newBuilder()
      private Builder() {}
      
      private static Builder create() {
        Builder builder = new Builder();
        builder.result = new pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId();
        return builder;
      }
      
      protected pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId internalGetResult() {
        return result;
      }
      
      public Builder clear() {
        if (result == null) {
          throw new IllegalStateException(
            "Cannot call clear() after build().");
        }
        result = new pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId();
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(result);
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId.getDescriptor();
      }
      
      public pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId getDefaultInstanceForType() {
        return pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId.getDefaultInstance();
      }
      
      public boolean isInitialized() {
        return result.isInitialized();
      }
      public pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId build() {
        if (result != null && !isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return buildPartial();
      }
      
      private pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        if (!isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return buildPartial();
      }
      
      public pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId buildPartial() {
        if (result == null) {
          throw new IllegalStateException(
            "build() has already been called on this Builder.");
        }
        pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId returnMe = result;
        result = null;
        return returnMe;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId) {
          return mergeFrom((pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId other) {
        if (other == pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId.getDefaultInstance()) return this;
        if (other.hasSource()) {
          setSource(other.getSource());
        }
        if (other.hasValue()) {
          setValue(other.getValue());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
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
              return this;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                this.setUnknownFields(unknownFields.build());
                return this;
              }
              break;
            }
            case 10: {
              setSource(input.readString());
              break;
            }
            case 18: {
              setValue(input.readString());
              break;
            }
          }
        }
      }
      
      
      // required string source = 1;
      public boolean hasSource() {
        return result.hasSource();
      }
      public java.lang.String getSource() {
        return result.getSource();
      }
      public Builder setSource(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasSource = true;
        result.source_ = value;
        return this;
      }
      public Builder clearSource() {
        result.hasSource = false;
        result.source_ = getDefaultInstance().getSource();
        return this;
      }
      
      // required string value = 2;
      public boolean hasValue() {
        return result.hasValue();
      }
      public java.lang.String getValue() {
        return result.getValue();
      }
      public Builder setValue(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasValue = true;
        result.value_ = value;
        return this;
      }
      public Builder clearValue() {
        result.hasValue = false;
        result.value_ = getDefaultInstance().getValue();
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:ExtId)
    }
    
    static {
      defaultInstance = new ExtId(true);
      pl.edu.icm.coansys.importers.model.DocumentProtos.internalForceInit();
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:ExtId)
  }
  
  public static final class AffiliationRef extends
      com.google.protobuf.GeneratedMessage {
    // Use AffiliationRef.newBuilder() to construct.
    private AffiliationRef() {
      initFields();
    }
    private AffiliationRef(boolean noInit) {}
    
    private static final AffiliationRef defaultInstance;
    public static AffiliationRef getDefaultInstance() {
      return defaultInstance;
    }
    
    public AffiliationRef getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return pl.edu.icm.coansys.importers.model.DocumentProtos.internal_static_AffiliationRef_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return pl.edu.icm.coansys.importers.model.DocumentProtos.internal_static_AffiliationRef_fieldAccessorTable;
    }
    
    // required string key = 1;
    public static final int KEY_FIELD_NUMBER = 1;
    private boolean hasKey;
    private java.lang.String key_ = "";
    public boolean hasKey() { return hasKey; }
    public java.lang.String getKey() { return key_; }
    
    // required string affiliationId = 2;
    public static final int AFFILIATIONID_FIELD_NUMBER = 2;
    private boolean hasAffiliationId;
    private java.lang.String affiliationId_ = "";
    public boolean hasAffiliationId() { return hasAffiliationId; }
    public java.lang.String getAffiliationId() { return affiliationId_; }
    
    private void initFields() {
    }
    public final boolean isInitialized() {
      if (!hasKey) return false;
      if (!hasAffiliationId) return false;
      return true;
    }
    
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (hasKey()) {
        output.writeString(1, getKey());
      }
      if (hasAffiliationId()) {
        output.writeString(2, getAffiliationId());
      }
      getUnknownFields().writeTo(output);
    }
    
    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;
    
      size = 0;
      if (hasKey()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(1, getKey());
      }
      if (hasAffiliationId()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(2, getAffiliationId());
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }
    
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.AffiliationRef parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.AffiliationRef parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.AffiliationRef parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.AffiliationRef parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.AffiliationRef parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.AffiliationRef parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.AffiliationRef parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.AffiliationRef parseDelimitedFrom(
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
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.AffiliationRef parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.AffiliationRef parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(pl.edu.icm.coansys.importers.model.DocumentProtos.AffiliationRef prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }
    
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> {
      private pl.edu.icm.coansys.importers.model.DocumentProtos.AffiliationRef result;
      
      // Construct using pl.edu.icm.coansys.importers.model.DocumentProtos.AffiliationRef.newBuilder()
      private Builder() {}
      
      private static Builder create() {
        Builder builder = new Builder();
        builder.result = new pl.edu.icm.coansys.importers.model.DocumentProtos.AffiliationRef();
        return builder;
      }
      
      protected pl.edu.icm.coansys.importers.model.DocumentProtos.AffiliationRef internalGetResult() {
        return result;
      }
      
      public Builder clear() {
        if (result == null) {
          throw new IllegalStateException(
            "Cannot call clear() after build().");
        }
        result = new pl.edu.icm.coansys.importers.model.DocumentProtos.AffiliationRef();
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(result);
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return pl.edu.icm.coansys.importers.model.DocumentProtos.AffiliationRef.getDescriptor();
      }
      
      public pl.edu.icm.coansys.importers.model.DocumentProtos.AffiliationRef getDefaultInstanceForType() {
        return pl.edu.icm.coansys.importers.model.DocumentProtos.AffiliationRef.getDefaultInstance();
      }
      
      public boolean isInitialized() {
        return result.isInitialized();
      }
      public pl.edu.icm.coansys.importers.model.DocumentProtos.AffiliationRef build() {
        if (result != null && !isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return buildPartial();
      }
      
      private pl.edu.icm.coansys.importers.model.DocumentProtos.AffiliationRef buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        if (!isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return buildPartial();
      }
      
      public pl.edu.icm.coansys.importers.model.DocumentProtos.AffiliationRef buildPartial() {
        if (result == null) {
          throw new IllegalStateException(
            "build() has already been called on this Builder.");
        }
        pl.edu.icm.coansys.importers.model.DocumentProtos.AffiliationRef returnMe = result;
        result = null;
        return returnMe;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof pl.edu.icm.coansys.importers.model.DocumentProtos.AffiliationRef) {
          return mergeFrom((pl.edu.icm.coansys.importers.model.DocumentProtos.AffiliationRef)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(pl.edu.icm.coansys.importers.model.DocumentProtos.AffiliationRef other) {
        if (other == pl.edu.icm.coansys.importers.model.DocumentProtos.AffiliationRef.getDefaultInstance()) return this;
        if (other.hasKey()) {
          setKey(other.getKey());
        }
        if (other.hasAffiliationId()) {
          setAffiliationId(other.getAffiliationId());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
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
              return this;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                this.setUnknownFields(unknownFields.build());
                return this;
              }
              break;
            }
            case 10: {
              setKey(input.readString());
              break;
            }
            case 18: {
              setAffiliationId(input.readString());
              break;
            }
          }
        }
      }
      
      
      // required string key = 1;
      public boolean hasKey() {
        return result.hasKey();
      }
      public java.lang.String getKey() {
        return result.getKey();
      }
      public Builder setKey(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasKey = true;
        result.key_ = value;
        return this;
      }
      public Builder clearKey() {
        result.hasKey = false;
        result.key_ = getDefaultInstance().getKey();
        return this;
      }
      
      // required string affiliationId = 2;
      public boolean hasAffiliationId() {
        return result.hasAffiliationId();
      }
      public java.lang.String getAffiliationId() {
        return result.getAffiliationId();
      }
      public Builder setAffiliationId(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasAffiliationId = true;
        result.affiliationId_ = value;
        return this;
      }
      public Builder clearAffiliationId() {
        result.hasAffiliationId = false;
        result.affiliationId_ = getDefaultInstance().getAffiliationId();
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:AffiliationRef)
    }
    
    static {
      defaultInstance = new AffiliationRef(true);
      pl.edu.icm.coansys.importers.model.DocumentProtos.internalForceInit();
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:AffiliationRef)
  }
  
  public static final class Affiliation extends
      com.google.protobuf.GeneratedMessage {
    // Use Affiliation.newBuilder() to construct.
    private Affiliation() {
      initFields();
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
      return pl.edu.icm.coansys.importers.model.DocumentProtos.internal_static_Affiliation_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return pl.edu.icm.coansys.importers.model.DocumentProtos.internal_static_Affiliation_fieldAccessorTable;
    }
    
    // required string key = 1;
    public static final int KEY_FIELD_NUMBER = 1;
    private boolean hasKey;
    private java.lang.String key_ = "";
    public boolean hasKey() { return hasKey; }
    public java.lang.String getKey() { return key_; }
    
    // required string affiliationId = 2;
    public static final int AFFILIATIONID_FIELD_NUMBER = 2;
    private boolean hasAffiliationId;
    private java.lang.String affiliationId_ = "";
    public boolean hasAffiliationId() { return hasAffiliationId; }
    public java.lang.String getAffiliationId() { return affiliationId_; }
    
    // required string text = 3;
    public static final int TEXT_FIELD_NUMBER = 3;
    private boolean hasText;
    private java.lang.String text_ = "";
    public boolean hasText() { return hasText; }
    public java.lang.String getText() { return text_; }
    
    private void initFields() {
    }
    public final boolean isInitialized() {
      if (!hasKey) return false;
      if (!hasAffiliationId) return false;
      if (!hasText) return false;
      return true;
    }
    
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (hasKey()) {
        output.writeString(1, getKey());
      }
      if (hasAffiliationId()) {
        output.writeString(2, getAffiliationId());
      }
      if (hasText()) {
        output.writeString(3, getText());
      }
      getUnknownFields().writeTo(output);
    }
    
    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;
    
      size = 0;
      if (hasKey()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(1, getKey());
      }
      if (hasAffiliationId()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(2, getAffiliationId());
      }
      if (hasText()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(3, getText());
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }
    
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.Affiliation parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.Affiliation parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.Affiliation parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.Affiliation parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.Affiliation parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.Affiliation parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.Affiliation parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.Affiliation parseDelimitedFrom(
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
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.Affiliation parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.Affiliation parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(pl.edu.icm.coansys.importers.model.DocumentProtos.Affiliation prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }
    
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> {
      private pl.edu.icm.coansys.importers.model.DocumentProtos.Affiliation result;
      
      // Construct using pl.edu.icm.coansys.importers.model.DocumentProtos.Affiliation.newBuilder()
      private Builder() {}
      
      private static Builder create() {
        Builder builder = new Builder();
        builder.result = new pl.edu.icm.coansys.importers.model.DocumentProtos.Affiliation();
        return builder;
      }
      
      protected pl.edu.icm.coansys.importers.model.DocumentProtos.Affiliation internalGetResult() {
        return result;
      }
      
      public Builder clear() {
        if (result == null) {
          throw new IllegalStateException(
            "Cannot call clear() after build().");
        }
        result = new pl.edu.icm.coansys.importers.model.DocumentProtos.Affiliation();
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(result);
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return pl.edu.icm.coansys.importers.model.DocumentProtos.Affiliation.getDescriptor();
      }
      
      public pl.edu.icm.coansys.importers.model.DocumentProtos.Affiliation getDefaultInstanceForType() {
        return pl.edu.icm.coansys.importers.model.DocumentProtos.Affiliation.getDefaultInstance();
      }
      
      public boolean isInitialized() {
        return result.isInitialized();
      }
      public pl.edu.icm.coansys.importers.model.DocumentProtos.Affiliation build() {
        if (result != null && !isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return buildPartial();
      }
      
      private pl.edu.icm.coansys.importers.model.DocumentProtos.Affiliation buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        if (!isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return buildPartial();
      }
      
      public pl.edu.icm.coansys.importers.model.DocumentProtos.Affiliation buildPartial() {
        if (result == null) {
          throw new IllegalStateException(
            "build() has already been called on this Builder.");
        }
        pl.edu.icm.coansys.importers.model.DocumentProtos.Affiliation returnMe = result;
        result = null;
        return returnMe;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof pl.edu.icm.coansys.importers.model.DocumentProtos.Affiliation) {
          return mergeFrom((pl.edu.icm.coansys.importers.model.DocumentProtos.Affiliation)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(pl.edu.icm.coansys.importers.model.DocumentProtos.Affiliation other) {
        if (other == pl.edu.icm.coansys.importers.model.DocumentProtos.Affiliation.getDefaultInstance()) return this;
        if (other.hasKey()) {
          setKey(other.getKey());
        }
        if (other.hasAffiliationId()) {
          setAffiliationId(other.getAffiliationId());
        }
        if (other.hasText()) {
          setText(other.getText());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
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
              return this;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                this.setUnknownFields(unknownFields.build());
                return this;
              }
              break;
            }
            case 10: {
              setKey(input.readString());
              break;
            }
            case 18: {
              setAffiliationId(input.readString());
              break;
            }
            case 26: {
              setText(input.readString());
              break;
            }
          }
        }
      }
      
      
      // required string key = 1;
      public boolean hasKey() {
        return result.hasKey();
      }
      public java.lang.String getKey() {
        return result.getKey();
      }
      public Builder setKey(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasKey = true;
        result.key_ = value;
        return this;
      }
      public Builder clearKey() {
        result.hasKey = false;
        result.key_ = getDefaultInstance().getKey();
        return this;
      }
      
      // required string affiliationId = 2;
      public boolean hasAffiliationId() {
        return result.hasAffiliationId();
      }
      public java.lang.String getAffiliationId() {
        return result.getAffiliationId();
      }
      public Builder setAffiliationId(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasAffiliationId = true;
        result.affiliationId_ = value;
        return this;
      }
      public Builder clearAffiliationId() {
        result.hasAffiliationId = false;
        result.affiliationId_ = getDefaultInstance().getAffiliationId();
        return this;
      }
      
      // required string text = 3;
      public boolean hasText() {
        return result.hasText();
      }
      public java.lang.String getText() {
        return result.getText();
      }
      public Builder setText(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasText = true;
        result.text_ = value;
        return this;
      }
      public Builder clearText() {
        result.hasText = false;
        result.text_ = getDefaultInstance().getText();
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:Affiliation)
    }
    
    static {
      defaultInstance = new Affiliation(true);
      pl.edu.icm.coansys.importers.model.DocumentProtos.internalForceInit();
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:Affiliation)
  }
  
  public static final class Author extends
      com.google.protobuf.GeneratedMessage {
    // Use Author.newBuilder() to construct.
    private Author() {
      initFields();
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
      return pl.edu.icm.coansys.importers.model.DocumentProtos.internal_static_Author_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return pl.edu.icm.coansys.importers.model.DocumentProtos.internal_static_Author_fieldAccessorTable;
    }
    
    // required string key = 1;
    public static final int KEY_FIELD_NUMBER = 1;
    private boolean hasKey;
    private java.lang.String key_ = "";
    public boolean hasKey() { return hasKey; }
    public java.lang.String getKey() { return key_; }
    
    // optional string forenames = 2;
    public static final int FORENAMES_FIELD_NUMBER = 2;
    private boolean hasForenames;
    private java.lang.String forenames_ = "";
    public boolean hasForenames() { return hasForenames; }
    public java.lang.String getForenames() { return forenames_; }
    
    // optional string surname = 3;
    public static final int SURNAME_FIELD_NUMBER = 3;
    private boolean hasSurname;
    private java.lang.String surname_ = "";
    public boolean hasSurname() { return hasSurname; }
    public java.lang.String getSurname() { return surname_; }
    
    // optional string name = 4;
    public static final int NAME_FIELD_NUMBER = 4;
    private boolean hasName;
    private java.lang.String name_ = "";
    public boolean hasName() { return hasName; }
    public java.lang.String getName() { return name_; }
    
    // optional string email = 5;
    public static final int EMAIL_FIELD_NUMBER = 5;
    private boolean hasEmail;
    private java.lang.String email_ = "";
    public boolean hasEmail() { return hasEmail; }
    public java.lang.String getEmail() { return email_; }
    
    // repeated .AffiliationRef affiliationRef = 6;
    public static final int AFFILIATIONREF_FIELD_NUMBER = 6;
    private java.util.List<pl.edu.icm.coansys.importers.model.DocumentProtos.AffiliationRef> affiliationRef_ =
      java.util.Collections.emptyList();
    public java.util.List<pl.edu.icm.coansys.importers.model.DocumentProtos.AffiliationRef> getAffiliationRefList() {
      return affiliationRef_;
    }
    public int getAffiliationRefCount() { return affiliationRef_.size(); }
    public pl.edu.icm.coansys.importers.model.DocumentProtos.AffiliationRef getAffiliationRef(int index) {
      return affiliationRef_.get(index);
    }
    
    // optional string docId = 7;
    public static final int DOCID_FIELD_NUMBER = 7;
    private boolean hasDocId;
    private java.lang.String docId_ = "";
    public boolean hasDocId() { return hasDocId; }
    public java.lang.String getDocId() { return docId_; }
    
    // optional int32 positionNumber = 8;
    public static final int POSITIONNUMBER_FIELD_NUMBER = 8;
    private boolean hasPositionNumber;
    private int positionNumber_ = 0;
    public boolean hasPositionNumber() { return hasPositionNumber; }
    public int getPositionNumber() { return positionNumber_; }
    
    // repeated .ExtId extId = 9;
    public static final int EXTID_FIELD_NUMBER = 9;
    private java.util.List<pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId> extId_ =
      java.util.Collections.emptyList();
    public java.util.List<pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId> getExtIdList() {
      return extId_;
    }
    public int getExtIdCount() { return extId_.size(); }
    public pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId getExtId(int index) {
      return extId_.get(index);
    }
    
    private void initFields() {
    }
    public final boolean isInitialized() {
      if (!hasKey) return false;
      for (pl.edu.icm.coansys.importers.model.DocumentProtos.AffiliationRef element : getAffiliationRefList()) {
        if (!element.isInitialized()) return false;
      }
      for (pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId element : getExtIdList()) {
        if (!element.isInitialized()) return false;
      }
      return true;
    }
    
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (hasKey()) {
        output.writeString(1, getKey());
      }
      if (hasForenames()) {
        output.writeString(2, getForenames());
      }
      if (hasSurname()) {
        output.writeString(3, getSurname());
      }
      if (hasName()) {
        output.writeString(4, getName());
      }
      if (hasEmail()) {
        output.writeString(5, getEmail());
      }
      for (pl.edu.icm.coansys.importers.model.DocumentProtos.AffiliationRef element : getAffiliationRefList()) {
        output.writeMessage(6, element);
      }
      if (hasDocId()) {
        output.writeString(7, getDocId());
      }
      if (hasPositionNumber()) {
        output.writeInt32(8, getPositionNumber());
      }
      for (pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId element : getExtIdList()) {
        output.writeMessage(9, element);
      }
      getUnknownFields().writeTo(output);
    }
    
    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;
    
      size = 0;
      if (hasKey()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(1, getKey());
      }
      if (hasForenames()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(2, getForenames());
      }
      if (hasSurname()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(3, getSurname());
      }
      if (hasName()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(4, getName());
      }
      if (hasEmail()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(5, getEmail());
      }
      for (pl.edu.icm.coansys.importers.model.DocumentProtos.AffiliationRef element : getAffiliationRefList()) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(6, element);
      }
      if (hasDocId()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(7, getDocId());
      }
      if (hasPositionNumber()) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(8, getPositionNumber());
      }
      for (pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId element : getExtIdList()) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(9, element);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }
    
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.Author parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.Author parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.Author parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.Author parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.Author parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.Author parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.Author parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.Author parseDelimitedFrom(
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
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.Author parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.Author parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(pl.edu.icm.coansys.importers.model.DocumentProtos.Author prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }
    
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> {
      private pl.edu.icm.coansys.importers.model.DocumentProtos.Author result;
      
      // Construct using pl.edu.icm.coansys.importers.model.DocumentProtos.Author.newBuilder()
      private Builder() {}
      
      private static Builder create() {
        Builder builder = new Builder();
        builder.result = new pl.edu.icm.coansys.importers.model.DocumentProtos.Author();
        return builder;
      }
      
      protected pl.edu.icm.coansys.importers.model.DocumentProtos.Author internalGetResult() {
        return result;
      }
      
      public Builder clear() {
        if (result == null) {
          throw new IllegalStateException(
            "Cannot call clear() after build().");
        }
        result = new pl.edu.icm.coansys.importers.model.DocumentProtos.Author();
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(result);
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return pl.edu.icm.coansys.importers.model.DocumentProtos.Author.getDescriptor();
      }
      
      public pl.edu.icm.coansys.importers.model.DocumentProtos.Author getDefaultInstanceForType() {
        return pl.edu.icm.coansys.importers.model.DocumentProtos.Author.getDefaultInstance();
      }
      
      public boolean isInitialized() {
        return result.isInitialized();
      }
      public pl.edu.icm.coansys.importers.model.DocumentProtos.Author build() {
        if (result != null && !isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return buildPartial();
      }
      
      private pl.edu.icm.coansys.importers.model.DocumentProtos.Author buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        if (!isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return buildPartial();
      }
      
      public pl.edu.icm.coansys.importers.model.DocumentProtos.Author buildPartial() {
        if (result == null) {
          throw new IllegalStateException(
            "build() has already been called on this Builder.");
        }
        if (result.affiliationRef_ != java.util.Collections.EMPTY_LIST) {
          result.affiliationRef_ =
            java.util.Collections.unmodifiableList(result.affiliationRef_);
        }
        if (result.extId_ != java.util.Collections.EMPTY_LIST) {
          result.extId_ =
            java.util.Collections.unmodifiableList(result.extId_);
        }
        pl.edu.icm.coansys.importers.model.DocumentProtos.Author returnMe = result;
        result = null;
        return returnMe;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof pl.edu.icm.coansys.importers.model.DocumentProtos.Author) {
          return mergeFrom((pl.edu.icm.coansys.importers.model.DocumentProtos.Author)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(pl.edu.icm.coansys.importers.model.DocumentProtos.Author other) {
        if (other == pl.edu.icm.coansys.importers.model.DocumentProtos.Author.getDefaultInstance()) return this;
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
        if (!other.affiliationRef_.isEmpty()) {
          if (result.affiliationRef_.isEmpty()) {
            result.affiliationRef_ = new java.util.ArrayList<pl.edu.icm.coansys.importers.model.DocumentProtos.AffiliationRef>();
          }
          result.affiliationRef_.addAll(other.affiliationRef_);
        }
        if (other.hasDocId()) {
          setDocId(other.getDocId());
        }
        if (other.hasPositionNumber()) {
          setPositionNumber(other.getPositionNumber());
        }
        if (!other.extId_.isEmpty()) {
          if (result.extId_.isEmpty()) {
            result.extId_ = new java.util.ArrayList<pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId>();
          }
          result.extId_.addAll(other.extId_);
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
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
              return this;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                this.setUnknownFields(unknownFields.build());
                return this;
              }
              break;
            }
            case 10: {
              setKey(input.readString());
              break;
            }
            case 18: {
              setForenames(input.readString());
              break;
            }
            case 26: {
              setSurname(input.readString());
              break;
            }
            case 34: {
              setName(input.readString());
              break;
            }
            case 42: {
              setEmail(input.readString());
              break;
            }
            case 50: {
              pl.edu.icm.coansys.importers.model.DocumentProtos.AffiliationRef.Builder subBuilder = pl.edu.icm.coansys.importers.model.DocumentProtos.AffiliationRef.newBuilder();
              input.readMessage(subBuilder, extensionRegistry);
              addAffiliationRef(subBuilder.buildPartial());
              break;
            }
            case 58: {
              setDocId(input.readString());
              break;
            }
            case 64: {
              setPositionNumber(input.readInt32());
              break;
            }
            case 74: {
              pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId.Builder subBuilder = pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId.newBuilder();
              input.readMessage(subBuilder, extensionRegistry);
              addExtId(subBuilder.buildPartial());
              break;
            }
          }
        }
      }
      
      
      // required string key = 1;
      public boolean hasKey() {
        return result.hasKey();
      }
      public java.lang.String getKey() {
        return result.getKey();
      }
      public Builder setKey(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasKey = true;
        result.key_ = value;
        return this;
      }
      public Builder clearKey() {
        result.hasKey = false;
        result.key_ = getDefaultInstance().getKey();
        return this;
      }
      
      // optional string forenames = 2;
      public boolean hasForenames() {
        return result.hasForenames();
      }
      public java.lang.String getForenames() {
        return result.getForenames();
      }
      public Builder setForenames(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasForenames = true;
        result.forenames_ = value;
        return this;
      }
      public Builder clearForenames() {
        result.hasForenames = false;
        result.forenames_ = getDefaultInstance().getForenames();
        return this;
      }
      
      // optional string surname = 3;
      public boolean hasSurname() {
        return result.hasSurname();
      }
      public java.lang.String getSurname() {
        return result.getSurname();
      }
      public Builder setSurname(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasSurname = true;
        result.surname_ = value;
        return this;
      }
      public Builder clearSurname() {
        result.hasSurname = false;
        result.surname_ = getDefaultInstance().getSurname();
        return this;
      }
      
      // optional string name = 4;
      public boolean hasName() {
        return result.hasName();
      }
      public java.lang.String getName() {
        return result.getName();
      }
      public Builder setName(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasName = true;
        result.name_ = value;
        return this;
      }
      public Builder clearName() {
        result.hasName = false;
        result.name_ = getDefaultInstance().getName();
        return this;
      }
      
      // optional string email = 5;
      public boolean hasEmail() {
        return result.hasEmail();
      }
      public java.lang.String getEmail() {
        return result.getEmail();
      }
      public Builder setEmail(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasEmail = true;
        result.email_ = value;
        return this;
      }
      public Builder clearEmail() {
        result.hasEmail = false;
        result.email_ = getDefaultInstance().getEmail();
        return this;
      }
      
      // repeated .AffiliationRef affiliationRef = 6;
      public java.util.List<pl.edu.icm.coansys.importers.model.DocumentProtos.AffiliationRef> getAffiliationRefList() {
        return java.util.Collections.unmodifiableList(result.affiliationRef_);
      }
      public int getAffiliationRefCount() {
        return result.getAffiliationRefCount();
      }
      public pl.edu.icm.coansys.importers.model.DocumentProtos.AffiliationRef getAffiliationRef(int index) {
        return result.getAffiliationRef(index);
      }
      public Builder setAffiliationRef(int index, pl.edu.icm.coansys.importers.model.DocumentProtos.AffiliationRef value) {
        if (value == null) {
          throw new NullPointerException();
        }
        result.affiliationRef_.set(index, value);
        return this;
      }
      public Builder setAffiliationRef(int index, pl.edu.icm.coansys.importers.model.DocumentProtos.AffiliationRef.Builder builderForValue) {
        result.affiliationRef_.set(index, builderForValue.build());
        return this;
      }
      public Builder addAffiliationRef(pl.edu.icm.coansys.importers.model.DocumentProtos.AffiliationRef value) {
        if (value == null) {
          throw new NullPointerException();
        }
        if (result.affiliationRef_.isEmpty()) {
          result.affiliationRef_ = new java.util.ArrayList<pl.edu.icm.coansys.importers.model.DocumentProtos.AffiliationRef>();
        }
        result.affiliationRef_.add(value);
        return this;
      }
      public Builder addAffiliationRef(pl.edu.icm.coansys.importers.model.DocumentProtos.AffiliationRef.Builder builderForValue) {
        if (result.affiliationRef_.isEmpty()) {
          result.affiliationRef_ = new java.util.ArrayList<pl.edu.icm.coansys.importers.model.DocumentProtos.AffiliationRef>();
        }
        result.affiliationRef_.add(builderForValue.build());
        return this;
      }
      public Builder addAllAffiliationRef(
          java.lang.Iterable<? extends pl.edu.icm.coansys.importers.model.DocumentProtos.AffiliationRef> values) {
        if (result.affiliationRef_.isEmpty()) {
          result.affiliationRef_ = new java.util.ArrayList<pl.edu.icm.coansys.importers.model.DocumentProtos.AffiliationRef>();
        }
        super.addAll(values, result.affiliationRef_);
        return this;
      }
      public Builder clearAffiliationRef() {
        result.affiliationRef_ = java.util.Collections.emptyList();
        return this;
      }
      
      // optional string docId = 7;
      public boolean hasDocId() {
        return result.hasDocId();
      }
      public java.lang.String getDocId() {
        return result.getDocId();
      }
      public Builder setDocId(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasDocId = true;
        result.docId_ = value;
        return this;
      }
      public Builder clearDocId() {
        result.hasDocId = false;
        result.docId_ = getDefaultInstance().getDocId();
        return this;
      }
      
      // optional int32 positionNumber = 8;
      public boolean hasPositionNumber() {
        return result.hasPositionNumber();
      }
      public int getPositionNumber() {
        return result.getPositionNumber();
      }
      public Builder setPositionNumber(int value) {
        result.hasPositionNumber = true;
        result.positionNumber_ = value;
        return this;
      }
      public Builder clearPositionNumber() {
        result.hasPositionNumber = false;
        result.positionNumber_ = 0;
        return this;
      }
      
      // repeated .ExtId extId = 9;
      public java.util.List<pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId> getExtIdList() {
        return java.util.Collections.unmodifiableList(result.extId_);
      }
      public int getExtIdCount() {
        return result.getExtIdCount();
      }
      public pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId getExtId(int index) {
        return result.getExtId(index);
      }
      public Builder setExtId(int index, pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId value) {
        if (value == null) {
          throw new NullPointerException();
        }
        result.extId_.set(index, value);
        return this;
      }
      public Builder setExtId(int index, pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId.Builder builderForValue) {
        result.extId_.set(index, builderForValue.build());
        return this;
      }
      public Builder addExtId(pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId value) {
        if (value == null) {
          throw new NullPointerException();
        }
        if (result.extId_.isEmpty()) {
          result.extId_ = new java.util.ArrayList<pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId>();
        }
        result.extId_.add(value);
        return this;
      }
      public Builder addExtId(pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId.Builder builderForValue) {
        if (result.extId_.isEmpty()) {
          result.extId_ = new java.util.ArrayList<pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId>();
        }
        result.extId_.add(builderForValue.build());
        return this;
      }
      public Builder addAllExtId(
          java.lang.Iterable<? extends pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId> values) {
        if (result.extId_.isEmpty()) {
          result.extId_ = new java.util.ArrayList<pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId>();
        }
        super.addAll(values, result.extId_);
        return this;
      }
      public Builder clearExtId() {
        result.extId_ = java.util.Collections.emptyList();
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:Author)
    }
    
    static {
      defaultInstance = new Author(true);
      pl.edu.icm.coansys.importers.model.DocumentProtos.internalForceInit();
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:Author)
  }
  
  public static final class DocumentMetadata extends
      com.google.protobuf.GeneratedMessage {
    // Use DocumentMetadata.newBuilder() to construct.
    private DocumentMetadata() {
      initFields();
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
      return pl.edu.icm.coansys.importers.model.DocumentProtos.internal_static_DocumentMetadata_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return pl.edu.icm.coansys.importers.model.DocumentProtos.internal_static_DocumentMetadata_fieldAccessorTable;
    }
    
    // required string key = 1;
    public static final int KEY_FIELD_NUMBER = 1;
    private boolean hasKey;
    private java.lang.String key_ = "";
    public boolean hasKey() { return hasKey; }
    public java.lang.String getKey() { return key_; }
    
    // optional string title = 2;
    public static final int TITLE_FIELD_NUMBER = 2;
    private boolean hasTitle;
    private java.lang.String title_ = "";
    public boolean hasTitle() { return hasTitle; }
    public java.lang.String getTitle() { return title_; }
    
    // optional string abstrakt = 3;
    public static final int ABSTRAKT_FIELD_NUMBER = 3;
    private boolean hasAbstrakt;
    private java.lang.String abstrakt_ = "";
    public boolean hasAbstrakt() { return hasAbstrakt; }
    public java.lang.String getAbstrakt() { return abstrakt_; }
    
    // repeated string keyword = 4;
    public static final int KEYWORD_FIELD_NUMBER = 4;
    private java.util.List<java.lang.String> keyword_ =
      java.util.Collections.emptyList();
    public java.util.List<java.lang.String> getKeywordList() {
      return keyword_;
    }
    public int getKeywordCount() { return keyword_.size(); }
    public java.lang.String getKeyword(int index) {
      return keyword_.get(index);
    }
    
    // repeated .Author author = 5;
    public static final int AUTHOR_FIELD_NUMBER = 5;
    private java.util.List<pl.edu.icm.coansys.importers.model.DocumentProtos.Author> author_ =
      java.util.Collections.emptyList();
    public java.util.List<pl.edu.icm.coansys.importers.model.DocumentProtos.Author> getAuthorList() {
      return author_;
    }
    public int getAuthorCount() { return author_.size(); }
    public pl.edu.icm.coansys.importers.model.DocumentProtos.Author getAuthor(int index) {
      return author_.get(index);
    }
    
    // repeated .DocumentMetadata reference = 6;
    public static final int REFERENCE_FIELD_NUMBER = 6;
    private java.util.List<pl.edu.icm.coansys.importers.model.DocumentProtos.DocumentMetadata> reference_ =
      java.util.Collections.emptyList();
    public java.util.List<pl.edu.icm.coansys.importers.model.DocumentProtos.DocumentMetadata> getReferenceList() {
      return reference_;
    }
    public int getReferenceCount() { return reference_.size(); }
    public pl.edu.icm.coansys.importers.model.DocumentProtos.DocumentMetadata getReference(int index) {
      return reference_.get(index);
    }
    
    // optional int32 bibRefPosition = 7;
    public static final int BIBREFPOSITION_FIELD_NUMBER = 7;
    private boolean hasBibRefPosition;
    private int bibRefPosition_ = 0;
    public boolean hasBibRefPosition() { return hasBibRefPosition; }
    public int getBibRefPosition() { return bibRefPosition_; }
    
    // optional string collection = 10;
    public static final int COLLECTION_FIELD_NUMBER = 10;
    private boolean hasCollection;
    private java.lang.String collection_ = "";
    public boolean hasCollection() { return hasCollection; }
    public java.lang.String getCollection() { return collection_; }
    
    // optional string doi = 11;
    public static final int DOI_FIELD_NUMBER = 11;
    private boolean hasDoi;
    private java.lang.String doi_ = "";
    public boolean hasDoi() { return hasDoi; }
    public java.lang.String getDoi() { return doi_; }
    
    // optional string isbn = 12;
    public static final int ISBN_FIELD_NUMBER = 12;
    private boolean hasIsbn;
    private java.lang.String isbn_ = "";
    public boolean hasIsbn() { return hasIsbn; }
    public java.lang.String getIsbn() { return isbn_; }
    
    // optional string issn = 13;
    public static final int ISSN_FIELD_NUMBER = 13;
    private boolean hasIssn;
    private java.lang.String issn_ = "";
    public boolean hasIssn() { return hasIssn; }
    public java.lang.String getIssn() { return issn_; }
    
    // optional string issue = 14;
    public static final int ISSUE_FIELD_NUMBER = 14;
    private boolean hasIssue;
    private java.lang.String issue_ = "";
    public boolean hasIssue() { return hasIssue; }
    public java.lang.String getIssue() { return issue_; }
    
    // optional string journal = 15;
    public static final int JOURNAL_FIELD_NUMBER = 15;
    private boolean hasJournal;
    private java.lang.String journal_ = "";
    public boolean hasJournal() { return hasJournal; }
    public java.lang.String getJournal() { return journal_; }
    
    // optional .ExtId extId = 16;
    public static final int EXTID_FIELD_NUMBER = 16;
    private boolean hasExtId;
    private pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId extId_;
    public boolean hasExtId() { return hasExtId; }
    public pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId getExtId() { return extId_; }
    
    // repeated .ClassifCode classifCode = 17;
    public static final int CLASSIFCODE_FIELD_NUMBER = 17;
    private java.util.List<pl.edu.icm.coansys.importers.model.DocumentProtos.ClassifCode> classifCode_ =
      java.util.Collections.emptyList();
    public java.util.List<pl.edu.icm.coansys.importers.model.DocumentProtos.ClassifCode> getClassifCodeList() {
      return classifCode_;
    }
    public int getClassifCodeCount() { return classifCode_.size(); }
    public pl.edu.icm.coansys.importers.model.DocumentProtos.ClassifCode getClassifCode(int index) {
      return classifCode_.get(index);
    }
    
    // optional string pages = 18;
    public static final int PAGES_FIELD_NUMBER = 18;
    private boolean hasPages;
    private java.lang.String pages_ = "";
    public boolean hasPages() { return hasPages; }
    public java.lang.String getPages() { return pages_; }
    
    // optional string source = 19;
    public static final int SOURCE_FIELD_NUMBER = 19;
    private boolean hasSource;
    private java.lang.String source_ = "";
    public boolean hasSource() { return hasSource; }
    public java.lang.String getSource() { return source_; }
    
    // optional string text = 20;
    public static final int TEXT_FIELD_NUMBER = 20;
    private boolean hasText;
    private java.lang.String text_ = "";
    public boolean hasText() { return hasText; }
    public java.lang.String getText() { return text_; }
    
    // optional string volume = 21;
    public static final int VOLUME_FIELD_NUMBER = 21;
    private boolean hasVolume;
    private java.lang.String volume_ = "";
    public boolean hasVolume() { return hasVolume; }
    public java.lang.String getVolume() { return volume_; }
    
    // optional bool clusterActive = 22;
    public static final int CLUSTERACTIVE_FIELD_NUMBER = 22;
    private boolean hasClusterActive;
    private boolean clusterActive_ = false;
    public boolean hasClusterActive() { return hasClusterActive; }
    public boolean getClusterActive() { return clusterActive_; }
    
    // repeated string clusterContents = 23;
    public static final int CLUSTERCONTENTS_FIELD_NUMBER = 23;
    private java.util.List<java.lang.String> clusterContents_ =
      java.util.Collections.emptyList();
    public java.util.List<java.lang.String> getClusterContentsList() {
      return clusterContents_;
    }
    public int getClusterContentsCount() { return clusterContents_.size(); }
    public java.lang.String getClusterContents(int index) {
      return clusterContents_.get(index);
    }
    
    // optional string partOfCluster = 24;
    public static final int PARTOFCLUSTER_FIELD_NUMBER = 24;
    private boolean hasPartOfCluster;
    private java.lang.String partOfCluster_ = "";
    public boolean hasPartOfCluster() { return hasPartOfCluster; }
    public java.lang.String getPartOfCluster() { return partOfCluster_; }
    
    private void initFields() {
      extId_ = pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId.getDefaultInstance();
    }
    public final boolean isInitialized() {
      if (!hasKey) return false;
      for (pl.edu.icm.coansys.importers.model.DocumentProtos.Author element : getAuthorList()) {
        if (!element.isInitialized()) return false;
      }
      for (pl.edu.icm.coansys.importers.model.DocumentProtos.DocumentMetadata element : getReferenceList()) {
        if (!element.isInitialized()) return false;
      }
      if (hasExtId()) {
        if (!getExtId().isInitialized()) return false;
      }
      for (pl.edu.icm.coansys.importers.model.DocumentProtos.ClassifCode element : getClassifCodeList()) {
        if (!element.isInitialized()) return false;
      }
      return true;
    }
    
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (hasKey()) {
        output.writeString(1, getKey());
      }
      if (hasTitle()) {
        output.writeString(2, getTitle());
      }
      if (hasAbstrakt()) {
        output.writeString(3, getAbstrakt());
      }
      for (java.lang.String element : getKeywordList()) {
        output.writeString(4, element);
      }
      for (pl.edu.icm.coansys.importers.model.DocumentProtos.Author element : getAuthorList()) {
        output.writeMessage(5, element);
      }
      for (pl.edu.icm.coansys.importers.model.DocumentProtos.DocumentMetadata element : getReferenceList()) {
        output.writeMessage(6, element);
      }
      if (hasBibRefPosition()) {
        output.writeInt32(7, getBibRefPosition());
      }
      if (hasCollection()) {
        output.writeString(10, getCollection());
      }
      if (hasDoi()) {
        output.writeString(11, getDoi());
      }
      if (hasIsbn()) {
        output.writeString(12, getIsbn());
      }
      if (hasIssn()) {
        output.writeString(13, getIssn());
      }
      if (hasIssue()) {
        output.writeString(14, getIssue());
      }
      if (hasJournal()) {
        output.writeString(15, getJournal());
      }
      if (hasExtId()) {
        output.writeMessage(16, getExtId());
      }
      for (pl.edu.icm.coansys.importers.model.DocumentProtos.ClassifCode element : getClassifCodeList()) {
        output.writeMessage(17, element);
      }
      if (hasPages()) {
        output.writeString(18, getPages());
      }
      if (hasSource()) {
        output.writeString(19, getSource());
      }
      if (hasText()) {
        output.writeString(20, getText());
      }
      if (hasVolume()) {
        output.writeString(21, getVolume());
      }
      if (hasClusterActive()) {
        output.writeBool(22, getClusterActive());
      }
      for (java.lang.String element : getClusterContentsList()) {
        output.writeString(23, element);
      }
      if (hasPartOfCluster()) {
        output.writeString(24, getPartOfCluster());
      }
      getUnknownFields().writeTo(output);
    }
    
    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;
    
      size = 0;
      if (hasKey()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(1, getKey());
      }
      if (hasTitle()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(2, getTitle());
      }
      if (hasAbstrakt()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(3, getAbstrakt());
      }
      {
        int dataSize = 0;
        for (java.lang.String element : getKeywordList()) {
          dataSize += com.google.protobuf.CodedOutputStream
            .computeStringSizeNoTag(element);
        }
        size += dataSize;
        size += 1 * getKeywordList().size();
      }
      for (pl.edu.icm.coansys.importers.model.DocumentProtos.Author element : getAuthorList()) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(5, element);
      }
      for (pl.edu.icm.coansys.importers.model.DocumentProtos.DocumentMetadata element : getReferenceList()) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(6, element);
      }
      if (hasBibRefPosition()) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(7, getBibRefPosition());
      }
      if (hasCollection()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(10, getCollection());
      }
      if (hasDoi()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(11, getDoi());
      }
      if (hasIsbn()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(12, getIsbn());
      }
      if (hasIssn()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(13, getIssn());
      }
      if (hasIssue()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(14, getIssue());
      }
      if (hasJournal()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(15, getJournal());
      }
      if (hasExtId()) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(16, getExtId());
      }
      for (pl.edu.icm.coansys.importers.model.DocumentProtos.ClassifCode element : getClassifCodeList()) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(17, element);
      }
      if (hasPages()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(18, getPages());
      }
      if (hasSource()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(19, getSource());
      }
      if (hasText()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(20, getText());
      }
      if (hasVolume()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(21, getVolume());
      }
      if (hasClusterActive()) {
        size += com.google.protobuf.CodedOutputStream
          .computeBoolSize(22, getClusterActive());
      }
      {
        int dataSize = 0;
        for (java.lang.String element : getClusterContentsList()) {
          dataSize += com.google.protobuf.CodedOutputStream
            .computeStringSizeNoTag(element);
        }
        size += dataSize;
        size += 2 * getClusterContentsList().size();
      }
      if (hasPartOfCluster()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(24, getPartOfCluster());
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }
    
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.DocumentMetadata parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.DocumentMetadata parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.DocumentMetadata parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.DocumentMetadata parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.DocumentMetadata parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.DocumentMetadata parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.DocumentMetadata parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.DocumentMetadata parseDelimitedFrom(
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
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.DocumentMetadata parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.DocumentMetadata parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(pl.edu.icm.coansys.importers.model.DocumentProtos.DocumentMetadata prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }
    
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> {
      private pl.edu.icm.coansys.importers.model.DocumentProtos.DocumentMetadata result;
      
      // Construct using pl.edu.icm.coansys.importers.model.DocumentProtos.DocumentMetadata.newBuilder()
      private Builder() {}
      
      private static Builder create() {
        Builder builder = new Builder();
        builder.result = new pl.edu.icm.coansys.importers.model.DocumentProtos.DocumentMetadata();
        return builder;
      }
      
      protected pl.edu.icm.coansys.importers.model.DocumentProtos.DocumentMetadata internalGetResult() {
        return result;
      }
      
      public Builder clear() {
        if (result == null) {
          throw new IllegalStateException(
            "Cannot call clear() after build().");
        }
        result = new pl.edu.icm.coansys.importers.model.DocumentProtos.DocumentMetadata();
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(result);
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return pl.edu.icm.coansys.importers.model.DocumentProtos.DocumentMetadata.getDescriptor();
      }
      
      public pl.edu.icm.coansys.importers.model.DocumentProtos.DocumentMetadata getDefaultInstanceForType() {
        return pl.edu.icm.coansys.importers.model.DocumentProtos.DocumentMetadata.getDefaultInstance();
      }
      
      public boolean isInitialized() {
        return result.isInitialized();
      }
      public pl.edu.icm.coansys.importers.model.DocumentProtos.DocumentMetadata build() {
        if (result != null && !isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return buildPartial();
      }
      
      private pl.edu.icm.coansys.importers.model.DocumentProtos.DocumentMetadata buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        if (!isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return buildPartial();
      }
      
      public pl.edu.icm.coansys.importers.model.DocumentProtos.DocumentMetadata buildPartial() {
        if (result == null) {
          throw new IllegalStateException(
            "build() has already been called on this Builder.");
        }
        if (result.keyword_ != java.util.Collections.EMPTY_LIST) {
          result.keyword_ =
            java.util.Collections.unmodifiableList(result.keyword_);
        }
        if (result.author_ != java.util.Collections.EMPTY_LIST) {
          result.author_ =
            java.util.Collections.unmodifiableList(result.author_);
        }
        if (result.reference_ != java.util.Collections.EMPTY_LIST) {
          result.reference_ =
            java.util.Collections.unmodifiableList(result.reference_);
        }
        if (result.classifCode_ != java.util.Collections.EMPTY_LIST) {
          result.classifCode_ =
            java.util.Collections.unmodifiableList(result.classifCode_);
        }
        if (result.clusterContents_ != java.util.Collections.EMPTY_LIST) {
          result.clusterContents_ =
            java.util.Collections.unmodifiableList(result.clusterContents_);
        }
        pl.edu.icm.coansys.importers.model.DocumentProtos.DocumentMetadata returnMe = result;
        result = null;
        return returnMe;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof pl.edu.icm.coansys.importers.model.DocumentProtos.DocumentMetadata) {
          return mergeFrom((pl.edu.icm.coansys.importers.model.DocumentProtos.DocumentMetadata)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(pl.edu.icm.coansys.importers.model.DocumentProtos.DocumentMetadata other) {
        if (other == pl.edu.icm.coansys.importers.model.DocumentProtos.DocumentMetadata.getDefaultInstance()) return this;
        if (other.hasKey()) {
          setKey(other.getKey());
        }
        if (other.hasTitle()) {
          setTitle(other.getTitle());
        }
        if (other.hasAbstrakt()) {
          setAbstrakt(other.getAbstrakt());
        }
        if (!other.keyword_.isEmpty()) {
          if (result.keyword_.isEmpty()) {
            result.keyword_ = new java.util.ArrayList<java.lang.String>();
          }
          result.keyword_.addAll(other.keyword_);
        }
        if (!other.author_.isEmpty()) {
          if (result.author_.isEmpty()) {
            result.author_ = new java.util.ArrayList<pl.edu.icm.coansys.importers.model.DocumentProtos.Author>();
          }
          result.author_.addAll(other.author_);
        }
        if (!other.reference_.isEmpty()) {
          if (result.reference_.isEmpty()) {
            result.reference_ = new java.util.ArrayList<pl.edu.icm.coansys.importers.model.DocumentProtos.DocumentMetadata>();
          }
          result.reference_.addAll(other.reference_);
        }
        if (other.hasBibRefPosition()) {
          setBibRefPosition(other.getBibRefPosition());
        }
        if (other.hasCollection()) {
          setCollection(other.getCollection());
        }
        if (other.hasDoi()) {
          setDoi(other.getDoi());
        }
        if (other.hasIsbn()) {
          setIsbn(other.getIsbn());
        }
        if (other.hasIssn()) {
          setIssn(other.getIssn());
        }
        if (other.hasIssue()) {
          setIssue(other.getIssue());
        }
        if (other.hasJournal()) {
          setJournal(other.getJournal());
        }
        if (other.hasExtId()) {
          mergeExtId(other.getExtId());
        }
        if (!other.classifCode_.isEmpty()) {
          if (result.classifCode_.isEmpty()) {
            result.classifCode_ = new java.util.ArrayList<pl.edu.icm.coansys.importers.model.DocumentProtos.ClassifCode>();
          }
          result.classifCode_.addAll(other.classifCode_);
        }
        if (other.hasPages()) {
          setPages(other.getPages());
        }
        if (other.hasSource()) {
          setSource(other.getSource());
        }
        if (other.hasText()) {
          setText(other.getText());
        }
        if (other.hasVolume()) {
          setVolume(other.getVolume());
        }
        if (other.hasClusterActive()) {
          setClusterActive(other.getClusterActive());
        }
        if (!other.clusterContents_.isEmpty()) {
          if (result.clusterContents_.isEmpty()) {
            result.clusterContents_ = new java.util.ArrayList<java.lang.String>();
          }
          result.clusterContents_.addAll(other.clusterContents_);
        }
        if (other.hasPartOfCluster()) {
          setPartOfCluster(other.getPartOfCluster());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
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
              return this;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                this.setUnknownFields(unknownFields.build());
                return this;
              }
              break;
            }
            case 10: {
              setKey(input.readString());
              break;
            }
            case 18: {
              setTitle(input.readString());
              break;
            }
            case 26: {
              setAbstrakt(input.readString());
              break;
            }
            case 34: {
              addKeyword(input.readString());
              break;
            }
            case 42: {
              pl.edu.icm.coansys.importers.model.DocumentProtos.Author.Builder subBuilder = pl.edu.icm.coansys.importers.model.DocumentProtos.Author.newBuilder();
              input.readMessage(subBuilder, extensionRegistry);
              addAuthor(subBuilder.buildPartial());
              break;
            }
            case 50: {
              pl.edu.icm.coansys.importers.model.DocumentProtos.DocumentMetadata.Builder subBuilder = pl.edu.icm.coansys.importers.model.DocumentProtos.DocumentMetadata.newBuilder();
              input.readMessage(subBuilder, extensionRegistry);
              addReference(subBuilder.buildPartial());
              break;
            }
            case 56: {
              setBibRefPosition(input.readInt32());
              break;
            }
            case 82: {
              setCollection(input.readString());
              break;
            }
            case 90: {
              setDoi(input.readString());
              break;
            }
            case 98: {
              setIsbn(input.readString());
              break;
            }
            case 106: {
              setIssn(input.readString());
              break;
            }
            case 114: {
              setIssue(input.readString());
              break;
            }
            case 122: {
              setJournal(input.readString());
              break;
            }
            case 130: {
              pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId.Builder subBuilder = pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId.newBuilder();
              if (hasExtId()) {
                subBuilder.mergeFrom(getExtId());
              }
              input.readMessage(subBuilder, extensionRegistry);
              setExtId(subBuilder.buildPartial());
              break;
            }
            case 138: {
              pl.edu.icm.coansys.importers.model.DocumentProtos.ClassifCode.Builder subBuilder = pl.edu.icm.coansys.importers.model.DocumentProtos.ClassifCode.newBuilder();
              input.readMessage(subBuilder, extensionRegistry);
              addClassifCode(subBuilder.buildPartial());
              break;
            }
            case 146: {
              setPages(input.readString());
              break;
            }
            case 154: {
              setSource(input.readString());
              break;
            }
            case 162: {
              setText(input.readString());
              break;
            }
            case 170: {
              setVolume(input.readString());
              break;
            }
            case 176: {
              setClusterActive(input.readBool());
              break;
            }
            case 186: {
              addClusterContents(input.readString());
              break;
            }
            case 194: {
              setPartOfCluster(input.readString());
              break;
            }
          }
        }
      }
      
      
      // required string key = 1;
      public boolean hasKey() {
        return result.hasKey();
      }
      public java.lang.String getKey() {
        return result.getKey();
      }
      public Builder setKey(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasKey = true;
        result.key_ = value;
        return this;
      }
      public Builder clearKey() {
        result.hasKey = false;
        result.key_ = getDefaultInstance().getKey();
        return this;
      }
      
      // optional string title = 2;
      public boolean hasTitle() {
        return result.hasTitle();
      }
      public java.lang.String getTitle() {
        return result.getTitle();
      }
      public Builder setTitle(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasTitle = true;
        result.title_ = value;
        return this;
      }
      public Builder clearTitle() {
        result.hasTitle = false;
        result.title_ = getDefaultInstance().getTitle();
        return this;
      }
      
      // optional string abstrakt = 3;
      public boolean hasAbstrakt() {
        return result.hasAbstrakt();
      }
      public java.lang.String getAbstrakt() {
        return result.getAbstrakt();
      }
      public Builder setAbstrakt(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasAbstrakt = true;
        result.abstrakt_ = value;
        return this;
      }
      public Builder clearAbstrakt() {
        result.hasAbstrakt = false;
        result.abstrakt_ = getDefaultInstance().getAbstrakt();
        return this;
      }
      
      // repeated string keyword = 4;
      public java.util.List<java.lang.String> getKeywordList() {
        return java.util.Collections.unmodifiableList(result.keyword_);
      }
      public int getKeywordCount() {
        return result.getKeywordCount();
      }
      public java.lang.String getKeyword(int index) {
        return result.getKeyword(index);
      }
      public Builder setKeyword(int index, java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.keyword_.set(index, value);
        return this;
      }
      public Builder addKeyword(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  if (result.keyword_.isEmpty()) {
          result.keyword_ = new java.util.ArrayList<java.lang.String>();
        }
        result.keyword_.add(value);
        return this;
      }
      public Builder addAllKeyword(
          java.lang.Iterable<? extends java.lang.String> values) {
        if (result.keyword_.isEmpty()) {
          result.keyword_ = new java.util.ArrayList<java.lang.String>();
        }
        super.addAll(values, result.keyword_);
        return this;
      }
      public Builder clearKeyword() {
        result.keyword_ = java.util.Collections.emptyList();
        return this;
      }
      
      // repeated .Author author = 5;
      public java.util.List<pl.edu.icm.coansys.importers.model.DocumentProtos.Author> getAuthorList() {
        return java.util.Collections.unmodifiableList(result.author_);
      }
      public int getAuthorCount() {
        return result.getAuthorCount();
      }
      public pl.edu.icm.coansys.importers.model.DocumentProtos.Author getAuthor(int index) {
        return result.getAuthor(index);
      }
      public Builder setAuthor(int index, pl.edu.icm.coansys.importers.model.DocumentProtos.Author value) {
        if (value == null) {
          throw new NullPointerException();
        }
        result.author_.set(index, value);
        return this;
      }
      public Builder setAuthor(int index, pl.edu.icm.coansys.importers.model.DocumentProtos.Author.Builder builderForValue) {
        result.author_.set(index, builderForValue.build());
        return this;
      }
      public Builder addAuthor(pl.edu.icm.coansys.importers.model.DocumentProtos.Author value) {
        if (value == null) {
          throw new NullPointerException();
        }
        if (result.author_.isEmpty()) {
          result.author_ = new java.util.ArrayList<pl.edu.icm.coansys.importers.model.DocumentProtos.Author>();
        }
        result.author_.add(value);
        return this;
      }
      public Builder addAuthor(pl.edu.icm.coansys.importers.model.DocumentProtos.Author.Builder builderForValue) {
        if (result.author_.isEmpty()) {
          result.author_ = new java.util.ArrayList<pl.edu.icm.coansys.importers.model.DocumentProtos.Author>();
        }
        result.author_.add(builderForValue.build());
        return this;
      }
      public Builder addAllAuthor(
          java.lang.Iterable<? extends pl.edu.icm.coansys.importers.model.DocumentProtos.Author> values) {
        if (result.author_.isEmpty()) {
          result.author_ = new java.util.ArrayList<pl.edu.icm.coansys.importers.model.DocumentProtos.Author>();
        }
        super.addAll(values, result.author_);
        return this;
      }
      public Builder clearAuthor() {
        result.author_ = java.util.Collections.emptyList();
        return this;
      }
      
      // repeated .DocumentMetadata reference = 6;
      public java.util.List<pl.edu.icm.coansys.importers.model.DocumentProtos.DocumentMetadata> getReferenceList() {
        return java.util.Collections.unmodifiableList(result.reference_);
      }
      public int getReferenceCount() {
        return result.getReferenceCount();
      }
      public pl.edu.icm.coansys.importers.model.DocumentProtos.DocumentMetadata getReference(int index) {
        return result.getReference(index);
      }
      public Builder setReference(int index, pl.edu.icm.coansys.importers.model.DocumentProtos.DocumentMetadata value) {
        if (value == null) {
          throw new NullPointerException();
        }
        result.reference_.set(index, value);
        return this;
      }
      public Builder setReference(int index, pl.edu.icm.coansys.importers.model.DocumentProtos.DocumentMetadata.Builder builderForValue) {
        result.reference_.set(index, builderForValue.build());
        return this;
      }
      public Builder addReference(pl.edu.icm.coansys.importers.model.DocumentProtos.DocumentMetadata value) {
        if (value == null) {
          throw new NullPointerException();
        }
        if (result.reference_.isEmpty()) {
          result.reference_ = new java.util.ArrayList<pl.edu.icm.coansys.importers.model.DocumentProtos.DocumentMetadata>();
        }
        result.reference_.add(value);
        return this;
      }
      public Builder addReference(pl.edu.icm.coansys.importers.model.DocumentProtos.DocumentMetadata.Builder builderForValue) {
        if (result.reference_.isEmpty()) {
          result.reference_ = new java.util.ArrayList<pl.edu.icm.coansys.importers.model.DocumentProtos.DocumentMetadata>();
        }
        result.reference_.add(builderForValue.build());
        return this;
      }
      public Builder addAllReference(
          java.lang.Iterable<? extends pl.edu.icm.coansys.importers.model.DocumentProtos.DocumentMetadata> values) {
        if (result.reference_.isEmpty()) {
          result.reference_ = new java.util.ArrayList<pl.edu.icm.coansys.importers.model.DocumentProtos.DocumentMetadata>();
        }
        super.addAll(values, result.reference_);
        return this;
      }
      public Builder clearReference() {
        result.reference_ = java.util.Collections.emptyList();
        return this;
      }
      
      // optional int32 bibRefPosition = 7;
      public boolean hasBibRefPosition() {
        return result.hasBibRefPosition();
      }
      public int getBibRefPosition() {
        return result.getBibRefPosition();
      }
      public Builder setBibRefPosition(int value) {
        result.hasBibRefPosition = true;
        result.bibRefPosition_ = value;
        return this;
      }
      public Builder clearBibRefPosition() {
        result.hasBibRefPosition = false;
        result.bibRefPosition_ = 0;
        return this;
      }
      
      // optional string collection = 10;
      public boolean hasCollection() {
        return result.hasCollection();
      }
      public java.lang.String getCollection() {
        return result.getCollection();
      }
      public Builder setCollection(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasCollection = true;
        result.collection_ = value;
        return this;
      }
      public Builder clearCollection() {
        result.hasCollection = false;
        result.collection_ = getDefaultInstance().getCollection();
        return this;
      }
      
      // optional string doi = 11;
      public boolean hasDoi() {
        return result.hasDoi();
      }
      public java.lang.String getDoi() {
        return result.getDoi();
      }
      public Builder setDoi(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasDoi = true;
        result.doi_ = value;
        return this;
      }
      public Builder clearDoi() {
        result.hasDoi = false;
        result.doi_ = getDefaultInstance().getDoi();
        return this;
      }
      
      // optional string isbn = 12;
      public boolean hasIsbn() {
        return result.hasIsbn();
      }
      public java.lang.String getIsbn() {
        return result.getIsbn();
      }
      public Builder setIsbn(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasIsbn = true;
        result.isbn_ = value;
        return this;
      }
      public Builder clearIsbn() {
        result.hasIsbn = false;
        result.isbn_ = getDefaultInstance().getIsbn();
        return this;
      }
      
      // optional string issn = 13;
      public boolean hasIssn() {
        return result.hasIssn();
      }
      public java.lang.String getIssn() {
        return result.getIssn();
      }
      public Builder setIssn(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasIssn = true;
        result.issn_ = value;
        return this;
      }
      public Builder clearIssn() {
        result.hasIssn = false;
        result.issn_ = getDefaultInstance().getIssn();
        return this;
      }
      
      // optional string issue = 14;
      public boolean hasIssue() {
        return result.hasIssue();
      }
      public java.lang.String getIssue() {
        return result.getIssue();
      }
      public Builder setIssue(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasIssue = true;
        result.issue_ = value;
        return this;
      }
      public Builder clearIssue() {
        result.hasIssue = false;
        result.issue_ = getDefaultInstance().getIssue();
        return this;
      }
      
      // optional string journal = 15;
      public boolean hasJournal() {
        return result.hasJournal();
      }
      public java.lang.String getJournal() {
        return result.getJournal();
      }
      public Builder setJournal(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasJournal = true;
        result.journal_ = value;
        return this;
      }
      public Builder clearJournal() {
        result.hasJournal = false;
        result.journal_ = getDefaultInstance().getJournal();
        return this;
      }
      
      // optional .ExtId extId = 16;
      public boolean hasExtId() {
        return result.hasExtId();
      }
      public pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId getExtId() {
        return result.getExtId();
      }
      public Builder setExtId(pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId value) {
        if (value == null) {
          throw new NullPointerException();
        }
        result.hasExtId = true;
        result.extId_ = value;
        return this;
      }
      public Builder setExtId(pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId.Builder builderForValue) {
        result.hasExtId = true;
        result.extId_ = builderForValue.build();
        return this;
      }
      public Builder mergeExtId(pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId value) {
        if (result.hasExtId() &&
            result.extId_ != pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId.getDefaultInstance()) {
          result.extId_ =
            pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId.newBuilder(result.extId_).mergeFrom(value).buildPartial();
        } else {
          result.extId_ = value;
        }
        result.hasExtId = true;
        return this;
      }
      public Builder clearExtId() {
        result.hasExtId = false;
        result.extId_ = pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId.getDefaultInstance();
        return this;
      }
      
      // repeated .ClassifCode classifCode = 17;
      public java.util.List<pl.edu.icm.coansys.importers.model.DocumentProtos.ClassifCode> getClassifCodeList() {
        return java.util.Collections.unmodifiableList(result.classifCode_);
      }
      public int getClassifCodeCount() {
        return result.getClassifCodeCount();
      }
      public pl.edu.icm.coansys.importers.model.DocumentProtos.ClassifCode getClassifCode(int index) {
        return result.getClassifCode(index);
      }
      public Builder setClassifCode(int index, pl.edu.icm.coansys.importers.model.DocumentProtos.ClassifCode value) {
        if (value == null) {
          throw new NullPointerException();
        }
        result.classifCode_.set(index, value);
        return this;
      }
      public Builder setClassifCode(int index, pl.edu.icm.coansys.importers.model.DocumentProtos.ClassifCode.Builder builderForValue) {
        result.classifCode_.set(index, builderForValue.build());
        return this;
      }
      public Builder addClassifCode(pl.edu.icm.coansys.importers.model.DocumentProtos.ClassifCode value) {
        if (value == null) {
          throw new NullPointerException();
        }
        if (result.classifCode_.isEmpty()) {
          result.classifCode_ = new java.util.ArrayList<pl.edu.icm.coansys.importers.model.DocumentProtos.ClassifCode>();
        }
        result.classifCode_.add(value);
        return this;
      }
      public Builder addClassifCode(pl.edu.icm.coansys.importers.model.DocumentProtos.ClassifCode.Builder builderForValue) {
        if (result.classifCode_.isEmpty()) {
          result.classifCode_ = new java.util.ArrayList<pl.edu.icm.coansys.importers.model.DocumentProtos.ClassifCode>();
        }
        result.classifCode_.add(builderForValue.build());
        return this;
      }
      public Builder addAllClassifCode(
          java.lang.Iterable<? extends pl.edu.icm.coansys.importers.model.DocumentProtos.ClassifCode> values) {
        if (result.classifCode_.isEmpty()) {
          result.classifCode_ = new java.util.ArrayList<pl.edu.icm.coansys.importers.model.DocumentProtos.ClassifCode>();
        }
        super.addAll(values, result.classifCode_);
        return this;
      }
      public Builder clearClassifCode() {
        result.classifCode_ = java.util.Collections.emptyList();
        return this;
      }
      
      // optional string pages = 18;
      public boolean hasPages() {
        return result.hasPages();
      }
      public java.lang.String getPages() {
        return result.getPages();
      }
      public Builder setPages(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasPages = true;
        result.pages_ = value;
        return this;
      }
      public Builder clearPages() {
        result.hasPages = false;
        result.pages_ = getDefaultInstance().getPages();
        return this;
      }
      
      // optional string source = 19;
      public boolean hasSource() {
        return result.hasSource();
      }
      public java.lang.String getSource() {
        return result.getSource();
      }
      public Builder setSource(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasSource = true;
        result.source_ = value;
        return this;
      }
      public Builder clearSource() {
        result.hasSource = false;
        result.source_ = getDefaultInstance().getSource();
        return this;
      }
      
      // optional string text = 20;
      public boolean hasText() {
        return result.hasText();
      }
      public java.lang.String getText() {
        return result.getText();
      }
      public Builder setText(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasText = true;
        result.text_ = value;
        return this;
      }
      public Builder clearText() {
        result.hasText = false;
        result.text_ = getDefaultInstance().getText();
        return this;
      }
      
      // optional string volume = 21;
      public boolean hasVolume() {
        return result.hasVolume();
      }
      public java.lang.String getVolume() {
        return result.getVolume();
      }
      public Builder setVolume(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasVolume = true;
        result.volume_ = value;
        return this;
      }
      public Builder clearVolume() {
        result.hasVolume = false;
        result.volume_ = getDefaultInstance().getVolume();
        return this;
      }
      
      // optional bool clusterActive = 22;
      public boolean hasClusterActive() {
        return result.hasClusterActive();
      }
      public boolean getClusterActive() {
        return result.getClusterActive();
      }
      public Builder setClusterActive(boolean value) {
        result.hasClusterActive = true;
        result.clusterActive_ = value;
        return this;
      }
      public Builder clearClusterActive() {
        result.hasClusterActive = false;
        result.clusterActive_ = false;
        return this;
      }
      
      // repeated string clusterContents = 23;
      public java.util.List<java.lang.String> getClusterContentsList() {
        return java.util.Collections.unmodifiableList(result.clusterContents_);
      }
      public int getClusterContentsCount() {
        return result.getClusterContentsCount();
      }
      public java.lang.String getClusterContents(int index) {
        return result.getClusterContents(index);
      }
      public Builder setClusterContents(int index, java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.clusterContents_.set(index, value);
        return this;
      }
      public Builder addClusterContents(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  if (result.clusterContents_.isEmpty()) {
          result.clusterContents_ = new java.util.ArrayList<java.lang.String>();
        }
        result.clusterContents_.add(value);
        return this;
      }
      public Builder addAllClusterContents(
          java.lang.Iterable<? extends java.lang.String> values) {
        if (result.clusterContents_.isEmpty()) {
          result.clusterContents_ = new java.util.ArrayList<java.lang.String>();
        }
        super.addAll(values, result.clusterContents_);
        return this;
      }
      public Builder clearClusterContents() {
        result.clusterContents_ = java.util.Collections.emptyList();
        return this;
      }
      
      // optional string partOfCluster = 24;
      public boolean hasPartOfCluster() {
        return result.hasPartOfCluster();
      }
      public java.lang.String getPartOfCluster() {
        return result.getPartOfCluster();
      }
      public Builder setPartOfCluster(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasPartOfCluster = true;
        result.partOfCluster_ = value;
        return this;
      }
      public Builder clearPartOfCluster() {
        result.hasPartOfCluster = false;
        result.partOfCluster_ = getDefaultInstance().getPartOfCluster();
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:DocumentMetadata)
    }
    
    static {
      defaultInstance = new DocumentMetadata(true);
      pl.edu.icm.coansys.importers.model.DocumentProtos.internalForceInit();
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:DocumentMetadata)
  }
  
  public static final class Media extends
      com.google.protobuf.GeneratedMessage {
    // Use Media.newBuilder() to construct.
    private Media() {
      initFields();
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
      return pl.edu.icm.coansys.importers.model.DocumentProtos.internal_static_Media_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return pl.edu.icm.coansys.importers.model.DocumentProtos.internal_static_Media_fieldAccessorTable;
    }
    
    // required string key = 1;
    public static final int KEY_FIELD_NUMBER = 1;
    private boolean hasKey;
    private java.lang.String key_ = "";
    public boolean hasKey() { return hasKey; }
    public java.lang.String getKey() { return key_; }
    
    // required string mediaType = 2;
    public static final int MEDIATYPE_FIELD_NUMBER = 2;
    private boolean hasMediaType;
    private java.lang.String mediaType_ = "";
    public boolean hasMediaType() { return hasMediaType; }
    public java.lang.String getMediaType() { return mediaType_; }
    
    // required bytes content = 3;
    public static final int CONTENT_FIELD_NUMBER = 3;
    private boolean hasContent;
    private com.google.protobuf.ByteString content_ = com.google.protobuf.ByteString.EMPTY;
    public boolean hasContent() { return hasContent; }
    public com.google.protobuf.ByteString getContent() { return content_; }
    
    private void initFields() {
    }
    public final boolean isInitialized() {
      if (!hasKey) return false;
      if (!hasMediaType) return false;
      if (!hasContent) return false;
      return true;
    }
    
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (hasKey()) {
        output.writeString(1, getKey());
      }
      if (hasMediaType()) {
        output.writeString(2, getMediaType());
      }
      if (hasContent()) {
        output.writeBytes(3, getContent());
      }
      getUnknownFields().writeTo(output);
    }
    
    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;
    
      size = 0;
      if (hasKey()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(1, getKey());
      }
      if (hasMediaType()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(2, getMediaType());
      }
      if (hasContent()) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(3, getContent());
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }
    
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.Media parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.Media parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.Media parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.Media parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.Media parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.Media parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.Media parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.Media parseDelimitedFrom(
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
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.Media parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.Media parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(pl.edu.icm.coansys.importers.model.DocumentProtos.Media prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }
    
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> {
      private pl.edu.icm.coansys.importers.model.DocumentProtos.Media result;
      
      // Construct using pl.edu.icm.coansys.importers.model.DocumentProtos.Media.newBuilder()
      private Builder() {}
      
      private static Builder create() {
        Builder builder = new Builder();
        builder.result = new pl.edu.icm.coansys.importers.model.DocumentProtos.Media();
        return builder;
      }
      
      protected pl.edu.icm.coansys.importers.model.DocumentProtos.Media internalGetResult() {
        return result;
      }
      
      public Builder clear() {
        if (result == null) {
          throw new IllegalStateException(
            "Cannot call clear() after build().");
        }
        result = new pl.edu.icm.coansys.importers.model.DocumentProtos.Media();
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(result);
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return pl.edu.icm.coansys.importers.model.DocumentProtos.Media.getDescriptor();
      }
      
      public pl.edu.icm.coansys.importers.model.DocumentProtos.Media getDefaultInstanceForType() {
        return pl.edu.icm.coansys.importers.model.DocumentProtos.Media.getDefaultInstance();
      }
      
      public boolean isInitialized() {
        return result.isInitialized();
      }
      public pl.edu.icm.coansys.importers.model.DocumentProtos.Media build() {
        if (result != null && !isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return buildPartial();
      }
      
      private pl.edu.icm.coansys.importers.model.DocumentProtos.Media buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        if (!isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return buildPartial();
      }
      
      public pl.edu.icm.coansys.importers.model.DocumentProtos.Media buildPartial() {
        if (result == null) {
          throw new IllegalStateException(
            "build() has already been called on this Builder.");
        }
        pl.edu.icm.coansys.importers.model.DocumentProtos.Media returnMe = result;
        result = null;
        return returnMe;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof pl.edu.icm.coansys.importers.model.DocumentProtos.Media) {
          return mergeFrom((pl.edu.icm.coansys.importers.model.DocumentProtos.Media)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(pl.edu.icm.coansys.importers.model.DocumentProtos.Media other) {
        if (other == pl.edu.icm.coansys.importers.model.DocumentProtos.Media.getDefaultInstance()) return this;
        if (other.hasKey()) {
          setKey(other.getKey());
        }
        if (other.hasMediaType()) {
          setMediaType(other.getMediaType());
        }
        if (other.hasContent()) {
          setContent(other.getContent());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
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
              return this;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                this.setUnknownFields(unknownFields.build());
                return this;
              }
              break;
            }
            case 10: {
              setKey(input.readString());
              break;
            }
            case 18: {
              setMediaType(input.readString());
              break;
            }
            case 26: {
              setContent(input.readBytes());
              break;
            }
          }
        }
      }
      
      
      // required string key = 1;
      public boolean hasKey() {
        return result.hasKey();
      }
      public java.lang.String getKey() {
        return result.getKey();
      }
      public Builder setKey(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasKey = true;
        result.key_ = value;
        return this;
      }
      public Builder clearKey() {
        result.hasKey = false;
        result.key_ = getDefaultInstance().getKey();
        return this;
      }
      
      // required string mediaType = 2;
      public boolean hasMediaType() {
        return result.hasMediaType();
      }
      public java.lang.String getMediaType() {
        return result.getMediaType();
      }
      public Builder setMediaType(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasMediaType = true;
        result.mediaType_ = value;
        return this;
      }
      public Builder clearMediaType() {
        result.hasMediaType = false;
        result.mediaType_ = getDefaultInstance().getMediaType();
        return this;
      }
      
      // required bytes content = 3;
      public boolean hasContent() {
        return result.hasContent();
      }
      public com.google.protobuf.ByteString getContent() {
        return result.getContent();
      }
      public Builder setContent(com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasContent = true;
        result.content_ = value;
        return this;
      }
      public Builder clearContent() {
        result.hasContent = false;
        result.content_ = getDefaultInstance().getContent();
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:Media)
    }
    
    static {
      defaultInstance = new Media(true);
      pl.edu.icm.coansys.importers.model.DocumentProtos.internalForceInit();
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:Media)
  }
  
  public static final class MediaConteiner extends
      com.google.protobuf.GeneratedMessage {
    // Use MediaConteiner.newBuilder() to construct.
    private MediaConteiner() {
      initFields();
    }
    private MediaConteiner(boolean noInit) {}
    
    private static final MediaConteiner defaultInstance;
    public static MediaConteiner getDefaultInstance() {
      return defaultInstance;
    }
    
    public MediaConteiner getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return pl.edu.icm.coansys.importers.model.DocumentProtos.internal_static_MediaConteiner_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return pl.edu.icm.coansys.importers.model.DocumentProtos.internal_static_MediaConteiner_fieldAccessorTable;
    }
    
    // repeated .Media media = 1;
    public static final int MEDIA_FIELD_NUMBER = 1;
    private java.util.List<pl.edu.icm.coansys.importers.model.DocumentProtos.Media> media_ =
      java.util.Collections.emptyList();
    public java.util.List<pl.edu.icm.coansys.importers.model.DocumentProtos.Media> getMediaList() {
      return media_;
    }
    public int getMediaCount() { return media_.size(); }
    public pl.edu.icm.coansys.importers.model.DocumentProtos.Media getMedia(int index) {
      return media_.get(index);
    }
    
    private void initFields() {
    }
    public final boolean isInitialized() {
      for (pl.edu.icm.coansys.importers.model.DocumentProtos.Media element : getMediaList()) {
        if (!element.isInitialized()) return false;
      }
      return true;
    }
    
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      for (pl.edu.icm.coansys.importers.model.DocumentProtos.Media element : getMediaList()) {
        output.writeMessage(1, element);
      }
      getUnknownFields().writeTo(output);
    }
    
    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;
    
      size = 0;
      for (pl.edu.icm.coansys.importers.model.DocumentProtos.Media element : getMediaList()) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(1, element);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }
    
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.MediaConteiner parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.MediaConteiner parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.MediaConteiner parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.MediaConteiner parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.MediaConteiner parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.MediaConteiner parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.MediaConteiner parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.MediaConteiner parseDelimitedFrom(
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
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.MediaConteiner parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static pl.edu.icm.coansys.importers.model.DocumentProtos.MediaConteiner parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(pl.edu.icm.coansys.importers.model.DocumentProtos.MediaConteiner prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }
    
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> {
      private pl.edu.icm.coansys.importers.model.DocumentProtos.MediaConteiner result;
      
      // Construct using pl.edu.icm.coansys.importers.model.DocumentProtos.MediaConteiner.newBuilder()
      private Builder() {}
      
      private static Builder create() {
        Builder builder = new Builder();
        builder.result = new pl.edu.icm.coansys.importers.model.DocumentProtos.MediaConteiner();
        return builder;
      }
      
      protected pl.edu.icm.coansys.importers.model.DocumentProtos.MediaConteiner internalGetResult() {
        return result;
      }
      
      public Builder clear() {
        if (result == null) {
          throw new IllegalStateException(
            "Cannot call clear() after build().");
        }
        result = new pl.edu.icm.coansys.importers.model.DocumentProtos.MediaConteiner();
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(result);
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return pl.edu.icm.coansys.importers.model.DocumentProtos.MediaConteiner.getDescriptor();
      }
      
      public pl.edu.icm.coansys.importers.model.DocumentProtos.MediaConteiner getDefaultInstanceForType() {
        return pl.edu.icm.coansys.importers.model.DocumentProtos.MediaConteiner.getDefaultInstance();
      }
      
      public boolean isInitialized() {
        return result.isInitialized();
      }
      public pl.edu.icm.coansys.importers.model.DocumentProtos.MediaConteiner build() {
        if (result != null && !isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return buildPartial();
      }
      
      private pl.edu.icm.coansys.importers.model.DocumentProtos.MediaConteiner buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        if (!isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return buildPartial();
      }
      
      public pl.edu.icm.coansys.importers.model.DocumentProtos.MediaConteiner buildPartial() {
        if (result == null) {
          throw new IllegalStateException(
            "build() has already been called on this Builder.");
        }
        if (result.media_ != java.util.Collections.EMPTY_LIST) {
          result.media_ =
            java.util.Collections.unmodifiableList(result.media_);
        }
        pl.edu.icm.coansys.importers.model.DocumentProtos.MediaConteiner returnMe = result;
        result = null;
        return returnMe;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof pl.edu.icm.coansys.importers.model.DocumentProtos.MediaConteiner) {
          return mergeFrom((pl.edu.icm.coansys.importers.model.DocumentProtos.MediaConteiner)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(pl.edu.icm.coansys.importers.model.DocumentProtos.MediaConteiner other) {
        if (other == pl.edu.icm.coansys.importers.model.DocumentProtos.MediaConteiner.getDefaultInstance()) return this;
        if (!other.media_.isEmpty()) {
          if (result.media_.isEmpty()) {
            result.media_ = new java.util.ArrayList<pl.edu.icm.coansys.importers.model.DocumentProtos.Media>();
          }
          result.media_.addAll(other.media_);
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
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
              return this;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                this.setUnknownFields(unknownFields.build());
                return this;
              }
              break;
            }
            case 10: {
              pl.edu.icm.coansys.importers.model.DocumentProtos.Media.Builder subBuilder = pl.edu.icm.coansys.importers.model.DocumentProtos.Media.newBuilder();
              input.readMessage(subBuilder, extensionRegistry);
              addMedia(subBuilder.buildPartial());
              break;
            }
          }
        }
      }
      
      
      // repeated .Media media = 1;
      public java.util.List<pl.edu.icm.coansys.importers.model.DocumentProtos.Media> getMediaList() {
        return java.util.Collections.unmodifiableList(result.media_);
      }
      public int getMediaCount() {
        return result.getMediaCount();
      }
      public pl.edu.icm.coansys.importers.model.DocumentProtos.Media getMedia(int index) {
        return result.getMedia(index);
      }
      public Builder setMedia(int index, pl.edu.icm.coansys.importers.model.DocumentProtos.Media value) {
        if (value == null) {
          throw new NullPointerException();
        }
        result.media_.set(index, value);
        return this;
      }
      public Builder setMedia(int index, pl.edu.icm.coansys.importers.model.DocumentProtos.Media.Builder builderForValue) {
        result.media_.set(index, builderForValue.build());
        return this;
      }
      public Builder addMedia(pl.edu.icm.coansys.importers.model.DocumentProtos.Media value) {
        if (value == null) {
          throw new NullPointerException();
        }
        if (result.media_.isEmpty()) {
          result.media_ = new java.util.ArrayList<pl.edu.icm.coansys.importers.model.DocumentProtos.Media>();
        }
        result.media_.add(value);
        return this;
      }
      public Builder addMedia(pl.edu.icm.coansys.importers.model.DocumentProtos.Media.Builder builderForValue) {
        if (result.media_.isEmpty()) {
          result.media_ = new java.util.ArrayList<pl.edu.icm.coansys.importers.model.DocumentProtos.Media>();
        }
        result.media_.add(builderForValue.build());
        return this;
      }
      public Builder addAllMedia(
          java.lang.Iterable<? extends pl.edu.icm.coansys.importers.model.DocumentProtos.Media> values) {
        if (result.media_.isEmpty()) {
          result.media_ = new java.util.ArrayList<pl.edu.icm.coansys.importers.model.DocumentProtos.Media>();
        }
        super.addAll(values, result.media_);
        return this;
      }
      public Builder clearMedia() {
        result.media_ = java.util.Collections.emptyList();
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:MediaConteiner)
    }
    
    static {
      defaultInstance = new MediaConteiner(true);
      pl.edu.icm.coansys.importers.model.DocumentProtos.internalForceInit();
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:MediaConteiner)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_ClassifCode_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_ClassifCode_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_ExtId_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_ExtId_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_AffiliationRef_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_AffiliationRef_fieldAccessorTable;
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
    internal_static_DocumentMetadata_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_DocumentMetadata_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_Media_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_Media_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_MediaConteiner_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_MediaConteiner_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\tbw2.proto\",\n\013ClassifCode\022\016\n\006source\030\001 \002" +
      "(\t\022\r\n\005value\030\002 \002(\t\"&\n\005ExtId\022\016\n\006source\030\001 \002" +
      "(\t\022\r\n\005value\030\002 \002(\t\"4\n\016AffiliationRef\022\013\n\003k" +
      "ey\030\001 \002(\t\022\025\n\raffiliationId\030\002 \002(\t\"?\n\013Affil" +
      "iation\022\013\n\003key\030\001 \002(\t\022\025\n\raffiliationId\030\002 \002" +
      "(\t\022\014\n\004text\030\003 \002(\t\"\275\001\n\006Author\022\013\n\003key\030\001 \002(\t" +
      "\022\021\n\tforenames\030\002 \001(\t\022\017\n\007surname\030\003 \001(\t\022\014\n\004" +
      "name\030\004 \001(\t\022\r\n\005email\030\005 \001(\t\022\'\n\016affiliation" +
      "Ref\030\006 \003(\0132\017.AffiliationRef\022\r\n\005docId\030\007 \001(" +
      "\t\022\026\n\016positionNumber\030\010 \001(\005\022\025\n\005extId\030\t \003(\013",
      "2\006.ExtId\"\303\003\n\020DocumentMetadata\022\013\n\003key\030\001 \002" +
      "(\t\022\r\n\005title\030\002 \001(\t\022\020\n\010abstrakt\030\003 \001(\t\022\017\n\007k" +
      "eyword\030\004 \003(\t\022\027\n\006author\030\005 \003(\0132\007.Author\022$\n" +
      "\treference\030\006 \003(\0132\021.DocumentMetadata\022\026\n\016b" +
      "ibRefPosition\030\007 \001(\005\022\022\n\ncollection\030\n \001(\t\022" +
      "\013\n\003doi\030\013 \001(\t\022\014\n\004isbn\030\014 \001(\t\022\014\n\004issn\030\r \001(\t" +
      "\022\r\n\005issue\030\016 \001(\t\022\017\n\007journal\030\017 \001(\t\022\025\n\005extI" +
      "d\030\020 \001(\0132\006.ExtId\022!\n\013classifCode\030\021 \003(\0132\014.C" +
      "lassifCode\022\r\n\005pages\030\022 \001(\t\022\016\n\006source\030\023 \001(" +
      "\t\022\014\n\004text\030\024 \001(\t\022\016\n\006volume\030\025 \001(\t\022\025\n\rclust",
      "erActive\030\026 \001(\010\022\027\n\017clusterContents\030\027 \003(\t\022" +
      "\025\n\rpartOfCluster\030\030 \001(\t\"8\n\005Media\022\013\n\003key\030\001" +
      " \002(\t\022\021\n\tmediaType\030\002 \002(\t\022\017\n\007content\030\003 \002(\014" +
      "\"\'\n\016MediaConteiner\022\025\n\005media\030\001 \003(\0132\006.Medi" +
      "aB4\n\"pl.edu.icm.coansys.importers.modelB" +
      "\016DocumentProtos"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_ClassifCode_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_ClassifCode_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_ClassifCode_descriptor,
              new java.lang.String[] { "Source", "Value", },
              pl.edu.icm.coansys.importers.model.DocumentProtos.ClassifCode.class,
              pl.edu.icm.coansys.importers.model.DocumentProtos.ClassifCode.Builder.class);
          internal_static_ExtId_descriptor =
            getDescriptor().getMessageTypes().get(1);
          internal_static_ExtId_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_ExtId_descriptor,
              new java.lang.String[] { "Source", "Value", },
              pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId.class,
              pl.edu.icm.coansys.importers.model.DocumentProtos.ExtId.Builder.class);
          internal_static_AffiliationRef_descriptor =
            getDescriptor().getMessageTypes().get(2);
          internal_static_AffiliationRef_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_AffiliationRef_descriptor,
              new java.lang.String[] { "Key", "AffiliationId", },
              pl.edu.icm.coansys.importers.model.DocumentProtos.AffiliationRef.class,
              pl.edu.icm.coansys.importers.model.DocumentProtos.AffiliationRef.Builder.class);
          internal_static_Affiliation_descriptor =
            getDescriptor().getMessageTypes().get(3);
          internal_static_Affiliation_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_Affiliation_descriptor,
              new java.lang.String[] { "Key", "AffiliationId", "Text", },
              pl.edu.icm.coansys.importers.model.DocumentProtos.Affiliation.class,
              pl.edu.icm.coansys.importers.model.DocumentProtos.Affiliation.Builder.class);
          internal_static_Author_descriptor =
            getDescriptor().getMessageTypes().get(4);
          internal_static_Author_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_Author_descriptor,
              new java.lang.String[] { "Key", "Forenames", "Surname", "Name", "Email", "AffiliationRef", "DocId", "PositionNumber", "ExtId", },
              pl.edu.icm.coansys.importers.model.DocumentProtos.Author.class,
              pl.edu.icm.coansys.importers.model.DocumentProtos.Author.Builder.class);
          internal_static_DocumentMetadata_descriptor =
            getDescriptor().getMessageTypes().get(5);
          internal_static_DocumentMetadata_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_DocumentMetadata_descriptor,
              new java.lang.String[] { "Key", "Title", "Abstrakt", "Keyword", "Author", "Reference", "BibRefPosition", "Collection", "Doi", "Isbn", "Issn", "Issue", "Journal", "ExtId", "ClassifCode", "Pages", "Source", "Text", "Volume", "ClusterActive", "ClusterContents", "PartOfCluster", },
              pl.edu.icm.coansys.importers.model.DocumentProtos.DocumentMetadata.class,
              pl.edu.icm.coansys.importers.model.DocumentProtos.DocumentMetadata.Builder.class);
          internal_static_Media_descriptor =
            getDescriptor().getMessageTypes().get(6);
          internal_static_Media_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_Media_descriptor,
              new java.lang.String[] { "Key", "MediaType", "Content", },
              pl.edu.icm.coansys.importers.model.DocumentProtos.Media.class,
              pl.edu.icm.coansys.importers.model.DocumentProtos.Media.Builder.class);
          internal_static_MediaConteiner_descriptor =
            getDescriptor().getMessageTypes().get(7);
          internal_static_MediaConteiner_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_MediaConteiner_descriptor,
              new java.lang.String[] { "Media", },
              pl.edu.icm.coansys.importers.model.DocumentProtos.MediaConteiner.class,
              pl.edu.icm.coansys.importers.model.DocumentProtos.MediaConteiner.Builder.class);
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
  }
  
  public static void internalForceInit() {}
  
  // @@protoc_insertion_point(outer_class_scope)
}
