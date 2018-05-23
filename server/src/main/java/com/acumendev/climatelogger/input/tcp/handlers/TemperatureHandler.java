package com.acumendev.climatelogger.input.tcp.handlers;

import com.acumendev.climatelogger.protocol.BaseMessageOuterClass;
import com.acumendev.climatelogger.protocol.TemperatureOuterClass;
import com.acumendev.climatelogger.repository.temperature.TemperatureReadingsAsyncRepository;
import com.acumendev.climatelogger.repository.SensorAsyncRepository;
import com.acumendev.climatelogger.repository.dbo.SensorDbo;
import com.acumendev.climatelogger.repository.temperature.TemperatureSettingRepository;
import com.acumendev.climatelogger.repository.temperature.dto.ReadingDbo;
import com.acumendev.climatelogger.repository.temperature.dto.TempSettingsDbo;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TemperatureHandler implements SensorHandler<BaseMessageOuterClass.BaseMessage> {

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
    public long getSensorId() {
        return sensorDbo.getId();
    }

    @Override
    public void disconnect() {

    }

    private void updateLastActiveTime() {
        sensorAsyncRepository.sensorActive(sensorDbo.getId(), System.currentTimeMillis());

    }

    @Override
    public void procces(BaseMessageOuterClass.BaseMessage msg) {

        if (!msg.hasTemperature()) {
            log.error("Не поддерживаемый тип пакета {}", msg);
            channel.close();
            return;
        }

        TemperatureOuterClass.Temperature temperature = msg.getTemperature();


        if (temperature.hasNotify()) {
            TemperatureOuterClass.Notify request = temperature.getNotify();
            ReadingDbo dbo = ReadingDbo.builder()
                    .sensorId(sensorDbo.getId())
                    .userId(sensorDbo.getUserId())
                    .timeStamp(System.currentTimeMillis())
                    .value(request.getCurrent())
                    .build();
            updateLastActiveTime();
            temperatureReadingsAsyncRepository.add(dbo);


        } else if (temperature.hasConfig()) {
            TemperatureOuterClass.Config config = temperature.getConfig();
            TempSettingsDbo settingsDbo = TempSettingsDbo.builder()
                    .sensorId(sensorDbo.getId())
                    .gisteris(config.getGisteris())
                    .target(config.getTarget())
                    .tuningSensor(config.getTuningSensor())
                    .build();
            updateLastActiveTime();
            settingRepository.update(settingsDbo);
        } else {
            log.error("Не поддерживаемый тип пакета {}", msg);
        }

    }
}