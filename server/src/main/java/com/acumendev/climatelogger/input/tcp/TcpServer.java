package com.acumendev.climatelogger.input.tcp;

import com.acumendev.climatelogger.input.AuthHandler;
import com.acumendev.climatelogger.input.tcp.handlers.SensorHandler;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Map;

@Slf4j
@Component
public class TcpServer extends Thread {

    private final Map<String, SensorHandler> sensorHandlers;
    private final AuthHandler authHandler;
    private ChannelFuture future;

    public TcpServer(Map<String, SensorHandler> sensorHandlers,
                     AuthHandler authHandler) {
        this.sensorHandlers = sensorHandlers;
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
                                    new ProtobufDecoder(TemperatureProtocol.BaseMessage.getDefaultInstance()),
                                    new TcpHandler(sensorHandlers, authHandler),
                                    new ProtobufEncoder()
                            );
                        }
                    }).option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            future = b.bind(9999).sync();
            future.channel().closeFuture().awaitUninterruptibly().sync();

        } catch (Exception e) {
            log.error("Ошбка сервера tcp", e);

        } finally {

            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    @PreDestroy
    private void destroyServer() {
        try {
            log.info("Остановка сервера tcp {}", future);
            future.channel().disconnect();
            future.channel().close();
        } catch (Exception e) {
            log.error("Ошбка остановки сервера tcp", e);
        }
    }
}
