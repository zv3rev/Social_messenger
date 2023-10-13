package com.relex.relex_social.controller;

import com.relex.relex_social.dto.response.MessageDto;
import com.relex.relex_social.service.interfaces.IAuthService;
import com.relex.relex_social.service.interfaces.IMessageService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/messages")
public class MessageController {
    private final IMessageService messageService;
    private final IAuthService authService;

    @PostMapping
    public ResponseEntity sendMessage(@RequestParam Long recipientId, @RequestBody String messageText) {
        Long profileId = authService.getAuthId();
        Long messageId = messageService.send(profileId, recipientId, messageText);
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
        Long profileId = authService.getAuthId();

        List<MessageDto> messageList = messageService.getConversation(profileId, chatPartnerId);
        return ResponseEntity.ok().body(messageList);
    }
}
