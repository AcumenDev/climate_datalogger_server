package com.acumendev.climatelogger.repository;

import com.acumendev.climatelogger.repository.dbo.ActiveSensorDbo;
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
    private final String insertReadings = "INSERT INTO sensor (login, room, num, type,  last_active_date_time) VALUES (:login, :room, :num, :type, :last_active_date_time) ON CONFLICT (login, room, num, type) DO UPDATE SET last_active_date_time = EXCLUDED.last_active_date_time;";
    private final String selectByLogin = "SELECT * FROM sensor WHERE login = :login;";

    public void updateActive(List<ActiveSensorDbo> sensors) {
        MapSqlParameterSource[] mapSqlParameterSource = new MapSqlParameterSource[sensors.size()];
        for (int i = 0; i < sensors.size(); i++) {
            ActiveSensorDbo item = sensors.get(i);
            mapSqlParameterSource[i] = new MapSqlParameterSource()
                    .addValue("login", item.getLogin())
                    .addValue("room", item.getRoom())
                    .addValue("num", item.getNum())
                    .addValue("type", item.getType())
                    .addValue("last_active_date_time", new Timestamp(item.getLastActiveDateTime()));
        }
        jdbcTemplate.batchUpdate(insertReadings, mapSqlParameterSource);
    }

    public List<ActiveSensorDbo> getAllByLogin(String login) {
        return jdbcTemplate.query(selectByLogin,
                new MapSqlParameterSource("login", login),
                (rs, rowNum) -> build(rs));

    }

    private ActiveSensorDbo build(ResultSet rs) throws SQLException {
        return ActiveSensorDbo.builder()
                .login(rs.getString("login"))
                .room(rs.getInt("room"))
                .num(rs.getInt("num"))
                .type(rs.getInt("type"))
                .lastActiveDateTime(rs.getTimestamp("last_active_date_time").getTime())
                .build();

    }

}
