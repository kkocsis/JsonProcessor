package com.kkocsis.jsonprocessor.service;

import com.kkocsis.jsonprocessor.domain.Message;

import java.util.List;

public interface MessageService {

    Message save(String rawMessage);

    List<Message> findAll();
}
