package com.acumen.tcp;


import com.acumendev.climatelogger.protocol.BaseMessageOuterClass;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Service;

import java.util.concurrent.LinkedBlockingQueue;

@Service
public class ConnectStore {

    final LinkedBlockingQueue<BaseMessageOuterClass.BaseMessage> queue = new LinkedBlockingQueue<>();

    private ChannelHandlerContext ctx;

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

}
