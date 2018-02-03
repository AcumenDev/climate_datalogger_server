package com.acumendev.climatelogger.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BaseResponse<T> {
    private State state;
    private T data;

    public static <T> BaseResponse ok(T data) {
        return BaseResponse.<T>builder().data(data).state(State.builder().code(0).build()).build();
    }

    public static <T> BaseResponse ok() {
        return BaseResponse.<T>builder().state(State.builder().code(0).build()).build();
    }

    public static <T> BaseResponse error(int code, String description) {
        return BaseResponse.<T>builder().state(State.builder().code(code).description(description).build()).build();
    }


    @Getter
    @Setter
    @Builder
    public static class State {
        private int code;
        private String description;


    }
}
