package com.acumendev.climatelogger.repository;

import com.acumendev.climatelogger.repository.dbo.DashboardDbo;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Created by vladimir akummail@gmail.com on 6/13/18.
 */
@Repository
@ParametersAreNonnullByDefault
public class DashboardRepository {
    private final static String GET_DEFAULT_DASHBOARD = "SELECT * FROM dashboard WHERE user_Id=:userId LIMIT 1;";
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public DashboardRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public DashboardDbo getDefault(long userId) {
        return jdbcTemplate.query(GET_DEFAULT_DASHBOARD, new MapSqlParameterSource("userId", userId),
                rs -> {
                    return new DashboardDbo(rs.getLong("dashboard_id"), rs.getString("name"));
                });

    }
}
