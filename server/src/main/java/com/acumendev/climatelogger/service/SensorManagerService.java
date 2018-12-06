package com.acumendev.climatelogger.service;

import com.acumendev.climatelogger.api.dto.SensorCreateDto;
import com.acumendev.climatelogger.repository.SensorRepository;
import com.acumendev.climatelogger.repository.dbo.SensorDbo;
import com.acumendev.climatelogger.type.CurrentUser;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class SensorManagerService {
    private final SensorRepository sensorRepository;
    private final Map<SensorAuthDescriptor, SensorDbo> sensorsEnabled;

    public SensorManagerService(SensorRepository sensorRepository,
                                Map<SensorAuthDescriptor, SensorDbo> sensorsEnabled) {
        this.sensorRepository = sensorRepository;
        this.sensorsEnabled = sensorsEnabled;
    }

    public void create(CurrentUser user, SensorCreateDto dto) {
        SensorDbo sensorDbo = new SensorDbo(
                user.getId(),
                dto.name,
                dto.num,
                dto.type,
                dto.description,
                dto.state,
                UUID.randomUUID().toString(),
                System.currentTimeMillis());

        SensorDbo dbo = sensorRepository.add(sensorDbo);

        if (dbo.state) {
            sensorsEnabled.put(new SensorAuthDescriptor(dbo.apiKey, dbo.type), dbo);
        }
    }
}
