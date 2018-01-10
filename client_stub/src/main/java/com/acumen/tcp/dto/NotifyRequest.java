package com.acumen.tcp.dto;

import lombok.Getter;
import lombok.Setter;

import java.nio.ByteBuffer;

@Getter
@Setter
public class NotifyRequest implements RequestPacket {
    private float current;
    private float target;

    private float gisteris;

    private boolean heating;
    private boolean cooling;

    @Override
    public short getId() {
        return 1;
    }

    @Override
    public byte[] getBytes() {
        return ByteBuffer.allocate(14)
                .putFloat(current)
                .putFloat(target)
                .putFloat(gisteris)
                .put((byte) (heating ? 1 : 0))
                .put((byte) (cooling ? 1 : 0))
                .array();
    }
}
