package com.relex.relex_social.utility;

import com.relex.relex_social.dto.response.MessageDto;
import com.relex.relex_social.entity.Message;
import org.springframework.stereotype.Component;

@Component
public class MessageUtils {
    public MessageDto toDto(Message message) {
        return new MessageDto(
                message.getId(),
                message.getSenderId(),
                message.getSendTime(),
                message.getMessageText()
        );
    }
}
