package com.acumen;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClientStubApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ClientStubApplication.class, args);
	}

    @Override
    public void run(String... args) throws Exception {

    }
}
