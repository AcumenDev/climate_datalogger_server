package com.acumendev.climatelogger.input.tcp.dto;

import io.netty.buffer.ByteBuf;

public interface RequestPacket<T> {
    short getId();

    T readFromBytes(ByteBuf in);
}
