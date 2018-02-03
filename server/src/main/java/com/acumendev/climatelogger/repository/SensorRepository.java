package com.acumendev.climatelogger.repository;

import com.acumendev.climatelogger.repository.dbo.SensorDbo;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
@AllArgsConstructor
public class SensorRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final String updateActiveTime = "UPDATE sensor SET last_active_date_time = :last_active_date_time WHERE id=:id;";
    private final String selectByLogin = "SELECT * FROM sensor WHERE user_id = :user_id;";

    private final String selectEnabled = "SELECT * FROM sensor WHERE state = TRUE;";


    public void updateActive(List<Long> sensorsIds) {
        Long time = System.currentTimeMillis();
        MapSqlParameterSource[] mapSqlParameterSource = new MapSqlParameterSource[sensorsIds.size()];
        for (int i = 0; i < sensorsIds.size(); i++) {
            Long item = sensorsIds.get(i);
            mapSqlParameterSource[i] = new MapSqlParameterSource()
                    .addValue("id", item)
                    .addValue("last_active_date_time", new Timestamp(time));
        }
        jdbcTemplate.batchUpdate(updateActiveTime, mapSqlParameterSource);
    }


    public void updateActive(long sensorsId, long time) {
        jdbcTemplate.update(updateActiveTime,
                new MapSqlParameterSource("id", sensorsId)
                        .addValue("last_active_date_time",
                                new Timestamp(time)));
    }

    public List<SensorDbo> getAllByUserId(long userId) {
        return jdbcTemplate.query(selectByLogin,
                new MapSqlParameterSource("user_id", userId),
                (rs, rowNum) -> build(rs));
    }

    private SensorDbo build(ResultSet rs) throws SQLException {
        return SensorDbo.builder()
                .id(rs.getLong("id"))
                .userId(rs.getLong("user_id"))
                .name(rs.getString("name"))
                .num(rs.getInt("num"))
                .type(rs.getInt("type"))
                .apiKey(rs.getString("api_key"))
                .description(rs.getString("description"))
                .lastActiveDateTime(
                        rs.getTimestamp("last_active_date_time") != null ?
                                rs.getTimestamp("last_active_date_time").getTime()
                                : null)
                .build();
    }

    public List<SensorDbo> getEnabled() {
        return jdbcTemplate.query(selectEnabled, (rs, rowNum) -> build(rs));
    }
}