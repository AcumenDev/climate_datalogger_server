package com.acumendev.climatelogger.api.dto.mapper;

import com.acumendev.climatelogger.api.dto.SensorDto;
import com.acumendev.climatelogger.repository.dbo.SensorDbo;

public class SensorDtoMapper {
    public static SensorDto map(SensorDbo dbo) {
        return SensorDto.builder()
                .id(dbo.getId())
                .userId(dbo.getUserId())
                .name(dbo.getName())
                .num(dbo.getNum())
                .type(dbo.getType())
                .state(dbo.isState())
                .apiKey(dbo.getApiKey())
                .description(dbo.getDescription())
                .lastActiveTime(dbo.getLastActiveDateTime())
                .createTime(dbo.getCreateTime())
                .build();
    }
}
