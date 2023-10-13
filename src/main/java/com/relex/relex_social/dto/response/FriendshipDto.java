package com.relex.relex_social.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class FriendshipDto {
    Long id;
    Long senderId;
    Long recipientId;
    Timestamp requestDate;
    Timestamp approvedDate;
    Timestamp deniedDate;
}
