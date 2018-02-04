package com.acumendev.climatelogger.api;

import com.acumendev.climatelogger.api.dto.BaseResponse;
import com.acumendev.climatelogger.service.sensor.SensorService;
import com.acumendev.climatelogger.type.CurrentUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
public class ReadingsController {

    Map<Integer, SensorService> sensorsService;

    @GetMapping(path = "/api/readings")
    public BaseResponse readings(@AuthenticationPrincipal CurrentUser user,
                                 @RequestParam("sensor_id") long sensorId,
                                 @RequestParam("sensor_type") int sensorType,
                                 @RequestParam(value = "from", required = false) Long from,
                                 @RequestParam(value = "to", required = false) Long to) {
        //log.warn("{} {} {} {} {}", user.getId(),sensorId,sensorType, from, to);

        SensorService service = sensorsService.get(sensorType);
        return BaseResponse.ok(service.getReadings(user, sensorId));

    }
}
