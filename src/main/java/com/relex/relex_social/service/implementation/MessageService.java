package com.relex.relex_social.service.implementation;

import com.relex.relex_social.dto.response.MessageDto;
import com.relex.relex_social.entity.Message;
import com.relex.relex_social.repository.MessageRepository;
import com.relex.relex_social.service.interfaces.IMessageService;
import com.relex.relex_social.utility.MessageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService implements IMessageService {
    private final MessageRepository messageRepository;
    private final MessageUtils messageUtils;

    @Override
    public Long send(Long senderId, Long recipientId, String messageText) {
        Message message = Message.builder()
                .senderId(senderId)
                .recipientId(recipientId)
                .sendTime(new Timestamp(new Date().getTime()))
                .messageText(messageText)
                .build();
        return messageRepository.save(message).getId();
    }

    @Override
    public List<MessageDto> getConversation(Long profileId, Long chatPartnerId) {
        return messageRepository.getMessagesBetweenUsers(profileId, chatPartnerId).stream()
                .map(messageUtils::toDto).collect(Collectors.toList());
    }

}
