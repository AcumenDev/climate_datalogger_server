package com.acumendev.climatelogger.config;

import com.acumendev.climatelogger.service.SensorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class AppConfig {

    @Bean
    Map<String, SensorHandler> sensorHandlers() {
        Map<String, SensorHandler> map = new ConcurrentHashMap<>();


        SensorHandler handler = new SensorHandler();
        handler.setSensorType(1);
        handler.setVersion(1);
        map.put("12345678901234567890", handler);
        return map;


    }

}
