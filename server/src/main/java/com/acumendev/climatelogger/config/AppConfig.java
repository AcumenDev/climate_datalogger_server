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
                    new SensorDescriptor(sensorDbo.apiKey, sensorDbo.type);

            sensorDbos.put(sensorDescriptor, sensorDbo);
        });
        return sensorDbos;
    }

    /////todo Сенсоры с активными сессиями до оборудования, вынести в отдельный класс манаджер сессий,
    // запись их в бд, вести там лог продолжительности сисий вохможно время ответа на команды
    @Bean
    Map<Integer, SensorHandler> sensorsActiveSession() {
        return new ConcurrentHashMap<>();
    }
}