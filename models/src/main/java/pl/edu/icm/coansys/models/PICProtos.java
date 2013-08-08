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

public final class PICProtos {
  private PICProtos() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface PicOutOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // required string docId = 1;
    boolean hasDocId();
    String getDocId();
    
    // repeated .pl.edu.icm.coansys.models.Reference refs = 2;
    java.util.List<pl.edu.icm.coansys.models.PICProtos.Reference> 
        getRefsList();
    pl.edu.icm.coansys.models.PICProtos.Reference getRefs(int index);
    int getRefsCount();
    java.util.List<? extends pl.edu.icm.coansys.models.PICProtos.ReferenceOrBuilder> 
        getRefsOrBuilderList();
    pl.edu.icm.coansys.models.PICProtos.ReferenceOrBuilder getRefsOrBuilder(
        int index);
    
    // repeated .pl.edu.icm.coansys.models.Auxiliar auxs = 3;
    java.util.List<pl.edu.icm.coansys.models.PICProtos.Auxiliar> 
        getAuxsList();
    pl.edu.icm.coansys.models.PICProtos.Auxiliar getAuxs(int index);
    int getAuxsCount();
    java.util.List<? extends pl.edu.icm.coansys.models.PICProtos.AuxiliarOrBuilder> 
        getAuxsOrBuilderList();
    pl.edu.icm.coansys.models.PICProtos.AuxiliarOrBuilder getAuxsOrBuilder(
        int index);
  }
  public static final class PicOut extends
      com.google.protobuf.GeneratedMessage
      implements PicOutOrBuilder {
    // Use PicOut.newBuilder() to construct.
    private PicOut(Builder builder) {
      super(builder);
    }
    private PicOut(boolean noInit) {}
    
    private static final PicOut defaultInstance;
    public static PicOut getDefaultInstance() {
      return defaultInstance;
    }
    
    public PicOut getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return pl.edu.icm.coansys.models.PICProtos.internal_static_pl_edu_icm_coansys_models_PicOut_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return pl.edu.icm.coansys.models.PICProtos.internal_static_pl_edu_icm_coansys_models_PicOut_fieldAccessorTable;
    }
    
    private int bitField0_;
    // required string docId = 1;
    public static final int DOCID_FIELD_NUMBER = 1;
    private java.lang.Object docId_;
    public boolean hasDocId() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
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
    
    // repeated .pl.edu.icm.coansys.models.Reference refs = 2;
    public static final int REFS_FIELD_NUMBER = 2;
    private java.util.List<pl.edu.icm.coansys.models.PICProtos.Reference> refs_;
    public java.util.List<pl.edu.icm.coansys.models.PICProtos.Reference> getRefsList() {
      return refs_;
    }
    public java.util.List<? extends pl.edu.icm.coansys.models.PICProtos.ReferenceOrBuilder> 
        getRefsOrBuilderList() {
      return refs_;
    }
    public int getRefsCount() {
      return refs_.size();
    }
    public pl.edu.icm.coansys.models.PICProtos.Reference getRefs(int index) {
      return refs_.get(index);
    }
    public pl.edu.icm.coansys.models.PICProtos.ReferenceOrBuilder getRefsOrBuilder(
        int index) {
      return refs_.get(index);
    }
    
    // repeated .pl.edu.icm.coansys.models.Auxiliar auxs = 3;
    public static final int AUXS_FIELD_NUMBER = 3;
    private java.util.List<pl.edu.icm.coansys.models.PICProtos.Auxiliar> auxs_;
    public java.util.List<pl.edu.icm.coansys.models.PICProtos.Auxiliar> getAuxsList() {
      return auxs_;
    }
    public java.util.List<? extends pl.edu.icm.coansys.models.PICProtos.AuxiliarOrBuilder> 
        getAuxsOrBuilderList() {
      return auxs_;
    }
    public int getAuxsCount() {
      return auxs_.size();
    }
    public pl.edu.icm.coansys.models.PICProtos.Auxiliar getAuxs(int index) {
      return auxs_.get(index);
    }
    public pl.edu.icm.coansys.models.PICProtos.AuxiliarOrBuilder getAuxsOrBuilder(
        int index) {
      return auxs_.get(index);
    }
    
    private void initFields() {
      docId_ = "";
      refs_ = java.util.Collections.emptyList();
      auxs_ = java.util.Collections.emptyList();
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
      if (!hasDocId()) {
        memoizedIsInitialized = 0;
        return false;
      }
      for (int i = 0; i < getRefsCount(); i++) {
        if (!getRefs(i).isInitialized()) {
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
        output.writeBytes(1, getDocIdBytes());
      }
      for (int i = 0; i < refs_.size(); i++) {
        output.writeMessage(2, refs_.get(i));
      }
      for (int i = 0; i < auxs_.size(); i++) {
        output.writeMessage(3, auxs_.get(i));
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
          .computeBytesSize(1, getDocIdBytes());
      }
      for (int i = 0; i < refs_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(2, refs_.get(i));
      }
      for (int i = 0; i < auxs_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(3, auxs_.get(i));
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
    
    public static pl.edu.icm.coansys.models.PICProtos.PicOut parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static pl.edu.icm.coansys.models.PICProtos.PicOut parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.models.PICProtos.PicOut parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static pl.edu.icm.coansys.models.PICProtos.PicOut parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.models.PICProtos.PicOut parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static pl.edu.icm.coansys.models.PICProtos.PicOut parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.models.PICProtos.PicOut parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static pl.edu.icm.coansys.models.PICProtos.PicOut parseDelimitedFrom(
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
    public static pl.edu.icm.coansys.models.PICProtos.PicOut parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static pl.edu.icm.coansys.models.PICProtos.PicOut parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(pl.edu.icm.coansys.models.PICProtos.PicOut prototype) {
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
       implements pl.edu.icm.coansys.models.PICProtos.PicOutOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return pl.edu.icm.coansys.models.PICProtos.internal_static_pl_edu_icm_coansys_models_PicOut_descriptor;
      }
      
      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return pl.edu.icm.coansys.models.PICProtos.internal_static_pl_edu_icm_coansys_models_PicOut_fieldAccessorTable;
      }
      
      // Construct using pl.edu.icm.coansys.models.PICProtos.PicOut.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }
      
      private Builder(BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getRefsFieldBuilder();
          getAuxsFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }
      
      public Builder clear() {
        super.clear();
        docId_ = "";
        bitField0_ = (bitField0_ & ~0x00000001);
        if (refsBuilder_ == null) {
          refs_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000002);
        } else {
          refsBuilder_.clear();
        }
        if (auxsBuilder_ == null) {
          auxs_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000004);
        } else {
          auxsBuilder_.clear();
        }
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return pl.edu.icm.coansys.models.PICProtos.PicOut.getDescriptor();
      }
      
      public pl.edu.icm.coansys.models.PICProtos.PicOut getDefaultInstanceForType() {
        return pl.edu.icm.coansys.models.PICProtos.PicOut.getDefaultInstance();
      }
      
      public pl.edu.icm.coansys.models.PICProtos.PicOut build() {
        pl.edu.icm.coansys.models.PICProtos.PicOut result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private pl.edu.icm.coansys.models.PICProtos.PicOut buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        pl.edu.icm.coansys.models.PICProtos.PicOut result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public pl.edu.icm.coansys.models.PICProtos.PicOut buildPartial() {
        pl.edu.icm.coansys.models.PICProtos.PicOut result = new pl.edu.icm.coansys.models.PICProtos.PicOut(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.docId_ = docId_;
        if (refsBuilder_ == null) {
          if (((bitField0_ & 0x00000002) == 0x00000002)) {
            refs_ = java.util.Collections.unmodifiableList(refs_);
            bitField0_ = (bitField0_ & ~0x00000002);
          }
          result.refs_ = refs_;
        } else {
          result.refs_ = refsBuilder_.build();
        }
        if (auxsBuilder_ == null) {
          if (((bitField0_ & 0x00000004) == 0x00000004)) {
            auxs_ = java.util.Collections.unmodifiableList(auxs_);
            bitField0_ = (bitField0_ & ~0x00000004);
          }
          result.auxs_ = auxs_;
        } else {
          result.auxs_ = auxsBuilder_.build();
        }
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof pl.edu.icm.coansys.models.PICProtos.PicOut) {
          return mergeFrom((pl.edu.icm.coansys.models.PICProtos.PicOut)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(pl.edu.icm.coansys.models.PICProtos.PicOut other) {
        if (other == pl.edu.icm.coansys.models.PICProtos.PicOut.getDefaultInstance()) return this;
        if (other.hasDocId()) {
          setDocId(other.getDocId());
        }
        if (refsBuilder_ == null) {
          if (!other.refs_.isEmpty()) {
            if (refs_.isEmpty()) {
              refs_ = other.refs_;
              bitField0_ = (bitField0_ & ~0x00000002);
            } else {
              ensureRefsIsMutable();
              refs_.addAll(other.refs_);
            }
            onChanged();
          }
        } else {
          if (!other.refs_.isEmpty()) {
            if (refsBuilder_.isEmpty()) {
              refsBuilder_.dispose();
              refsBuilder_ = null;
              refs_ = other.refs_;
              bitField0_ = (bitField0_ & ~0x00000002);
              refsBuilder_ = 
                com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                   getRefsFieldBuilder() : null;
            } else {
              refsBuilder_.addAllMessages(other.refs_);
            }
          }
        }
        if (auxsBuilder_ == null) {
          if (!other.auxs_.isEmpty()) {
            if (auxs_.isEmpty()) {
              auxs_ = other.auxs_;
              bitField0_ = (bitField0_ & ~0x00000004);
            } else {
              ensureAuxsIsMutable();
              auxs_.addAll(other.auxs_);
            }
            onChanged();
          }
        } else {
          if (!other.auxs_.isEmpty()) {
            if (auxsBuilder_.isEmpty()) {
              auxsBuilder_.dispose();
              auxsBuilder_ = null;
              auxs_ = other.auxs_;
              bitField0_ = (bitField0_ & ~0x00000004);
              auxsBuilder_ = 
                com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                   getAuxsFieldBuilder() : null;
            } else {
              auxsBuilder_.addAllMessages(other.auxs_);
            }
          }
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
        if (!hasDocId()) {
          
          return false;
        }
        for (int i = 0; i < getRefsCount(); i++) {
          if (!getRefs(i).isInitialized()) {
            
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
              docId_ = input.readBytes();
              break;
            }
            case 18: {
              pl.edu.icm.coansys.models.PICProtos.Reference.Builder subBuilder = pl.edu.icm.coansys.models.PICProtos.Reference.newBuilder();
              input.readMessage(subBuilder, extensionRegistry);
              addRefs(subBuilder.buildPartial());
              break;
            }
            case 26: {
              pl.edu.icm.coansys.models.PICProtos.Auxiliar.Builder subBuilder = pl.edu.icm.coansys.models.PICProtos.Auxiliar.newBuilder();
              input.readMessage(subBuilder, extensionRegistry);
              addAuxs(subBuilder.buildPartial());
              break;
            }
          }
        }
      }
      
      private int bitField0_;
      
      // required string docId = 1;
      private java.lang.Object docId_ = "";
      public boolean hasDocId() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
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
  bitField0_ |= 0x00000001;
        docId_ = value;
        onChanged();
        return this;
      }
      public Builder clearDocId() {
        bitField0_ = (bitField0_ & ~0x00000001);
        docId_ = getDefaultInstance().getDocId();
        onChanged();
        return this;
      }
      void setDocId(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000001;
        docId_ = value;
        onChanged();
      }
      
      // repeated .pl.edu.icm.coansys.models.Reference refs = 2;
      private java.util.List<pl.edu.icm.coansys.models.PICProtos.Reference> refs_ =
        java.util.Collections.emptyList();
      private void ensureRefsIsMutable() {
        if (!((bitField0_ & 0x00000002) == 0x00000002)) {
          refs_ = new java.util.ArrayList<pl.edu.icm.coansys.models.PICProtos.Reference>(refs_);
          bitField0_ |= 0x00000002;
         }
      }
      
      private com.google.protobuf.RepeatedFieldBuilder<
          pl.edu.icm.coansys.models.PICProtos.Reference, pl.edu.icm.coansys.models.PICProtos.Reference.Builder, pl.edu.icm.coansys.models.PICProtos.ReferenceOrBuilder> refsBuilder_;
      
      public java.util.List<pl.edu.icm.coansys.models.PICProtos.Reference> getRefsList() {
        if (refsBuilder_ == null) {
          return java.util.Collections.unmodifiableList(refs_);
        } else {
          return refsBuilder_.getMessageList();
        }
      }
      public int getRefsCount() {
        if (refsBuilder_ == null) {
          return refs_.size();
        } else {
          return refsBuilder_.getCount();
        }
      }
      public pl.edu.icm.coansys.models.PICProtos.Reference getRefs(int index) {
        if (refsBuilder_ == null) {
          return refs_.get(index);
        } else {
          return refsBuilder_.getMessage(index);
        }
      }
      public Builder setRefs(
          int index, pl.edu.icm.coansys.models.PICProtos.Reference value) {
        if (refsBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureRefsIsMutable();
          refs_.set(index, value);
          onChanged();
        } else {
          refsBuilder_.setMessage(index, value);
        }
        return this;
      }
      public Builder setRefs(
          int index, pl.edu.icm.coansys.models.PICProtos.Reference.Builder builderForValue) {
        if (refsBuilder_ == null) {
          ensureRefsIsMutable();
          refs_.set(index, builderForValue.build());
          onChanged();
        } else {
          refsBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }
      public Builder addRefs(pl.edu.icm.coansys.models.PICProtos.Reference value) {
        if (refsBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureRefsIsMutable();
          refs_.add(value);
          onChanged();
        } else {
          refsBuilder_.addMessage(value);
        }
        return this;
      }
      public Builder addRefs(
          int index, pl.edu.icm.coansys.models.PICProtos.Reference value) {
        if (refsBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureRefsIsMutable();
          refs_.add(index, value);
          onChanged();
        } else {
          refsBuilder_.addMessage(index, value);
        }
        return this;
      }
      public Builder addRefs(
          pl.edu.icm.coansys.models.PICProtos.Reference.Builder builderForValue) {
        if (refsBuilder_ == null) {
          ensureRefsIsMutable();
          refs_.add(builderForValue.build());
          onChanged();
        } else {
          refsBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }
      public Builder addRefs(
          int index, pl.edu.icm.coansys.models.PICProtos.Reference.Builder builderForValue) {
        if (refsBuilder_ == null) {
          ensureRefsIsMutable();
          refs_.add(index, builderForValue.build());
          onChanged();
        } else {
          refsBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }
      public Builder addAllRefs(
          java.lang.Iterable<? extends pl.edu.icm.coansys.models.PICProtos.Reference> values) {
        if (refsBuilder_ == null) {
          ensureRefsIsMutable();
          super.addAll(values, refs_);
          onChanged();
        } else {
          refsBuilder_.addAllMessages(values);
        }
        return this;
      }
      public Builder clearRefs() {
        if (refsBuilder_ == null) {
          refs_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000002);
          onChanged();
        } else {
          refsBuilder_.clear();
        }
        return this;
      }
      public Builder removeRefs(int index) {
        if (refsBuilder_ == null) {
          ensureRefsIsMutable();
          refs_.remove(index);
          onChanged();
        } else {
          refsBuilder_.remove(index);
        }
        return this;
      }
      public pl.edu.icm.coansys.models.PICProtos.Reference.Builder getRefsBuilder(
          int index) {
        return getRefsFieldBuilder().getBuilder(index);
      }
      public pl.edu.icm.coansys.models.PICProtos.ReferenceOrBuilder getRefsOrBuilder(
          int index) {
        if (refsBuilder_ == null) {
          return refs_.get(index);  } else {
          return refsBuilder_.getMessageOrBuilder(index);
        }
      }
      public java.util.List<? extends pl.edu.icm.coansys.models.PICProtos.ReferenceOrBuilder> 
           getRefsOrBuilderList() {
        if (refsBuilder_ != null) {
          return refsBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(refs_);
        }
      }
      public pl.edu.icm.coansys.models.PICProtos.Reference.Builder addRefsBuilder() {
        return getRefsFieldBuilder().addBuilder(
            pl.edu.icm.coansys.models.PICProtos.Reference.getDefaultInstance());
      }
      public pl.edu.icm.coansys.models.PICProtos.Reference.Builder addRefsBuilder(
          int index) {
        return getRefsFieldBuilder().addBuilder(
            index, pl.edu.icm.coansys.models.PICProtos.Reference.getDefaultInstance());
      }
      public java.util.List<pl.edu.icm.coansys.models.PICProtos.Reference.Builder> 
           getRefsBuilderList() {
        return getRefsFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilder<
          pl.edu.icm.coansys.models.PICProtos.Reference, pl.edu.icm.coansys.models.PICProtos.Reference.Builder, pl.edu.icm.coansys.models.PICProtos.ReferenceOrBuilder> 
          getRefsFieldBuilder() {
        if (refsBuilder_ == null) {
          refsBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
              pl.edu.icm.coansys.models.PICProtos.Reference, pl.edu.icm.coansys.models.PICProtos.Reference.Builder, pl.edu.icm.coansys.models.PICProtos.ReferenceOrBuilder>(
                  refs_,
                  ((bitField0_ & 0x00000002) == 0x00000002),
                  getParentForChildren(),
                  isClean());
          refs_ = null;
        }
        return refsBuilder_;
      }
      
      // repeated .pl.edu.icm.coansys.models.Auxiliar auxs = 3;
      private java.util.List<pl.edu.icm.coansys.models.PICProtos.Auxiliar> auxs_ =
        java.util.Collections.emptyList();
      private void ensureAuxsIsMutable() {
        if (!((bitField0_ & 0x00000004) == 0x00000004)) {
          auxs_ = new java.util.ArrayList<pl.edu.icm.coansys.models.PICProtos.Auxiliar>(auxs_);
          bitField0_ |= 0x00000004;
         }
      }
      
      private com.google.protobuf.RepeatedFieldBuilder<
          pl.edu.icm.coansys.models.PICProtos.Auxiliar, pl.edu.icm.coansys.models.PICProtos.Auxiliar.Builder, pl.edu.icm.coansys.models.PICProtos.AuxiliarOrBuilder> auxsBuilder_;
      
      public java.util.List<pl.edu.icm.coansys.models.PICProtos.Auxiliar> getAuxsList() {
        if (auxsBuilder_ == null) {
          return java.util.Collections.unmodifiableList(auxs_);
        } else {
          return auxsBuilder_.getMessageList();
        }
      }
      public int getAuxsCount() {
        if (auxsBuilder_ == null) {
          return auxs_.size();
        } else {
          return auxsBuilder_.getCount();
        }
      }
      public pl.edu.icm.coansys.models.PICProtos.Auxiliar getAuxs(int index) {
        if (auxsBuilder_ == null) {
          return auxs_.get(index);
        } else {
          return auxsBuilder_.getMessage(index);
        }
      }
      public Builder setAuxs(
          int index, pl.edu.icm.coansys.models.PICProtos.Auxiliar value) {
        if (auxsBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureAuxsIsMutable();
          auxs_.set(index, value);
          onChanged();
        } else {
          auxsBuilder_.setMessage(index, value);
        }
        return this;
      }
      public Builder setAuxs(
          int index, pl.edu.icm.coansys.models.PICProtos.Auxiliar.Builder builderForValue) {
        if (auxsBuilder_ == null) {
          ensureAuxsIsMutable();
          auxs_.set(index, builderForValue.build());
          onChanged();
        } else {
          auxsBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }
      public Builder addAuxs(pl.edu.icm.coansys.models.PICProtos.Auxiliar value) {
        if (auxsBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureAuxsIsMutable();
          auxs_.add(value);
          onChanged();
        } else {
          auxsBuilder_.addMessage(value);
        }
        return this;
      }
      public Builder addAuxs(
          int index, pl.edu.icm.coansys.models.PICProtos.Auxiliar value) {
        if (auxsBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureAuxsIsMutable();
          auxs_.add(index, value);
          onChanged();
        } else {
          auxsBuilder_.addMessage(index, value);
        }
        return this;
      }
      public Builder addAuxs(
          pl.edu.icm.coansys.models.PICProtos.Auxiliar.Builder builderForValue) {
        if (auxsBuilder_ == null) {
          ensureAuxsIsMutable();
          auxs_.add(builderForValue.build());
          onChanged();
        } else {
          auxsBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }
      public Builder addAuxs(
          int index, pl.edu.icm.coansys.models.PICProtos.Auxiliar.Builder builderForValue) {
        if (auxsBuilder_ == null) {
          ensureAuxsIsMutable();
          auxs_.add(index, builderForValue.build());
          onChanged();
        } else {
          auxsBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }
      public Builder addAllAuxs(
          java.lang.Iterable<? extends pl.edu.icm.coansys.models.PICProtos.Auxiliar> values) {
        if (auxsBuilder_ == null) {
          ensureAuxsIsMutable();
          super.addAll(values, auxs_);
          onChanged();
        } else {
          auxsBuilder_.addAllMessages(values);
        }
        return this;
      }
      public Builder clearAuxs() {
        if (auxsBuilder_ == null) {
          auxs_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000004);
          onChanged();
        } else {
          auxsBuilder_.clear();
        }
        return this;
      }
      public Builder removeAuxs(int index) {
        if (auxsBuilder_ == null) {
          ensureAuxsIsMutable();
          auxs_.remove(index);
          onChanged();
        } else {
          auxsBuilder_.remove(index);
        }
        return this;
      }
      public pl.edu.icm.coansys.models.PICProtos.Auxiliar.Builder getAuxsBuilder(
          int index) {
        return getAuxsFieldBuilder().getBuilder(index);
      }
      public pl.edu.icm.coansys.models.PICProtos.AuxiliarOrBuilder getAuxsOrBuilder(
          int index) {
        if (auxsBuilder_ == null) {
          return auxs_.get(index);  } else {
          return auxsBuilder_.getMessageOrBuilder(index);
        }
      }
      public java.util.List<? extends pl.edu.icm.coansys.models.PICProtos.AuxiliarOrBuilder> 
           getAuxsOrBuilderList() {
        if (auxsBuilder_ != null) {
          return auxsBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(auxs_);
        }
      }
      public pl.edu.icm.coansys.models.PICProtos.Auxiliar.Builder addAuxsBuilder() {
        return getAuxsFieldBuilder().addBuilder(
            pl.edu.icm.coansys.models.PICProtos.Auxiliar.getDefaultInstance());
      }
      public pl.edu.icm.coansys.models.PICProtos.Auxiliar.Builder addAuxsBuilder(
          int index) {
        return getAuxsFieldBuilder().addBuilder(
            index, pl.edu.icm.coansys.models.PICProtos.Auxiliar.getDefaultInstance());
      }
      public java.util.List<pl.edu.icm.coansys.models.PICProtos.Auxiliar.Builder> 
           getAuxsBuilderList() {
        return getAuxsFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilder<
          pl.edu.icm.coansys.models.PICProtos.Auxiliar, pl.edu.icm.coansys.models.PICProtos.Auxiliar.Builder, pl.edu.icm.coansys.models.PICProtos.AuxiliarOrBuilder> 
          getAuxsFieldBuilder() {
        if (auxsBuilder_ == null) {
          auxsBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
              pl.edu.icm.coansys.models.PICProtos.Auxiliar, pl.edu.icm.coansys.models.PICProtos.Auxiliar.Builder, pl.edu.icm.coansys.models.PICProtos.AuxiliarOrBuilder>(
                  auxs_,
                  ((bitField0_ & 0x00000004) == 0x00000004),
                  getParentForChildren(),
                  isClean());
          auxs_ = null;
        }
        return auxsBuilder_;
      }
      
      // @@protoc_insertion_point(builder_scope:pl.edu.icm.coansys.models.PicOut)
    }
    
    static {
      defaultInstance = new PicOut(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:pl.edu.icm.coansys.models.PicOut)
  }
  
  public interface ReferenceOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // required int32 refNum = 1;
    boolean hasRefNum();
    int getRefNum();
    
    // required string docId = 2;
    boolean hasDocId();
    String getDocId();
    
    // repeated .pl.edu.icm.coansys.models.Auxiliar auxs = 3;
    java.util.List<pl.edu.icm.coansys.models.PICProtos.Auxiliar> 
        getAuxsList();
    pl.edu.icm.coansys.models.PICProtos.Auxiliar getAuxs(int index);
    int getAuxsCount();
    java.util.List<? extends pl.edu.icm.coansys.models.PICProtos.AuxiliarOrBuilder> 
        getAuxsOrBuilderList();
    pl.edu.icm.coansys.models.PICProtos.AuxiliarOrBuilder getAuxsOrBuilder(
        int index);
  }
  public static final class Reference extends
      com.google.protobuf.GeneratedMessage
      implements ReferenceOrBuilder {
    // Use Reference.newBuilder() to construct.
    private Reference(Builder builder) {
      super(builder);
    }
    private Reference(boolean noInit) {}
    
    private static final Reference defaultInstance;
    public static Reference getDefaultInstance() {
      return defaultInstance;
    }
    
    public Reference getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return pl.edu.icm.coansys.models.PICProtos.internal_static_pl_edu_icm_coansys_models_Reference_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return pl.edu.icm.coansys.models.PICProtos.internal_static_pl_edu_icm_coansys_models_Reference_fieldAccessorTable;
    }
    
    private int bitField0_;
    // required int32 refNum = 1;
    public static final int REFNUM_FIELD_NUMBER = 1;
    private int refNum_;
    public boolean hasRefNum() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    public int getRefNum() {
      return refNum_;
    }
    
    // required string docId = 2;
    public static final int DOCID_FIELD_NUMBER = 2;
    private java.lang.Object docId_;
    public boolean hasDocId() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
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
    
    // repeated .pl.edu.icm.coansys.models.Auxiliar auxs = 3;
    public static final int AUXS_FIELD_NUMBER = 3;
    private java.util.List<pl.edu.icm.coansys.models.PICProtos.Auxiliar> auxs_;
    public java.util.List<pl.edu.icm.coansys.models.PICProtos.Auxiliar> getAuxsList() {
      return auxs_;
    }
    public java.util.List<? extends pl.edu.icm.coansys.models.PICProtos.AuxiliarOrBuilder> 
        getAuxsOrBuilderList() {
      return auxs_;
    }
    public int getAuxsCount() {
      return auxs_.size();
    }
    public pl.edu.icm.coansys.models.PICProtos.Auxiliar getAuxs(int index) {
      return auxs_.get(index);
    }
    public pl.edu.icm.coansys.models.PICProtos.AuxiliarOrBuilder getAuxsOrBuilder(
        int index) {
      return auxs_.get(index);
    }
    
    private void initFields() {
      refNum_ = 0;
      docId_ = "";
      auxs_ = java.util.Collections.emptyList();
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
      if (!hasRefNum()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasDocId()) {
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
        output.writeInt32(1, refNum_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeBytes(2, getDocIdBytes());
      }
      for (int i = 0; i < auxs_.size(); i++) {
        output.writeMessage(3, auxs_.get(i));
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
          .computeInt32Size(1, refNum_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(2, getDocIdBytes());
      }
      for (int i = 0; i < auxs_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(3, auxs_.get(i));
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
    
    public static pl.edu.icm.coansys.models.PICProtos.Reference parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static pl.edu.icm.coansys.models.PICProtos.Reference parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.models.PICProtos.Reference parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static pl.edu.icm.coansys.models.PICProtos.Reference parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.models.PICProtos.Reference parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static pl.edu.icm.coansys.models.PICProtos.Reference parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.models.PICProtos.Reference parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static pl.edu.icm.coansys.models.PICProtos.Reference parseDelimitedFrom(
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
    public static pl.edu.icm.coansys.models.PICProtos.Reference parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static pl.edu.icm.coansys.models.PICProtos.Reference parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(pl.edu.icm.coansys.models.PICProtos.Reference prototype) {
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
       implements pl.edu.icm.coansys.models.PICProtos.ReferenceOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return pl.edu.icm.coansys.models.PICProtos.internal_static_pl_edu_icm_coansys_models_Reference_descriptor;
      }
      
      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return pl.edu.icm.coansys.models.PICProtos.internal_static_pl_edu_icm_coansys_models_Reference_fieldAccessorTable;
      }
      
      // Construct using pl.edu.icm.coansys.models.PICProtos.Reference.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }
      
      private Builder(BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getAuxsFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }
      
      public Builder clear() {
        super.clear();
        refNum_ = 0;
        bitField0_ = (bitField0_ & ~0x00000001);
        docId_ = "";
        bitField0_ = (bitField0_ & ~0x00000002);
        if (auxsBuilder_ == null) {
          auxs_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000004);
        } else {
          auxsBuilder_.clear();
        }
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return pl.edu.icm.coansys.models.PICProtos.Reference.getDescriptor();
      }
      
      public pl.edu.icm.coansys.models.PICProtos.Reference getDefaultInstanceForType() {
        return pl.edu.icm.coansys.models.PICProtos.Reference.getDefaultInstance();
      }
      
      public pl.edu.icm.coansys.models.PICProtos.Reference build() {
        pl.edu.icm.coansys.models.PICProtos.Reference result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private pl.edu.icm.coansys.models.PICProtos.Reference buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        pl.edu.icm.coansys.models.PICProtos.Reference result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public pl.edu.icm.coansys.models.PICProtos.Reference buildPartial() {
        pl.edu.icm.coansys.models.PICProtos.Reference result = new pl.edu.icm.coansys.models.PICProtos.Reference(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.refNum_ = refNum_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.docId_ = docId_;
        if (auxsBuilder_ == null) {
          if (((bitField0_ & 0x00000004) == 0x00000004)) {
            auxs_ = java.util.Collections.unmodifiableList(auxs_);
            bitField0_ = (bitField0_ & ~0x00000004);
          }
          result.auxs_ = auxs_;
        } else {
          result.auxs_ = auxsBuilder_.build();
        }
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof pl.edu.icm.coansys.models.PICProtos.Reference) {
          return mergeFrom((pl.edu.icm.coansys.models.PICProtos.Reference)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(pl.edu.icm.coansys.models.PICProtos.Reference other) {
        if (other == pl.edu.icm.coansys.models.PICProtos.Reference.getDefaultInstance()) return this;
        if (other.hasRefNum()) {
          setRefNum(other.getRefNum());
        }
        if (other.hasDocId()) {
          setDocId(other.getDocId());
        }
        if (auxsBuilder_ == null) {
          if (!other.auxs_.isEmpty()) {
            if (auxs_.isEmpty()) {
              auxs_ = other.auxs_;
              bitField0_ = (bitField0_ & ~0x00000004);
            } else {
              ensureAuxsIsMutable();
              auxs_.addAll(other.auxs_);
            }
            onChanged();
          }
        } else {
          if (!other.auxs_.isEmpty()) {
            if (auxsBuilder_.isEmpty()) {
              auxsBuilder_.dispose();
              auxsBuilder_ = null;
              auxs_ = other.auxs_;
              bitField0_ = (bitField0_ & ~0x00000004);
              auxsBuilder_ = 
                com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                   getAuxsFieldBuilder() : null;
            } else {
              auxsBuilder_.addAllMessages(other.auxs_);
            }
          }
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
        if (!hasRefNum()) {
          
          return false;
        }
        if (!hasDocId()) {
          
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
            case 8: {
              bitField0_ |= 0x00000001;
              refNum_ = input.readInt32();
              break;
            }
            case 18: {
              bitField0_ |= 0x00000002;
              docId_ = input.readBytes();
              break;
            }
            case 26: {
              pl.edu.icm.coansys.models.PICProtos.Auxiliar.Builder subBuilder = pl.edu.icm.coansys.models.PICProtos.Auxiliar.newBuilder();
              input.readMessage(subBuilder, extensionRegistry);
              addAuxs(subBuilder.buildPartial());
              break;
            }
          }
        }
      }
      
      private int bitField0_;
      
      // required int32 refNum = 1;
      private int refNum_ ;
      public boolean hasRefNum() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      public int getRefNum() {
        return refNum_;
      }
      public Builder setRefNum(int value) {
        bitField0_ |= 0x00000001;
        refNum_ = value;
        onChanged();
        return this;
      }
      public Builder clearRefNum() {
        bitField0_ = (bitField0_ & ~0x00000001);
        refNum_ = 0;
        onChanged();
        return this;
      }
      
      // required string docId = 2;
      private java.lang.Object docId_ = "";
      public boolean hasDocId() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
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
  bitField0_ |= 0x00000002;
        docId_ = value;
        onChanged();
        return this;
      }
      public Builder clearDocId() {
        bitField0_ = (bitField0_ & ~0x00000002);
        docId_ = getDefaultInstance().getDocId();
        onChanged();
        return this;
      }
      void setDocId(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000002;
        docId_ = value;
        onChanged();
      }
      
      // repeated .pl.edu.icm.coansys.models.Auxiliar auxs = 3;
      private java.util.List<pl.edu.icm.coansys.models.PICProtos.Auxiliar> auxs_ =
        java.util.Collections.emptyList();
      private void ensureAuxsIsMutable() {
        if (!((bitField0_ & 0x00000004) == 0x00000004)) {
          auxs_ = new java.util.ArrayList<pl.edu.icm.coansys.models.PICProtos.Auxiliar>(auxs_);
          bitField0_ |= 0x00000004;
         }
      }
      
      private com.google.protobuf.RepeatedFieldBuilder<
          pl.edu.icm.coansys.models.PICProtos.Auxiliar, pl.edu.icm.coansys.models.PICProtos.Auxiliar.Builder, pl.edu.icm.coansys.models.PICProtos.AuxiliarOrBuilder> auxsBuilder_;
      
      public java.util.List<pl.edu.icm.coansys.models.PICProtos.Auxiliar> getAuxsList() {
        if (auxsBuilder_ == null) {
          return java.util.Collections.unmodifiableList(auxs_);
        } else {
          return auxsBuilder_.getMessageList();
        }
      }
      public int getAuxsCount() {
        if (auxsBuilder_ == null) {
          return auxs_.size();
        } else {
          return auxsBuilder_.getCount();
        }
      }
      public pl.edu.icm.coansys.models.PICProtos.Auxiliar getAuxs(int index) {
        if (auxsBuilder_ == null) {
          return auxs_.get(index);
        } else {
          return auxsBuilder_.getMessage(index);
        }
      }
      public Builder setAuxs(
          int index, pl.edu.icm.coansys.models.PICProtos.Auxiliar value) {
        if (auxsBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureAuxsIsMutable();
          auxs_.set(index, value);
          onChanged();
        } else {
          auxsBuilder_.setMessage(index, value);
        }
        return this;
      }
      public Builder setAuxs(
          int index, pl.edu.icm.coansys.models.PICProtos.Auxiliar.Builder builderForValue) {
        if (auxsBuilder_ == null) {
          ensureAuxsIsMutable();
          auxs_.set(index, builderForValue.build());
          onChanged();
        } else {
          auxsBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }
      public Builder addAuxs(pl.edu.icm.coansys.models.PICProtos.Auxiliar value) {
        if (auxsBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureAuxsIsMutable();
          auxs_.add(value);
          onChanged();
        } else {
          auxsBuilder_.addMessage(value);
        }
        return this;
      }
      public Builder addAuxs(
          int index, pl.edu.icm.coansys.models.PICProtos.Auxiliar value) {
        if (auxsBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureAuxsIsMutable();
          auxs_.add(index, value);
          onChanged();
        } else {
          auxsBuilder_.addMessage(index, value);
        }
        return this;
      }
      public Builder addAuxs(
          pl.edu.icm.coansys.models.PICProtos.Auxiliar.Builder builderForValue) {
        if (auxsBuilder_ == null) {
          ensureAuxsIsMutable();
          auxs_.add(builderForValue.build());
          onChanged();
        } else {
          auxsBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }
      public Builder addAuxs(
          int index, pl.edu.icm.coansys.models.PICProtos.Auxiliar.Builder builderForValue) {
        if (auxsBuilder_ == null) {
          ensureAuxsIsMutable();
          auxs_.add(index, builderForValue.build());
          onChanged();
        } else {
          auxsBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }
      public Builder addAllAuxs(
          java.lang.Iterable<? extends pl.edu.icm.coansys.models.PICProtos.Auxiliar> values) {
        if (auxsBuilder_ == null) {
          ensureAuxsIsMutable();
          super.addAll(values, auxs_);
          onChanged();
        } else {
          auxsBuilder_.addAllMessages(values);
        }
        return this;
      }
      public Builder clearAuxs() {
        if (auxsBuilder_ == null) {
          auxs_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000004);
          onChanged();
        } else {
          auxsBuilder_.clear();
        }
        return this;
      }
      public Builder removeAuxs(int index) {
        if (auxsBuilder_ == null) {
          ensureAuxsIsMutable();
          auxs_.remove(index);
          onChanged();
        } else {
          auxsBuilder_.remove(index);
        }
        return this;
      }
      public pl.edu.icm.coansys.models.PICProtos.Auxiliar.Builder getAuxsBuilder(
          int index) {
        return getAuxsFieldBuilder().getBuilder(index);
      }
      public pl.edu.icm.coansys.models.PICProtos.AuxiliarOrBuilder getAuxsOrBuilder(
          int index) {
        if (auxsBuilder_ == null) {
          return auxs_.get(index);  } else {
          return auxsBuilder_.getMessageOrBuilder(index);
        }
      }
      public java.util.List<? extends pl.edu.icm.coansys.models.PICProtos.AuxiliarOrBuilder> 
           getAuxsOrBuilderList() {
        if (auxsBuilder_ != null) {
          return auxsBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(auxs_);
        }
      }
      public pl.edu.icm.coansys.models.PICProtos.Auxiliar.Builder addAuxsBuilder() {
        return getAuxsFieldBuilder().addBuilder(
            pl.edu.icm.coansys.models.PICProtos.Auxiliar.getDefaultInstance());
      }
      public pl.edu.icm.coansys.models.PICProtos.Auxiliar.Builder addAuxsBuilder(
          int index) {
        return getAuxsFieldBuilder().addBuilder(
            index, pl.edu.icm.coansys.models.PICProtos.Auxiliar.getDefaultInstance());
      }
      public java.util.List<pl.edu.icm.coansys.models.PICProtos.Auxiliar.Builder> 
           getAuxsBuilderList() {
        return getAuxsFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilder<
          pl.edu.icm.coansys.models.PICProtos.Auxiliar, pl.edu.icm.coansys.models.PICProtos.Auxiliar.Builder, pl.edu.icm.coansys.models.PICProtos.AuxiliarOrBuilder> 
          getAuxsFieldBuilder() {
        if (auxsBuilder_ == null) {
          auxsBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
              pl.edu.icm.coansys.models.PICProtos.Auxiliar, pl.edu.icm.coansys.models.PICProtos.Auxiliar.Builder, pl.edu.icm.coansys.models.PICProtos.AuxiliarOrBuilder>(
                  auxs_,
                  ((bitField0_ & 0x00000004) == 0x00000004),
                  getParentForChildren(),
                  isClean());
          auxs_ = null;
        }
        return auxsBuilder_;
      }
      
      // @@protoc_insertion_point(builder_scope:pl.edu.icm.coansys.models.Reference)
    }
    
    static {
      defaultInstance = new Reference(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:pl.edu.icm.coansys.models.Reference)
  }
  
  public interface AuxiliarOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // optional string type = 1;
    boolean hasType();
    String getType();
    
    // optional string value = 2;
    boolean hasValue();
    String getValue();
  }
  public static final class Auxiliar extends
      com.google.protobuf.GeneratedMessage
      implements AuxiliarOrBuilder {
    // Use Auxiliar.newBuilder() to construct.
    private Auxiliar(Builder builder) {
      super(builder);
    }
    private Auxiliar(boolean noInit) {}
    
    private static final Auxiliar defaultInstance;
    public static Auxiliar getDefaultInstance() {
      return defaultInstance;
    }
    
    public Auxiliar getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return pl.edu.icm.coansys.models.PICProtos.internal_static_pl_edu_icm_coansys_models_Auxiliar_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return pl.edu.icm.coansys.models.PICProtos.internal_static_pl_edu_icm_coansys_models_Auxiliar_fieldAccessorTable;
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
    
    // optional string value = 2;
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
    
    private void initFields() {
      type_ = "";
      value_ = "";
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
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
        output.writeBytes(2, getValueBytes());
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
          .computeBytesSize(2, getValueBytes());
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
    
    public static pl.edu.icm.coansys.models.PICProtos.Auxiliar parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static pl.edu.icm.coansys.models.PICProtos.Auxiliar parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.models.PICProtos.Auxiliar parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static pl.edu.icm.coansys.models.PICProtos.Auxiliar parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.models.PICProtos.Auxiliar parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static pl.edu.icm.coansys.models.PICProtos.Auxiliar parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.models.PICProtos.Auxiliar parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static pl.edu.icm.coansys.models.PICProtos.Auxiliar parseDelimitedFrom(
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
    public static pl.edu.icm.coansys.models.PICProtos.Auxiliar parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static pl.edu.icm.coansys.models.PICProtos.Auxiliar parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(pl.edu.icm.coansys.models.PICProtos.Auxiliar prototype) {
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
       implements pl.edu.icm.coansys.models.PICProtos.AuxiliarOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return pl.edu.icm.coansys.models.PICProtos.internal_static_pl_edu_icm_coansys_models_Auxiliar_descriptor;
      }
      
      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return pl.edu.icm.coansys.models.PICProtos.internal_static_pl_edu_icm_coansys_models_Auxiliar_fieldAccessorTable;
      }
      
      // Construct using pl.edu.icm.coansys.models.PICProtos.Auxiliar.newBuilder()
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
        type_ = "";
        bitField0_ = (bitField0_ & ~0x00000001);
        value_ = "";
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return pl.edu.icm.coansys.models.PICProtos.Auxiliar.getDescriptor();
      }
      
      public pl.edu.icm.coansys.models.PICProtos.Auxiliar getDefaultInstanceForType() {
        return pl.edu.icm.coansys.models.PICProtos.Auxiliar.getDefaultInstance();
      }
      
      public pl.edu.icm.coansys.models.PICProtos.Auxiliar build() {
        pl.edu.icm.coansys.models.PICProtos.Auxiliar result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private pl.edu.icm.coansys.models.PICProtos.Auxiliar buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        pl.edu.icm.coansys.models.PICProtos.Auxiliar result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public pl.edu.icm.coansys.models.PICProtos.Auxiliar buildPartial() {
        pl.edu.icm.coansys.models.PICProtos.Auxiliar result = new pl.edu.icm.coansys.models.PICProtos.Auxiliar(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.type_ = type_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.value_ = value_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof pl.edu.icm.coansys.models.PICProtos.Auxiliar) {
          return mergeFrom((pl.edu.icm.coansys.models.PICProtos.Auxiliar)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(pl.edu.icm.coansys.models.PICProtos.Auxiliar other) {
        if (other == pl.edu.icm.coansys.models.PICProtos.Auxiliar.getDefaultInstance()) return this;
        if (other.hasType()) {
          setType(other.getType());
        }
        if (other.hasValue()) {
          setValue(other.getValue());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
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
              value_ = input.readBytes();
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
      
      // optional string value = 2;
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
      
      // @@protoc_insertion_point(builder_scope:pl.edu.icm.coansys.models.Auxiliar)
    }
    
    static {
      defaultInstance = new Auxiliar(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:pl.edu.icm.coansys.models.Auxiliar)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_pl_edu_icm_coansys_models_PicOut_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_pl_edu_icm_coansys_models_PicOut_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_pl_edu_icm_coansys_models_Reference_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_pl_edu_icm_coansys_models_Reference_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_pl_edu_icm_coansys_models_Auxiliar_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_pl_edu_icm_coansys_models_Auxiliar_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\rpic_out.proto\022\031pl.edu.icm.coansys.mode" +
      "ls\"~\n\006PicOut\022\r\n\005docId\030\001 \002(\t\0222\n\004refs\030\002 \003(" +
      "\0132$.pl.edu.icm.coansys.models.Reference\022" +
      "1\n\004auxs\030\003 \003(\0132#.pl.edu.icm.coansys.model" +
      "s.Auxiliar\"]\n\tReference\022\016\n\006refNum\030\001 \002(\005\022" +
      "\r\n\005docId\030\002 \002(\t\0221\n\004auxs\030\003 \003(\0132#.pl.edu.ic" +
      "m.coansys.models.Auxiliar\"\'\n\010Auxiliar\022\014\n" +
      "\004type\030\001 \001(\t\022\r\n\005value\030\002 \001(\tB&\n\031pl.edu.icm" +
      ".coansys.modelsB\tPICProtos"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_pl_edu_icm_coansys_models_PicOut_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_pl_edu_icm_coansys_models_PicOut_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_pl_edu_icm_coansys_models_PicOut_descriptor,
              new java.lang.String[] { "DocId", "Refs", "Auxs", },
              pl.edu.icm.coansys.models.PICProtos.PicOut.class,
              pl.edu.icm.coansys.models.PICProtos.PicOut.Builder.class);
          internal_static_pl_edu_icm_coansys_models_Reference_descriptor =
            getDescriptor().getMessageTypes().get(1);
          internal_static_pl_edu_icm_coansys_models_Reference_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_pl_edu_icm_coansys_models_Reference_descriptor,
              new java.lang.String[] { "RefNum", "DocId", "Auxs", },
              pl.edu.icm.coansys.models.PICProtos.Reference.class,
              pl.edu.icm.coansys.models.PICProtos.Reference.Builder.class);
          internal_static_pl_edu_icm_coansys_models_Auxiliar_descriptor =
            getDescriptor().getMessageTypes().get(2);
          internal_static_pl_edu_icm_coansys_models_Auxiliar_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_pl_edu_icm_coansys_models_Auxiliar_descriptor,
              new java.lang.String[] { "Type", "Value", },
              pl.edu.icm.coansys.models.PICProtos.Auxiliar.class,
              pl.edu.icm.coansys.models.PICProtos.Auxiliar.Builder.class);
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
