package com.acumendev.climatelogger.service.sensor;

import com.acumendev.climatelogger.type.CurrentUser;

public interface SensorService<T> {
    int getType();

    T getReadings(CurrentUser user, long sensorId);
}
