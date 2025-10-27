package org.sid.compteservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReservationavionApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ReservationavionApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(ReservationavionApplication.class, args);
    }
}
