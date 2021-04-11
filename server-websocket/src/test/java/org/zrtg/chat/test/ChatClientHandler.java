package org.zrtg.chat.test;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zrtg.chat.common.constant.Constants;
import org.zrtg.chat.common.model.proto.MessageBodyProto;
import org.zrtg.chat.common.model.proto.MessageProto;
import org.zrtg.chat.test.data.MessageData;

/**
 * @author wangq
 * @create_at 2021-4-9 16:44
 */
@ChannelHandler.Sharable
public class ChatClientHandler  extends ChannelInboundHandlerAdapter
{
    private final static Logger logger = LoggerFactory.getLogger(ChatClientHandler.class);
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object o) throws Exception {
        MessageProto.Model message = (MessageProto.Model) o;

        if(message.getCmd()== Constants.CmdType.HEARTBEAT){
            ctx.channel().writeAndFlush(new MessageData().generateHeartbeat());
            System.out.println("------------心跳检测--------------"+message);
        }else if(message.getCmd()==Constants.CmdType.ONLINE){
            System.out.println(message.getSender()+"------------上线了--------------");
        }else if(message.getCmd()==Constants.CmdType.RECON){
            System.out.println(message.getSender()+"------------重新连接--------------");
        }else if(message.getCmd()==Constants.CmdType.OFFLINE){
            System.out.println(message.getSender()+"------------下线了--------------");
        }else if(message.getCmd()==Constants.CmdType.MESSAGE){
            MessageBodyProto.MessageBody  content =  MessageBodyProto.MessageBody.parseFrom(message.getContent()) ;
            logger.info(message.getSender()+" 回复我 :" + content.getContent());
        }


    }

    public void userEventTriggered(ChannelHandlerContext ctx, Object o) throws Exception {
        //断线重连
    }
}
