package com.relex.relex_social.service.implementation;

import com.relex.relex_social.entity.JwtToken;
import com.relex.relex_social.repository.JwtTokenRepository;
import com.relex.relex_social.service.interfaces.IJwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JwtTokenService implements IJwtTokenService {
    private final JwtTokenRepository jwtTokenRepository;

    @Transactional
    @Override
    public void registerToken(Long profileId, String token) {
        jwtTokenRepository.deleteByProfileId(profileId);

        JwtToken jwtToken = JwtToken.builder()
                .profileId(profileId)
                .token(token)
                .build();

        jwtTokenRepository.save(jwtToken);
    }

    @Override
    public boolean isTokenValid(String token) {
        return jwtTokenRepository.existsByToken(token);
    }

    @Override
    public void invalidToken(Long profileId) {
        jwtTokenRepository.deleteByProfileId(profileId);
    }
}
