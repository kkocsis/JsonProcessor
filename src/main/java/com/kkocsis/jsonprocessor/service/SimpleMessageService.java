package com.kkocsis.jsonprocessor.service;

import com.kkocsis.jsonprocessor.domain.Message;
import com.kkocsis.jsonprocessor.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SimpleMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public Message save(String rawMessage) {
        Message message = new Message();
        message.setMessage(rawMessage);
        Message persistedMessage = messageRepository.save(message);
        messagingTemplate.convertAndSend("/topic/message", persistedMessage);
        return persistedMessage;
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }
}
