package com.acumendev.climatelogger.service;

import java.util.Objects;

public class SensorAuthDescriptor {

    final public String apiKey;
    final public int type;

    public SensorAuthDescriptor(String apiKey, int type) {
        this.apiKey = apiKey;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SensorAuthDescriptor that = (SensorAuthDescriptor) o;
        return type == that.type &&
                Objects.equals(apiKey, that.apiKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(apiKey, type);
    }

}
