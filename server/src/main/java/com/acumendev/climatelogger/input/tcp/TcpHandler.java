package com.acumendev.climatelogger.input.tcp;

import com.acumendev.climatelogger.input.AuthHandler;
import com.acumendev.climatelogger.input.SensorChannel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Map;

public class TcpHandler extends SimpleChannelInboundHandler<TempNew.BaseMessage> {

    private final Map<String, SensorChannel> clientHandlers;
    private final AuthHandler authHandler;

    TcpHandler(Map<String, SensorChannel> clientHandlers, AuthHandler authHandler) {
        this.clientHandlers = clientHandlers;
        this.authHandler = authHandler;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        System.out.println("Registered " + ctx.channel().remoteAddress().toString());
        System.out.println(" " + ctx.channel().id().asLongText());
        clientHandlers.put(ctx.channel().id().asLongText(), new SensorChannel(authHandler, ctx.channel()));
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) {
        System.out.println("Unregistered " + ctx.channel().remoteAddress().toString());
        String id = ctx.channel().id().asLongText();
        clientHandlers.get(id).disconnect();
        clientHandlers.remove(id);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TempNew.BaseMessage msg) {
        String id = ctx.channel().id().asLongText();
        System.out.println("channelRead0  \n" + msg.toString());
        clientHandlers.get(id).procces(msg);
    }
}