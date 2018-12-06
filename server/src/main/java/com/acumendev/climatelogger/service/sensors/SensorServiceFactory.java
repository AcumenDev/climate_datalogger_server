package com.acumendev.climatelogger.service.sensors;

import com.acumendev.climatelogger.repository.SensorRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SensorServiceFactory {

    private final Map<SensorType, SensorService> sensorsService;

    private volatile Map<Integer, SensorType> sensorIdToSensorType = new HashMap<>();

    private SensorRepository sensorRepository;

    public SensorServiceFactory(List<SensorService> services, SensorRepository sensorRepository) {
        this.sensorsService = services.stream().collect(Collectors.toMap(SensorService::getType, Function.identity()));
        this.sensorRepository = sensorRepository;
    }

    @PostConstruct
    public void loadSensor() {
        List<AbstractMap.Entry<Integer, Integer>> list = sensorRepository.getAllIdToType();
        Map<Integer, SensorType> sensorPrepare = new HashMap<>();
        list.forEach(integerIntegerEntry ->
                sensorPrepare.put(integerIntegerEntry.getKey(), SensorType.fromInt(integerIntegerEntry.getValue())));

        sensorIdToSensorType = sensorPrepare;
    }

    public SensorType getSensorType(int sensorId) {
        return sensorIdToSensorType.get(sensorId);
    }

    public SensorService get(int sensorId) {
        return sensorsService.get(sensorIdToSensorType.get(sensorId));
    }
}