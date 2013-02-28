// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: kwd_extraction_out.proto

package pl.edu.icm.coansys.importers.models;

public final class KeywordExtractionProtos {
  private KeywordExtractionProtos() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface ExtractedKeywordsOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // required string docId = 1;
    boolean hasDocId();
    String getDocId();
    
    // optional int64 timestamp = 2;
    boolean hasTimestamp();
    long getTimestamp();
    
    // repeated string keyword = 3;
    java.util.List<String> getKeywordList();
    int getKeywordCount();
    String getKeyword(int index);
    
    // optional string algorithm = 4;
    boolean hasAlgorithm();
    String getAlgorithm();
    
    // optional string comment = 5;
    boolean hasComment();
    String getComment();
  }
  public static final class ExtractedKeywords extends
      com.google.protobuf.GeneratedMessage
      implements ExtractedKeywordsOrBuilder {
    // Use ExtractedKeywords.newBuilder() to construct.
    private ExtractedKeywords(Builder builder) {
      super(builder);
    }
    private ExtractedKeywords(boolean noInit) {}
    
    private static final ExtractedKeywords defaultInstance;
    public static ExtractedKeywords getDefaultInstance() {
      return defaultInstance;
    }
    
    public ExtractedKeywords getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return pl.edu.icm.coansys.importers.models.KeywordExtractionProtos.internal_static_ExtractedKeywords_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return pl.edu.icm.coansys.importers.models.KeywordExtractionProtos.internal_static_ExtractedKeywords_fieldAccessorTable;
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
    
    // optional int64 timestamp = 2;
    public static final int TIMESTAMP_FIELD_NUMBER = 2;
    private long timestamp_;
    public boolean hasTimestamp() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    public long getTimestamp() {
      return timestamp_;
    }
    
    // repeated string keyword = 3;
    public static final int KEYWORD_FIELD_NUMBER = 3;
    private com.google.protobuf.LazyStringList keyword_;
    public java.util.List<String>
        getKeywordList() {
      return keyword_;
    }
    public int getKeywordCount() {
      return keyword_.size();
    }
    public String getKeyword(int index) {
      return keyword_.get(index);
    }
    
    // optional string algorithm = 4;
    public static final int ALGORITHM_FIELD_NUMBER = 4;
    private java.lang.Object algorithm_;
    public boolean hasAlgorithm() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    public String getAlgorithm() {
      java.lang.Object ref = algorithm_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          algorithm_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getAlgorithmBytes() {
      java.lang.Object ref = algorithm_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        algorithm_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // optional string comment = 5;
    public static final int COMMENT_FIELD_NUMBER = 5;
    private java.lang.Object comment_;
    public boolean hasComment() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
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
      docId_ = "";
      timestamp_ = 0L;
      keyword_ = com.google.protobuf.LazyStringArrayList.EMPTY;
      algorithm_ = "";
      comment_ = "";
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
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
        output.writeBytes(1, getDocIdBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeInt64(2, timestamp_);
      }
      for (int i = 0; i < keyword_.size(); i++) {
        output.writeBytes(3, keyword_.getByteString(i));
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeBytes(4, getAlgorithmBytes());
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeBytes(5, getCommentBytes());
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
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt64Size(2, timestamp_);
      }
      {
        int dataSize = 0;
        for (int i = 0; i < keyword_.size(); i++) {
          dataSize += com.google.protobuf.CodedOutputStream
            .computeBytesSizeNoTag(keyword_.getByteString(i));
        }
        size += dataSize;
        size += 1 * getKeywordList().size();
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(4, getAlgorithmBytes());
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(5, getCommentBytes());
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
    
    public static pl.edu.icm.coansys.importers.models.KeywordExtractionProtos.ExtractedKeywords parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static pl.edu.icm.coansys.importers.models.KeywordExtractionProtos.ExtractedKeywords parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.importers.models.KeywordExtractionProtos.ExtractedKeywords parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static pl.edu.icm.coansys.importers.models.KeywordExtractionProtos.ExtractedKeywords parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.importers.models.KeywordExtractionProtos.ExtractedKeywords parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static pl.edu.icm.coansys.importers.models.KeywordExtractionProtos.ExtractedKeywords parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static pl.edu.icm.coansys.importers.models.KeywordExtractionProtos.ExtractedKeywords parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static pl.edu.icm.coansys.importers.models.KeywordExtractionProtos.ExtractedKeywords parseDelimitedFrom(
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
    public static pl.edu.icm.coansys.importers.models.KeywordExtractionProtos.ExtractedKeywords parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static pl.edu.icm.coansys.importers.models.KeywordExtractionProtos.ExtractedKeywords parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(pl.edu.icm.coansys.importers.models.KeywordExtractionProtos.ExtractedKeywords prototype) {
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
       implements pl.edu.icm.coansys.importers.models.KeywordExtractionProtos.ExtractedKeywordsOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return pl.edu.icm.coansys.importers.models.KeywordExtractionProtos.internal_static_ExtractedKeywords_descriptor;
      }
      
      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return pl.edu.icm.coansys.importers.models.KeywordExtractionProtos.internal_static_ExtractedKeywords_fieldAccessorTable;
      }
      
      // Construct using pl.edu.icm.coansys.importers.models.KeywordExtractionProtos.ExtractedKeywords.newBuilder()
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
        docId_ = "";
        bitField0_ = (bitField0_ & ~0x00000001);
        timestamp_ = 0L;
        bitField0_ = (bitField0_ & ~0x00000002);
        keyword_ = com.google.protobuf.LazyStringArrayList.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000004);
        algorithm_ = "";
        bitField0_ = (bitField0_ & ~0x00000008);
        comment_ = "";
        bitField0_ = (bitField0_ & ~0x00000010);
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return pl.edu.icm.coansys.importers.models.KeywordExtractionProtos.ExtractedKeywords.getDescriptor();
      }
      
      public pl.edu.icm.coansys.importers.models.KeywordExtractionProtos.ExtractedKeywords getDefaultInstanceForType() {
        return pl.edu.icm.coansys.importers.models.KeywordExtractionProtos.ExtractedKeywords.getDefaultInstance();
      }
      
      public pl.edu.icm.coansys.importers.models.KeywordExtractionProtos.ExtractedKeywords build() {
        pl.edu.icm.coansys.importers.models.KeywordExtractionProtos.ExtractedKeywords result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private pl.edu.icm.coansys.importers.models.KeywordExtractionProtos.ExtractedKeywords buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        pl.edu.icm.coansys.importers.models.KeywordExtractionProtos.ExtractedKeywords result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public pl.edu.icm.coansys.importers.models.KeywordExtractionProtos.ExtractedKeywords buildPartial() {
        pl.edu.icm.coansys.importers.models.KeywordExtractionProtos.ExtractedKeywords result = new pl.edu.icm.coansys.importers.models.KeywordExtractionProtos.ExtractedKeywords(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.docId_ = docId_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.timestamp_ = timestamp_;
        if (((bitField0_ & 0x00000004) == 0x00000004)) {
          keyword_ = new com.google.protobuf.UnmodifiableLazyStringList(
              keyword_);
          bitField0_ = (bitField0_ & ~0x00000004);
        }
        result.keyword_ = keyword_;
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000004;
        }
        result.algorithm_ = algorithm_;
        if (((from_bitField0_ & 0x00000010) == 0x00000010)) {
          to_bitField0_ |= 0x00000008;
        }
        result.comment_ = comment_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof pl.edu.icm.coansys.importers.models.KeywordExtractionProtos.ExtractedKeywords) {
          return mergeFrom((pl.edu.icm.coansys.importers.models.KeywordExtractionProtos.ExtractedKeywords)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(pl.edu.icm.coansys.importers.models.KeywordExtractionProtos.ExtractedKeywords other) {
        if (other == pl.edu.icm.coansys.importers.models.KeywordExtractionProtos.ExtractedKeywords.getDefaultInstance()) return this;
        if (other.hasDocId()) {
          setDocId(other.getDocId());
        }
        if (other.hasTimestamp()) {
          setTimestamp(other.getTimestamp());
        }
        if (!other.keyword_.isEmpty()) {
          if (keyword_.isEmpty()) {
            keyword_ = other.keyword_;
            bitField0_ = (bitField0_ & ~0x00000004);
          } else {
            ensureKeywordIsMutable();
            keyword_.addAll(other.keyword_);
          }
          onChanged();
        }
        if (other.hasAlgorithm()) {
          setAlgorithm(other.getAlgorithm());
        }
        if (other.hasComment()) {
          setComment(other.getComment());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
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
            case 10: {
              bitField0_ |= 0x00000001;
              docId_ = input.readBytes();
              break;
            }
            case 16: {
              bitField0_ |= 0x00000002;
              timestamp_ = input.readInt64();
              break;
            }
            case 26: {
              ensureKeywordIsMutable();
              keyword_.add(input.readBytes());
              break;
            }
            case 34: {
              bitField0_ |= 0x00000008;
              algorithm_ = input.readBytes();
              break;
            }
            case 42: {
              bitField0_ |= 0x00000010;
              comment_ = input.readBytes();
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
      
      // optional int64 timestamp = 2;
      private long timestamp_ ;
      public boolean hasTimestamp() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      public long getTimestamp() {
        return timestamp_;
      }
      public Builder setTimestamp(long value) {
        bitField0_ |= 0x00000002;
        timestamp_ = value;
        onChanged();
        return this;
      }
      public Builder clearTimestamp() {
        bitField0_ = (bitField0_ & ~0x00000002);
        timestamp_ = 0L;
        onChanged();
        return this;
      }
      
      // repeated string keyword = 3;
      private com.google.protobuf.LazyStringList keyword_ = com.google.protobuf.LazyStringArrayList.EMPTY;
      private void ensureKeywordIsMutable() {
        if (!((bitField0_ & 0x00000004) == 0x00000004)) {
          keyword_ = new com.google.protobuf.LazyStringArrayList(keyword_);
          bitField0_ |= 0x00000004;
         }
      }
      public java.util.List<String>
          getKeywordList() {
        return java.util.Collections.unmodifiableList(keyword_);
      }
      public int getKeywordCount() {
        return keyword_.size();
      }
      public String getKeyword(int index) {
        return keyword_.get(index);
      }
      public Builder setKeyword(
          int index, String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  ensureKeywordIsMutable();
        keyword_.set(index, value);
        onChanged();
        return this;
      }
      public Builder addKeyword(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  ensureKeywordIsMutable();
        keyword_.add(value);
        onChanged();
        return this;
      }
      public Builder addAllKeyword(
          java.lang.Iterable<String> values) {
        ensureKeywordIsMutable();
        super.addAll(values, keyword_);
        onChanged();
        return this;
      }
      public Builder clearKeyword() {
        keyword_ = com.google.protobuf.LazyStringArrayList.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000004);
        onChanged();
        return this;
      }
      void addKeyword(com.google.protobuf.ByteString value) {
        ensureKeywordIsMutable();
        keyword_.add(value);
        onChanged();
      }
      
      // optional string algorithm = 4;
      private java.lang.Object algorithm_ = "";
      public boolean hasAlgorithm() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      public String getAlgorithm() {
        java.lang.Object ref = algorithm_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          algorithm_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setAlgorithm(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000008;
        algorithm_ = value;
        onChanged();
        return this;
      }
      public Builder clearAlgorithm() {
        bitField0_ = (bitField0_ & ~0x00000008);
        algorithm_ = getDefaultInstance().getAlgorithm();
        onChanged();
        return this;
      }
      void setAlgorithm(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000008;
        algorithm_ = value;
        onChanged();
      }
      
      // optional string comment = 5;
      private java.lang.Object comment_ = "";
      public boolean hasComment() {
        return ((bitField0_ & 0x00000010) == 0x00000010);
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
  bitField0_ |= 0x00000010;
        comment_ = value;
        onChanged();
        return this;
      }
      public Builder clearComment() {
        bitField0_ = (bitField0_ & ~0x00000010);
        comment_ = getDefaultInstance().getComment();
        onChanged();
        return this;
      }
      void setComment(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000010;
        comment_ = value;
        onChanged();
      }
      
      // @@protoc_insertion_point(builder_scope:ExtractedKeywords)
    }
    
    static {
      defaultInstance = new ExtractedKeywords(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:ExtractedKeywords)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_ExtractedKeywords_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_ExtractedKeywords_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\030kwd_extraction_out.proto\"j\n\021ExtractedK" +
      "eywords\022\r\n\005docId\030\001 \002(\t\022\021\n\ttimestamp\030\002 \001(" +
      "\003\022\017\n\007keyword\030\003 \003(\t\022\021\n\talgorithm\030\004 \001(\t\022\017\n" +
      "\007comment\030\005 \001(\tB>\n#pl.edu.icm.coansys.imp" +
      "orters.modelsB\027KeywordExtractionProtos"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_ExtractedKeywords_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_ExtractedKeywords_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_ExtractedKeywords_descriptor,
              new java.lang.String[] { "DocId", "Timestamp", "Keyword", "Algorithm", "Comment", },
              pl.edu.icm.coansys.importers.models.KeywordExtractionProtos.ExtractedKeywords.class,
              pl.edu.icm.coansys.importers.models.KeywordExtractionProtos.ExtractedKeywords.Builder.class);
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
