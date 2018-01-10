package com.acumendev.climatelogger.input.tcp.dto;


import io.netty.buffer.ByteBuf;
import lombok.Getter;

import java.nio.charset.Charset;

@Getter
public class AuthRequest implements RequestPacket<AuthRequest> {

    private int type;
    private int room;
    private int version;
   // private String apiKey;

    @Override
    public short getId() {
        return 0;
    }

    @Override
    public AuthRequest readFromBytes(ByteBuf in) {
        type = in.readInt();
        room = in.readInt();
        version = in.readInt();
       // apiKey = in. readCharSequence(20, Charset.defaultCharset()).toString();
        return this;
    }

    @Override
    public String toString() {
        return "AuthRequest{" +
                "type=" + type +
                ", room=" + room +
                ", version=" + version +
               // ", apiKey='" + apiKey + '\'' +
                '}';
    }
}
