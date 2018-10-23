package com.acumendev.climatelogger.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class SensorDto {
    public final long id;
    public final long userId;
    public final String name;
    public final int num;
    public final int type;
    public final String description;
    public final boolean state;
    public final String apiKey;
    public final Long lastActiveTime;
    public final long createTime;

    public SensorDto(long id, long userId, String name, int num, int type, String description, boolean state, String apiKey, Long lastActiveTime, long createTime) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.num = num;
        this.type = type;
        this.description = description;
        this.state = state;
        this.apiKey = apiKey;
        this.lastActiveTime = lastActiveTime;
        this.createTime = createTime;
    }
}
