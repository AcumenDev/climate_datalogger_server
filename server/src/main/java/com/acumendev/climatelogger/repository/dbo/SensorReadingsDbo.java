package com.acumendev.climatelogger.repository.dbo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class SensorReadingsDbo {
    private final Long id;
    private final String login;
    private final Integer room;
    private final Integer num;
    private final Integer type;
    private final Double value;
    private final Long dateTime;
}
