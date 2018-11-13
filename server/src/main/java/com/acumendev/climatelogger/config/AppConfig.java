package com.acumendev.climatelogger.config;

import com.acumendev.climatelogger.input.tcp.handlers.SensorHandler;
import com.acumendev.climatelogger.repository.SensorRepository;
import com.acumendev.climatelogger.repository.dbo.SensorDbo;
import com.acumendev.climatelogger.service.SensorAuthDescriptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class AppConfig {

    /////todo Сенсоры включенные пользователями перенести в SensorServiceFactory назвать по лучше
    @Bean
    Map<SensorAuthDescriptor, SensorDbo> sensorsEnabled(SensorRepository repository) {
        List<SensorDbo> sensorList = repository.getEnabled();

        Map<SensorAuthDescriptor, SensorDbo> sensorDbos = new ConcurrentHashMap<>();

        sensorList.forEach(sensorDbo -> {
            SensorAuthDescriptor sensorAuthDescriptor =
                    new SensorAuthDescriptor(sensorDbo.apiKey, sensorDbo.type);

            sensorDbos.put(sensorAuthDescriptor, sensorDbo);
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