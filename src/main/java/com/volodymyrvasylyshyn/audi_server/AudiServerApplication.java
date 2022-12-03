package com.volodymyrvasylyshyn.audi_server;


import com.volodymyrvasylyshyn.audi_server.model.User;
import com.volodymyrvasylyshyn.audi_server.repository.UserRepository;
import com.volodymyrvasylyshyn.audi_server.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AudiServerApplication {


    public static void main(String[] args) {
        SpringApplication.run(AudiServerApplication.class, args);

    }

}
