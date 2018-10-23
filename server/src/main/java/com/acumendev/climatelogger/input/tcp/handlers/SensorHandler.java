package com.acumendev.climatelogger.input.tcp.handlers;


public interface SensorHandler<T> {

    int getSensorId();

    void disconnect();

    void procces(T msg);

    void init();
}
