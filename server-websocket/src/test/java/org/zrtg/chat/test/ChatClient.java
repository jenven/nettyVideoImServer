package org.zrtg.chat.test;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.zrtg.chat.common.constant.Constants;
import org.zrtg.chat.common.model.proto.MessageProto;
import org.zrtg.chat.common.utils.SessionUtils;
import org.zrtg.chat.test.data.MessageData;

import java.util.Scanner;
import java.util.UUID;

/**
 * @author wangq
 * @create_at 2021-4-9 16:42
 */
@Slf4j
public class ChatClient
{

    public  String host = "127.0.0.1";
    public  int port = 2001;

    public Bootstrap bootstrap = getBootstrap();
    public Channel channel = getChannel(host, port);

    /**
     * Init Bootstrap
     */
    public   Bootstrap getBootstrap() {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group);
        b.channel(NioSocketChannel.class);
        b.handler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast("frameDecoder", new ProtobufVarint32FrameDecoder());
                pipeline.addLast("decoder", new ProtobufDecoder(MessageProto.Model.getDefaultInstance()));
                pipeline.addLast("frameEncoder", new ProtobufVarint32LengthFieldPrepender());
                pipeline.addLast(new IdleStateHandler(Constants.ImserverConfig.READ_IDLE_TIME,Constants.ImserverConfig.WRITE_IDLE_TIME,0));
                pipeline.addLast("encoder", new ProtobufEncoder());
                pipeline.addLast("handler", new ChatClientHandler());
            }
        });
        return b;
    }

    public   Channel getChannel(String host, int port) {
        Channel channel;
        try {
            channel = bootstrap.connect(host, port).sync().channel();
        } catch (Exception e) {
            System.out.println("Connect Server (host[" + host + "]:port[" + port + "]) Failure." + e);
            return null;
        }
        return channel;
    }

    public  void connect(Object msg) throws Exception {
        if (channel != null) {
            log.info("send msg:{}",msg);
            channel.writeAndFlush(msg).sync();
        }
    }

    public static void main(String[] args) throws Exception {
        try {
            //String currentuser = "abc";
            String currentuser = "";
            //链接socket服务
            ChatClient client =    new ChatClient();
            client.connect(new MessageData().generateConnect(currentuser));
            Scanner sc = new Scanner(System.in);
            System.out.println("输入聊天用户SessionId：");
            String reuser=sc.next();
            System.out.println("和"+reuser+"聊天吧！");
            sc.nextLine();
            while (true) {
                String line = sc.nextLine();
                System.out.println("发送内容："+ line);
                client.connect(new MessageData().generateSend(currentuser, reuser,line));
                if (line.equals("exit")) break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
