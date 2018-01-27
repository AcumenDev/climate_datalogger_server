package com.acumendev.climatelogger.repository;

import com.acumendev.climatelogger.repository.dbo.SensorReadingsDbo;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
public class SensorReadingsRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final String insertReadings = "INSERT INTO sensor_readings (login, room, num, type, value, date_time) VALUES (:login, :room, :num, :type, :value, :date_time);";
    private final String selectReadings = "SELECT * FROM sensor_readings WHERE login =:login AND type=:type AND  date_time  BETWEEN :to  AND :from;";

    private final String queryFindByLoginAndType = "SELECT * FROM sensor_readings WHERE login =:login AND type=:type";

    public void saveBatch(List<SensorReadingsDbo> readingsDbos) {

        MapSqlParameterSource[] mapSqlParameterSource = new MapSqlParameterSource[readingsDbos.size()];
        for (int i = 0; i < readingsDbos.size(); i++) {
            SensorReadingsDbo item = readingsDbos.get(i);
            mapSqlParameterSource[i] = new MapSqlParameterSource()
                    .addValue("login", item.getLogin())
                    .addValue("room", item.getRoom())
                    .addValue("num", item.getNum())
                    .addValue("type", item.getType())
                    .addValue("value", item.getValue())
                    .addValue("date_time", new Timestamp(item.getDateTime()));
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

    public List<SensorReadingsDbo> findByLoginAndType(String login, Integer type) {

        return jdbcTemplate.query(queryFindByLoginAndType,
                new MapSqlParameterSource()
                        .addValue("login", login)
                        .addValue("type", type),
                (rs, rowNum) -> build(rs));

    }

    private  SensorReadingsDbo build(@NonNull ResultSet rs) throws SQLException {

        return SensorReadingsDbo.builder()
                .id(rs.getLong("id"))
                .login(rs.getString("login"))
                .room(rs.getInt("room"))
                .num(rs.getInt("num"))
                .type(rs.getInt("type"))
                .value(rs.getDouble("value"))
                .dateTime(rs.getTimestamp("date_time").getTime())
                .build();

    }
}
