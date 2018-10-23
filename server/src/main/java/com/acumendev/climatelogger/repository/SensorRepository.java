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

        // ( java.util.UUID ) rs.getObject( "api_key" ); rs.getObject("",UUID.class)

        Long lastActiveDateTime =
                rs.getTimestamp("last_active_date_time") != null ?
                        rs.getTimestamp("last_active_date_time").getTime() :
                        null;

        return new SensorDbo(
                rs.getInt("id"),
                rs.getLong("user_id"),
                rs.getString("name"),
                rs.getInt("num"),
                rs.getInt("type"),
                rs.getString("description"),
                lastActiveDateTime,
                rs.getBoolean("state"),
                rs.getString("api_key"),
                rs.getTimestamp("create_time").getTime());
    }

    public List<SensorDbo> getEnabled() {
        return jdbcTemplate.query(SELECT_ENABLED, (rs, rowNum) -> build(rs));
    }

    public SensorDbo add(SensorDbo sensorDbo) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("userId", sensorDbo.userId)
                .addValue("name", sensorDbo.name)
                .addValue("num", sensorDbo.num)
                .addValue("type", sensorDbo.type)
                .addValue("apiKey", UUID.fromString(sensorDbo.apiKey))
                .addValue("description", sensorDbo.description)
                .addValue("createTime", new Timestamp(sensorDbo.createTime))
                .addValue("state", sensorDbo.state);
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