package com.relex.relex_social.dto.request;

import lombok.Data;

@Data
public class JwtRequest {
    private String username;
    private String password;
}
