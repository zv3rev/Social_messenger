package com.relex.relex_social.service.interfaces;

import com.relex.relex_social.dto.request.SendMessageRequest;
import com.relex.relex_social.dto.response.MessageDto;

import java.util.List;

public interface IMessageService {
    Long send(Long senderId, String recipientId, SendMessageRequest sendMessageRequest);

    List<MessageDto> getConversation(Long profileId, String chatPartnerNickname);
}
