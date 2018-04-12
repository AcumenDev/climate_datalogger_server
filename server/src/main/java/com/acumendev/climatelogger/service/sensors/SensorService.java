package com.acumendev.climatelogger.service.sensors;

import com.acumendev.climatelogger.type.CurrentUser;

public interface SensorService<T> {
    int getType();

    T getReadings(CurrentUser user, long sensorId);

    T getReadings(CurrentUser user, long sensorId, long from, long to, int i);
}
