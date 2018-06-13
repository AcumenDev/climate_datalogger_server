package com.acumendev.climatelogger.repository.dbo;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;

/**
 * Created by vladimir akummail@gmail.com on 6/13/18.
 */
@Immutable
@ParametersAreNonnullByDefault
public class DashboardDbo {
    public final long id;
    public final String name;

    public DashboardDbo(long id, String name) {
        this.id = id;
        this.name = name;
    }
}
