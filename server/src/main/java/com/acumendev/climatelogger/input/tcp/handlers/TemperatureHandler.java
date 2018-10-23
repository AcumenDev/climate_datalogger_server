package com.acumendev.climatelogger.input.tcp.handlers;

import com.acumendev.climatelogger.protocol.BaseMessageOuterClass;
import com.acumendev.climatelogger.protocol.TemperatureOuterClass;
import com.acumendev.climatelogger.repository.SensorAsyncRepository;
import com.acumendev.climatelogger.repository.dbo.SensorDbo;
import com.acumendev.climatelogger.repository.temperature.TemperatureReadingsAsyncRepository;
import com.acumendev.climatelogger.repository.temperature.TemperatureSettingRepository;
import com.acumendev.climatelogger.repository.temperature.dto.ReadingDbo;
import com.acumendev.climatelogger.repository.temperature.dto.TempSettingsDbo;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TemperatureHandler implements SensorHandler<BaseMessageOuterClass.BaseMessage> {

    private final Logger LOGGER = LoggerFactory.getLogger(TemperatureHandler.class);


    private final SensorDbo sensorDbo;
    private final TemperatureSettingRepository settingRepository;
    private final TemperatureReadingsAsyncRepository temperatureReadingsAsyncRepository;
    private final Channel channel;
    private final SensorAsyncRepository sensorAsyncRepository;

    public TemperatureHandler(SensorDbo sensorDbo,
                              Channel channel,
                              TemperatureSettingRepository settingRepository,
                              TemperatureReadingsAsyncRepository temperatureReadingsAsyncRepository,
                              SensorAsyncRepository sensorAsyncRepository) {

        this.sensorDbo = sensorDbo;
        this.channel = channel;
        this.settingRepository = settingRepository;
        this.temperatureReadingsAsyncRepository = temperatureReadingsAsyncRepository;
        this.sensorAsyncRepository = sensorAsyncRepository;
    }

    @Override
    public void init() {
        sendRequestGetActualConfig();
    }

    private void sendRequestGetActualConfig() {
        BaseMessageOuterClass.BaseMessage.Builder msg = BaseMessageOuterClass.BaseMessage.newBuilder();
        TemperatureOuterClass.Temperature.Builder temperature = TemperatureOuterClass.Temperature.newBuilder();
        temperature.setConfig(TemperatureOuterClass.Config.getDefaultInstance());
        msg.setTemperature(temperature);
        channel.write(msg.build());
    }

    @Override
    public int getSensorId() {
        return sensorDbo.id;
    }

    @Override
    public void disconnect() {

    }

    private void updateLastActiveTime() {
        sensorAsyncRepository.sensorActive(sensorDbo.id, System.currentTimeMillis());
    }

    @Override
    public void procces(BaseMessageOuterClass.BaseMessage msg) {

        if (!msg.hasTemperature()) {
            LOGGER.error("Не поддерживаемый тип пакета {}", msg);
            channel.close();
            return;
        }

        TemperatureOuterClass.Temperature temperature = msg.getTemperature();


        if (temperature.hasNotify()) {
            TemperatureOuterClass.Notify request = temperature.getNotify();
            updateLastActiveTime();
            temperatureReadingsAsyncRepository.add(new ReadingDbo(
                    sensorDbo.userId,
                    sensorDbo.id,
                    request.getCurrent(),
                    System.currentTimeMillis()));

        } else if (temperature.hasConfig()) {
            TemperatureOuterClass.Config config = temperature.getConfig();
            TempSettingsDbo settingsDbo = new TempSettingsDbo(
                    sensorDbo.id,
                    config.getGisteris(),
                    config.getTarget(),
                    config.getTuningSensor());
            updateLastActiveTime();
            settingRepository.update(settingsDbo);
        } else {
            LOGGER.error("Не поддерживаемый тип пакета {}", msg);
        }

    }
}