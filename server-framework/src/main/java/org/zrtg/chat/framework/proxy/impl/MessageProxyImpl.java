
package org.zrtg.chat.framework.proxy.impl;


import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.zrtg.chat.common.constant.Constants;
import org.zrtg.chat.common.domain.UserMessageEntity;
import org.zrtg.chat.common.model.MessageWrapper;
import org.zrtg.chat.common.model.proto.MessageBodyProto;
import org.zrtg.chat.common.model.proto.MessageProto;
import org.zrtg.chat.common.model.proto.MessageRoomProto;
import org.zrtg.chat.common.rebot.proxy.RebotProxy;
import org.zrtg.chat.common.service.IUserMessageService;
import org.zrtg.chat.framework.proxy.MessageProxy;

import java.util.Date;

@Service
public class MessageProxyImpl implements MessageProxy
{
	private final static Logger log = LoggerFactory.getLogger(MessageProxyImpl.class);

	@Autowired
	private RebotProxy rebotProxy;
	@Autowired
	private IUserMessageService userMessageServiceImpl;

    @Override
	public MessageWrapper convertToMessageWrapper(String sessionId , MessageProto.Model message) {
    	
        
        switch (message.getCmd()) {
			case Constants.CmdType.BIND:
				 try {
		            	return new MessageWrapper(MessageWrapper.MessageProtocol.CONNECT, message.getSender(), null,message);
		            } catch (Exception e) {
		                e.printStackTrace();
		            }
				break;
			case Constants.CmdType.HEARTBEAT:
				try {
	                 return new MessageWrapper(MessageWrapper.MessageProtocol.HEART_BEAT, sessionId,null, null);
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
				break;
			case Constants.CmdType.ONLINE:
				
				break;
			case Constants.CmdType.OFFLINE:
				
				break;
			case Constants.CmdType.MESSAGE:
					try {
						  MessageProto.Model.Builder  result = MessageProto.Model.newBuilder(message);
						  result.setTimeStamp(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
						  result.setSender(sessionId);//???????????????sessionId
						  message =  MessageProto.Model.parseFrom(result.build().toByteArray());
						  //??????????????????????????????
						  if(StringUtils.isNotEmpty(message.getReceiver())){
							  //?????????????????????????????????
							  if(message.getReceiver().equals(Constants.ImserverConfig.REBOT_SESSIONID)){
								  MessageBodyProto.MessageBody  msg =  MessageBodyProto.MessageBody.parseFrom(message.getContent());
								  return  rebotProxy.botMessageReply(sessionId, msg.getContent());
							  }else{
								  return new MessageWrapper(MessageWrapper.MessageProtocol.REPLY, sessionId,message.getReceiver(), message);
							  }
						  }else if(StringUtils.isNotEmpty(message.getGroupId())){
							  return new MessageWrapper(MessageWrapper.MessageProtocol.GROUP, sessionId, null,message);
						  }else {
							  return new MessageWrapper(MessageWrapper.MessageProtocol.SEND, sessionId, null,message);
						  }
		            } catch (Exception e) {
		                e.printStackTrace();
		            }
				break;
			case  Constants.CmdType.JOINROOM://???????????????

			case Constants.CmdType.RECEIVEVIDEOFROM://??????????????????-????????????

			case Constants.CmdType.LEAVEROOM://???????????????

			case Constants.CmdType.ONICECANDIDATE:// ??????????????????????????????
				try {

					MessageProto.Model.Builder  result = MessageProto.Model.newBuilder(message);
					result.setTimeStamp(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
					if (message.getCmd() ==Constants.CmdType.RECEIVEVIDEOFROM ){
						sessionId = result.getSender();
					}
					result.setSender(sessionId);//???????????????sessionId
					message =  MessageProto.Model.parseFrom(result.build().toByteArray());
					return new MessageWrapper(MessageWrapper.MessageProtocol.GROUP_CALL, sessionId, null,message);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
		} 
        return null;
    }

    
 
    @Override
	public void saveOnlineMessageToDB(MessageWrapper message) {
    	try{
    		UserMessageEntity userMessage = convertMessageWrapperToBean(message);
    		if(userMessage!=null){
    			userMessage.setIsread(1);
            	userMessageServiceImpl.save(userMessage);
    		}
    	}catch(Exception e){
    		 log.error("MessageProxyImpl  user "+message.getSessionId()+" send msg to "+message.getReSessionId()+" error");
    		 throw new RuntimeException(e.getCause());
    	}
	}
    
    
    @Override
	public void saveOfflineMessageToDB(MessageWrapper message) {
    	try{
    		 
    		UserMessageEntity  userMessage = convertMessageWrapperToBean(message);
    		if(userMessage!=null){
    			userMessage.setIsread(0);
            	userMessageServiceImpl.save(userMessage);
    		}
    	}catch(Exception e){
    		 log.error("MessageProxyImpl  user "+message.getSessionId()+" send msg to "+message.getReSessionId()+" error");
    		 throw new RuntimeException(e.getCause());
    	} 
	}
    
    
    private UserMessageEntity convertMessageWrapperToBean(MessageWrapper message){
    	try{
    		//????????????????????????
    		if(!message.getSessionId().equals(Constants.ImserverConfig.REBOT_SESSIONID)){
    			MessageProto.Model  msg =  (MessageProto.Model)message.getBody();
            	MessageBodyProto.MessageBody  msgConten =  MessageBodyProto.MessageBody.parseFrom(msg.getContent());
            	UserMessageEntity  userMessage = new UserMessageEntity();
            	userMessage.setSenduser(message.getSessionId());
            	userMessage.setReceiveuser(message.getReSessionId());
            	userMessage.setContent(msgConten.getContent());
            	userMessage.setGroupid(msg.getGroupId());
            	userMessage.setCreatedate(msg.getTimeStamp());
            	userMessage.setUpdatedate(msg.getTimeStamp());
            	userMessage.setIsread(1);
            	return userMessage;
    		}
    	}catch(Exception e){
    		 throw new RuntimeException(e.getCause());
    	} 
    	return null;
    }

	public void setRebotProxy(RebotProxy rebotProxy) {
		this.rebotProxy = rebotProxy;
	}



	@Override
	public MessageProto.Model getOnLineStateMsg(String sessionId) {
		MessageProto.Model.Builder  result = MessageProto.Model.newBuilder();
		result.setTimeStamp(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		result.setSender(sessionId);//???????????????sessionId
		result.setCmd(Constants.CmdType.ONLINE);
		return result.build();
	}

	@Override
	public MessageProto.Model getSelfOnLineStateMsg(String sessionId)
	{
		MessageProto.Model.Builder  result = MessageProto.Model.newBuilder();
		result.setTimeStamp(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		result.setSender(sessionId);//???????????????sessionId
		result.setReceiver(sessionId);//???????????????sessionId
		result.setCmd(Constants.CmdType.ONLINE);
		return result.build();
	}


	@Override
	public MessageProto.Model getOffLineStateMsg(String sessionId) {
		MessageProto.Model.Builder  result = MessageProto.Model.newBuilder();
		result.setTimeStamp(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		result.setSender(sessionId);//???????????????sessionId
		result.setCmd(Constants.CmdType.OFFLINE);
		return result.build();
	}



	@Override
	public MessageWrapper getReConnectionStateMsg(String sessionId) {
		 MessageProto.Model.Builder  result = MessageProto.Model.newBuilder();
		 result.setTimeStamp(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		 result.setSender(sessionId);//???????????????sessionId
		 result.setCmd(Constants.CmdType.RECON);
		 return  new MessageWrapper(MessageWrapper.MessageProtocol.SEND, sessionId, null,result.build());
	}


	
    
    
}
