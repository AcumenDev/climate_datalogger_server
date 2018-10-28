package com.acumendev.climatelogger.api;

import com.acumendev.climatelogger.api.dto.BaseResponse;
import com.acumendev.climatelogger.api.dto.SensorCreateDto;
import com.acumendev.climatelogger.api.dto.SensorDto;
import com.acumendev.climatelogger.api.dto.mapper.SensorDtoMapper;
import com.acumendev.climatelogger.repository.SensorRepository;
import com.acumendev.climatelogger.service.SensorManagerService;
import com.acumendev.climatelogger.type.CurrentUser;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Api
@RestController
public class SensorController {
    private final Logger LOGGER = LoggerFactory.getLogger(ReadingsController.class);
    private final SensorRepository sensorRepository;
    private final SensorManagerService sensorManager;

    public SensorController(SensorRepository sensorRepository, SensorManagerService sensorManager) {
        this.sensorRepository = sensorRepository;
        this.sensorManager = sensorManager;
    }

    @GetMapping(path = "/api/sensors")
    public List<SensorDto> getSensors(@AuthenticationPrincipal CurrentUser user) {
        return sensorRepository.getAllByUserId(user.getId())
                .stream()
                .map(SensorDtoMapper::map)
                .collect(Collectors.toList());
    }

    @PostMapping(path = "/api/sensors")
    public BaseResponse create(@AuthenticationPrincipal CurrentUser user, @RequestBody SensorCreateDto dto) {
        try {
            sensorManager.create(user, dto);
        } catch (Exception e) {
            LOGGER.error("Ошибка при создании датчика {} {}", dto, e);
            return BaseResponse.error(1, "Ошибка при создании датчика.");
        }
        return BaseResponse.ok();
    }

    @GetMapping(path = "/api/sensors/{id}")
    public BaseResponse getSensors(@AuthenticationPrincipal CurrentUser user, @PathVariable("id") long id) {
        return BaseResponse.ok(SensorDtoMapper.map(sensorRepository.getByIdAndUserId(id, user.getId())));
    }


}
