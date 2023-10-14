package com.relex.relex_social.converter;

import com.relex.relex_social.entity.AllowedToSend;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class AllowedToSendConverter implements AttributeConverter<AllowedToSend, String> {
    @Override
    public String convertToDatabaseColumn(AllowedToSend allowedToSend) {
        return allowedToSend.getName();
    }

    @Override
    public AllowedToSend convertToEntityAttribute(String string) {
        return AllowedToSend.valueOf(string);
    }
}
