package com.acumendev.climatelogger.input.tcp;

import com.acumendev.climatelogger.input.tcp.dto.RequestPacketFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

public class RequestDecoder extends ReplayingDecoder<Void> {
    /*
        private final Charset charset = Charset.forName("UTF-8");*/
    private final RequestPacketFactory packetFactory;

    public RequestDecoder(RequestPacketFactory packetFactory) {
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

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
    }
}
