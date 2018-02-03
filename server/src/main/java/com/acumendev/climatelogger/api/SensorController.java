package com.acumendev.climatelogger.api;

import com.acumendev.climatelogger.api.dto.BaseResponse;
import com.acumendev.climatelogger.api.dto.SensorCreateDto;
import com.acumendev.climatelogger.api.dto.SensorDto;
import com.acumendev.climatelogger.api.dto.mapper.SensorDtoMapper;
import com.acumendev.climatelogger.repository.SensorRepository;
import com.acumendev.climatelogger.service.SensorManagerService;
import com.acumendev.climatelogger.type.CurrentUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@AllArgsConstructor
public class SensorController {
    private final SensorRepository sensorRepository;
    private final SensorManagerService sensorManager;

    @GetMapping(path = "/api/sensors")
    public List<SensorDto> getSensors(@AuthenticationPrincipal CurrentUser user) {
        return sensorRepository.getAllByUserId(user.getId())
                .stream()
                .map(SensorDtoMapper::map)
                .collect(Collectors.toList());
    }

    @PostMapping(path = "/api/sensors")
    public BaseResponse create(@RequestBody SensorCreateDto dto) {
        try {
            sensorManager.create(dto);
        } catch (Exception e) {
            log.error("Ошибка при создании датчика {} {}", dto, e);
            return BaseResponse.error(1, "Ошибка при создании датчика.");
        }
        return BaseResponse.ok();
    }
}
