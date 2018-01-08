package com.kkocsis.jsonprocessor.service;

import com.google.common.collect.ImmutableList;
import com.kkocsis.jsonprocessor.domain.Message;
import com.kkocsis.jsonprocessor.repository.MessageRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SimpleMessageServiceTest {

    private static final String TEST_MESSAGE = "test message";

    @InjectMocks
    private SimpleMessageService subject;

    @Mock
    private MessageRepository messageRepository;
    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Test
    public void givenMessage_whenSaveMessage_thenSendToWebsocketAndReturnTheSavedMessage() {
        //GIVEN
        Message message = new Message();
        message.setId(1L);
        message.setCreateDate(LocalDateTime.now());
        message.setMessage(TEST_MESSAGE);
        //WHEN
        when(messageRepository.save(any(Message.class))).thenReturn(message);
        Message actualMessage = subject.save(TEST_MESSAGE);
        //THEN
        verify(messagingTemplate).convertAndSend(anyString(), any(Object.class));
        assertEquals(message, actualMessage);
    }

    @Test
    public void givenMessage_whenFindAllMessage_thenReturnAllMessagesFromDatabase() {
        //GIVEN
        Message message = new Message();
        message.setMessage(TEST_MESSAGE);
        //WHEN
        when(messageRepository.findAll()).thenReturn(ImmutableList.of(message));
        List<Message> messages = subject.findAll();
        //THEN
        assertEquals(1, messages.size());
        assertEquals(message, messages.get(0));
    }
}
