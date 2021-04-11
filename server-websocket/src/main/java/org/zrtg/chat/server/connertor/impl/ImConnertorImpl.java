package org.zrtg.chat.server.connertor.impl;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zrtg.chat.common.constant.Constants;
import org.zrtg.chat.common.dwrmanage.DwrUtil;
import org.zrtg.chat.common.model.MessageWrapper;
import org.zrtg.chat.common.model.Session;
import org.zrtg.chat.common.model.proto.MessageProto;
import org.zrtg.chat.common.utils.SessionUtils;
import org.zrtg.chat.framework.exception.PushException;
import org.zrtg.chat.framework.group.ImChannelGroup;
import org.zrtg.chat.framework.proxy.MessageProxy;
import org.zrtg.chat.framework.session.impl.SessionManagerImpl;
import org.zrtg.chat.server.connertor.ImConnertor;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ImConnertorImpl implements ImConnertor
{
	private final static Logger log = LoggerFactory.getLogger(ImConnertorImpl.class);

	private static Map<String, String> UN_AUTH_SESSIONS = new ConcurrentHashMap<String, String>();

	@Autowired
    private SessionManagerImpl sessionManager;

	@Autowired
    private MessageProxy proxy;

	public void setSessionManager(SessionManagerImpl sessionManager) {
		this.sessionManager = sessionManager;
	}
	public void setProxy(MessageProxy proxy) {
		this.proxy = proxy;
	}


	@Override
	public void heartbeatToClient(ChannelHandlerContext hander, MessageWrapper wrapper) {
		//设置心跳响应时间
		hander.channel().attr(Constants.SessionConfig.SERVER_SESSION_HEARBEAT).set(System.currentTimeMillis());
	}

	@Override
	public void pushGroupMessage(MessageWrapper wrapper)
			throws RuntimeException {
		  //这里判断群组ID 是否存在 并且该用户是否在群组内
		  ImChannelGroup.broadcast(wrapper.getBody());
		  DwrUtil.sedMessageToAll((MessageProto.Model)wrapper.getBody());
		  proxy.saveOnlineMessageToDB(wrapper);
	}

	@Override  
	public void pushMessage(MessageWrapper wrapper) throws RuntimeException {
        try {
        	//sessionManager.send(wrapper.getSessionId(), wrapper.getBody());
        	Session session = sessionManager.getSession(wrapper.getSessionId());
      		/*
      		 * 服务器集群时，可以在此
      		 * 判断当前session是否连接于本台服务器，如果是，继续往下走，如果不是，将此消息发往当前session连接的服务器并 return
      		 * if(session!=null&&!session.isLocalhost()){//判断当前session是否连接于本台服务器，如不是
      		 * //发往目标服务器处理
      		 * return; 
      		 * }
      		 */ 
      		if (session != null) {
      			boolean result = session.write(wrapper.getBody());
      			return ;
      		}
        } catch (Exception e) {
        	log.error("connector pushMessage  Exception.", e);
            throw new RuntimeException(e.getCause());
        }
    }
	
    
	@Override    
	public void pushMessage(String sessionId,MessageWrapper wrapper) throws RuntimeException{
		//判断是不是无效用户回复    
		if(!sessionId.equals(Constants.ImserverConfig.REBOT_SESSIONID)){//判断非机器人回复时验证
			Session session = sessionManager.getSession(sessionId);
	        if (session == null) {
	        	 throw new RuntimeException(String.format("session %s is not exist.", sessionId));
	        } 
		}
	   try {
	    	///取得接收人 给接收人写入消息
	    	Session responseSession = sessionManager.getSession(wrapper.getReSessionId());
	  		if (responseSession != null && responseSession.isConnected() ) {
	  			boolean result = responseSession.write(wrapper.getBody());
	  			if(result){
	  				proxy.saveOnlineMessageToDB(wrapper);
	  			}else{
	  				proxy.saveOfflineMessageToDB(wrapper);
	  			}
	  			return;
	  		}else{
	  			proxy.saveOfflineMessageToDB(wrapper);
	  		}
	    } catch (PushException e) {
	    	log.error("connector send occur PushException.", e);
	       
	        throw new RuntimeException(e.getCause());
	    } catch (Exception e) {
	    	log.error("connector send occur Exception.", e);
	        throw new RuntimeException(e.getCause());
	    }  
	    
	}
	@Override  
    public boolean validateSession(MessageWrapper wrapper) throws RuntimeException {
        try {
            return sessionManager.exist(wrapper.getSessionId());
        } catch (Exception e) {
        	log.error("connector validateSession Exception!", e);
            throw new RuntimeException(e.getCause());
        }
    }

	@Override
	public void close(ChannelHandlerContext hander,MessageWrapper wrapper) {
		String sessionId = getChannelSessionId(hander);
        if (StringUtils.isNotBlank(sessionId)) {
        	close(hander); 
            log.warn("connector close channel sessionId -> " + sessionId + ", ctx -> " + hander.toString());
        }
	}
	
	@Override
	public void close(ChannelHandlerContext hander) {
		   String sessionId = getChannelSessionId(hander);
		   try {
			    String nid = hander.channel().id().asShortText();
	        	Session session = sessionManager.getSession(sessionId);
	      		if (session != null) {
	      			sessionManager.removeSession(sessionId,nid); 
	      			ImChannelGroup.remove(hander.channel());
	      		  log.info("connector close sessionId -> " + sessionId + " success " );
	      		}
	        } catch (Exception e) {
	        	log.error("connector close sessionId -->"+sessionId+"  Exception.", e);
	            throw new RuntimeException(e.getCause());
	        }
	}
	
	@Override
	public void close(String sessionId) {
		 try {
        	 Session session = sessionManager.getSession(sessionId);
      		 if (session != null) {
      			sessionManager.removeSession(sessionId); 
      			List<Channel> list = session.getSessionAll();
      			for(Channel ch:list){
      				ImChannelGroup.remove(ch);
      			} 
      		    log.info("connector close sessionId -> " + sessionId + " success " );
      		 }
	     } catch (Exception e) {
        	log.error("connector close sessionId -->"+sessionId+"  Exception.", e);
            throw new RuntimeException(e.getCause());
	     }
	}
	
    @Override
    public void connect(ChannelHandlerContext ctx, MessageWrapper wrapper) {
        try {

			  String sessionId0 = getChannelSessionId(ctx);
        	  String sessionId = wrapper.getSessionId();
        	  if (sessionId0.isEmpty()){
				  throw new Exception("ChannelSessionId has not existed");
			  }

        	  //当sessionID存在或者相等  视为同一用户重新连接
              if (sessionId.equals(sessionId0)) {
                  log.info("connector reconnect sessionId -> " + sessionId + ", ctx -> " + ctx.toString());
                  pushMessage(proxy.getReConnectionStateMsg(sessionId0));
              } else {

              	  wrapper.setSessionId(sessionId0);

                  log.info("connector connect sessionId -> " + sessionId + ", sessionId0 -> " + sessionId0 + ", ctx -> " + ctx.toString());
                  sessionManager.createSession(wrapper, ctx);
                  //setChannelSessionId(ctx, sessionId);
                  log.info("create channel attr sessionId " + sessionId + " successful, ctx -> " + ctx.toString());
                  //这里当做认证(最好校验key和签名)
				  if (isAuth(wrapper)){
					  removeUnAuthSessionId(sessionId0);
				  }

              }
        } catch (Exception e) {
        	log.error("connector connect  Exception:{}", e.getMessage());
        }
    }
     
	@Override
	public boolean exist(String sessionId) throws Exception {
		return sessionManager.exist(sessionId);
	} 
	@Override  
    public String getChannelSessionId(ChannelHandlerContext ctx) {
        return ctx.channel().attr(Constants.SessionConfig.SERVER_SESSION_ID).get();
    }

	@Override
	public void removeUnAuthSessionId(String sessionId)
	{
		UN_AUTH_SESSIONS.remove(sessionId);
	}

	@Override
	public void closeIfNotAuth(String sessionId)
	{
		if (UN_AUTH_SESSIONS.containsKey(sessionId)){
			close(sessionId);
			removeUnAuthSessionId(sessionId);
		}
	}

	@Override
	public void createChannelSessionId(ChannelHandlerContext ctx)
	{
		try{
			String sessionId0 = getChannelSessionId(ctx);
			if (StringUtils.isEmpty(sessionId0)){
				sessionId0 = SessionUtils.getSessionId();
				setChannelSessionId(ctx, sessionId0);
				//将 sessionId 存入未验证的map中
				UN_AUTH_SESSIONS.computeIfAbsent(sessionId0,k-> "1");
			}else {
				throw new Exception("ChannelSessionId has existed");
			}
		}catch (Exception e){
			log.error(" createChannelSessionId  Exception: {}", e.getMessage());
		}
	}

	private void setChannelSessionId(ChannelHandlerContext ctx, String sessionId) {
        ctx.channel().attr(Constants.SessionConfig.SERVER_SESSION_ID).set(sessionId);
    }

	/**
	 * 校验签名
	 * @param wrapper
	 * @return
	 */
	private boolean isAuth(MessageWrapper wrapper){

		/**
		 * TODO 签名校验
		 */

		return true;
	}

}
