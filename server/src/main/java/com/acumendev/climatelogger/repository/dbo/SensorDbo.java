package com.acumendev.climatelogger.repository.dbo;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SensorDbo {
    private final long id;
    private final long userId;
    private final String name;
    private final int num;
    private final int type;
    private final String description;
    private final Long lastActiveDateTime;
    private final boolean state;
    private final String apiKey;
    private final long createTime;
}
