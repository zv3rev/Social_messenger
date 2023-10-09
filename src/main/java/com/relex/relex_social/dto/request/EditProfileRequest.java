package com.relex.relex_social.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EditProfileRequest {
    String email;
    String nickname;
    String firstName;
    String surname;
    String bio;
}
