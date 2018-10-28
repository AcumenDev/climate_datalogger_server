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
    private final static String GET_DASHBOARD = "SELECT * FROM dashboard WHERE dashboard_id =:dashboard_id AND user_Id=:user_id;";
    private final static String GET_DASHBOARD_ITEMS = "SELECT * FROM dashboard_item WHERE dashboard_id=:dashboardId;";
    private final static String INSERT_DASHBOARD = "INSERT INTO dashboard(id,name) VALUES(:id,:name);";
    private final static String INSERT_DASHBORD_ITEM = "INSERT INTO dashboard_item(dashboard_id,sensor_id,data) VALUES(:dashboard_id,:sensor_id,:data);";
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
                                new DashboardItemDbo(dashboardId, rs.getInt("sensor_id"), rs.getString("data"))
                );
    }

    public void add(DashboardDbo dashboardDbo) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", dashboardDbo.id);
        parameterSource.addValue("name", dashboardDbo.name);
        jdbcTemplate.update(INSERT_DASHBOARD, parameterSource);
    }

    public DashboardDbo get(long userId, int dashordId) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("user_id", userId)
                .addValue("dashord_id", dashordId);

        return jdbcTemplate.query(GET_DASHBOARD, parameterSource,
                rs -> {
                    if (rs.next()) {
                        return new DashboardDbo(rs.getLong("dashboard_id"), rs.getString("name"));
                    }
                    return null;
                });
    }

    public void addItem(DashboardItemDbo dashboardItemDbo) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("dashboard_id", dashboardItemDbo.dashboardId)
                .addValue("sensor_id", dashboardItemDbo.sensorId)
                .addValue("data", dashboardItemDbo.data);
        jdbcTemplate.update(INSERT_DASHBORD_ITEM, parameterSource);
    }
}