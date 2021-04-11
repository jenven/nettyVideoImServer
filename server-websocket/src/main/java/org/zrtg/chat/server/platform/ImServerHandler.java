package org.zrtg.chat.server.platform;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zrtg.chat.common.constant.Constants;
import org.zrtg.chat.common.model.MessageWrapper;
import org.zrtg.chat.common.model.proto.MessageProto;
import org.zrtg.chat.common.utils.ImUtils;
import org.zrtg.chat.framework.proxy.MessageProxy;
import org.zrtg.chat.server.connertor.impl.ImConnertorImpl;

import java.util.concurrent.TimeUnit;

@Sharable
public class ImServerHandler  extends ChannelInboundHandlerAdapter{
    private final static Logger log = LoggerFactory.getLogger(ImServerHandler.class);
    
    
    private ImConnertorImpl connertor = null;
    private MessageProxy proxy = null;

    public ImServerHandler(MessageProxy proxy,  ImConnertorImpl connertor) {
        this.connertor = connertor;
        this.proxy = proxy;
    }
    
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object o) throws Exception {
    	 String sessionId = ctx.channel().attr(Constants.SessionConfig.SERVER_SESSION_ID).get();
    	//发送心跳包
    	if (o instanceof IdleStateEvent && ((IdleStateEvent) o).state().equals(IdleState.WRITER_IDLE)) {
		      //ctx.channel().attr(Constants.SessionConfig.SERVER_SESSION_HEARBEAT).set(System.currentTimeMillis());
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
	    	Long currentTime = System.currentTimeMillis();
	    	
	    	if(lastTime == null || ( (currentTime - lastTime)/1000 >= Constants.ImserverConfig.PING_TIME_OUT))
	     	{
	     		connertor.close(ctx);
	     	}
	     	//ctx.channel().attr(Constants.SessionConfig.SERVER_SESSION_HEARBEAT).set(null);
	    }
	}
    
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object o) throws Exception {
        log.info("socket server recive message:{}",o);
        try {
            if (o instanceof MessageProto.Model) {
                MessageProto.Model message = (MessageProto.Model) o;
                String sessionId = connertor.getChannelSessionId(ctx);
                // inbound
                if (message.getMsgtype() == Constants.ProtobufType.SEND) {
                	ctx.channel().attr(Constants.SessionConfig.SERVER_SESSION_HEARBEAT).set(System.currentTimeMillis());
                    MessageWrapper wrapper = proxy.convertToMessageWrapper(sessionId, message);
                    if (wrapper != null) {
                        receiveMessages(ctx, wrapper);
                    }
                }
                // outbound
                if (message.getMsgtype() == Constants.ProtobufType.REPLY) {
                	MessageWrapper wrapper = proxy.convertToMessageWrapper(sessionId, message);
                	if (wrapper != null) {
                        receiveMessages(ctx, wrapper);
                    }
                }
            } else {
                log.warn("ImServerHandler channelRead message is not proto.");
            }
        } catch (Exception e) {
            log.error("ImServerHandler channerRead error.", e);
            throw e;
        }
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
    	log.info("ImServerHandler  join from "+ ImUtils.getRemoteAddress(ctx)+" nid:" + ctx.channel().id().asShortText());
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        log.debug("ImServerHandler Disconnected from {" +ctx.channel().remoteAddress()+"--->"+ ctx.channel().localAddress() + "}");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        log.debug("ImServerHandler channelActive from (" + ImUtils.getRemoteAddress(ctx) + ")");
        //设置一个定时器
        ctx.executor().schedule(new ConnectionTerminator(ctx), 30, TimeUnit.SECONDS);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        log.debug("ImServerHandler channelInactive from (" + ImUtils.getRemoteAddress(ctx) + ")");
        String sessionId = connertor.getChannelSessionId(ctx);
        receiveMessages(ctx,new MessageWrapper(MessageWrapper.MessageProtocol.CLOSE, sessionId,null, null));  
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.warn("ImServerHandler (" + ImUtils.getRemoteAddress(ctx) + ") -> Unexpected exception from downstream." + cause);
    }





    /**
     * to send  message
     *
     * @param hander
     * @param wrapper
     */
    private void receiveMessages(ChannelHandlerContext hander, MessageWrapper wrapper) {
    	//设置消息来源为socket
    	wrapper.setSource(Constants.ImserverConfig.SOCKET);
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
        }
    }



}
