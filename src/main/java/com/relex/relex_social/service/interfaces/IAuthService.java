package com.relex.relex_social.service.interfaces;

import com.relex.relex_social.dto.request.JwtRequest;
import com.relex.relex_social.exception.ResourceNotFoundException;

public interface IAuthService {
    String createToken(JwtRequest jwtRequest) throws ResourceNotFoundException;

    Long getAuthId();

    void deleteToken(Long profileId);
}
