package com.relex.relex_social.dto.request;

import com.relex.relex_social.entity.AllowedToSend;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class EditProfileRequest {
    @Email(message = "Incorrect email format")
    String email;

    @NotBlank(message = "Username is required")
    @Size(min = 6, max = 100, message = "Length of the username must be between 6 and 100")
    String nickname;

    @NotBlank(message = "First name is required")
    String firstName;

    @NotBlank(message = "Surname is required")
    String surname;

    @NotNull(message = "Bio can't be null")
    String bio;

    @NotBlank(message = "Allowed to send is required")
    AllowedToSend allowedToSend;
}
