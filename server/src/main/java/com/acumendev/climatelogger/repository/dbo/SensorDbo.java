package com.acumendev.climatelogger.repository.dbo;


public class SensorDbo {
    public final Integer id;
    public final long userId;
    public final String name;
    public final int num;
    public final int type;
    public final String description;
    public final Long lastActiveDateTime;
    public final boolean state;
    public final String apiKey;
    public final long createTime;


    public SensorDbo(
            long userId,
            String name,
            int num,
            int type,
            String description,
            boolean state,
            String apiKey,
            long createTime) {
        this(null, userId, name, num, type, description, null, state, apiKey, createTime);
    }

    public SensorDbo(
            Integer id,
            long userId,
            String name,
            int num,
            int type,
            String description,
            Long lastActiveDateTime,
            boolean state,
            String apiKey,
            long createTime) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.num = num;
        this.type = type;
        this.description = description;
        this.lastActiveDateTime = lastActiveDateTime;
        this.state = state;
        this.apiKey = apiKey;
        this.createTime = createTime;
    }

}
