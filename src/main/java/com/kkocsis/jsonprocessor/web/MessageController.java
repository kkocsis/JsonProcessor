package com.kkocsis.jsonprocessor.web;

import com.kkocsis.jsonprocessor.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MessageController {

    @Value("${jsonprocessor.redis.topic.pattern}")
    private String redisTopicPattern;

    private final StringRedisTemplate redisTemplate;
    private final MessageService messageService;

    @PostMapping
    public ResponseEntity postJson(@RequestBody String rawMessage) {
        redisTemplate.convertAndSend(redisTopicPattern, rawMessage);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity findAllJson() {
        return ResponseEntity.ok().body(messageService.findAll());
    }

}
