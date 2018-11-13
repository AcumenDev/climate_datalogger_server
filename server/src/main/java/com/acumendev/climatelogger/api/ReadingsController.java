package com.acumendev.climatelogger.api;

import com.acumendev.climatelogger.api.dto.BaseResponse;
import com.acumendev.climatelogger.api.dto.readings.BatchReadingsDto;
import com.acumendev.climatelogger.api.dto.readings.ShortReadingsDto;
import com.acumendev.climatelogger.service.sensors.SensorService;
import com.acumendev.climatelogger.service.sensors.SensorServiceFactory;
import com.acumendev.climatelogger.type.CurrentUser;
import com.acumendev.climatelogger.utils.SecurityUtils;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Api
@RestController
public class ReadingsController {

    private final Logger LOGGER = LoggerFactory.getLogger(ReadingsController.class);
    private final SensorServiceFactory sensorServiceFactory;

    public ReadingsController(SensorServiceFactory sensorServiceFactory) {
        this.sensorServiceFactory = sensorServiceFactory;
    }

    @Timed(value = "api.readings", longTask = true)
    @GetMapping(path = "/api/readings")
    public BaseResponse readings(
            @RequestParam("sensor_id") int sensorId,
            @RequestParam("sensor_type") int sensorType,
            @RequestParam(value = "from", required = false) Long from,
            @RequestParam(value = "to", required = false) Long to) {
        CurrentUser user = SecurityUtils.getUser();
        LOGGER.warn("{} {} {} {} {}", user.getId(), sensorId, sensorType, from != null ? new Timestamp(from) : null, to != null ? new Timestamp(to) : null);
        SensorService service = sensorServiceFactory.get(sensorId);
        if (from != null && to != null) {
            return BaseResponse.ok(service.getReadings(user, sensorId, from, to, 500));
        }
        return BaseResponse.ok(service.getReadings(user, sensorId));
    }

    @PostMapping("/api/readings/batch")
    public BaseResponse getReadingBatch(@RequestBody BatchReadingsDto batchReadingsDto) {
        CurrentUser user = SecurityUtils.getUser();
        ConcurrentHashMap<Integer, Object> sensorReadinsResult = new ConcurrentHashMap<>();
        batchReadingsDto
                .sensorIds
                .parallelStream()
                .forEach(sensorId -> {
                    SensorService service = sensorServiceFactory.get(sensorId);
                    sensorReadinsResult.put(sensorId, service.getReadings(user, sensorId, batchReadingsDto.timeFrom, batchReadingsDto.timeTo, 500));
                });

        return BaseResponse.ok(sensorReadinsResult);
    }
}