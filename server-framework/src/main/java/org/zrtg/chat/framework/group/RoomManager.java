package org.zrtg.chat.framework.group;

import org.kurento.client.KurentoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.zrtg.chat.common.model.Room;
import org.zrtg.chat.common.model.Session;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author wangq
 * @create_at 2021-4-16 19:32
 */
public class RoomManager
{

    private final Logger log = LoggerFactory.getLogger(RoomManager.class);

    @Autowired
    private KurentoClient kurento;

    private final ConcurrentMap<String, Room> rooms = new ConcurrentHashMap<>();

    private final ConcurrentMap<String, String> bindRoom = new ConcurrentHashMap<>();


    public void bindRoom(Session participant,String roomId){
        bindRoom.put(participant.getAccount(),roomId);
    }

    public String getRoomIdBySession(Session participant){
        String roomId = "";
        if(bindRoom.containsKey(participant.getAccount())){
            roomId = bindRoom.get(participant.getAccount());
        }
        return roomId;
    }

    /**
     * Looks for a room in the active room list.
     *
     * @param roomName
     *          the name of the room
     * @return the room if it was already created, or a new one if it is the first time this room is
     *         accessed
     */
    public Room getRoom(String roomName) {
        log.info("Searching for room {}", roomName);
        Room room = rooms.get(roomName);

        if (room == null) {
            log.info("Room {} not existent. Will create now!", roomName);
            room = new Room(roomName, kurento.createMediaPipeline());
            rooms.put(roomName, room);
        }
        log.info("Room {} found!", roomName);
        return room;
    }

    /**
     * Removes a room from the list of available rooms.
     *
     * @param room
     *          the room to be removed
     */
    public void removeRoom(Room room) throws IOException
    {
        this.rooms.remove(room.getName());
        room.close();
        log.info("Room {} removed and closed", room.getName());
    }
}
