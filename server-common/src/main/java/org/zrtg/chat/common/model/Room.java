package org.zrtg.chat.common.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.kurento.client.Continuation;
import org.kurento.client.MediaPipeline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zrtg.chat.common.constant.Constants;
import org.zrtg.chat.common.model.proto.*;

import javax.annotation.PreDestroy;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author wangq
 * @create_at 2021-4-16 19:33
 */
public class Room implements Closeable
{
    private final Logger log = LoggerFactory.getLogger(Room.class);

    private final ConcurrentMap<String, Session> participants = new ConcurrentHashMap<>();



    private final MediaPipeline pipeline;
    private final String name;

    public String getName() {
        return name;
    }

    public Room(String roomName, MediaPipeline pipeline) {
        this.name = roomName;
        this.pipeline = pipeline;
        log.info("ROOM {} has been created", roomName);
    }

    @PreDestroy
    private void shutdown() throws IOException
    {
        this.close();
    }

    public Session join(Session participant) throws IOException {
        log.info("ROOM {}: adding participant {}", this.name, participant.getAccount());

        joinRoom(participant);
        participants.put(participant.getAccount(), participant);
        sendParticipantNames(participant);
        return participant;
    }


    public void sendParticipantNames(Session user) throws IOException {


        MessageRoomSessionProto.MessageRoomSession.Builder msg =  MessageRoomSessionProto.MessageRoomSession.newBuilder();

        int count = 0;
        for (final Session participant : this.getParticipants()) {
            if (!participant.getAccount().equals(user.getAccount())) {
                count++;
                msg.addSessions(participant.getAccount());
            }
        }


        log.info("PARTICIPANT {}: sending a list of {} participants", user.getAccount(), count);

        MessageProto.Model.Builder builder = MessageProto.Model.newBuilder();
        builder.setCmd(Constants.CmdType.EXISTINGPARTICIPANTS);
        builder.setSender(user.getAccount());
        builder.setMsgtype(Constants.ProtobufType.GROUP_CALL);
        builder.setToken(user.getAccount());
        builder.setContent(msg.build().toByteString());

        user.write(builder);
    }


    public void leave(Session user) throws IOException {
        log.debug("PARTICIPANT {}: Leaving room {}", user.getAccount(), this.name);
        this.removeParticipant(user.getAccount());
        user.close();
    }

    private Collection<String> joinRoom(Session newParticipant) throws IOException {

        newParticipant.setPipeline(this.pipeline);

        MessageProto.Model.Builder builder = MessageProto.Model.newBuilder();
        builder.setCmd(Constants.CmdType.NEWPARTICIPANTARRIVED);
        builder.setSender(newParticipant.getAccount());
        builder.setMsgtype(Constants.ProtobufType.GROUP_CALL);
        builder.setToken(newParticipant.getAccount());
        MessageRoomProto.MessageRoom.Builder msg =  MessageRoomProto.MessageRoom.newBuilder();
        msg.setExtend("newParticipantArrived");
        builder.setContent(msg.build().toByteString());


        final List<String> participantsList = new ArrayList<>(participants.values().size());
        log.debug("ROOM {}: notifying other participants of new participant {}", name,
                newParticipant.getAccount());

        for (final Session participant : participants.values()) {
            builder.setReceiver(participant.getAccount());
            participant.write(builder);
            participantsList.add(participant.getAccount());
        }


        return participantsList;
    }

    private void removeParticipant(String sessionId) throws IOException {
        participants.remove(sessionId);

        log.debug("ROOM {}: notifying all users that {} is leaving the room", this.name, sessionId);

        final List<String> unnotifiedParticipants = new ArrayList<>();

        MessageProto.Model.Builder builder = MessageProto.Model.newBuilder();
        builder.setCmd(Constants.CmdType.PARTICIPANTLEFT);
        builder.setSender(sessionId);
        builder.setMsgtype(Constants.ProtobufType.GROUP_CALL);
        builder.setToken(sessionId);
        MessageRoomProto.MessageRoom.Builder msg =  MessageRoomProto.MessageRoom.newBuilder();
        msg.setExtend("participantLeft");
        builder.setContent(msg.build().toByteString());

        for (final Session participant : participants.values()) {
            log.debug("notifying {} that {} is leaving the room",participant.getAccount(),sessionId);
            builder.setReceiver(participant.getAccount());

            participant.cancelVideoFrom(sessionId);
            participant.write(builder);
        }

        if (!unnotifiedParticipants.isEmpty()) {
            log.debug("ROOM {}: The users {} could not be notified that {} left the room", this.name,
                    unnotifiedParticipants, name);
        }

    }



    public Collection<Session> getParticipants() {
        return participants.values();
    }

    public Session getParticipant(String sessionId) {
        return participants.get(sessionId);
    }

    @Override
    public void close() throws IOException
    {
        for (final Session user : participants.values()) {
            user.close();
        }

        participants.clear();

        pipeline.release(new Continuation<Void>() {

            @Override
            public void onSuccess(Void result) throws Exception {
                log.debug("ROOM {}: Released Pipeline", Room.this.name);
            }

            @Override
            public void onError(Throwable cause) throws Exception {
                log.warn("PARTICIPANT {}: Could not release Pipeline", Room.this.name);
            }
        });

        log.debug("Room {} closed", this.name);
    }
}
