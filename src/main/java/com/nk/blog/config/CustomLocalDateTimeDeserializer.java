package com.nk.blog.config;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.nk.blog.exception.BadRequestException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
    private final DateTimeFormatter formatter;

    @Override
    public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        try {
            String dateString = parser.getText();
            return LocalDateTime.parse(dateString, formatter);
        } catch (Exception ex) {
            throw new BadRequestException("Invalid input value for date. Expected a string field with format yyyy-MM-dd HH:mm:ss");
        }
    }
}
