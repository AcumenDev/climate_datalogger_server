package com.acumendev.climatelogger.api;

import com.acumendev.climatelogger.api.dto.BaseResponse;
import com.acumendev.climatelogger.service.sensors.SensorService;
import com.acumendev.climatelogger.type.CurrentUser;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.Map;


@RestController
public class ReadingsController {

    private final Logger LOGGER = LoggerFactory.getLogger(ReadingsController.class);
    private final Map<Integer, SensorService> sensorsService;

    public ReadingsController(Map<Integer, SensorService> sensorsService) {
        this.sensorsService = sensorsService;
    }

    @Timed(value = "api.readings", longTask = true)
    @GetMapping(path = "/api/readings")
    public BaseResponse readings(@AuthenticationPrincipal CurrentUser user,
                                 @RequestParam("sensor_id") long sensorId,
                                 @RequestParam("sensor_type") int sensorType,
                                 @RequestParam(value = "from", required = false) Long from,
                                 @RequestParam(value = "to", required = false) Long to) {
        LOGGER.warn("{} {} {} {} {}", user.getId(), sensorId, sensorType, from != null ? new Timestamp(from) : null, to != null ? new Timestamp(to) : null);

        SensorService service = sensorsService.get(sensorType);

        if (from != null) {
            return BaseResponse.ok(service.getReadings(user, sensorId, from, to, 500));
        }

        return BaseResponse.ok(service.getReadings(user, sensorId));

    }
}
