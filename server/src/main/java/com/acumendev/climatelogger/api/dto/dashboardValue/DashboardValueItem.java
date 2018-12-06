package com.acumendev.climatelogger.api.dto.dashboardValue;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DashboardValueItem {
    @JsonProperty("sensorId")
    public final int sensorId;

    @JsonProperty("sensorType")
    public final int sensorType;

    @JsonProperty("data")
    public final Object data;

    public DashboardValueItem(
            @JsonProperty("sensorId") int sensorId,
            @JsonProperty("sensorType") int sensorType,
            @JsonProperty("data") Object data) {
        this.sensorId = sensorId;
        this.sensorType = sensorType;
        this.data = data;
    }
}
