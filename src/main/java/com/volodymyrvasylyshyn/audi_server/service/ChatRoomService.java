package com.volodymyrvasylyshyn.audi_server.service;

import com.volodymyrvasylyshyn.audi_server.model.ChatRoom;
import com.volodymyrvasylyshyn.audi_server.repository.ChatRoomRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public ChatRoomService(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    public Optional<String> getChatId(
            Long senderId, Long recipientId, boolean createIfNotExist) {

        return chatRoomRepository
                .findBySenderIdAndRecipientId(senderId, recipientId)
                .map(ChatRoom::getChatId)
                .or(() -> {
                    if(!createIfNotExist) {
                        return  Optional.empty();
                    }
                    String chatId =
                            String.format("%s_%s", senderId, recipientId);
                    if(senderId.equals(recipientId)){
                        ChatRoom senderRecipient = ChatRoom
                                .builder()
                                .chatId(chatId)
                                .senderId(senderId)
                                .recipientId(recipientId)
                                .build();
                        chatRoomRepository.save(senderRecipient);
                    }
                    else {
                        ChatRoom senderRecipient = ChatRoom
                                .builder()
                                .chatId(chatId)
                                .senderId(senderId)
                                .recipientId(recipientId)
                                .build();

                        ChatRoom recipientSender = ChatRoom
                                .builder()
                                .chatId(chatId)
                                .senderId(recipientId)
                                .recipientId(senderId)
                                .build();
                        chatRoomRepository.save(senderRecipient);
                        chatRoomRepository.save(recipientSender);
                    }
                    return Optional.of(chatId);
                });
    }
    public Long findRecipientIdByChatIdAndSenderId(Long chatId, Long senderId) {
        return Objects.requireNonNull(chatRoomRepository.findBySenderIdAndId(senderId, chatId).orElse(null)).getRecipientId();
    }
}
