package com.acumendev.climatelogger.service;


import com.acumendev.climatelogger.input.api.dto.InputValueDto;
import com.acumendev.climatelogger.repository.SensorReadingsRepository;
import com.acumendev.climatelogger.repository.SensorRepository;
import com.acumendev.climatelogger.repository.dbo.ActiveSensorDbo;
import com.acumendev.climatelogger.repository.dbo.SensorReadingsDbo;
import com.acumendev.climatelogger.repository.dbo.mapper.ActiveSensorDboMapper;
import com.acumendev.climatelogger.utils.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class SensorReadingsService {

    private final SensorReadingsRepository sensorReadingsRepository;

    private final SensorRepository sensorRepository;

    public void addReadings(InputValueDto body) {

        switch (body.getType()) {
            case 4: {
                long currentTime = System.currentTimeMillis();
                List<SensorReadingsDbo> readingsDbos = new ArrayList<>(3);

                readingsDbos.add(SensorReadingsDbo.builder()
                        .login(body.getUser())
                        .num(body.getNumber())
                        .room(body.getRommNumber())
                        .dateTime(currentTime)
                        .type(1)
                        .value(body.getData().getTemperature())
                        .build());
                readingsDbos.add(SensorReadingsDbo.builder()
                        .login(body.getUser())
                        .num(body.getNumber())
                        .room(body.getRommNumber())
                        .dateTime(currentTime)
                        .type(2)
                        .value(body.getData().getHumidity())
                        .build());
                readingsDbos.add(SensorReadingsDbo.builder()
                        .login(body.getUser())
                        .num(body.getNumber())
                        .room(body.getRommNumber())
                        .dateTime(currentTime)
                        .type(3)
                        .value(body.getData().getPressure())
                        .build());

                sensorReadingsRepository.saveBatch(readingsDbos);
                sensorRepository.updateActive(readingsDbos
                        .stream()
                        .map(ActiveSensorDboMapper::map)
                        .collect(Collectors.toList()));
                break;
            }
            default: {
                log.warn("Не известный тип датчика {}", JsonUtils.dump(body));
            }
        }
    }
}
