package com.acumendev.climatelogger.config;

import com.acumendev.climatelogger.repository.SensorRepository;
import com.acumendev.climatelogger.repository.dbo.SensorDbo;
import com.acumendev.climatelogger.service.SensorDescriptor;
import com.acumendev.climatelogger.service.sensor.hadlers.SensorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class AppConfig {

    /////Сенсоры включенные пользователями
    @Bean
    Map<SensorDescriptor, SensorDbo> sensorsEnadled(SensorRepository repository) {
        List<SensorDbo> sensorList = repository.getEnabled();

        Map<SensorDescriptor, SensorDbo> sensorDbos = new ConcurrentHashMap<>();

        sensorList.forEach(sensorDbo -> {
            SensorDescriptor sensorDescriptor =
                    SensorDescriptor.builder()
                            .type(sensorDbo.getType())
                            .apiKey(sensorDbo.getApiKey())
                            .build();

            sensorDbos.put(sensorDescriptor, sensorDbo);
        });
        return sensorDbos;
    }



    /////Сенсоры с активными сессиями до оборудования
    @Bean
    Map<Long, SensorHandler> sensorsActiveSession() {
        return new ConcurrentHashMap<>();
    }

}
