package com.acumendev.climatelogger.api.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SensorDto {
    private final long id;
    private final long userId;
    private final String name;
    private final int num;
    private final int type;
    private final String description;
    private final boolean state;
    private final String apiKey;
    private final Long lastActiveTime;
}
