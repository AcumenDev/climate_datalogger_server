package com.acumendev.climatelogger.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
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
    private final long createTime;
}
