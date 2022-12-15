package com.volodymyrvasylyshyn.audi_server.repository;

import com.volodymyrvasylyshyn.audi_server.model.Interlocutor;
import com.volodymyrvasylyshyn.audi_server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterlocutorRepository extends JpaRepository<Interlocutor,Integer> {
    boolean existsByFirstUserAndSecondUser(User first, User second);
    Interlocutor findByFirstUserAndSecondUser(User firts,User second);
    List<Interlocutor> findByFirstUser(User currentUser);

    List<Interlocutor> findBySecondUser(User currentUser);
}
