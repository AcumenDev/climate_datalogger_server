package com.acumendev.climatelogger.api.dto;

import java.util.List;


public class ReadingsDto {

    /* Integer room;
 Integer num;*/
    public final List<Data> data;
    public final String login;
    public final Integer type;

    public ReadingsDto(List<Data> data, String login, Integer type) {
        this.data = data;
        this.login = login;
        this.type = type;
    }

    public static class Data {
        public final Long id;
        public final String login;
        public final Integer room;
        public final Integer num;
        public final Integer type;
        public final Double value;
        public final Long dateTime;

        public Data(Long id, String login, Integer room, Integer num, Integer type, Double value, Long dateTime) {
            this.id = id;
            this.login = login;
            this.room = room;
            this.num = num;
            this.type = type;
            this.value = value;
            this.dateTime = dateTime;
        }
    }
}
