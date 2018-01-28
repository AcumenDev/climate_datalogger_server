package com.acumendev.climatelogger.service;


import com.acumendev.climatelogger.repository.SensorReadingsRepository;
import com.acumendev.climatelogger.repository.SensorRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class SensorReadingsService {

    private final SensorReadingsRepository sensorReadingsRepository;

    private final SensorRepository sensorRepository;
}
