package com.acumendev.climatelogger.service.sensors.temperature;


import com.acumendev.climatelogger.repository.temperature.TemperatureReadingsRepository;
import com.acumendev.climatelogger.repository.temperature.dto.ReadingDbo;
import com.acumendev.climatelogger.service.sensors.SensorService;
import com.acumendev.climatelogger.service.sensors.temperature.dto.TemperatureReadings;
import com.acumendev.climatelogger.type.CurrentUser;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service

public class TemperatureService implements SensorService<List<TemperatureReadings>> {

    private final TemperatureReadingsRepository readingsRepository;

    public TemperatureService(TemperatureReadingsRepository readingsRepository) {
        this.readingsRepository = readingsRepository;
    }

    @Override
    public int getType() {
        return 1;
    }

    @Override
    public List<TemperatureReadings> getReadings(CurrentUser user, long sensorId) {

        List<ReadingDbo> readingDbos = readingsRepository.findByIdAndUserId(sensorId, user.getId());

        return readingDbos.stream()
                .map(readingDbo -> new TemperatureReadings(readingDbo.value, readingDbo.timeStamp))
                .collect(Collectors.toList());
    }

    @Override
    public List<TemperatureReadings> getReadings(CurrentUser user, long sensorId, long from, long to, int i) {
        List<ReadingDbo> readingDbos = readingsRepository.findByIdAndUserIdInInterval(sensorId, user.getId(), i, from, to);
        return readingDbos.stream()
                .map(readingDbo -> new TemperatureReadings(readingDbo.value, readingDbo.timeStamp))
                .collect(Collectors.toList());
    }


}
