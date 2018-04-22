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
            "INSERT INTO sensor_temperature_readings (user_id, sensor_id,  value,  date_time) VALUES (:user_id, :sensor_id, :value, :date_time);";




/*
SELECT

  extract ('epoch' from   timestamp'2018-02-05 22:50:00.000000'- timestamp'2018-02-05 22:40:00.000000');



SELECT
  (date_trunc('seconds', (date_time - TIMESTAMPTZ 'epoch') / 300) * 300 + TIMESTAMPTZ 'epoch') AS time,
  avg(value) as VALUE ,
  count(1)
FROM sensor_temperature_readings
  WHERE date_time   BETWEEN '2018-02-03 21:20:00.000000'  AND '2018-02-05 22:50:00.000000'
GROUP BY time
ORDER BY time;


    */

    private final String selectSmartReadings = "" +
            "SELECT" +
            "  (date_trunc('seconds', (date_time - TIMESTAMPTZ 'epoch') / :bucketSize) * :bucketSize + TIMESTAMPTZ 'epoch') AS time," +
            "  avg(value) AS value," +
            "  count(1)" +
            "FROM sensor_temperature_readings" +
            "  WHERE date_time BETWEEN  :from  AND :to" +
            "  AND user_id = :user_id " +
            "  AND sensor_id = :sensor_id " +
            "GROUP BY 1 " +
            "ORDER BY 1";
    private final String selectReadings = "SELECT * FROM sensor_temperature_readings WHERE user_id=:user_id AND sensor_id =:sensor_id ORDER BY date_time;";// AND  date_time  BETWEEN :to  AND :from;";
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public TemperatureReadingsRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public void add(ReadingDbo dbo) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("user_id", dbo.getUserId())
                .addValue("sensor_id", dbo.getSensorId())
                .addValue("value", dbo.getValue())
                .addValue("date_time", new Timestamp(dbo.getTimeStamp()));
        jdbcTemplate.update(insertReadings, parameterSource);
    }

    private ReadingDbo build(@NonNull ResultSet rs) throws SQLException {

        return ReadingDbo.builder()
                .userId(rs.getLong("user_id"))
                .sensorId(rs.getInt("sensor_id"))
                .value(rs.getFloat("value"))
                .timeStamp(rs.getTimestamp("date_time").getTime())
                .build();

    }


    public List<ReadingDbo> findByIdAndUserIdInInterval(long sensorId, long userId, int size, long from, long to) {

        long bucketSize = (to - from) / 1000 / size;

        return jdbcTemplate.query(selectSmartReadings,
                new MapSqlParameterSource()
                        .addValue("bucketSize", bucketSize)
                        .addValue("bucketSize", bucketSize)
                        .addValue("to", new Timestamp(to))
                        .addValue("from", new Timestamp(from))
                        .addValue("user_id", userId)
                        .addValue("sensor_id", sensorId),
                (rs, rowNum) -> buildAgregation(rs));
    }

    private ReadingDbo buildAgregation(@NonNull ResultSet rs) throws SQLException {

        return ReadingDbo.builder()
                .value(rs.getFloat("value"))
                .timeStamp(rs.getTimestamp("time").getTime())
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
