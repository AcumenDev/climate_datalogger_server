package com.acumendev.climatelogger.repository.dbo;

public class DashboardItemDbo {

    public final long dashboardId;
    public final int sensorId;
    public final String data;

    public DashboardItemDbo(long dashboardId, int sensorId, String data) {
        this.dashboardId = dashboardId;
        this.sensorId = sensorId;
        this.data = data;
    }
}
