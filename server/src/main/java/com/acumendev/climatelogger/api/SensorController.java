package com.acumendev.climatelogger.api;

import com.acumendev.climatelogger.config.CurrentUser;
import com.acumendev.climatelogger.repository.SensorRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
public class SensorController {
    private final SensorRepository sensorRepository;

    @GetMapping(path = "/api/sensors")
    public Object getSensors(@AuthenticationPrincipal CurrentUser user) {
        //  List<SensorDbo> sensorDbos = sensorRepository.getAllByUserId(principal());
        //   return sensorDbos.stream().map(SensorDtoMapper::map).collect(Collectors.toList());

        return sensorRepository.getAllByUserId(user.getId());
        //  return new LinkedList<>();
    }
}
