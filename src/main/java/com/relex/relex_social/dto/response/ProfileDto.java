package com.relex.relex_social.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ProfileDto {
    Long id;
    String email;
    String nickname;
    String firstName;
    String surname;
    String bio;
}
