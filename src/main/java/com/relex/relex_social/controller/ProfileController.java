package com.relex.relex_social.controller;

import com.relex.relex_social.dto.request.ChangePasswordRequest;
import com.relex.relex_social.dto.request.CreateProfileRequest;
import com.relex.relex_social.dto.request.EditProfileRequest;
import com.relex.relex_social.service.interfaces.IAuthService;
import com.relex.relex_social.service.interfaces.IProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;

@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
public class ProfileController {
    private final IProfileService profileService;
    private final IAuthService authService;

    @PostMapping
    public ResponseEntity registerProfile(@Valid @RequestBody CreateProfileRequest createProfileRequest) {
        Long createdId = profileService.register(createProfileRequest);
        return ResponseEntity.created(
                        ServletUriComponentsBuilder
                                .fromCurrentRequestUri()
                                .path("/{id}")
                                .buildAndExpand(createdId)
                                .toUri())
                .build();
    }

    @GetMapping
    public ResponseEntity getAllUsers() {
        return ResponseEntity.ok().body(profileService.getAllUsers());
    }

    @PutMapping
    public ResponseEntity editProfile(@RequestBody @Valid EditProfileRequest editProfileRequest) {
        Long profileId = authService.getAuthId();
        return ResponseEntity.ok().body(profileService.edit(profileId, editProfileRequest));
    }

    @PatchMapping
    public ResponseEntity changePassword(@RequestBody @Valid ChangePasswordRequest changePasswordRequest) {
        Long profileId = authService.getAuthId();
        profileService.changePassword(profileId, changePasswordRequest.getNewPassword());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity deleteProfile() {
        Long profileId = authService.getAuthId();
        profileService.delete(profileId);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/restore/{nickname}")
    public ResponseEntity restoreProfile(@PathVariable String nickname) {
        profileService.restore(nickname);
        return ResponseEntity.ok().build();
    }
}
