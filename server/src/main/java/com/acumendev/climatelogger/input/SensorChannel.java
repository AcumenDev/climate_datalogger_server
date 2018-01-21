package com.acumendev.climatelogger.input;

import com.acumendev.climatelogger.input.tcp.TempNew;
import com.acumendev.climatelogger.service.SensorHandler;
import io.netty.channel.Channel;

public class SensorChannel {

    private final AuthHandler authHandler;
    private final Channel channel;

    SensorHandler handler;

    public SensorChannel(AuthHandler authHandler, Channel channel) {
        this.authHandler = authHandler;
        this.channel = channel;
    }

    public void procces(TempNew.BaseMessage msg) {
        if (!authHandler.clientAuth(channel.id().asLongText())) {

            if (msg.getType() == TempNew.PacketType.authRequest) {
                handler = authHandler.auth(channel.id().asLongText(), msg.getAuthRequest());

                TempNew.BaseMessage baseMessage = TempNew.BaseMessage.newBuilder()
                        .setType(TempNew.PacketType.authResponse)
                        .setAuthResponse(TempNew.AuthResponse.newBuilder().setState(0).build())
                        .build();
                channel.writeAndFlush(baseMessage);
            }

        } else {
            handler.setChannel(this);
            handler.procces(msg);
        }
    }

    public void disconnect() {

        authHandler.disconnect(channel.id().asLongText());
    }
}
