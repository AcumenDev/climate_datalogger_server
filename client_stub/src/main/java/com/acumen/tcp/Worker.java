package com.acumen.tcp;

import com.acumen.tcp.dto_new.TemperatureProtocol;
import com.acumen.tcp.dto_new.TemperatureProtocol.AuthRequest;
import com.acumen.tcp.dto_new.TemperatureProtocol.BaseMessage;
import com.acumen.tcp.dto_new.TemperatureProtocol.PacketType;
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

                BaseMessage reciveMsg = connectStore.queue.poll(1, TimeUnit.SECONDS);

                if (reciveMsg != null) {

                    if (reciveMsg.getType() == PacketType.authResponse) {
                        if (reciveMsg.getAuthResponse().getState() == 0) {
                            auth = true;
                        }
                    }
                }


                if (!auth) {

                    AuthRequest request = AuthRequest.newBuilder()
                            .setType(1)
                            .setVersion(1)
                            .setApiKey("53ae8ff6-3d8c-11e8-b467-0ed5f89f718b")
                            .build();

               /* connectStore.getCtx().writeAndFlush(AuthRequest.builder()
                        //  .apiKey("12345678901234567890")
                        .room((short) 1)
                        .type((short) 1)
                        .version((short) 1)
                        .build());*/
                    BaseMessage baseMessage = BaseMessage.newBuilder()
                            .setType(PacketType.authRequest)
                            .setAuthRequest(request)
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

    private void work(BaseMessage reciveMsg) {
        TemperatureProtocol.NotifyRequest notifyRequest = TemperatureProtocol.NotifyRequest.newBuilder()
                .setCurrent(Math.abs(random.nextInt(20)))

                .build();

        BaseMessage baseMessage = BaseMessage.newBuilder()
                .setType(PacketType.notifyRequest)
                .setNotifyRequest(notifyRequest)
                .build();

        connectStore.getCtx().channel().writeAndFlush(baseMessage);

    }

    @PreDestroy
    public void dest() {
        state = false;
    }
}
