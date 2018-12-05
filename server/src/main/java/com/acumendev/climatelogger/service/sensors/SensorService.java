package com.acumendev.climatelogger.service.sensors;

import com.acumendev.climatelogger.type.CurrentUser;

import java.util.List;

public interface SensorService<T> {
    SensorType getType();

    List<T> getReadings(CurrentUser user, long sensorId);

    List<T> getReadings(CurrentUser user, long sensorId, long from, long to, int maxRecords);

    T getLastReading(CurrentUser user, long sensorId);
}
