package com.acumendev.climatelogger.utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BooleanSupplier;

public abstract class ThreadTemplate implements DisposableBean, InitializingBean, Runnable {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private final AtomicBoolean working = new AtomicBoolean(false);
    private final Thread thread = new Thread(this);
    private volatile long delayMillis;
    private volatile long initialDelayMillis;
    private volatile long workErrorDelayMillis = 5000L;

    public void startWork() {
        if (this.working.compareAndSet(false, true)) {
            thread.start();
        } else if (!thread.isInterrupted()) {
            this.logger.warn("Уже работает");
        }
    }

    public void stopWork() {
        this.working.set(false);
    }

    public boolean isWorking() {
        return this.working.get();
    }

    @Override
    public void afterPropertiesSet() {
        this.startWork();
        this.logger.info(" - Компонент запущен");
    }

    @Override
    public void destroy() {
        this.stopWork();

        try {
            thread.join();
        } catch (InterruptedException var2) {
            this.logger.error("Ошибка ожидания завершения потока.", var2);
        }

        this.logger.info(" - Компонент остановлен");
    }

    protected void setName(String name) {
        thread.setName(name);
    }

    @Override
    public void run() {
        try {
            try {
                this.before();
            } catch (Throwable var4) {
                this.logger.error("Ошибка на шаге before: {}", var4);
            }

            this.safeSleep(this.initialDelayMillis);

            while (this.working.get()) {
                try {
                    this.work();
                    this.safeSleep(this.delayMillis);
                } catch (Throwable var3) {
                    this.logger.error("Ошибка на шаге work: {}", var3);
                    this.safeSleep(this.workErrorDelayMillis);
                }
            }

            try {
                this.after();
            } catch (Throwable var2) {
                this.logger.error("Ошибка на шаге after: {}", var2);
            }
        } catch (Throwable var5) {
            this.logger.error("Ошибка: {}", var5);
            this.logger.error("Остановка потока.");
        }
    }


    public long getDelayMillis() {
        return this.delayMillis;
    }

    public void setDelayMillis(long delayMillis) {
        this.delayMillis = delayMillis;
    }

    public long getInitialDelayMillis() {
        return this.initialDelayMillis;
    }

    public void setInitialDelayMillis(long initialDelayMillis) {
        this.initialDelayMillis = initialDelayMillis;
    }

    public long getWorkErrorDelayMillis() {
        return this.workErrorDelayMillis;
    }

    public void setWorkErrorDelayMillis(long workErrorDelayMillis) {
        this.workErrorDelayMillis = workErrorDelayMillis;
    }

    protected void safeSleep(long millis) {
        safeSleep(millis, this::isWorking);
    }

    protected void safeSleep(long millis, BooleanSupplier supplier) {

        int step = 10;

        if (!thread.isInterrupted()) {
            try {
                long fullMillis = millis;

                while (fullMillis - step > 0 && supplier.getAsBoolean()) {
                    Thread.sleep(step);
                    fullMillis -= step;
                }

                Thread.sleep(fullMillis);
            } catch (InterruptedException var4) {
                this.logger.error("Ошибка прерывания сна Interrupted!", var4);
            }
        }
    }

    protected void before() {
    }


    protected abstract void after();


    protected abstract void work();
}