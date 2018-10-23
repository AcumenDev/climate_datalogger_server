package com.acumendev.climatelogger.input;

import com.acumendev.climatelogger.input.tcp.handlers.SensorHandler;
import com.acumendev.climatelogger.input.tcp.handlers.TemperatureHandler;
import com.acumendev.climatelogger.protocol.BaseMessageOuterClass;
import com.acumendev.climatelogger.repository.SensorAsyncRepository;
import com.acumendev.climatelogger.repository.dbo.SensorDbo;
import com.acumendev.climatelogger.repository.temperature.TemperatureReadingsAsyncRepository;
import com.acumendev.climatelogger.repository.temperature.TemperatureSettingRepository;
import com.acumendev.climatelogger.service.SensorDescriptor;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AuthHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(AuthHandler.class);

    private final Map<SensorDescriptor, SensorDbo> sensorsEnabled;

    private final Map<Integer, SensorHandler> sensorsActiveSession;

    private final TemperatureSettingRepository settingRepository;
    private final TemperatureReadingsAsyncRepository temperatureReadingsAsyncRepository;

    private final SensorAsyncRepository sensorAsyncRepository;

    public AuthHandler(Map<SensorDescriptor, SensorDbo> sensorsEnabled,
                       Map<Integer, SensorHandler> sensorsActiveSession,
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

        SensorDbo sensorDbo = sensorsEnabled.get(new SensorDescriptor(authRequest.getApiKey(), authRequest.getType()));

        if (sensorDbo != null) {
            LOGGER.info("Авторизованн channelId {}  {}", channelId, authRequest);

            channel.writeAndFlush(BaseMessageOuterClass.BaseMessage.newBuilder().setAuth(BaseMessageOuterClass.Auth.newBuilder().setState(0).build()).build());
            SensorHandler<BaseMessageOuterClass.BaseMessage> handler = buildHandler(sensorDbo, channel);

            if (handler != null) {
                sensorsActiveSession.put(sensorDbo.id, handler);
                return handler;
            }
        }
        LOGGER.warn("Не смогли авторизовать channelId {}  {}", channelId, authRequest);
        return null;
    }

    private SensorHandler<BaseMessageOuterClass.BaseMessage> buildHandler(SensorDbo sensorDbo, Channel channel) {

        switch (sensorDbo.type) {
            case 1: {
                return new TemperatureHandler(sensorDbo, channel, settingRepository, temperatureReadingsAsyncRepository, sensorAsyncRepository);
            }
            default: {
                LOGGER.error("Не смогли создать обработчик датчика {}", sensorDbo);
            }
        }
        return null;
    }

    public void unregistered(SensorHandler handler) {
        sensorsActiveSession.remove(handler.getSensorId());
    }
}
