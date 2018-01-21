package com.acumendev.climatelogger.service;

import com.acumendev.climatelogger.input.SensorChannel;
import com.acumendev.climatelogger.input.tcp.TempNew;

public class SensorHandler {
    private SensorChannel channel;
    private int sensorType;
    private int version;


    public void setChannel(SensorChannel channel) {
        this.channel = channel;
    }

    public int getSensorType() {
        return sensorType;
    }

    public void setSensorType(int sensorType) {
        this.sensorType = sensorType;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void procces(TempNew.BaseMessage msg) {

    }
}
