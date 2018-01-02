package com.acumendev.climatelogger.api.dto.mapper;

import com.acumendev.climatelogger.api.dto.SensorDto;
import com.acumendev.climatelogger.repository.dbo.ActiveSensorDbo;

public class SensorDtoMapper {
    public static SensorDto map(ActiveSensorDbo dbo) {
        return SensorDto.builder()
                .num(dbo.getNum())
                .room(dbo.getRoom())
                .type(dbo.getType())
                .lastActiveTime(dbo.getLastActiveDateTime())
                .build();
    }
}
