package com.acumendev.climatelogger.input.tcp.dto;

import io.netty.buffer.ByteBuf;
import lombok.Getter;

import java.nio.ByteBuffer;

@Getter
public class NotifyRequest implements RequestPacket<NotifyRequest> {
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
    public NotifyRequest readFromBytes(ByteBuf in) {

        current = in.readFloat();
        target = in.readFloat();
        gisteris = in.readFloat();
        heating = in.readBoolean();
        cooling = in.readBoolean();

        return this;
    }

    @Override
    public String toString() {
        return "NotifyRequest{" +
                "current=" + current +
                ", target=" + target +
                ", gisteris=" + gisteris +
                ", heating=" + heating +
                ", cooling=" + cooling +
                '}';
    }
}
