package com.acumendev.climatelogger.api;

import com.acumendev.climatelogger.api.dto.SensorDto;
import com.acumendev.climatelogger.api.dto.mapper.SensorDtoMapper;
import com.acumendev.climatelogger.repository.SensorRepository;
import com.acumendev.climatelogger.repository.dbo.ActiveSensorDbo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@AllArgsConstructor
public class SensorController {
    private final SensorRepository sensorRepository;

    @GetMapping(path = "/api/sensors")
    public List<SensorDto> getSensors(@RequestParam("login") String login) {
        List<ActiveSensorDbo> sensorDbos = sensorRepository.getAllByLogin(login);
        return sensorDbos.stream().map(SensorDtoMapper::map).collect(Collectors.toList());
    }
}
