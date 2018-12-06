package com.acumendev.climatelogger.service.sensors;

public enum SensorType {
    TEMPERATURE(1);

    public final int VALUE;

    SensorType(int i) {
        this.VALUE = i;
    }


    public static SensorType fromInt(int type) {
        for (SensorType sensorType : SensorType.values()) {
            if (sensorType.VALUE == type) {
                return sensorType;
            }
        }
        throw new RuntimeException("Не известный тип сенсора " + type);
    }
}
