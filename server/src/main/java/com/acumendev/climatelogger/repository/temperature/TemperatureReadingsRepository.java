package com.acumendev.climatelogger.repository.temperature;

import com.acumendev.climatelogger.repository.temperature.dto.ReadingDbo;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
public class TemperatureReadingsRepository {
    private final String insertReadings =
            "INSERT INTO sensor_temperature_readings (user_id, sensor_id,  value, cooling_state, heating_state,  date_time) VALUES (:user_id, :sensor_id, :value, :cooling_state, :heating_state, :date_time);";

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

}
