package com.acumendev.climatelogger.repository.dbo.mapper;

import com.acumendev.climatelogger.repository.dbo.ActiveSensorDbo;
import com.acumendev.climatelogger.repository.dbo.SensorReadingsDbo;

public class ActiveSensorDboMapper {
    public static ActiveSensorDbo map(SensorReadingsDbo readingsDbo) {
        return ActiveSensorDbo.builder()
                .lastActiveDateTime(readingsDbo.getDateTime())
                .login(readingsDbo.getLogin())
                .num(readingsDbo.getNum())
                .room(readingsDbo.getRoom())
                .type(readingsDbo.getType())
                .build();
    }
}
