package org.zrtg.chat.server.handler;

import com.alibaba.fastjson.JSON;
import com.google.gson.JsonObject;
import com.google.protobuf.InvalidProtocolBufferException;
import lombok.extern.slf4j.Slf4j;
import org.kurento.client.IceCandidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zrtg.chat.common.constant.Constants;
import org.zrtg.chat.common.model.MessageWrapper;
import org.zrtg.chat.common.model.Room;
import org.zrtg.chat.common.model.Session;
import org.zrtg.chat.common.model.proto.MessageBodyProto;
import org.zrtg.chat.common.model.proto.MessageCandidateProto;
import org.zrtg.chat.common.model.proto.MessageProto;
import org.zrtg.chat.common.model.proto.MessageRoomProto;
import org.zrtg.chat.framework.group.RoomManager;
import org.zrtg.chat.framework.group.UserRegistry;
import org.zrtg.chat.framework.session.impl.SessionManagerImpl;

import java.io.IOException;

/**
 * @author wangq
 * @create_at 2021-4-17 15:10
 */
@Slf4j
public class GroupVideoChatHandler
{
    @Autowired
    private RoomManager roomManager;

    @Autowired
    private UserRegistry registry;

    @Autowired
    private SessionManagerImpl sessionManager;


    public void  handleGroupChatMessage(String sessionId, MessageWrapper wrapper){
        try{
            //当前连接用户session
            Session session =  sessionManager.getSession(sessionId);

            MessageProto.Model  msg =  (MessageProto.Model)wrapper.getBody();
            switch (msg.getCmd()){
                case  Constants.CmdType.JOINROOM://加入直播间
                    log.info("加入直播间 session:{}", sessionId);
                    joinRoom(msg, session);
                    break;
                case Constants.CmdType.RECEIVEVIDEOFROM://收到视频消息
                    log.info("收到视频消息 session:{}", sessionId);
                    dealReciveVideo(msg,session);
                    break;
                case Constants.CmdType.LEAVEROOM://离开直播间
                    log.info("离开直播间 session:{}", sessionId);
                    leaveRoom(session);
                    break;
                case Constants.CmdType.ONICECANDIDATE:// 媒体协商消息
                    log.info("媒体协商消息 session:{}", sessionId);
                    dealOnIceCandidate(msg,session);
                    break;
                default:

            }
        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getCause());
        }
    }

    public void leaveRoom(String sessionId) throws IOException
    {
        Session session =  sessionManager.getSession(sessionId);
        if (session !=null){
            registry.removeBySession(sessionId);
            leaveRoom(session);
        }
    }


    private void dealOnIceCandidate(MessageProto.Model  msg, Session session) throws InvalidProtocolBufferException
    {
        MessageCandidateProto.MessageCandidate messageCandidate = MessageCandidateProto.MessageCandidate.parseFrom(msg.getContent());
        if (messageCandidate != null){
            IceCandidate cand = new IceCandidate(messageCandidate.getCandidate(),
                    messageCandidate.getSdpMid(),messageCandidate.getSdpMLineIndex());

            session.addCandidate(cand,session.getAccount());
        }
    }

    /**
     * 处理接收视频
     * @param msg
     * @param session
     * @throws IOException
     */
   private void dealReciveVideo(MessageProto.Model  msg, Session session) throws IOException
   {
       MessageRoomProto.MessageRoom  msgConten =   MessageRoomProto.MessageRoom.parseFrom(msg.getContent());
       if (msgConten !=null){
           String senderSessionid = msg.getSender();
           if (!"".equals(senderSessionid)){
               final Session sender = registry.getBySessionId(senderSessionid);
               final String sdpOffer = msgConten.getExtend();
               if (sender ==null){
                   log.error("处理视频消息失败：sender 未找到");
               }else {
                   log.info("开始处理视频消息 session:{} receiveVideoFrom ：{}", session.getAccount(),senderSessionid);
                   session.receiveVideoFrom(sender, sdpOffer);
               }

           }else {
               log.error("处理视频消息失败：senderSessionid 为空");
           }
       }else{
           log.error("处理视频消息失败：sdpOffer 为空");
       }
   }

    private void joinRoom(MessageProto.Model  msg, Session session) throws IOException
    {
        MessageRoomProto.MessageRoom  msgConten =   MessageRoomProto.MessageRoom.parseFrom(msg.getContent());
        if (msgConten !=null){
            String roomId = msgConten.getRoomid();
            if (!"".equals(roomId)){
                log.info("PARTICIPANT {}: trying to join room {}", session.getAccount(), roomId);

                Room room = roomManager.getRoom(roomId);
                roomManager.bindRoom(session,roomId);
                final Session user = room.join(session);
                registry.register(user);
            }
        }
    }

    private void leaveRoom(Session user) throws IOException {

        String roomId = roomManager.getRoomIdBySession(user);
        if ("".equals(roomId)){
            log.info("未找到房间信息");
            return;
        }
        Room room = roomManager.getRoom(roomId);
        room.leave(user);
        if (room.getParticipants().isEmpty()) {
            roomManager.removeRoom(room);
        }
    }

}
