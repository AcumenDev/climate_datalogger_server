package com.acumendev.climatelogger.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<T> {
    private Error error;
    private T data;

    public static <T> BaseResponse ok(T data) {
        return BaseResponse.<T>builder().data(data).build();
    }

    public static <T> BaseResponse ok() {
        return BaseResponse.<T>builder().build();
    }

    public static <T> BaseResponse error(int code, String description) {
        return BaseResponse.<T>builder().error(Error.builder().code(code).description(description).build()).build();
    }


    @Getter
    @Setter
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private static class Error {
        private int code;
        private String description;
    }
}
