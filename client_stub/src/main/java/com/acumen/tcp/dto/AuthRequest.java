package com.acumen.tcp.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.nio.ByteBuffer;

@Setter
@Getter
@Builder
public class AuthRequest implements RequestPacket {

    private short type;
    private short room;
    private short version;
   // private String apiKey;

    @Override
    public short getId() {
        return 0;
    }

    @Override
    public byte[] getBytes() {
        return ByteBuffer.allocate(8 ) //+ (apiKey.length())
                .putShort(getId())
                .putShort(type)
                .putShort(room)
                .putShort(version)
               // .put(apiKey.getBytes())
                .array();
    }
}
