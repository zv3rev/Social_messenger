package com.relex.relex_social.dto.response;

import com.relex.relex_social.entity.AllowedToSend;
import com.relex.relex_social.entity.ProfileStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@AllArgsConstructor
@Data
public class ProfileDto {
    Long id;
    String email;
    String nickname;
    String firstName;
    String surname;
    String bio;
    ProfileStatus status;
    AllowedToSend allowedToSend;
    Boolean isFriendsListVisible;
    Timestamp deleteDate;
}
