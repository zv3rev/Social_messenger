package com.relex.relex_social.controller;

import com.relex.relex_social.dto.request.EditProfileRequest;
import com.relex.relex_social.exception.EmailAlreadyExistsException;
import com.relex.relex_social.exception.NicknameAlreadyExistsException;
import com.relex.relex_social.dto.request.CreateProfileRequest;
import com.relex.relex_social.exception.ResourceNotFoundException;
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

    @PutMapping("/{id}")
    public ResponseEntity editProfile(@PathVariable Long id, @RequestBody EditProfileRequest editProfileRequest) throws ResourceNotFoundException, EmailAlreadyExistsException, NicknameAlreadyExistsException {
        profileService.edit(id, editProfileRequest);
        return ResponseEntity.noContent().build();
    }
}
