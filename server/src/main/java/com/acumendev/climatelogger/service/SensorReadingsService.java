package com.acumendev.climatelogger.service;


import com.acumendev.climatelogger.repository.SensorReadingsRepository;
import com.acumendev.climatelogger.repository.SensorRepository;
import org.springframework.stereotype.Service;

@Service
public class SensorReadingsService {

    private final SensorReadingsRepository sensorReadingsRepository;

    private final SensorRepository sensorRepository;

    public SensorReadingsService(SensorReadingsRepository sensorReadingsRepository, SensorRepository sensorRepository) {
        this.sensorReadingsRepository = sensorReadingsRepository;
        this.sensorRepository = sensorRepository;
    }
}
