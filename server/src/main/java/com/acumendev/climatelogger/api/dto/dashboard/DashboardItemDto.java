package com.acumendev.climatelogger.api.dto.dashboard;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DashboardItemDto {

    public final int dashordId;
    public final int sensorId;
    public final String data;

    public DashboardItemDto(@JsonProperty("dashordId") int dashordId, @JsonProperty("sensorId") int sensorId, @JsonProperty("data") String data) {
        this.dashordId = dashordId;
        this.sensorId = sensorId;
        this.data = data;
    }
}
