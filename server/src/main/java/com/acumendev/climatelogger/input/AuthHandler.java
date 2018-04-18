package com.acumendev.climatelogger.input;

import com.acumendev.climatelogger.input.tcp.handlers.SensorHandler;
import com.acumendev.climatelogger.input.tcp.handlers.TemperatureHandler;
import com.acumendev.climatelogger.protocol.BaseMessageOuterClass;
import com.acumendev.climatelogger.repository.SensorRepository;
import com.acumendev.climatelogger.repository.dbo.SensorDbo;
import com.acumendev.climatelogger.repository.temperature.TemperatureReadingsRepository;
import com.acumendev.climatelogger.repository.temperature.TemperatureSettingRepository;
import com.acumendev.climatelogger.service.SensorDescriptor;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class AuthHandler {
    private final Map<SensorDescriptor, SensorDbo> sensorsEnadled;

    private final Map<Long, SensorHandler> sensorsActiveSession;

    private final TemperatureReadingsRepository readingsRepository;
    private final TemperatureSettingRepository settingRepository;
    private final SensorRepository sensorRepository;

    public AuthHandler(Map<SensorDescriptor, SensorDbo> sensorsEnadled,
                       Map<Long, SensorHandler> sensorsActiveSession,
                       TemperatureReadingsRepository readingsRepository,
                       TemperatureSettingRepository settingRepository,
                       SensorRepository sensorRepository) {
        this.sensorsEnadled = sensorsEnadled;
        this.sensorsActiveSession = sensorsActiveSession;
        this.readingsRepository = readingsRepository;
        this.settingRepository = settingRepository;
        this.sensorRepository = sensorRepository;
    }

    public SensorHandler auth(Channel channel, String channelId, BaseMessageOuterClass.Auth authRequest) {
        SensorDescriptor sensorDescriptor = SensorDescriptor.builder()
                .apiKey(authRequest.getApiKey())
                .type(authRequest.getType())
                .build();
        SensorDbo sensorDbo = sensorsEnadled.get(sensorDescriptor);

        if (sensorDbo != null) {
            log.info("Авторизованн channelId {}  {}", channelId, authRequest);

            channel.writeAndFlush(BaseMessageOuterClass.BaseMessage.newBuilder().setAuth(BaseMessageOuterClass.Auth.newBuilder().setState(0).build()).build());
            SensorHandler handler = buildHandler(sensorDbo, channel);

            if (handler != null) {
                sensorsActiveSession.put(sensorDbo.getId(), handler);
                return handler;
            }
        }
        log.warn("Не смогли авторизовать channelId {}  {}", channelId, authRequest);
        return null;
    }

    private SensorHandler buildHandler(SensorDbo sensorDbo, Channel channel) {

        switch (sensorDbo.getType()) {
            case 1: {
                return new TemperatureHandler(sensorDbo, channel, readingsRepository, settingRepository, sensorRepository);
            }
            default: {
                log.error("Не смогли создать обработчик датчика {}", sensorDbo);
            }
        }
        return null;
    }

    public void unregistered(SensorHandler handler) {
        sensorsActiveSession.remove(handler.getSensorId());
    }
}
