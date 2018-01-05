package com.kkocsis.jsonprocessor.service;

import com.kkocsis.jsonprocessor.domain.Message;
import com.kkocsis.jsonprocessor.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SimpleMessageService implements MessageService {

    private final MessageRepository messageRepository;

    @Override
    public Message save(String rawMessage) {
        Message message = new Message();
        message.setMessage(rawMessage);
        return messageRepository.save(message);
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }
}
