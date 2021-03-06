
package org.zrtg.chat.server.websocket;

import com.google.protobuf.MessageLite;
import com.google.protobuf.MessageLiteOrBuilder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.zrtg.chat.common.constant.Constants;
import org.zrtg.chat.common.model.proto.MessageProto;
import org.zrtg.chat.common.utils.SecureChatServerInitializer;
import org.zrtg.chat.framework.proxy.MessageProxy;
import org.zrtg.chat.server.connertor.impl.ImConnertorImpl;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import java.io.InputStream;
import java.util.List;

import static io.netty.buffer.Unpooled.wrappedBuffer;

@Component
public class ImWebsocketServer  {

    private final static Logger log = LoggerFactory.getLogger(ImWebsocketServer.class);
    
    private ProtobufDecoder decoder = new ProtobufDecoder(MessageProto.Model.getDefaultInstance());

    @Autowired
    private MessageProxy proxy = null;

    @Autowired
    private ImConnertorImpl connertor;

    private int port = 1314;
 
    private final EventLoopGroup bossGroup = new NioEventLoopGroup();
    private final EventLoopGroup workerGroup = new NioEventLoopGroup(10);
    private Channel channel;

    @PostConstruct
    public void init() throws Exception {
        log.info("start chat websocketserver ...");

        // Server ????????????
        ServerBootstrap bootstrap = new ServerBootstrap();

        bootstrap.group(bossGroup, workerGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
	    		ChannelPipeline pipeline = ch.pipeline();
	    		// ssl??????
                InputStream stream = getClass().getClassLoader().getResourceAsStream("keystore.jks");

                SSLContext sslContext = SecureChatServerInitializer.createSSLContext("JKS",stream,"kurento");
                SSLEngine engine = sslContext.createSSLEngine();
                engine.setUseClientMode(false);

                pipeline.addLast(new SslHandler(engine));
	    		 // HTTP????????????????????????
	            pipeline.addLast(new HttpServerCodec());
	            // ???????????????????????????????????????FullHttpRequest??????FullHttpResponse???
	            // ?????????HTTP?????????????????????HTTP?????????????????????????????????HttpRequest/HttpResponse,HttpContent,LastHttpContent
	            pipeline.addLast(new HttpObjectAggregator(Constants.ImserverConfig.MAX_AGGREGATED_CONTENT_LENGTH));
	            // ?????????????????????????????????????????????1G???????????????????????????????????????????????????jvm?????????; ??????????????????????????????????????????
	            pipeline.addLast(new ChunkedWriteHandler());
	            // WebSocket????????????
	            pipeline.addLast(new WebSocketServerCompressionHandler());
	            // ?????????????????????
	            pipeline.addLast(new WebSocketServerProtocolHandler("/ws", null, true, Constants.ImserverConfig.MAX_FRAME_LENGTH));
	            // ???????????????
	            pipeline.addLast(new MessageToMessageDecoder<WebSocketFrame>() {
	                @Override
	                protected void decode(ChannelHandlerContext ctx, WebSocketFrame frame, List<Object> objs) throws Exception {
	                    ByteBuf buf = ((BinaryWebSocketFrame) frame).content();
	                    objs.add(buf);
	                    buf.retain();
	                }
	            });
	            // ???????????????
	            pipeline.addLast(new MessageToMessageEncoder<MessageLiteOrBuilder>() {
	                @Override
	                protected void encode(ChannelHandlerContext ctx, MessageLiteOrBuilder msg, List<Object> out) throws Exception {
	                    ByteBuf result = null;
	                    if (msg instanceof MessageLite) {
	                        result = wrappedBuffer(((MessageLite) msg).toByteArray());
	                    }
	                    if (msg instanceof MessageLite.Builder) {
	                        result = wrappedBuffer(((MessageLite.Builder) msg).build().toByteArray());
	                    }
	                    // ?????????????????????websocket????????????????????????????????????????????????protobuf???????????????
	                    WebSocketFrame frame = new BinaryWebSocketFrame(result);
	                    out.add(frame);
	                }
	            });
	            // ????????????????????????Protobuf?????????????????????CommonProtocol??????
	            pipeline.addLast(decoder);
	            pipeline.addLast(new IdleStateHandler(Constants.ImserverConfig.READ_IDLE_TIME,Constants.ImserverConfig.WRITE_IDLE_TIME,0));
	            // ???????????????
	            pipeline.addLast(new ImWebSocketServerHandler(proxy,connertor));
	    		 
            }
        });
        
        // ????????????
    	bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
        // ?????????????????????????????????
        log.info("start chat websocketserver at port[" + port + "].");
        ChannelFuture future = bootstrap.bind(port).sync();
    	channel = future.channel();
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    log.info("websocketserver have success bind to " + port);
                } else {
                    log.error("websocketserver fail bind to " + port);
                }
            }
        });
       // future.channel().closeFuture().syncUninterruptibly();
    }

    @PreDestroy
    public void destroy() {
        log.info("destroy chat websocketserver ...");
        // ?????????????????????
        if (channel != null) {
			channel.close();
		}
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        log.info("destroy chat webscoketserver complate.");
    }
    
 
    

    public void setPort(int port) {
        this.port = port;
    }

	public void setProxy(MessageProxy proxy) {
		this.proxy = proxy;
	}
 

    public void setConnertor(ImConnertorImpl connertor) {
		this.connertor = connertor;
	}
    
    
}
