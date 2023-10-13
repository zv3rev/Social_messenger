package com.relex.relex_social.service.interfaces;

import com.relex.relex_social.dto.request.CreateProfileRequest;
import com.relex.relex_social.dto.request.EditProfileRequest;
import com.relex.relex_social.dto.response.ProfileDto;

import java.util.List;

public interface IProfileService {
    Long register(CreateProfileRequest request);

    List<String> getAllUsernamesAndRealNames();

    ProfileDto edit(Long profileId, EditProfileRequest editProfileRequest);

    void changePassword(Long profileId, String newPassword);

    void delete(Long profileId);
}
