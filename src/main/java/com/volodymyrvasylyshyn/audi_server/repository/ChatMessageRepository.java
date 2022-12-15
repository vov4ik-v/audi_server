package com.volodymyrvasylyshyn.audi_server.repository;

import com.volodymyrvasylyshyn.audi_server.enums.MessageStatus;
import com.volodymyrvasylyshyn.audi_server.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long> {
    long countBySenderIdAndRecipientIdAndStatus(Long senderId, Long recipientId, MessageStatus status);
    ChatMessage findBySenderIdAndRecipientId(Long senderId,Long recipientId);
    List<ChatMessage> findAllBySenderIdAndRecipientIdAndStatus(Long senderId,Long recipientId,MessageStatus status);
    List<ChatMessage> findByChatId(String chatId);
}
