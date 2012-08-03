/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.importers.hbaseRest.model_toy;

public final class AddressBookProtos {
  private AddressBookProtos() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface PersonOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // required string name = 1;
    boolean hasName();
    String getName();
    
    // required int32 id = 2;
    boolean hasId();
    int getId();
    
    // optional string email = 3;
    boolean hasEmail();
    String getEmail();
    
    // repeated .tutorial.Person.PhoneNumber phone = 4;
    java.util.List<pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumber> 
        getPhoneList();
    pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumber getPhone(int index);
    int getPhoneCount();
    java.util.List<? extends pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumberOrBuilder> 
        getPhoneOrBuilderList();
    pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumberOrBuilder getPhoneOrBuilder(
        int index);
  }
  public static final class Person extends
      com.google.protobuf.GeneratedMessage
      implements PersonOrBuilder {
    // Use Person.newBuilder() to construct.
    private Person(Builder builder) {
      super(builder);
    }
    private Person(boolean noInit) {}
    
    private static final Person defaultInstance;
    public static Person getDefaultInstance() {
      return defaultInstance;
    }
    
    public Person getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.internal_static_tutorial_Person_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.internal_static_tutorial_Person_fieldAccessorTable;
    }
    
    public enum PhoneType
        implements com.google.protobuf.ProtocolMessageEnum {
      MOBILE(0, 0),
      HOME(1, 1),
      WORK(2, 2),
      ;
      
      public static final int MOBILE_VALUE = 0;
      public static final int HOME_VALUE = 1;
      public static final int WORK_VALUE = 2;
      
      
      public final int getNumber() { return value; }
      
      public static PhoneType valueOf(int value) {
        switch (value) {
          case 0: return MOBILE;
          case 1: return HOME;
          case 2: return WORK;
          default: return null;
        }
      }
      
      public static com.google.protobuf.Internal.EnumLiteMap<PhoneType>
          internalGetValueMap() {
        return internalValueMap;
      }
      private static com.google.protobuf.Internal.EnumLiteMap<PhoneType>
          internalValueMap =
            new com.google.protobuf.Internal.EnumLiteMap<PhoneType>() {
              public PhoneType findValueByNumber(int number) {
                return PhoneType.valueOf(number);
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
        return pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.getDescriptor().getEnumTypes().get(0);
      }
      
      private static final PhoneType[] VALUES = {
        MOBILE, HOME, WORK, 
      };
      
      public static PhoneType valueOf(
          com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
        if (desc.getType() != getDescriptor()) {
          throw new java.lang.IllegalArgumentException(
            "EnumValueDescriptor is not for this type.");
        }
        return VALUES[desc.getIndex()];
      }
      
      private final int index;
      private final int value;
      
      private PhoneType(int index, int value) {
        this.index = index;
        this.value = value;
      }
      
      // @@protoc_insertion_point(enum_scope:tutorial.Person.PhoneType)
    }
    
    public interface PhoneNumberOrBuilder
        extends com.google.protobuf.MessageOrBuilder {
      
      // required string number = 1;
      boolean hasNumber();
      String getNumber();
      
      // optional .tutorial.Person.PhoneType type = 2 [default = HOME];
      boolean hasType();
      pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneType getType();
    }
    public static final class PhoneNumber extends
        com.google.protobuf.GeneratedMessage
        implements PhoneNumberOrBuilder {
      // Use PhoneNumber.newBuilder() to construct.
      private PhoneNumber(Builder builder) {
        super(builder);
      }
      private PhoneNumber(boolean noInit) {}
      
      private static final PhoneNumber defaultInstance;
      public static PhoneNumber getDefaultInstance() {
        return defaultInstance;
      }
      
      public PhoneNumber getDefaultInstanceForType() {
        return defaultInstance;
      }
      
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.internal_static_tutorial_Person_PhoneNumber_descriptor;
      }
      
      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.internal_static_tutorial_Person_PhoneNumber_fieldAccessorTable;
      }
      
      private int bitField0_;
      // required string number = 1;
      public static final int NUMBER_FIELD_NUMBER = 1;
      private java.lang.Object number_;
      public boolean hasNumber() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      public String getNumber() {
        java.lang.Object ref = number_;
        if (ref instanceof String) {
          return (String) ref;
        } else {
          com.google.protobuf.ByteString bs = 
              (com.google.protobuf.ByteString) ref;
          String s = bs.toStringUtf8();
          if (com.google.protobuf.Internal.isValidUtf8(bs)) {
            number_ = s;
          }
          return s;
        }
      }
      private com.google.protobuf.ByteString getNumberBytes() {
        java.lang.Object ref = number_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8((String) ref);
          number_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      
      // optional .tutorial.Person.PhoneType type = 2 [default = HOME];
      public static final int TYPE_FIELD_NUMBER = 2;
      private pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneType type_;
      public boolean hasType() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      public pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneType getType() {
        return type_;
      }
      
      private void initFields() {
        number_ = "";
        type_ = pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneType.HOME;
      }
      private byte memoizedIsInitialized = -1;
      public final boolean isInitialized() {
        byte isInitialized = memoizedIsInitialized;
        if (isInitialized != -1) return isInitialized == 1;
        
        if (!hasNumber()) {
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
          output.writeBytes(1, getNumberBytes());
        }
        if (((bitField0_ & 0x00000002) == 0x00000002)) {
          output.writeEnum(2, type_.getNumber());
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
            .computeBytesSize(1, getNumberBytes());
        }
        if (((bitField0_ & 0x00000002) == 0x00000002)) {
          size += com.google.protobuf.CodedOutputStream
            .computeEnumSize(2, type_.getNumber());
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
      
      public static pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumber parseFrom(
          com.google.protobuf.ByteString data)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return newBuilder().mergeFrom(data).buildParsed();
      }
      public static pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumber parseFrom(
          com.google.protobuf.ByteString data,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return newBuilder().mergeFrom(data, extensionRegistry)
                 .buildParsed();
      }
      public static pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumber parseFrom(byte[] data)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return newBuilder().mergeFrom(data).buildParsed();
      }
      public static pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumber parseFrom(
          byte[] data,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return newBuilder().mergeFrom(data, extensionRegistry)
                 .buildParsed();
      }
      public static pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumber parseFrom(java.io.InputStream input)
          throws java.io.IOException {
        return newBuilder().mergeFrom(input).buildParsed();
      }
      public static pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumber parseFrom(
          java.io.InputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return newBuilder().mergeFrom(input, extensionRegistry)
                 .buildParsed();
      }
      public static pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumber parseDelimitedFrom(java.io.InputStream input)
          throws java.io.IOException {
        Builder builder = newBuilder();
        if (builder.mergeDelimitedFrom(input)) {
          return builder.buildParsed();
        } else {
          return null;
        }
      }
      public static pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumber parseDelimitedFrom(
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
      public static pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumber parseFrom(
          com.google.protobuf.CodedInputStream input)
          throws java.io.IOException {
        return newBuilder().mergeFrom(input).buildParsed();
      }
      public static pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumber parseFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return newBuilder().mergeFrom(input, extensionRegistry)
                 .buildParsed();
      }
      
      public static Builder newBuilder() { return Builder.create(); }
      public Builder newBuilderForType() { return newBuilder(); }
      public static Builder newBuilder(pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumber prototype) {
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
         implements pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumberOrBuilder {
        public static final com.google.protobuf.Descriptors.Descriptor
            getDescriptor() {
          return pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.internal_static_tutorial_Person_PhoneNumber_descriptor;
        }
        
        protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
            internalGetFieldAccessorTable() {
          return pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.internal_static_tutorial_Person_PhoneNumber_fieldAccessorTable;
        }
        
        // Construct using com.example.tutorial.AddressBookProtos.Person.PhoneNumber.newBuilder()
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
          number_ = "";
          bitField0_ = (bitField0_ & ~0x00000001);
          type_ = pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneType.HOME;
          bitField0_ = (bitField0_ & ~0x00000002);
          return this;
        }
        
        public Builder clone() {
          return create().mergeFrom(buildPartial());
        }
        
        public com.google.protobuf.Descriptors.Descriptor
            getDescriptorForType() {
          return pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumber.getDescriptor();
        }
        
        public pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumber getDefaultInstanceForType() {
          return pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumber.getDefaultInstance();
        }
        
        public pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumber build() {
          pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumber result = buildPartial();
          if (!result.isInitialized()) {
            throw newUninitializedMessageException(result);
          }
          return result;
        }
        
        private pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumber buildParsed()
            throws com.google.protobuf.InvalidProtocolBufferException {
          pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumber result = buildPartial();
          if (!result.isInitialized()) {
            throw newUninitializedMessageException(
              result).asInvalidProtocolBufferException();
          }
          return result;
        }
        
        public pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumber buildPartial() {
          pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumber result = new pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumber(this);
          int from_bitField0_ = bitField0_;
          int to_bitField0_ = 0;
          if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
            to_bitField0_ |= 0x00000001;
          }
          result.number_ = number_;
          if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
            to_bitField0_ |= 0x00000002;
          }
          result.type_ = type_;
          result.bitField0_ = to_bitField0_;
          onBuilt();
          return result;
        }
        
        public Builder mergeFrom(com.google.protobuf.Message other) {
          if (other instanceof pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumber) {
            return mergeFrom((pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumber)other);
          } else {
            super.mergeFrom(other);
            return this;
          }
        }
        
        public Builder mergeFrom(pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumber other) {
          if (other == pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumber.getDefaultInstance()) return this;
          if (other.hasNumber()) {
            setNumber(other.getNumber());
          }
          if (other.hasType()) {
            setType(other.getType());
          }
          this.mergeUnknownFields(other.getUnknownFields());
          return this;
        }
        
        public final boolean isInitialized() {
          if (!hasNumber()) {
            
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
                number_ = input.readBytes();
                break;
              }
              case 16: {
                int rawValue = input.readEnum();
                pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneType value = pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneType.valueOf(rawValue);
                if (value == null) {
                  unknownFields.mergeVarintField(2, rawValue);
                } else {
                  bitField0_ |= 0x00000002;
                  type_ = value;
                }
                break;
              }
            }
          }
        }
        
        private int bitField0_;
        
        // required string number = 1;
        private java.lang.Object number_ = "";
        public boolean hasNumber() {
          return ((bitField0_ & 0x00000001) == 0x00000001);
        }
        public String getNumber() {
          java.lang.Object ref = number_;
          if (!(ref instanceof String)) {
            String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
            number_ = s;
            return s;
          } else {
            return (String) ref;
          }
        }
        public Builder setNumber(String value) {
          if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
          number_ = value;
          onChanged();
          return this;
        }
        public Builder clearNumber() {
          bitField0_ = (bitField0_ & ~0x00000001);
          number_ = getDefaultInstance().getNumber();
          onChanged();
          return this;
        }
        void setNumber(com.google.protobuf.ByteString value) {
          bitField0_ |= 0x00000001;
          number_ = value;
          onChanged();
        }
        
        // optional .tutorial.Person.PhoneType type = 2 [default = HOME];
        private pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneType type_ = pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneType.HOME;
        public boolean hasType() {
          return ((bitField0_ & 0x00000002) == 0x00000002);
        }
        public pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneType getType() {
          return type_;
        }
        public Builder setType(pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneType value) {
          if (value == null) {
            throw new NullPointerException();
          }
          bitField0_ |= 0x00000002;
          type_ = value;
          onChanged();
          return this;
        }
        public Builder clearType() {
          bitField0_ = (bitField0_ & ~0x00000002);
          type_ = pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneType.HOME;
          onChanged();
          return this;
        }
        
        // @@protoc_insertion_point(builder_scope:tutorial.Person.PhoneNumber)
      }
      
      static {
        defaultInstance = new PhoneNumber(true);
        defaultInstance.initFields();
      }
      
      // @@protoc_insertion_point(class_scope:tutorial.Person.PhoneNumber)
    }
    
    private int bitField0_;
    // required string name = 1;
    public static final int NAME_FIELD_NUMBER = 1;
    private java.lang.Object name_;
    public boolean hasName() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
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
    
    // required int32 id = 2;
    public static final int ID_FIELD_NUMBER = 2;
    private int id_;
    public boolean hasId() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    public int getId() {
      return id_;
    }
    
    // optional string email = 3;
    public static final int EMAIL_FIELD_NUMBER = 3;
    private java.lang.Object email_;
    public boolean hasEmail() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
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
    
    // repeated .tutorial.Person.PhoneNumber phone = 4;
    public static final int PHONE_FIELD_NUMBER = 4;
    private java.util.List<pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumber> phone_;
    public java.util.List<pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumber> getPhoneList() {
      return phone_;
    }
    public java.util.List<? extends pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumberOrBuilder> 
        getPhoneOrBuilderList() {
      return phone_;
    }
    public int getPhoneCount() {
      return phone_.size();
    }
    public pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumber getPhone(int index) {
      return phone_.get(index);
    }
    public pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumberOrBuilder getPhoneOrBuilder(
        int index) {
      return phone_.get(index);
    }
    
    private void initFields() {
      name_ = "";
      id_ = 0;
      email_ = "";
      phone_ = java.util.Collections.emptyList();
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
      if (!hasName()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasId()) {
        memoizedIsInitialized = 0;
        return false;
      }
      for (int i = 0; i < getPhoneCount(); i++) {
        if (!getPhone(i).isInitialized()) {
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
        output.writeBytes(1, getNameBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeInt32(2, id_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeBytes(3, getEmailBytes());
      }
      for (int i = 0; i < phone_.size(); i++) {
        output.writeMessage(4, phone_.get(i));
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
          .computeBytesSize(1, getNameBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(2, id_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(3, getEmailBytes());
      }
      for (int i = 0; i < phone_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(4, phone_.get(i));
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
    
    public static pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person parseDelimitedFrom(
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
    public static pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person prototype) {
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
       implements pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.PersonOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.internal_static_tutorial_Person_descriptor;
      }
      
      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.internal_static_tutorial_Person_fieldAccessorTable;
      }
      
      // Construct using com.example.tutorial.AddressBookProtos.Person.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }
      
      private Builder(BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getPhoneFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }
      
      public Builder clear() {
        super.clear();
        name_ = "";
        bitField0_ = (bitField0_ & ~0x00000001);
        id_ = 0;
        bitField0_ = (bitField0_ & ~0x00000002);
        email_ = "";
        bitField0_ = (bitField0_ & ~0x00000004);
        if (phoneBuilder_ == null) {
          phone_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000008);
        } else {
          phoneBuilder_.clear();
        }
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.getDescriptor();
      }
      
      public pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person getDefaultInstanceForType() {
        return pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.getDefaultInstance();
      }
      
      public pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person build() {
        pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person buildPartial() {
        pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person result = new pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.name_ = name_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.id_ = id_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.email_ = email_;
        if (phoneBuilder_ == null) {
          if (((bitField0_ & 0x00000008) == 0x00000008)) {
            phone_ = java.util.Collections.unmodifiableList(phone_);
            bitField0_ = (bitField0_ & ~0x00000008);
          }
          result.phone_ = phone_;
        } else {
          result.phone_ = phoneBuilder_.build();
        }
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person) {
          return mergeFrom((pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person other) {
        if (other == pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.getDefaultInstance()) return this;
        if (other.hasName()) {
          setName(other.getName());
        }
        if (other.hasId()) {
          setId(other.getId());
        }
        if (other.hasEmail()) {
          setEmail(other.getEmail());
        }
        if (phoneBuilder_ == null) {
          if (!other.phone_.isEmpty()) {
            if (phone_.isEmpty()) {
              phone_ = other.phone_;
              bitField0_ = (bitField0_ & ~0x00000008);
            } else {
              ensurePhoneIsMutable();
              phone_.addAll(other.phone_);
            }
            onChanged();
          }
        } else {
          if (!other.phone_.isEmpty()) {
            if (phoneBuilder_.isEmpty()) {
              phoneBuilder_.dispose();
              phoneBuilder_ = null;
              phone_ = other.phone_;
              bitField0_ = (bitField0_ & ~0x00000008);
              phoneBuilder_ = 
                com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                   getPhoneFieldBuilder() : null;
            } else {
              phoneBuilder_.addAllMessages(other.phone_);
            }
          }
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
        if (!hasName()) {
          
          return false;
        }
        if (!hasId()) {
          
          return false;
        }
        for (int i = 0; i < getPhoneCount(); i++) {
          if (!getPhone(i).isInitialized()) {
            
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
              name_ = input.readBytes();
              break;
            }
            case 16: {
              bitField0_ |= 0x00000002;
              id_ = input.readInt32();
              break;
            }
            case 26: {
              bitField0_ |= 0x00000004;
              email_ = input.readBytes();
              break;
            }
            case 34: {
              pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumber.Builder subBuilder = pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumber.newBuilder();
              input.readMessage(subBuilder, extensionRegistry);
              addPhone(subBuilder.buildPartial());
              break;
            }
          }
        }
      }
      
      private int bitField0_;
      
      // required string name = 1;
      private java.lang.Object name_ = "";
      public boolean hasName() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
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
  bitField0_ |= 0x00000001;
        name_ = value;
        onChanged();
        return this;
      }
      public Builder clearName() {
        bitField0_ = (bitField0_ & ~0x00000001);
        name_ = getDefaultInstance().getName();
        onChanged();
        return this;
      }
      void setName(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000001;
        name_ = value;
        onChanged();
      }
      
      // required int32 id = 2;
      private int id_ ;
      public boolean hasId() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      public int getId() {
        return id_;
      }
      public Builder setId(int value) {
        bitField0_ |= 0x00000002;
        id_ = value;
        onChanged();
        return this;
      }
      public Builder clearId() {
        bitField0_ = (bitField0_ & ~0x00000002);
        id_ = 0;
        onChanged();
        return this;
      }
      
      // optional string email = 3;
      private java.lang.Object email_ = "";
      public boolean hasEmail() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
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
  bitField0_ |= 0x00000004;
        email_ = value;
        onChanged();
        return this;
      }
      public Builder clearEmail() {
        bitField0_ = (bitField0_ & ~0x00000004);
        email_ = getDefaultInstance().getEmail();
        onChanged();
        return this;
      }
      void setEmail(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000004;
        email_ = value;
        onChanged();
      }
      
      // repeated .tutorial.Person.PhoneNumber phone = 4;
      private java.util.List<pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumber> phone_ =
        java.util.Collections.emptyList();
      private void ensurePhoneIsMutable() {
        if (!((bitField0_ & 0x00000008) == 0x00000008)) {
          phone_ = new java.util.ArrayList<pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumber>(phone_);
          bitField0_ |= 0x00000008;
         }
      }
      
      private com.google.protobuf.RepeatedFieldBuilder<
          pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumber, pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumber.Builder, pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumberOrBuilder> phoneBuilder_;
      
      public java.util.List<pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumber> getPhoneList() {
        if (phoneBuilder_ == null) {
          return java.util.Collections.unmodifiableList(phone_);
        } else {
          return phoneBuilder_.getMessageList();
        }
      }
      public int getPhoneCount() {
        if (phoneBuilder_ == null) {
          return phone_.size();
        } else {
          return phoneBuilder_.getCount();
        }
      }
      public pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumber getPhone(int index) {
        if (phoneBuilder_ == null) {
          return phone_.get(index);
        } else {
          return phoneBuilder_.getMessage(index);
        }
      }
      public Builder setPhone(
          int index, pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumber value) {
        if (phoneBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensurePhoneIsMutable();
          phone_.set(index, value);
          onChanged();
        } else {
          phoneBuilder_.setMessage(index, value);
        }
        return this;
      }
      public Builder setPhone(
          int index, pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumber.Builder builderForValue) {
        if (phoneBuilder_ == null) {
          ensurePhoneIsMutable();
          phone_.set(index, builderForValue.build());
          onChanged();
        } else {
          phoneBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }
      public Builder addPhone(pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumber value) {
        if (phoneBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensurePhoneIsMutable();
          phone_.add(value);
          onChanged();
        } else {
          phoneBuilder_.addMessage(value);
        }
        return this;
      }
      public Builder addPhone(
          int index, pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumber value) {
        if (phoneBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensurePhoneIsMutable();
          phone_.add(index, value);
          onChanged();
        } else {
          phoneBuilder_.addMessage(index, value);
        }
        return this;
      }
      public Builder addPhone(
          pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumber.Builder builderForValue) {
        if (phoneBuilder_ == null) {
          ensurePhoneIsMutable();
          phone_.add(builderForValue.build());
          onChanged();
        } else {
          phoneBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }
      public Builder addPhone(
          int index, pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumber.Builder builderForValue) {
        if (phoneBuilder_ == null) {
          ensurePhoneIsMutable();
          phone_.add(index, builderForValue.build());
          onChanged();
        } else {
          phoneBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }
      public Builder addAllPhone(
          java.lang.Iterable<? extends pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumber> values) {
        if (phoneBuilder_ == null) {
          ensurePhoneIsMutable();
          super.addAll(values, phone_);
          onChanged();
        } else {
          phoneBuilder_.addAllMessages(values);
        }
        return this;
      }
      public Builder clearPhone() {
        if (phoneBuilder_ == null) {
          phone_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000008);
          onChanged();
        } else {
          phoneBuilder_.clear();
        }
        return this;
      }
      public Builder removePhone(int index) {
        if (phoneBuilder_ == null) {
          ensurePhoneIsMutable();
          phone_.remove(index);
          onChanged();
        } else {
          phoneBuilder_.remove(index);
        }
        return this;
      }
      public pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumber.Builder getPhoneBuilder(
          int index) {
        return getPhoneFieldBuilder().getBuilder(index);
      }
      public pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumberOrBuilder getPhoneOrBuilder(
          int index) {
        if (phoneBuilder_ == null) {
          return phone_.get(index);  } else {
          return phoneBuilder_.getMessageOrBuilder(index);
        }
      }
      public java.util.List<? extends pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumberOrBuilder> 
           getPhoneOrBuilderList() {
        if (phoneBuilder_ != null) {
          return phoneBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(phone_);
        }
      }
      public pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumber.Builder addPhoneBuilder() {
        return getPhoneFieldBuilder().addBuilder(
            pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumber.getDefaultInstance());
      }
      public pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumber.Builder addPhoneBuilder(
          int index) {
        return getPhoneFieldBuilder().addBuilder(
            index, pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumber.getDefaultInstance());
      }
      public java.util.List<pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumber.Builder> 
           getPhoneBuilderList() {
        return getPhoneFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilder<
          pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumber, pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumber.Builder, pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumberOrBuilder> 
          getPhoneFieldBuilder() {
        if (phoneBuilder_ == null) {
          phoneBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
              pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumber, pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumber.Builder, pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumberOrBuilder>(
                  phone_,
                  ((bitField0_ & 0x00000008) == 0x00000008),
                  getParentForChildren(),
                  isClean());
          phone_ = null;
        }
        return phoneBuilder_;
      }
      
      // @@protoc_insertion_point(builder_scope:tutorial.Person)
    }
    
    static {
      defaultInstance = new Person(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:tutorial.Person)
  }
  
  public interface AddressBookOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // repeated .tutorial.Person person = 1;
    java.util.List<pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person> 
        getPersonList();
    pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person getPerson(int index);
    int getPersonCount();
    java.util.List<? extends pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.PersonOrBuilder> 
        getPersonOrBuilderList();
    pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.PersonOrBuilder getPersonOrBuilder(
        int index);
  }
  public static final class AddressBook extends
      com.google.protobuf.GeneratedMessage
      implements AddressBookOrBuilder {
    // Use AddressBook.newBuilder() to construct.
    private AddressBook(Builder builder) {
      super(builder);
    }
    private AddressBook(boolean noInit) {}
    
    private static final AddressBook defaultInstance;
    public static AddressBook getDefaultInstance() {
      return defaultInstance;
    }
    
    public AddressBook getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.internal_static_tutorial_AddressBook_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.internal_static_tutorial_AddressBook_fieldAccessorTable;
    }
    
    // repeated .tutorial.Person person = 1;
    public static final int PERSON_FIELD_NUMBER = 1;
    private java.util.List<pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person> person_;
    public java.util.List<pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person> getPersonList() {
      return person_;
    }
    public java.util.List<? extends pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.PersonOrBuilder> 
        getPersonOrBuilderList() {
      return person_;
    }
    public int getPersonCount() {
      return person_.size();
    }
    public pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person getPerson(int index) {
      return person_.get(index);
    }
    public pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.PersonOrBuilder getPersonOrBuilder(
        int index) {
      return person_.get(index);
    }
    
    private void initFields() {
      person_ = java.util.Collections.emptyList();
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
      for (int i = 0; i < getPersonCount(); i++) {
        if (!getPerson(i).isInitialized()) {
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
      for (int i = 0; i < person_.size(); i++) {
        output.writeMessage(1, person_.get(i));
      }
      getUnknownFields().writeTo(output);
    }
    
    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;
    
      size = 0;
      for (int i = 0; i < person_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(1, person_.get(i));
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
    
    public static pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.AddressBook parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.AddressBook parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.AddressBook parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.AddressBook parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.AddressBook parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.AddressBook parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.AddressBook parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.AddressBook parseDelimitedFrom(
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
    public static pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.AddressBook parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.AddressBook parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.AddressBook prototype) {
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
       implements pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.AddressBookOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.internal_static_tutorial_AddressBook_descriptor;
      }
      
      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.internal_static_tutorial_AddressBook_fieldAccessorTable;
      }
      
      // Construct using com.example.tutorial.AddressBookProtos.AddressBook.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }
      
      private Builder(BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getPersonFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }
      
      public Builder clear() {
        super.clear();
        if (personBuilder_ == null) {
          person_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000001);
        } else {
          personBuilder_.clear();
        }
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.AddressBook.getDescriptor();
      }
      
      public pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.AddressBook getDefaultInstanceForType() {
        return pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.AddressBook.getDefaultInstance();
      }
      
      public pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.AddressBook build() {
        pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.AddressBook result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.AddressBook buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.AddressBook result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.AddressBook buildPartial() {
        pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.AddressBook result = new pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.AddressBook(this);
        int from_bitField0_ = bitField0_;
        if (personBuilder_ == null) {
          if (((bitField0_ & 0x00000001) == 0x00000001)) {
            person_ = java.util.Collections.unmodifiableList(person_);
            bitField0_ = (bitField0_ & ~0x00000001);
          }
          result.person_ = person_;
        } else {
          result.person_ = personBuilder_.build();
        }
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.AddressBook) {
          return mergeFrom((pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.AddressBook)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.AddressBook other) {
        if (other == pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.AddressBook.getDefaultInstance()) return this;
        if (personBuilder_ == null) {
          if (!other.person_.isEmpty()) {
            if (person_.isEmpty()) {
              person_ = other.person_;
              bitField0_ = (bitField0_ & ~0x00000001);
            } else {
              ensurePersonIsMutable();
              person_.addAll(other.person_);
            }
            onChanged();
          }
        } else {
          if (!other.person_.isEmpty()) {
            if (personBuilder_.isEmpty()) {
              personBuilder_.dispose();
              personBuilder_ = null;
              person_ = other.person_;
              bitField0_ = (bitField0_ & ~0x00000001);
              personBuilder_ = 
                com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                   getPersonFieldBuilder() : null;
            } else {
              personBuilder_.addAllMessages(other.person_);
            }
          }
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
        for (int i = 0; i < getPersonCount(); i++) {
          if (!getPerson(i).isInitialized()) {
            
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
              pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.Builder subBuilder = pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.newBuilder();
              input.readMessage(subBuilder, extensionRegistry);
              addPerson(subBuilder.buildPartial());
              break;
            }
          }
        }
      }
      
      private int bitField0_;
      
      // repeated .tutorial.Person person = 1;
      private java.util.List<pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person> person_ =
        java.util.Collections.emptyList();
      private void ensurePersonIsMutable() {
        if (!((bitField0_ & 0x00000001) == 0x00000001)) {
          person_ = new java.util.ArrayList<pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person>(person_);
          bitField0_ |= 0x00000001;
         }
      }
      
      private com.google.protobuf.RepeatedFieldBuilder<
          pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person, pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.Builder, pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.PersonOrBuilder> personBuilder_;
      
      public java.util.List<pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person> getPersonList() {
        if (personBuilder_ == null) {
          return java.util.Collections.unmodifiableList(person_);
        } else {
          return personBuilder_.getMessageList();
        }
      }
      public int getPersonCount() {
        if (personBuilder_ == null) {
          return person_.size();
        } else {
          return personBuilder_.getCount();
        }
      }
      public pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person getPerson(int index) {
        if (personBuilder_ == null) {
          return person_.get(index);
        } else {
          return personBuilder_.getMessage(index);
        }
      }
      public Builder setPerson(
          int index, pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person value) {
        if (personBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensurePersonIsMutable();
          person_.set(index, value);
          onChanged();
        } else {
          personBuilder_.setMessage(index, value);
        }
        return this;
      }
      public Builder setPerson(
          int index, pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.Builder builderForValue) {
        if (personBuilder_ == null) {
          ensurePersonIsMutable();
          person_.set(index, builderForValue.build());
          onChanged();
        } else {
          personBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }
      public Builder addPerson(pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person value) {
        if (personBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensurePersonIsMutable();
          person_.add(value);
          onChanged();
        } else {
          personBuilder_.addMessage(value);
        }
        return this;
      }
      public Builder addPerson(
          int index, pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person value) {
        if (personBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensurePersonIsMutable();
          person_.add(index, value);
          onChanged();
        } else {
          personBuilder_.addMessage(index, value);
        }
        return this;
      }
      public Builder addPerson(
          pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.Builder builderForValue) {
        if (personBuilder_ == null) {
          ensurePersonIsMutable();
          person_.add(builderForValue.build());
          onChanged();
        } else {
          personBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }
      public Builder addPerson(
          int index, pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.Builder builderForValue) {
        if (personBuilder_ == null) {
          ensurePersonIsMutable();
          person_.add(index, builderForValue.build());
          onChanged();
        } else {
          personBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }
      public Builder addAllPerson(
          java.lang.Iterable<? extends pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person> values) {
        if (personBuilder_ == null) {
          ensurePersonIsMutable();
          super.addAll(values, person_);
          onChanged();
        } else {
          personBuilder_.addAllMessages(values);
        }
        return this;
      }
      public Builder clearPerson() {
        if (personBuilder_ == null) {
          person_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000001);
          onChanged();
        } else {
          personBuilder_.clear();
        }
        return this;
      }
      public Builder removePerson(int index) {
        if (personBuilder_ == null) {
          ensurePersonIsMutable();
          person_.remove(index);
          onChanged();
        } else {
          personBuilder_.remove(index);
        }
        return this;
      }
      public pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.Builder getPersonBuilder(
          int index) {
        return getPersonFieldBuilder().getBuilder(index);
      }
      public pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.PersonOrBuilder getPersonOrBuilder(
          int index) {
        if (personBuilder_ == null) {
          return person_.get(index);  } else {
          return personBuilder_.getMessageOrBuilder(index);
        }
      }
      public java.util.List<? extends pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.PersonOrBuilder> 
           getPersonOrBuilderList() {
        if (personBuilder_ != null) {
          return personBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(person_);
        }
      }
      public pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.Builder addPersonBuilder() {
        return getPersonFieldBuilder().addBuilder(
            pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.getDefaultInstance());
      }
      public pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.Builder addPersonBuilder(
          int index) {
        return getPersonFieldBuilder().addBuilder(
            index, pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.getDefaultInstance());
      }
      public java.util.List<pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.Builder> 
           getPersonBuilderList() {
        return getPersonFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilder<
          pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person, pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.Builder, pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.PersonOrBuilder> 
          getPersonFieldBuilder() {
        if (personBuilder_ == null) {
          personBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
              pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person, pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.Builder, pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.PersonOrBuilder>(
                  person_,
                  ((bitField0_ & 0x00000001) == 0x00000001),
                  getParentForChildren(),
                  isClean());
          person_ = null;
        }
        return personBuilder_;
      }
      
      // @@protoc_insertion_point(builder_scope:tutorial.AddressBook)
    }
    
    static {
      defaultInstance = new AddressBook(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:tutorial.AddressBook)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_tutorial_Person_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_tutorial_Person_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_tutorial_Person_PhoneNumber_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_tutorial_Person_PhoneNumber_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_tutorial_AddressBook_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_tutorial_AddressBook_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\021addressbook.proto\022\010tutorial\"\332\001\n\006Person" +
      "\022\014\n\004name\030\001 \002(\t\022\n\n\002id\030\002 \002(\005\022\r\n\005email\030\003 \001(" +
      "\t\022+\n\005phone\030\004 \003(\0132\034.tutorial.Person.Phone" +
      "Number\032M\n\013PhoneNumber\022\016\n\006number\030\001 \002(\t\022.\n" +
      "\004type\030\002 \001(\0162\032.tutorial.Person.PhoneType:" +
      "\004HOME\"+\n\tPhoneType\022\n\n\006MOBILE\020\000\022\010\n\004HOME\020\001" +
      "\022\010\n\004WORK\020\002\"/\n\013AddressBook\022 \n\006person\030\001 \003(" +
      "\0132\020.tutorial.PersonB)\n\024com.example.tutor" +
      "ialB\021AddressBookProtos"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_tutorial_Person_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_tutorial_Person_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_tutorial_Person_descriptor,
              new java.lang.String[] { "Name", "Id", "Email", "Phone", },
              pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.class,
              pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.Builder.class);
          internal_static_tutorial_Person_PhoneNumber_descriptor =
            internal_static_tutorial_Person_descriptor.getNestedTypes().get(0);
          internal_static_tutorial_Person_PhoneNumber_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_tutorial_Person_PhoneNumber_descriptor,
              new java.lang.String[] { "Number", "Type", },
              pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumber.class,
              pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person.PhoneNumber.Builder.class);
          internal_static_tutorial_AddressBook_descriptor =
            getDescriptor().getMessageTypes().get(1);
          internal_static_tutorial_AddressBook_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_tutorial_AddressBook_descriptor,
              new java.lang.String[] { "Person", },
              pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.AddressBook.class,
              pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.AddressBook.Builder.class);
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
