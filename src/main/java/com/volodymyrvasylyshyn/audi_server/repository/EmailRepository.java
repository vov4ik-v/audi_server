package com.volodymyrvasylyshyn.audi_server.repository;

import com.volodymyrvasylyshyn.audi_server.model.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailRepository extends JpaRepository<Email,Integer> {
    Optional<Email> findById(Integer id);
}
