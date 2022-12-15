package com.volodymyrvasylyshyn.audi_server.repository;

import com.volodymyrvasylyshyn.audi_server.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> {
    Optional<ChatRoom> findBySenderIdAndRecipientId(Long senderId, Long recipientId);
    Optional<ChatRoom> findBySenderIdAndId(Long senderId, Long chatId);

}
