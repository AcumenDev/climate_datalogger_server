package com.acumendev.climatelogger.input.tcp;

import com.acumendev.climatelogger.input.tcp.dto.AuthStatus;
import com.acumendev.climatelogger.input.tcp.dto.RequestPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Map;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    private final Map<String, ChannelHandlerContext> clientHandlers;

    public ClientHandler(Map<String, ChannelHandlerContext> clientHandlers) {
        this.clientHandlers = clientHandlers;
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        RequestPacket packet = (RequestPacket) msg;

        System.out.println("channelRead0  " + packet.getId());
        System.out.println("channelRead0  " + packet.toString());
        if (packet.getId() == 0) {
            ctx.writeAndFlush(AuthStatus.builder().status(true).build());
        }

    }
/*    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestPacket msg) throws Exception {
        System.out.println("channelRead0  " + msg.getId());
        System.out.println("channelRead0  " + msg.toString());
        if (msg.getId() == 0) {
            ctx.writeAndFlush(AuthStatus.builder().status(true).build());
        }




    }*/


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        System.out.println("userEventTriggered");
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Registered " + ctx.channel().remoteAddress().toString());
        // ctx.writeAndFlush(AuthRequest.builder().msg("api-key").build());
        clientHandlers.put(ctx.channel().remoteAddress().toString(), ctx);
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Unregistered " + ctx.channel().remoteAddress().toString());
        clientHandlers.remove(ctx.channel().remoteAddress().toString());
    }

}