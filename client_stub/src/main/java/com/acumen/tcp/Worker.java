package com.acumen.tcp;

import com.acumen.tcp.dto.AuthRequest;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
public class Worker extends Thread {

    private final ConnectStore connectStore;
    private boolean state = true;

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
            if (connectStore.getCtx() != null) {

                connectStore.getCtx().writeAndFlush(AuthRequest.builder()
                      //  .apiKey("12345678901234567890")
                        .room((short) 1)
                        .type((short) 1)
                        .version((short) 1)
                        .build());
                System.out.println("send AuthRequest ");

            }
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @PreDestroy
    public void dest() {
        state = false;
    }
}
