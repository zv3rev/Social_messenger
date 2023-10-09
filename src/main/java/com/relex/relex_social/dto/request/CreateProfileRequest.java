package com.relex.relex_social.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class CreateProfileRequest {
    @Email(message = "Incorrect email format")
    String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Length of the password must be between 6 and 100")
    String password;

    @NotBlank(message = "Username is required")
    @Size(min = 6, max = 100, message = "Length of the username must be between 6 and 100")
    String nickname;

    @NotBlank(message = "First name is required")
    String firstName;

    @NotBlank(message = "Surname is required")
    String surname;
    String bio;
}
