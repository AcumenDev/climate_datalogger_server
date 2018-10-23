package com.acumendev.climatelogger.repository;

import com.acumendev.climatelogger.repository.dbo.DashboardDbo;
import com.acumendev.climatelogger.repository.dbo.DashboardItemDbo;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

/**
 * Created by vladimir akummail@gmail.com on 6/13/18.
 */
@Repository
@ParametersAreNonnullByDefault
public class DashboardRepository {
    private final static String GET_DEFAULT_DASHBOARD = "SELECT * FROM dashboard WHERE user_Id=:userId LIMIT 1;";
    private final static String GET_DASHBOARD_ITEMS = "SELECT * FROM dashboard_item WHERE dashboard_id=:dashboardId;";
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public DashboardRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public DashboardDbo getDefault(long userId) {
        return jdbcTemplate.query(GET_DEFAULT_DASHBOARD, new MapSqlParameterSource("userId", userId),
                rs -> {
                    if (rs.next()) {
                        return new DashboardDbo(rs.getLong("dashboard_id"), rs.getString("name"));
                    }
                    return null;
                });
    }

    public List<DashboardItemDbo> getItems(long dashboardId) {
        return jdbcTemplate
                .query(GET_DASHBOARD_ITEMS,
                        new MapSqlParameterSource("dashboardId", dashboardId),
                        (rs, rowNum) ->
                                new DashboardItemDbo(dashboardId, rs.getLong("sensor_id"), rs.getString("data"))
                );
    }
}