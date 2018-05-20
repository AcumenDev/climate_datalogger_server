package com.acumendev.climatelogger.input.tcp;

import com.acumendev.climatelogger.input.tcp.handlers.SensorHandler;
import com.acumendev.climatelogger.protocol.BaseMessageOuterClass;
import com.acumendev.climatelogger.repository.temperature.dto.ReadingDbo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

@Configuration
public class Config {
    @Bean
    Map<String, SensorHandler<BaseMessageOuterClass.BaseMessage>> tcpSensorHandlers() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    BlockingQueue<ReadingDbo> notifyQueue() {
        return new LinkedBlockingQueue<>();
    }
}
