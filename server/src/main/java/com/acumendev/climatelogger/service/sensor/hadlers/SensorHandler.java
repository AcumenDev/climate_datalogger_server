package com.acumendev.climatelogger.service.sensor.hadlers;


import com.acumendev.climatelogger.input.tcp.TemperatureProtocol;
import io.netty.channel.Channel;

public interface SensorHandler {

    long getSensorId();

    void disconnect();

    void procces(TemperatureProtocol.BaseMessage msg);

    void init(Channel channel);
}
