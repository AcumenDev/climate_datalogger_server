package com.acumendev.climatelogger.repository.dbo;

public class DashboardItemDbo {

    public final long dashboardId;
    public final long sensorId;
    public final String data;

    public DashboardItemDbo(long dashboardId, long sensorId, String data) {
        this.dashboardId = dashboardId;
        this.sensorId = sensorId;
        this.data = data;
    }
}
