package com.acumen.tcp;

import com.acumen.tcp.dto.RequestPacketFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;


@Service
public class Client extends Thread {

    private final RequestPacketFactory packetFactory;
    private final ConnectStore connectStore;

    public Client(RequestPacketFactory packetFactory, ConnectStore connectStore) {
        this.packetFactory = packetFactory;
        this.connectStore = connectStore;
    }

    @PostConstruct
    public void init() {
        start();
    }

    @Override
    public void run() {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        try {
            b.group(group)
                    .channel(NioSocketChannel.class)

                    .remoteAddress("localhost", 9999)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(
                                    new ResponseDecoder(packetFactory),
                                    new RequestEncoder(),
                                    new ClientHandler(connectStore));
                        }
                    });

            // Start the connection attempt.
            ChannelFuture channelFuture = b.connect().sync().channel().closeFuture().syncUninterruptibly();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}
