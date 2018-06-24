package com.acumendev.climatelogger.repository;

import com.acumendev.climatelogger.repository.dbo.SensorDbo;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Repository
public class SensorRepository {
    private static final String SELECT_BY_LOGIN_AND_IDS = "SELECT * FROM sensor WHERE user_id = :user_id and id in (:ids);";
    private static final String SELECT_BY_LOGIN = "SELECT * FROM sensor WHERE user_id = :user_id;";
    private static final String SELECT_ENABLED = "SELECT * FROM sensor WHERE state = TRUE;";
    private static final String INSERT_SENSOR = "INSERT INTO sensor (user_id, name, num, type, api_key, description,create_time, state) " +
            "VALUES (:userId, :name, :num, :type, :apiKey, :description, :createTime, :state) RETURNING * ;";
    private static final String SELECT_BY_ID_AND_USER_ID = "SELECT * FROM sensor WHERE id=:id AND user_id = :user_id;";


    private final NamedParameterJdbcTemplate jdbcTemplate;

    public SensorRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<SensorDbo> getAllByUserId(long userId) {
        return jdbcTemplate.query(SELECT_BY_LOGIN,
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
        return jdbcTemplate.query(SELECT_ENABLED, (rs, rowNum) -> build(rs));
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
        return jdbcTemplate.queryForObject(INSERT_SENSOR, parameterSource, (rs, rowNum) -> build(rs));
    }

    public SensorDbo getByIdAndUserId(long sensorsId, long userId) {
        return jdbcTemplate.queryForObject(SELECT_BY_ID_AND_USER_ID,
                new MapSqlParameterSource()
                        .addValue("id", sensorsId)
                        .addValue("user_id", userId),
                (rs, rowNum) -> build(rs));
    }

    public List<SensorDbo> getByIds(long userId, List<Long> sensorIds) {
        return jdbcTemplate.query(SELECT_BY_LOGIN_AND_IDS,
                new MapSqlParameterSource("user_id", userId).addValue("ids", sensorIds),
                (rs, rowNum) -> build(rs));
    }
}