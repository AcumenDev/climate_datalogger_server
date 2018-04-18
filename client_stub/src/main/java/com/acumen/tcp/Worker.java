package com.acumen.tcp;


import com.acumendev.climatelogger.protocol.BaseMessageOuterClass;
import com.acumendev.climatelogger.protocol.TemperatureOuterClass;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class Worker extends Thread {

    private final ConnectStore connectStore;
    private boolean state = true;
    private boolean auth = false;

    private Random random = new Random();

    public Worker(ConnectStore connectStore) {
        this.connectStore = connectStore;
    }

    @PostConstruct
    public void init() {
        start();

    }


    @Override
    public void run() {
        while (state) {
            try {

                BaseMessageOuterClass.BaseMessage reciveMsg = connectStore.queue.poll(1, TimeUnit.SECONDS);

                if (reciveMsg != null) {

                    if (reciveMsg.hasAuth()) {
                        if (reciveMsg.getAuth().getState() == 0) {
                            auth = true;
                        }
                    }
                }


                if (!auth) {

                    BaseMessageOuterClass.Auth request = BaseMessageOuterClass.Auth.newBuilder()
                            .setType(1)
                            .setVersion(1)
                            .setApiKey("b5d52c0b-9f15-42ac-a2f8-650aca23c682")
                            .build();

               /* connectStore.getCtx().writeAndFlush(AuthRequest.builder()
                        //  .apiKey("12345678901234567890")
                        .room((short) 1)
                        .type((short) 1)
                        .version((short) 1)
                        .build());*/
                    BaseMessageOuterClass.BaseMessage baseMessage = BaseMessageOuterClass.BaseMessage.newBuilder()
                            .setAuth(request)
                            .build();


                    connectStore.getCtx().channel().writeAndFlush(baseMessage);
                    // request

                    System.out.println("send AuthRequest ");
                    sleep(10000);
                    continue;
                }


                work(reciveMsg);

                sleep(500);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void work(BaseMessageOuterClass.BaseMessage reciveMsg) {
        TemperatureOuterClass.Notify notify = TemperatureOuterClass.Notify.newBuilder()
                .setCurrent(Math.abs(random.nextInt(20)))

                .build();

        TemperatureOuterClass.Temperature temperature = TemperatureOuterClass.Temperature.newBuilder().setNotify(notify).build();

        BaseMessageOuterClass.BaseMessage baseMessage = BaseMessageOuterClass.BaseMessage.newBuilder()
                .setTemperature(temperature)
                .build();

        connectStore.getCtx().channel().writeAndFlush(baseMessage);

    }

    @PreDestroy
    public void dest() {
        state = false;
    }
}
