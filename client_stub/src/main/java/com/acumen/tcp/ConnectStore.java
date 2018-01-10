package com.acumen.tcp;

import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Service;

@Service
public class ConnectStore {

    private ChannelHandlerContext ctx;

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }
}
