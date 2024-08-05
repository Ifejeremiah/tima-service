package com.tima;


import ch.rasc.sse.eventbus.config.EnableSseEventBus;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableSseEventBus
@EnableScheduling
public class Application {

    public static void main(String... args) {
        SpringApplication.run(Application.class, args);
    }
}
