package com.relex.relex_social.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class JwtRequest {
    @NotBlank(message = "Username is required")
    private String username;
    @NotBlank(message = "Password is required")
    private String password;
}
