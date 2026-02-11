package com.palladium46.odonatesdota.lobbysauvage.model;


import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class LobbyStatusConverter implements AttributeConverter<LobbyStatus, String> {

    @Override
    public String convertToDatabaseColumn(LobbyStatus status) {
        if (status == null) {
            return null;
        }
        return status.toString();
    }

    @Override
    public LobbyStatus convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return LobbyStatus.valueOf(dbData);
    }
}

