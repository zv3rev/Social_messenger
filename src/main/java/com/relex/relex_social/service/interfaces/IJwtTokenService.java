package com.relex.relex_social.service.interfaces;

import com.relex.relex_social.entity.JwtType;

public interface IJwtTokenService {
    void registerToken(Long profileId, String token, JwtType type);

    boolean isTokenValid(String token);

    void invalidTokens(Long profileId);
}
