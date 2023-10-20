package com.relex.relex_social.service.implementation;

import com.relex.relex_social.dto.request.JwtRequest;
import com.relex.relex_social.dto.response.JwtResponse;
import com.relex.relex_social.entity.JwtType;
import com.relex.relex_social.entity.Profile;
import com.relex.relex_social.exception.ResourceNotFoundException;
import com.relex.relex_social.repository.ProfileRepository;
import com.relex.relex_social.service.interfaces.IAuthService;
import com.relex.relex_social.service.interfaces.IJwtTokenService;
import com.relex.relex_social.utility.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {
    private final UserDetailsService profileDetailsService;
    private final ProfileRepository profileRepository;
    private final JwtTokenUtils jwtTokenUtils;
    private final IJwtTokenService jwtTokenService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    @Override
    public JwtResponse createAccessToken(JwtRequest jwtRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword()));
        UserDetails userDetails = profileDetailsService.loadUserByUsername(jwtRequest.getUsername());
        String accessToken = jwtTokenUtils.generateAccessToken(userDetails);
        String refreshToken = jwtTokenUtils.generateRefreshToken(userDetails);
        Profile profile = profileRepository.findByNickname(jwtRequest.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User wasn't found"));
        jwtTokenService.invalidTokens(profile.getId());
        jwtTokenService.registerToken(profile.getId(), accessToken, JwtType.ACCESS);
        jwtTokenService.registerToken(profile.getId(), refreshToken, JwtType.REFRESH);
        return new JwtResponse(accessToken, refreshToken);
    }


    private Profile getAuthUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return null;
        }
        return profileRepository.findByNickname(auth.getPrincipal().toString())
                .orElseThrow(() -> new ResourceNotFoundException("User with this JWT wasn't found"));
    }

    @Override
    public Long getAuthId() {
        Profile authProfile = getAuthUser();
        if (authProfile == null) {
            throw new ResourceNotFoundException("User with this JWT wasn't found");
        }
        return authProfile.getId();
    }

    @Transactional
    @Override
    public void deleteToken(Long profileId) {
        jwtTokenService.invalidTokens(profileId);
    }

    @Override
    @Transactional
    public JwtResponse refreshTokens(String refreshToken) {
        if (!jwtTokenUtils.validateRefreshToken(refreshToken) && !jwtTokenService.isTokenValid(refreshToken)) {
            throw new AccessDeniedException("This token is invalid");
        }
        String username = jwtTokenUtils.getUsernameFromRefreshToken(refreshToken);
        UserDetails userDetails = profileDetailsService.loadUserByUsername(username);
        Profile profile = profileRepository.findByNickname(username)
                .orElseThrow(() -> new ResourceNotFoundException("User wasn't found"));
        jwtTokenService.invalidTokens(profile.getId());
        String newAccessToken = jwtTokenUtils.generateAccessToken(userDetails);
        String newRefreshToken = jwtTokenUtils.generateRefreshToken(userDetails);
        jwtTokenService.registerToken(profile.getId(), newAccessToken, JwtType.ACCESS);
        jwtTokenService.registerToken(profile.getId(), newRefreshToken, JwtType.REFRESH);
        return new JwtResponse(newAccessToken, newRefreshToken);
    }
}
