package com.acumendev.climatelogger.service.sensor;


import com.acumendev.climatelogger.type.CurrentUser;
import com.acumendev.climatelogger.repository.temperature.TemperatureReadingsRepository;
import com.acumendev.climatelogger.repository.temperature.dto.ReadingDbo;
import com.acumendev.climatelogger.service.sensor.dto.TemperatureReadings;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TemperatureService implements SensorService<TemperatureReadings> {


    private final TemperatureReadingsRepository readingsRepository;


    @Override
    public int getType() {
        return 1;
    }

    @Override
    public TemperatureReadings getReadings(CurrentUser user, long sensorId) {

        List<ReadingDbo> readingDbos = readingsRepository.findByIdAndUserId(sensorId, user.getId());

        return TemperatureReadings.builder()
                .type(getType())
                .data(readingDbos.stream().map(readingDbo ->
                        TemperatureReadings.Data.builder()
                                .value(readingDbo.getValue())
                                .coolingState(readingDbo.isCoolingState())
                                .heatingState(readingDbo.isHeatingState())
                                .dateTime(readingDbo.getTimeStamp())
                                .build()

                ).collect(Collectors.toList()))
                .build();
    }


}
