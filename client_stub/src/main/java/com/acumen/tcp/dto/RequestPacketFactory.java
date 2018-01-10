package com.acumen.tcp.dto;

import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RequestPacketFactory {
    public ResponsePacket getPacket(ByteBuf in) throws Exception {
        short id = in.readShort();
        switch (id) {
            case 1000: {
                return new AuthStatus().getFromBytes(in);
            }


            default: {

                throw new Exception(String.format("Не изветсный тип пакета id %d", id));
            }
        }
    }
}
