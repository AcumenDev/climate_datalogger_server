package com.acumendev.climatelogger.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SensorCreateDto {
    private String name;
    private int num;
    private int type;
    private String description;
    private boolean state;
    private String apiKey;
}
