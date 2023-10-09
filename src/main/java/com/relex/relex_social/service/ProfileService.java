package com.relex.relex_social.service;

import com.relex.relex_social.dto.request.EditProfileRequest;
import com.relex.relex_social.entity.Profile;
import com.relex.relex_social.exception.EmailAlreadyExistsException;
import com.relex.relex_social.exception.NicknameAlreadyExistsException;
import com.relex.relex_social.exception.ResourceNotFoundException;
import com.relex.relex_social.repository.ProfileRepository;
import com.relex.relex_social.dto.request.CreateProfileRequest;
import com.relex.relex_social.utility.ProfileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class ProfileService{
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProfileUtils profileUtils;
    private final AuthService authService;

    @Transactional
    public Long register(CreateProfileRequest request) throws NicknameAlreadyExistsException, EmailAlreadyExistsException {
        Profile profile = profileUtils.toEntity(request);
        if(profileRepository.existsByNickname(profile.getNickname())){
            throw new NicknameAlreadyExistsException();
        }
        if (profileRepository.existsByEmail(profile.getEmail())){
            throw new EmailAlreadyExistsException();
        }

        //TODO: Добавить подтверждение по почте
        encodeProfilePassword(profile);
        return profileRepository.save(profile).getId();
    }

    public List<String> getAllUsernamesAndRealNames(){
        Iterable<Profile> profiles = profileRepository.findAll();
        return StreamSupport.stream(profiles.spliterator(),false)
                .map((profile) -> String.format("%s %s (%s)", profile.getFirstName(), profile.getSurname(), profile.getNickname()))
                .collect(Collectors.toList());
    }

    private void encodeProfilePassword(Profile profile) {
        profile.setPassword(passwordEncoder.encode(profile.getPassword()));
    }

    public void edit(Long id, EditProfileRequest editProfileRequest) throws ResourceNotFoundException, NicknameAlreadyExistsException, EmailAlreadyExistsException {
        Profile profileToEdit = profileRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format("User with id %d not found", id)));
        if (!profileToEdit.getNickname().equals(authService.getAuthUserNickname())){
            throw new AccessDeniedException(String.format("You have no access to account with id %d", id));
        }
        if(!profileToEdit.getNickname().equals(editProfileRequest.getNickname()) && profileRepository.existsByNickname(editProfileRequest.getNickname())){
            throw new NicknameAlreadyExistsException();
        }
        if (!profileToEdit.getEmail().equals(editProfileRequest.getEmail())){
            if (profileRepository.existsByEmail(editProfileRequest.getEmail())) {
                throw new EmailAlreadyExistsException();
            }
            //TODO: добавить подтверждение новой почты
        }
        profileToEdit.setNickname(editProfileRequest.getNickname());
        profileToEdit.setEmail(editProfileRequest.getEmail());
        profileToEdit.setFirstName(editProfileRequest.getFirstName());
        profileToEdit.setSurname(editProfileRequest.getSurname());
        profileToEdit.setBio(editProfileRequest.getBio());
        profileRepository.save(profileToEdit);
    }
}
