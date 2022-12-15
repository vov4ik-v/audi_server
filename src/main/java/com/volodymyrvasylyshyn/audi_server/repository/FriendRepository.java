package com.volodymyrvasylyshyn.audi_server.repository;

import com.volodymyrvasylyshyn.audi_server.model.Friend;
import com.volodymyrvasylyshyn.audi_server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<Friend,Integer> {
    boolean existsByFirstUserAndSecondUser(User first, User second);
    Friend findByFirstUserAndSecondUser(User first, User second);
    List<Friend> findByFirstUser(User user);
    List<Friend> findBySecondUser(User user);
}
