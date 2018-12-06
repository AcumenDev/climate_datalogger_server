package com.acumendev.climatelogger.repository.dbo;


public class SensorReadingsDbo {
    public final Long id;
    public final String login;
    public final Integer room;
    public final Integer num;
    public final Integer type;
    public final Double value;
    public final Long dateTime;

    public SensorReadingsDbo(Long id, String login, Integer room, Integer num, Integer type, Double value, Long dateTime) {
        this.id = id;
        this.login = login;
        this.room = room;
        this.num = num;
        this.type = type;
        this.value = value;
        this.dateTime = dateTime;
    }
}
