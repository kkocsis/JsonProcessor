package com.kkocsis.jsonprocessor.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kkocsis.jsonprocessor.domain.Message;
import com.kkocsis.jsonprocessor.service.MessageService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MessageController {

    @Value("${jsonprocessor.redis.topic.pattern}")
    private String redisTopicPattern;

    private final StringRedisTemplate redisTemplate;
    private final MessageService messageService;
    private final ObjectMapper objectMapper;

    @ApiOperation(value = "Post new message", notes = "Any valid JSON is acceptable, there isn't any structural restriction")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity postJson(@RequestBody String rawMessage) {
        if (!isValidJSON(rawMessage)) {
            log.error("Not valid JSON: " + rawMessage);
            return ResponseEntity.badRequest().build();
        }
        redisTemplate.convertAndSend(redisTopicPattern, rawMessage);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "List all messages")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Message> findAllJson() {
        return messageService.findAll();
    }

    private boolean isValidJSON(final String json) {
        try {
            objectMapper.readTree(json);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

}
