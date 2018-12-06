package com.acumendev.climatelogger.service.dashboard;

import com.acumendev.climatelogger.api.dto.dashboardValue.BatchValuesDto;
import com.acumendev.climatelogger.api.dto.dashboardValue.DashboardValueItem;
import com.acumendev.climatelogger.service.sensors.SensorService;
import com.acumendev.climatelogger.service.sensors.SensorServiceFactory;
import com.acumendev.climatelogger.service.sensors.SensorType;
import com.acumendev.climatelogger.service.sensors.temperature.dto.TemperatureReadings;
import com.acumendev.climatelogger.type.CurrentUser;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardValueService { ////todo может другое название?

    private final SensorServiceFactory sensorServiceFactory;

    public DashboardValueService(SensorServiceFactory sensorServiceFactory) {
        this.sensorServiceFactory = sensorServiceFactory;
    }

    public Map<Integer, DashboardValueItem>  getItemsValue(CurrentUser user, BatchValuesDto batchValuesDto) {
        Map<Integer, DashboardValueItem> result = new HashMap<>(batchValuesDto.sensorIds.size());

        for (Integer sensorId : batchValuesDto.sensorIds) {
            SensorService sensorService = sensorServiceFactory.get(sensorId);
            SensorType sensorType = sensorServiceFactory.getSensorType(sensorId);
            TemperatureReadings readings = (TemperatureReadings) sensorService.getLastReading(user, sensorId);
            result.put(sensorId, new DashboardValueItem(sensorId, sensorType.VALUE, readings));
        }
        return result;
    }
}
