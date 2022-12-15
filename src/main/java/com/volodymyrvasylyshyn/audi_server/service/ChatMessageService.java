package com.volodymyrvasylyshyn.audi_server.service;

import com.volodymyrvasylyshyn.audi_server.enums.MessageStatus;
import com.volodymyrvasylyshyn.audi_server.exeptions.ResourceNotFoundException;
import com.volodymyrvasylyshyn.audi_server.model.ChatMessage;
import com.volodymyrvasylyshyn.audi_server.repository.ChatMessageRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChatMessageService {

    private final ChatMessageRepository repository;
    private final ChatRoomService chatRoomService;
    private final InterlocutorService interlocutorService;

    public ChatMessageService(ChatMessageRepository repository, ChatRoomService chatRoomService, InterlocutorService interlocutorService) {
        this.repository = repository;
        this.chatRoomService = chatRoomService;
        this.interlocutorService = interlocutorService;
    }

    public ChatMessage save(ChatMessage chatMessage) {
        chatMessage.setStatus(MessageStatus.RECEIVED);
        repository.save(chatMessage);
        return chatMessage;
    }

    public long countNewMessages(Long senderId, Long recipientId) {
        return repository.countBySenderIdAndRecipientIdAndStatus(
                senderId, recipientId, MessageStatus.RECEIVED);
    }
    public void checkForFirstMessage(ChatMessage chatMessage){
        int countMessages  = getCountMessages(chatMessage.getSenderId(), chatMessage.getRecipientId());
        if(countMessages == 1){
            interlocutorService.addInterlocatorById(chatMessage.getSenderId(),chatMessage.getRecipientId());
            interlocutorService.addInterlocatorById(chatMessage.getRecipientId(),chatMessage.getSenderId());
        }

    }

    private int getCountMessages(Long senderId, Long recipientId){
        Optional<String> chatId = chatRoomService.getChatId(senderId, recipientId, false);

        List<ChatMessage> messages =
                chatId.map(repository::findByChatId).orElse(new ArrayList<>());
        return messages.size();
    }

    public List<ChatMessage> findChatMessages(Long senderId, Long recipientId) {
        Optional<String> chatId = chatRoomService.getChatId(senderId, recipientId, false);

        List<ChatMessage> messages =
                chatId.map(repository::findByChatId).orElse(new ArrayList<>());

        if(messages.size() > 0) {
            updateStatuses(senderId, recipientId, MessageStatus.DELIVERED);
        }

        return messages;
    }

    public ChatMessage findById(Long id) {
        return repository
                .findById(id)
                .map(chatMessage -> {
                    chatMessage.setStatus(MessageStatus.DELIVERED);
                    return repository.save(chatMessage);
                })
                .orElseThrow(() ->
                        new ResourceNotFoundException("can't find message (" + id + ")"));
    }

    public void updateStatuses(Long senderId, Long recipientId, MessageStatus status) {
        List<ChatMessage> chatMessages = repository.findAllBySenderIdAndRecipientIdAndStatus(senderId,recipientId,MessageStatus.RECEIVED);
        chatMessages.stream().map((message) ->{
            message.setStatus(status);
            repository.save(message);
            return message;
        }).collect(Collectors.toList());
//        chatMessages.setStatus(status);
//        repository.save(chatMessages);
    }


}
