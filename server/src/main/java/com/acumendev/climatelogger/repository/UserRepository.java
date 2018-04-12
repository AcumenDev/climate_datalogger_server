package com.acumendev.climatelogger.repository;

import com.acumendev.climatelogger.repository.dbo.UserDbo;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class UserRepository {
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

        return UserDbo.builder()
                .id(rs.getLong("id"))
                .login(rs.getString("login"))
                .password(rs.getString("password"))
                .state(rs.getBoolean("state"))
                .build();
    }
}
