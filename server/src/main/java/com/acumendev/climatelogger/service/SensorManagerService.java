package com.acumendev.climatelogger.service;

import com.acumendev.climatelogger.api.dto.SensorCreateDto;
import com.acumendev.climatelogger.repository.SensorRepository;
import com.acumendev.climatelogger.repository.dbo.SensorDbo;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SensorManagerService {
    private final SensorRepository sensorRepository;
    private final Map<SensorDescriptor, SensorDbo> sensorsEnadled;

    public SensorManagerService(SensorRepository sensorRepository,
                                Map<SensorDescriptor, SensorDbo> sensorsEnadled) {
        this.sensorRepository = sensorRepository;
        this.sensorsEnadled = sensorsEnadled;
    }

    public void create(SensorCreateDto dto) {

    }
}
