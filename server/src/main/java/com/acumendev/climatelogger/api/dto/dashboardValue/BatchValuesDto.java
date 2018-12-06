package com.acumendev.climatelogger.api.dto.dashboardValue;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class BatchValuesDto {
    @JsonProperty("sensorIds")
    public final List<Integer> sensorIds;

    public BatchValuesDto(@JsonProperty("sensorIds") List<Integer> sensorIds) {
        this.sensorIds = sensorIds;
    }
}
