package com.relex.relex_social.service.interfaces;

import com.relex.relex_social.dto.request.JwtRequest;
import com.relex.relex_social.dto.response.JwtResponse;

public interface IAuthService {
    JwtResponse createAccessToken(JwtRequest jwtRequest);


    Long getAuthId();

    void deleteToken(Long profileId);

    JwtResponse refreshTokens(String refroshToken);
}
