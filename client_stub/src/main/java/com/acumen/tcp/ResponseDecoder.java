package com.acumen.tcp;

import com.acumen.tcp.dto.RequestPacketFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.nio.charset.Charset;
import java.util.List;

public class ResponseDecoder extends ReplayingDecoder<Enum> {

    private final RequestPacketFactory packetFactory;

    public ResponseDecoder(RequestPacketFactory packetFactory) {
        this.packetFactory = packetFactory;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        try {
            out.add(packetFactory.getPacket(in));
        } catch (Exception e) {
            throw new Exception(ctx.channel().remoteAddress().toString(), e);
        }
    }
}
