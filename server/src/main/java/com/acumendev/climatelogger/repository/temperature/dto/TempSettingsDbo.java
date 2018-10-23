package com.acumendev.climatelogger.repository.temperature.dto;


public class TempSettingsDbo {

    public final long sensorId;
    public final float target;
    public final float gisteris;
    public final float tuningSensor;

    public TempSettingsDbo(long sensorId, float target, float gisteris, float tuningSensor) {
        this.sensorId = sensorId;
        this.target = target;
        this.gisteris = gisteris;
        this.tuningSensor = tuningSensor;
    }
}
