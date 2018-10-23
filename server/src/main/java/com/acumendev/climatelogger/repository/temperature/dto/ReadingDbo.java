package com.acumendev.climatelogger.repository.temperature.dto;


public class ReadingDbo {
    public final Long userId;
    public final Integer sensorId;
    public final float value;
    public final long timeStamp;

    public ReadingDbo(float value, long timeStamp) {  //// TODO Создать отдельный обьект ReadingShortDbo
        this(null, null, value, timeStamp);

    }

    public ReadingDbo(Long userId, Integer sensorId, float value, long timeStamp) {
        this.userId = userId;
        this.sensorId = sensorId;
        this.value = value;
        this.timeStamp = timeStamp;
    }
}
