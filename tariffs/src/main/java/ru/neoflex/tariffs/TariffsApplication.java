package ru.neoflex.tariffs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TariffsApplication {

    public static void main(String[] args) {
        SpringApplication.run(TariffsApplication.class, args);
    }

}
