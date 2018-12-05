package com.acumendev.climatelogger.api.dto.dashboard;

import com.acumendev.climatelogger.repository.dbo.SensorDbo;

import java.util.List;
import java.util.stream.Collectors;

public class DashboardDto {
    public final long id;
    public final String name;
    public final List<DashboardItem> items;

    public DashboardDto(long id, String name, List<SensorDbo> items) {
        this.id = id;
        this.name = name;
        this.items = items.stream()
                .map(sensorDbo -> new DashboardItem(sensorDbo.id, sensorDbo.type, sensorDbo.name))
                .collect(Collectors.toList());
    }

    private class DashboardItem {
        public final long id;
        public final int type;
        public final String name;

        private DashboardItem(long id, int type, String name) {
            this.id = id;
            this.type = type;
            this.name = name;
        }
    }
}
