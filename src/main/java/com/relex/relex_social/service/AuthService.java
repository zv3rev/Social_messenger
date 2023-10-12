package com.relex.relex_social.service;

import com.relex.relex_social.dto.request.JwtRequest;
import com.relex.relex_social.entity.Profile;
import com.relex.relex_social.entity.ProfileStatus;
import com.relex.relex_social.exception.AccessToDeletedAccountException;
import com.relex.relex_social.exception.ResourceNotFoundException;
import com.relex.relex_social.repository.ProfileRepository;
import com.relex.relex_social.utility.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final ProfileDetailsService profileDetailsService;
    private final ProfileRepository profileRepository;
    private final JwtTokenUtils jwtTokenUtils;
    private final JwtTokenService jwtTokenService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public String createToken(JwtRequest jwtRequest) throws ResourceNotFoundException, AccessToDeletedAccountException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(),jwtRequest.getPassword()));
        UserDetails userDetails = profileDetailsService.loadUserByUsername(jwtRequest.getUsername());
        String token = jwtTokenUtils.generateToken(userDetails);
        Profile profile = profileRepository.findByNickname(jwtRequest.getUsername()).orElseThrow(() -> new ResourceNotFoundException("User wasn't found"));
        if (profile.getProfileStatus().equals(ProfileStatus.DELETED)){
            throw new AccessToDeletedAccountException("You can not get access to deleted account");
        }
        jwtTokenService.registerToken(profile.getId(),token);
        return token;
    }

    private Profile getAuthUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return null;
        }
        return profileRepository.findByNickname(auth.getPrincipal().toString()).orElse(null);
    }

    public Long getAuthId() {
        Profile authProfile = getAuthUser();
        if (authProfile == null){
            throw new AccessDeniedException("Access attempt by an un-authenticated user");
        }
        return authProfile.getId();
    }

    @Transactional
    public void deleteToken(Long profileId) {
        jwtTokenService.invalidToken(profileId);
    }
}
