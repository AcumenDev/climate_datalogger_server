package com.acumen.tcp;

import com.acumen.tcp.dto_new.TemperatureProtocol;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;


@Service
public class Client extends Thread {

    private final ConnectStore connectStore;

    public Client(ConnectStore connectStore) {
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
                                    new LoggingHandler(),
                                    // new ProtobufVarint32FrameDecoder(),
                                    new ProtobufDecoder(TemperatureProtocol.BaseMessage.getDefaultInstance()),
                                    new ClientHandler(connectStore),
                                    //new ProtobufVarint32LengthFieldPrepender(),
                                    new ProtobufEncoder()
                            );
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
