package com.relex.relex_social.service;

import com.relex.relex_social.entity.JwtToken;
import com.relex.relex_social.repository.JwtTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JwtTokenService {
    private final JwtTokenRepository jwtTokenRepository;

    @Transactional
    public void registerToken(Long profileId, String token){
        jwtTokenRepository.deleteByProfileId(profileId);

        JwtToken jwtToken = JwtToken.builder()
                .profileId(profileId)
                .token(token)
                .build();

        jwtTokenRepository.save(jwtToken);
    }

    public boolean isTokenValid(String token){
        return jwtTokenRepository.existsByToken(token);
    }

    public void invalidToken(String token){
        jwtTokenRepository.deleteByToken(token);
    }
}
