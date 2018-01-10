package com.acumendev.climatelogger.input.tcp.dto;

import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RequestPacketFactory {
    public RequestPacket getPacket(ByteBuf in) throws Exception {
        short id = in.readShort();
        switch (id) {
            case 0: {
                return new AuthRequest().readFromBytes(in);
            }
            case 1: {
                return new NotifyRequest().readFromBytes(in);
            }

            default: {

                throw new Exception(String.format("Не изветсный тип пакета id %d", id));
            }
        }
    }
}
