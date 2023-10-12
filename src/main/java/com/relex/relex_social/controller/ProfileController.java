package com.relex.relex_social.controller;

import com.relex.relex_social.dto.request.CreateProfileRequest;
import com.relex.relex_social.dto.request.EditProfileRequest;
import com.relex.relex_social.exception.EmailAlreadyExistsException;
import com.relex.relex_social.exception.NicknameAlreadyExistsException;
import com.relex.relex_social.exception.ResourceNotFoundException;
import com.relex.relex_social.service.AuthService;
import com.relex.relex_social.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;
    private final AuthService authService;

    @PostMapping
    public ResponseEntity registerProfile(@Valid @RequestBody CreateProfileRequest createProfileRequest) throws EmailAlreadyExistsException, NicknameAlreadyExistsException {
        Long createdId = profileService.register(createProfileRequest);
        return ResponseEntity.created(
                        ServletUriComponentsBuilder
                                .fromCurrentRequest()
                                .path("/{id}")
                                .buildAndExpand(createdId)
                                .toUri())
                .build();
    }

    @GetMapping
    public ResponseEntity<List<String>> getAllUsers(){
        return ResponseEntity.ok().body(profileService.getAllUsernamesAndRealNames());
    }

    @PutMapping
    public ResponseEntity editProfile(@RequestBody EditProfileRequest editProfileRequest) throws ResourceNotFoundException, EmailAlreadyExistsException, NicknameAlreadyExistsException {
        Long profileId = authService.getAuthId();
        return ResponseEntity.ok().body(profileService.edit(profileId ,editProfileRequest));
    }

    @PatchMapping
    public ResponseEntity changePassword(@RequestBody String newPassword) throws ResourceNotFoundException {
        Long profileId = authService.getAuthId();
        profileService.changePassword(profileId,newPassword);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity deleteProfile() throws ResourceNotFoundException {
        Long profileId = authService.getAuthId();
        profileService.delete(profileId);
        return ResponseEntity.noContent().build();
    }
}
