package com.acumendev.climatelogger.input.tcp;

import com.acumendev.climatelogger.input.tcp.dto.ResponsePacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ResponseEncoder extends MessageToByteEncoder<ResponsePacket> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ResponsePacket msg, ByteBuf out) {
        out.writeBytes(msg.getBytes());
    }
}