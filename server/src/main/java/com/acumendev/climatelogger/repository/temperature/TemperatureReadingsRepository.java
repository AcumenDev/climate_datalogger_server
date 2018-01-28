package com.acumendev.climatelogger.repository.temperature;

import com.acumendev.climatelogger.repository.temperature.dto.ReadingDbo;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class TemperatureReadingsRepository {
    private final String insertReadings =
            "INSERT INTO sensor_temperature_readings (user_id, sensor_id,  value, cooling_state, heating_state,  date_time) VALUES (:user_id, :sensor_id, :value, :cooling_state, :heating_state, :date_time);";


    private final String selectReadings = "SELECT * FROM sensor_temperature_readings WHERE user_id=:user_id AND sensor_id =:sensor_id;";// AND  date_time  BETWEEN :to  AND :from;";
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public TemperatureReadingsRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public void add(ReadingDbo dbo) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("user_id", dbo.getUserId())
                .addValue("sensor_id", dbo.getSensorId())
                .addValue("value", dbo.getValue())
                .addValue("cooling_state", dbo.isCoolingState())
                .addValue("heating_state", dbo.isHeatingState())
                .addValue("date_time", new Timestamp(dbo.getTimeStamp()));
        jdbcTemplate.update(insertReadings, parameterSource);
    }

    private ReadingDbo build(@NonNull ResultSet rs) throws SQLException {

        return ReadingDbo.builder()
                .userId(rs.getLong("user_id"))
                .sensorId(rs.getInt("sensor_id"))
                .value(rs.getFloat("value"))
                .heatingState(rs.getBoolean("heating_state"))
                .coolingState(rs.getBoolean("cooling_state"))
                .timeStamp(rs.getTimestamp("date_time").getTime())
                .build();

    }


    public List<ReadingDbo> findByIdAndUserId(long sensorId, long userId) {

        return jdbcTemplate.query(selectReadings,
                new MapSqlParameterSource()
                        .addValue("sensor_id", sensorId)
                        .addValue("user_id", userId),
                (rs, rowNum) -> build(rs));

    }

}
