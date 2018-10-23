package com.acumendev.climatelogger.service.sensors.temperature.dto;


public class TemperatureReadings {
    public final float value;
    public final boolean heatingState;
    public final boolean coolingState;
    public final Long dateTime;

    public TemperatureReadings(float value, boolean heatingState, boolean coolingState, Long dateTime) {
        this.value = value;
        this.heatingState = heatingState;
        this.coolingState = coolingState;
        this.dateTime = dateTime;
    }

    public TemperatureReadings(float value, Long dateTime) {
        this.value = value;
        this.heatingState = false;
        this.coolingState = false;
        this.dateTime = dateTime;
    }
}
