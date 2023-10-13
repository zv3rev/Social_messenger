package com.relex.relex_social.service.interfaces;

import com.relex.relex_social.dto.request.JwtRequest;

public interface IAuthService {
    String createToken(JwtRequest jwtRequest);

    Long getAuthId();

    void deleteToken(Long profileId);
}
