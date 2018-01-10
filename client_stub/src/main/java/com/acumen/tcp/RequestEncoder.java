package com.acumen.tcp;

import com.acumen.tcp.dto.RequestPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class RequestEncoder extends MessageToByteEncoder<RequestPacket> {

    @Override
    protected void encode(ChannelHandlerContext ctx, RequestPacket msg, ByteBuf out) {
        out.writeBytes(msg.getBytes());
    }
}