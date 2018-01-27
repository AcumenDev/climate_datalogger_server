package com.acumendev.climatelogger.repository.dbo.mapper;

import com.acumendev.climatelogger.repository.dbo.SensorDbo;
import com.acumendev.climatelogger.repository.dbo.SensorReadingsDbo;

public class ActiveSensorDboMapper {
    public static SensorDbo map(SensorReadingsDbo readingsDbo) {
        return SensorDbo.builder()
                .lastActiveDateTime(readingsDbo.getDateTime())
               // .login(readingsDbo.getLogin())
                .num(readingsDbo.getNum())
               // .room(readingsDbo.getRoom())
                .type(readingsDbo.getType())
                .build();
    }
}
