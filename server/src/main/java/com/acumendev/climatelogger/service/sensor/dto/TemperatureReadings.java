package com.acumendev.climatelogger.service.sensor.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Builder
public class TemperatureReadings {
    private List<Data> data;
    private Integer type;

    @Getter
    @Setter
    @Builder
    public static class Data {

        private final float value;
        private final boolean heatingState;
        private final boolean coolingState;
        private final Long dateTime;
    }
}
