package com.acumen.tcp;

import com.acumen.tcp.dto.AuthRequest;
import com.acumen.tcp.dto.ResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler extends SimpleChannelInboundHandler<ResponsePacket> {

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
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ResponsePacket msg) throws Exception {
        System.out.println("channelRead0  " + msg.getId());
       // ctx.writeAndFlush(ResponseData.builder().val("1234567890").build());
    }
}
