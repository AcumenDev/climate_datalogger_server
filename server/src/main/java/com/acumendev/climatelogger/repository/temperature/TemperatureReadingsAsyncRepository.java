package com.acumendev.climatelogger.repository.temperature;

import com.acumendev.climatelogger.repository.temperature.dto.ReadingDbo;
import com.acumendev.climatelogger.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

@Repository
public class TemperatureReadingsAsyncRepository {
    private final Logger LOGGER = LoggerFactory.getLogger(TemperatureReadingsAsyncRepository.class);
    private final BlockingQueue<ReadingDbo> notifyQueue;

    public TemperatureReadingsAsyncRepository(BlockingQueue<ReadingDbo> notifyQueue) {
        this.notifyQueue = notifyQueue;
    }

    public void add(ReadingDbo dbo) {
        try {
            notifyQueue.add(dbo);
        } catch (Exception e) {
            LOGGER.error("Ошибка вставки {} в очередь запросов к БД: {}", JsonUtils.dump(dbo), e);
        }
    }

    public List<ReadingDbo> getBatch(int batchSize) {
        List<ReadingDbo> readings = new ArrayList<>(batchSize);
        try {
            notifyQueue.drainTo(readings, batchSize);
        } catch (Exception e) {
            LOGGER.error("Ошибка чтения пачки, {} записей, из очереди запросов к БД: {}", batchSize, e);
        }
        return readings;
    }

    public long size() {
        return notifyQueue.size();
    }

    public boolean isQueueEmpty() {
        return size() == 0;
    }
}
