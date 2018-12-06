package com.acumendev.climatelogger.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<T> {
    public final Error error;
    public final T data;

    public BaseResponse(T data, Error error) {
        this.error = error;
        this.data = data;
    }

    public BaseResponse(Error error) {
        this.error = error;
        this.data = null;
    }

    public BaseResponse(T data) {
        this.error = null;
        this.data = data;
    }

    public BaseResponse() {
        this.error = null;
        this.data = null;
    }

    public static <T> BaseResponse ok(T data) {
        return new BaseResponse<>(data);
    }

    public static <T> BaseResponse ok() {
        return new BaseResponse<>();
    }

    public static <T> BaseResponse error(int code, String description) {
        return new BaseResponse<>(new Error(code, description));
    }


    @JsonInclude(JsonInclude.Include.NON_NULL)
    private static class Error {
        public final int code;
        public final String description;

        private Error(int code, String description) {
            this.code = code;
            this.description = description;
        }
    }
}
