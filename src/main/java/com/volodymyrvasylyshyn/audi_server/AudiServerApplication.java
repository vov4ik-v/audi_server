package com.volodymyrvasylyshyn.audi_server;

import com.volodymyrvasylyshyn.audi_server.validations.EmailValidator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AudiServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AudiServerApplication.class, args);
    }

}
