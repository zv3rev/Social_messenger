package com.relex.relex_social.controller;

import com.relex.relex_social.dto.request.JwtRequest;
import com.relex.relex_social.dto.response.JwtResponse;
import com.relex.relex_social.service.interfaces.IAuthService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final IAuthService authService;

    @PostMapping("/login")
    public ResponseEntity createToken(@RequestBody JwtRequest jwtRequest) {
        return ResponseEntity.ok(new JwtResponse(authService.createToken(jwtRequest), ""));
    }

    @DeleteMapping("/logout")
    public ResponseEntity deleteToken() {
        Long profileId = authService.getAuthId();
        authService.deleteToken(profileId);
        return ResponseEntity.noContent().build();
    }
}
