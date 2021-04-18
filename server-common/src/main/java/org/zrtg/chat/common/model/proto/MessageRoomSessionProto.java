// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: proto/java/MessageRoomSession.proto

package org.zrtg.chat.common.model.proto;

public final class MessageRoomSessionProto {
  private MessageRoomSessionProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface MessageRoomSessionOrBuilder extends
      // @@protoc_insertion_point(interface_extends:org.zrtg.chat.common.model.proto.MessageRoomSession)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <pre>
     *直播间已存在的session
     * </pre>
     *
     * <code>repeated string sessions = 1;</code>
     */
    java.util.List<String>
        getSessionsList();
    /**
     * <pre>
     *直播间已存在的session
     * </pre>
     *
     * <code>repeated string sessions = 1;</code>
     */
    int getSessionsCount();
    /**
     * <pre>
     *直播间已存在的session
     * </pre>
     *
     * <code>repeated string sessions = 1;</code>
     */
    String getSessions(int index);
    /**
     * <pre>
     *直播间已存在的session
     * </pre>
     *
     * <code>repeated string sessions = 1;</code>
     */
    com.google.protobuf.ByteString
        getSessionsBytes(int index);
  }
  /**
   * Protobuf type {@code org.zrtg.chat.common.model.proto.MessageRoomSession}
   */
  public  static final class MessageRoomSession extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:org.zrtg.chat.common.model.proto.MessageRoomSession)
      MessageRoomSessionOrBuilder {
  private static final long serialVersionUID = 0L;
    // Use MessageRoomSession.newBuilder() to construct.
    private MessageRoomSession(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private MessageRoomSession() {
      sessions_ = com.google.protobuf.LazyStringArrayList.EMPTY;
    }

    @Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return this.unknownFields;
    }
    private MessageRoomSession(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      this();
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!parseUnknownFieldProto3(
                  input, unknownFields, extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 10: {
              String s = input.readStringRequireUtf8();
              if (!((mutable_bitField0_ & 0x00000001) == 0x00000001)) {
                sessions_ = new com.google.protobuf.LazyStringArrayList();
                mutable_bitField0_ |= 0x00000001;
              }
              sessions_.add(s);
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e).setUnfinishedMessage(this);
      } finally {
        if (((mutable_bitField0_ & 0x00000001) == 0x00000001)) {
          sessions_ = sessions_.getUnmodifiableView();
        }
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return MessageRoomSessionProto.internal_static_org_zrtg_chat_common_model_proto_MessageRoomSession_descriptor;
    }

    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return MessageRoomSessionProto.internal_static_org_zrtg_chat_common_model_proto_MessageRoomSession_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              MessageRoomSession.class, Builder.class);
    }

    public static final int SESSIONS_FIELD_NUMBER = 1;
    private com.google.protobuf.LazyStringList sessions_;
    /**
     * <pre>
     *直播间已存在的session
     * </pre>
     *
     * <code>repeated string sessions = 1;</code>
     */
    public com.google.protobuf.ProtocolStringList
        getSessionsList() {
      return sessions_;
    }
    /**
     * <pre>
     *直播间已存在的session
     * </pre>
     *
     * <code>repeated string sessions = 1;</code>
     */
    public int getSessionsCount() {
      return sessions_.size();
    }
    /**
     * <pre>
     *直播间已存在的session
     * </pre>
     *
     * <code>repeated string sessions = 1;</code>
     */
    public String getSessions(int index) {
      return sessions_.get(index);
    }
    /**
     * <pre>
     *直播间已存在的session
     * </pre>
     *
     * <code>repeated string sessions = 1;</code>
     */
    public com.google.protobuf.ByteString
        getSessionsBytes(int index) {
      return sessions_.getByteString(index);
    }

    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      for (int i = 0; i < sessions_.size(); i++) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 1, sessions_.getRaw(i));
      }
      unknownFields.writeTo(output);
    }

    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      {
        int dataSize = 0;
        for (int i = 0; i < sessions_.size(); i++) {
          dataSize += computeStringSizeNoTag(sessions_.getRaw(i));
        }
        size += dataSize;
        size += 1 * getSessionsList().size();
      }
      size += unknownFields.getSerializedSize();
      memoizedSize = size;
      return size;
    }

    @Override
    public boolean equals(final Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof MessageRoomSession)) {
        return super.equals(obj);
      }
      MessageRoomSession other = (MessageRoomSession) obj;

      boolean result = true;
      result = result && getSessionsList()
          .equals(other.getSessionsList());
      result = result && unknownFields.equals(other.unknownFields);
      return result;
    }

    @Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      if (getSessionsCount() > 0) {
        hash = (37 * hash) + SESSIONS_FIELD_NUMBER;
        hash = (53 * hash) + getSessionsList().hashCode();
      }
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static MessageRoomSession parseFrom(
        java.nio.ByteBuffer data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static MessageRoomSession parseFrom(
        java.nio.ByteBuffer data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static MessageRoomSession parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static MessageRoomSession parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static MessageRoomSession parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static MessageRoomSession parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static MessageRoomSession parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static MessageRoomSession parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static MessageRoomSession parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static MessageRoomSession parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static MessageRoomSession parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static MessageRoomSession parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(MessageRoomSession prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @Override
    protected Builder newBuilderForType(
        BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code org.zrtg.chat.common.model.proto.MessageRoomSession}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:org.zrtg.chat.common.model.proto.MessageRoomSession)
        MessageRoomSessionOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return MessageRoomSessionProto.internal_static_org_zrtg_chat_common_model_proto_MessageRoomSession_descriptor;
      }

      protected FieldAccessorTable
          internalGetFieldAccessorTable() {
        return MessageRoomSessionProto.internal_static_org_zrtg_chat_common_model_proto_MessageRoomSession_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                MessageRoomSession.class, Builder.class);
      }

      // Construct using org.zrtg.chat.common.model.proto.MessageRoomSessionProto.MessageRoomSession.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessageV3
                .alwaysUseFieldBuilders) {
        }
      }
      public Builder clear() {
        super.clear();
        sessions_ = com.google.protobuf.LazyStringArrayList.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000001);
        return this;
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return MessageRoomSessionProto.internal_static_org_zrtg_chat_common_model_proto_MessageRoomSession_descriptor;
      }

      public MessageRoomSession getDefaultInstanceForType() {
        return MessageRoomSession.getDefaultInstance();
      }

      public MessageRoomSession build() {
        MessageRoomSession result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public MessageRoomSession buildPartial() {
        MessageRoomSession result = new MessageRoomSession(this);
        int from_bitField0_ = bitField0_;
        if (((bitField0_ & 0x00000001) == 0x00000001)) {
          sessions_ = sessions_.getUnmodifiableView();
          bitField0_ = (bitField0_ & ~0x00000001);
        }
        result.sessions_ = sessions_;
        onBuilt();
        return result;
      }

      public Builder clone() {
        return (Builder) super.clone();
      }
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return (Builder) super.setField(field, value);
      }
      public Builder clearField(
          com.google.protobuf.Descriptors.FieldDescriptor field) {
        return (Builder) super.clearField(field);
      }
      public Builder clearOneof(
          com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return (Builder) super.clearOneof(oneof);
      }
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index, Object value) {
        return (Builder) super.setRepeatedField(field, index, value);
      }
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return (Builder) super.addRepeatedField(field, value);
      }
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof MessageRoomSession) {
          return mergeFrom((MessageRoomSession)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(MessageRoomSession other) {
        if (other == MessageRoomSession.getDefaultInstance()) return this;
        if (!other.sessions_.isEmpty()) {
          if (sessions_.isEmpty()) {
            sessions_ = other.sessions_;
            bitField0_ = (bitField0_ & ~0x00000001);
          } else {
            ensureSessionsIsMutable();
            sessions_.addAll(other.sessions_);
          }
          onChanged();
        }
        this.mergeUnknownFields(other.unknownFields);
        onChanged();
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        MessageRoomSession parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (MessageRoomSession) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private com.google.protobuf.LazyStringList sessions_ = com.google.protobuf.LazyStringArrayList.EMPTY;
      private void ensureSessionsIsMutable() {
        if (!((bitField0_ & 0x00000001) == 0x00000001)) {
          sessions_ = new com.google.protobuf.LazyStringArrayList(sessions_);
          bitField0_ |= 0x00000001;
         }
      }
      /**
       * <pre>
       *直播间已存在的session
       * </pre>
       *
       * <code>repeated string sessions = 1;</code>
       */
      public com.google.protobuf.ProtocolStringList
          getSessionsList() {
        return sessions_.getUnmodifiableView();
      }
      /**
       * <pre>
       *直播间已存在的session
       * </pre>
       *
       * <code>repeated string sessions = 1;</code>
       */
      public int getSessionsCount() {
        return sessions_.size();
      }
      /**
       * <pre>
       *直播间已存在的session
       * </pre>
       *
       * <code>repeated string sessions = 1;</code>
       */
      public String getSessions(int index) {
        return sessions_.get(index);
      }
      /**
       * <pre>
       *直播间已存在的session
       * </pre>
       *
       * <code>repeated string sessions = 1;</code>
       */
      public com.google.protobuf.ByteString
          getSessionsBytes(int index) {
        return sessions_.getByteString(index);
      }
      /**
       * <pre>
       *直播间已存在的session
       * </pre>
       *
       * <code>repeated string sessions = 1;</code>
       */
      public Builder setSessions(
          int index, String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  ensureSessionsIsMutable();
        sessions_.set(index, value);
        onChanged();
        return this;
      }
      /**
       * <pre>
       *直播间已存在的session
       * </pre>
       *
       * <code>repeated string sessions = 1;</code>
       */
      public Builder addSessions(
          String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  ensureSessionsIsMutable();
        sessions_.add(value);
        onChanged();
        return this;
      }
      /**
       * <pre>
       *直播间已存在的session
       * </pre>
       *
       * <code>repeated string sessions = 1;</code>
       */
      public Builder addAllSessions(
          Iterable<String> values) {
        ensureSessionsIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(
            values, sessions_);
        onChanged();
        return this;
      }
      /**
       * <pre>
       *直播间已存在的session
       * </pre>
       *
       * <code>repeated string sessions = 1;</code>
       */
      public Builder clearSessions() {
        sessions_ = com.google.protobuf.LazyStringArrayList.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000001);
        onChanged();
        return this;
      }
      /**
       * <pre>
       *直播间已存在的session
       * </pre>
       *
       * <code>repeated string sessions = 1;</code>
       */
      public Builder addSessionsBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
        ensureSessionsIsMutable();
        sessions_.add(value);
        onChanged();
        return this;
      }
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.setUnknownFieldsProto3(unknownFields);
      }

      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.mergeUnknownFields(unknownFields);
      }


      // @@protoc_insertion_point(builder_scope:org.zrtg.chat.common.model.proto.MessageRoomSession)
    }

    // @@protoc_insertion_point(class_scope:org.zrtg.chat.common.model.proto.MessageRoomSession)
    private static final MessageRoomSession DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new MessageRoomSession();
    }

    public static MessageRoomSession getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<MessageRoomSession>
        PARSER = new com.google.protobuf.AbstractParser<MessageRoomSession>() {
      public MessageRoomSession parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
          return new MessageRoomSession(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<MessageRoomSession> parser() {
      return PARSER;
    }

    @Override
    public com.google.protobuf.Parser<MessageRoomSession> getParserForType() {
      return PARSER;
    }

    public MessageRoomSession getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_org_zrtg_chat_common_model_proto_MessageRoomSession_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_org_zrtg_chat_common_model_proto_MessageRoomSession_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n#proto/java/MessageRoomSession.proto\022 o" +
      "rg.zrtg.chat.common.model.proto\"&\n\022Messa" +
      "geRoomSession\022\020\n\010sessions\030\001 \003(\tB\031B\027Messa" +
      "geRoomSessionProtob\006proto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
    internal_static_org_zrtg_chat_common_model_proto_MessageRoomSession_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_org_zrtg_chat_common_model_proto_MessageRoomSession_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_org_zrtg_chat_common_model_proto_MessageRoomSession_descriptor,
        new String[] { "Sessions", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}