package com.acumendev.climatelogger.input.tcp;

import io.netty.channel.ChannelHandlerContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class Config {

    @Bean
    Map<String, ChannelHandlerContext> clientHandlers() {
        return new ConcurrentHashMap<>();
    }

}
