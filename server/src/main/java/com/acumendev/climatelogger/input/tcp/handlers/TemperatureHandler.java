package com.acumendev.climatelogger.input.tcp.handlers;

import com.acumendev.climatelogger.input.tcp.TemperatureProtocol;
import com.acumendev.climatelogger.repository.dbo.SensorDbo;
import com.acumendev.climatelogger.repository.temperature.TemperatureReadingsRepository;
import com.acumendev.climatelogger.repository.temperature.TemperatureSettingRepository;
import com.acumendev.climatelogger.repository.temperature.dto.ReadingDbo;
import com.acumendev.climatelogger.repository.temperature.dto.TempSettingsDbo;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TemperatureHandler implements SensorHandler {


    private final SensorDbo sensorDbo;
    private final TemperatureReadingsRepository readingsRepository;
    private final TemperatureSettingRepository settingRepository;
    private final Channel channel;

    public TemperatureHandler(SensorDbo sensorDbo,
                              Channel channel,
                              TemperatureReadingsRepository readingsRepository,
                              TemperatureSettingRepository settingRepository) {

        this.sensorDbo = sensorDbo;
        this.channel = channel;
        this.readingsRepository = readingsRepository;
        this.settingRepository = settingRepository;

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

    @Override
    public void procces(TemperatureProtocol.BaseMessage msg) {
        switch (msg.getType()) {
            case notifyRequest: {
                TemperatureProtocol.NotifyRequest request = msg.getNotifyRequest();
                ReadingDbo dbo = ReadingDbo.builder()
                        .sensorId(sensorDbo.getId())
                        .userId(sensorDbo.getUserId())
                        .timeStamp(System.currentTimeMillis())
                        .value(request.getCurrent())
                        .coolingState(request.getCoolingState() == 1)
                        .heatingState(request.getHeatingState() == 1)
                        .build();
                readingsRepository.add(dbo);
                channel.writeAndFlush(TemperatureProtocol.BaseMessage.newBuilder().setType(TemperatureProtocol.PacketType.notifyResponse).build());
                return;
            }

            case actualConfig: {
                TempSettingsDbo settingsDbo = TempSettingsDbo.builder()
                        .sensorId(sensorDbo.getId())
                        .gisteris(msg.getActualConfig().getGisteris())
                        .target(msg.getActualConfig().getTarget())
                        .tuningSensor(msg.getActualConfig().getTuningSensor())
                        .build();

                settingRepository.update(settingsDbo);
                return;
            }
            case configChanges: {

/*
                TemperatureProtocol.ConfigChanges configChanges = msg.getConfigChanges();


                TempSettingsDbo settingsDbo = TempSettingsDbo.builder()
                        .sensorId(descriptor.getId())
                        .gisteris(msg.getConfigChanges().getGisteris())
                        .target(msg.getActualConfig().getTarget())
                        .tuningSensor(msg.getActualConfig().getTuningSensor())
                        .build();

                settingRepository.update(settingsDbo);*/


                return;
            }
            default: {
                log.error("Не поддерживаемый тип пакета {}", msg);
            }
        }
    }


}
