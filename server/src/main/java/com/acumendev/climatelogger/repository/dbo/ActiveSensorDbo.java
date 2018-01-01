package com.acumendev.climatelogger.repository.dbo;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ActiveSensorDbo {
    private final String login;
    private final Integer room;
    private final Integer num;
    private final Integer type;
    private final Long lastActiveDateTime;
}
