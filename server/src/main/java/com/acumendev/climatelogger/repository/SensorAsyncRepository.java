package com.acumendev.climatelogger.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

@Slf4j
@Repository
@AllArgsConstructor
public class SensorAsyncRepository {
    private final String updateActiveTime = "UPDATE sensor SET last_active_date_time = :last_active_date_time WHERE id=:id;";
    private final NamedParameterJdbcTemplate jdbcTemplate;


    private final BlockingQueue<SensorActiveEvent> sensorsIdsQueue = new LinkedBlockingQueue<>();


    public void sensorActive(long id, long time) {
        sensorsIdsQueue.add(new SensorActiveEvent(id, time));
    }

    private void updateActive(List<SensorActiveEvent> sensorsIds) {
        MapSqlParameterSource[] mapSqlParameterSource = new MapSqlParameterSource[sensorsIds.size()];
        for (int i = 0; i < sensorsIds.size(); i++) {
            SensorActiveEvent item = sensorsIds.get(i);
            mapSqlParameterSource[i] = new MapSqlParameterSource()
                    .addValue("id", item.getId())
                    .addValue("last_active_date_time", new Timestamp(item.getTime()));
        }
        jdbcTemplate.batchUpdate(updateActiveTime, mapSqlParameterSource);
    }

    @Scheduled(fixedRate = 2000)
    private void flush() {
        List<SensorActiveEvent> ids = new ArrayList<>();
        sensorsIdsQueue.drainTo(ids);
        List<SensorActiveEvent> distinctIds = ids.stream().distinct().collect(Collectors.toList());

        if (!distinctIds.isEmpty()) {
            log.debug("Запись в бд времени последней активности сенсоров {}", distinctIds);
            try {
                updateActive(distinctIds);
            } catch (Exception e) {
                log.error("Ошибка записи в бд, времени последней активности сенсоров {}", distinctIds);

            }
        }
    }

    private static class SensorActiveEvent {
        private final long id;
        private final long time;

        SensorActiveEvent(long id, long time) {
            this.id = id;
            this.time = time;
        }

        public long getId() {
            return id;
        }

        public long getTime() {
            return time;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SensorActiveEvent that = (SensorActiveEvent) o;
            return id == that.id;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, time);
        }
    }
}
