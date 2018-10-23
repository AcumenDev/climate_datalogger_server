package com.acumendev.climatelogger.input.tcp;

import com.acumendev.climatelogger.input.AuthHandler;
import com.acumendev.climatelogger.input.tcp.handlers.SensorHandler;
import com.acumendev.climatelogger.protocol.BaseMessageOuterClass;
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
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Map;


@Component
public class TcpServer extends Thread {

    private final Logger LOGGER = LoggerFactory.getLogger(TcpServer.class);

    private final Map<String, SensorHandler<BaseMessageOuterClass.BaseMessage>> sensorHandlers;
    private final AuthHandler authHandler;
    private final int port;
    private ChannelFuture future;

    public TcpServer(Map<String, SensorHandler<BaseMessageOuterClass.BaseMessage>> sensorHandlers,
                     AuthHandler authHandler,
                     @Value("${input.tcp.port:9999}") int port) {
        this.sensorHandlers = sensorHandlers;
        this.authHandler = authHandler;
        this.port = port;
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
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel socketChannel) {
                            socketChannel.pipeline()
                                    .addFirst("log", new LoggingHandler(LogLevel.TRACE))
                                    //  .addAfter("log", "FrameDecoder", new ProtobufVarint32FrameDecoder())
                                    .addLast(new ProtobufDecoder(BaseMessageOuterClass.BaseMessage.getDefaultInstance()))
                                    .addLast(new TcpHandler(sensorHandlers, authHandler))
                                    //  .addAfter("ClientHandler", "FrameEncoder", new ProtobufVarint32LengthFieldPrepender())
                                    .addLast(new ProtobufEncoder());

                        }
                    }).option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.SO_REUSEADDR, true);

            future = serverBootstrap.bind(port).sync();
            future.channel().closeFuture().awaitUninterruptibly().sync();

        } catch (Exception e) {
            LOGGER.error("Ошбка запуска  сервера tcp", e);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    @PreDestroy
    private void destroyServer() {
        try {
            LOGGER.info("Остановка сервера tcp {}", future);
            future.channel().disconnect();
            future.channel().close();
        } catch (Exception e) {
            LOGGER.error("Ошбка остановки сервера tcp", e);
        }
    }
}
