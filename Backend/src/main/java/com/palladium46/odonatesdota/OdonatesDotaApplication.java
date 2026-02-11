package com.palladium46.odonatesdota;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@SpringBootApplication
@EnableWebMvc
@EnableAsync
public class OdonatesDotaApplication {
    public static void main(String[] args) {
        SpringApplication.run(OdonatesDotaApplication.class, args);
    }
}
