package com.relex.relex_social.service.interfaces;

import com.relex.relex_social.dto.response.MessageDto;

import java.util.List;

public interface IMessageService {
    Long send(Long senderId, Long recipientId, String messageText);

    List<MessageDto> getConversation(Long profileId, Long chatPartnerId);
}
