package com.relex.relex_social.service.interfaces;

import com.relex.relex_social.dto.request.CreateProfileRequest;
import com.relex.relex_social.dto.request.EditProfileRequest;
import com.relex.relex_social.dto.response.ProfileDto;
import com.relex.relex_social.exception.EmailAlreadyExistsException;
import com.relex.relex_social.exception.NicknameAlreadyExistsException;
import com.relex.relex_social.exception.ResourceNotFoundException;

import java.util.List;

public interface IProfileService {
    Long register(CreateProfileRequest request) throws NicknameAlreadyExistsException, EmailAlreadyExistsException;

    List<String> getAllUsernamesAndRealNames();

    ProfileDto edit(Long profileId, EditProfileRequest editProfileRequest) throws ResourceNotFoundException, NicknameAlreadyExistsException, EmailAlreadyExistsException;

    void changePassword(Long profileId, String newPassword) throws ResourceNotFoundException;

    void delete(Long profileId) throws ResourceNotFoundException;
}
