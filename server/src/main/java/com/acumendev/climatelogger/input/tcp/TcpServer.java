package com.acumendev.climatelogger.input.tcp;

import com.acumendev.climatelogger.input.AuthHandler;
import com.acumendev.climatelogger.input.SensorChannel;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

@Component
public class TcpServer extends Thread {

    private final Map<String, SensorChannel> clientHandlers;
    private final AuthHandler authHandler;

    public TcpServer(Map<String, SensorChannel> clientHandlers,
                     AuthHandler authHandler) {
        this.clientHandlers = clientHandlers;
        this.authHandler = authHandler;
    }

    @PostConstruct
    public void init() {
        start();
    }

    @Override
    public void run() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(
                                    //new LoggingHandler(LogLevel.TRACE),
                                    new ProtobufDecoder(TempNew.BaseMessage.getDefaultInstance()),
                                    new TcpHandler(clientHandlers, authHandler),
                                    new ProtobufEncoder()
                            );
                        }
                    }).option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture f = b.bind(9999).sync();
            f.channel().closeFuture().awaitUninterruptibly().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
