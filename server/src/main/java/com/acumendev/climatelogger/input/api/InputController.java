package com.acumendev.climatelogger.input.api;

import com.acumendev.climatelogger.input.api.dto.InputValueDto;
import com.acumendev.climatelogger.service.SensorReadingsService;
import com.acumendev.climatelogger.utils.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
public class InputController {

    private final SensorReadingsService sensorReadingsService;

    @PostMapping(path = "/api/input")
    public void inputData(@RequestBody InputValueDto body) {
        log.info(JsonUtils.dump(body));
        sensorReadingsService.addReadings(body);

    }
}
