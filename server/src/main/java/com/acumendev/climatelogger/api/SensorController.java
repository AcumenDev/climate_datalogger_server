package com.acumendev.climatelogger.api;

import com.acumendev.climatelogger.api.dto.SensorDto;
import com.acumendev.climatelogger.api.dto.mapper.SensorDtoMapper;
import com.acumendev.climatelogger.config.CurrentUser;
import com.acumendev.climatelogger.repository.SensorRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@AllArgsConstructor
public class SensorController {
    private final SensorRepository sensorRepository;

    @GetMapping(path = "/api/sensors")
    public List<SensorDto> getSensors(@AuthenticationPrincipal CurrentUser user) {
        return sensorRepository.getAllByUserId(user.getId())
                .stream()
                .map(SensorDtoMapper::map)
                .collect(Collectors.toList());
    }
}
