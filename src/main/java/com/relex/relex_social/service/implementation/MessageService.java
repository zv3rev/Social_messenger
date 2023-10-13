package com.relex.relex_social.service.implementation;

import com.relex.relex_social.dto.request.SendMessageRequest;
import com.relex.relex_social.dto.response.MessageDto;
import com.relex.relex_social.entity.Message;
import com.relex.relex_social.entity.Profile;
import com.relex.relex_social.exception.ResourceNotFoundException;
import com.relex.relex_social.repository.MessageRepository;
import com.relex.relex_social.repository.ProfileRepository;
import com.relex.relex_social.service.interfaces.IMessageService;
import com.relex.relex_social.utility.MessageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService implements IMessageService {
    private final MessageRepository messageRepository;
    private final MessageUtils messageUtils;
    private final ProfileRepository profileRepository;

    @Override
    @Transactional
    public Long send(Long senderId, String recipientNickname, SendMessageRequest sendMessageRequest) throws ResourceNotFoundException {
        Profile recipient = profileRepository.findByNickname(recipientNickname).orElseThrow(() -> new ResourceNotFoundException(String.format("User with nickname %s not found", recipientNickname)));

        Message message = Message.builder()
                .senderId(senderId)
                .recipientId(recipient.getId())
                .sendTime(new Timestamp(new Date().getTime()))
                .messageText(sendMessageRequest.getMessage())
                .build();
        return messageRepository.save(message).getId();
    }

    @Override
    @Transactional
    public List<MessageDto> getConversation(Long profileId, String chatPartnerNickname) throws ResourceNotFoundException {
        Profile chatPartner = profileRepository.findByNickname(chatPartnerNickname).orElseThrow(() -> new ResourceNotFoundException(String.format("User with nickname %s not found", chatPartnerNickname)));

        return messageRepository.getMessagesBetweenUsers(profileId, chatPartner.getId()).stream()
                .map(messageUtils::toDto).collect(Collectors.toList());
    }

}
