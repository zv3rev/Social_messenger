package com.relex.relex_social.converter;

import com.relex.relex_social.entity.ProfileStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class ProfileStatusConverter implements AttributeConverter<ProfileStatus, String> {

    @Override
    public String convertToDatabaseColumn(ProfileStatus profileStatus) {
        return profileStatus.getName();
    }

    @Override
    public ProfileStatus convertToEntityAttribute(String string) {
        return ProfileStatus.valueOf(string);
    }
}
