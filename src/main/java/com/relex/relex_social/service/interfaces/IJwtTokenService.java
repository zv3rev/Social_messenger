package com.relex.relex_social.service.interfaces;

public interface IJwtTokenService {
    void registerToken(Long profileId, String token);

    boolean isTokenValid(String token);

    void invalidToken(Long profileId);
}
