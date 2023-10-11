package com.relex.relex_social.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.sql.Timestamp;

@AllArgsConstructor
@Data
public class MessageDto {
    private Long id;
    private Long senderId;
    private Timestamp sendTime;
    private String messageText;
}
