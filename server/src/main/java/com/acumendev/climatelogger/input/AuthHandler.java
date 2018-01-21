package com.acumendev.climatelogger.input;

import com.acumendev.climatelogger.input.tcp.TempNew;
import com.acumendev.climatelogger.service.SensorHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;

@Slf4j
@Component
public class AuthHandler {
    private final Map<String, SensorHandler> sensorHandlers;
    private HashSet<String> auth = new HashSet<>();

    public AuthHandler(Map<String, SensorHandler> sensorHandlers) {

        this.sensorHandlers = sensorHandlers;
    }

    public boolean clientAuth(String channelId) {

        return auth.contains(channelId);
    }

    public void disconnect(String channelId) {
        auth.remove(channelId);
    }

    public SensorHandler auth(String channelId, TempNew.AuthRequest authRequest) {
        if (sensorHandlers.containsKey(authRequest.getApiKey())) {
            SensorHandler handler = sensorHandlers.get(authRequest.getApiKey());

            if (handler.getSensorType() == authRequest.getType() &&
                    handler.getVersion() == authRequest.getVersion()) {
                auth.add(channelId);
                log.info("Авторизованн channelId {}  {}", channelId, authRequest);
                return handler;
            }
        }
        log.warn("Не смогли авторизовать channelId {}  {}", channelId, authRequest);
        return null;
    }
}
