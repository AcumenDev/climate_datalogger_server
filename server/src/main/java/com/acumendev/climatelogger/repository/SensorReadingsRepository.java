package com.acumendev.climatelogger.repository;

import com.acumendev.climatelogger.repository.dbo.SensorReadingsDbo;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class SensorReadingsRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final String insertReadings = "INSERT INTO sensor_readings (login, room, num, type, value, date_time) VALUES (:login, :room, :num, :type, :value, :date_time);";
    private final String selectReadings = "SELECT * FROM sensor_readings WHERE login =:login AND type=:type AND  date_time  BETWEEN :to  AND :from;";

    private final String queryFindByLoginAndType = "SELECT * FROM sensor_readings WHERE login =:login AND type=:type";

    public SensorReadingsRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveBatch(List<SensorReadingsDbo> readingsDbos) {

        MapSqlParameterSource[] mapSqlParameterSource = new MapSqlParameterSource[readingsDbos.size()];
        for (int i = 0; i < readingsDbos.size(); i++) {
            SensorReadingsDbo item = readingsDbos.get(i);
            mapSqlParameterSource[i] = new MapSqlParameterSource()
                    .addValue("login", item.login)
                    .addValue("room", item.room)
                    .addValue("num", item.num)
                    .addValue("type", item.type)
                    .addValue("value", item.value)
                    .addValue("date_time", new Timestamp(item.dateTime));
        }
        jdbcTemplate.batchUpdate(insertReadings, mapSqlParameterSource);
    }

    public List<SensorReadingsDbo> findByLoginAndTypeAndFromAndTo(String login, Integer type, Long from, Long to) {

        return jdbcTemplate.query(selectReadings,
                new MapSqlParameterSource("login", login)
                        .addValue("type", type)
                        .addValue("from", new Timestamp(from))
                        .addValue("to", new Timestamp(to)),
                (rs, rowNum) -> build(rs));

    }

    public List<SensorReadingsDbo> findByIdAndUserId(String login, Integer type) {

        return jdbcTemplate.query(queryFindByLoginAndType,
                new MapSqlParameterSource()
                        .addValue("login", login)
                        .addValue("type", type),
                (rs, rowNum) -> build(rs));

    }

    private SensorReadingsDbo build(@NonNull ResultSet rs) throws SQLException {

        return new SensorReadingsDbo(
                rs.getLong("id"),
                rs.getString("login"),
                rs.getInt("room"),
                rs.getInt("num"),
                rs.getInt("type"),
                rs.getDouble("value"),
                rs.getTimestamp("date_time").getTime());

    }
}
