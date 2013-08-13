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

package pl.edu.icm.coansys.citations.data;

public final class CitationMatchingProtos {
    private CitationMatchingProtos() {
    }

    public static void registerAllExtensions(
            com.google.protobuf.ExtensionRegistry registry) {
    }

    public interface MatchableEntityDataOrBuilder
            extends com.google.protobuf.MessageOrBuilder {

        // required string id = 1;
        boolean hasId();

        String getId();

        // optional string author = 2;
        boolean hasAuthor();

        String getAuthor();

        // optional string year = 3;
        boolean hasYear();

        String getYear();

        // optional string pages = 4;
        boolean hasPages();

        String getPages();

        // optional string source = 5;
        boolean hasSource();

        String getSource();

        // optional string title = 6;
        boolean hasTitle();

        String getTitle();

        // repeated .pl.edu.icm.coansys.citations.KeyValue auxiliary = 7;
        java.util.List<pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue>
        getAuxiliaryList();

        pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue getAuxiliary(int index);

        int getAuxiliaryCount();

        java.util.List<? extends pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValueOrBuilder>
        getAuxiliaryOrBuilderList();

        pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValueOrBuilder getAuxiliaryOrBuilder(
                int index);
    }

    public static final class MatchableEntityData extends
            com.google.protobuf.GeneratedMessage
            implements MatchableEntityDataOrBuilder {
        // Use MatchableEntityData.newBuilder() to construct.
        private MatchableEntityData(Builder builder) {
            super(builder);
        }

        private MatchableEntityData(boolean noInit) {
        }

        private static final MatchableEntityData defaultInstance;

        public static MatchableEntityData getDefaultInstance() {
            return defaultInstance;
        }

        public MatchableEntityData getDefaultInstanceForType() {
            return defaultInstance;
        }

        public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
            return pl.edu.icm.coansys.citations.data.CitationMatchingProtos.internal_static_pl_edu_icm_coansys_citations_MatchableEntityData_descriptor;
        }

        protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
            return pl.edu.icm.coansys.citations.data.CitationMatchingProtos.internal_static_pl_edu_icm_coansys_citations_MatchableEntityData_fieldAccessorTable;
        }

        private int bitField0_;
        // required string id = 1;
        public static final int ID_FIELD_NUMBER = 1;
        private java.lang.Object id_;

        public boolean hasId() {
            return ((bitField0_ & 0x00000001) == 0x00000001);
        }

        public String getId() {
            java.lang.Object ref = id_;
            if (ref instanceof String) {
                return (String) ref;
            } else {
                com.google.protobuf.ByteString bs =
                        (com.google.protobuf.ByteString) ref;
                String s = bs.toStringUtf8();
                if (com.google.protobuf.Internal.isValidUtf8(bs)) {
                    id_ = s;
                }
                return s;
            }
        }

        private com.google.protobuf.ByteString getIdBytes() {
            java.lang.Object ref = id_;
            if (ref instanceof String) {
                com.google.protobuf.ByteString b =
                        com.google.protobuf.ByteString.copyFromUtf8((String) ref);
                id_ = b;
                return b;
            } else {
                return (com.google.protobuf.ByteString) ref;
            }
        }

        // optional string author = 2;
        public static final int AUTHOR_FIELD_NUMBER = 2;
        private java.lang.Object author_;

        public boolean hasAuthor() {
            return ((bitField0_ & 0x00000002) == 0x00000002);
        }

        public String getAuthor() {
            java.lang.Object ref = author_;
            if (ref instanceof String) {
                return (String) ref;
            } else {
                com.google.protobuf.ByteString bs =
                        (com.google.protobuf.ByteString) ref;
                String s = bs.toStringUtf8();
                if (com.google.protobuf.Internal.isValidUtf8(bs)) {
                    author_ = s;
                }
                return s;
            }
        }

        private com.google.protobuf.ByteString getAuthorBytes() {
            java.lang.Object ref = author_;
            if (ref instanceof String) {
                com.google.protobuf.ByteString b =
                        com.google.protobuf.ByteString.copyFromUtf8((String) ref);
                author_ = b;
                return b;
            } else {
                return (com.google.protobuf.ByteString) ref;
            }
        }

        // optional string year = 3;
        public static final int YEAR_FIELD_NUMBER = 3;
        private java.lang.Object year_;

        public boolean hasYear() {
            return ((bitField0_ & 0x00000004) == 0x00000004);
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

        // optional string pages = 4;
        public static final int PAGES_FIELD_NUMBER = 4;
        private java.lang.Object pages_;

        public boolean hasPages() {
            return ((bitField0_ & 0x00000008) == 0x00000008);
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

        // optional string source = 5;
        public static final int SOURCE_FIELD_NUMBER = 5;
        private java.lang.Object source_;

        public boolean hasSource() {
            return ((bitField0_ & 0x00000010) == 0x00000010);
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

        // optional string title = 6;
        public static final int TITLE_FIELD_NUMBER = 6;
        private java.lang.Object title_;

        public boolean hasTitle() {
            return ((bitField0_ & 0x00000020) == 0x00000020);
        }

        public String getTitle() {
            java.lang.Object ref = title_;
            if (ref instanceof String) {
                return (String) ref;
            } else {
                com.google.protobuf.ByteString bs =
                        (com.google.protobuf.ByteString) ref;
                String s = bs.toStringUtf8();
                if (com.google.protobuf.Internal.isValidUtf8(bs)) {
                    title_ = s;
                }
                return s;
            }
        }

        private com.google.protobuf.ByteString getTitleBytes() {
            java.lang.Object ref = title_;
            if (ref instanceof String) {
                com.google.protobuf.ByteString b =
                        com.google.protobuf.ByteString.copyFromUtf8((String) ref);
                title_ = b;
                return b;
            } else {
                return (com.google.protobuf.ByteString) ref;
            }
        }

        // repeated .pl.edu.icm.coansys.citations.KeyValue auxiliary = 7;
        public static final int AUXILIARY_FIELD_NUMBER = 7;
        private java.util.List<pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue> auxiliary_;

        public java.util.List<pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue> getAuxiliaryList() {
            return auxiliary_;
        }

        public java.util.List<? extends pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValueOrBuilder>
        getAuxiliaryOrBuilderList() {
            return auxiliary_;
        }

        public int getAuxiliaryCount() {
            return auxiliary_.size();
        }

        public pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue getAuxiliary(int index) {
            return auxiliary_.get(index);
        }

        public pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValueOrBuilder getAuxiliaryOrBuilder(
                int index) {
            return auxiliary_.get(index);
        }

        private void initFields() {
            id_ = "";
            author_ = "";
            year_ = "";
            pages_ = "";
            source_ = "";
            title_ = "";
            auxiliary_ = java.util.Collections.emptyList();
        }

        private byte memoizedIsInitialized = -1;

        public final boolean isInitialized() {
            byte isInitialized = memoizedIsInitialized;
            if (isInitialized != -1) return isInitialized == 1;

            if (!hasId()) {
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
                output.writeBytes(1, getIdBytes());
            }
            if (((bitField0_ & 0x00000002) == 0x00000002)) {
                output.writeBytes(2, getAuthorBytes());
            }
            if (((bitField0_ & 0x00000004) == 0x00000004)) {
                output.writeBytes(3, getYearBytes());
            }
            if (((bitField0_ & 0x00000008) == 0x00000008)) {
                output.writeBytes(4, getPagesBytes());
            }
            if (((bitField0_ & 0x00000010) == 0x00000010)) {
                output.writeBytes(5, getSourceBytes());
            }
            if (((bitField0_ & 0x00000020) == 0x00000020)) {
                output.writeBytes(6, getTitleBytes());
            }
            for (int i = 0; i < auxiliary_.size(); i++) {
                output.writeMessage(7, auxiliary_.get(i));
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
                        .computeBytesSize(1, getIdBytes());
            }
            if (((bitField0_ & 0x00000002) == 0x00000002)) {
                size += com.google.protobuf.CodedOutputStream
                        .computeBytesSize(2, getAuthorBytes());
            }
            if (((bitField0_ & 0x00000004) == 0x00000004)) {
                size += com.google.protobuf.CodedOutputStream
                        .computeBytesSize(3, getYearBytes());
            }
            if (((bitField0_ & 0x00000008) == 0x00000008)) {
                size += com.google.protobuf.CodedOutputStream
                        .computeBytesSize(4, getPagesBytes());
            }
            if (((bitField0_ & 0x00000010) == 0x00000010)) {
                size += com.google.protobuf.CodedOutputStream
                        .computeBytesSize(5, getSourceBytes());
            }
            if (((bitField0_ & 0x00000020) == 0x00000020)) {
                size += com.google.protobuf.CodedOutputStream
                        .computeBytesSize(6, getTitleBytes());
            }
            for (int i = 0; i < auxiliary_.size(); i++) {
                size += com.google.protobuf.CodedOutputStream
                        .computeMessageSize(7, auxiliary_.get(i));
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

        public static pl.edu.icm.coansys.citations.data.CitationMatchingProtos.MatchableEntityData parseFrom(
                com.google.protobuf.ByteString data)
                throws com.google.protobuf.InvalidProtocolBufferException {
            return newBuilder().mergeFrom(data).buildParsed();
        }

        public static pl.edu.icm.coansys.citations.data.CitationMatchingProtos.MatchableEntityData parseFrom(
                com.google.protobuf.ByteString data,
                com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                throws com.google.protobuf.InvalidProtocolBufferException {
            return newBuilder().mergeFrom(data, extensionRegistry)
                    .buildParsed();
        }

        public static pl.edu.icm.coansys.citations.data.CitationMatchingProtos.MatchableEntityData parseFrom(byte[] data)
                throws com.google.protobuf.InvalidProtocolBufferException {
            return newBuilder().mergeFrom(data).buildParsed();
        }

        public static pl.edu.icm.coansys.citations.data.CitationMatchingProtos.MatchableEntityData parseFrom(
                byte[] data,
                com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                throws com.google.protobuf.InvalidProtocolBufferException {
            return newBuilder().mergeFrom(data, extensionRegistry)
                    .buildParsed();
        }

        public static pl.edu.icm.coansys.citations.data.CitationMatchingProtos.MatchableEntityData parseFrom(java.io.InputStream input)
                throws java.io.IOException {
            return newBuilder().mergeFrom(input).buildParsed();
        }

        public static pl.edu.icm.coansys.citations.data.CitationMatchingProtos.MatchableEntityData parseFrom(
                java.io.InputStream input,
                com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                throws java.io.IOException {
            return newBuilder().mergeFrom(input, extensionRegistry)
                    .buildParsed();
        }

        public static pl.edu.icm.coansys.citations.data.CitationMatchingProtos.MatchableEntityData parseDelimitedFrom(java.io.InputStream input)
                throws java.io.IOException {
            Builder builder = newBuilder();
            if (builder.mergeDelimitedFrom(input)) {
                return builder.buildParsed();
            } else {
                return null;
            }
        }

        public static pl.edu.icm.coansys.citations.data.CitationMatchingProtos.MatchableEntityData parseDelimitedFrom(
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

        public static pl.edu.icm.coansys.citations.data.CitationMatchingProtos.MatchableEntityData parseFrom(
                com.google.protobuf.CodedInputStream input)
                throws java.io.IOException {
            return newBuilder().mergeFrom(input).buildParsed();
        }

        public static pl.edu.icm.coansys.citations.data.CitationMatchingProtos.MatchableEntityData parseFrom(
                com.google.protobuf.CodedInputStream input,
                com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                throws java.io.IOException {
            return newBuilder().mergeFrom(input, extensionRegistry)
                    .buildParsed();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(pl.edu.icm.coansys.citations.data.CitationMatchingProtos.MatchableEntityData prototype) {
            return newBuilder().mergeFrom(prototype);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        @java.lang.Override
        protected Builder newBuilderForType(
                com.google.protobuf.GeneratedMessage.BuilderParent parent) {
            Builder builder = new Builder(parent);
            return builder;
        }

        public static final class Builder extends
                com.google.protobuf.GeneratedMessage.Builder<Builder>
                implements pl.edu.icm.coansys.citations.data.CitationMatchingProtos.MatchableEntityDataOrBuilder {
            public static final com.google.protobuf.Descriptors.Descriptor
            getDescriptor() {
                return pl.edu.icm.coansys.citations.data.CitationMatchingProtos.internal_static_pl_edu_icm_coansys_citations_MatchableEntityData_descriptor;
            }

            protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
            internalGetFieldAccessorTable() {
                return pl.edu.icm.coansys.citations.data.CitationMatchingProtos.internal_static_pl_edu_icm_coansys_citations_MatchableEntityData_fieldAccessorTable;
            }

            // Construct using pl.edu.icm.coansys.citations.data.CitationMatchingProtos.MatchableEntityData.newBuilder()
            private Builder() {
                maybeForceBuilderInitialization();
            }

            private Builder(com.google.protobuf.GeneratedMessage.BuilderParent parent) {
                super(parent);
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
                    getAuxiliaryFieldBuilder();
                }
            }

            private static Builder create() {
                return new Builder();
            }

            public Builder clear() {
                super.clear();
                id_ = "";
                bitField0_ = (bitField0_ & ~0x00000001);
                author_ = "";
                bitField0_ = (bitField0_ & ~0x00000002);
                year_ = "";
                bitField0_ = (bitField0_ & ~0x00000004);
                pages_ = "";
                bitField0_ = (bitField0_ & ~0x00000008);
                source_ = "";
                bitField0_ = (bitField0_ & ~0x00000010);
                title_ = "";
                bitField0_ = (bitField0_ & ~0x00000020);
                if (auxiliaryBuilder_ == null) {
                    auxiliary_ = java.util.Collections.emptyList();
                    bitField0_ = (bitField0_ & ~0x00000040);
                } else {
                    auxiliaryBuilder_.clear();
                }
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(buildPartial());
            }

            public com.google.protobuf.Descriptors.Descriptor
            getDescriptorForType() {
                return pl.edu.icm.coansys.citations.data.CitationMatchingProtos.MatchableEntityData.getDescriptor();
            }

            public pl.edu.icm.coansys.citations.data.CitationMatchingProtos.MatchableEntityData getDefaultInstanceForType() {
                return pl.edu.icm.coansys.citations.data.CitationMatchingProtos.MatchableEntityData.getDefaultInstance();
            }

            public pl.edu.icm.coansys.citations.data.CitationMatchingProtos.MatchableEntityData build() {
                pl.edu.icm.coansys.citations.data.CitationMatchingProtos.MatchableEntityData result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException(result);
                }
                return result;
            }

            private pl.edu.icm.coansys.citations.data.CitationMatchingProtos.MatchableEntityData buildParsed()
                    throws com.google.protobuf.InvalidProtocolBufferException {
                pl.edu.icm.coansys.citations.data.CitationMatchingProtos.MatchableEntityData result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException(
                            result).asInvalidProtocolBufferException();
                }
                return result;
            }

            public pl.edu.icm.coansys.citations.data.CitationMatchingProtos.MatchableEntityData buildPartial() {
                pl.edu.icm.coansys.citations.data.CitationMatchingProtos.MatchableEntityData result = new pl.edu.icm.coansys.citations.data.CitationMatchingProtos.MatchableEntityData(this);
                int from_bitField0_ = bitField0_;
                int to_bitField0_ = 0;
                if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
                    to_bitField0_ |= 0x00000001;
                }
                result.id_ = id_;
                if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
                    to_bitField0_ |= 0x00000002;
                }
                result.author_ = author_;
                if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
                    to_bitField0_ |= 0x00000004;
                }
                result.year_ = year_;
                if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
                    to_bitField0_ |= 0x00000008;
                }
                result.pages_ = pages_;
                if (((from_bitField0_ & 0x00000010) == 0x00000010)) {
                    to_bitField0_ |= 0x00000010;
                }
                result.source_ = source_;
                if (((from_bitField0_ & 0x00000020) == 0x00000020)) {
                    to_bitField0_ |= 0x00000020;
                }
                result.title_ = title_;
                if (auxiliaryBuilder_ == null) {
                    if (((bitField0_ & 0x00000040) == 0x00000040)) {
                        auxiliary_ = java.util.Collections.unmodifiableList(auxiliary_);
                        bitField0_ = (bitField0_ & ~0x00000040);
                    }
                    result.auxiliary_ = auxiliary_;
                } else {
                    result.auxiliary_ = auxiliaryBuilder_.build();
                }
                result.bitField0_ = to_bitField0_;
                onBuilt();
                return result;
            }

            public Builder mergeFrom(com.google.protobuf.Message other) {
                if (other instanceof pl.edu.icm.coansys.citations.data.CitationMatchingProtos.MatchableEntityData) {
                    return mergeFrom((pl.edu.icm.coansys.citations.data.CitationMatchingProtos.MatchableEntityData) other);
                } else {
                    super.mergeFrom(other);
                    return this;
                }
            }

            public Builder mergeFrom(pl.edu.icm.coansys.citations.data.CitationMatchingProtos.MatchableEntityData other) {
                if (other == pl.edu.icm.coansys.citations.data.CitationMatchingProtos.MatchableEntityData.getDefaultInstance())
                    return this;
                if (other.hasId()) {
                    setId(other.getId());
                }
                if (other.hasAuthor()) {
                    setAuthor(other.getAuthor());
                }
                if (other.hasYear()) {
                    setYear(other.getYear());
                }
                if (other.hasPages()) {
                    setPages(other.getPages());
                }
                if (other.hasSource()) {
                    setSource(other.getSource());
                }
                if (other.hasTitle()) {
                    setTitle(other.getTitle());
                }
                if (auxiliaryBuilder_ == null) {
                    if (!other.auxiliary_.isEmpty()) {
                        if (auxiliary_.isEmpty()) {
                            auxiliary_ = other.auxiliary_;
                            bitField0_ = (bitField0_ & ~0x00000040);
                        } else {
                            ensureAuxiliaryIsMutable();
                            auxiliary_.addAll(other.auxiliary_);
                        }
                        onChanged();
                    }
                } else {
                    if (!other.auxiliary_.isEmpty()) {
                        if (auxiliaryBuilder_.isEmpty()) {
                            auxiliaryBuilder_.dispose();
                            auxiliaryBuilder_ = null;
                            auxiliary_ = other.auxiliary_;
                            bitField0_ = (bitField0_ & ~0x00000040);
                            auxiliaryBuilder_ =
                                    com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                                            getAuxiliaryFieldBuilder() : null;
                        } else {
                            auxiliaryBuilder_.addAllMessages(other.auxiliary_);
                        }
                    }
                }
                this.mergeUnknownFields(other.getUnknownFields());
                return this;
            }

            public final boolean isInitialized() {
                if (!hasId()) {

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
                            id_ = input.readBytes();
                            break;
                        }
                        case 18: {
                            bitField0_ |= 0x00000002;
                            author_ = input.readBytes();
                            break;
                        }
                        case 26: {
                            bitField0_ |= 0x00000004;
                            year_ = input.readBytes();
                            break;
                        }
                        case 34: {
                            bitField0_ |= 0x00000008;
                            pages_ = input.readBytes();
                            break;
                        }
                        case 42: {
                            bitField0_ |= 0x00000010;
                            source_ = input.readBytes();
                            break;
                        }
                        case 50: {
                            bitField0_ |= 0x00000020;
                            title_ = input.readBytes();
                            break;
                        }
                        case 58: {
                            pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue.Builder subBuilder = pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue.newBuilder();
                            input.readMessage(subBuilder, extensionRegistry);
                            addAuxiliary(subBuilder.buildPartial());
                            break;
                        }
                    }
                }
            }

            private int bitField0_;

            // required string id = 1;
            private java.lang.Object id_ = "";

            public boolean hasId() {
                return ((bitField0_ & 0x00000001) == 0x00000001);
            }

            public String getId() {
                java.lang.Object ref = id_;
                if (!(ref instanceof String)) {
                    String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
                    id_ = s;
                    return s;
                } else {
                    return (String) ref;
                }
            }

            public Builder setId(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                bitField0_ |= 0x00000001;
                id_ = value;
                onChanged();
                return this;
            }

            public Builder clearId() {
                bitField0_ = (bitField0_ & ~0x00000001);
                id_ = getDefaultInstance().getId();
                onChanged();
                return this;
            }

            void setId(com.google.protobuf.ByteString value) {
                bitField0_ |= 0x00000001;
                id_ = value;
                onChanged();
            }

            // optional string author = 2;
            private java.lang.Object author_ = "";

            public boolean hasAuthor() {
                return ((bitField0_ & 0x00000002) == 0x00000002);
            }

            public String getAuthor() {
                java.lang.Object ref = author_;
                if (!(ref instanceof String)) {
                    String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
                    author_ = s;
                    return s;
                } else {
                    return (String) ref;
                }
            }

            public Builder setAuthor(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                bitField0_ |= 0x00000002;
                author_ = value;
                onChanged();
                return this;
            }

            public Builder clearAuthor() {
                bitField0_ = (bitField0_ & ~0x00000002);
                author_ = getDefaultInstance().getAuthor();
                onChanged();
                return this;
            }

            void setAuthor(com.google.protobuf.ByteString value) {
                bitField0_ |= 0x00000002;
                author_ = value;
                onChanged();
            }

            // optional string year = 3;
            private java.lang.Object year_ = "";

            public boolean hasYear() {
                return ((bitField0_ & 0x00000004) == 0x00000004);
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
                bitField0_ |= 0x00000004;
                year_ = value;
                onChanged();
                return this;
            }

            public Builder clearYear() {
                bitField0_ = (bitField0_ & ~0x00000004);
                year_ = getDefaultInstance().getYear();
                onChanged();
                return this;
            }

            void setYear(com.google.protobuf.ByteString value) {
                bitField0_ |= 0x00000004;
                year_ = value;
                onChanged();
            }

            // optional string pages = 4;
            private java.lang.Object pages_ = "";

            public boolean hasPages() {
                return ((bitField0_ & 0x00000008) == 0x00000008);
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
                bitField0_ |= 0x00000008;
                pages_ = value;
                onChanged();
                return this;
            }

            public Builder clearPages() {
                bitField0_ = (bitField0_ & ~0x00000008);
                pages_ = getDefaultInstance().getPages();
                onChanged();
                return this;
            }

            void setPages(com.google.protobuf.ByteString value) {
                bitField0_ |= 0x00000008;
                pages_ = value;
                onChanged();
            }

            // optional string source = 5;
            private java.lang.Object source_ = "";

            public boolean hasSource() {
                return ((bitField0_ & 0x00000010) == 0x00000010);
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
                bitField0_ |= 0x00000010;
                source_ = value;
                onChanged();
                return this;
            }

            public Builder clearSource() {
                bitField0_ = (bitField0_ & ~0x00000010);
                source_ = getDefaultInstance().getSource();
                onChanged();
                return this;
            }

            void setSource(com.google.protobuf.ByteString value) {
                bitField0_ |= 0x00000010;
                source_ = value;
                onChanged();
            }

            // optional string title = 6;
            private java.lang.Object title_ = "";

            public boolean hasTitle() {
                return ((bitField0_ & 0x00000020) == 0x00000020);
            }

            public String getTitle() {
                java.lang.Object ref = title_;
                if (!(ref instanceof String)) {
                    String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
                    title_ = s;
                    return s;
                } else {
                    return (String) ref;
                }
            }

            public Builder setTitle(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                bitField0_ |= 0x00000020;
                title_ = value;
                onChanged();
                return this;
            }

            public Builder clearTitle() {
                bitField0_ = (bitField0_ & ~0x00000020);
                title_ = getDefaultInstance().getTitle();
                onChanged();
                return this;
            }

            void setTitle(com.google.protobuf.ByteString value) {
                bitField0_ |= 0x00000020;
                title_ = value;
                onChanged();
            }

            // repeated .pl.edu.icm.coansys.citations.KeyValue auxiliary = 7;
            private java.util.List<pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue> auxiliary_ =
                    java.util.Collections.emptyList();

            private void ensureAuxiliaryIsMutable() {
                if (!((bitField0_ & 0x00000040) == 0x00000040)) {
                    auxiliary_ = new java.util.ArrayList<pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue>(auxiliary_);
                    bitField0_ |= 0x00000040;
                }
            }

            private com.google.protobuf.RepeatedFieldBuilder<
                    pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue, pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue.Builder, pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValueOrBuilder> auxiliaryBuilder_;

            public java.util.List<pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue> getAuxiliaryList() {
                if (auxiliaryBuilder_ == null) {
                    return java.util.Collections.unmodifiableList(auxiliary_);
                } else {
                    return auxiliaryBuilder_.getMessageList();
                }
            }

            public int getAuxiliaryCount() {
                if (auxiliaryBuilder_ == null) {
                    return auxiliary_.size();
                } else {
                    return auxiliaryBuilder_.getCount();
                }
            }

            public pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue getAuxiliary(int index) {
                if (auxiliaryBuilder_ == null) {
                    return auxiliary_.get(index);
                } else {
                    return auxiliaryBuilder_.getMessage(index);
                }
            }

            public Builder setAuxiliary(
                    int index, pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue value) {
                if (auxiliaryBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureAuxiliaryIsMutable();
                    auxiliary_.set(index, value);
                    onChanged();
                } else {
                    auxiliaryBuilder_.setMessage(index, value);
                }
                return this;
            }

            public Builder setAuxiliary(
                    int index, pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue.Builder builderForValue) {
                if (auxiliaryBuilder_ == null) {
                    ensureAuxiliaryIsMutable();
                    auxiliary_.set(index, builderForValue.build());
                    onChanged();
                } else {
                    auxiliaryBuilder_.setMessage(index, builderForValue.build());
                }
                return this;
            }

            public Builder addAuxiliary(pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue value) {
                if (auxiliaryBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureAuxiliaryIsMutable();
                    auxiliary_.add(value);
                    onChanged();
                } else {
                    auxiliaryBuilder_.addMessage(value);
                }
                return this;
            }

            public Builder addAuxiliary(
                    int index, pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue value) {
                if (auxiliaryBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureAuxiliaryIsMutable();
                    auxiliary_.add(index, value);
                    onChanged();
                } else {
                    auxiliaryBuilder_.addMessage(index, value);
                }
                return this;
            }

            public Builder addAuxiliary(
                    pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue.Builder builderForValue) {
                if (auxiliaryBuilder_ == null) {
                    ensureAuxiliaryIsMutable();
                    auxiliary_.add(builderForValue.build());
                    onChanged();
                } else {
                    auxiliaryBuilder_.addMessage(builderForValue.build());
                }
                return this;
            }

            public Builder addAuxiliary(
                    int index, pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue.Builder builderForValue) {
                if (auxiliaryBuilder_ == null) {
                    ensureAuxiliaryIsMutable();
                    auxiliary_.add(index, builderForValue.build());
                    onChanged();
                } else {
                    auxiliaryBuilder_.addMessage(index, builderForValue.build());
                }
                return this;
            }

            public Builder addAllAuxiliary(
                    java.lang.Iterable<? extends pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue> values) {
                if (auxiliaryBuilder_ == null) {
                    ensureAuxiliaryIsMutable();
                    super.addAll(values, auxiliary_);
                    onChanged();
                } else {
                    auxiliaryBuilder_.addAllMessages(values);
                }
                return this;
            }

            public Builder clearAuxiliary() {
                if (auxiliaryBuilder_ == null) {
                    auxiliary_ = java.util.Collections.emptyList();
                    bitField0_ = (bitField0_ & ~0x00000040);
                    onChanged();
                } else {
                    auxiliaryBuilder_.clear();
                }
                return this;
            }

            public Builder removeAuxiliary(int index) {
                if (auxiliaryBuilder_ == null) {
                    ensureAuxiliaryIsMutable();
                    auxiliary_.remove(index);
                    onChanged();
                } else {
                    auxiliaryBuilder_.remove(index);
                }
                return this;
            }

            public pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue.Builder getAuxiliaryBuilder(
                    int index) {
                return getAuxiliaryFieldBuilder().getBuilder(index);
            }

            public pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValueOrBuilder getAuxiliaryOrBuilder(
                    int index) {
                if (auxiliaryBuilder_ == null) {
                    return auxiliary_.get(index);
                } else {
                    return auxiliaryBuilder_.getMessageOrBuilder(index);
                }
            }

            public java.util.List<? extends pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValueOrBuilder>
            getAuxiliaryOrBuilderList() {
                if (auxiliaryBuilder_ != null) {
                    return auxiliaryBuilder_.getMessageOrBuilderList();
                } else {
                    return java.util.Collections.unmodifiableList(auxiliary_);
                }
            }

            public pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue.Builder addAuxiliaryBuilder() {
                return getAuxiliaryFieldBuilder().addBuilder(
                        pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue.getDefaultInstance());
            }

            public pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue.Builder addAuxiliaryBuilder(
                    int index) {
                return getAuxiliaryFieldBuilder().addBuilder(
                        index, pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue.getDefaultInstance());
            }

            public java.util.List<pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue.Builder>
            getAuxiliaryBuilderList() {
                return getAuxiliaryFieldBuilder().getBuilderList();
            }

            private com.google.protobuf.RepeatedFieldBuilder<
                    pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue, pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue.Builder, pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValueOrBuilder>
            getAuxiliaryFieldBuilder() {
                if (auxiliaryBuilder_ == null) {
                    auxiliaryBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
                            pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue, pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue.Builder, pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValueOrBuilder>(
                            auxiliary_,
                            ((bitField0_ & 0x00000040) == 0x00000040),
                            getParentForChildren(),
                            isClean());
                    auxiliary_ = null;
                }
                return auxiliaryBuilder_;
            }

            // @@protoc_insertion_point(builder_scope:pl.edu.icm.coansys.citations.MatchableEntityData)
        }

        static {
            defaultInstance = new MatchableEntityData(true);
            defaultInstance.initFields();
        }

        // @@protoc_insertion_point(class_scope:pl.edu.icm.coansys.citations.MatchableEntityData)
    }

    public interface KeyValueOrBuilder
            extends com.google.protobuf.MessageOrBuilder {

        // optional string key = 1;
        boolean hasKey();

        String getKey();

        // optional string value = 2;
        boolean hasValue();

        String getValue();
    }

    public static final class KeyValue extends
            com.google.protobuf.GeneratedMessage
            implements KeyValueOrBuilder {
        // Use KeyValue.newBuilder() to construct.
        private KeyValue(Builder builder) {
            super(builder);
        }

        private KeyValue(boolean noInit) {
        }

        private static final KeyValue defaultInstance;

        public static KeyValue getDefaultInstance() {
            return defaultInstance;
        }

        public KeyValue getDefaultInstanceForType() {
            return defaultInstance;
        }

        public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
            return pl.edu.icm.coansys.citations.data.CitationMatchingProtos.internal_static_pl_edu_icm_coansys_citations_KeyValue_descriptor;
        }

        protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
            return pl.edu.icm.coansys.citations.data.CitationMatchingProtos.internal_static_pl_edu_icm_coansys_citations_KeyValue_fieldAccessorTable;
        }

        private int bitField0_;
        // optional string key = 1;
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
            key_ = "";
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
                output.writeBytes(1, getKeyBytes());
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
                        .computeBytesSize(1, getKeyBytes());
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

        public static pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue parseFrom(
                com.google.protobuf.ByteString data)
                throws com.google.protobuf.InvalidProtocolBufferException {
            return newBuilder().mergeFrom(data).buildParsed();
        }

        public static pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue parseFrom(
                com.google.protobuf.ByteString data,
                com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                throws com.google.protobuf.InvalidProtocolBufferException {
            return newBuilder().mergeFrom(data, extensionRegistry)
                    .buildParsed();
        }

        public static pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue parseFrom(byte[] data)
                throws com.google.protobuf.InvalidProtocolBufferException {
            return newBuilder().mergeFrom(data).buildParsed();
        }

        public static pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue parseFrom(
                byte[] data,
                com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                throws com.google.protobuf.InvalidProtocolBufferException {
            return newBuilder().mergeFrom(data, extensionRegistry)
                    .buildParsed();
        }

        public static pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue parseFrom(java.io.InputStream input)
                throws java.io.IOException {
            return newBuilder().mergeFrom(input).buildParsed();
        }

        public static pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue parseFrom(
                java.io.InputStream input,
                com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                throws java.io.IOException {
            return newBuilder().mergeFrom(input, extensionRegistry)
                    .buildParsed();
        }

        public static pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue parseDelimitedFrom(java.io.InputStream input)
                throws java.io.IOException {
            Builder builder = newBuilder();
            if (builder.mergeDelimitedFrom(input)) {
                return builder.buildParsed();
            } else {
                return null;
            }
        }

        public static pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue parseDelimitedFrom(
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

        public static pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue parseFrom(
                com.google.protobuf.CodedInputStream input)
                throws java.io.IOException {
            return newBuilder().mergeFrom(input).buildParsed();
        }

        public static pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue parseFrom(
                com.google.protobuf.CodedInputStream input,
                com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                throws java.io.IOException {
            return newBuilder().mergeFrom(input, extensionRegistry)
                    .buildParsed();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue prototype) {
            return newBuilder().mergeFrom(prototype);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        @java.lang.Override
        protected Builder newBuilderForType(
                com.google.protobuf.GeneratedMessage.BuilderParent parent) {
            Builder builder = new Builder(parent);
            return builder;
        }

        public static final class Builder extends
                com.google.protobuf.GeneratedMessage.Builder<Builder>
                implements pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValueOrBuilder {
            public static final com.google.protobuf.Descriptors.Descriptor
            getDescriptor() {
                return pl.edu.icm.coansys.citations.data.CitationMatchingProtos.internal_static_pl_edu_icm_coansys_citations_KeyValue_descriptor;
            }

            protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
            internalGetFieldAccessorTable() {
                return pl.edu.icm.coansys.citations.data.CitationMatchingProtos.internal_static_pl_edu_icm_coansys_citations_KeyValue_fieldAccessorTable;
            }

            // Construct using pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue.newBuilder()
            private Builder() {
                maybeForceBuilderInitialization();
            }

            private Builder(com.google.protobuf.GeneratedMessage.BuilderParent parent) {
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
                key_ = "";
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
                return pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue.getDescriptor();
            }

            public pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue getDefaultInstanceForType() {
                return pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue.getDefaultInstance();
            }

            public pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue build() {
                pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException(result);
                }
                return result;
            }

            private pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue buildParsed()
                    throws com.google.protobuf.InvalidProtocolBufferException {
                pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException(
                            result).asInvalidProtocolBufferException();
                }
                return result;
            }

            public pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue buildPartial() {
                pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue result = new pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue(this);
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
                result.bitField0_ = to_bitField0_;
                onBuilt();
                return result;
            }

            public Builder mergeFrom(com.google.protobuf.Message other) {
                if (other instanceof pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue) {
                    return mergeFrom((pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue) other);
                } else {
                    super.mergeFrom(other);
                    return this;
                }
            }

            public Builder mergeFrom(pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue other) {
                if (other == pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue.getDefaultInstance())
                    return this;
                if (other.hasKey()) {
                    setKey(other.getKey());
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
                            key_ = input.readBytes();
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

            // optional string key = 1;
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

            // @@protoc_insertion_point(builder_scope:pl.edu.icm.coansys.citations.KeyValue)
        }

        static {
            defaultInstance = new KeyValue(true);
            defaultInstance.initFields();
        }

        // @@protoc_insertion_point(class_scope:pl.edu.icm.coansys.citations.KeyValue)
    }

    private static com.google.protobuf.Descriptors.Descriptor
            internal_static_pl_edu_icm_coansys_citations_MatchableEntityData_descriptor;
    private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
            internal_static_pl_edu_icm_coansys_citations_MatchableEntityData_fieldAccessorTable;
    private static com.google.protobuf.Descriptors.Descriptor
            internal_static_pl_edu_icm_coansys_citations_KeyValue_descriptor;
    private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
            internal_static_pl_edu_icm_coansys_citations_KeyValue_fieldAccessorTable;

    public static com.google.protobuf.Descriptors.FileDescriptor
    getDescriptor() {
        return descriptor;
    }

    private static com.google.protobuf.Descriptors.FileDescriptor
            descriptor;

    static {
        java.lang.String[] descriptorData = {
                "\n\027citation_matching.proto\022\034pl.edu.icm.co" +
                        "ansys.citations\"\250\001\n\023MatchableEntityData\022" +
                        "\n\n\002id\030\001 \002(\t\022\016\n\006author\030\002 \001(\t\022\014\n\004year\030\003 \001(" +
                        "\t\022\r\n\005pages\030\004 \001(\t\022\016\n\006source\030\005 \001(\t\022\r\n\005titl" +
                        "e\030\006 \001(\t\0229\n\tauxiliary\030\007 \003(\0132&.pl.edu.icm." +
                        "coansys.citations.KeyValue\"&\n\010KeyValue\022\013" +
                        "\n\003key\030\001 \001(\t\022\r\n\005value\030\002 \001(\tB;\n!pl.edu.icm" +
                        ".coansys.citations.dataB\026CitationMatchin" +
                        "gProtos"
        };
        com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
                new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
                    public com.google.protobuf.ExtensionRegistry assignDescriptors(
                            com.google.protobuf.Descriptors.FileDescriptor root) {
                        descriptor = root;
                        internal_static_pl_edu_icm_coansys_citations_MatchableEntityData_descriptor =
                                getDescriptor().getMessageTypes().get(0);
                        internal_static_pl_edu_icm_coansys_citations_MatchableEntityData_fieldAccessorTable = new
                                com.google.protobuf.GeneratedMessage.FieldAccessorTable(
                                internal_static_pl_edu_icm_coansys_citations_MatchableEntityData_descriptor,
                                new java.lang.String[]{"Id", "Author", "Year", "Pages", "Source", "Title", "Auxiliary",},
                                pl.edu.icm.coansys.citations.data.CitationMatchingProtos.MatchableEntityData.class,
                                pl.edu.icm.coansys.citations.data.CitationMatchingProtos.MatchableEntityData.Builder.class);
                        internal_static_pl_edu_icm_coansys_citations_KeyValue_descriptor =
                                getDescriptor().getMessageTypes().get(1);
                        internal_static_pl_edu_icm_coansys_citations_KeyValue_fieldAccessorTable = new
                                com.google.protobuf.GeneratedMessage.FieldAccessorTable(
                                internal_static_pl_edu_icm_coansys_citations_KeyValue_descriptor,
                                new java.lang.String[]{"Key", "Value",},
                                pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue.class,
                                pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue.Builder.class);
                        return null;
                    }
                };
        com.google.protobuf.Descriptors.FileDescriptor
                .internalBuildGeneratedFileFrom(descriptorData,
                        new com.google.protobuf.Descriptors.FileDescriptor[]{
                        }, assigner);
    }

    // @@protoc_insertion_point(outer_class_scope)
}
