package com.relex.relex_social.service.interfaces;

import com.relex.relex_social.dto.request.SendMessageRequest;
import com.relex.relex_social.dto.response.MessageDto;
import com.relex.relex_social.exception.ResourceNotFoundException;

import java.util.List;

public interface IMessageService {
    Long send(Long senderId, String recipientId, SendMessageRequest sendMessageRequest) throws ResourceNotFoundException;

    List<MessageDto> getConversation(Long profileId, String chatPartnerNickname) throws ResourceNotFoundException;
}
