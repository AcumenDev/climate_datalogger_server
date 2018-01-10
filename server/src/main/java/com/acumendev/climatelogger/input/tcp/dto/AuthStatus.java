package com.acumendev.climatelogger.input.tcp.dto;

import lombok.Builder;
import lombok.Getter;

import java.nio.ByteBuffer;

@Getter
@Builder
public class AuthStatus implements ResponsePacket {

    boolean status;

    @Override
    public short getId() {
        return 1000;
    }


    @Override
    public byte[] getBytes() {
        return ByteBuffer.allocate(2).putShort(getId()).put((byte) (status ? 1 : 0)).array();
    }
}
