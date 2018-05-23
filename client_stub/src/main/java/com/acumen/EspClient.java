package com.acumen;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.DoubleStream;

/**
 * Created by vladimir akummail@gmail.com on 1/2/18.
 */
@Slf4j
//@Component
public class EspClient extends Thread  {

    private final RestTemplate restTemplate;
    private final String url = "http://localhost:8080/api/input";
    private final Random random = new Random();
    private volatile boolean state;


    public EspClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    public void init() {
        start();
    }


    @Override
    public void run() {

        state = true;

        while (state) {
            try {
                 DoubleStream humidity = random.doubles(50, 99);
                 DoubleStream temperature = random.doubles(0, 30);
                 DoubleStream pressure = random.doubles(10000, 99999);
//                InputValueDto inputValueDto = InputValueDto.builder()
//                        .user("akum")
//                        .number(1)
//                        .type(4)
//                        .rommNumber(1)
//                        .data(InputValueDto.Data.builder()
//                                .humidity(humidity.findFirst().getAsDouble())
//                                .temperature(temperature.findFirst().getAsDouble())
//                                .pressure(pressure.findFirst().getAsDouble())
//                                .build())
//                        .build();

               // restTemplate.postForEntity(url, inputValueDto, String.class);


            } catch (Exception e) {
                log.error("Ошибка отправки данных", e);
            }

            try {
                Thread.sleep(1000*30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @PreDestroy
    public void exitContext() throws InterruptedException {
        state = false;
        join();
    }
}
