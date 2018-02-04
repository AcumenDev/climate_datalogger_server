package com.acumendev.climatelogger.repository.temperature;

import com.acumendev.climatelogger.repository.temperature.dto.TempSettingsDbo;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TemperatureSettingRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public TemperatureSettingRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void update(TempSettingsDbo settingsDbo){
       // jdbcTemplate.update("",)
    }
}
