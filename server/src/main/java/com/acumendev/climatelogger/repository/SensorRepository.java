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
import java.util.UUID;

@Repository
@AllArgsConstructor
public class SensorRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
  private final String selectByLogin = "SELECT * FROM sensor WHERE user_id = :user_id;";

    private final String selectEnabled = "SELECT * FROM sensor WHERE state = TRUE;";
    private final String insertSensor = "INSERT INTO sensor (user_id, name, num, type, api_key, description,create_time, state) " +
            "VALUES (:userId, :name, :num, :type, :apiKey, :description, :createTime, :state) RETURNING * ;";

    private final String selectByIdAndUserId = "SELECT * FROM sensor WHERE id=:id AND user_id = :user_id;";




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
                .apiKey(rs.getString("api_key"))  // ( java.util.UUID ) rs.getObject( "api_key" ); rs.getObject("",UUID.class)
                .description(rs.getString("description"))
                .state(rs.getBoolean("state"))
                .createTime(rs.getTimestamp("create_time").getTime())
                .lastActiveDateTime(
                        rs.getTimestamp("last_active_date_time") != null ?
                                rs.getTimestamp("last_active_date_time").getTime()
                                : null)
                .build();
    }

    public List<SensorDbo> getEnabled() {
        return jdbcTemplate.query(selectEnabled, (rs, rowNum) -> build(rs));
    }

    public SensorDbo add(SensorDbo sensorDbo) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("userId", sensorDbo.getUserId())
                .addValue("name", sensorDbo.getName())
                .addValue("num", sensorDbo.getNum())
                .addValue("type", sensorDbo.getType())
                .addValue("apiKey", UUID.fromString(sensorDbo.getApiKey()))
                .addValue("description", sensorDbo.getDescription())
                .addValue("createTime", new Timestamp(sensorDbo.getCreateTime()))
                .addValue("state", sensorDbo.isState());
        return jdbcTemplate.queryForObject(insertSensor, parameterSource, (rs, rowNum) -> build(rs));
    }

    public SensorDbo getByIdAndUserId(long sensorsId, long userId) {
        return jdbcTemplate.queryForObject(selectByIdAndUserId,
                new MapSqlParameterSource()
                        .addValue("id", sensorsId)
                        .addValue("user_id", userId),
                (rs, rowNum) -> build(rs));


    }
}