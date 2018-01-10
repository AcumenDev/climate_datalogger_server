package com.acumen.tcp.dto;

import io.netty.buffer.ByteBuf;

public abstract class ResponsePacket<T> {
    public abstract short getId();

    abstract T getFromBytes(ByteBuf in);
}
