package com.acumendev.climatelogger.repository;

import com.acumendev.climatelogger.repository.temperature.dto.ReadingDbo;
import com.acumendev.climatelogger.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

@Slf4j
@Repository
@RequiredArgsConstructor
public class NotifyQueueRepository {
    private final BlockingQueue<ReadingDbo> notifyQueue;

    public void add(ReadingDbo dbo) {
        try {
            notifyQueue.add(dbo);
        } catch (Exception e) {
            log.error("Ошибка вставки {} в очередь запросов: {}", JsonUtils.dump(dbo), e);
        }
    }

    public List<ReadingDbo> getBatch(int batchSize) {
        List<ReadingDbo> readings = new ArrayList<>(batchSize);
        try {
            notifyQueue.drainTo(readings, batchSize);
        } catch (Exception e) {
            log.error("Ошибка чтения пачки, {} запсией, из очереди запросов: {}", batchSize, e);
        }
        return readings;
    }

    public long size() {
        return notifyQueue.size();
    }
}
