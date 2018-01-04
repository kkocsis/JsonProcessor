package com.kkocsis.jsonprocessor.web;

import com.kkocsis.jsonprocessor.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public ResponseEntity postJson(@RequestBody String rawMessage) {
        messageService.save(rawMessage);
        return ResponseEntity.ok().body(rawMessage);
    }

    @GetMapping
    public ResponseEntity findAllJson() {
        return ResponseEntity.ok().body(messageService.findAll());
    }

}
