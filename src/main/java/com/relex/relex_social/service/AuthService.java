package com.relex.relex_social.service;

import com.relex.relex_social.dto.request.JwtRequest;
import com.relex.relex_social.utility.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final ProfileService profileService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;

    public String createToken(JwtRequest jwtRequest){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(),jwtRequest.getPassword()));
        UserDetails userDetails = profileService.loadUserByUsername(jwtRequest.getUsername());
        String token = jwtTokenUtils.generateToken(userDetails);
        /// TODO: Добавить регистрацию токена в базе
        return token;
    }
}
