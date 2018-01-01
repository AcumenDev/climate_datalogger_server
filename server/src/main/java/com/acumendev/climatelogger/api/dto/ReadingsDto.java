package com.acumendev.climatelogger.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReadingsDto {
    List<Data> data;
    String login;
    Integer type;
   /* Integer room;
    Integer num;*/

    @Getter
    @Setter
    @Builder
    public static class Data {
        private final Long id;
        private final String login;
        private final Integer room;
        private final Integer num;
        private final Integer type;
        private final Double value;
        private final Long dateTime;
    }
}
