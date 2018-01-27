package com.acumendev.climatelogger.service;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;


@Setter
@Getter
@Builder
public class SensorDescriptor {
    private String apiKey;
    private int type;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SensorDescriptor that = (SensorDescriptor) o;
        return type == that.type &&
                Objects.equals(apiKey, that.apiKey);
    }

    @Override
    public int hashCode() {

        return Objects.hash(apiKey, type);
    }
}
