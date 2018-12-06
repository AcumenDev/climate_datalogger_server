package com.acumendev.climatelogger.api.dto.mapper;

import com.acumendev.climatelogger.api.dto.SensorDto;
import com.acumendev.climatelogger.repository.dbo.SensorDbo;

public class SensorDtoMapper {
    public static SensorDto map(SensorDbo dbo) {
        return new SensorDto(
                dbo.id,
                dbo.userId,
                dbo.name,
                dbo.num,
                dbo.type,
                dbo.description,
                dbo.state,
                dbo.apiKey,
                dbo.lastActiveDateTime,
                dbo.createTime);
    }
}
