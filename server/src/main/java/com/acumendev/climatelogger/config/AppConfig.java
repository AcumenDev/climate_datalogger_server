package com.acumendev.climatelogger.config;

import com.acumendev.climatelogger.input.tcp.handlers.SensorHandler;
import com.acumendev.climatelogger.repository.SensorRepository;
import com.acumendev.climatelogger.repository.dbo.SensorDbo;
import com.acumendev.climatelogger.service.SensorDescriptor;
import com.acumendev.climatelogger.service.sensors.SensorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class AppConfig {

    /////Сенсоры включенные пользователями
    @Bean
    Map<SensorDescriptor, SensorDbo> sensorsEnabled(SensorRepository repository) {
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


    @Bean
    Map<Integer, SensorService> sensorsService(List<SensorService> services) {
        return services.stream().collect(Collectors.toMap(SensorService::getType, Function.identity()));
    }
}
