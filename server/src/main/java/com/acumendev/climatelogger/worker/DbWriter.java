package com.acumendev.climatelogger.worker;

import com.acumendev.climatelogger.repository.NotifyQueueRepository;
import com.acumendev.climatelogger.repository.temperature.TemperatureReadingsRepository;
import com.acumendev.climatelogger.repository.temperature.dto.ReadingDbo;
import com.acumendev.climatelogger.utils.ThreadTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class DbWriter extends ThreadTemplate {

    private final NotifyQueueRepository notifyQueueRepository;
    private final TemperatureReadingsRepository readingsRepository;
    private final int batchSize;
    private final long flushTimeout;
    private final long idleTimeout;
    private long lastWrite;

    public DbWriter(NotifyQueueRepository notifyQueueRepository,
                    TemperatureReadingsRepository readingsRepository,
                    @Value("${write.sql.batch.size:10}") int batchSize,
                    @Value("${write.sql.flush.timeout:15000}") long flushTimeout,
                    @Value("${write.sql.idle.timeout:2000}") long idleTimeout) {
        this.notifyQueueRepository = notifyQueueRepository;
        this.readingsRepository = readingsRepository;
        this.batchSize = batchSize;
        this.flushTimeout = flushTimeout;
        this.idleTimeout = idleTimeout;
        this.lastWrite = System.currentTimeMillis();
    }

    @Override
    protected void work() {

        long nextWriteTime = lastWrite + flushTimeout;
        long queueSize = notifyQueueRepository.size();

        if (System.currentTimeMillis() < nextWriteTime && queueSize < batchSize || notifyQueueRepository.isQueueEmpty()) {
            log.debug("Условие записи не наступило");
            safeSleep(idleTimeout);
            return;
        }

        List<ReadingDbo> readingDbos = notifyQueueRepository.getBatch(batchSize);
        readingsRepository.addBatch(readingDbos);
        lastWrite = System.currentTimeMillis();
        log.debug("Записано в БД {} записей, в очереди {}", readingDbos.size(), queueSize);
        safeSleep(idleTimeout);
    }

    @Override
    protected void after() {
    }
}
