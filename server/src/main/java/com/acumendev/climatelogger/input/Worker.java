package com.acumendev.climatelogger.input;

import com.acumendev.climatelogger.input.tcp.dto.AuthRequest;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Map;

@Component
public class Worker extends Thread {


    private final Map<String, ChannelHandlerContext> clientHandlers;


    private boolean state = true;

    public Worker(Map<String, ChannelHandlerContext> clientHandlers) {
        this.clientHandlers = clientHandlers;
    }

    @PostConstruct
    public void init() {
        setName(getClass().getSimpleName());
        start();
    }

    @Override
    public void run() {
        while (state) {
            clientHandlers.forEach((s, clientHandler) -> {

              //  clientHandler.writeAndFlush(AuthRequest.builder().msg("ping").build());

            });
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @PreDestroy
    public void des() {
        state = false;
    }
}
