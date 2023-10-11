package com.relex.relex_social.controller;

import com.relex.relex_social.dto.response.MessageDto;
import com.relex.relex_social.service.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/messages")
public class MessageController {
    private final MessageService messageService;

    @PostMapping
    public ResponseEntity sendMessage(@RequestParam Long recipientId, @RequestBody String messageText) {
        Long messageId = messageService.send(recipientId, messageText);
        return ResponseEntity.created(
                        ServletUriComponentsBuilder
                                .fromCurrentRequest()
                                .path("/{id}")
                                .buildAndExpand(messageId)
                                .toUri())
                .build();
    }

    @GetMapping
    public ResponseEntity getConversationWithUser(@RequestParam Long chatPartnerId) {
        List<MessageDto> messageList = messageService.getConversation(chatPartnerId);
        return ResponseEntity.ok().body(messageList);
    }
}
