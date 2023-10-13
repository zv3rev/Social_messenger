package com.relex.relex_social.service.implementation;

import com.relex.relex_social.dto.request.CreateProfileRequest;
import com.relex.relex_social.dto.request.EditProfileRequest;
import com.relex.relex_social.dto.response.ProfileDto;
import com.relex.relex_social.entity.Profile;
import com.relex.relex_social.entity.ProfileStatus;
import com.relex.relex_social.exception.EmailAlreadyExistsException;
import com.relex.relex_social.exception.NicknameAlreadyExistsException;
import com.relex.relex_social.exception.ResourceNotFoundException;
import com.relex.relex_social.repository.ProfileRepository;
import com.relex.relex_social.service.interfaces.IJwtTokenService;
import com.relex.relex_social.service.interfaces.IProfileService;
import com.relex.relex_social.utility.ProfileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class ProfileService implements IProfileService {
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProfileUtils profileUtils;
    private final IJwtTokenService jwtTokenService;

    @Transactional
    @Override
    public Long register(CreateProfileRequest request) {
        Profile profile = profileUtils.toEntity(request);
        if (profileRepository.existsByNickname(profile.getNickname())) {
            throw new NicknameAlreadyExistsException("This nickname is already occupied");
        }
        if (profileRepository.existsByEmail(profile.getEmail())) {
            throw new EmailAlreadyExistsException("This email is already occupied");
        }

        //TODO: Добавить подтверждение по почте
        encodeProfilePassword(profile);
        return profileRepository.save(profile).getId();
    }

    @Override
    public List<String> getAllUsernamesAndRealNames() {
        Iterable<Profile> profiles = profileRepository.findAll();
        return StreamSupport.stream(profiles.spliterator(), false)
                .map((profile) -> String.format("%s %s (%s)", profile.getFirstName(), profile.getSurname(), profile.getNickname()))
                .collect(Collectors.toList());
    }

    private void encodeProfilePassword(Profile profile) {
        profile.setPassword(passwordEncoder.encode(profile.getPassword()));
    }

    @Transactional
    @Override
    public ProfileDto edit(Long profileId, EditProfileRequest editProfileRequest) {
        Profile profile = profileRepository.findById(profileId).orElseThrow(() -> new ResourceNotFoundException(String.format("User with id %d not found", profileId)));

        if (!profile.getNickname().equals(editProfileRequest.getNickname()) && profileRepository.existsByNickname(editProfileRequest.getNickname())) {
            throw new NicknameAlreadyExistsException("This nickname is already occupied");
        }
        if (!profile.getEmail().equals(editProfileRequest.getEmail())) {
            if (profileRepository.existsByEmail(editProfileRequest.getEmail())) {
                throw new EmailAlreadyExistsException("This email is already occupied");
            }
            //TODO: добавить подтверждение новой почты
        }

        profile.setNickname(editProfileRequest.getNickname());
        profile.setEmail(editProfileRequest.getEmail());
        profile.setFirstName(editProfileRequest.getFirstName());
        profile.setSurname(editProfileRequest.getSurname());
        profile.setBio(editProfileRequest.getBio());
        return profileUtils.toDto(profileRepository.save(profile));
    }

    @Override
    public void changePassword(Long profileId, String newPassword) throws ResourceNotFoundException {
        Profile profileToChange = profileRepository.findById(profileId).orElseThrow(() -> new ResourceNotFoundException(String.format("User with id %d not found", profileId)));
        profileToChange.setPassword(newPassword);
        encodeProfilePassword(profileToChange);
        profileRepository.save(profileToChange);
    }

    @Transactional
    @Override
    public void delete(Long profileId) throws ResourceNotFoundException {
        //TODO: Отправить сообщение на почту "Ваш аккаунт был удален, для востановления перейдите по ссылке"
        Profile profileToDelete = profileRepository.findById(profileId).orElseThrow(() -> new ResourceNotFoundException(String.format("User with id %d not found", profileId)));
        profileToDelete.setProfileStatus(ProfileStatus.DELETED);
        profileRepository.save(profileToDelete);
        jwtTokenService.invalidToken(profileId);
    }
}
