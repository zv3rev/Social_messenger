package com.relex.relex_social.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class JwtResponse {
    private String accessToken;
    private String refreshToken;
}
