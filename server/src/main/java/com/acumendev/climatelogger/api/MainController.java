package com.acumendev.climatelogger.api;


import com.acumendev.climatelogger.api.dto.ReadingsDto;
import com.acumendev.climatelogger.repository.SensorReadingsRepository;
import com.acumendev.climatelogger.repository.dbo.SensorReadingsDbo;
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
public class MainController {

    private final SensorReadingsRepository sensorReadingsRepository;

    @GetMapping(path = "/api/readings")
    public ReadingsDto readings(
            @RequestParam("login") String login,
            @RequestParam("type") Integer type,
            @RequestParam("from") Long from,
            @RequestParam("to") Long to) {
        log.warn("{} {} {} {}", login, from, to, type);
        ReadingsDto readingsDto = new ReadingsDto();
        List<SensorReadingsDbo> dbos = sensorReadingsRepository.findByLoginAndTypeAndFromAndTo(login, type, from, to);
        readingsDto.setLogin(login);
        readingsDto.setType(type);
        List<ReadingsDto.Data> data = dbos
                .stream()
                .map(item ->
                        ReadingsDto.Data.builder()
                                .value(item.getValue())
                                .dateTime(item.getDateTime())
                                .build()).collect(Collectors.toList());
        readingsDto.setData(data);
        return readingsDto;
    }

    @GetMapping(path = "/api/readings/login")
    public ReadingsDto readingsByLogin(
            @RequestParam("login") String login,
            @RequestParam("type") Integer type) {
        ReadingsDto readingsDto = new ReadingsDto();
        List<SensorReadingsDbo> dbos = sensorReadingsRepository.findByLoginAndType(login, type);
        readingsDto.setLogin(login);
        readingsDto.setType(type);
        List<ReadingsDto.Data> data = dbos
                .stream()
                .map(item ->
                        ReadingsDto.Data.builder()
                                .id(item.getId())
                                .login(item.getLogin())
                                .num(item.getNum())
                                .room(item.getRoom())
                                .type(item.getType())
                                .value(item.getValue())
                                .dateTime(item.getDateTime())
                                .build()).collect(Collectors.toList());

        readingsDto.setData(data);
        return readingsDto;
    }
}
