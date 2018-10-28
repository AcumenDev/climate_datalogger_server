package com.acumendev.climatelogger.api.dto.readings;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class BatchReadingsDto {

    public final Long timeFrom;
    public final Long timeTo;
    public final List<Integer> sensorIds;

    public BatchReadingsDto(
            @JsonProperty("timeFrom") Long timeFrom,
            @JsonProperty("timeTo") Long timeTo,
            @JsonProperty("sensorIds") List<Integer> sensorIds
    ) {
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.sensorIds = sensorIds;
    }
}
