package com.acumendev.climatelogger.input.tcp;

import com.acumendev.climatelogger.input.tcp.dto.RequestPacketFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

@Component
public class TcpInput extends Thread {


    private final Map<String, ChannelHandlerContext> clientHandlers;
    private final RequestPacketFactory packetFactory;


    public TcpInput(Map<String, ChannelHandlerContext> clientHandlers, RequestPacketFactory packetFactory) {
        this.clientHandlers = clientHandlers;
        this.packetFactory = packetFactory;
    }


    @PostConstruct
    public void init() {

      start();

    }


    @Override
    public void run()  {
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
                                    new RequestDecoder(packetFactory),
                                    new ResponseEncoder(),
                                    new ClientHandler(clientHandlers));
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
