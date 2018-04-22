package com.acumendev.climatelogger.input.tcp.handlers;

import com.acumendev.climatelogger.protocol.BaseMessageOuterClass;
import com.acumendev.climatelogger.protocol.TemperatureOuterClass;
import com.acumendev.climatelogger.repository.SensorRepository;
import com.acumendev.climatelogger.repository.dbo.SensorDbo;
import com.acumendev.climatelogger.repository.temperature.TemperatureReadingsRepository;
import com.acumendev.climatelogger.repository.temperature.TemperatureSettingRepository;
import com.acumendev.climatelogger.repository.temperature.dto.ReadingDbo;
import com.acumendev.climatelogger.repository.temperature.dto.TempSettingsDbo;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TemperatureHandler implements SensorHandler<BaseMessageOuterClass.BaseMessage> {


    private final SensorDbo sensorDbo;
    private final TemperatureReadingsRepository readingsRepository;
    private final TemperatureSettingRepository settingRepository;
    private final SensorRepository sensorRepository;
    private final Channel channel;

    public TemperatureHandler(SensorDbo sensorDbo,
                              Channel channel,
                              TemperatureReadingsRepository readingsRepository,
                              TemperatureSettingRepository settingRepository,
                              SensorRepository sensorRepository) {

        this.sensorDbo = sensorDbo;
        this.channel = channel;
        this.readingsRepository = readingsRepository;
        this.settingRepository = settingRepository;

        this.sensorRepository = sensorRepository;
    }

    @Override
    public void init() {
//        channel.writeAndFlush(TemperatureProtocol.BaseMessage.newBuilder()
//                .setType(TemperatureProtocol.PacketType.getActualConfig).build());
    }

    @Override
    public long getSensorId() {
        return sensorDbo.getId();
    }

    @Override
    public void disconnect() {

    }

    private void updateLastActiveTime() {
        sensorRepository.updateActive(sensorDbo.getId(), System.currentTimeMillis());

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
            readingsRepository.add(dbo);
            updateLastActiveTime();
        } else if (temperature.hasConfig()) {
            TemperatureOuterClass.Config config = temperature.getConfig();
            TempSettingsDbo settingsDbo = TempSettingsDbo.builder()
                    .sensorId(sensorDbo.getId())
                    .gisteris(config.getGisteris())
                    .target(config.getTarget())
                    .tuningSensor(config.getTuningSensor())
                    .build();

            settingRepository.update(settingsDbo);
            updateLastActiveTime();
        } else {
            log.error("Не поддерживаемый тип пакета {}", msg);

        }
    }
}