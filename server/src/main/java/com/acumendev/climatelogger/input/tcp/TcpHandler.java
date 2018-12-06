package com.acumendev.climatelogger.input.tcp;

import com.acumendev.climatelogger.input.AuthHandler;
import com.acumendev.climatelogger.input.tcp.handlers.SensorHandler;
import com.acumendev.climatelogger.protocol.BaseMessageOuterClass;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


public class TcpHandler extends SimpleChannelInboundHandler<BaseMessageOuterClass.BaseMessage> {

    private final Logger LOGGER = LoggerFactory.getLogger(TcpHandler.class);

    private final Map<String, SensorHandler<BaseMessageOuterClass.BaseMessage>> tcpSensorHandlers ;
    private final AuthHandler authHandler;

    TcpHandler(Map<String, SensorHandler<BaseMessageOuterClass.BaseMessage>> tcpSensorHandlers, AuthHandler authHandler) {
        this.tcpSensorHandlers = tcpSensorHandlers;
        this.authHandler = authHandler;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        String id = ctx.channel().id().asLongText();
        LOGGER.info("Registered {} {}", ctx.channel().remoteAddress().toString(), id);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) {
        String id = ctx.channel().id().asLongText();
        LOGGER.info("Unregistered {} {}", ctx.channel().remoteAddress().toString(), id);

        SensorHandler handler = tcpSensorHandlers.get(id);
        if (handler != null) {
            handler.disconnect();
            authHandler.unregistered(handler);
            tcpSensorHandlers.remove(id);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BaseMessageOuterClass.BaseMessage msg) {
        String id = ctx.channel().id().asLongText();
        LOGGER.debug("ChannelRead {}\n {}", id, msg.toString());

        if (tcpSensorHandlers.containsKey(id)) {
            tcpSensorHandlers.get(id).procces(msg);
            return;
        }

        if (msg.hasAuth()) {
            BaseMessageOuterClass.Auth authRequest = msg.getAuth();

            SensorHandler<BaseMessageOuterClass.BaseMessage> handler = authHandler.auth(ctx.channel(), id, authRequest);
            if (handler != null) {
                tcpSensorHandlers.put(id, handler);
                handler.init();
            } else {
                ctx.channel().close();
            }
        }
    }
}