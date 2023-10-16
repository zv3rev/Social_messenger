package com.relex.relex_social.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {
    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Length of the password must be between 6 and 100")
    String newPassword;
}
