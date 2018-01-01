package com.acumendev.climatelogger.api.dto.mapper;

import com.acumendev.climatelogger.api.dto.SensorDto;
import com.acumendev.climatelogger.repository.dbo.ActiveSensorDbo;

public class SensorDtoMapper {
    public static SensorDto map(ActiveSensorDbo dbo) {
        return SensorDto.builder()
                .login(dbo.getLogin())
                .num(dbo.getNum())
                .room(dbo.getRoom())
                .type(dbo.getType())
                .lastActiveDateTime(dbo.getLastActiveDateTime())
                .build();
    }
}
