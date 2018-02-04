package com.acumendev.climatelogger.service.sensor.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TemperatureReadings {
    private final float value;
    private final boolean heatingState;
    private final boolean coolingState;
    private final Long dateTime;
}
