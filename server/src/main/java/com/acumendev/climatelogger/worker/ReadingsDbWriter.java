package com.acumendev.climatelogger.worker;

import com.acumendev.climatelogger.repository.temperature.TemperatureReadingsAsyncRepository;
import com.acumendev.climatelogger.repository.temperature.TemperatureReadingsRepository;
import com.acumendev.climatelogger.repository.temperature.dto.ReadingDbo;
import com.acumendev.climatelogger.utils.ThreadTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class ReadingsDbWriter extends ThreadTemplate {

    private final Logger LOGGER = LoggerFactory.getLogger(ReadingsDbWriter.class);

    ///todo подумать над обобщение
    private final TemperatureReadingsAsyncRepository temperatureReadingsAsyncRepository;
    private final TemperatureReadingsRepository readingsRepository;
    private final int batchSize;
    private final long flushTimeout;
    private final long idleTimeout;
    private long lastWrite;

    public ReadingsDbWriter(TemperatureReadingsAsyncRepository temperatureReadingsAsyncRepository,
                            TemperatureReadingsRepository readingsRepository,
                            @Value("${write.sql.batch.size:10}") int batchSize,
                            @Value("${write.sql.flush.timeout:5000}") long flushTimeout,
                            @Value("${write.sql.idle.timeout:2000}") long idleTimeout) {
        this.temperatureReadingsAsyncRepository = temperatureReadingsAsyncRepository;
        this.readingsRepository = readingsRepository;
        this.batchSize = batchSize;
        this.flushTimeout = flushTimeout;
        this.idleTimeout = idleTimeout;
        this.lastWrite = System.currentTimeMillis();
    }

    @Override
    protected void work() {

        long nextWriteTime = lastWrite + flushTimeout;
        long queueSize = temperatureReadingsAsyncRepository.size();

        if (System.currentTimeMillis() < nextWriteTime && queueSize < batchSize || temperatureReadingsAsyncRepository.isQueueEmpty()) {
            LOGGER.debug("Условие записи не наступило");
            safeSleep(idleTimeout);
            return;
        }

        List<ReadingDbo> readingDbos = temperatureReadingsAsyncRepository.getBatch(batchSize);
        readingsRepository.saveBatch(readingDbos);
        lastWrite = System.currentTimeMillis();
        LOGGER.debug("Записано в БД {} записей, в очереди {}", readingDbos.size(), queueSize);
        safeSleep(idleTimeout);
    }

    @Override
    protected void after() {
    }
}
