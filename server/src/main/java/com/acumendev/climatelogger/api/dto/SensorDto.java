package com.acumendev.climatelogger.api.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SensorDto {


    private final String login;
    private final Integer room;
    private final Integer num;
    private final Integer type;
    private final Long lastActiveDateTime;
}
