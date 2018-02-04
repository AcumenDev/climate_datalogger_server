package com.acumendev.climatelogger.service;

import com.acumendev.climatelogger.api.dto.SensorCreateDto;
import com.acumendev.climatelogger.repository.SensorRepository;
import com.acumendev.climatelogger.repository.dbo.SensorDbo;
import com.acumendev.climatelogger.type.CurrentUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class SensorManagerService {
    private final SensorRepository sensorRepository;
    private final Map<SensorDescriptor, SensorDbo> sensorsEnadled;

    public SensorManagerService(SensorRepository sensorRepository,
                                Map<SensorDescriptor, SensorDbo> sensorsEnadled) {
        this.sensorRepository = sensorRepository;
        this.sensorsEnadled = sensorsEnadled;
    }

    public void create(CurrentUser user, SensorCreateDto dto) {
        SensorDbo sensorDbo = SensorDbo.builder()
                .userId(user.getId())
                .name(dto.getName())
                .type(dto.getType())
                .num(dto.getNum())
                .description(dto.getDescription())
                .state(dto.isState())
                .apiKey(UUID.randomUUID().toString())
                .createTime(System.currentTimeMillis())
                .build();

        SensorDbo dbo = sensorRepository.add(sensorDbo);

        if (dbo.isState()) {
            SensorDescriptor descriptor = SensorDescriptor.builder()
                    .apiKey(dbo.getApiKey())
                    .type(dbo.getType())
                    .build();
            sensorsEnadled.put(descriptor, dbo);
        }
    }
}
