package com.acumendev.climatelogger.worker;

import com.acumendev.climatelogger.repository.NotifyQueueRepository;
import com.acumendev.climatelogger.repository.temperature.TemperatureReadingsRepository;
import com.acumendev.climatelogger.repository.temperature.dto.ReadingDbo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Component
public class DbWriter extends Thread {

    private final NotifyQueueRepository notifyQueueRepository;
    private final TemperatureReadingsRepository readingsRepository;
    private final int batchSize;
    private final long flushTimeout;
    private final long idleTimeout;
    private long lastWrite;

    public DbWriter(NotifyQueueRepository notifyQueueRepository,
                    TemperatureReadingsRepository readingsRepository,
                    @Value("${write.sql.batch.size:50}") int batchSize,
                    @Value("${write.sql.flush.timeout:30000}") long flushTimeout,
                    @Value("${write.sql.idle.timeout:5000}") long idleTimeout) {
        this.notifyQueueRepository = notifyQueueRepository;
        this.readingsRepository = readingsRepository;
        this.batchSize = batchSize;
        this.flushTimeout = flushTimeout;
        this.idleTimeout = idleTimeout;
        this.lastWrite = System.currentTimeMillis();
    }

    @PostConstruct
    public void init() {
        start();
    }

    @Override
    public void run() {

        if (System.currentTimeMillis() < (lastWrite + flushTimeout) && notifyQueueRepository.size() < batchSize) {
            log.debug("Время записи не наступило");
            safeSleep(idleTimeout);
        }

        List<ReadingDbo> readingDbos = notifyQueueRepository.getBatch(batchSize);
        readingsRepository.addBatch(readingDbos);
        lastWrite = System.currentTimeMillis();
        log.debug("Записано в БД {} записей", readingDbos.size());
        safeSleep(idleTimeout);

        super.run();
    }

    private void safeSleep(final long timeout) {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
