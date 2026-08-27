package com.palette;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement //Enable annotation-based transaction management
@EnableCaching
@EnableScheduling
@Slf4j
public class PaletteApplication {
    public static void main(String[] args) {
        SpringApplication.run(PaletteApplication.class, args);
        log.info("server started");
    }
}
