package com.acumendev.climatelogger.repository.dbo;

import java.util.List;

public class DashboardItemsGroup {
    public final long id;
    public final String name;
    public final List<DashboardItemDbo> items;

    public DashboardItemsGroup(long id, String name, List<DashboardItemDbo> items) {
        this.id = id;
        this.name = name;
        this.items = items;
    }
}
