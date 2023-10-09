package com.relex.relex_social.utility;

import com.relex.relex_social.entity.Profile;
import com.relex.relex_social.dto.request.CreateProfileRequest;
import org.springframework.stereotype.Component;

@Component
public class ProfileUtils {
    public Profile toEntity(CreateProfileRequest request){
        return Profile.builder()
                .nickname(request.getNickname())
                .password(request.getPassword())
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .surname(request.getSurname())
                .bio(request.getBio())
                .build();
    }
}
