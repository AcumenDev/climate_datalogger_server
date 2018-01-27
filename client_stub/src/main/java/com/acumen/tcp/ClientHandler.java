package com.acumen.tcp;

import com.acumen.tcp.dto_new.TemperatureProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler extends SimpleChannelInboundHandler<TemperatureProtocol.BaseMessage> {

    private final ConnectStore connectStore;

    public ClientHandler(ConnectStore connectStore) {
        this.connectStore = connectStore;

    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        System.out.println("channelRegistered ");
        connectStore.setCtx(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        System.out.println("channelUnregistered ");
        connectStore.setCtx(null);
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TemperatureProtocol.BaseMessage msg) throws Exception {
        System.out.println("channelRead0  \n" + msg.toString());
        // ctx.writeAndFlush(ResponseData.builder().val("1234567890").build());
        connectStore.queue.add(msg);
    }
}
