package com.kkocsis.jsonprocessor.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;
import com.google.common.collect.ImmutableList;
import com.kkocsis.jsonprocessor.domain.Message;
import com.kkocsis.jsonprocessor.service.MessageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MessageControllerTest {

    @InjectMocks
    private MessageController subject;

    @Mock
    private StringRedisTemplate redisTemplate;
    @Mock
    private MessageService messageService;
    @Mock
    private ObjectMapper objectMapper;

    @Test
    public void givenAValidMessage_whenPostToController_thenItSendsToRedisAndResponseOk() throws Exception {
        //GIVEN
        String message = "{\"test\": \"message\"}";
        //WHEN
        when(objectMapper.readTree(anyString())).thenReturn(new TextNode("test"));
        ResponseEntity response = subject.postMessage(message);
        //THEN
        assertTrue(response.getStatusCode().is2xxSuccessful());
        verify(redisTemplate).convertAndSend(anyString(), any());
    }

    @Test
    public void givenAnInValidMessage_whenPostToController_thenBadRequestResponse() throws Exception {
        //GIVE
        String notValidMessage = "}{";
        //WHEN
        when(objectMapper.readTree(anyString())).thenThrow(new IOException());
        ResponseEntity response = subject.postMessage(notValidMessage);
        //THEN
        assertTrue(response.getStatusCode().is4xxClientError());
        verify(redisTemplate, times(0)).convertAndSend(anyString(), any());
    }

    @Test
    public void givenListOfAllMessages_whenFindAll_thenMessageServiceGivesBackAllMessages() throws Exception {
        //GIVEN
        Message message = new Message();
        message.setId(1L);
        message.setCreateDate(LocalDateTime.now());
        List<Message> allMessages = ImmutableList.of(message);
        //WHEN
        when(messageService.findAll()).thenReturn(allMessages);
        List<Message> actualMessages = subject.findAllMessages();
        //THEN
        assertEquals(allMessages, actualMessages);
    }

}
