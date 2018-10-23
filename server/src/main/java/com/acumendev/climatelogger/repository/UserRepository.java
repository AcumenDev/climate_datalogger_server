package com.acumendev.climatelogger.repository;

import com.acumendev.climatelogger.repository.dbo.UserDbo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class UserRepository {

    private final Logger LOGGER = LoggerFactory.getLogger(UserRepository.class);

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private String selectByLoginAndPasswd = "SELECT * FROM \"user\" WHERE login =:login AND password=:passwrd;";
    private String selectByLogin = "SELECT * FROM \"user\" WHERE login =:login;";


    public UserRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public UserDbo getUserByLoginAndPass(String login, String passwd) {
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("login", login);
        sqlParameterSource.addValue("passwrd", passwd);
        return jdbcTemplate.query(selectByLoginAndPasswd, sqlParameterSource, this::build);
    }

    public UserDbo getUserByLogin(String login) {
        return jdbcTemplate.queryForObject(selectByLogin,
                new MapSqlParameterSource("login", login),
                (rs, rowNum) -> build(rs)
        );
    }


    private UserDbo build(ResultSet rs) throws SQLException {
        return new UserDbo(
                rs.getString("login"),
                rs.getLong("id"),
                rs.getString("password"),
                rs.getBoolean("state"));
    }
}
