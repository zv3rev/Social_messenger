package com.relex.relex_social.converter;

import com.relex.relex_social.entity.JwtType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class JwtTypeConverter implements AttributeConverter<JwtType, String> {
    @Override
    public String convertToDatabaseColumn(JwtType jwtType) {
        return jwtType.getName();
    }

    @Override
    public JwtType convertToEntityAttribute(String s) {
        return JwtType.valueOf(s);
    }
}
