package com.relex.relex_social.utility;

import com.relex.relex_social.dto.response.ProfileDto;
import com.relex.relex_social.entity.AllowedToSend;
import com.relex.relex_social.entity.Profile;
import com.relex.relex_social.dto.request.CreateProfileRequest;
import com.relex.relex_social.entity.ProfileStatus;
import org.springframework.stereotype.Component;

@Component
public class ProfileUtils {
    public Profile toEntity(CreateProfileRequest request) {
        return Profile.builder()
                .nickname(request.getNickname())
                .password(request.getPassword())
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .surname(request.getSurname())
                .bio(request.getBio())
                .profileStatus(ProfileStatus.UNCONFIRMED)
                .allowedToSend(AllowedToSend.ALL)
                .isFriendsListVisible(true)
                .build();
    }

    public ProfileDto toDto(Profile profile) {
        return new ProfileDto(
                profile.getId(),
                profile.getEmail(),
                profile.getNickname(),
                profile.getFirstName(),
                profile.getSurname(),
                profile.getBio(),
                profile.getProfileStatus(),
                profile.getAllowedToSend(),
                profile.getIsFriendsListVisible());
    }
}
