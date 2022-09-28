package com.volodymyrvasylyshyn.audi_server.repository;

import com.volodymyrvasylyshyn.audi_server.model.Post;
import com.volodymyrvasylyshyn.audi_server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

    List<Post> findAllByUserOrderByCreatedDateDesc(User user);
    List<Post> findAllByOrderByCreatedDateDesc();
    Optional<Post> findPostByIdAndUser(Long postId, User user);



}
