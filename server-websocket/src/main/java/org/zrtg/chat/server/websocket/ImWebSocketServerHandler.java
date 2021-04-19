package org.zrtg.chat.server.websocket;


import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zrtg.chat.common.constant.Constants;
import org.zrtg.chat.common.model.MessageWrapper;
import org.zrtg.chat.common.model.proto.MessageProto;
import org.zrtg.chat.common.utils.ImUtils;
import org.zrtg.chat.common.utils.SpringUtils;
import org.zrtg.chat.framework.proxy.MessageProxy;
import org.zrtg.chat.server.connertor.impl.ImConnertorImpl;
import org.zrtg.chat.server.handler.GroupVideoChatHandler;

import java.util.concurrent.TimeUnit;

@Sharable
public class ImWebSocketServerHandler   extends SimpleChannelInboundHandler<MessageProto.Model>{

	private final static Logger log = LoggerFactory.getLogger(ImWebSocketServerHandler.class);
    private ImConnertorImpl connertor = null;
    private MessageProxy proxy = null;

    private GroupVideoChatHandler groupVideoChatHandler = SpringUtils.getBean(GroupVideoChatHandler.class);

    public ImWebSocketServerHandler(MessageProxy proxy, ImConnertorImpl connertor) {
        this.connertor = connertor;
        this.proxy = proxy;
    }
	
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object o) throws Exception {
    	 String sessionId = ctx.channel().attr(Constants.SessionConfig.SERVER_SESSION_ID).get();
    	//发送心跳包
    	if (o instanceof IdleStateEvent && ((IdleStateEvent) o).state().equals(IdleState.WRITER_IDLE)) {
			  if(StringUtils.isNotEmpty(sessionId)){
				  MessageProto.Model.Builder builder = MessageProto.Model.newBuilder();
				  builder.setCmd(Constants.CmdType.HEARTBEAT);
			      builder.setMsgtype(Constants.ProtobufType.SEND);
				  ctx.channel().writeAndFlush(builder);
			  } 
 			 log.debug(IdleState.WRITER_IDLE +"... from "+sessionId+"-->"+ctx.channel().remoteAddress()+" nid:" +ctx.channel().id().asShortText());
 	    } 
	        
	    //如果心跳请求发出70秒内没收到响应，则关闭连接
	    if ( o instanceof IdleStateEvent && ((IdleStateEvent) o).state().equals(IdleState.READER_IDLE)){
			log.debug(IdleState.READER_IDLE +"... from "+sessionId+" nid:" +ctx.channel().id().asShortText());
			Long lastTime = (Long) ctx.channel().attr(Constants.SessionConfig.SERVER_SESSION_HEARBEAT).get();
	     	if(lastTime == null || ((System.currentTimeMillis() - lastTime)/1000>= Constants.ImserverConfig.PING_TIME_OUT))
	     	{
	     		connertor.close(ctx);
	     	}
	     	//ctx.channel().attr(Constants.SessionConfig.SERVER_SESSION_HEARBEAT).set(null);
	    }
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, MessageProto.Model message)
			throws Exception {

          log.info("websocket server recive message  cmd:{}",message.getCmd());
		  try {
			   String sessionId = connertor.getChannelSessionId(ctx);
                // inbound
                if (message.getMsgtype() == Constants.ProtobufType.SEND) {
                	ctx.channel().attr(Constants.SessionConfig.SERVER_SESSION_HEARBEAT).set(System.currentTimeMillis());
                    MessageWrapper wrapper = proxy.convertToMessageWrapper(sessionId, message);
                    if (wrapper != null){
                        receiveMessages(ctx, wrapper);
                    }
                }
                // outbound
                if (message.getMsgtype() == Constants.ProtobufType.REPLY) {
                	MessageWrapper wrapper = proxy.convertToMessageWrapper(sessionId, message);
                	if (wrapper != null){
                        receiveMessages(ctx, wrapper);
                    }

                }
                if (message.getMsgtype() == Constants.ProtobufType.GROUP_CALL){//直播间消息
                    log.info("直播间消息:{}",sessionId);
                    MessageWrapper wrapper = proxy.convertToMessageWrapper(sessionId, message);
                    if (wrapper != null){
                        receiveMessages(ctx, wrapper);
                    }
                }
	        } catch (Exception e) {
	            log.error("ImWebSocketServerHandler channerRead error.", e);
	            throw e;
	        }
		 
	}
	
	
	@Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
    	log.info("ImWebSocketServerHandler  join from "+ImUtils.getRemoteAddress(ctx)+" nid:" + ctx.channel().id().asShortText());
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        log.debug("ImWebSocketServerHandler Disconnected from {" +ctx.channel().remoteAddress()+"--->"+ ctx.channel().localAddress() + "}");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        log.debug("ImWebSocketServerHandler channelActive from (" + ImUtils.getRemoteAddress(ctx) + ")");
        //为channel创建sessionid
        connertor.createChannelSessionId(ctx);
        //设置一个定时器
        ctx.executor().schedule(new ConnectionTerminator(ctx), 30, TimeUnit.SECONDS);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        log.debug("ImWebSocketServerHandler channelInactive from (" + ImUtils.getRemoteAddress(ctx) + ")");
        String sessionId = connertor.getChannelSessionId(ctx);
        log.debug("ImWebSocketServerHandler channel sessionId (" + sessionId + ")");
        groupVideoChatHandler.leaveRoom(sessionId);
        receiveMessages(ctx,new MessageWrapper(MessageWrapper.MessageProtocol.CLOSE, sessionId,null, null));

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.warn("ImWebSocketServerHandler (" + ImUtils.getRemoteAddress(ctx) + ") -> Unexpected exception from downstream." + cause);
    }





    /**
     * to send message
     *
     * @param hander
     * @param wrapper
     */
    private void receiveMessages(ChannelHandlerContext hander, MessageWrapper wrapper) {
    	//设置消息来源为Websocket
    	wrapper.setSource(Constants.ImserverConfig.WEBSOCKET);
        if (wrapper.isConnect()) {
       	    connertor.connect(hander, wrapper); 
        } else if (wrapper.isClose()) {
        	connertor.close(hander,wrapper);

        } else if (wrapper.isHeartbeat()) {
        	connertor.heartbeatToClient(hander,wrapper);
        }else if (wrapper.isGroup()) {
        	connertor.pushGroupMessage(wrapper);
        }else if (wrapper.isSend()) {
        	connertor.pushMessage(wrapper);
        } else if (wrapper.isReply()) {
        	connertor.pushMessage(wrapper.getSessionId(),wrapper);
        }  else if (wrapper.isGroupCall()){
            if (groupVideoChatHandler == null){
                log.error("groupVideoChatHandler not init");
            }else {
                //当前连接的sessionid
                String sessionId = connertor.getChannelSessionId(hander);
                groupVideoChatHandler.handleGroupChatMessage(sessionId,wrapper);
            }
        }
    }

    private class ConnectionTerminator implements Runnable{
        ChannelHandlerContext ctx;
        public ConnectionTerminator(ChannelHandlerContext ctx) {
            // TODO Auto-generated constructor stub
            this.ctx = ctx;
        }

        @Override
        public void run()
        {
            String sessionId = connertor.getChannelSessionId(this.ctx);
            log.debug("timer run sessionid:{}",sessionId);
            if (sessionId.isEmpty()){//说明没有发送连接命令
                this.ctx.close();
            }else {
                //判断有没有认证
                connertor.closeIfNotAuth(sessionId);
            }

        }
    }
}
