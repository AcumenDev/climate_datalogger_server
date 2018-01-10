package com.acumen.tcp.dto;

import io.netty.buffer.ByteBuf;
import lombok.Getter;

@Getter
public class AuthStatus extends ResponsePacket<AuthStatus> {

    boolean status;

    @Override
    public short getId() {
        return 1000;
    }

    @Override
    AuthStatus getFromBytes(ByteBuf in) {
        status = in.readBoolean();
        return this;
    }
}
