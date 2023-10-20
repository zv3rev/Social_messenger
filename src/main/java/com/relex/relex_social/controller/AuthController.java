package com.relex.relex_social.controller;

import com.relex.relex_social.dto.request.JwtRequest;
import com.relex.relex_social.service.interfaces.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final IAuthService authService;

    @PostMapping("/login")
    public ResponseEntity createTokens(@RequestBody @Valid JwtRequest jwtRequest) {
        return ResponseEntity.ok(authService.createAccessToken(jwtRequest));
    }

    @DeleteMapping("/logout")
    public ResponseEntity deleteToken() {
        Long profileId = authService.getAuthId();
        authService.deleteToken(profileId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity refreshToken(@RequestBody String refreshToken) {
        return ResponseEntity.ok().body(authService.refreshTokens(refreshToken));
    }
}
