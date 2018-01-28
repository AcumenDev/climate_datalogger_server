package com.acumendev.climatelogger.input.tcp.handlers;


import com.acumendev.climatelogger.input.tcp.TemperatureProtocol;

public interface SensorHandler {

    long getSensorId();

    void disconnect();

    void procces(TemperatureProtocol.BaseMessage msg);

    void init();
}
