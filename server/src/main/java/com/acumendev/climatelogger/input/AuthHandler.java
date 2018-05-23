package com.acumendev.climatelogger.input;

import com.acumendev.climatelogger.input.tcp.handlers.SensorHandler;
import com.acumendev.climatelogger.input.tcp.handlers.TemperatureHandler;
import com.acumendev.climatelogger.protocol.BaseMessageOuterClass;
import com.acumendev.climatelogger.repository.temperature.TemperatureReadingsAsyncRepository;
import com.acumendev.climatelogger.repository.SensorAsyncRepository;
import com.acumendev.climatelogger.repository.dbo.SensorDbo;
import com.acumendev.climatelogger.repository.temperature.TemperatureSettingRepository;
import com.acumendev.climatelogger.service.SensorDescriptor;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class AuthHandler {
    private final Map<SensorDescriptor, SensorDbo> sensorsEnabled;

    private final Map<Long, SensorHandler> sensorsActiveSession;

    private final TemperatureSettingRepository settingRepository;
    private final TemperatureReadingsAsyncRepository temperatureReadingsAsyncRepository;

    private final SensorAsyncRepository sensorAsyncRepository;

    public AuthHandler(Map<SensorDescriptor, SensorDbo> sensorsEnabled,
                       Map<Long, SensorHandler> sensorsActiveSession,
                       TemperatureSettingRepository settingRepository,
                       TemperatureReadingsAsyncRepository temperatureReadingsAsyncRepository,
                       SensorAsyncRepository sensorAsyncRepository) {
        this.sensorsEnabled = sensorsEnabled;
        this.sensorsActiveSession = sensorsActiveSession;
        this.settingRepository = settingRepository;
        this.temperatureReadingsAsyncRepository = temperatureReadingsAsyncRepository;
        this.sensorAsyncRepository = sensorAsyncRepository;
    }

    public SensorHandler<BaseMessageOuterClass.BaseMessage> auth(Channel channel, String channelId, BaseMessageOuterClass.Auth authRequest) {
        SensorDescriptor sensorDescriptor = SensorDescriptor.builder()
                .apiKey(authRequest.getApiKey())
                .type(authRequest.getType())
                .build();
        SensorDbo sensorDbo = sensorsEnabled.get(sensorDescriptor);

        if (sensorDbo != null) {
            log.info("Авторизованн channelId {}  {}", channelId, authRequest);

            channel.writeAndFlush(BaseMessageOuterClass.BaseMessage.newBuilder().setAuth(BaseMessageOuterClass.Auth.newBuilder().setState(0).build()).build());
            SensorHandler<BaseMessageOuterClass.BaseMessage> handler = buildHandler(sensorDbo, channel);

            if (handler != null) {
                sensorsActiveSession.put(sensorDbo.getId(), handler);
                return handler;
            }
        }
        log.warn("Не смогли авторизовать channelId {}  {}", channelId, authRequest);
        return null;
    }

    private SensorHandler<BaseMessageOuterClass.BaseMessage> buildHandler(SensorDbo sensorDbo, Channel channel) {

        switch (sensorDbo.getType()) {
            case 1: {
                return new TemperatureHandler(sensorDbo, channel, settingRepository, temperatureReadingsAsyncRepository, sensorAsyncRepository);
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
