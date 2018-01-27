package com.acumendev.climatelogger.repository.temperature.dto;

import lombok.Builder;

@Builder
public class TempSettingsDbo {

    private long sensorId;
    private float target;
    private float gisteris;
    private float tuningSensor;
}
