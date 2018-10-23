package com.acumendev.climatelogger.api.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

public class SensorCreateDto {
    public final String name;
    public final int num;
    public final int type;
    public final String description;
    public final boolean state;

    public SensorCreateDto(
            @JsonProperty("name") String name,
            @JsonProperty("num") int num,
            @JsonProperty("type") int type,
            @JsonProperty("description") String description,
            @JsonProperty("state") boolean state) {
        this.name = name;
        this.num = num;
        this.type = type;
        this.description = description;
        this.state = state;
    }
}
