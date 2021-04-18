/**
 * @fileoverview
 * @enhanceable
 * @suppress {messageConventions} JS Compiler reports an error if a variable or
 *     field starts with 'MSG_' and isn't a translatable message.
 * @public
 */
// GENERATED CODE -- DO NOT EDIT!

var jspb = require('google-protobuf');
var goog = jspb;
var global = Function('return this')();

goog.exportSymbol('proto.MessageRoom', null, global);

/**
 * Generated by JsPbCodeGenerator.
 * @param {Array=} opt_data Optional initial data array, typically from a
 * server response, or constructed directly in Javascript. The array is used
 * in place and becomes part of the constructed object. It is not cloned.
 * If no data is provided, the constructed object will be empty, but still
 * valid.
 * @extends {jspb.Message}
 * @constructor
 */
proto.MessageRoom = function(opt_data) {
  jspb.Message.initialize(this, opt_data, 0, -1, null, null);
};
goog.inherits(proto.MessageRoom, jspb.Message);
if (goog.DEBUG && !COMPILED) {
  proto.MessageRoom.displayName = 'proto.MessageRoom';
}


if (jspb.Message.GENERATE_TO_OBJECT) {
/**
 * Creates an object representation of this proto suitable for use in Soy templates.
 * Field names that are reserved in JavaScript and will be renamed to pb_name.
 * To access a reserved field use, foo.pb_<name>, eg, foo.pb_default.
 * For the list of reserved names please see:
 *     com.google.apps.jspb.JsClassTemplate.JS_RESERVED_WORDS.
 * @param {boolean=} opt_includeInstance Whether to include the JSPB instance
 *     for transitional soy proto support: http://goto/soy-param-migration
 * @return {!Object}
 */
proto.MessageRoom.prototype.toObject = function(opt_includeInstance) {
  return proto.MessageRoom.toObject(opt_includeInstance, this);
};


/**
 * Static version of the {@see toObject} method.
 * @param {boolean|undefined} includeInstance Whether to include the JSPB
 *     instance for transitional soy proto support:
 *     http://goto/soy-param-migration
 * @param {!proto.MessageRoom} msg The msg instance to transform.
 * @return {!Object}
 * @suppress {unusedLocalVariables} f is only used for nested messages
 */
proto.MessageRoom.toObject = function(includeInstance, msg) {
  var f, obj = {
    title: jspb.Message.getFieldWithDefault(msg, 1, ""),
    roomid: jspb.Message.getFieldWithDefault(msg, 2, ""),
    time: jspb.Message.getFieldWithDefault(msg, 3, ""),
    type: jspb.Message.getFieldWithDefault(msg, 4, 0),
    extend: jspb.Message.getFieldWithDefault(msg, 5, ""),
    username: jspb.Message.getFieldWithDefault(msg, 6, ""),
    nickname: jspb.Message.getFieldWithDefault(msg, 7, ""),
    avatar: jspb.Message.getFieldWithDefault(msg, 8, "")
  };

  if (includeInstance) {
    obj.$jspbMessageInstance = msg;
  }
  return obj;
};
}


/**
 * Deserializes binary data (in protobuf wire format).
 * @param {jspb.ByteSource} bytes The bytes to deserialize.
 * @return {!proto.MessageRoom}
 */
proto.MessageRoom.deserializeBinary = function(bytes) {
  var reader = new jspb.BinaryReader(bytes);
  var msg = new proto.MessageRoom;
  return proto.MessageRoom.deserializeBinaryFromReader(msg, reader);
};


/**
 * Deserializes binary data (in protobuf wire format) from the
 * given reader into the given message object.
 * @param {!proto.MessageRoom} msg The message object to deserialize into.
 * @param {!jspb.BinaryReader} reader The BinaryReader to use.
 * @return {!proto.MessageRoom}
 */
proto.MessageRoom.deserializeBinaryFromReader = function(msg, reader) {
  while (reader.nextField()) {
    if (reader.isEndGroup()) {
      break;
    }
    var field = reader.getFieldNumber();
    switch (field) {
    case 1:
      var value = /** @type {string} */ (reader.readString());
      msg.setTitle(value);
      break;
    case 2:
      var value = /** @type {string} */ (reader.readString());
      msg.setRoomid(value);
      break;
    case 3:
      var value = /** @type {string} */ (reader.readString());
      msg.setTime(value);
      break;
    case 4:
      var value = /** @type {number} */ (reader.readUint32());
      msg.setType(value);
      break;
    case 5:
      var value = /** @type {string} */ (reader.readString());
      msg.setExtend(value);
      break;
    case 6:
      var value = /** @type {string} */ (reader.readString());
      msg.setUsername(value);
      break;
    case 7:
      var value = /** @type {string} */ (reader.readString());
      msg.setNickname(value);
      break;
    case 8:
      var value = /** @type {string} */ (reader.readString());
      msg.setAvatar(value);
      break;
    default:
      reader.skipField();
      break;
    }
  }
  return msg;
};


/**
 * Serializes the message to binary data (in protobuf wire format).
 * @return {!Uint8Array}
 */
proto.MessageRoom.prototype.serializeBinary = function() {
  var writer = new jspb.BinaryWriter();
  proto.MessageRoom.serializeBinaryToWriter(this, writer);
  return writer.getResultBuffer();
};


/**
 * Serializes the given message to binary data (in protobuf wire
 * format), writing to the given BinaryWriter.
 * @param {!proto.MessageRoom} message
 * @param {!jspb.BinaryWriter} writer
 * @suppress {unusedLocalVariables} f is only used for nested messages
 */
proto.MessageRoom.serializeBinaryToWriter = function(message, writer) {
  var f = undefined;
  f = message.getTitle();
  if (f.length > 0) {
    writer.writeString(
      1,
      f
    );
  }
  f = message.getRoomid();
  if (f.length > 0) {
    writer.writeString(
      2,
      f
    );
  }
  f = message.getTime();
  if (f.length > 0) {
    writer.writeString(
      3,
      f
    );
  }
  f = message.getType();
  if (f !== 0) {
    writer.writeUint32(
      4,
      f
    );
  }
  f = message.getExtend();
  if (f.length > 0) {
    writer.writeString(
      5,
      f
    );
  }
  f = message.getUsername();
  if (f.length > 0) {
    writer.writeString(
      6,
      f
    );
  }
  f = message.getNickname();
  if (f.length > 0) {
    writer.writeString(
      7,
      f
    );
  }
  f = message.getAvatar();
  if (f.length > 0) {
    writer.writeString(
      8,
      f
    );
  }
};


/**
 * optional string title = 1;
 * @return {string}
 */
proto.MessageRoom.prototype.getTitle = function() {
  return /** @type {string} */ (jspb.Message.getFieldWithDefault(this, 1, ""));
};


/** @param {string} value */
proto.MessageRoom.prototype.setTitle = function(value) {
  jspb.Message.setField(this, 1, value);
};


/**
 * optional string roomid = 2;
 * @return {string}
 */
proto.MessageRoom.prototype.getRoomid = function() {
  return /** @type {string} */ (jspb.Message.getFieldWithDefault(this, 2, ""));
};


/** @param {string} value */
proto.MessageRoom.prototype.setRoomid = function(value) {
  jspb.Message.setField(this, 2, value);
};


/**
 * optional string time = 3;
 * @return {string}
 */
proto.MessageRoom.prototype.getTime = function() {
  return /** @type {string} */ (jspb.Message.getFieldWithDefault(this, 3, ""));
};


/** @param {string} value */
proto.MessageRoom.prototype.setTime = function(value) {
  jspb.Message.setField(this, 3, value);
};


/**
 * optional uint32 type = 4;
 * @return {number}
 */
proto.MessageRoom.prototype.getType = function() {
  return /** @type {number} */ (jspb.Message.getFieldWithDefault(this, 4, 0));
};


/** @param {number} value */
proto.MessageRoom.prototype.setType = function(value) {
  jspb.Message.setField(this, 4, value);
};


/**
 * optional string extend = 5;
 * @return {string}
 */
proto.MessageRoom.prototype.getExtend = function() {
  return /** @type {string} */ (jspb.Message.getFieldWithDefault(this, 5, ""));
};


/** @param {string} value */
proto.MessageRoom.prototype.setExtend = function(value) {
  jspb.Message.setField(this, 5, value);
};


/**
 * optional string username = 6;
 * @return {string}
 */
proto.MessageRoom.prototype.getUsername = function() {
  return /** @type {string} */ (jspb.Message.getFieldWithDefault(this, 6, ""));
};


/** @param {string} value */
proto.MessageRoom.prototype.setUsername = function(value) {
  jspb.Message.setField(this, 6, value);
};


/**
 * optional string nickname = 7;
 * @return {string}
 */
proto.MessageRoom.prototype.getNickname = function() {
  return /** @type {string} */ (jspb.Message.getFieldWithDefault(this, 7, ""));
};


/** @param {string} value */
proto.MessageRoom.prototype.setNickname = function(value) {
  jspb.Message.setField(this, 7, value);
};


/**
 * optional string avatar = 8;
 * @return {string}
 */
proto.MessageRoom.prototype.getAvatar = function() {
  return /** @type {string} */ (jspb.Message.getFieldWithDefault(this, 8, ""));
};


/** @param {string} value */
proto.MessageRoom.prototype.setAvatar = function(value) {
  jspb.Message.setField(this, 8, value);
};


goog.object.extend(exports, proto);