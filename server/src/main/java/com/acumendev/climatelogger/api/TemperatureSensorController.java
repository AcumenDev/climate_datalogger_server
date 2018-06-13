package com.acumendev.climatelogger.api;

import com.acumendev.climatelogger.api.dto.BaseResponse;
import com.acumendev.climatelogger.api.dto.mapper.SensorDtoMapper;
import com.acumendev.climatelogger.repository.SensorRepository;
import com.acumendev.climatelogger.service.SensorManagerService;
import com.acumendev.climatelogger.type.CurrentUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TemperatureSensorController {
    private final SensorRepository sensorRepository;
    private final SensorManagerService sensorManager;

    public TemperatureSensorController(SensorRepository sensorRepository,
                                       SensorManagerService sensorManager) {
        this.sensorRepository = sensorRepository;
        this.sensorManager = sensorManager;
    }

    @GetMapping(path = "/api/sensors/temp/{id}")
    public BaseResponse getSensor(
            @AuthenticationPrincipal CurrentUser user,
            @PathVariable("id") long id) {
        return BaseResponse.ok(SensorDtoMapper.map(sensorRepository.getByIdAndUserId(id, user.getId())));
    }
}
