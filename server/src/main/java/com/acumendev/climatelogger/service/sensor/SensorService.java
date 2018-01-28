package com.acumendev.climatelogger.service.sensor;

import com.acumendev.climatelogger.api.dto.ReadingsDto;
import com.acumendev.climatelogger.config.CurrentUser;

public interface SensorService<T> {
    int getType();

    T getReadings(CurrentUser user, long sensorId);
}
